package info.bliki.wiki.filter;

import info.bliki.wiki.model.Configuration;
import info.bliki.wiki.model.IWikiModel;
import info.bliki.wiki.tags.HTMLTag;
import info.bliki.wiki.tags.WPTag;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a wiki table (i.e. table syntax bordered by
 * <code>{| ..... |}</code> ). See: <a
 * href="http://meta.wikimedia.org/wiki/Help:Table">Help - Table</a>
 *
 */
public class WPTable extends WPTag implements PlainTextConvertable {

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
     * @param row
     * @return <tt>true</tt>
     */
    public boolean add(WPRow row) {
        return fRows.add(row);
    }

    /**
     * @param index
     * @return the row at the given index
     */
    public WPRow get(int index) {
        return fRows.get(index);
    }

    public int getRowsSize() {
        return fRows.size();
    }

    /**
     * @return table size (number of rows)
     */
    public int size() {
        return fRows.size();
    }

    @Override
    public void renderHTML(ITextConverter converter, Appendable buf, IWikiModel wikiModel) throws IOException {
        if (fRows.size() > 0) {
            boolean hasContentRow = false;
            if (Configuration.AVOID_PAGE_BREAK_IN_TABLE) {
                // TODO: integrate into table attributes?
                buf.append("\n<div style=\"page-break-inside: avoid;\">");
            }
            if (NEW_LINES) {
                buf.append("\n<table");
            } else {
                buf.append("<table");
            }
            HTMLTag.appendEscapedAttributes(buf, fAttributes);
            buf.append(">");
            for (WPRow row : fRows) {
                if (row.getType() != WPCell.CAPTION) {
                    hasContentRow = true;
                }
                row.renderHTML(converter, buf, wikiModel);
            }
            if (!hasContentRow) {
                WPRow row = new WPRow(Collections.singletonList(new WPCell(0)));
                row.renderHTML(converter, buf, wikiModel);
            }
            buf.append("</table>");
            if (Configuration.AVOID_PAGE_BREAK_IN_TABLE) {
                buf.append("</div>");
            }
        }
    }

    public int getNumColumns() {
        int maxCols = 0;
        WPRow row;
        for (WPRow fRow : fRows) {
            row = fRow;
            if (maxCols < row.getNumColumns()) {
                maxCols = row.getNumColumns();
            }
        }
        return maxCols;
    }

    @SuppressWarnings("unchecked")
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
            tt.fAttributes = new HashMap<>(fAttributes);
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

    @Override
    public void renderPlainText(ITextConverter converter, Appendable buf, IWikiModel wikiModel) throws IOException {
        for (WPRow row : fRows) {
            row.renderPlainText(converter, buf, wikiModel);
        }
    }
}
