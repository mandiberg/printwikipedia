package info.bliki.html.wikipedia;

import info.bliki.htmlcleaner.TagNode;

import java.util.Map;

public class PreTag extends OpenCloseTag {

    protected static final String PRE_OPEN = "\n<pre>";

    protected static final String PRE_CLOSE = "\n</pre>\n";

    protected static final String SOURCE_OPEN = "\n<source>";

    protected static final String SOURCE_CLOSE = "\n</source>\n";

    public PreTag() {
        super(PRE_OPEN, PRE_CLOSE, false, false);
    }

    @Override
    public void open(TagNode node, StringBuilder resultBuffer) {
        resultBuffer.append(openStr);
    }

    @Override
    public void close(TagNode node, StringBuilder resultBuffer) {
        resultBuffer.append(closeStr);
    }

    @Override
    public void content(AbstractHTMLToWiki w, TagNode node, StringBuilder resultBuffer, boolean showWithoutTag) {
        Map<String, String> attr = node.getAttributes();
        if ("code".equals(attr.get("name"))) {
            String sourceStr = SOURCE_OPEN;
            setCloseStr(SOURCE_CLOSE);
            if (attr.get("class").equals("html") || attr.get("class").equals("xml")) {
                sourceStr = "\n<source lang=\"xml\">";
            } else if (attr.get("class").equals("java")) {
                sourceStr = "\n<source lang=\"java\">";
            } else if (attr.get("class").equals("javascript")) {
                sourceStr = "\n<source lang=\"javascript\">";
            }
            setOpenStr(sourceStr);
        }
        super.content(w, node, resultBuffer, showWithoutTag);
    }
}
