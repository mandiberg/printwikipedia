package info.bliki.wiki.tags;

import info.bliki.Messages;
import info.bliki.wiki.filter.ITextConverter;
import info.bliki.wiki.filter.StringPair;
import info.bliki.wiki.model.ITableOfContent;
import info.bliki.wiki.model.IWikiModel;
import info.bliki.wiki.tags.util.IBodyTag;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Wiki tag for rendering the &quot;table of contents&quot inside a wikipedia
 * article
 * 
 */
public class TableOfContentTag extends HTMLTag implements IBodyTag, ITableOfContent {
	private List<Object> fTableOfContent = null;

	private boolean fShowToC;

	private boolean fIsTOCIdentifier;

	public TableOfContentTag(String name) {
		super(name);
		this.fShowToC = false;
		this.fIsTOCIdentifier = false;
	}

	@Override
	public void renderHTML(ITextConverter converter, Appendable writer, IWikiModel model) throws IOException {
		// writer.append("<div id=\"tableofcontent\" />");
		if (fShowToC && fTableOfContent != null && fTableOfContent.size() > 0) {
			String contentString = Messages.getString(model.getResourceBundle(), Messages.WIKI_TAGS_TOC_CONTENT);
			writer.append("<table id=\"toc\" class=\"toc\" summary=\"");
			writer.append(contentString);
			writer.append("\">\n" + "<tr>\n" + "<td>\n" + "<div id=\"toctitle\">\n" + "<h2>");
			writer.append(contentString);
			writer.append("</h2>\n</div>");
			// writer.append("<table id=\"toc\" border=\"0\"><tr><th>");
			// writer.append("</th></tr><tr><td>");
			renderToC(writer, fTableOfContent, 0);
			writer.append("</td></tr></table><hr/>\n");
		}
	}

	private void renderToC(Appendable writer, List<Object> toc, int level) throws IOException {
		writer.append("\n<ul>");
		boolean counted = false;
		for (int i = 0; i < toc.size(); i++) {
			if (toc.get(i) instanceof StringPair) {
				if (!counted) {
					level++;
					counted = true;
				}
				StringPair pair = (StringPair) toc.get(i);
				String head = pair.getFirst();
				String anchor = pair.getSecond();
				writer.append("\n<li class=\"toclevel-");
				writer.append(Integer.toString(level));
				writer.append("\"><a href=\"#");
				writer.append(anchor);
				writer.append("\">");
				writer.append(head);
				writer.append("</a>\n</li>");
			} else {
				renderToC(writer, (List<Object>) toc.get(i), level);
			}
		}
		writer.append("\n</ul>");
	}

	@Override
	public boolean isReduceTokenStack() {
		return false;
	}

	public boolean isShowToC() {
		return fShowToC;
	}

	/**
	 * Enable or disable the rendering of the &quot;table of content&quot;
	 * 
	 * @param showToC
	 *          if <code>true</code> render the &quot;table of content&quot;
	 */
	public void setShowToC(boolean showToC) {
		fShowToC = showToC;
	}

	@Override
	public Object clone() {
		TableOfContentTag tocTag = (TableOfContentTag) super.clone();
		tocTag.fShowToC = this.fShowToC;
		tocTag.fIsTOCIdentifier = this.fIsTOCIdentifier;
		if (this.fTableOfContent == null) {
			tocTag.fTableOfContent = null;
		} else {
			tocTag.fTableOfContent = new ArrayList<Object>(this.fTableOfContent);
		}
		return tocTag;
	}

	public List<Object> getTableOfContent() {
		if (fTableOfContent == null) {
			fTableOfContent = new ArrayList<Object>();
		}
		return fTableOfContent;
	}

	public boolean isTOCIdentifier() {
		return fIsTOCIdentifier;
	}

	public void setTOCIdentifier(boolean isTOCIdentifier) {
		fIsTOCIdentifier = isTOCIdentifier;
	}

}