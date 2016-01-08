/*  Copyright (c) 2006-2007, Vladimir Nikic
 All rights reserved.

 Redistribution and use of this software in source and binary forms,
 with or without modification, are permitted provided that the following
 conditions are met:

 * Redistributions of source code must retain the above
 copyright notice, this list of conditions and the
 following disclaimer.

 * Redistributions in binary form must reproduce the above
 copyright notice, this list of conditions and the
 following disclaimer in the documentation and/or other
 materials provided with the distribution.

 * The name of HtmlCleaner may not be used to endorse or promote
 products derived from this software without specific prior
 written permission.

 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 POSSIBILITY OF SUCH DAMAGE.

 You can contact Vladimir Nikic by sending e-mail to
 nikic_vladimir@yahoo.com. Please include the word "HtmlCleaner" in the
 subject line.
 */

package info.bliki.htmlcleaner;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * <p>
 * Common utilities.
 * </p>
 *
 * Created by: Vladimir Nikic<br/>
 * Date: November, 2006.
 */
public class Utils {
    public static String VAR_START = "${";
    public static String VAR_END = "}";

    /**
     * Trims specified string from left.
     *
     * @param s
     */
    public static String ltrim(String s) {
        if (s == null) {
            return null;
        }

        int index = 0;
        int len = s.length();

        while (index < len && Character.isWhitespace(s.charAt(index))) {
            index++;
        }

        if (index == 0) {
            return s;
        }
        return (index >= len) ? "" : s.substring(index);
    }

    /**
     * Trims specified string from right.
     *
     * @param s
     */
    public static String rtrim(String s) {
        if (s == null) {
            return null;
        }

        int len = s.length();
        int index = len;

        while (index > 0 && Character.isWhitespace(s.charAt(index - 1))) {
            index--;
        }

        if (index == len) {
            return s;
        }
        return (index <= 0) ? "" : s.substring(0, index);
    }

    /**
     * Trims specified string from left and stop at <code>\n</code> character
     *
     * @param s
     */
    public static String ltrimNewline(String s) {
        if (s == null) {
            return null;
        }

        int index = 0;
        int len = s.length();

        while (index < len && Character.isWhitespace(s.charAt(index))) {
            if (s.charAt(index) == '\n') {
                break;
            }
            index++;
        }
        if (index == 0) {
            return s;
        }
        return (index >= len) ? "" : s.substring(index);
    }

    /**
     * Trims specified string from left and stops at <code>\n</code> character on
     * the left
     *
     * @param s
     */
    public static String trimNewlineLeft(String s) {
        if (s == null) {
            return null;
        }
        int leftIndex = 0;
        int len = s.length();
        int lastIndex = -1;

        while (leftIndex < len && Character.isWhitespace(s.charAt(leftIndex))) {
            if (s.charAt(leftIndex) == '\n') {
                lastIndex = leftIndex;
            }
            leftIndex++;
        }
        if (lastIndex >= 0) {
            leftIndex = lastIndex;
        }
        if (leftIndex >= len) {
            return "";
        }

        int rightIndex = len;
        while (rightIndex > 0 && Character.isWhitespace(s.charAt(rightIndex - 1))) {
            rightIndex--;
        }
        if (rightIndex <= 0) {
            return "";
        }
        if ((leftIndex == 0) && rightIndex == len) {
            return s;
        }
        return s.substring(leftIndex, rightIndex);

    }

    /**
     * Trims specified string from right and stops at <code>\n</code> character on
     * the right
     *
     * @param s
     */
    public static String trimNewlineRight(String s) {
        if (s == null) {
            return null;
        }
        int leftIndex = 0;
        int len = s.length();

        while (leftIndex < len && Character.isWhitespace(s.charAt(leftIndex))) {
            leftIndex++;
        }
        if (leftIndex >= len) {
            return "";
        }

        int rightIndex = len;
        int lastIndex = -1;
        while (rightIndex > 0 && Character.isWhitespace(s.charAt(rightIndex - 1))) {
            rightIndex--;
            if (s.charAt(rightIndex) == '\n') {
                lastIndex = rightIndex + 1;
            }
        }
        if (lastIndex >= 0) {
            rightIndex = lastIndex;
        }
        if (rightIndex <= 0) {
            return "";
        }
        if ((leftIndex == 0) && rightIndex == len) {
            return s;
        }
        return s.substring(leftIndex, rightIndex);

    }

    /**
     * Reads content from the specified URL with specified charset into string
     *
     * @param url
     * @param charset
     * @throws IOException
     */
    public static StringBuffer readUrl(URL url, String charset) throws IOException {
        StringBuffer buffer = new StringBuffer(1024);

        Object content = url.getContent();
        if (content instanceof InputStream) {
            InputStreamReader reader = new InputStreamReader((InputStream) content, charset);
            char[] charArray = new char[1024];
            try {
                int charsRead = 0;
                do {
                    charsRead = reader.read(charArray);
                    if (charsRead >= 0) {
                        buffer.append(charArray, 0, charsRead);
                    }
                } while (charsRead > 0);
            } finally {
                reader.close();
            }
        }

        return buffer;
    }

    public static boolean isHexadecimalDigit(char ch) {
        return Character.isDigit(ch) || ch == 'A' || ch == 'a' || ch == 'B' || ch == 'b' || ch == 'C' || ch == 'c' || ch == 'D'
                || ch == 'd' || ch == 'E' || ch == 'e' || ch == 'F' || ch == 'f';
    }

    /**
     * Escapes XML string.
     */
    public static String escapeXml(String s, boolean advanced, boolean recognizeUnicodeChars, boolean translateSpecialEntities) {
        if (s != null && s.length() != 0) {
            int len = s.length();
            StringBuilder result = new StringBuilder(len + len / 10);

            try {
                escapeXmlToBuffer(s, result, advanced, recognizeUnicodeChars, translateSpecialEntities);
            } catch (IOException e) {
                return "Error in escapeXml: IOException";
            }

            return result.toString();
        }

        return "";
    }

    /**
     * Escapes XML string into the given result buffer.
     */
    public static void escapeXmlToBuffer(String s, Appendable result, boolean advanced, boolean recognizeUnicodeChars,
            boolean translateSpecialEntities) throws IOException {
        escapeXmlToBuffer(s, result, advanced, recognizeUnicodeChars, translateSpecialEntities, false);
    }

    /**
     * Escapes XML string into the given result buffer.
     */
    public static void escapeXmlToBuffer(String s, Appendable result, boolean advanced, boolean recognizeUnicodeChars,
            boolean translateSpecialEntities, boolean plainText) throws IOException {
        if (s != null) {
            int len = s.length();
            for (int i = 0; i < len; i++) {
                char ch = s.charAt(i);

                if (ch == '&') {
                    if (recognizeUnicodeChars && (i < len - 1) && (s.charAt(i + 1) == '#')) {
                        int charIndex = i + 2;
                        String unicode = "";
                        while (charIndex < len
                                && (isHexadecimalDigit(s.charAt(charIndex)) || s.charAt(charIndex) == 'x' || s.charAt(charIndex) == 'X')) {
                            unicode += s.charAt(charIndex);
                            charIndex++;
                        }
                        if (charIndex == len || !"".equals(unicode)) {
                            try {
                                char unicodeChar = unicode.toLowerCase().startsWith("x") ? (char) Integer.parseInt(unicode.substring(1), 16)
                                        : (char) Integer.parseInt(unicode);
                                if ("&<>\'\"".indexOf(unicodeChar) < 0) {
                                    int replaceChunkSize = (charIndex < len && s.charAt(charIndex) == ';') ? unicode.length() + 1 : unicode.length();
                                    result.append(String.valueOf(unicodeChar));
                                    i += replaceChunkSize + 1;
                                } else {
                                    i = charIndex;
                                    result.append("&#" + unicode + ";");
                                }
                            } catch (NumberFormatException e) {
                                i = charIndex;
                                result.append("&amp;#" + unicode + ";");
                            }
                        } else {
                            result.append("&amp;");
                        }
                    } else {
                        if (translateSpecialEntities) {
                            // get following sequence of most 10 characters
                            String seq = s.substring(i, i + Math.min(10, len - i));
                            int semiIndex = seq.indexOf(';');
                            if (semiIndex > 0) {
                                String entity = seq.substring(1, semiIndex);
                                Integer code = (Integer) SpecialEntities.entities.get(entity);
                                if (code != null) {
                                    int entityLen = entity.length();
                                    if (recognizeUnicodeChars) {
                                        char unicodeChar = (char) code.intValue();
                                        if ("&<>\'\"".indexOf(unicodeChar) < 0) {
                                            result.append(String.valueOf(unicodeChar));
                                            i += entityLen + 1;
                                            continue;
                                        }
                                    }

                                    result.append("&#");
                                    result.append(Integer.toString(code.intValue()));
                                    result.append(";");
                                    i += entityLen + 1;
                                    continue;
                                }
                            }
                        }

                        if (advanced) {
                            String sub = s.substring(i);
                            if (sub.startsWith("&amp;")) {
                                // result.append("&amp;");
                                result.append("&#38;");
                                i += 4;
                            } else if (sub.startsWith("&apos;")) {
                                // result.append("&apos;");
                                result.append("&#39;");
                                i += 5;
                            } else if (sub.startsWith("&gt;")) {
                                // result.append("&gt;");
                                result.append("&#62;");
                                i += 3;
                            } else if (sub.startsWith("&lt;")) {
                                // result.append("&lt;");
                                result.append("&#60;");
                                i += 3;
                            } else if (sub.startsWith("&quot;")) {
                                // result.append("&quot;");
                                result.append("&#34;");
                                i += 5;
                            } else if (sub.startsWith("&nbsp;")) {
                                result.append("&#160;");
                                i += 5;
                            } else {
                                // result.append("&amp;");
                                result.append("&#38;");
                            }

                            continue;
                        } else if (plainText) {
                            String sub = s.substring(i);
                            if (sub.startsWith("&amp;")) {
                                result.append("&");
                                i += 4;
                            } else if (sub.startsWith("&#38;")) {
                                result.append("&");
                                i += 4;
                            } else if (sub.startsWith("&apos;")) {
                                result.append("\'");
                                i += 5;
                            } else if (sub.startsWith("&#39;")) {
                                result.append("\'");
                                i += 4;
                            } else if (sub.startsWith("&gt;")) {
                                result.append(">");
                                i += 3;
                            } else if (sub.startsWith("&#62;")) {
                                result.append(">");
                                i += 4;
                            } else if (sub.startsWith("&lt;")) {
                                result.append("<");
                                i += 3;
                            } else if (sub.startsWith("&#60;")) {
                                result.append("<");
                                i += 4;
                            } else if (sub.startsWith("&quot;")) {
                                result.append("\"");
                                i += 5;
                            } else if (sub.startsWith("&#34;")) {
                                result.append("\"");
                                i += 4;
                            } else if (sub.startsWith("&nbsp;")) {
                                result.append(" ");
                                i += 5;
                            } else if (sub.startsWith("&#160;")) {
                                result.append(" ");
                                i += 5;
                            } else {
                                result.append("&");
                            }

                            continue;
                        }

                        result.append("&amp;");
                    }
                } else if (plainText) {
                    result.append(ch);
                } else {
                    if (ch == '\'') {
                        // result.append("&apos;");
                        result.append("&#39;");
                    } else if (ch == '>') {
                        // result.append("&gt;");
                        result.append("&#62;");
                    } else if (ch == '<') {
                        // result.append("&lt;");
                        result.append("&#60;");
                    } else if (ch == '\"') {
                        // result.append("&quot;");
                        result.append("&#34;");
                    } else {
                        result.append(ch);
                    }
                }
            }
        }
    }

    public static String escapeXmlChars(String s) {
        if (s != null) {
            int len = s.length();
            StringBuilder result = new StringBuilder(len + len / 10);

            for (int i = 0; i < len; i++) {
                char ch = s.charAt(i);
                if (ch == '\'') {
                    result.append("&apos;");
                } else if (ch == '>') {
                    result.append("&gt;");
                } else if (ch == '<') {
                    result.append("&lt;");
                } else if (ch == '\"') {
                    result.append("&quot;");
                } else {
                    result.append(ch);
                }
            }

            return result.toString();
        }

        return null;
    }

    public static void appendAmpersandEscapedAttribute(StringBuilder writer, String attributeName, Map<String, String> tagAtttributes) {
        String attributeValue = tagAtttributes.get(attributeName);
        if (attributeValue != null) {
            if (writer.length() > 0) {
                writer.append("&amp;");
            }
            writer.append(attributeName);
            writer.append("=");
            try {
                writer.append(URLEncoder.encode(attributeValue, UTF_8.name()));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    public static void appendEscapedAttribute(Appendable writer, String attributeName, Map<String, String> tagAtttributes)
            throws IOException {
        String attributeValue = tagAtttributes.get(attributeName);
        if (attributeValue != null) {
            Utils.escapeXmlToBuffer(attributeValue, writer, false, false, false);
        }
    }

    /**
     * Evaluates string template for specified map of variables. Template string
     * can contain dynamic parts in the form of ${VARNAME}. Each such part is
     * replaced with value of the variable if such exists in the map, or with
     * empty string otherwise.
     *
     * @param template
     *          Template string
     * @param variables
     *          Map of variables (can be null)
     * @return Evaluated string
     */
    public static String evaluateTemplate(String template, Map<String, String> variables) {
        if (template == null) {
            return template;
        }

        StringBuffer result = new StringBuffer();

        int startIndex = template.indexOf(VAR_START);
        int endIndex = -1;

        while (startIndex >= 0 && startIndex < template.length()) {
            result.append(template.substring(endIndex + 1, startIndex));
            endIndex = template.indexOf(VAR_END, startIndex);

            if (endIndex > startIndex) {
                String varName = template.substring(startIndex + VAR_START.length(), endIndex);
                Object resultObj = variables != null ? variables.get(varName.toLowerCase()) : "";
                result.append(resultObj == null ? "" : resultObj.toString());
            }

            startIndex = template.indexOf(VAR_START, Math.max(endIndex + VAR_END.length(), startIndex + 1));
        }

        result.append(template.substring(endIndex + 1));

        return result.toString();
    }
}
