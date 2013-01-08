/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wikitopdf.utils;

import wikitopdf.utils.WikiLogger;
import java.io.File;
import java.util.List;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import wikitopdf.pdf.WikiFont;

/**
 *
 * @author Denis Lunev <den.lunev@gmail.com>
 */
public class WikiSettings {

    /**
     *
     * @return
     */
    static public WikiSettings getInstance() {
        if (_instance == null) {
            _instance = new WikiSettings();
        }

        return _instance;
    }

    private WikiSettings() {
        _parseSettingsFile();
    }

    private void _parseSettingsFile() {
        List<Element> fileNodes = null;
        try {
            SAXBuilder builder = new SAXBuilder();
            _docXml = builder.build(new File(_configFileName));
            Element settings = _docXml.getRootElement();
            _outputFileName = settings.getChild("output-file-name").getTextTrim();
            _outputFolder = settings.getChild("output-folder").getTextTrim();
            _imgFolder = settings.getChild("img-folder").getTextTrim();
            _dbHost = settings.getChild("db-host").getTextTrim();
            _dbPort = Integer.valueOf(settings.getChild("db-port").getTextTrim());
            _dbName = settings.getChild("db-name").getTextTrim();
            _dbUser = settings.getChild("db-user").getTextTrim();
            _dbPass = settings.getChild("db-pass").getTextTrim();
            _articleBunch = Integer.valueOf(settings.getChild("article-bunch").getTextTrim());
            _pageLimit = Integer.valueOf(settings.getChild("page-file-limit").getTextTrim());
            _startPage = Integer.valueOf(settings.getChild("start-page").getTextTrim());
            _threadLimit = Integer.valueOf(settings.getChild("thread-limit").getTextTrim());
            logFolder = settings.getChild("log-folder").getTextTrim();

            parseFonts();

        } catch (Exception ex) {
            //Load default settings
            WikiLogger.getLogger().severe(ex.getMessage() + " (Load default settings)");
        }
    }

    private void parseFonts() {
        Element root = _docXml.getRootElement();
        String fonts[] = {"title", "text", "comment", "contributor"};

        for (String fontStr : fonts) {
            //If node doesnt exists, retrieve default value
            Element fontXml = root.getChild(fontStr);
            float fontSize = Float.valueOf(fontXml.getChild("font-size").getTextTrim());
            String fontFamily = fontXml.getChild("font-family").getTextTrim();


            int fontColorsInt[] = {0, 0, 0};
            String[] fontColors = fontXml.getChild("font-color").getTextTrim().split(",");
            for (int i = 0; i < fontColors.length; i++) {
                fontColorsInt[i] = Integer.valueOf(fontColors[i]);
            }

            WikiFont font = new WikiFont();
            font.setFontColor(fontColorsInt);
            font.setFontFamily(fontFamily);
            font.setFontSize(fontSize);

            if ("title" == null ? fontStr == null : "title".equals(fontStr)) {

                titleFont = font;
            } else if ("text" == null ? fontStr == null : "text".equals(fontStr)) {
                textFont = font;
            } else if ("comment" == null ? fontStr == null : "comment".equals(fontStr)) {
                commentFont = font;
            } else if ("contributor" == null ? fontStr == null : "contributor".equals(fontStr)) {
                contributorFont = font;
            }
        }
    }

    /**
     *
     * @return
     */
    public int getArticleBunch() {
        return _articleBunch;
    }

    /**
     *
     * @return
     */
    public int getPageLimit() {
        return _pageLimit;
    }
    
    /**
     *
     * @return
     */
    public String getOutputFileName() {
        return _outputFileName;
    }

    /**
     *
     * @return
     */
    public String getOutputFolder() {
        return _outputFolder;
    }
    
    /**
     *
     * @return
     */
    public int getDbPort() {
        return _dbPort;
    }

    /**
     *
     * @return
     */
    public String getDbHost() {
        return _dbHost;
    }

    /**
     *
     * @return
     */
    public String getDbName() {
        return _dbName;
    }

    /**
     *
     * @return
     */
    public String getDbPass() {
        return _dbPass;
    }

    /**
     *
     * @return
     */
    public String getDbUser() {
        return _dbUser;
    }

    /**
     *
     * @return
     */
    public WikiFont getCommentFont() {
        return commentFont;
    }

    /**
     *
     * @return
     */
    public WikiFont getContributorFont() {
        return contributorFont;
    }

    /**
     *
     * @return
     */
    public WikiFont getTextFont() {
        return textFont;
    }

    /**
     *
     * @return
     */
    public WikiFont getTitleFont() {
        return titleFont;
    }

    /**
     *
     * @return
     */
    public int getStartPage() {
        return _startPage;
    }

    /**
     *
     * @return
     */
    public String getImgFolder() {
        return _imgFolder;
    }

    public int getThreadLimit() {
        return _threadLimit;
    }

    public String getLogFolder() {
        return logFolder;
    }

    
    
    static private WikiSettings _instance = null;
    private Document _docXml = null;
    private String _configFileName = "settings.xml";
    private String _outputFileName = "output3.pdf";
    private String _outputFolder = "./output";
    private String _imgFolder = "./output/img";
    private int _dbPort = 3306;
    private String _dbHost = "localhost";
    private String _dbName = "";
    private String _dbUser = "";
    private String _dbPass = "";
    private int _articleBunch = 10;
    private int _pageLimit = 0;
    private int _startPage = 0;
    private int _threadLimit = 0;
    private String logFolder = "";
    private WikiFont titleFont;
    private WikiFont textFont;
    private WikiFont commentFont;
    private WikiFont contributorFont;
}
