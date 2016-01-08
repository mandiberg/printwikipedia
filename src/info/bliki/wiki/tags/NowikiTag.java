package info.bliki.wiki.tags;

import info.bliki.wiki.filter.ITextConverter;
import info.bliki.wiki.model.IWikiModel;
import info.bliki.wiki.tags.util.INoBodyParsingTag;

import java.io.IOException;

/**
 * Wiki tag for escaping the wiki syntax.
 *
 */
public class NowikiTag extends HTMLTag implements INoBodyParsingTag {
    protected NowikiTag(String name) {
        super(name);
    }

    public NowikiTag() {
        super("nowiki");
    }

    /**
     * Copy the text in the resulting buffer and escape special html characters
     * (&lt; &gt; &quot; &amp; &#39;)
     *
     * @param replaceAmpersand
     *          replace the ampersand character with the HTML number entity
     */
    public static void copyPre(String text, Appendable writer, boolean replaceAmpersand) throws IOException {
        final int len = text.length();
        int currentIndex = 0;
        int lastIndex = currentIndex;
        while (currentIndex < len) {
            switch (text.charAt(currentIndex++)) {
            case '<': // special html escape character
                if (lastIndex < (currentIndex - 1)) {
                    writer.append(text, lastIndex, currentIndex - 1);
                    lastIndex = currentIndex;
                } else {
                    lastIndex++;
                }
                writer.append("&lt;");
                break;
            case '>': // special html escape character
                if (lastIndex < (currentIndex - 1)) {
                    writer.append(text, lastIndex, currentIndex - 1);
                    lastIndex = currentIndex;
                } else {
                    lastIndex++;
                }
                writer.append("&gt;");
                break;
            case '&': // special html escape character
                if (lastIndex < (currentIndex - 1)) {
                    writer.append(text, lastIndex, currentIndex - 1);
                    lastIndex = currentIndex;
                } else {
                    lastIndex++;
                }
                if (replaceAmpersand) {
                    writer.append("&#38;");
                } else {
                    writer.append("&");
                }
                break;
            case '\'': // special html escape character
                if (lastIndex < (currentIndex - 1)) {
                    writer.append(text, lastIndex, currentIndex - 1);
                    lastIndex = currentIndex;
                } else {
                    lastIndex++;
                }
                writer.append("&#39;");
                break;
            case '\"': // special html escape character
                if (lastIndex < (currentIndex - 1)) {
                    writer.append(text, lastIndex, currentIndex - 1);
                    lastIndex = currentIndex;
                } else {
                    lastIndex++;
                }
                writer.append("&#34;");
                break;
            }
        }
        if (lastIndex < (currentIndex)) {
            writer.append(text, lastIndex, currentIndex);
        }
    }

    /**
     * Copy the text in the resulting buffer and escape special html characters
     * (&lt; &gt; )
     */
    public static void copyMathLTGT(String text, Appendable writer) throws IOException {
        final int len = text.length();
        int currentIndex = 0;
        int lastIndex = currentIndex;
        while (currentIndex < len) {
            switch (text.charAt(currentIndex++)) {
            case '<': // special html escape character
                if (lastIndex < (currentIndex - 1)) {
                    writer.append(text, lastIndex, currentIndex - 1);
                    lastIndex = currentIndex;
                } else {
                    lastIndex++;
                }
                writer.append("&lt;");
                break;
            case '>': // special html escape character
                if (lastIndex < (currentIndex - 1)) {
                    writer.append(text, lastIndex, currentIndex - 1);
                    lastIndex = currentIndex;
                } else {
                    lastIndex++;
                }
                writer.append("&gt;");
                break;
            case '&': // special html escape character
                if (lastIndex < (currentIndex - 1)) {
                    writer.append(text, lastIndex, currentIndex - 1);
                    lastIndex = currentIndex;
                } else {
                    lastIndex++;
                }
                writer.append("&amp;");
                break;
            }
        }
        if (lastIndex < (currentIndex)) {
            writer.append(text, lastIndex, currentIndex);
        }
    }

    @Override
    public void renderHTML(ITextConverter converter, Appendable buf, IWikiModel model) throws IOException {
        String content = getBodyString();
        if (content != null && content.length() > 0) {
            copyPre(content, buf, true);
        }
    }

    @Override
    public boolean isReduceTokenStack() {
        return false;
    }
}
