package info.bliki.wiki.template.expr.operator;

import info.bliki.wiki.template.expr.ast.ASTNode;
import info.bliki.wiki.template.expr.ast.IParserFactory;

public class SubtractOperator extends InfixOperator {
	public SubtractOperator(final String oper, final String functionName, final int precedence,
			final int grouping) {
		super(oper, functionName, precedence, grouping);
	}

	public ASTNode createFunction(final IParserFactory factory, final ASTNode lhs,
			final ASTNode rhs) {
		return factory.createFunction(factory.createSymbol("Plus"), lhs,
				factory.createFunction(factory.createSymbol("Times"),
						factory.createInteger(-1), rhs));
	}
}
