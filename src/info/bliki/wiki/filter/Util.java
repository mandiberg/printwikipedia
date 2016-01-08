package info.bliki.wiki.filter;

import info.bliki.htmlcleaner.TagNode;
import info.bliki.wiki.tags.util.NodeAttribute;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Util {

    private Util() {
    }

    /**
     * Scan the attributes string and add the attributes to the given node
     *
     * @param node
     * @param attributesString
     */
    public static NodeAttribute addAttributes(TagNode node, String attributesString) {
        if (attributesString != null) {
            List<NodeAttribute> attributes = getNodeAttributes(attributesString);
            if (attributes != null) {
                NodeAttribute attr = null;
                for (int i = 0; i < attributes.size(); i++) {
                    attr = attributes.get(i);
                    node.addAttribute(attr.getName(), attr.getValue(), true);
                }
                return attr;
            }
        }
        return null;
    }

    public static List<NodeAttribute> getNodeAttributes(String attributesString) {
        List<NodeAttribute> attributes = null;
        if (attributesString != null) {
            String trimmed = attributesString.trim();
            if (trimmed.length() != 0) {
                WikipediaScanner scanner = new WikipediaScanner(trimmed);
                attributes = scanner.parseAttributes(0, trimmed.length());
            }
        }
        return attributes;
    }

    public static Map<String, String> getAttributes(String attributesString) {
        TreeMap<String, String> map = null;
        if (attributesString != null) {
            String trimmed = attributesString.trim();
            if (trimmed.length() != 0) {
                map = new TreeMap<>();
                WikipediaScanner scanner = new WikipediaScanner(trimmed);
                List<NodeAttribute> attributes = scanner.parseAttributes(0, trimmed.length());
                NodeAttribute attr;
                for (int i = 0; i < attributes.size(); i++) {
                    attr = attributes.get(i);
                    map.put(attr.getName(), attr.getValue());
                }
            }
        }
        return map;
    }

    /**
     * Returns the index within the searchable string of the first occurrence of
     * the concatenated <i>start</i> and <i>end</i> substring. The end substring
     * is matched ignoring case considerations.
     *
     * @param searchableString
     *          the searchable string
     * @param startPattern
     *          the start string which should be searched in exact case mode
     * @param endPattern
     *          the end string which should be searched in ignore case mode
     * @param fromIndex
     *          the index from which to start the search.
     * @return the index within this string of the first occurrence of the
     *         specified substring, starting at the specified index.
     */
    public static int indexOfIgnoreCase(String searchableString, String startPattern, String endPattern, int fromIndex) {
        int n = endPattern.length();
        int index;
        int len = startPattern.length();
        while (searchableString.length() > ((fromIndex + n) - 1)) {
            index = searchableString.indexOf(startPattern, fromIndex);
            if (index >= 0) {
                fromIndex = index + len;
                if (searchableString.regionMatches(true, fromIndex, endPattern, 0, n)) {
                    return fromIndex - len;
                }
            } else {
                return -1;
            }
            fromIndex++;
        }

        return -1;
    }

    /**
     * Returns the nested index within the searchable string of the first
     * occurrence of <code>&lt;</code> and the <i>end</i> string. The end string
     * is matched ignoring case considerations.
     *
     * @param searchableString
     *          the searchable string
     * @param endPattern
     *          the end string which should be searched in ignore case mode
     * @param fromIndex
     *          the index from which to start the search.
     * @return the index within this string of the first occurrence of the
     *         specified substring, starting at the specified index.
     */
    public static int indexOfNestedIgnoreCase(String searchableString, String endPattern, int fromIndex) {
        int n = endPattern.length();
        int index;
        int level = 0;
        while (searchableString.length() > ((fromIndex + n) - 1)) {
            index = searchableString.indexOf('<', fromIndex);
            if (index < 0) {
                return -1;
            }
            if (searchableString.length() > index + n) {
                if (searchableString.charAt(index + 1) == '/') {
                    // closing tag
                    fromIndex = index + 2;
                    if (searchableString.regionMatches(true, fromIndex, endPattern, 0, n)) {
                        if (level == 0) {
                            return fromIndex - 2;
                        }
                        level--;
                    }
                } else {
                    // opening tag
                    fromIndex = index + 1;
                    if (searchableString.regionMatches(true, fromIndex, endPattern, 0, n)) {
                        level++;
                    }
                }
                fromIndex = index + 1;
            } else {
                return -1;
            }
        }

        return -1;
    }

    /**
     * Trim whitespace characters from the left and right side of the string,
     * until we find a non whitespace character or a new line character on the
     * righ side. Delete the first newline found on the right side of the string.
     *
     * @param str
     * @return
     */
    public static String trimNewlineRight(String str) {
        int end = str.length();
        int start = 0;

        while ((start < end) && Character.isWhitespace(str.charAt(start))) {
            start++;
        }
        while ((start < end) && Character.isWhitespace(str.charAt(end - 1))) {
            if (str.charAt(end - 1) == '\n') {
                end--;
                break;
            }
            end--;
        }
        if ((start < end) && str.charAt(end - 1) == '\r') {
            end--;
        }
        return ((start > 0) || (end < str.length())) ? str.substring(start, end) : str;
    }

    /**
     * Check if the template name contains an invalid (ISO Control) character.
     *
     * @param templateName
     * @return
     */
    public static boolean isInvalidTemplateName(CharSequence templateName) {
        boolean noNamespaceCharacter = true;
        for (int i = 0; i < templateName.length(); i++) {
            if (templateName.charAt(i) == ':') {
                noNamespaceCharacter = false;
                continue;
            }
            if (Character.isISOControl(templateName.charAt(i))) {
                if (noNamespaceCharacter) {
                    return true;
                }
                continue;
            }
            noNamespaceCharacter = true;
        }
        return false;
    }

    /**
     * Get the first position of the <code>rawWikitext</code> string, which
     * contains a character requiring template parsing.
     *
     * @param rawWikitext
     *          the raw wiki text
     * @return <code>-1</code> if character is found, which requires template
     *         parsing.
     */
    public static int indexOfTemplateParsing(CharSequence rawWikitext) {
        char ch;
        int len = rawWikitext.length() - 2;
        for (int i = 0; i < len; i++) {
            ch = rawWikitext.charAt(i);
            if (ch == '{') {
                return i;
            }
            if (ch == '<') {
                return i;
            }
            if (ch == '[') {
                return i;
            }
            if (ch == '~') {
                return i;
            }
        }
        return -1;
    }
}
