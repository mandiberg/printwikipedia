package info.bliki.wiki.filter;

import info.bliki.wiki.model.IWikiModel;

import java.io.IOException;

public interface PlainTextConvertable {
    void renderPlainText(ITextConverter converter, Appendable buf, IWikiModel wikiModel) throws IOException;
}
