package info.bliki.wiki.addon.filter;

import info.bliki.htmlcleaner.BaseToken;
import info.bliki.htmlcleaner.ContentToken;
import info.bliki.htmlcleaner.EndTagToken;
import info.bliki.htmlcleaner.TagNode;
import info.bliki.wiki.addon.latex.PropertyManager;
import info.bliki.wiki.filter.ITextConverter;
import info.bliki.wiki.filter.WPCell;
import info.bliki.wiki.filter.WPList;
import info.bliki.wiki.filter.WPListElement;
import info.bliki.wiki.filter.WPRow;
import info.bliki.wiki.filter.WPTable;
import info.bliki.wiki.filter.WPList.InternalList;
import info.bliki.wiki.model.IWikiModel;
import info.bliki.wiki.model.ImageFormat;
import info.bliki.wiki.tags.BrTag;
import info.bliki.wiki.tags.HTMLTag;
import info.bliki.wiki.tags.WPParagraphTag;
import info.bliki.wiki.tags.WPTag;
import info.bliki.wiki.tags.util.TagStack;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * A converter which renders the internal node representation as LaTeX text
 * 
 */
public class LaTeXConverter implements ITextConverter {

	public static final String TEMP_BRACES = "TEMP-BRACES";

	/**
	 * Escaped characters which can be used inside LaTeX
	 */
	static final String[] CHAR_REPLACEMENTS = new String[] { "\\textbackslash" + TEMP_BRACES, "\\textasciitilde" + TEMP_BRACES,
			"\\textasciicircum" + TEMP_BRACES, "\\textbar" + TEMP_BRACES, "\\textless" + TEMP_BRACES, "\\textgreater" + TEMP_BRACES,
			"\\$", "$\\{$", "$\\}$", "\\%", "\\&", "\\#", "\\_{}", "''" };

	/**
	 * Original chars which have to be escaped, because they have a special
	 * meaning in LaTeX
	 */
	static final String CHARS_TO_REPLACE = "\\~^|<>${}%&#_\"";

	static final String[] STRING_REPLACEMENTS = new String[] { "\\dots{}" };

	/** replacements for whole Strings */
	static final String[] STRINGS_TO_REPLACE = new String[] { "..." };

	public LaTeXConverter() {
	}

	public void nodesToText(List<? extends Object> nodes, Appendable resultBuffer, IWikiModel model) throws IOException {
		if (nodes != null && !nodes.isEmpty()) {
			Iterator<? extends Object> childrenIt = nodes.iterator();
			while (childrenIt.hasNext()) {
				Object item = childrenIt.next();
				if (item != null) {
					if (item instanceof List) {
						nodesToText((List) item, resultBuffer, model);
					} else if (item instanceof ContentToken) {
						ContentToken contentToken = (ContentToken) item;
						resultBuffer.append(texEscapeString(contentToken.getContent()));
					} else if (item instanceof HTMLTag) {
						renderLaTeX(((HTMLTag) item), this, resultBuffer, model);
					} else if (item instanceof TagNode) {
						TagNode node = (TagNode) item;
						Map map = node.getObjectAttributes();
						if (map != null && map.size() > 0) {
							Object attValue = map.get("wikiobject");
							if (attValue instanceof ImageFormat) {
								// imageNodeToText(node, (ImageFormat) attValue, resultBuffer,
								// model);
							}
						} else {
							nodeToLaTeX(node, resultBuffer, model);
						}
					} else if (item instanceof EndTagToken) {
						//
						if (item instanceof BrTag) {
							resultBuffer.append(PropertyManager.get("LineBreak"));
						}
					}
				}
			}
		}
	}

	protected void nodeToLaTeX(TagNode node, Appendable resultBuffer, IWikiModel model) throws IOException {
		String name = node.getName();
		if (HTMLTag.NEW_LINES) {
			if (name.equals("div") || name.equals("p") || name.equals("table") || name.equals("ul") || name.equals("ol")
					|| name.equals("li") || name.equals("th") || name.equals("tr") || name.equals("td") || name.equals("pre")) {
				resultBuffer.append('\n');
			}
		}

		List children = node.getChildren();
		if (children.size() != 0) {
			nodesToText(children, resultBuffer, model);
		}
	}

	public void imageNodeToText(TagNode imageTagNode, ImageFormat imageFormat, Appendable resultBuffer, IWikiModel model)
			throws IOException {
		Map<String, String> map = imageTagNode.getAttributes();
		String caption = imageFormat.getCaption();
		String location = imageFormat.getLocation();
		String type = imageFormat.getType();
		int pxSize = imageFormat.getWidth();
		if (pxSize != -1) {
			resultBuffer.append("<div style=\"");
			resultBuffer.append("width:");
			resultBuffer.append(Integer.toString(pxSize));
			resultBuffer.append("px");
			resultBuffer.append("\">");
		}
		resultBuffer.append("<a class=\"internal\" href=\"");
		resultBuffer.append(map.get("href").toString());
		resultBuffer.append("\" title=\"");
		resultBuffer.append(caption);
		resultBuffer.append("\">");

		resultBuffer.append("<img src=\"");
		resultBuffer.append(map.get("src").toString());
		resultBuffer.append("\"");

		if (caption != null && caption.length() > 0) {
			resultBuffer.append(" alt=\"").append(caption).append("\"");
			resultBuffer.append(" title=\"").append(caption).append("\"");
		}

		resultBuffer.append(" class=\"");
		if (location != null) {
			resultBuffer.append(" location-").append(location);
		}
		if (type != null) {
			resultBuffer.append(" type-").append(type);
		}
		resultBuffer.append("\"");

		if (pxSize != -1) {
			resultBuffer.append(" width=\"").append(Integer.toString(pxSize)).append("px\"");
		}
		resultBuffer.append(" />\n");

		resultBuffer.append("</a>");
		List children = imageTagNode.getChildren();
		if (children.size() != 0) {
			nodesToText(children, resultBuffer, model);
		}

		if (pxSize != -1) {
			resultBuffer.append("</div>\n");
		}
	}

	/**
	 * @return The String 's' with all special characters escaped and linebreaks
	 *         converted to "\\"
	 */
	public static String verbToText(String s) {
		String text = texEscapeString(s);
		text = text.replace("\r", "");
		text = text.replace("\n", PropertyManager.get("LineBreak"));
		return text;
	}

	/**
	 * Escapes characters or whole strings that otherwise have a special meaning
	 * in tex.
	 */
	public static String texEscapeString(String s) {
		for (int i = 0; i < LaTeXConverter.CHARS_TO_REPLACE.length(); i++) {
			s = s.replace(LaTeXConverter.CHARS_TO_REPLACE.charAt(i) + "", LaTeXConverter.CHAR_REPLACEMENTS[i]);
		}
		for (int i = 0; i < LaTeXConverter.STRINGS_TO_REPLACE.length; i++) {
			s = s.replace(LaTeXConverter.STRINGS_TO_REPLACE[i], LaTeXConverter.STRING_REPLACEMENTS[i]);
		}
		s = s.replace(LaTeXConverter.TEMP_BRACES, "{}");
		return s;
	}

	private String getColumnFormat(int cols) {
		StringBuilder res = new StringBuilder(3 + 2 * cols);
		res.append("{|");
		for (int i = 0, n = cols; i < n; i++)
			res.append("l|");
		res.append('}');
		return res.toString();
	}

	public void renderLaTeX(HTMLTag node, ITextConverter converter, Appendable _out, IWikiModel model) throws IOException {
		String name = node.getName();
		if (HTMLTag.NEW_LINES) {
			if (name.equals("div") || name.equals("p") || name.equals("table") || name.equals("ul") || name.equals("ol")
					|| name.equals("li") || name.equals("th") || name.equals("tr") || name.equals("td") || name.equals("pre")) {
				_out.append('\n');
			}
		}
		if (node instanceof WPTable) {
			renderLaTeX((WPTable) node, converter, _out, model);
		} else if (node instanceof WPList) {
			WPList list = (WPList) node;
			if (!list.isEmpty()) {

				// fInternalListStack = null;
				
				for (int i = 0; i < list.getNestedElements().size(); i++) {
					Object element = list.getNestedElements().get(i);
					if (element instanceof WPList.InternalList) {
						WPList.InternalList subList = (WPList.InternalList) element;
						beginLaTeXTag(_out, subList);
						renderSubListLaTeX(subList, converter, _out, model);
						endLaTeXTag(_out, subList);
					} else {
						TagStack stack = ((WPListElement) element).getTagStack();
						if (stack != null) {
							converter.nodesToText(stack.getNodeList(), _out, model);
						}
						// wikiModel.appendStack(((WPListElement)
						// element).getTagStack());
					}
				}
			}
		} else if (node instanceof WPParagraphTag) {
			List children = node.getChildren();
			if (children.size() != 0) {
				converter.nodesToText(children, _out, model);
				_out.append(PropertyManager.get("Paragraph.End"));
			}
		} else if (node instanceof WPTag) {
			if (name.equals("i")) {
				_out.append(PropertyManager.get("Font.Italic.On") + "{}");
			} else if (name.equals("em")) {
				_out.append(PropertyManager.get("Font.Italic.On") + "{}");
			} else if (name.equals("b")) {
				_out.append(PropertyManager.get("Font.Bold.On") + "{}");
			} else if (name.equals("strong")) {
				_out.append(PropertyManager.get("Font.Bold.On") + "{}");
			} else if (name.equals("h1")) {
				_out.append(PropertyManager.get("Heading.3"));
			} else if (name.equals("h2")) {
				_out.append(PropertyManager.get("Heading.3"));
			} else if (name.equals("h3")) {
				_out.append(PropertyManager.get("Heading.2"));
			} else if (name.equals("h4")) {
				_out.append(PropertyManager.get("Heading.1"));
			} else if (name.equals("h5")) {
				_out.append(PropertyManager.get("Heading.0"));
			} else if (name.equals("h6")) {
				_out.append(PropertyManager.get("Heading.0"));
			}

			List children = node.getChildren();
			if (children.size() != 0) {
				converter.nodesToText(children, _out, model);
			}
			if (name.equals("i")) {
				_out.append(PropertyManager.get("Font.Italic.Off") + "{}");
			} else if (name.equals("em")) {
				_out.append(PropertyManager.get("Font.Italic.Off") + "{}");
			} else if (name.equals("b")) {
				_out.append(PropertyManager.get("Font.Bold.Off") + "{}");
			} else if (name.equals("strong")) {
				_out.append(PropertyManager.get("Font.Bold.Off") + "{}");
			} else if ((name.equals("h1")) || (name.equals("h2")) || (name.equals("h3")) || (name.equals("h4"))) {
				_out.append(PropertyManager.get("Heading.End"));
			} else if (name.equals("h5")) {
				_out.append(PropertyManager.get("Paragraph.End"));
			} else if (name.equals("h6")) {
				_out.append(PropertyManager.get("Paragraph.End"));
			}
		} else if (name.equals("nowiki")) {
			renderBodyString(node, converter, _out, model);
		} else if (name.equals("pre")) {
			String content = node.getBodyString();
			if (content != null && content.length() > 0) {
				boolean inTable = node.getParent() instanceof WPTable;
				if (inTable) {
					_out.append(PropertyManager.get("CodeBlock.inTable.Begin"));
					_out.append(LaTeXConverter.verbToText(content));
					_out.append(PropertyManager.get("CodeBlock.inTable.End"));
				} else {
					_out.append(PropertyManager.get("CodeBlock.Begin"));
					_out.append(content);
					_out.append(PropertyManager.get("CodeBlock.End"));
				}
			}
		} else if (name.equals("source")) {
			String content = node.getBodyString();
			if (content != null && content.length() > 0) {
				boolean inTable = node.getParent() instanceof WPTable;
				if (inTable) {
					_out.append(PropertyManager.get("CodeBlock.inTable.Begin"));
					_out.append(LaTeXConverter.verbToText(content));
					_out.append(PropertyManager.get("CodeBlock.inTable.End"));
				} else {
					_out.append(PropertyManager.get("CodeBlock.Begin"));
					_out.append(content);
					_out.append(PropertyManager.get("CodeBlock.End"));
				}
			}
		} else if (name.equals("math")) {
			String content = node.getBodyString();
			if (content != null && content.length() > 0) {
				_out.append("\\begin{math}\n");
				_out.append(content + "\n");
				_out.append("\\end{math}");
			}
		} else {
			// Map tagAtttributes = node.getAttributes();
			//
			// appendAttributes(buf, tagAtttributes);
			List children = node.getChildren();
			if (children.size() != 0) {
				converter.nodesToText(children, _out, model);
			}
		}
	}

	private void renderSubListLaTeX(InternalList list, ITextConverter converter, Appendable _out, IWikiModel wikiModel)
			throws IOException {
		if (list.size() > 0) {
			_out.append(PropertyManager.get("List.Item.Begin"));
		}
		String itemStr = PropertyManager.get("List.Item.Begin") + PropertyManager.get("List.Item.End");
		for (int i = 0; i < list.size(); i++) {
			Object element = list.get(i);

			if (element instanceof InternalList) {
				InternalList subList = (InternalList) element;
				beginLaTeXTag(_out, subList);
				// recursive call:
				renderSubListLaTeX(subList, converter, _out, wikiModel);
				_out.append(PropertyManager.get("List.Item.End"));
			} else {
				TagStack stack = ((WPListElement) element).getTagStack();
				converter.nodesToText(stack.getNodeList(), _out, wikiModel);
				// wikiModel.appendStack(((WPListElement)
				// element).getTagStack());
			}

			if ((i < list.size() - 1) && list.get(i + 1) instanceof WPListElement) {
				// open new lists as required by 'descr'
				_out.append(itemStr);
			}

		}
		if (list.size() > 0) {
			_out.append(PropertyManager.get("List.Item.End"));
		}
	}

	public void renderBodyString(HTMLTag node, ITextConverter converter, Appendable _out, IWikiModel model) throws IOException {
		String content = node.getBodyString();
		if (content != null && content.length() > 0) {
			_out.append(content);
		}
	}

	public void renderLaTeX(WPTable table, ITextConverter converter, Appendable _out, IWikiModel model) throws IOException {
		int cols = table.getNumColumns();
		_out.append(PropertyManager.get("Table.Begin", getColumnFormat(cols)));
		WPRow row;
		for (int i = 0; i < table.getRowsSize(); i++) {
			if (i == 0) {
				_out.append(PropertyManager.get("Table.Row.Begin.First"));
			}
			row = table.get(i);
			renderLaTeX(row, converter, _out, model, cols);
			if (i == 0 || i == table.getRowsSize() - 1)
				_out.append(PropertyManager.get("Table.Row.End.FirstOrLast"));
			else
				_out.append(PropertyManager.get("Table.Row.End"));
		}
		_out.append(PropertyManager.get("Table.End"));
	}

	public void renderLaTeX(WPRow row, ITextConverter converter, Appendable _out, IWikiModel wikiModel, int maxCols)
			throws IOException {
		if (row.size() > 0) {
			int missingCols = maxCols - row.size();
			WPCell cell;
			for (int i = 0; i < row.size(); i++) {
				cell = row.get(i);
				renderLaTeX(cell, converter, _out, wikiModel);
				if (i < row.size() - 1) {
					_out.append(PropertyManager.get("Table.ColumnSep"));
				}
			}
			while (missingCols-- > 0) {
				_out.append(PropertyManager.get("Table.ColumnSep"));
			}
		}
	}

	public void renderLaTeX(WPCell cell, ITextConverter converter, Appendable _out, IWikiModel wikiModel) throws IOException {
		TagStack tagStack = cell.getTagStack();
		int cellType = cell.getType();
		if (tagStack != null) {
			boolean hasLBrks = false;
			List<BaseToken> list = tagStack.getNodeList();
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i) instanceof BrTag) {
					hasLBrks = true;
					break; // for
				}
			}

			if (cellType == WPCell.TH) {
				_out.append(PropertyManager.get("Table.HeaderCell.Begin"));
			}
			if (hasLBrks) {
				_out.append(PropertyManager.get("Table.CellWithLBreaks.Begin"));
			}
			converter.nodesToText(tagStack.getNodeList(), _out, wikiModel);

			if (hasLBrks) {
				_out.append(PropertyManager.get("Table.CellWithLBreaks.End"));
			}
			if (cellType == WPCell.TH) {
				_out.append(PropertyManager.get("Table.HeaderCell.End"));
			}

		}
	}

	private void beginLaTeXTag(Appendable _out, WPList.InternalList subList) throws IOException {
		if (subList.getChar() == '*') {
			// bullet list
			_out.append(PropertyManager.get("List.Unnumbered.Begin"));
		} else if (subList.getChar() == '#') {
			// numbered list
			_out.append(PropertyManager.get("List.Numbered.Begin"));
		}
	}

	private void endLaTeXTag(Appendable _out, WPList.InternalList subList) throws IOException {
		if (subList.getChar() == '*') {
			// bullet list
			_out.append(PropertyManager.get("List.Unnumbered.End"));
		} else if (subList.getChar() == '#') {
			// numbered list
			_out.append(PropertyManager.get("List.Numbered.End"));
		}
	}

	public boolean noLinks() {
		return true;
	}
}
