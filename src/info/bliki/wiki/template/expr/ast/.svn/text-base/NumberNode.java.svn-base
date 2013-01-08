package info.bliki.wiki.template.expr.ast;

/**
 * The basic node for a parsed expression string
 * 
 */
public class NumberNode extends ASTNode {

	protected boolean sign;

	protected NumberNode(final String value) {
		super(value);
		sign = false;
	}

	public void toggleSign() {
		sign = !sign;
	}

	public String getString() {
		if (sign) {
			return "-" + fStringValue;
		}
		return fStringValue;
	}

	public String toString() {
		if (sign) {
			return "-" + fStringValue;
		}
		return fStringValue;
	}

	public boolean isSign() {
		return sign;
	}

	public double doubleValue() {
		return Double.parseDouble(toString());
	}

	public boolean equals(Object obj) {
		if (obj instanceof NumberNode) {
			return fStringValue.equals(((NumberNode) obj).fStringValue) && sign == ((NumberNode) obj).sign;
		}
		return false;
	}

	public int hashCode() {
		if (sign) {
			return fStringValue.hashCode() * 17;
		}
		return fStringValue.hashCode();
	}
}
