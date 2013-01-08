package info.bliki.wiki.template.expr.ast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * A list of <code>ASTNode</code>'s which represents a parsed function.<br/s>
 * The head of the function (i.e. Ceil, Floor, Trunc, Plus, Times,...) is stored
 * in the 0-th index of the list.<br/> The arguments of the function are stored
 * in the 1...n-th index of the list.
 */
public class FunctionNode extends ASTNode implements java.util.List<ASTNode> {
	private ArrayList<ASTNode> fNodesList;

	public FunctionNode(final ASTNode head) {
		super(null);
		fNodesList = new ArrayList<ASTNode>(5);
		fNodesList.add(head);
	}

	public FunctionNode(final SymbolNode head, final ASTNode arg0) {
		super(null);
		fNodesList = new ArrayList<ASTNode>(3);
		fNodesList.add(head);
		fNodesList.add(arg0);
	}

	public FunctionNode(final SymbolNode head, final ASTNode arg0, final ASTNode arg1) {
		super(null);
		fNodesList = new ArrayList<ASTNode>(3);
		fNodesList.add(head);
		fNodesList.add(arg0);
		fNodesList.add(arg1);
	}

	public void add(final int index, final ASTNode element) {
		fNodesList.add(index, element);
	}

	/**
	 * Appends the node to the end of this list.
	 * 
	 * @param node
	 *          element to be appended to this list
	 * @return <tt>true</tt> (as specified by {@link Collection#add})
	 */
	public boolean add(final ASTNode node) {
		return fNodesList.add(node);
	}

	public boolean addAll(final Collection<? extends ASTNode> c) {
		return fNodesList.addAll(c);
	}

	public boolean addAll(final int index, final Collection<? extends ASTNode> c) {
		return fNodesList.addAll(index, c);
	}

	public void clear() {
		fNodesList.clear();
	}

	public boolean contains(final Object o) {
		return fNodesList.contains(o);
	}

	public boolean containsAll(final Collection<?> c) {
		return fNodesList.containsAll(c);
	}

	public void ensureCapacity(final int minCapacity) {
		fNodesList.ensureCapacity(minCapacity);
	}

	public boolean equals(final Object o) {
		if (o instanceof FunctionNode) {
			return fNodesList.equals(((FunctionNode) o).fNodesList);
		}
		return false;
	}

	/**
	 * Returns the node at the specified position in this list.
	 * 
	 * @param index
	 *          index of the ASTNode to return
	 * @return the ASTNode at the specified position in this list
	 * 
	 * @throws IndexOutOfBoundsException
	 */
	public ASTNode get(final int index) {
		return fNodesList.get(index);
	}

	public int hashCode() {
		return fNodesList.hashCode();
	}

	public int indexOf(final Object o) {
		return fNodesList.indexOf(o);
	}

	public boolean isEmpty() {
		return fNodesList.isEmpty();
	}

	public Iterator<ASTNode> iterator() {
		return fNodesList.iterator();
	}

	public int lastIndexOf(final Object o) {
		return fNodesList.lastIndexOf(o);
	}

	public ListIterator<ASTNode> listIterator() {
		return fNodesList.listIterator();
	}

	public ListIterator<ASTNode> listIterator(final int index) {
		return fNodesList.listIterator(index);
	}

	public ASTNode remove(final int index) {
		return fNodesList.remove(index);
	}

	public boolean remove(final Object o) {
		return fNodesList.remove(o);
	}

	public boolean removeAll(final Collection<?> c) {
		return fNodesList.removeAll(c);
	}

	public boolean retainAll(final Collection<?> c) {
		return fNodesList.retainAll(c);
	}

	public ASTNode set(final int index, final ASTNode element) {
		return fNodesList.set(index, element);
	}

	public int size() {
		return fNodesList.size();
	}

	public List<ASTNode> subList(final int fromIndex, final int toIndex) {
		return fNodesList.subList(fromIndex, toIndex);
	}

	public Object[] toArray() {
		return fNodesList.toArray();
	}

	public Object[] toArray(final Object[] a) {
		return fNodesList.toArray(a);
	}

	public String toString() {
		ASTNode temp = (ASTNode) fNodesList.get(0);
		final StringBuffer buf = new StringBuffer();
		if (temp == null) {
			buf.append("<null-tag>");
		} else {
			buf.append(temp.toString());
		}
		buf.append("[");
		for (int i = 1; i < size(); i++) {
			temp = (ASTNode) get(i);
			buf.append(temp == this ? "(this ListNode)" : String.valueOf(temp));
			if (i < size() - 1) {
				buf.append(", ");
			}
		}
		buf.append("]");
		return buf.toString();
	}

	public void trimToSize() {
		fNodesList.trimToSize();
	}

}
