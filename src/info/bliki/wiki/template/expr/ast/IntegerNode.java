package info.bliki.wiki.template.expr.ast;

/**
 * A node for a parsed integer string
 * 
 */
public class IntegerNode extends NumberNode {
	public final static IntegerNode C1 = new IntegerNode("1", 10);

	private final int fNumberFormat; 

	private final int fIntValue;

	public IntegerNode(final String value) {
		this(value, 10);
	}

	public IntegerNode(final String value, final int numberFormat) {
		super(value);
		fNumberFormat = numberFormat;
		fIntValue = 0;
	}

	public IntegerNode(final int intValue) {
		super(null);
		fNumberFormat = 10;
		fIntValue = intValue;
	}

	public String toString() {
		if (fStringValue == null) {
			if (sign) {
				return Integer.toString(fIntValue * (-1));
			}
			return Integer.toString(fIntValue);
		}
		if (sign) {
			return "-" + fStringValue;
		}
		return fStringValue;
	}

	public int getNumberFormat() {
		return fNumberFormat;
	}

	public int getIntValue() {
		return fIntValue;
	}

	public boolean equals(Object obj) {
		if (obj instanceof IntegerNode) {
			if (fStringValue == null) {
				return toString().equals(obj.toString());
			}
			return fStringValue.equals(((NumberNode) obj).fStringValue) && sign == ((NumberNode) obj).sign;
		}
		return false;
	}

	public int hashCode() {
		if (fStringValue == null) {
			return toString().hashCode();
		}
		return super.hashCode();
	}
}
