package info.bliki.wiki.tags.code;

import java.util.HashMap;

/**
 * Syntax highlighting support for ABAP source codes
 *
 */
public class ABAPCodeFilter extends AbstractCodeFormatter {

    private static HashMap<String, String> KEYWORD_SET = new HashMap<>();

    private static final String[] KEYWORDS = { "ADD", "ALL", "AND", "ANY", "ADD-CORRESPONDING", "APPEND", "APPENDING", "ASSIGN",
            "ASSIGNED", "ASSIGNING", "AT", "AUTHORITY-CHECK", "BACK", "BEGIN", "BETWEEN", "BINARY", "BREAK-POINT", "BY", "CALL", "CASE",
            "CHANGING", "CHECK", "CLEAR", "CLOSE", "CA", "CN", "CO", "CP", "CS", "CNT", "COLLECT", "COMMIT", "COMMUNICATION", "COMPUTE",
            "CONCATENATE", "CONDENSE", "CONSTANTS", "CONTINUE", "CONTROLS", "CONVERT", "CORRESPONDING", "COUNT", "CUSTOMER-FUNCTION",
            "CREATE", "CURRENCY", "CURSOR", "DATA", "DEFINE", "DELETE", "DESCRIBE", "DETAIL", "DIVIDE", "DIVIDE-CORRESPONDING", "DO",
            "EDIT", "EDITOR-CALL", "ELSE", "ELSEIF", "END", "END-OF-DEFINITION", "END-OF-PAGE", "END-OF-SELECTION", "ENDAT", "ENDCASE",
            "ENDDO", "ENDEXEC", "ENDFORM", "ENDFUNCTION", "ENDIF", "ENDIFEND", "ENDLOOP", "ENDMODULE", "ENDON", "ENDPROVIDE",
            "ENDSELECT", "ENDWHILE", "ENTRIES", "EQ", "EXCEPTIONS", "EXEC", "EXIT", "EXIT FROM STEP LOOP", "EXPORT", "EXPORTING",
            "EXTRACT", "FETCH", "FIELD", "FIELD-GROUPS", "FIELD-SYMBOLS", "FIELDS", "FOR", "FORM", "FORMAT", "FREE", "FROM", "FUNCTION",
            "FUNCTION-POOL", "GENERATE", "GET", "GE", "GT", "HIDE", "ID", "INITIAL", "IF", "IS", "IMPORT", "IMPORTING", "INCLUDE",
            "INFOTYPES", "INPUT", "IN", "INITIALIZATION", "INPUT", "INSERT", "INTO", "KEY", "LE", "LEAVE", "LIKE", "LINE", "LOAD",
            "LOCAL", "LOOP", "LT", "M", "MASK", "MEMORY", "METHOD", "MESSAGE", "MODIFY", "MODULE", "MOVE", "MOVE-CORRESPONDING",
            "MULTIPLY", "MULTIPLY-CORRESPONDING", "NA", "NP", "NS", "NE", "NEW-LINE", "NEW-PAGE", "NEW-SECTION", "NO", "NOT", "O", "OF",
            "ON", "OR", "OUTPUT", "OVERLAY", "PACK", "PARAMETER", "PARAMETERS", "PERFORM", "POSITION", "PRINT-CONTROL", "PROGRAM",
            "PROVIDE", "PUT", "RAISE", "RANGES", "READ", "RECEIVE", "REF", "REFRESH", "REJECT", "REPLACE", "REPORT", "REQUESTED",
            "RESERVE", "RESTORE", "ROLLBACK", "ROWS", "SCAN", "SCREEN", "SCROLL", "SEARCH", "SELECT", "SELECT-OPTIONS",
            "SELECTION-SCREEN", "SEPARATED", "SET", "SHIFT", "SINGLE", "SKIP", "SORT", "SPACE", "SPLIT", "START-OF-SELECTION", "STATICS",
            "STOP", "STRUCTURE", "SUBMIT", "SUBTRACT", "SUBTRACT-CORRESPONDING", "SUM", "SUMMARY", "SUPPRESS", "SYNTAX-CHECK",
            "SYNTAX-TRACE", "TABLE", "TABLES", "TIMES", "TO", "TOP-OF-PAGE", "TRANSACTION", "TRANSFER", "TRANSLATE", "TRANSPORTING",
            "TYPE", "TYPE-POOL", "TYPE-POOLS", "TYPES", "ULINE", "USING", "UNPACK", "UP", "UPDATE", "VALUE", "WHEN", "WHILE", "WINDOW",
            "WAIT", "WHERE", "WITH", "WORK", "WRITE", "Z" };

    private static HashMap<String, String> OBJECT_SET = new HashMap<>();

    static {
        for (int i = 0; i < KEYWORDS.length; i++) {
            createHashMap(KEYWORD_SET, KEYWORDS[i]);
        }
    }

    public ABAPCodeFilter() {
    }

    /**
     * @return Returns the KEYWORD_SET.
     */
    @Override
    public HashMap<String, String> getKeywordSet() {
        return KEYWORD_SET;
    }

    /**
     * @return Returns the OBJECT_SET.
     */
    @Override
    public HashMap<String, String> getObjectSet() {
        return OBJECT_SET;
    }

    //
    // public static void appendChar(StringBuilder result, char currentChar) {
    // switch (currentChar) {
    // case '\"': // special html escape character
    // result.append("&#34;");
    // break;
    // case '<': // special html escape character
    // result.append("&#60;");
    // break;
    // case '>': // special html escape character
    // result.append("&#62;");
    // break;
    // case '&': // special html escape character
    // result.append("&#38;");
    // break;
    // case '\'': // special html escape character
    // result.append("&#39;");
    // break;
    // default:
    // result.append(currentChar);
    // }
    // }
    //
    // public static void createHashMap(HashMap<String, String> map, String str) {
    // map.put(str, AbstractCPPBasedCodeFilter.FONT_KEYWORD + str +
    // AbstractCPPBasedCodeFilter.FONT_END);
    // }

    private int appendIdentifier(String input, int identStart, int currentPosition, HashMap<String, String> keywords,
            HashMap<String, String> objectWords, StringBuilder result) {
        String originalIdent = input.substring(identStart, --currentPosition);
        String keywordIdent = originalIdent;
        if (!isKeywordLowerCase()) {
            keywordIdent = keywordIdent.toLowerCase();
        }
        String keywordValue = (String) keywords.get(keywordIdent);
        if (keywordValue != null) {
            result.append(keywordValue);
            // } else if (objectWords != null && objectWords.contains(originalIdent))
            // {
            // result.append("<font color=\"#7F9FBF\">");
            // result.append(originalIdent);
            // result.append("</font>");
        } else {
            result.append(originalIdent);
        }
        return currentPosition;
    }

    @Override
    public String filter(String input) {
        char[] source = input.toCharArray();
        int currentPosition = 0;
        int identStart = 0;
        char currentChar = ' ';

        HashMap<String, String> keywordsSet = getKeywordSet();
        HashMap<String, String> objectsSet = getObjectSet();
        StringBuilder result = new StringBuilder(input.length() + input.length() / 4);
        boolean identFound = false;
        // result.append("<font color=\"#000000\">");
        try {
            while (true) {
                currentChar = source[currentPosition++];
                if ((currentChar >= 'A' && currentChar <= 'Z') || (currentChar >= 'a' && currentChar <= 'z')) {
                    identStart = currentPosition - 1;
                    identFound = true;
                    // start of identifier ?
                    while ((currentChar >= 'a' && currentChar <= 'z') || (currentChar >= 'A' && currentChar <= 'Z') || currentChar == '-'
                            || currentChar == '_') {
                        currentChar = source[currentPosition++];
                    }
                    currentPosition = appendIdentifier(input, identStart, currentPosition, keywordsSet, objectsSet, result);
                    identFound = false;
                    continue; // while loop
                } else if (currentChar == '\'') { // strings
                    result.append(AbstractCPPBasedCodeFilter.FONT_STRINGS);
                    appendChar(result, currentChar);
                    while (currentPosition < input.length()) {
                        currentChar = source[currentPosition++];
                        appendChar(result, currentChar);
                        if (currentChar == '\'') {
                            if (currentPosition < input.length() && source[currentPosition] != '\'') {
                                break;
                            } else {
                                appendChar(result, currentChar);
                                currentPosition++;
                            }
                        }
                        if (currentChar == '\n') {
                            break;
                        }
                    }
                    result.append(AbstractCPPBasedCodeFilter.FONT_END);
                    continue;
                } else if (currentChar == '\n' && currentPosition < input.length() && source[currentPosition] == '*') {
                    // line comment
                    result.append(AbstractCPPBasedCodeFilter.FONT_COMMENT);
                    appendChar(result, currentChar);
                    appendChar(result, source[currentPosition++]);
                    while (currentPosition < input.length()) {
                        currentChar = source[currentPosition++];
                        appendChar(result, currentChar);
                        if (currentChar == '\n') {
                            currentPosition--;
                            break;
                        }
                    }
                    result.append(AbstractCPPBasedCodeFilter.FONT_END);
                    continue;
                } else if (currentChar == '"' && currentPosition < input.length()) {
                    // line comment until \n
                    result.append(AbstractCPPBasedCodeFilter.FONT_COMMENT);
                    appendChar(result, currentChar);
                    appendChar(result, source[currentPosition++]);
                    while (currentPosition < input.length()) {
                        currentChar = source[currentPosition++];
                        appendChar(result, currentChar);
                        if (currentChar == '\n') {
                            break;
                        }
                    }
                    result.append(AbstractCPPBasedCodeFilter.FONT_END);
                    continue;
                }
                appendChar(result, currentChar);

            }
        } catch (IndexOutOfBoundsException e) {
            if (identFound) {
                currentPosition = appendIdentifier(input, identStart, currentPosition, keywordsSet, null, result);
            }
        }
        // result.append("</font>");
        return result.toString();
    }

    /**
     * @return Returns the KEYWORD_MAP.
     */
    public boolean isKeywordLowerCase() {
        return true;
    }

    /**
   *
   */
    public boolean isPHPTag() {
        return false;
    }

}
