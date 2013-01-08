package info.bliki.wiki.tags;

import info.bliki.wiki.filter.ITextConverter;
import info.bliki.wiki.model.Configuration;
import info.bliki.wiki.model.IWikiModel;
import info.bliki.wiki.tags.util.INoBodyParsingTag;

import java.io.IOException;
import java.util.regex.Pattern;

/**
 * Wiki tag for the HTML <code>pre</code> Tag.
 * 
 * @see WPPreTag
 */
public class PreTag extends HTMLBlockTag implements INoBodyParsingTag {// implements
																																				// IPreBodyParsingTag
																																				// {

	public PreTag() {
		super("pre", Configuration.SPECIAL_BLOCK_TAGS);
	}

	@Override
	public void renderHTML(ITextConverter converter, Appendable writer, IWikiModel model) throws IOException {
		String content = getBodyString();
		if (content != null && content.length() > 0) {
			writer.append("\n<pre>");
			content = Configuration.NOWIKI_OPEN_PATTERN.matcher(content).replaceAll("");
			content = Configuration.NOWIKI_CLOSE_PATTERN.matcher(content).replaceAll("");
			NowikiTag.copyPre(content, writer, false);
			writer.append("</pre>");
		}
	}

	@Override
	public boolean isReduceTokenStack() {
		return true;
	}
}