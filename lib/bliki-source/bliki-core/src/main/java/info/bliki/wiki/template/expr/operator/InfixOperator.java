package info.bliki.wiki.template.expr.operator;

import info.bliki.wiki.template.expr.ast.ASTNode;
import info.bliki.wiki.template.expr.ast.IParserFactory;

public class InfixOperator extends Operator {
	private int fGrouping;

	public final static int NONE = 0;

	public final static int RIGHT_ASSOCIATIVE = 1;

	public final static int LEFT_ASSOCIATIVE = 2;

	public InfixOperator(final String oper, final String functionName,
			final int precedence, final int grouping) {
		super(oper, functionName, precedence);
		fGrouping = grouping;
	}

	/**
	 * Return the grouping of the Infix-Operator (i.e. NONE,LEFT_ASSOCIATIVE,
	 * RIGHT_ASSOCIATIVE)
	 * 
	 * @return
	 */
	public int getGrouping() {
		return fGrouping;
	}

	public ASTNode createFunction(final IParserFactory factory,
			final ASTNode lhs, final ASTNode rhs) {
		return factory.createFunction(factory.createSymbol(getFunctionName()),
				lhs, rhs);
	}
}
