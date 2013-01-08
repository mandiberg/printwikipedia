/*
 */

package wikitest;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import wikitopdf.wiki.WikiPage;

/**
 *
 * @author Denis Lunev <den.lunev@gmail.com>
 */
public class Charset {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SQLException, UnsupportedEncodingException {

        Connection con;

        String dbUser = "root";
        String dbPass = "cli33nt";
        String dbHost = "127.0.0.1";
        int dbPort = 3306;
        String dbName = "wiki";

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (java.lang.ClassNotFoundException e) {
            System.err.print("ClassNotFoundException: ");

            System.err.println(e.getMessage());
        }

        String jdbcUrl = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName; //+ "?useUnicode=true&characterEncoding=utf8";
        con = DriverManager.getConnection(jdbcUrl, dbUser, dbPass);
    
        Statement stmt;
        
        String query = "SELECT old_text FROM text WHERE old_id = 267194594";

        try {

            stmt = con.createStatement();
            //stmt.executeQuery("SET NAMES utf8");
            //stmt.executeQuery("SET CHARACTER SET utf8");

            ResultSet rs = stmt.executeQuery(query);
            
            while (rs.next()) {
                //new String(rs.getString("company").getBytes("utf8"), "UTF-8")
                String txt = new String(rs.getString(1).getBytes("utf8"), "utf8");
                //String txt = rs.getString(1);

                System.out.println(txt);
                System.out.println("");
                System.out.println("[[zh:艾哈迈德二世]]");
            }


            stmt.close();
            con.close();
        } catch (SQLException ex) {
            System.err.print("SQLException: ");
            System.err.println(ex.getMessage());
        }
    }
}
