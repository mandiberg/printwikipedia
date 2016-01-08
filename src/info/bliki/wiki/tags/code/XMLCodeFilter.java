package info.bliki.wiki.tags.code;

import java.util.HashMap;

/**
 * Syntax highlighting support for XML/HTML source code
 *
 */
public class XMLCodeFilter extends AbstractCPPBasedCodeFilter implements SourceCodeFormatter {

    public XMLCodeFilter() {
    }

    /**
     * @return Returns the KEYWORD_SET.
     */
    @Override
    public HashMap<String, String> getKeywordSet() {
        return null;
    }

    /**
     * @return Returns the OBJECT_SET.
     */
    @Override
    public HashMap<String, String> getObjectSet() {
        return null;
    }

    @Override
    public String filter(String input) {
        char[] source = input.toCharArray();
        int currentPosition = 0;
        char currentChar = ' ';
        StringBuilder result = new StringBuilder(input.length() + input.length() / 4);
        try {
            while (true) {
                currentChar = source[currentPosition++];
                if (currentChar == '<') { // opening tags
                    if (source[currentPosition] == '!' && source[currentPosition + 1] == '-' && source[currentPosition + 2] == '-') {
                        // html comment
                        result.append(FONT_COMMENT);
                        appendChar(result, currentChar);// '<'
                        appendChar(result, source[currentPosition++]); // '!'
                        appendChar(result, source[currentPosition++]); // '-'
                        appendChar(result, source[currentPosition++]); // '-'
                        while (currentPosition < input.length()) {
                            currentChar = source[currentPosition++];
                            if (currentChar == '-' && source[currentPosition] == '-' && source[currentPosition + 1] == '>') {
                                appendChar(result, currentChar);
                                appendChar(result, source[currentPosition++]);
                                appendChar(result, source[currentPosition++]);
                                break;
                            }
                            appendChar(result, currentChar);
                        }
                        result.append(FONT_END);

                        continue;
                    } else {
                        result.append(FONT_KEYWORD);
                        appendChar(result, currentChar);
                        // start of identifier ?
                        currentChar = source[currentPosition++];
                        if (currentChar == '/') {
                            appendChar(result, currentChar);
                            currentChar = source[currentPosition++];
                        }
                        while ((currentChar >= 'a' && currentChar <= 'z') || (currentChar >= 'A' && currentChar <= 'Z')
                                || (currentChar >= '0' && currentChar <= '9') || currentChar == '_' || currentChar == '-'|| currentChar == ':') {
                            appendChar(result, currentChar);
                            currentChar = source[currentPosition++];
                        }
                        if (currentChar == '>') {
                            appendChar(result, currentChar);
                        } else {
                            currentPosition--;
                        }
                        result.append(FONT_END);
                        continue; // while loop
                    }
                } else if (currentChar == '/') { // closing tags
                    result.append(FONT_KEYWORD);
                    appendChar(result, currentChar);
                    if (source[currentPosition] == '>') {
                        currentChar = source[currentPosition++];
                        appendChar(result, currentChar);
                    }
                    result.append(FONT_END);
                    continue;
                } else if (currentChar == '>') { // closing tags
                    result.append(FONT_KEYWORD);
                    appendChar(result, currentChar);
                    result.append(FONT_END);
                    continue;
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
                }
                appendChar(result, currentChar);

            }
        } catch (IndexOutOfBoundsException e) {

        }
        // result.append(FONT_END);
        return result.toString();
    }

    @Override
    public boolean isKeywordCaseSensitive() {
        return false;
    }

    /**
     *
     */
    @Override
    public boolean isPHPTag() {
        return true;
    }
}
