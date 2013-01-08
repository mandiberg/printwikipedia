package info.bliki.wiki.template.expr.operator;

import info.bliki.wiki.template.expr.ast.ASTNode;
import info.bliki.wiki.template.expr.ast.FractionNode;
import info.bliki.wiki.template.expr.ast.IParserFactory;
import info.bliki.wiki.template.expr.ast.IntegerNode;

public class DivideOperator extends InfixOperator {
	public DivideOperator(final String oper, final String functionName,
			final int precedence, final int grouping) {
		super(oper, functionName, precedence, grouping);
	}

	public ASTNode createFunction(final IParserFactory factory,
			final ASTNode lhs, final ASTNode rhs) {
		if (rhs instanceof IntegerNode) {
			if (lhs instanceof IntegerNode) {
				return new FractionNode((IntegerNode) lhs, (IntegerNode) rhs);
			}
			return factory.createFunction(factory.createSymbol("Times"), lhs,
					new FractionNode(IntegerNode.C1, (IntegerNode) rhs));
		}
		return factory.createFunction(factory.createSymbol("Times"), lhs,
				factory.createFunction(factory.createSymbol("Power"), rhs,
						factory.createInteger(-1)));
	}
}
