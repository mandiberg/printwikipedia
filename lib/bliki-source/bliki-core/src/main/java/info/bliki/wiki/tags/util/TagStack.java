package info.bliki.wiki.tags.util;

import info.bliki.htmlcleaner.BaseToken;
import info.bliki.htmlcleaner.TagNode;
import info.bliki.htmlcleaner.TagToken;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;

/**
 * Stack which contains the currently generated HTML/Wiki tags from the parsing
 * process.
 * 
 */
public class TagStack extends ArrayList<TagToken> {

	/**
	 * List of generated HTML/Wiki tags.
	 */
	protected ArrayList<BaseToken> fNodeList;

	private static final long serialVersionUID = 7377721039394435077L;

	public TagStack() {
		this(40, 80);
	}

	public TagStack(int initialStackCapacity, int initialNodelistCapacity) {
		super(initialStackCapacity);
		fNodeList = new ArrayList<BaseToken>(initialNodelistCapacity);
	}

	/**
	 * Looks at the <code>TagToken</code> at the top of this stack without
	 * removing it from the stack.
	 * 
	 * @return the object at the top of this stack.
	 */
	public TagToken peek() {
		return get(size() - 1);
	}

	/**
	 * Removes the <code>TagToken</code> at the top of this stack and returns that
	 * <code>TagToken</code>.
	 * 
	 * @return The <code>TagToken</code> at the top of this stack.
	 */
	public TagToken pop() {
		if (size() > 0) {
			TagToken node = remove(size() - 1);
			if (size() == 0) {
				// last element in the list
				fNodeList.add(node);
			} else {
				TagToken topNode = peek();
				if (topNode instanceof TagNode) {
					((TagNode) topNode).addChild(node);
				} else {
					throw new UnsupportedOperationException("No TagNode available!");
				}
			}
			return node;
		}
		return null;
	}

	/**
	 * Pushes a <code>TagToken</code> item onto the top of this stack.
	 * 
	 * @param item
	 *          the <code>TagToken</code> item to be pushed onto this stack.
	 * @return <code>true</code> if this collection changed as a result of the
	 *         call
	 */
	public boolean push(TagToken item) {
		return add(item);
	}

	/**
	 * Pushes a new <code>TagNode</code> created from the nodeString onto the top
	 * of this stack.
	 * 
	 * @param nodeString
	 *          the string for the new <code>TagNode</code> item to be pushed onto
	 *          this stack.
	 * @return <code>true</code> if this collection changed as a result of the
	 *         call
	 */
	public boolean push(String nodeString) {
		return add(new TagNode(nodeString));
	}

	/**
	 * Return the internal list of nodes (size maybe 0)
	 * 
	 * @return
	 */
	public List<BaseToken> getNodeList() {
		return fNodeList;
	}

	public void append(BaseToken contentNode) {
		if (size() > 0) {
			TagToken node = peek();
			if (node instanceof TagNode) {
				((TagNode) node).addChild(contentNode);
			} else {
				throw new UnsupportedOperationException("No TagNode available for tag: " + node.getName());
			}
		} else {
			fNodeList.add(contentNode);
		}
	}

	public void append(TagStack stack) {
		for (int i = 0; i < stack.fNodeList.size(); i++) {
			append(stack.fNodeList.get(i));
		}
		// for (int i = 0; i < stack.size(); i++) {
		// append((BaseToken) stack.get(i));
		// }
	}

}
