package info.bliki.wiki.tags;

import info.bliki.wiki.filter.ITextConverter;
import info.bliki.wiki.model.Configuration;
import info.bliki.wiki.model.IWikiModel;
import info.bliki.wiki.tags.code.SourceCodeFormatter;
import info.bliki.wiki.tags.util.INoBodyParsingTag;

import java.io.IOException;
import java.util.Map;

/*
 * Allows source code to be syntax highlighted on the wiki pages.
 * 
 * The syntax is similar to this <a href="http://www.mediawiki.org/wiki/Extension:SyntaxHighlight_GeSHi">syntax highlighting extension</a>.
 *  
 */
public class SourceTag extends HTMLTag implements INoBodyParsingTag {

	protected final static String SOURCE_START = "<pre class=\"code\">";

	protected final static String SOURCE_END = "</pre>";

	public SourceTag() {
		super("source");
	}

	@Override
	public void renderHTML(ITextConverter converter, Appendable writer, IWikiModel model) throws IOException {

		String content = getBodyString();
		if (content != null && content.length() > 0) {
			Map<String, String> attributes = getAttributes();
			String sourceCodeLanguage = attributes.get("lang");
			if (sourceCodeLanguage != null) {
				boolean show = model.showSyntax(sourceCodeLanguage);
				if (show) {
					String result;
					SourceCodeFormatter formatter = model.getCodeFormatterMap().get(sourceCodeLanguage);
					if (formatter != null) {
						result = formatter.filter(content);
						writer.append(SOURCE_START);
						writer.append(result);
						// writer.append(replace(result));
						writer.append(SOURCE_END);
					}
				} else {
					// show plain source code text
					writer.append(SOURCE_START);
					writer.append(content);
					writer.append(SOURCE_END);
				}
			}
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
}
