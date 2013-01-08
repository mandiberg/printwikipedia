package info.bliki.wiki.addon.tags;

import info.bliki.htmlcleaner.TagNode;
import info.bliki.htmlcleaner.Utils;
import info.bliki.wiki.filter.ITextConverter;
import info.bliki.wiki.model.IWikiModel;
import info.bliki.wiki.tags.NowikiTag;
import info.bliki.wiki.tags.util.INoBodyParsingTag;

import java.io.IOException;
import java.util.Map;


public class SampleTag extends NowikiTag implements INoBodyParsingTag {
	private final static String HEADER = "<div id=\"sample\">\n"
			+ "<a href=\"#\" id=\"show\" onclick=\"$(\'evalframe\').show();$(\'hide\').show();$(\'show\').hide();\" />Show Sample</a> \n"
			+ "<a href=\"#\" style=\"display: none;\" id=\"hide\" onclick=\"$(\'evalframe\').hide();;$(\'hide\').hide();$(\'show\').show();\" />Hide Sample</a><br />\n"
			+ "<iframe src=\"";

	private final static String FOOTER = "\" style=\"display: none;\" id=\"evalframe\" width=\"480\" height=\"160\" \n"
			+ "        scrolling=\"yes\" marginheight=\"0\" marginwidth=\"0\" frameborder=\"1\">\n"
			+ "  <p>You browser doesn\'t support IFRAMEs</p>\n" + "</iframe>\n" + "</div>";

	public SampleTag() {
		super("sample");
	}

	@Override
	public void renderHTML(ITextConverter converter, Appendable buf, IWikiModel model) throws IOException {

		TagNode node = this;
		Map<String, String> tagAtttributes = node.getAttributes();

		StringBuilder evalUrl = new StringBuilder(512);
		// sample input fields/textareas
		Utils.appendAmpersandEscapedAttribute(evalUrl, "ci", tagAtttributes);
		// sample actions
		Utils.appendAmpersandEscapedAttribute(evalUrl, "ca", tagAtttributes);
		buf.append(HEADER);
		// URL points to http://matheclipse.org/eval.jsp
		buf.append("../eval.jsp?");
		buf.append(evalUrl);
		// renderHTMLWithoutTag(converter, buf, model);
		buf.append(FOOTER);
	}

	@Override
	public boolean isReduceTokenStack() {
		return true;
	}
}
