package info.bliki.wiki.tags;

import info.bliki.wiki.filter.ITextConverter;
import info.bliki.wiki.model.Configuration;
import info.bliki.wiki.model.IWikiModel;
import info.bliki.wiki.tags.code.SourceCodeFormatter;
import info.bliki.wiki.tags.util.INoBodyParsingTag;

import java.io.IOException;
import java.util.Map;

/**
 * Allows source code to be syntax highlighted on the wiki pages.
 *
 * The syntax is similar to this <a
 * href="http://www.mediawiki.org/wiki/Extension:SyntaxHighlight_GeSHi">syntax
 * highlighting extension</a>.
 *
 */
public class SourceTag extends HTMLBlockTag implements INoBodyParsingTag {

    protected final static String SOURCE_START_1 = "<pre class=\"";
    protected final static String SOURCE_START_2 = "\">";
    protected final static String SOURCE_END = "</pre>";

    public SourceTag() {
        super("source", null);
    }

    @Override
    public Object clone() {
        SourceTag st = new SourceTag();
        return st;
    }

    @Override
    public void renderHTML(ITextConverter converter, Appendable writer, IWikiModel model) throws IOException {

        String content = getBodyString();
        if (content != null && content.length() > 0) {
            Map<String, String> attributes = getAttributes();
            String sourceCodeLanguage = attributes.get("lang");
            if (sourceCodeLanguage == null) {
                sourceCodeLanguage = getSourceType(content);
            }
            boolean show = model.showSyntax(sourceCodeLanguage);
            if (show) {
                String result;
                SourceCodeFormatter formatter = model.getCodeFormatterMap().get(sourceCodeLanguage);
                if (formatter != null) {
                    result = formatter.filter(content);
                    writer.append(SOURCE_START_1);
                    writer.append(sourceCodeLanguage);
                    writer.append(SOURCE_START_2);
                    writer.append(result);
                    // writer.append(replace(result));
                    writer.append(SOURCE_END);
                    return;
                }
            }
            // show plain source code text
            writer.append(SOURCE_START_1);
            writer.append(sourceCodeLanguage);
            writer.append(SOURCE_START_2);
            NowikiTag.copyPre(content, writer, true);
            writer.append(SOURCE_END);
        }
    }

    @Override
    public boolean isReduceTokenStack() {
        return true;
    }

    @Override
    public String getParents() {
        return Configuration.SPECIAL_BLOCK_TAGS;
    }

    /**
     * Determine the source code type by heuristics. If nothing found take the
     * <code>xml</code> formatter.
     *
     * @param src
     *          the source code snippet
     * @return the source code language
     */
    public static String getSourceType(String src) {
        int i = 0;
        while (i < src.length()) {
            if (!Character.isWhitespace(src.charAt(i))) {
                char ch = src.charAt(i);
                if (ch == '<') {
                    i++;
                    if (i < src.length() && src.charAt(i) == '?') {
                        i++;
                        while (i < src.length() && Character.isWhitespace(src.charAt(i++))) {
                        }
                        ch = src.charAt(i - 1);
                        if (i + 3 < src.length()) {
                            if (ch == 'x' || ch == 'X') {
                                if ((src.charAt(i) == 'm' || src.charAt(i) == 'M') && (src.charAt(i + 1) == 'l' || src.charAt(i + 1) == 'L')) {
                                    return "xml";
                                }
                            } else if (ch == 'p' || ch == 'P') {
                                if ((src.charAt(i) == 'h' || src.charAt(i) == 'H') && (src.charAt(i + 1) == 'p' || src.charAt(i + 1) == 'P')) {
                                    return "php";
                                }
                            }
                        }
                        return "xml";
                    }
                } else if (ch == 'p') {
                    if (i + 7 < src.length()) {
                        i++;
                        if (src.charAt(i) == 'a' && src.charAt(i + 1) == 'c' && src.charAt(i + 2) == 'k' && src.charAt(i + 3) == 'a'
                                && src.charAt(i + 4) == 'g' && src.charAt(i + 5) == 'e') {
                            // found 'package'
                            return "java";
                        }
                    }
                } else if (ch == 'i') {
                    if (i + 7 < src.length()) {
                        i++;
                        if (src.charAt(i) == 'm' && src.charAt(i + 1) == 'p' && src.charAt(i + 2) == 'o' && src.charAt(i + 3) == 'r'
                                && src.charAt(i + 4) == 't') {
                            // found 'import'
                            return "java";
                        }
                    }
                } else if (ch == 'f' || ch == 'F') {
                    i++;
                    if (i + 7 < src.length()) {
                        if ((src.charAt(i) == 'u' || src.charAt(i) == 'U') && (src.charAt(i + 1) == 'n' || src.charAt(i + 1) == 'N')
                                && (src.charAt(i + 2) == 'c' || src.charAt(i + 2) == 'C') && (src.charAt(i + 3) == 't' || src.charAt(i + 3) == 'T')
                                && (src.charAt(i + 4) == 'i' || src.charAt(i + 4) == 'I') && (src.charAt(i + 5) == 'o' || src.charAt(i + 5) == 'O')
                                && (src.charAt(i + 6) == 'n' || src.charAt(i + 6) == 'N')) {
                            // found function
                            return "abap";
                        }
                    }
                } else if (ch == 'm' || ch == 'M') {
                    i++;
                    if (i + 5 < src.length()) {
                        if ((src.charAt(i) == 'e' || src.charAt(i) == 'E') && (src.charAt(i + 1) == 't' || src.charAt(i + 1) == 'T')
                                && (src.charAt(i + 2) == 'h' || src.charAt(i + 2) == 'H') && (src.charAt(i + 3) == 'o' || src.charAt(i + 3) == 'O')
                                && (src.charAt(i + 4) == 'd' || src.charAt(i + 4) == 'D')) {
                            // found function
                            return "abap";
                        }
                    }
                } else if (ch == 'r' || ch == 'R') {
                    i++;
                    if (i + 5 < src.length()) {
                        if ((src.charAt(i) == 'e' || src.charAt(i) == 'E') && (src.charAt(i + 1) == 'p' || src.charAt(i + 1) == 'P')
                                && (src.charAt(i + 2) == 'o' || src.charAt(i + 2) == 'O') && (src.charAt(i + 3) == 'r' || src.charAt(i + 3) == 'R')
                                && (src.charAt(i + 4) == 't' || src.charAt(i + 4) == 'T')) {
                            // found report
                            return "abap";
                        }
                    }
                }
                break;
            }
            i++;
        }
        return "xml";
    }
}
