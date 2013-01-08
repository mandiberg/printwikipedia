package info.bliki.wiki.filter;

import info.bliki.wiki.model.Configuration;
import info.bliki.wiki.model.IWikiModel;
import info.bliki.wiki.tags.WPTag;
import info.bliki.wiki.tags.util.TagStack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Wikipedia list. See <a
 * href="http://meta.wikimedia.org/wiki/Help:List"
 * >http://meta.wikimedia.org/wiki/Help:List</a>
 * 
 * @see info.bliki.wiki.filter.WPListElement
 * 
 */
public class WPList extends WPTag {
	public final static char DL_DD_CHAR = ':';
	public final static char DL_DT_CHAR = ';';
	public final static char OL_CHAR = '#';
	public final static char UL_CHAR = '*';
	// public final static char DL_CHAR = '+';

	private char[] fLastSequence;

	private InternalList fNestedElements;

	private ArrayList<InternalList> fInternalListStack;

	public static class InternalList extends ArrayList<Object> {

		/**
		 * 
		 */
		private static final long serialVersionUID = -3760843632697162014L;

		char fChar;

		public char getChar() {
			return fChar;
		}

		public InternalList(char ch) {
			super();
			fChar = ch;
		}
	}

	public WPList() {
		super("*#:;");
		fLastSequence = null;
		fNestedElements = null;
		fInternalListStack = new ArrayList<InternalList>();
	}

	public boolean isEmpty() {
		return fNestedElements == null;
	}

	/**
	 * 
	 * @param listElement
	 * @return
	 */
	public boolean add(WPListElement listElement) {
		char[] sequence = listElement.getSequence();
		int s1Length = 0;
		int s2Length = sequence.length;
		if (fLastSequence != null) {
			s1Length = fLastSequence.length;
		} else {
			fNestedElements = new InternalList(sequence[0]);
			fInternalListStack.add(fNestedElements);
		}
		InternalList list;
		int min = 0;
		int level = 0;
		if (s1Length > s2Length) {
			min = s2Length;
		} else {
			min = s1Length;
		}
		level = min;
		for (int i = 0; i < min; i++) {
			if (sequence[i] != fLastSequence[i]) {
				if ((sequence[i] == DL_DT_CHAR || sequence[i] == DL_DD_CHAR)
						&& (fLastSequence[i] == DL_DT_CHAR || fLastSequence[i] == DL_DD_CHAR)) {
					continue;
				}
				level = i;
				break;
			}
		}

		popListStack(level);

		if (level < s2Length) {
			// push stack
			for (int i = level; i < s2Length; i++) {
				list = new InternalList(sequence[i]);
				((List) fInternalListStack.get(fInternalListStack.size() - 1)).add(list);
				fInternalListStack.add(list);
			}
		}
		((List) fInternalListStack.get(fInternalListStack.size() - 1)).add(listElement);

		fLastSequence = sequence;
		return true;
	}

	private void popListStack(int level) {
		if (fInternalListStack.size() > level) {
			for (int i = fInternalListStack.size() - 1; i > level; i--) {
				fInternalListStack.remove(i);
			}
		}
	}

	@Override
	public void renderHTML(ITextConverter converter, Appendable buf, IWikiModel wikiModel) throws IOException {
		if (!isEmpty()) {
			fInternalListStack = null;

			for (int i = 0; i < fNestedElements.size(); i++) {
				Object element = fNestedElements.get(i);
				if (element instanceof InternalList) {
					InternalList subList = (InternalList) element;
					beginHTMLTag(buf, subList);
					renderSubListHTML(subList, converter, buf, wikiModel);
					// if (NEW_LINES) {
					// buf.append('\n');
					// }
					if (subList.fChar == UL_CHAR) {
						// bullet list
						buf.append("</ul>");
					} else if (subList.fChar == OL_CHAR) {
						// numbered list
						buf.append("</ol>");
					} else {
						// definition list
						buf.append("</dl>");
					}
				} else {
					TagStack stack = ((WPListElement) element).getTagStack();
					if (stack != null) {
						converter.nodesToText(stack.getNodeList(), buf, wikiModel);
					}
				}
			}
		}
	}

	private void beginHTMLTag(Appendable buf, InternalList subList) throws IOException {
		if (NEW_LINES) {
			buf.append('\n');
		}
		if (subList.fChar == UL_CHAR) {
			// bullet list
			buf.append("<ul>");
		} else if (subList.fChar == OL_CHAR) {
			// numbered list
			buf.append("<ol>");
		} else {
			// definition list
			buf.append("<dl>");
		}
	}

	private void endHTMLTag(Appendable buf, InternalList subList) throws IOException {
		if (subList.fChar == UL_CHAR) {
			// bullet list
			buf.append("</ul>");
		} else if (subList.fChar == OL_CHAR) {
			// numbered list
			buf.append("</ol>");
		} else {
			// definition list
			buf.append("</dl>");
		}
	}

	private void renderSubListHTML(InternalList list, ITextConverter converter, Appendable buf, IWikiModel wikiModel)
			throws IOException {
		if (list.size() == 0) {
			return;
		}

		char currentChar = list.fChar;
		char lastChar = ' ';
		if (currentChar == DL_DD_CHAR) {
			buf.append("\n<dd>");
		} else if (currentChar == DL_DT_CHAR) {
			buf.append("\n<dt>");
		} else {
			buf.append("\n<li>");
		}

		for (int i = 0; i < list.size(); i++) {
			Object element = list.get(i);

			if (element instanceof InternalList) {
				InternalList subList = (InternalList) element;
				beginHTMLTag(buf, subList);
				// recursive call:
				renderSubListHTML(subList, converter, buf, wikiModel);
				endHTMLTag(buf, subList);
			} else {
				TagStack stack = ((WPListElement) element).getTagStack();
				if (stack != null) {
					converter.nodesToText(stack.getNodeList(), buf, wikiModel);
				}
			}

			if ((i < list.size() - 1) && list.get(i + 1) instanceof WPListElement) {
				lastChar = currentChar;
				char[] temp = ((WPListElement) list.get(i + 1)).getSequence();
				currentChar = temp[temp.length - 1];
				switch (lastChar) {
				case DL_DD_CHAR:
					buf.append("</dd>");
					break;
				case DL_DT_CHAR:
					buf.append("</dt>");
					break;
				default:
					buf.append("</li>");
					break;
				}
				if (NEW_LINES) {
					buf.append('\n');
				}
				switch (currentChar) {
				case DL_DD_CHAR:
					buf.append("<dd>");
					break;
				case DL_DT_CHAR:
					buf.append("<dt>");
					break;
				default:
					buf.append("<li>");
					break;
				}
			}

		}

		if (currentChar == DL_DD_CHAR) {
			buf.append("</dd>");
		} else if (currentChar == DL_DT_CHAR) {
			buf.append("</dt>");
		} else {
			buf.append("</li>");
		}
	}

	@Override
	public Object clone() {
		WPList tt = (WPList) super.clone();
		if (fNestedElements == null) {
			tt.fNestedElements = null;
		} else {
			tt.fNestedElements = (InternalList) this.fNestedElements.clone();
		}
		tt.fInternalListStack = (ArrayList<InternalList>) this.fInternalListStack.clone();
		if (fLastSequence == null) {
			tt.fLastSequence = null;
		} else {
			tt.fLastSequence = new char[this.fLastSequence.length];
			System.arraycopy(this.fLastSequence, 0, tt.fLastSequence, 0, this.fLastSequence.length);
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

	public InternalList getNestedElements() {
		return fNestedElements;
	}

	private void renderSubListPlainText(InternalList list, ITextConverter converter, Appendable buf, IWikiModel wikiModel)
			throws IOException {
		if (list.size() > 0) {
			buf.append("\n");// <li>");
		}

		for (int i = 0; i < list.size(); i++) {
			Object element = list.get(i);

			if (element instanceof InternalList) {
				InternalList subList = (InternalList) element;
				// beginHTMLTag(buf, subList);
				// recursive call:
				renderSubListPlainText(subList, converter, buf, wikiModel);
				// endHTMLTag(buf, subList);
			} else {
				TagStack stack = ((WPListElement) element).getTagStack();
				if (stack != null) {
					converter.nodesToText(stack.getNodeList(), buf, wikiModel);
				}
				// wikiModel.appendStack(((WPListElement)
				// element).getTagStack());
			}

			if ((i < list.size() - 1) && list.get(i + 1) instanceof WPListElement) {
				if (NEW_LINES) {
					buf.append("\n");
					// buf.append("</li>\n<li>");
				} else {
					// buf.append("</li><lSi>");
				}
			}

		}
	}

	public void renderPlainText(ITextConverter converter, Appendable buf, IWikiModel wikiModel) throws IOException {
		if (!isEmpty()) {
			fInternalListStack = null;

			for (int i = 0; i < fNestedElements.size(); i++) {
				Object element = fNestedElements.get(i);
				if (element instanceof InternalList) {
					InternalList subList = (InternalList) element;
					renderSubListPlainText(subList, converter, buf, wikiModel);
				} else {
					TagStack stack = ((WPListElement) element).getTagStack();
					if (stack != null) {
						converter.nodesToText(stack.getNodeList(), buf, wikiModel);
					}
				}
			}

			if (NEW_LINES) {
				buf.append("\n");
			}
		}
	}

	private void toStringSubList(InternalList list, Appendable buf) throws IOException {
		for (int i = 0; i < list.size(); i++) {
			Object element = list.get(i);

			if (element instanceof InternalList) {
				InternalList subList = (InternalList) element;
				buf.append(subList.fChar);
				buf.append("-");
				toStringSubList(subList, buf);
			} else {
				buf.append(element.toString());
				buf.append("\n");
			}
		}

	}

	@Override
	public String toString() {
		if (!isEmpty()) {
			try {
				StringBuilder buf = new StringBuilder();
				for (int i = 0; i < fNestedElements.size(); i++) {
					Object element = fNestedElements.get(i);
					if (element instanceof InternalList) {
						InternalList subList = (InternalList) element;
						buf.append(subList.fChar);
						buf.append("-");
						toStringSubList(subList, buf);
					} else {
						buf.append(element.toString());
						buf.append("\n");
					}
				}
				return buf.toString();
			} catch (IOException e) {
				return "IOException";
			}
		}
		return "";

	}
}