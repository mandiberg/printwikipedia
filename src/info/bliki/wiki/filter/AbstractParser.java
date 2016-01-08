package info.bliki.wiki.filter;

import info.bliki.wiki.tags.util.WikiTagNode;

public abstract class AbstractParser extends WikipediaScanner {
    protected char fCurrentCharacter;
    protected int fCurrentPosition;
    protected boolean fWhiteStart;
    protected int fWhiteStartPosition;

    public AbstractParser(String stringSource) {
        super(stringSource);
        fCurrentPosition = 0;
        fCurrentCharacter = '\000';
        fWhiteStart = false;
        fWhiteStartPosition = 0;
    }

    /**
     * Read the characters until the given string is found and set the current
     * position of the parser behind the found string.
     *
     * @param untilString
     * @return <code>true</code> if the string was found; <code>false</code>
     *         otherwise
     */
    protected final boolean readUntil(String untilString) {
        int index = fStringSource.indexOf(untilString, fCurrentPosition);
        if (index != (-1)) {
            fCurrentPosition = index + untilString.length();
            return true;
        }
        fCurrentPosition = fStringSource.length();
        return false;
    }

    /**
     * Read the characters until the concatenated <i>start</i> and <i>end</i>
     * substring is found. The end substring is matched ignoring case
     * considerations.
     *
     * @param startString
     *          the start string which should be searched in exact case mode
     * @param endString
     *          the end string which should be searched in ignore case mode
     * @return
     */
    protected final int readUntilIgnoreCase(String startString, String endString) {
        int index = Util.indexOfIgnoreCase(fStringSource, startString, endString, fCurrentPosition);
        if (index != (-1)) {
            fCurrentPosition = index + startString.length() + endString.length();
            return startString.length() + endString.length();
        }
        fCurrentPosition = fStringSource.length();
        return 0;
    }

    protected int readUntilNestedIgnoreCase(WikiTagNode node) {
        if (node.isEmptyXmlTag()) {
            fCurrentPosition = node.getEndPosition();
            return node.getEndPosition() - node.getStartPosition();
        } else {
            return readUntilNestedIgnoreCase(node.getTagName()+">");
        }
    }

    private int readUntilNestedIgnoreCase(String endString) {
        int index = Util.indexOfNestedIgnoreCase(fStringSource, endString, fCurrentPosition);
        if (index != -1) {
            fCurrentPosition = index + 2 + endString.length();
            return 2 + endString.length();
        }
        fCurrentPosition = fStringSource.length();
        return 0;
    }

    /**
     * Read until character is found or stop at end-of-line
     *
     * @param testedChar
     *          search the next position of this char
     * @return <code>true</code> if the tested character can be found
     */
    protected final boolean readUntilCharOrStopAtEOL(char testedChar) {
        int temp = fCurrentPosition;
        boolean attrMode = false; // allow newline characters in attributes
        try {
            while ((fCurrentCharacter = fSource[fCurrentPosition++]) != testedChar) {
                if (attrMode) {
                    if (fCurrentCharacter == '"') {
                        attrMode = false;
                    }
                } else {
                    if (fCurrentCharacter == '\n' || fCurrentCharacter == '\r') {
                        return false;
                    }
                    if (fCurrentCharacter == '"') {
                        attrMode = true;
                    }
                }
            }
            return true;
        } catch (IndexOutOfBoundsException e) {
            fCurrentPosition = temp;
            return false;
        }
    }

    protected boolean isEmptyLine(int diff) {
        int temp = fCurrentPosition - diff;
        char ch;
        try {
            while (true) {
                ch = fSource[temp++];
                if (!Character.isWhitespace(ch)) {
                    return false;
                }
                if (ch == '\n') {
                    return true;
                }
            }
        } catch (IndexOutOfBoundsException e) {
            // ..
        }
        return true;
    }

    protected int consumeWhitespace() {
        if (!Character.isWhitespace(fSource[fCurrentPosition + 1]))
            return -1;

        int temp = fCurrentPosition+1;
        char ch;
        while (fSource.length > temp) {
            ch = fSource[temp];
            if (Character.isWhitespace(ch)) {
                temp++;
            } else {
                break;
            }
        }
        fCurrentPosition = temp - 1;
        return temp;
    }

    protected int readWhitespaceUntilEndOfLine(int diff) {
        int temp = fCurrentPosition - diff;
        char ch;
        while (fSource.length > temp) {
            ch = fSource[temp];
            if (!Character.isWhitespace(ch)) {
                return -1;
            }
            if (ch == '\n') {
                fCurrentPosition = temp;
                return temp;
            }
            temp++;
        }
        fCurrentPosition = temp - 1;
        return temp;
    }

    protected int readWhitespaceUntilStartOfLine(int diff) {
        int temp = fCurrentPosition - diff;
        char ch;

        while (temp >= 0) {
            ch = fSource[temp];
            if (!Character.isWhitespace(ch)) {
                return -1;
            }
            if (ch == '\n') {
                return temp;
            }
            temp--;
        }

        return temp;
    }

    protected final boolean getNextChar(char testedChar) {
        int temp = fCurrentPosition;
        try {
            fCurrentCharacter = fSource[fCurrentPosition++];
            if (fCurrentCharacter != testedChar) {
                fCurrentPosition = temp;
                return false;
            }
            return true;

        } catch (IndexOutOfBoundsException e) {
            fCurrentPosition = temp;
            return false;
        }
    }

    protected final boolean getNextCharAsWhitespace() {
        int temp = fCurrentPosition;
        try {
            fCurrentCharacter = fSource[fCurrentPosition++];
            if (!Character.isWhitespace(fCurrentCharacter)) {
                fCurrentPosition = temp;
                return false;
            }
            return true;
        } catch (IndexOutOfBoundsException e) {
            fCurrentPosition = temp;
            return false;
        }
    }


    /**
     * Read the characters until the end position of the current wiki link is
     * found.
     *
     * @return <code>true</code> if the end of the wiki link was found.
     */
    protected final boolean findWikiLinkEnd() {
        char ch;
        int level = 1;
        int position = fCurrentPosition;
        boolean pipeSymbolFound = false;
        try {
            while (true) {
                ch = fSource[position++];
                if (ch == '|') {
                    pipeSymbolFound = true;
                } else if (ch == '[' && fSource[position] == '[') {
                    if (pipeSymbolFound) {
                        level++;
                        position++;
                    } else {
                        return false;
                    }
                } else if (ch == ']' && fSource[position] == ']') {
                    position++;
                    if (--level == 0) {
                        break;
                    }
                } else if (ch == '{' || ch == '}' || ch == '<' || ch == '>') {
                    if (!pipeSymbolFound) {
                        // see
                        // http://en.wikipedia.org/wiki/Help:Page_name#Special_characters
                        return false;
                    }
                }

                if ((!pipeSymbolFound) && (ch == '\n' || ch == '\r')) {
                    return false;
                }
            }
            fCurrentPosition = position;
            return true;
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }


}
