package info.bliki.wiki.template.expr.operator;


public abstract class Operator {
	protected String fFunctionName;

	protected String fOperatorString;

	protected int fPrecedence;

	public Operator(final String oper, final String functionName, final int precedence) {
		fOperatorString = oper;
		fFunctionName = functionName;
		fPrecedence = precedence;
	}

	public boolean equals(final Object obj) {
		if (obj instanceof Operator) {
			return fFunctionName.equals(((Operator) obj).fFunctionName);
		}
		return false;
	}

	/**
	 * @return the name of the head of the associated function
	 */
	public String getFunctionName() {
		return fFunctionName;
	}

	/**
	 * @return the operator string of this operator
	 */
	public String getOperatorString() {
		return fOperatorString;
	}

	/**
	 * @return the precedence of this operator
	 */
	public int getPrecedence() {
		return fPrecedence;
	}

	/**
	 * @return the hashCode of the function name
	 */
	public int hashCode() {
		return fFunctionName.hashCode();
	}

	public String toString() {
		return "["+fFunctionName + "," + fOperatorString + "," + fPrecedence+"]";
	}

}
