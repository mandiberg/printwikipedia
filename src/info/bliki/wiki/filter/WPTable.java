package info.bliki.wiki.filter;

import info.bliki.wiki.model.Configuration;
import info.bliki.wiki.model.IWikiModel;
import info.bliki.wiki.tags.HTMLTag;
import info.bliki.wiki.tags.WPTag;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a wiki table (i.e. table syntax bordered by
 * <code>{| ..... |}</code> ). See: <a
 * href="http://meta.wikimedia.org/wiki/Help:Table">Help - Table</a>
 * 
 */
public class WPTable extends WPTag {

	private String fParams;

	private ArrayList<WPRow> fRows;

	private Map<String, String> fAttributes;

	public WPTable(ArrayList<WPRow> rows) {
		super("{||}");
		fRows = rows;
		fParams = null;
		fAttributes = null;
	}

	/**
	 * @return Returns the params.
	 */
	public String getParams() {
		return fParams;
	}

	/**
	 * @param params
	 *          The params to set.
	 */
	public void setParams(String params) {
		this.fParams = params;
		this.fAttributes = Util.getAttributes(params);
	}

	/**
	 * @param o
	 * @return
	 */
	public boolean add(WPRow row) {
		return fRows.add(row);
	}

	/**
	 * @param index
	 * @return
	 */
	public WPRow get(int index) {
		return fRows.get(index);
	}

	public int getRowsSize() {
		return fRows.size();
	}

	/**
	 * @return
	 */
	public int size() {
		return fRows.size();
	}

	@Override
	public void renderHTML(ITextConverter converter, Appendable buf, IWikiModel wikiModel) throws IOException {
		if (fRows.size() > 0) {
			buf.append("\n<div style=\"page-break-inside:	avoid;\">");
			if (NEW_LINES) {
				buf.append("\n<table");
			} else {
				buf.append("<table");
			}
			HTMLTag.appendEscapedAttributes(buf, fAttributes);
			buf.append(">");
			WPRow row;
			for (int i = 0; i < fRows.size(); i++) {
				row = fRows.get(i);
				row.renderHTML(converter, buf, wikiModel);
			}
			buf.append("</table></div>");
		}
	}

	public int getNumColumns() {
		int maxCols = 0;
		WPRow row;
		for (int i = 0; i < fRows.size(); i++) {
			row = fRows.get(i);
			if (maxCols < row.getNumColumns()) {
				maxCols = row.getNumColumns();
			}
		}
		return maxCols;
	}

	@Override
	public Object clone() {
		WPTable tt = (WPTable) super.clone();
		tt.fParams = fParams;
		if (fRows == null) {
			tt.fRows = null;
		} else {
			tt.fRows = (ArrayList<WPRow>) this.fRows.clone();
		}

		if (fAttributes == null) {
			tt.fAttributes = null;
		} else {
			tt.fAttributes = new HashMap<String, String>(fAttributes);
		}
		return tt;
	}

	@Override
	public boolean isReduceTokenStack() {
		return true;
	}

	@Override
	public String getParents() {
		return Configuration.SPECIAL_BLOCK_TAGS;
	}

	public void renderPlainText(ITextConverter converter, Appendable buf, IWikiModel wikiModel) throws IOException {
		if (fRows.size() > 0) {
			WPRow row;
			for (int i = 0; i < fRows.size(); i++) {
				row = fRows.get(i);
				row.renderPlainText(converter, buf, wikiModel);
			}
		}
	}
}