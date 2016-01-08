package info.bliki.wiki.tags.code;

import java.util.HashMap;

public class SQLCodeFilter extends AbstractCPPBasedCodeFilter implements SourceCodeFormatter {
    private static final String[] KEYWORDS = { "alter", "and", "blob", "boolean", "character", "clob", "column", "comment",
            "constraint", "create", "default", "delete", "drop", "false", "from", "in", "insert", "integer", "is", "key", "lob", "not", "null",
            "on", "or", "procedure", "references", "select", "set", "table", "timestamp", "true", "update", "varchar", "where", };

    private static HashMap<String, String> KEYWORD_SET = new HashMap<>();

    static {
        for (String k : KEYWORDS) {
            createHashMap(KEYWORD_SET, k);
        }
    }

    public SQLCodeFilter() {
        // empty
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
        return null;
    }

    /**
     * Do the work of filtering one chunk of code in a &lt;source&gt; type element
     */
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
                if ((currentChar >= 'A' && currentChar <= 'Z') || (currentChar == '_') || (currentChar >= 'a' && currentChar <= 'z')) {
                    identStart = currentPosition - 1;
                    identFound = true;
                    // start of identifier ?
                    while ((currentChar >= 'a' && currentChar <= 'z') || (currentChar >= 'A' && currentChar <= 'Z') || currentChar == '_') {
                        currentChar = source[currentPosition++];
                    }
                    currentPosition = appendIdentifier(input, identStart, currentPosition, keywordsSet, objectsSet, result);
                    identFound = false;
                    continue; // while loop
                } else if (currentChar == '\"') { // strings
                    result.append(FONT_STRINGS);
                    appendChar(result, currentChar);
                    while (currentPosition < input.length()) {
                        currentChar = source[currentPosition++];
                        appendChar(result, currentChar);
                        if (currentChar == '\\') {
                            currentChar = source[currentPosition++];
                            appendChar(result, currentChar);
                            continue;
                        }
                        if (currentChar == '\"') {
                            break;
                        }
                    }
                    result.append(FONT_END);
                    continue;
                } else if (currentChar == '\'') { // characters
                    result.append(FONT_STRINGS);
                    appendChar(result, currentChar);
                    while (currentPosition < input.length()) {
                        currentChar = source[currentPosition++];
                        appendChar(result, currentChar);
                        if (currentChar == '\\') {
                            currentChar = source[currentPosition++];
                            appendChar(result, currentChar);
                            continue;
                        }
                        if (currentChar == '\'') {
                            break;
                        }
                    }
                    result.append(FONT_END);
                    continue;
                } else if (currentChar == '\n' && currentPosition < input.length() - 1 && source[currentPosition] == '-'
                        && source[currentPosition + 1] == '-') {
                    // line comment
                    result.append(FONT_COMMENT);
                    appendChar(result, currentChar);
                    appendChar(result, source[currentPosition++]);
                    while (currentPosition < input.length()) {
                        currentChar = source[currentPosition++];
                        appendChar(result, currentChar);
                        if (currentChar == '\n') {
                            break;
                        }
                    }
                    result.append(FONT_END);
                    continue;
                }
                appendChar(result, currentChar);

            }
        } catch (IndexOutOfBoundsException e) {
            if (identFound) {
                currentPosition = appendIdentifier(input, identStart, currentPosition, keywordsSet, null, result);
            }
        }
        // result.append(FONT_END);
        return result.toString();
    }

    @Override
    public boolean isKeywordCaseSensitive() {
        return false;
    }
}
