package info.bliki.wiki.filter;

import info.bliki.wiki.model.ITableOfContent;
import info.bliki.wiki.model.IWikiModel;
import info.bliki.wiki.tags.util.NodeAttribute;
import info.bliki.wiki.tags.util.WikiTagNode;

import java.util.ArrayList;
import java.util.List;

public class WikipediaScanner {
    public static final int EOF = -1;
    protected int fScannerPosition;
    protected IWikiModel fWikiModel;

    /**
     * The <code>String</code> of the given raw wiki text
     */
    protected final String fStringSource;

    /**
     * The corresponding <code>char[]</code> array for the string source
     */
    protected final char[] fSource;

    public WikipediaScanner(String src) {
        this(src, 0);
    }

    public WikipediaScanner(String src, int position) {
        fSource = src.toCharArray();
        fStringSource = src;
        fScannerPosition = position;
    }

    public void setModel(IWikiModel wikiModel) {
        fWikiModel = wikiModel;
    }

    public int getPosition() {
        return fScannerPosition;
    }

    public void setPosition(int newPos) {
        fScannerPosition = newPos;
    }

    /**
     * Scan a wikipedia table.
     *
     * See: <a href="http://meta.wikimedia.org/wiki/Help:Table">Help - Table</a>
     *
     * @param tableOfContentTag
     * @return <code>null</code> if no wiki table was found
     */
    public WPTable wpTable(ITableOfContent tableOfContentTag) {
        WPTable table = null;
        WPCell cell = null;
        ArrayList<WPCell> cells = new ArrayList<>();
        WPRow row = new WPRow(cells);
        try {
            if (fScannerPosition < 0) {
                // simulate newline
                fScannerPosition = 0;
            }
            if (fSource[fScannerPosition++] != '{') {
                return null;
            }
            if (fSource[fScannerPosition++] != '|') {
                return null;
            }
            ArrayList<WPRow> rows = new ArrayList<>();
            table = new WPTable(rows);
            int startPos = fScannerPosition;
            // read parameters until end of line
            nextNewline();
            table.setParams(fStringSource.substring(startPos, fScannerPosition));

            char ch = ' ';

            while (true) {
                ch = fSource[fScannerPosition++];
                switch (ch) {
                case '[':
                    int position = findNestedEndSingle(fSource, '[', ']', fScannerPosition);
                    if (position >= 0) {
                        fScannerPosition = position;
                        continue;
                    }
                    break;
                case '{':
                    int cposition = findNestedEndSingle(fSource, '{', '}', fScannerPosition);
                    if (cposition >= 0) {
                        fScannerPosition = cposition;
                        continue;
                    }
                    break;
                case '\n':
                    ch = fSource[fScannerPosition++];
                    // ignore whitespace at the beginning of the line
                    while (ch == ' ' || ch == '\t') {
                        ch = fSource[fScannerPosition++];
                    }
                    switch (ch) {
                    case '|': // "\n |"
                        if (cell != null) {
                            cell.createTagStack(table, fSource, fWikiModel, fScannerPosition - 2);
                            cell = null;
                        }

                        ch = fSource[fScannerPosition++];
                        switch (ch) {
                        case '-': // new row - "\n|-"
                            addTableRow(table, row);
                            cells = new ArrayList<>();
                            row = new WPRow(cells);
                            startPos = fScannerPosition;
                            nextNewlineCell(cell);
                            row.setParams(fStringSource.substring(startPos, fScannerPosition));
                            break;
                        case '+': // new row - "\n|+"
                            addTableRow(table, row);
                            cells = new ArrayList<>();
                            row = new WPRow(cells);
                            row.setType(WPCell.CAPTION);
                            cell = new WPCell(fScannerPosition);
                            cell.setType(WPCell.CAPTION);
                            cells.add(cell);
                            nextNewlineCell(cell);
                            cell.createTagStack(table, fSource, fWikiModel, fScannerPosition);
                            cell = null;

                            addTableRow(table, row);
                            cells = new ArrayList<>();
                            row = new WPRow(cells);
                            break;
                        case '}': // end of table - "\n|}"
                            addTableRow(table, row);
                            return table;
                        default:
                            fScannerPosition--;
                            cell = new WPCell(fScannerPosition);
                            cells.add(cell);
                        }

                        break;
                    case '!': // "\n !"
                        if (cell != null) {
                            cell.createTagStack(table, fSource, fWikiModel, fScannerPosition - 2);
                            cell = null;
                        }
                        ch = fSource[fScannerPosition++];
                        cell = new WPCell(fScannerPosition - 1);
                        cell.setType(WPCell.TH);
                        cells.add(cell);

                        break;
                    case '{': // "\n {"
                        if (fSource[fScannerPosition] == '|') {
                            // start of nested table?
                            fScannerPosition = indexEndOfTable();
                            break;
                        }
                        break;
                    default:
                        fScannerPosition--;
                    }
                    break;
                case '|':
                    ch = fSource[fScannerPosition++];
                    if (ch == '|') {
                        if (cell != null) {
                            cell.createTagStack(table, fSource, fWikiModel, fScannerPosition - 2);
                            cell = null;
                        }
                        cell = new WPCell(fScannerPosition);
                        cells.add(cell);
                    } else {
                        fScannerPosition--;
                        if (cell != null) {
                            cell.setAttributesStartPos(fScannerPosition - 1);
                        }
                    }
                    break;
                case '!':
                    ch = fSource[fScannerPosition++];
                    if (ch == '!') {
                        if (cell != null) {
                            cell.createTagStack(table, fSource, fWikiModel, fScannerPosition - 2);
                            cell = null;
                        }
                        cell = new WPCell(fScannerPosition);
                        cell.setType(WPCell.TH);
                        cells.add(cell);
                    } else {
                        fScannerPosition--;
                    }
                    break;
                default:
                    if (cell == null) {
                        cell = new WPCell(fScannerPosition - 1);
                        cell.setType(WPCell.UNDEFINED);
                        cells.add(cell);
                    }
                }
            }
        } catch (IndexOutOfBoundsException e) {
            // ...
            fScannerPosition = fSource.length;
            if (cell != null) {
                cell.createTagStack(table, fSource, fWikiModel, fScannerPosition);
                cell = null;
            }
            if (table != null && row != null && row.size() > 0) {
                addTableRow(table, row);
            }
        }
        if (table != null) {
            return table;
        }
        return null;
    }

    private void addTableRow(WPTable table, WPRow row) {
        if (row.getParams() != null) {
            table.add(row);
        } else {
            if (row.size() > 0) {
                table.add(row);
            }
        }
    }

    public WPList wpList() {
        WPList list = null;
        WPListElement listElement = null;
        int startPosition;
        try {
            char ch;
            char lastCh = ' ';
            char[] sequence = null;
            int count = 0;

            if (fScannerPosition < 0) {
                // simulate newline
                fScannerPosition = 0;
                ch = '\n';
            } else {
                ch = fSource[fScannerPosition++];
            }

            list = new WPList();

            while (true) {
                if (ch == WPList.DL_DD_CHAR) {
                    if ((fScannerPosition < fSource.length - 2) && fSource[fScannerPosition] == '/' && fSource[fScannerPosition + 1] == '/') {
                        if (fScannerPosition > 1 && Character.isLetter(fSource[fScannerPosition - 2])) {
                            // definition list with URL link
                            fScannerPosition += 2;
                            ch = fSource[fScannerPosition++];
                            continue;
                        }
                    }

                    if (lastCh == WPList.DL_DT_CHAR && sequence != null) {
                        startPosition = fScannerPosition;
                        if (listElement != null) {
                            listElement.createTagStack(fSource, fWikiModel, fScannerPosition - 1);
                            list.add(listElement);
                            listElement = null;
                        }
                        char[] ddSequence = new char[sequence.length];
                        System.arraycopy(sequence, 0, ddSequence, 0, sequence.length);
                        ddSequence[sequence.length - 1] = WPList.DL_DD_CHAR;
                        sequence = ddSequence;

                        int startPos;
                        while (true) {
                            ch = fSource[fScannerPosition++];
                            if (!Character.isWhitespace(ch)) {
                                startPos = fScannerPosition - 1;
                                listElement = new WPListElement(count, sequence, startPos);
                                break;
                            }
                            if (ch == '\n') {
                                fScannerPosition--; // to detect next row
                                startPos = fScannerPosition;
                                listElement = new WPListElement(count, sequence, startPos);
                                listElement.createTagStack(fSource, fWikiModel, startPos);
                                list.add(listElement);
                                listElement = null;
                                break;
                            }
                        }
                        lastCh = ' ';
                    }
                }
                if (ch == '\n' || fScannerPosition == 0) {
                    startPosition = fScannerPosition;
                    if (listElement != null) {
                        listElement.createTagStack(fSource, fWikiModel, fScannerPosition - 1);
                        list.add(listElement);
                        listElement = null;
                    }
                    ch = fSource[fScannerPosition++];
                    switch (ch) {
                    case WPList.DL_DD_CHAR:
                    case WPList.DL_DT_CHAR:
                    case WPList.OL_CHAR:
                    case WPList.UL_CHAR:
                        count = 1;
                        lastCh = ch;
                        while (fSource[fScannerPosition] == WPList.UL_CHAR || fSource[fScannerPosition] == WPList.OL_CHAR
                                || fSource[fScannerPosition] == WPList.DL_DD_CHAR || fSource[fScannerPosition] == WPList.DL_DT_CHAR) {
                            count++;
                            lastCh = fSource[fScannerPosition++];
                        }

                        sequence = new char[count];
                        System.arraycopy(fSource, fScannerPosition - count, sequence, 0, count);

                        int startPos;
                        while (true) {
                            ch = fSource[fScannerPosition++];
                            if (!Character.isWhitespace(ch)) {
                                startPos = fScannerPosition - 1;
                                listElement = new WPListElement(count, sequence, startPos);
                                break;
                            }
                            if (ch == '\n') {
                                fScannerPosition--; // to detect next row
                                startPos = fScannerPosition;
                                listElement = new WPListElement(count, sequence, startPos);
                                listElement.createTagStack(fSource, fWikiModel, startPos);
                                list.add(listElement);
                                listElement = null;
                                break;
                            }
                        }

                        break;

                    default:
                        fScannerPosition = startPosition;
                        return list;
                    }
                }

                if (ch == '<') {
                    int temp = readSpecialWikiTags(fScannerPosition);
                    if (temp >= 0) {
                        fScannerPosition = temp;
                    }
                } else if (ch == '[') {
                    int temp = findNestedEndSingle(fSource, '[', ']', fScannerPosition);
                    if (temp >= 0) {
                        fScannerPosition = temp;
                    }
                }
                ch = fSource[fScannerPosition++];
            }
        } catch (IndexOutOfBoundsException e) {
            fScannerPosition = fSource.length + 1;
        }
        if (list != null) {
            if (listElement != null) {
                listElement.createTagStack(fSource, fWikiModel, fScannerPosition - 1);
                list.add(listElement);
                listElement = null;
            }
            return list;
        }
        return null;
    }

    public int nextNewline() {
        while (true) {
            if (fSource[fScannerPosition++] == '\n') {
                return --fScannerPosition;
            }
        }
    }

    public int nextNewlineCell(WPCell cell) {
        char ch;
        while (true) {
            ch = fSource[fScannerPosition++];
            if (ch == '\n') {
                return --fScannerPosition;
            }
            if (ch == '|') {
                if (cell != null) {
                    cell.setAttributesStartPos(fScannerPosition - 1);
                }
            } else if (ch == '[') {
                int position = findNestedEndSingle(fSource, '[', ']', fScannerPosition);
                if (position >= 0) {
                    fScannerPosition = position;
                }
            } else if (ch == '{') {
                int cposition = findNestedEndSingle(fSource, '{', '}', fScannerPosition);
                if (cposition >= 0) {
                    fScannerPosition = cposition;
                }
            }
        }
    }

    /**
     * Get the offset position behind the next closing HTML comment tag (--&gt;).
     *
     * @return the offset position behind the next closing HTML comment tag or
     *         <code>-1</code> if no tag could be found.
     */
    public int indexEndOfComment() {
        char ch;
        while (fScannerPosition < fSource.length - 2) {
            ch = fSource[fScannerPosition++];
            if (ch == '-' && fSource[fScannerPosition] == '-' && fSource[fScannerPosition + 1] == '>') {
                return fScannerPosition + 2;
            }
        }
        return -1;
    }

    /**
     * Get the offset position behind the next &lt;/nowiki&gt; tag.
     *
     * @return the offset position behind the &lt;/nowiki&gt; tag or
     *         <code>-1</code> if no tag could be found.
     */
    public int indexEndOfNowiki() {
        char ch;
        while (fScannerPosition < fSource.length - 8) {
            ch = fSource[fScannerPosition++];
            if (ch == '<' && fSource[fScannerPosition] == '/' && fSource[fScannerPosition + 1] == 'n'
                    && fSource[fScannerPosition + 2] == 'o' && fSource[fScannerPosition + 3] == 'w' && fSource[fScannerPosition + 4] == 'i'
                    && fSource[fScannerPosition + 5] == 'k' && fSource[fScannerPosition + 6] == 'i' && fSource[fScannerPosition + 7] == '>') {
                return fScannerPosition + 8;
            }
        }
        return -1;
    }

    /**
     * Get the offset position behind the corresponding wiki table closing tag
     * (i.e. <code>|}</code>). The scanner detects HTML comment tags,
     * &lt;nowiki&gt; tags and nested wiki table tags (i.e.
     * <code>{|... {|...  ...|} ...|}</code>).
     *
     * @return the offset position behind the corresponding wiki table closing tag
     *         or <code>-1</code> if no corresponding tag could be found.
     */
    public int indexEndOfTable() {
        // check nowiki and html comments
        int nestedWikiTableCounter = 1;
        char ch;
        try {
            while (fScannerPosition < fSource.length) {
                ch = fSource[fScannerPosition++];
                if (ch == '<' && fSource[fScannerPosition] == '!' && fSource[fScannerPosition + 1] == '-'
                        && fSource[fScannerPosition + 2] == '-') {
                    // start of HTML comment
                    fScannerPosition += 3;
                    fScannerPosition = indexEndOfComment();
                    if (fScannerPosition == (-1)) {
                        return -1;
                    }
                } else if (ch == '<' && fSource[fScannerPosition] == 'n' && fSource[fScannerPosition + 1] == 'o'
                        && fSource[fScannerPosition + 2] == 'w' && fSource[fScannerPosition + 3] == 'i' && fSource[fScannerPosition + 4] == 'k'
                        && fSource[fScannerPosition + 5] == 'i' && fSource[fScannerPosition + 6] == '>') {
                    // start of <nowiki>
                    fScannerPosition += 7;
                    fScannerPosition = indexEndOfNowiki();
                    if (fScannerPosition == (-1)) {
                        return -1;
                    }
                } else if (ch == '\n' && fSource[fScannerPosition] == '{' && fSource[fScannerPosition + 1] == '|') {
                    // assume nested table
                    nestedWikiTableCounter++;
                } else if (ch == '\n') {
                    int oldPosition = fScannerPosition;
                    ch = fSource[fScannerPosition++];
                    // ignore SPACES and TABs at the beginning of the line
                    while (ch == ' ' || ch == '\t') {
                        ch = fSource[fScannerPosition++];
                    }
                    if (ch == '|' && fSource[fScannerPosition] == '}') {
                        if (--nestedWikiTableCounter == 0) {
                            return fScannerPosition + 1;
                        }
                    }
                    fScannerPosition = oldPosition;
                }
            }
        } catch (IndexOutOfBoundsException e) {
            // ..
        }
        return -1;
    }

    /**
     * <p>
     * Check if a String starts with a specified prefix (optionally case
     * insensitive).
     * </p>
     *
     * @see java.lang.String#startsWith(String)
     * @param str
     *          the String to check, may be null
     * @param toffset
     *          the starting offset of the subregion the String to check
     * @param prefix
     *          the prefix to find, may be null
     * @param ignoreCase
     *          inidicates whether the compare should ignore case (case
     *          insensitive) or not.
     * @return <code>true</code> if the String starts with the prefix or both
     *         <code>null</code>
     */
    public static boolean startsWith(String str, int toffset, String prefix, boolean ignoreCase) {
        if (str == null || prefix == null) {
            return (str == null && prefix == null);
        }
        if (prefix.length() > str.length() - toffset) {
            return false;
        }
        return str.regionMatches(ignoreCase, toffset, prefix, 0, prefix.length());
    }



    /**
     * Split the given src string by pipe symbol (i.e. &quot;|&quot;)
     *
     * @param sourceString
     * @param resultList
     *          the list which contains the splitted strings
     * @return splitted strings
     */
    public static List<String> splitByPipe(String sourceString, List<String> resultList) {
        return splitByChar('|', sourceString, resultList, -1);
    }

    /**
     * Split the given <code>srcArray</code> character array by pipe symbol (i.e.
     * &quot;|&quot;).
     *
     * @param srcArray
     *          the array to split
     * @param currOffset
     *          start position in <tt>srcArray</tt>
     * @param endOffset
     *          end position in <tt>srcArray</tt>
     * @param resultList
     *          the list which contains the splitted strings
     * u c bv1`nbvnvnbvnvnbvnbvnvnvnvnv
     * ^^was this a cat? 
     * @return splitted strings
     */
    public static List<String> splitByPipe(char[] srcArray, int currOffset, int endOffset, List<String> resultList) {
        return splitByChar('|', srcArray, currOffset, endOffset, resultList, -1);
    }

    /**
     * Split the given src string by pipe symbol (i.e. &quot;|&quot;)
     *
     * @param splitChar
     *          the character to split by
     * @param sourceString
     *          the string to split
     * @param resultList
     *          the list which contains the splitted strings
     * @param maxParts
     *          max number of parts to split the source into (less than <tt>0</tt>
     *          for infinite number of parts, otherwise only values greater than
     *          <tt>0</tt> allowed!)
     * @return splitted strings
     */
    public static List<String> splitByChar(final char splitChar, String sourceString, List<String> resultList, final int maxParts) {
        // TODO optimize this to avoid new char[] generation inside toCharArray() ?
        return splitByChar(splitChar, sourceString.toCharArray(), 0, sourceString.length(), resultList, maxParts);
    }

    /**
     * Split the given <code>srcArray</code> character array by the given
     * character.
     *
     * @param splitChar
     *          the character to split by
     * @param srcArray
     *          the array to split
     * @param currOffset
     *          start position in <tt>srcArray</tt>
     * @param endOffset
     *          end position in <tt>srcArray</tt>
     * @param resultList
     *          the list which contains the splitted strings
     * @param maxParts
     *          max number of parts to split the source into (less than <tt>0</tt>
     *          for infinite number of parts, otherwise only values greater than
     *          <tt>0</tt> allowed!)
     *
     * @return splitted strings
     */
    protected static List<String> splitByChar(final char splitChar, char[] srcArray, int currOffset, int endOffset,
            List<String> resultList, final int maxParts) {
        assert (maxParts != 0 && maxParts != 1); // this doesn't make any sense!
        if (resultList == null) {
            resultList = new ArrayList<>();
        }
        char ch;
        int[] temp = new int[] { -1, -1 };

        int lastOffset = currOffset;
        try {
            while (currOffset < endOffset) {
                ch = srcArray[currOffset++];
                if (ch == '[' && srcArray[currOffset] == '[') {
                    currOffset++;
                    temp[0] = findNestedEnd(srcArray, '[', ']', currOffset);
                    if (temp[0] >= 0) {
                        currOffset = temp[0];
                    }
                } else if (ch == '{' && srcArray[currOffset] == '{') {
                    currOffset++;
                    if (srcArray[currOffset] == '{' && srcArray[currOffset + 1] != '{') {
                        currOffset++;
                        temp = findNestedParamEnd(srcArray, currOffset);
                        if (temp[0] >= 0) {
                            currOffset = temp[0];
                        }
                    } else {
                        temp[0] = findNestedTemplateEnd(srcArray, currOffset);
                        if (temp[0] >= 0) {
                            currOffset = temp[0];
                        }
                    }
                } else if (ch == splitChar) {
                    if (maxParts > 0 && resultList.size() >= maxParts - 1) {
                        // take rest and put it into the last part
                        currOffset = endOffset;
                        break;
                    }
                    resultList.add(new String(srcArray, lastOffset, currOffset - lastOffset - 1));
                    lastOffset = currOffset;
                }
            }

            if (currOffset > lastOffset) {
                resultList.add(new String(srcArray, lastOffset, currOffset - lastOffset));
            } else if (currOffset == lastOffset) {
                resultList.add("");
            }
        } catch (IndexOutOfBoundsException e) {
            if (currOffset > lastOffset) {
                resultList.add(new String(srcArray, lastOffset, currOffset - lastOffset));
            } else if (currOffset == lastOffset) {
                resultList.add("");
            }
        }
        return resultList;
    }

    /**
     * Read until the end of a nested block i.e. something like
     * <code>[[...[[  ]]...]]</code>
     *
     * @param sourceArray
     * @param startCh
     * @param endChar
     * @param startPosition
     * @return the position of the nested end charcters or <code>-1</code> if not
     *         found
     */
    public static int findNestedEnd(final char[] sourceArray, final char startCh, final char endChar, int startPosition) {
        char ch;
        int level = 1;
        int position = startPosition;
        final int sourceArrayLength = sourceArray.length - 1;
        try {
            while (position < sourceArrayLength) {
                ch = sourceArray[position++];
                if (ch == startCh && sourceArray[position] == startCh) {
                    position++;
                    level++;
                } else if (ch == endChar && sourceArray[position] == endChar) {
                    position++;
                    if (--level == 0) {
                        return position;
                    }
                }
            }
            return -1;
        } catch (IndexOutOfBoundsException e) {
            return -1;
        }
    }

    /**
     * Read until the end of a nested block i.e. something like
     * <code>{{{...{...{{  }}...}...}}}</code>
     *
     * @param sourceArray
     * @param startCh
     * @param endChar
     * @param startPosition
     * @return the position of the nested end charcters or <code>-1</code> if not
     *         found
     */
    public static int findNestedEndSingle(final char[] sourceArray, final char startCh, final char endChar, int startPosition) {
        char ch;
        int level = 1;
        int position = startPosition;
        final int sourceArrayLength = sourceArray.length;
        try {
            while (position < sourceArrayLength) {
                ch = sourceArray[position++];
                if (ch == startCh) {
                    level++;
                } else if (ch == endChar) {
                    if (--level == 0) {
                        return position;
                    }
                }
            }
            return -1;
        } catch (IndexOutOfBoundsException e) {
            return -1;
        }
    }

    /**
     * @return the template end position or -1 if there is no end.
     * TODO: This logic needs to be improved, there are very likely cases where this does not work as
     * intended. Template parameters can take {} as well, e.g. <code>{{foo:bar|{|}|{}|baz}}</code>.
     */
    public static int findNestedTemplateEnd(final char[] sourceArray, int startPosition) {
        char ch;
        int countOpenBraces = 0;
        int position = startPosition;
        try {
            while (position < sourceArray.length) {
                ch = sourceArray[position++];
                switch (ch) {
                    case '{':
                        if (sourceArray[position - 2] == '{' || sourceArray[position] == '{') {
                            countOpenBraces++;
                        }
                        break;
                    case '}':
                        if (sourceArray[position - 2] != '}' &&  sourceArray[position] != '}')
                            break;

                        if (countOpenBraces > 0) {
                            countOpenBraces--;
                        } else {
                            return position + 1;
                        }
                        break;
                }
            }
            return -1;
        } catch (IndexOutOfBoundsException e) {
            return -1;
        }
    }

    /**
     * Find the end of a template parameter declaration or the end of a template
     * declaration.
     *
     * @param sourceArray
     * @param startPosition
     * @return an array of two integers. If <code>array[0] > 0</code> the scanner
     *         has found the end position of a template parameter declaration. If
     *         <code>array[1] > 0</code> the scanner has found the end position of
     *         a template declaration.
     */
    public static int[] findNestedParamEnd(final char[] sourceArray, int startPosition) {
        char ch;
        final int sourceArrayLength = sourceArray.length;
        int countSingleOpenBraces = 0;
        int parameterPosition = startPosition;
        try {
            while (parameterPosition < sourceArrayLength) {
                ch = sourceArray[parameterPosition++];
                if (ch == '{') {
                    if ((sourceArrayLength > parameterPosition) && sourceArray[parameterPosition] == '{') {
                        parameterPosition++;
                        if ((sourceArrayLength > parameterPosition) && sourceArray[parameterPosition] == '{'
                                && sourceArray[parameterPosition + 1] != '{') {
                            // template parameter beginning
                            parameterPosition++;
                            int[] temp = findNestedParamEnd(sourceArray, parameterPosition);
                            if (temp[0] >= 0) {
                                parameterPosition = temp[0];
                            } else {
                                if (temp[1] >= 0) {
                                    parameterPosition = temp[1];
                                } else {
                                    return new int[] { -1, -1 };
                                }
                            }
                        } else {
                            // template beginning
                            int temp = findNestedTemplateEnd(sourceArray, parameterPosition);
                            if (temp < 0) {
                                return new int[] { -1, -1 };
                            }
                            parameterPosition = temp;
                        }
                    } else {
                        countSingleOpenBraces++;
                    }
                } else if (ch == '}') {
                    if (countSingleOpenBraces > 0) {
                        countSingleOpenBraces--;
                    } else {
                        if ((sourceArrayLength > parameterPosition) && sourceArray[parameterPosition] == '}') {
                            if (sourceArray[parameterPosition + 1] == '}') {
                                // template parameter ending
                                return new int[] { parameterPosition + 2, -1 };
                            } else {
                                return new int[] { -1, parameterPosition + 1 };
                            }
                        }
                    }

                }
            }
            return new int[] { -1, -1 };
        } catch (IndexOutOfBoundsException e) {
            return new int[] { -1, -1 };
        }
    }

    /**
     * Parse a tag. Parse the name and attributes from a start tag.
     * <p>
     * From the <a href="http://www.w3.org/TR/html4/intro/sgmltut.html#h-3.2.2">
     * HTML 4.01 Specification, W3C Recommendation 24 December 1999</a>
     * http://www.w3.org/TR/html4/intro/sgmltut.html#h-3.2.2
     * <p>
     * <cite> 3.2.2 Attributes
     * <p>
     * Elements may have associated properties, called attributes, which may have
     * values (by default, or set by authors or scripts). Attribute/value pairs
     * appear before the final ">" of an element's start tag. Any number of
     * (legal) attribute value pairs, separated by spaces, may appear in an
     * element's start tag. They may appear in any order.
     * <p>
     * In this example, the id attribute is set for an H1 element: <code>
     * &lt;H1 id="section1"&gt;
     * </code> This is
     * an identified heading thanks to the id attribute <code>
     * &lt;/H1&gt;
     * </code> By default, SGML
     * requires that all attribute values be delimited using either double
     * quotation marks (ASCII decimal 34) or single quotation marks (ASCII decimal
     * 39). Single quote marks can be included within the attribute value when the
     * value is delimited by double quote marks, and vice versa. Authors may also
     * use numeric character references to represent double quotes (&amp;#34;) and
     * single quotes (&amp;#39;). For doublequotes authors can also use the
     * character entity reference &amp;quot;.
     * <p>
     * In certain cases, authors may specify the value of an attribute without any
     * quotation marks. The attribute value may only contain letters (a-z and
     * A-Z), digits (0-9), hyphens (ASCII decimal 45), periods (ASCII decimal 46),
     * underscores (ASCII decimal 95), and colons (ASCII decimal 58). We recommend
     * using quotation marks even when it is possible to eliminate them.
     * <p>
     * Attribute names are always case-insensitive.
     * <p>
     * Attribute values are generally case-insensitive. The definition of each
     * attribute in the reference manual indicates whether its value is
     * case-insensitive.
     * <p>
     * All the attributes defined by this specification are listed in the
     * attribute index.
     * <p>
     * </cite>
     * <p>
     * This method uses a state machine with the following states:
     * <ol>
     * <li>state 0 - outside of any attribute</li>
     * <li>state 1 - within attributre name</li>
     * <li>state 2 - equals hit</li>
     * <li>state 3 - within naked attribute value.</li>
     * <li>state 4 - within single quoted attribute value</li>
     * <li>state 5 - within double quoted attribute value</li>
     * <li>state 6 - whitespaces after attribute name could lead to state 2 (=)or
     * state 0</li>
     * </ol>
     * <p>
     * The starting point for the various components is stored in an array of
     * integers that match the initiation point for the states one-for-one, i.e.
     * bookmarks[0] is where state 0 began, bookmarks[1] is where state 1 began,
     * etc. Attributes are stored in a <code>Vector</code> having one slot for
     * each whitespace or attribute/value pair. The first slot is for attribute
     * name (kind of like a standalone attribute).
     *
     * @param start
     *          The position at which to start scanning.
     * @return The parsed tag.
     * @exception ParserException
     *              If a problem occurs reading from the source.
     */
    protected WikiTagNode parseTag(int start) {
        boolean done;
        char ch;
        int state;
        int[] bookmarks;

        done = false;
        ArrayList<NodeAttribute> attributes = new ArrayList<>();
        state = 0;
        fScannerPosition = start;
        bookmarks = new int[8];
        bookmarks[0] = fScannerPosition;
        try {
            while (!done) {
                bookmarks[state + 1] = fScannerPosition;
                ch = fSource[fScannerPosition++];
                switch (state) {
                case 0: // outside of any attribute
                    if ((EOF == ch) || ('>' == ch) || ('<' == ch)) {
                        if ('<' == ch) {
                            // don't consume the opening angle
                            bookmarks[state + 1] = --fScannerPosition;
                        }
                        whitespace(attributes, bookmarks);
                        done = true;
                    } else if (!Character.isWhitespace(ch)) {
                        whitespace(attributes, bookmarks);
                        state = 1;
                    }
                    break;
                case 1: // within attribute name
                    if ((EOF == ch) || ('>' == ch) || ('<' == ch)) {
                        if ('<' == ch) {
                            // don't consume the opening angle
                            bookmarks[state + 1] = --fScannerPosition;
                        }
                        standalone(attributes, bookmarks);
                        done = true;
                    } else if (Character.isWhitespace(ch)) {
                        // whitespaces might be followed by next attribute or an
                        // equal sign
                        // see Bug #891058 Bug in lexer.
                        bookmarks[6] = bookmarks[2]; // setting the
                        // bookmark[0]
                        // is done in state 6 if
                        // applicable
                        state = 6;
                    } else if ('=' == ch)
                        state = 2;
                    break;
                case 2: // equals hit
                    if ((EOF == ch) || ('>' == ch)) {
                        empty(attributes, bookmarks);
                        done = true;
                    } else if ('\'' == ch) {
                        state = 4;
                        bookmarks[4] = bookmarks[3];
                    } else if ('"' == ch) {
                        state = 5;
                        bookmarks[5] = bookmarks[3];
                    } else if (Character.isWhitespace(ch)) {
                        // collect white spaces after "=" into the assignment
                        // string;
                        // do nothing
                        // see Bug #891058 Bug in lexer.
                    } else
                        state = 3;
                    break;
                case 3: // within naked attribute value
                    if ((EOF == ch) || ('>' == ch)) {
                        naked(attributes, bookmarks);
                        done = true;
                    } else if (Character.isWhitespace(ch)) {
                        naked(attributes, bookmarks);
                        bookmarks[0] = bookmarks[4];
                        state = 0;
                    } else if (ch == '/' && fSource[fScannerPosition] == '>') {
                        naked(attributes, bookmarks);
                        bookmarks[0] = bookmarks[4];
                        fScannerPosition--;
                        state = 0;
                    }
                    break;
                case 4: // within single quoted attribute value
                    if (EOF == ch) {
                        single_quote(attributes, bookmarks);
                        done = true; // complain?
                    } else if ('\'' == ch) {
                        single_quote(attributes, bookmarks);
                        bookmarks[0] = bookmarks[5] + 1;
                        state = 0;
                    }
                    break;
                case 5: // within double quoted attribute value
                    if (EOF == ch) {
                        double_quote(attributes, bookmarks);
                        done = true; // complain?
                    } else if ('"' == ch) {
                        double_quote(attributes, bookmarks);
                        bookmarks[0] = bookmarks[6] + 1;
                        state = 0;
                    }
                    break;
                case 6: // undecided for state 0 or 2
                    // we have read white spaces after an attributte name
                    if (EOF == ch) {
                        // same as last else clause
                        standalone(attributes, bookmarks);
                        bookmarks[0] = bookmarks[6];
                        // mPage.ungetCharacter(mCursor);
                        --fScannerPosition;
                        state = 0;
                    } else if (Character.isWhitespace(ch)) {
                        // proceed
                    } else if ('=' == ch) {// yepp. the white spaces belonged to the
                        // equal.
                        bookmarks[2] = bookmarks[6];
                        bookmarks[3] = bookmarks[7];
                        state = 2;
                    } else {
                        // white spaces were not ended by equal
                        // meaning the attribute was a stand alone attribute
                        // now: create the stand alone attribute and rewind
                        // the cursor to the end of the white spaces
                        // and restart scanning as whitespace attribute.
                        standalone(attributes, bookmarks);
                        bookmarks[0] = bookmarks[6];
                        --fScannerPosition;
                        state = 0;
                    }
                    break;
                default:
                    throw new IllegalStateException("how did we get in state " + state);
                }
            }
            if (fSource[fScannerPosition - 1] != '>') {
                fScannerPosition = start;
                return null;
            }
            return (makeTag(start, fScannerPosition, attributes));
        } catch (IndexOutOfBoundsException e) {

            if (state == 3) {
                // within naked attribute value
                naked(attributes, bookmarks);
            }
        }
        fScannerPosition = start;
        return null;
    }

    protected List<NodeAttribute> parseAttributes(int start, int end) {
        boolean done;
        char ch;
        int state;
        int[] bookmarks;

        done = false;
        ArrayList<NodeAttribute> attributes = new ArrayList<>();
        state = 0;
        fScannerPosition = start;
        bookmarks = new int[8];
        bookmarks[0] = fScannerPosition;
        try {
            while (!done && fScannerPosition < end) {
                bookmarks[state + 1] = fScannerPosition;
                ch = fSource[fScannerPosition++];
                switch (state) {
                case 0: // outside of any attribute
                    if ((EOF == ch) || ('>' == ch) || ('<' == ch)) {
                        if ('<' == ch) {
                            // don't consume the opening angle
                            bookmarks[state + 1] = --fScannerPosition;
                        }
                        whitespace(attributes, bookmarks);
                        done = true;
                    } else if (!Character.isWhitespace(ch)) {
                        whitespace(attributes, bookmarks);
                        state = 1;
                    }
                    break;
                case 1: // within attribute name
                    if ((EOF == ch) || ('>' == ch) || ('<' == ch)) {
                        if ('<' == ch) {
                            // don't consume the opening angle
                            bookmarks[state + 1] = --fScannerPosition;
                        }
                        standalone(attributes, bookmarks);
                        done = true;
                    } else if (Character.isWhitespace(ch)) {
                        // whitespaces might be followed by next attribute or an
                        // equal sign
                        // see Bug #891058 Bug in lexer.
                        bookmarks[6] = bookmarks[2]; // setting the
                        // bookmark[0]
                        // is done in state 6 if
                        // applicable
                        state = 6;
                    } else if ('=' == ch)
                        state = 2;
                    break;
                case 2: // equals hit
                    if ((EOF == ch) || ('>' == ch)) {
                        empty(attributes, bookmarks);
                        done = true;
                    } else if ('\'' == ch) {
                        state = 4;
                        bookmarks[4] = bookmarks[3];
                    } else if ('"' == ch) {
                        state = 5;
                        bookmarks[5] = bookmarks[3];
                    } else if (Character.isWhitespace(ch)) {
                        // collect white spaces after "=" into the assignment
                        // string;
                        // do nothing
                        // see Bug #891058 Bug in lexer.
                    } else
                        state = 3;
                    break;
                case 3: // within naked attribute value
                    if ((EOF == ch) || ('>' == ch)) {
                        naked(attributes, bookmarks);
                        done = true;
                    } else if (Character.isWhitespace(ch)) {
                        naked(attributes, bookmarks);
                        bookmarks[0] = bookmarks[4];
                        state = 0;
                    }
                    break;
                case 4: // within single quoted attribute value
                    if (EOF == ch) {
                        single_quote(attributes, bookmarks);
                        done = true; // complain?
                    } else if ('\'' == ch) {
                        single_quote(attributes, bookmarks);
                        bookmarks[0] = bookmarks[5] + 1;
                        state = 0;
                    }
                    break;
                case 5: // within double quoted attribute value
                    if (EOF == ch) {
                        double_quote(attributes, bookmarks);
                        done = true; // complain?
                        // } else if ('\\' == ch && fSource[fScannerPosition] == '"') {
                        // fScannerPosition++;
                    } else if ('"' == ch) {
                        double_quote(attributes, bookmarks);
                        bookmarks[0] = bookmarks[6] + 1;
                        state = 0;
                    }
                    break;
                // patch for lexer state correction by
                // Gernot Fricke
                // See Bug # 891058 Bug in lexer.
                case 6: // undecided for state 0 or 2
                    // we have read white spaces after an attributte name
                    if (EOF == ch) {
                        // same as last else clause
                        standalone(attributes, bookmarks);
                        bookmarks[0] = bookmarks[6];
                        // mPage.ungetCharacter(mCursor);
                        --fScannerPosition;
                        state = 0;
                    } else if (Character.isWhitespace(ch)) {
                        // proceed
                    } else if ('=' == ch) // yepp. the white spaces belonged
                    // to the equal.
                    {
                        bookmarks[2] = bookmarks[6];
                        bookmarks[3] = bookmarks[7];
                        state = 2;
                    } else {
                        // white spaces were not ended by equal
                        // meaning the attribute was a stand alone attribute
                        // now: create the stand alone attribute and rewind
                        // the cursor to the end of the white spaces
                        // and restart scanning as whitespace attribute.
                        standalone(attributes, bookmarks);
                        bookmarks[0] = bookmarks[6];
                        --fScannerPosition;
                        state = 0;
                    }
                    break;
                default:
                    throw new IllegalStateException("how did we get in state " + state);
                }
            }
            if (state == 3 || state == 4 || state == 5) {
                // within naked attribute value
                bookmarks[state + 1] = fScannerPosition;
                naked(attributes, bookmarks);
            }
            return attributes;
        } catch (IndexOutOfBoundsException e) {

        }
        return null;
    }

    /**
     * Create a tag node based on the current cursor and the one provided.
     *
     * @param start
     *          The starting point of the node.
     * @param end
     *          The ending point of the node.
     * @param attributes
     *          The attributes parsed from the tag.
     * @exception ParserException
     *              If the nodefactory creation of the tag node fails.
     * @return The new Tag node.
     */
    protected WikiTagNode makeTag(int start, int end, ArrayList<NodeAttribute> attributes) {
        int length;
        length = end - start;
        if (0 != length) { // return tag based on second character, '/', '%',
            // Letter (ch), '!'
            if (2 > length) {
                // this is an error
                return null; // (makeString(start, end));
            }
            return new WikiTagNode(start, end, attributes);
        }
        return null;
    }

    /**
     * Generate a whitespace 'attribute',
     *
     * @param attributes
     *          The list so far.
     * @param bookmarks
     *          The array of positions.
     */
    private void whitespace(ArrayList<NodeAttribute> attributes, int[] bookmarks) {
        // if (bookmarks[1] > bookmarks[0])
        // attributes.addElement(new PageAttribute(fSource,-1, -1, bookmarks[0],
        // bookmarks[1], (char) 0));
    }

    /**
     * Generate a standalone attribute -- font.
     *
     * @param attributes
     *          The list so far.
     * @param bookmarks
     *          The array of positions.
     */
    private void standalone(ArrayList<NodeAttribute> attributes, int[] bookmarks) {
        attributes.add(new NodeAttribute(fSource, bookmarks[1], bookmarks[2], -1, -1, (char) 0));
    }

    /**
     * Generate an empty attribute -- color=.
     *
     * @param attributes
     *          The list so far.
     * @param bookmarks
     *          The array of positions.
     */
    private void empty(ArrayList<NodeAttribute> attributes, int[] bookmarks) {
        attributes.add(new NodeAttribute(fSource, bookmarks[1], bookmarks[2], bookmarks[2] + 1, -1, (char) 0));
    }

    /**
     * Generate an unquoted attribute -- size=1.
     *
     * @param attributes
     *          The list so far.
     * @param bookmarks
     *          The array of positions.
     */
    private void naked(ArrayList<NodeAttribute> attributes, int[] bookmarks) {
        attributes.add(new NodeAttribute(fSource, bookmarks[1], bookmarks[2], bookmarks[3], bookmarks[4], (char) 0));
    }

    /**
     * Generate an single quoted attribute -- width='100%'.
     *
     * @param attributes
     *          The list so far.
     * @param bookmarks
     *          The array of positions.
     */
    private void single_quote(ArrayList<NodeAttribute> attributes, int[] bookmarks) {
        attributes.add(new NodeAttribute(fSource, bookmarks[1], bookmarks[2], bookmarks[4] + 1, bookmarks[5], '\''));
    }

    /**
     * Generate an double quoted attribute -- CONTENT="Test Development".
     *
     * @param attributes
     *          The list so far.
     * @param bookmarks
     *          The array of positions.
     */
    private void double_quote(ArrayList<NodeAttribute> attributes, int[] bookmarks) {
        attributes.add(new NodeAttribute(fSource, bookmarks[1], bookmarks[2], bookmarks[5] + 1, bookmarks[6], '"'));
    }

    protected int readSpecialWikiTags(int start) {
        int startPosition = fScannerPosition;
        try {
            if (fSource[start] != '/') {
                // starting tag
                WikiTagNode tagNode = parseTag(start);
                if (tagNode != null && !tagNode.isEmptyXmlTag()) {
                    String tagName = tagNode.getTagName();
                    return readUntilIgnoreCase(fScannerPosition, "</", tagName+">");
                }
            }
        } catch (IndexOutOfBoundsException e) {
            // do nothing
        }
        fScannerPosition = startPosition;
        return -1;
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
    protected final int readUntilIgnoreCase(int start, String startString, String endString) {
        int index = Util.indexOfIgnoreCase(fStringSource, startString, endString, start);
        if (index != (-1)) {
            return index + startString.length() + endString.length();
        }
        return -1;
    }

    /**
     * Read the characters until no more letters are found or the given
     * <code>testChar</code> is found. If <code>testChar</code> was found, return
     * the offset position.
     *
     * @param testCh
     *          the test character
     * @param fromIndex
     *          read from this offset
     * @return <code>-1</code> if the character could not be found or no more
     *         letter character were found.
     */
    protected int indexOfUntilNoLetter(char testChar, int fromIndex) {
        int index = fromIndex;
        char ch;
        while (index < fSource.length) {
            ch = fSource[index++];
            if (ch == testChar) {
                return index - 1;
            }
            if (Character.isLetter(ch)) {
                if (fSource.length <= index) {
                    return -1;
                }
                continue;
            }
            return -1;
        }
        return -1;
    }
}
