package info.bliki.wiki.filter;

import info.bliki.htmlcleaner.Utils;

import java.io.IOException;

/**
 * Some helper methods for encoding strings to HTML, URL or local files system
 * names
 *
 */
public class Encoder {

    final static String[] hex = { "%00", "%01", "%02", "%03", "%04", "%05", "%06", "%07", "%08", "%09", "%0A", "%0B", "%0C", "%0D",
            "%0E", "%0F", "%10", "%11", "%12", "%13", "%14", "%15", "%16", "%17", "%18", "%19", "%1A", "%1B", "%1C", "%1D", "%1E", "%1F",
            "%20", "%21", "%22", "%23", "%24", "%25", "%26", "%27", "%28", "%29", "%2A", "%2B", "%2C", "%2D", "%2E", "%2F", "%30", "%31",
            "%32", "%33", "%34", "%35", "%36", "%37", "%38", "%39", "%3A", "%3B", "%3C", "%3D", "%3E", "%3F", "%40", "%41", "%42", "%43",
            "%44", "%45", "%46", "%47", "%48", "%49", "%4A", "%4B", "%4C", "%4D", "%4E", "%4F", "%50", "%51", "%52", "%53", "%54", "%55",
            "%56", "%57", "%58", "%59", "%5A", "%5B", "%5C", "%5D", "%5E", "%5F", "%60", "%61", "%62", "%63", "%64", "%65", "%66", "%67",
            "%68", "%69", "%6A", "%6B", "%6C", "%6D", "%6E", "%6F", "%70", "%71", "%72", "%73", "%74", "%75", "%76", "%77", "%78", "%79",
            "%7A", "%7B", "%7C", "%7D", "%7E", "%7F", "%80", "%81", "%82", "%83", "%84", "%85", "%86", "%87", "%88", "%89", "%8A", "%8B",
            "%8C", "%8D", "%8E", "%8F", "%90", "%91", "%92", "%93", "%94", "%95", "%96", "%97", "%98", "%99", "%9A", "%9B", "%9C", "%9D",
            "%9E", "%9F", "%A0", "%A1", "%A2", "%A3", "%A4", "%A5", "%A6", "%A7", "%A8", "%A9", "%AA", "%AB", "%AC", "%AD", "%AE", "%AF",
            "%B0", "%B1", "%B2", "%B3", "%B4", "%B5", "%B6", "%B7", "%B8", "%B9", "%BA", "%BB", "%BC", "%BD", "%BE", "%BF", "%C0", "%C1",
            "%C2", "%C3", "%C4", "%C5", "%C6", "%C7", "%C8", "%C9", "%CA", "%CB", "%CC", "%CD", "%CE", "%CF", "%D0", "%D1", "%D2", "%D3",
            "%D4", "%D5", "%D6", "%D7", "%D8", "%D9", "%DA", "%DB", "%DC", "%DD", "%DE", "%DF", "%E0", "%E1", "%E2", "%E3", "%E4", "%E5",
            "%E6", "%E7", "%E8", "%E9", "%EA", "%EB", "%EC", "%ED", "%EE", "%EF", "%F0", "%F1", "%F2", "%F3", "%F4", "%F5", "%F6", "%F7",
            "%F8", "%F9", "%FA", "%FB", "%FC", "%FD", "%FE", "%FF" };

    final static String[] hex1 = { ".00", ".01", ".02", ".03", ".04", ".05", ".06", ".07", ".08", ".09", ".0A", ".0B", ".0C", ".0D",
            ".0E", ".0F", ".10", ".11", ".12", ".13", ".14", ".15", ".16", ".17", ".18", ".19", ".1A", ".1B", ".1C", ".1D", ".1E", ".1F",
            ".20", ".21", ".22", ".23", ".24", ".25", ".26", ".27", ".28", ".29", ".2A", ".2B", ".2C", ".2D", ".2E", ".2F", ".30", ".31",
            ".32", ".33", ".34", ".35", ".36", ".37", ".38", ".39", ".3A", ".3B", ".3C", ".3D", ".3E", ".3F", ".40", ".41", ".42", ".43",
            ".44", ".45", ".46", ".47", ".48", ".49", ".4A", ".4B", ".4C", ".4D", ".4E", ".4F", ".50", ".51", ".52", ".53", ".54", ".55",
            ".56", ".57", ".58", ".59", ".5A", ".5B", ".5C", ".5D", ".5E", ".5F", ".60", ".61", ".62", ".63", ".64", ".65", ".66", ".67",
            ".68", ".69", ".6A", ".6B", ".6C", ".6D", ".6E", ".6F", ".70", ".71", ".72", ".73", ".74", ".75", ".76", ".77", ".78", ".79",
            ".7A", ".7B", ".7C", ".7D", ".7E", ".7F", ".80", ".81", ".82", ".83", ".84", ".85", ".86", ".87", ".88", ".89", ".8A", ".8B",
            ".8C", ".8D", ".8E", ".8F", ".90", ".91", ".92", ".93", ".94", ".95", ".96", ".97", ".98", ".99", ".9A", ".9B", ".9C", ".9D",
            ".9E", ".9F", ".A0", ".A1", ".A2", ".A3", ".A4", ".A5", ".A6", ".A7", ".A8", ".A9", ".AA", ".AB", ".AC", ".AD", ".AE", ".AF",
            ".B0", ".B1", ".B2", ".B3", ".B4", ".B5", ".B6", ".B7", ".B8", ".B9", ".BA", ".BB", ".BC", ".BD", ".BE", ".BF", ".C0", ".C1",
            ".C2", ".C3", ".C4", ".C5", ".C6", ".C7", ".C8", ".C9", ".CA", ".CB", ".CC", ".CD", ".CE", ".CF", ".D0", ".D1", ".D2", ".D3",
            ".D4", ".D5", ".D6", ".D7", ".D8", ".D9", ".DA", ".DB", ".DC", ".DD", ".DE", ".DF", ".E0", ".E1", ".E2", ".E3", ".E4", ".E5",
            ".E6", ".E7", ".E8", ".E9", ".EA", ".EB", ".EC", ".ED", ".EE", ".EF", ".F0", ".F1", ".F2", ".F3", ".F4", ".F5", ".F6", ".F7",
            ".F8", ".F9", ".FA", ".FB", ".FC", ".FD", ".FE", ".FF" };

    final static String[] hex2 = { "'00", "'01", "'02", "'03", "'04", "'05", "'06", "'07", "'08", "'09", "'0A", "'0B", "'0C", "'0D",
            "'0E", "'0F", "'10", "'11", "'12", "'13", "'14", "'15", "'16", "'17", "'18", "'19", "'1A", "'1B", "'1C", "'1D", "'1E", "'1F",
            "'20", "'21", "'22", "'23", "'24", "'25", "'26", "'27", "'28", "'29", "'2A", "'2B", "'2C", "'2D", "'2E", "'2F", "'30", "'31",
            "'32", "'33", "'34", "'35", "'36", "'37", "'38", "'39", "'3A", "'3B", "'3C", "'3D", "'3E", "'3F", "'40", "'41", "'42", "'43",
            "'44", "'45", "'46", "'47", "'48", "'49", "'4A", "'4B", "'4C", "'4D", "'4E", "'4F", "'50", "'51", "'52", "'53", "'54", "'55",
            "'56", "'57", "'58", "'59", "'5A", "'5B", "'5C", "'5D", "'5E", "'5F", "'60", "'61", "'62", "'63", "'64", "'65", "'66", "'67",
            "'68", "'69", "'6A", "'6B", "'6C", "'6D", "'6E", "'6F", "'70", "'71", "'72", "'73", "'74", "'75", "'76", "'77", "'78", "'79",
            "'7A", "'7B", "'7C", "'7D", "'7E", "'7F", "'80", "'81", "'82", "'83", "'84", "'85", "'86", "'87", "'88", "'89", "'8A", "'8B",
            "'8C", "'8D", "'8E", "'8F", "'90", "'91", "'92", "'93", "'94", "'95", "'96", "'97", "'98", "'99", "'9A", "'9B", "'9C", "'9D",
            "'9E", "'9F", "'A0", "'A1", "'A2", "'A3", "'A4", "'A5", "'A6", "'A7", "'A8", "'A9", "'AA", "'AB", "'AC", "'AD", "'AE", "'AF",
            "'B0", "'B1", "'B2", "'B3", "'B4", "'B5", "'B6", "'B7", "'B8", "'B9", "'BA", "'BB", "'BC", "'BD", "'BE", "'BF", "'C0", "'C1",
            "'C2", "'C3", "'C4", "'C5", "'C6", "'C7", "'C8", "'C9", "'CA", "'CB", "'CC", "'CD", "'CE", "'CF", "'D0", "'D1", "'D2", "'D3",
            "'D4", "'D5", "'D6", "'D7", "'D8", "'D9", "'DA", "'DB", "'DC", "'DD", "'DE", "'DF", "'E0", "'E1", "'E2", "'E3", "'E4", "'E5",
            "'E6", "'E7", "'E8", "'E9", "'EA", "'EB", "'EC", "'ED", "'EE", "'EF", "'F0", "'F1", "'F2", "'F3", "'F4", "'F5", "'F6", "'F7",
            "'F8", "'F9", "'FA", "'FB", "'FC", "'FD", "'FE", "'FF" };

    public static String toEntity(int c) {
        return "&#" + c + ";";
    }

    public static char toChar(String number) {
        return (char) Integer.decode(number.substring(1)).intValue();
    }

/**
     * Encode a string to the "x-www-form-urlencoded" form, enhanced with the
     * UTF-8-in-URL proposal. This is what happens: See
     * http://www.w3.org/International/URLUTF8Encoder.java ) Exception: a ' '
     * (space) will be replaced by a '_' (underscore) not '+' (plus); a '#' remain the same.
     *
     * <ul>
     * <li>
     * <p>
     * The ASCII characters 'a' through 'z', 'A' through 'Z', and '0' through '9'
     * remain the same.
     *
     * <li>
     * <p>
     * The unreserved characters - _ . ! ~ * ' ( ) / remain the same.
     *
     * <li>
     * <p>
     * The '#' character remains the same.
     *
     * <li>
     * <p>
     * The space character ' ' is converted into a underscore sign '_' (not a plus
     * sign '+'!).
     *
     * <li>
     * <p>
     * All other ASCII characters are converted into the 3-character string "%xy",
     * where xy is the two-digit hexadecimal representation of the character code
     *
     * <li>
     * <p>
     * All non-ASCII characters are encoded in two steps: first to a sequence of 2
     * or 3 bytes, using the UTF-8 algorithm; secondly each of these bytes is
     * encoded as ".xx".
     * </ul>
     *
     * @param s
     *          The string to be encoded
     * @return The encoded string
     */
    public static String encodeUrl(String s) {
        int len = s.length();
        if (len == 0) {
            return "";
        }
        StringBuilder sbuf = new StringBuilder(len + len / 10);
        for (int i = 0; i < len; i++) {
            int ch = s.charAt(i);
            encodeUrl(sbuf, ch);
        }
        return sbuf.toString();
    }

    /**
     * Encode character for webbrowsers
     */
    private static void encodeUrl(StringBuilder sbuf, int ch) {
        if ('A' <= ch && ch <= 'Z') { // 'A'..'Z'
            sbuf.append((char) ch);
        } else if ('a' <= ch && ch <= 'z') { // 'a'..'z'
            sbuf.append((char) ch);
        } else if ('0' <= ch && ch <= '9') { // '0'..'9'
            sbuf.append((char) ch);
        } else if (ch == ' ') { // space
            // sbuf.append('+');
            sbuf.append('_'); // special for wiki syntax !
        } else if (ch == '#') {
            sbuf.append('#');
        } else if (ch == '-' || ch == '_' || ch == '/' || ch == '.' || ch == ':' || ch == '!' || ch == '~' || ch == '\'' || ch == '('
                || ch == ')') {
            sbuf.append((char) ch);
        } else if (ch <= 0x007f) { // other ASCII
            sbuf.append(hex[ch]);
        } else if (ch <= 0x07FF) { // non-ASCII <= 0x7FF
            sbuf.append(hex[0xc0 | (ch >> 6)]);
            sbuf.append(hex[0x80 | (ch & 0x3F)]);
        } else { // 0x7FF < ch <= 0xFFFF
            sbuf.append(hex[0xe0 | (ch >> 12)]);
            sbuf.append(hex[0x80 | ((ch >> 6) & 0x3F)]);
            sbuf.append(hex[0x80 | (ch & 0x3F)]);
        }
    }

/**
     * Encode a string to the "x-www-form-urlencoded" form, enhanced with the
     * UTF-8-in-URL proposal. This is what happens: 8see
     * http://www.w3.org/International/URLUTF8Encoder.java ) Exception: a ' '
     * (space) will be replaced by a '_' (underscore) not '+' (plus)
     *
     * <ul>
     * <li>
     * <p>
     * The ASCII characters 'a' through 'z', 'A' through 'Z', and '0' through '9'
     * remain the same.
     *
     * <li>
     * <p>
     * The unreserved characters - _ . ! ~ * ' ( ) / remain the same.
     *
     * <li>
     * <p>
     * The space character ' ' is converted into a underscore sign '_' (not a plus
     * sign '+').
     *
     * <li>
     * <p>
     * All other ASCII characters are converted into the 3-character string "%xy",
     * where xy is the two-digit hexadecimal representation of the character code
     *
     * <li>
     * <p>
     * All non-ASCII characters are encoded in two steps: first to a sequence of 2
     * or 3 bytes, using the UTF-8 algorithm; secondly each of these bytes is
     * encoded as ".xx".
     * </ul>
     *
     * @param s
     *          The string to be encoded
     * @return The encoded string
     */
    public static String encodeDotUrl(String s) {
        final int len = s.length();
        if (len == 0) {
            return "";
        }
        StringBuilder sbuf = new StringBuilder(len + len / 10);
        for (int i = 0; i < len; i++) {
            int ch = s.charAt(i);
            encodeDotUrl(sbuf, ch);
        }
        return sbuf.toString();
    }

    /**
     * Encode character for webbrowsers
     */
    private static void encodeDotUrl(StringBuilder sbuf, int ch) {
        if ('A' <= ch && ch <= 'Z') { // 'A'..'Z'
            sbuf.append((char) ch);
        } else if ('a' <= ch && ch <= 'z') { // 'a'..'z'
            sbuf.append((char) ch);
        } else if ('0' <= ch && ch <= '9') { // '0'..'9'
            sbuf.append((char) ch);
        } else if (ch == ' ') { // space
            sbuf.append('_'); // special for wiki syntax !
        } else if (ch == '-' || ch == '_' || ch == '.' || ch == ':') {
            sbuf.append((char) ch);
        } else if (ch <= 0x007f) { // other ASCII
            sbuf.append(hex1[ch]);
        } else if (ch <= 0x07FF) { // non-ASCII <= 0x7FF
            sbuf.append(hex1[0xc0 | (ch >> 6)]);
            sbuf.append(hex1[0x80 | (ch & 0x3F)]);
        } else { // 0x7FF < ch <= 0xFFFF
            sbuf.append(hex1[0xe0 | (ch >> 12)]);
            sbuf.append(hex1[0x80 | ((ch >> 6) & 0x3F)]);
            sbuf.append(hex1[0x80 | (ch & 0x3F)]);
        }
    }

    /**
     * Encode character for local file system
     *
     * @param sbuf
     * @param ch
     */
    private static void encodeLocalUrl(StringBuilder sbuf, int ch) {
        if ('A' <= ch && ch <= 'Z') { // 'A'..'Z'
            sbuf.append((char) ch);
        } else if ('a' <= ch && ch <= 'z') { // 'a'..'z'
            sbuf.append((char) ch);
        } else if ('0' <= ch && ch <= '9') { // '0'..'9'
            sbuf.append((char) ch);
        } else if (ch == ' ') { // space
            // sbuf.append('+');
            sbuf.append('_'); // special for wiki syntax !
        } else if (ch == '.' || ch == '-' || ch == '_' || ch == '/' || ch == '!' || ch == '~' || ch == '(' || ch == ')') {
            sbuf.append((char) ch);
            // } else if (ch == ':') {
            // sbuf.append('/');
        } else if (ch <= 0x007f) { // other ASCII
            sbuf.append(hex2[ch]);
        } else if (ch <= 0x07FF) { // non-ASCII <= 0x7FF
            sbuf.append(hex2[0xc0 | (ch >> 6)]);
            sbuf.append(hex2[0x80 | (ch & 0x3F)]);
        } else { // 0x7FF < ch <= 0xFFFF
            sbuf.append(hex2[0xe0 | (ch >> 12)]);
            sbuf.append(hex2[0x80 | ((ch >> 6) & 0x3F)]);
            sbuf.append(hex2[0x80 | (ch & 0x3F)]);
        }
    }

    /**
     * Encode the <i>wiki links title</i> into a URL for HTML hyperlinks (i.e.
     * create the <i>href</i> attribute representation for the <i>a</i> tag). To
     * get the behavior of the MediaWiki software, which is configured to convert
     * the first letter to upper case, the <code>firstCharacterAsUpperCase</code>
     * parameters must be set to <code>true</code>.
     *
     * @param wikiTitle
     *          the raw wiki title
     * @param firstCharacterAsUpperCase
     *          if <code>true</code> convert the first of the title to upper case
     * @return the encoded wiki title
     */
    public static String encodeTitleToUrl(String wikiTitle, boolean firstCharacterAsUpperCase) {
        return normaliseTitle(wikiTitle, true, ' ', firstCharacterAsUpperCase, true);
    }

    public static String encodeTitleDotUrl(String wikiTitle, boolean firstCharacterAsUpperCase) {
        int len = wikiTitle.length();
        if (len == 0) {
            return "";
        }
        for (int j = len - 1; j > 0; j--) {
            if (Character.isWhitespace(wikiTitle.charAt(j))) {
                continue;
            }
            len = j + 1;
            break;
        }

        boolean whiteSpace = true;
        StringBuilder buffer = new StringBuilder(len + len / 10);
        char ch;
        for (int i = 0; i < len; i++) {
            ch = wikiTitle.charAt(i);
            if (whiteSpace && Character.isWhitespace(ch)) {
                continue;
            }
            if (whiteSpace) {
                whiteSpace = false;
                if (firstCharacterAsUpperCase) {
                    Encoder.encodeDotUrl(buffer, Character.toUpperCase(ch));
                    continue;
                }
            }
            Encoder.encodeDotUrl(buffer, ch);
        }
        return buffer.toString();
    }

    /**
     * Encode name for local file system
     *
     */
    public static String encodeTitleLocalUrl(final String title) {
        String wikiTitle = title;
        int slashIndex = title.lastIndexOf('/');
        int len = wikiTitle.length();
        if (len == 0) {
            return "";
        }
        for (int j = len - 1; j > 0; j--) {
            if (Character.isWhitespace(wikiTitle.charAt(j))) {
                continue;
            }
            len = j + 1;
            break;
        }

        boolean colon = false;
        boolean whiteSpace = true;
        StringBuilder buffer = new StringBuilder(len + len / 10);
        char ch;
        for (int i = 0; i < len; i++) {
            ch = wikiTitle.charAt(i);
            if (whiteSpace && Character.isWhitespace(ch)) {
                continue;
            }
            if (whiteSpace) {
                whiteSpace = false;
                Encoder.encodeLocalUrl(buffer, Character.toUpperCase(ch));
                continue;
            }
            if (ch == '.' && slashIndex > i) {
                buffer.append(hex2[ch]);
                continue;
            }
            if (colon) {
                colon = false;
                Encoder.encodeLocalUrl(buffer, Character.toUpperCase(ch));
                continue;
            }
            Encoder.encodeLocalUrl(buffer, ch);
        }
        return buffer.toString();
    }



    /**
     * Normalises the given title, i.e. capitalises the first letter and
     * replaces whitespace with <tt>whitespaceChar</tt>, also multiple
     * consecutive whitespace characters will be replaced by one
     * <tt>whitespaceChar</tt>.
     *
     * @param value
     *            the string
     * @param underScoreIsWhitespace
     *            whether '_' should be seen as whitespace or not
     * @param whiteSpaceChar
     *            the character to replace whitespace with
     * @param firstCharacterAsUpperCase
     *          if <code>true</code> convert the first of the title to upper case
     *
     * @return a normalised title
     */
    public static String normaliseTitle(final String value, boolean underScoreIsWhitespace,
            char whiteSpaceChar, boolean firstCharacterAsUpperCase) {
        return normaliseTitle(value, underScoreIsWhitespace, whiteSpaceChar,
                firstCharacterAsUpperCase, false);
    }

    /**
     * Normalises the given title, i.e. capitalises the first letter and
     * replaces whitespace with <tt>whitespaceChar</tt>, also multiple
     * consecutive whitespace characters will be replaced by one
     * <tt>whitespaceChar</tt>. If <tt>encodeUrl</tt> is set, each character
     * will be mangled through {@link #encodeUrl(StringBuilder, int)} after all
     * the previous replacements!
     *
     * @param value
     *            the string
     * @param underScoreIsWhitespace
     *            whether '_' should be seen as whitespace or not
     * @param whiteSpaceChar
     *            the character to replace whitespace with
     * @param firstCharacterAsUpperCase
     *            if <code>true</code> convert the first of the title to upper
     *            case
     * @param encodeUrl
     *            finally mangles each character through
     *            {@link #encodeUrl(StringBuilder, int)}
     *
     * @return a normalised title
     */
    public static String normaliseTitle(final String value, boolean underScoreIsWhitespace,
            char whiteSpaceChar, boolean firstCharacterAsUpperCase, boolean encodeUrl) {
        final int len = value.length();
        StringBuilder sb = encodeUrl ? new StringBuilder(len + len / 10) : new StringBuilder(len);
        boolean whiteSpace = true;
        boolean first = firstCharacterAsUpperCase;
        for (int i = 0; i < len; ++i) {
            char c = value.charAt(i);
            if (Character.isWhitespace(c) || (underScoreIsWhitespace && c == '_')) {
                if (!whiteSpace) {
                    whiteSpace = true;
                    if (encodeUrl) {
                        Encoder.encodeUrl(sb, whiteSpaceChar);
                    } else {
                        sb.append(whiteSpaceChar);
                    }
                }
            } else {
                char ch = c;
                if (first) {
                    ch = Character.toUpperCase(c);
                    first = false;
                }
                if (encodeUrl) {
                    Encoder.encodeUrl(sb, ch);
                } else {
                    sb.append(ch);
                }
                whiteSpace = false;
            }
        }
        return sb.toString().trim();
    }

    /**
     * copy the text in the resulting buffer and escape special html characters
     * (&lt; &gt; &#34; ( &quot; ) &amp; &#39;)
     *
     * @param buffer
     *          add converted text into the resulting buffer
     */
    public static void encodeHtml(String text, StringBuilder buffer) {
        if (text.length() == 0) {
            return;
        }
        try {
            Utils.escapeXmlToBuffer(text, buffer, true, true, true);
        } catch (IOException e) {
            buffer.append("Error in encodeHtml: IOException");
        }
        //
        // final int len = text.length();
        // if (len == 0) {
        // return;
        // }
        // int currentIndex = 0;
        // int lastIndex = currentIndex;
        // while (currentIndex < len) {
        // switch (text.charAt(currentIndex++)) {
        // case '\r': //
        // if (lastIndex < (currentIndex - 1)) {
        // buffer.append(text.substring(lastIndex, currentIndex - 1));
        // lastIndex = currentIndex;
        // }
        // break;
        // case '<': // special html escape character
        // if (lastIndex < (currentIndex - 1)) {
        // buffer.append(text.substring(lastIndex, currentIndex - 1));
        // lastIndex = currentIndex;
        // }
        // buffer.append("&lt;");
        // break;
        // case '>': // special html escape character
        // if (lastIndex < (currentIndex - 1)) {
        // buffer.append(text.substring(lastIndex, currentIndex - 1));
        // lastIndex = currentIndex;
        // } else {
        // lastIndex++;
        // }
        // buffer.append("&gt;");
        // break;
        // case '&': // special html escape character
        // if (lastIndex < (currentIndex - 1)) {
        // buffer.append(text.substring(lastIndex, currentIndex - 1));
        // lastIndex = currentIndex;
        // } else {
        // lastIndex++;
        // }
        // buffer.append("&amp;");
        // break;
        // case '\'': // special html escape character
        // if (lastIndex < (currentIndex - 1)) {
        // buffer.append(text.substring(lastIndex, currentIndex - 1));
        // lastIndex = currentIndex;
        // } else {
        // lastIndex++;
        // }
        // buffer.append("&#39;");
        // break;
        // case '\"': // special html escape character
        // if (lastIndex < (currentIndex - 1)) {
        // buffer.append(text.substring(lastIndex, currentIndex - 1));
        // lastIndex = currentIndex;
        // } else {
        // lastIndex++;
        // }
        // buffer.append("&#34;");
        // break;
        // }
        // }
        // if (lastIndex < currentIndex) {
        // buffer.append(text.substring(lastIndex, currentIndex));
        // }
    }

    public static String encodeHtml(String text) {
        int len = text.length();
        if (len == 0) {
            return "";
        }
        StringBuilder buffer = new StringBuilder(len + len / 10);
        encodeHtml(text, buffer);
        return buffer.toString();
    }

    /**
     * Determines if the specified character may be part of a url
     */
    public static boolean isUrlIdentifierPart(char ch) {
        if (Character.isLetterOrDigit(ch)) {
            return true;
        }
        // ' (#39;) character is excluded because of its use in wiki bold and italic
        // markup
        final String test = "-_.!~*;/?:@#&=+$,%";
        return test.indexOf(ch) != (-1);
    }

    /**
     * Determines if the specified character may be part of a wiki plugin
     * identifier as other than the first character
     */
    public static boolean isWikiPluginIdentifierPart(char ch) {
        return Character.isLetterOrDigit(ch) || (ch == '_');
    }

    /**
     * Determines if the specified character may be part the first character of a
     * wiki plugin identifier
     */
    public static boolean isWikiPluginIdentifierStart(char ch) {
        return Character.isLetter(ch);
    }
}
