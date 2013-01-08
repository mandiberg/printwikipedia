package info.bliki.wiki.template.expr.operator;

import info.bliki.wiki.template.expr.ast.ASTNode;
import info.bliki.wiki.template.expr.ast.IParserFactory;

public class PrefixOperator extends Operator {

	public PrefixOperator(final String oper, final String functionName, final int precedence) {
		super(oper, functionName, precedence);
	}

	public ASTNode createFunction(final IParserFactory factory,
			final ASTNode argument) {
		return factory.createFunction(factory.createSymbol(getFunctionName()),
				argument);
	}
}
