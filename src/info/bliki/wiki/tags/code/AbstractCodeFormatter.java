package info.bliki.wiki.tags.code;

import java.util.HashMap;

/**
 * Abstract base class for alle SourceCodeFormatters
 *
 */
abstract public class AbstractCodeFormatter implements SourceCodeFormatter {
    public final static String FONT_KEYWORD = "<span style=\"color:#7F0055; font-weight: bold; \">";
    // "<font color=\"#7F0055\">";

    public final static String FONT_OBJECTS = "<span style=\"color:#7F0055; \">";
    // "<font color=\"#7F0055\">";

    public final static String FONT_COMMENT = "<span style=\"color:#3F7F5F; \">";
    // = "<font color=\"#3F7F5F\">";

    public final static String FONT_STRINGS = "<span style=\"color:#2A00FF; \">";
    // = "<font color=\"#2A00FF\">";

    public final static String FONT_JAVADOC = "<span style=\"color:#3F5FBF; \">";
    // = "<font color=\"#3F5FBF\">";

    public final static String FONT_END = "</span>";

    public static void appendChar(StringBuilder result, char currentChar) {
        switch (currentChar) {
        case '\"': // special html escape character
            result.append("&#34;");
            break;
        case '<': // special html escape character
            result.append("&#60;");
            break;
        case '>': // special html escape character
            result.append("&#62;");
            break;
        case '&': // special html escape character
            result.append("&#38;");
            break;
        case '\'': // special html escape character
            result.append("&#39;");
            break;
        default:
            result.append(currentChar);
        }
    }

    public static void createHashMap(HashMap<String, String> map, String str) {
        map.put(str, FONT_KEYWORD + str + FONT_END);
    }

    public static void createObjectsMap(HashMap<String, String> map, String str) {
        map.put(str, FONT_OBJECTS + str + FONT_END);
    }

    /**
     * @return Returns the KEYWORD_SET.
     */
    abstract public HashMap<String, String> getKeywordSet();

    /**
     * @return Returns the OBJECT_SET.
     */
    abstract public HashMap<String, String> getObjectSet();

}
