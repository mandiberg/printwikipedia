package wikitopdf;

import wikitopdf.utils.WikiSettings;
import wikitopdf.utils.WikiLogger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import wikitopdf.wiki.WikiPage;

/**
 *
 * @author Denis Lunev <den.lunev@gmail.com>
 */
public class SQLProcessor {

    /**
     *
     * @throws SQLException
     */
    public SQLProcessor() throws SQLException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("try makin classname^");
            
        } catch (java.lang.ClassNotFoundException e) {
            System.err.print("ClassNotFoundException: ");
            System.err.println(e.getMessage());
        }

        String jdbcUrl = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName + "?useUnicode=true&characterEncoding=utf8";
        System.out.println(dbName);
        System.out.println(dbPass);
        con = DriverManager.getConnection(jdbcUrl, dbUser, dbPass);
        System.out.println(con);
        stmt = con.createStatement();
    }

    /**
     *
     * @param title
     * @throws SQLException
     */
    //this is never used
    public void saveTitle(String title) throws SQLException {
        PreparedStatement pstmt = con.prepareStatement("INSERT INTO titles (title)  VALUES (?)");
        pstmt.setString(1, title);
        pstmt.executeUpdate();

        pstmt.close();
    }

    /**
     *
     */
    public void close() {
        try {
            stmt.close();
            con.close();
        } catch (Exception ex) {
            WikiLogger.getLogger().severe(ex.getMessage());
        }
    }

    /**
     *
     * @param start
     * @param limit
     * @param textID
     * @return
     */
    public synchronized ArrayList<WikiPage> getBunch(int start, int limit, int textID) {
        //start is assigned the value of the startLimit variable from Processor.java
        ArrayList<WikiPage> pages = new ArrayList<WikiPage>();
        // old query from before we consolidated wikipedia entries into newmaster table
        /**String query = "SELECT " +
                "pagesubset.page_id, " +
                "pagesubset.page_title, " +
                "revision.rev_user_text, " +
                "revision.rev_comment, " +
                "text.old_text " +
                "FROM (SELECT page.page_id as page_id, page.page_title as page_title FROM `page` " +
                "ORDER BY page.page_title LIMIT " + start + ", " + limit + ") as pagesubset " + 
                "INNER JOIN revision ON (pagesubset.page_id = revision.rev_page) " +
                "INNER JOIN text ON (revision.rev_id = text.old_id)";
        */
       /** String query = "SELECT " +
                "page_id, " +
                "page_title, " +
                "rev_user_text, " +
                "rev_comment, " +
                "old_text " +
                "FROM `newmaster` " +
                "ORDER BY pkey LIMIT " + start + ", " + limit + ""; 
        */
        //old query that does not take into account the count of articles outside of bunch. 
        String query = "SELECT  " + 
                "page_id, page_title, rev_user_text, rev_comment, old_text FROM (Select * from newmaster WHERE pkey > " + start + " and page_title is not null limit " + limit + ") as x;"; 
        //updated query to keep track of count of articles remaining (if<limit...)
//        String query = "SELECT * FROM " +
//                        "((SELECT  page_id, page_title, rev_user_text, rev_comment, old_text" +
//                            "FROM (Select * FROM printwiki.newmaster WHERE pkey > " + start + " limit " + limit + ") as x) as main, " +
//                //main is the main query of this two subquery statement
//                            "(SELECT COUNT(page_id) as count FROM (SELECT page_id FROM printwiki.newmaster WHERE pkey > " + start + " limit " + limit + ")"+
//                            " as y) as counter);";//counter is just to tell how many are left in the db;

        //uncomment this countQuery and try catch function here when you get within 5000 articles of the end of wiki
//        String countQuery = "SELECT count(pkey) as count from printwiki.newmaster where pkey > " + start + " limit " + limit + ";";
//        try{
//            ResultSet crs = stmt.executeQuery(countQuery);
//        
//            crs.last();
//            int leftOver  = crs.getInt("count");
//            if(leftOver < limit){
//                lastBunch = true;
//            }
//
//        }
//        catch (SQLException ex) {
//            WikiLogger.getLogger().severe(ex.getMessage());
//        }
//        String query = "SELECT  page_id, page_title, rev_user_text, rev_comment, old_text, (select count(page_id) from printwiki.newmaster where pkey >" + start + ") as count FROM (Select * from printwiki.newmaster WHERE pkey > " + start + " limit " + limit + ") as  x;";
      
        try {

            //stmt.executeQuery("SET NAMES UTF8");
            //stmt.executeQuery("SET CHARACTER SET 'UTF8'");

            ResultSet rs = stmt.executeQuery(query);
            //if count of number of results is less than getbunch limit rs.count
            
            //then assign a true value to a new variable "lastBunch"
            isInProggres = false;
            while (rs.next()) {
                WikiPage page = new WikiPage();

                page.setId(rs.getInt(DB_PAGE_ID));
                page.setTitle(rs.getString(DB_PAGE_TITLE));
                page.getRevision().setComment(rs.getString(DB_COMMENT));

                //if text contains #REDIRECT[article] derictive, change it to 'See: article'
                page.getRevision().setText(
                        this.changeText(rs.getString(DB_TEXT)));
                page.getRevision().getContributor().setUserName(rs.getString(DB_USERNAME));

                pages.add(page);
                isInProggres = true;
            }
            //if lastBunch = true then isInProggres = false;
            if(lastBunch == true){
                isInProggres = false;
            }
            //stmt.close();
            //con.close();
        } catch (SQLException ex) {
            WikiLogger.getLogger().severe(ex.getMessage());
        }

        return pages;
    }

    /**
     *
     * @return
     */
    public int getArticlesCount() {// pretty sure all you need to do here is just pass the int. no need for query.
//        String query = "SELECT COUNT(page.page_id) as count FROM page"; 
        int pCount = 13450538;
//        try {  // pulling total number of pages from mysql db
//            ResultSet rs = stmt.executeQuery(query);
//            rs.next();
//            pCount = rs.getInt(1);
//        } //stmt.close();
//        //con.close();
//        catch (SQLException ex) {
//            WikiLogger.getLogger().severe(ex.getMessage());
//        }

        return pCount;  // returns total number of pages in db
    }

    /**
     *
     * @return
     */
    public synchronized boolean isInProggres() {
        return isInProggres;
    }

    private String changeText(String wikiText) {
//        String strPatternDE = "#WEITERLEITUNG\\s*\\[\\[(.*)\\]\\]";
//        String strPatternENG = "#redirect\\s*\\[\\[(.*)\\]\\]";
//        Pattern pde = Pattern.compile(strPatternDE, Pattern.CASE_INSENSITIVE);
//        Matcher mde = pde.matcher(wikiText);
//        Pattern peng = Pattern.compile(strPatternENG, Pattern.CASE_INSENSITIVE);
//        Matcher meng = peng.matcher(wikiText);
//
//        if (mde.find()) {
//            wikiText = "Siehe: " + mde.group(1);
//        }
//        else if(meng.find()){
//            wikiText = "Siehe: " + meng.group(1);
//        }

        return wikiText;
    }
    
    private static int DB_PAGE_ID = 1;
    private static int DB_PAGE_TITLE = 2;
    private static int DB_USERNAME = 3;
    private static int DB_COMMENT = 4;
    private static int DB_TEXT = 5;
    private Connection con;
    private Statement stmt;
    private boolean isInProggres = true;
    private boolean lastBunch = false;
    private String dbUser = WikiSettings.getInstance().getDbUser();
    private String dbPass = WikiSettings.getInstance().getDbPass();
    private String dbHost = WikiSettings.getInstance().getDbHost();
    private int dbPort = WikiSettings.getInstance().getDbPort();
    private String dbName = WikiSettings.getInstance().getDbName();
}
