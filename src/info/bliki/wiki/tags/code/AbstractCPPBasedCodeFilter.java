package info.bliki.wiki.tags.code;

import java.util.HashMap;

/**
 * Syntax highlighting support for C++ based source codes
 *
 */
abstract public class AbstractCPPBasedCodeFilter extends AbstractCodeFormatter {

    public AbstractCPPBasedCodeFilter() {
    }

    protected int appendIdentifier(String input, int identifierStart, int currentPosition, HashMap<String, String> keyWords,
            HashMap<String, String> objectWords, StringBuilder result) {
        String originalIdent = input.substring(identifierStart, --currentPosition);
        String keywordIdent = originalIdent;
        if (!isKeywordCaseSensitive()) {
            keywordIdent = keywordIdent.toLowerCase();
        }
        String keywordValue = keyWords.get(keywordIdent);
        if (keywordValue != null) {
            result.append(keywordValue);
        } else {
            if (objectWords == null) {
                result.append(originalIdent);
                return currentPosition;
            }
            String objectValue = objectWords.get(keywordIdent);
            if (objectValue != null) {
                result.append(objectValue);
            } else {
                result.append(originalIdent);
            }
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
                } else if (currentChar == '/' && currentPosition < input.length() && source[currentPosition] == '/') {
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
                } else if (currentChar == '/' && currentPosition < input.length() && source[currentPosition] == '*') {
                    if (currentPosition < (input.length() - 1) && source[currentPosition + 1] == '*') {
                        // javadoc style
                        result.append(FONT_JAVADOC);
                    } else {
                        // multiline comment
                        result.append(FONT_COMMENT);
                    }
                    appendChar(result, currentChar);
                    appendChar(result, source[currentPosition++]);
                    while (currentPosition < input.length()) {
                        currentChar = source[currentPosition++];
                        appendChar(result, currentChar);
                        if (currentChar == '/' && source[currentPosition - 2] == '*') {
                            break;
                        }
                    }
                    result.append(FONT_END);
                    continue;
                } else if (currentChar == '<' && isPHPTag() && currentPosition + 3 < input.length() && source[currentPosition] == '?'
                        && source[currentPosition + 1] == 'p' && source[currentPosition + 2] == 'h' && source[currentPosition + 3] == 'p') {
                    // php start tag
                    currentPosition += 4;
                    result.append(FONT_KEYWORD + "&#60;?php" + FONT_END);
                    continue;
                } else if (currentChar == '?' && isPHPTag() && currentPosition < input.length() && source[currentPosition] == '>') {
                    // php start tag
                    currentPosition++;
                    result.append(FONT_KEYWORD + "?&#62;" + FONT_END);
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

    /**
     * Test if the keywords are case sensitive. Returns <code>true</code> by
     * default.
     *
     * @return <code>true</code> if the keywords are case sensitive
     */
    public boolean isKeywordCaseSensitive() {
        return true;
    }

    /**
     *
     */
    public boolean isPHPTag() {
        return false;
    }
}
