package info.bliki.wiki.template.expr;

import info.bliki.wiki.template.expr.ast.ASTNode;
import info.bliki.wiki.template.expr.ast.IParserFactory;
import info.bliki.wiki.template.expr.ast.NumberNode;
import info.bliki.wiki.template.expr.ast.SymbolNode;
import info.bliki.wiki.template.expr.operator.ASTNodeFactory;
import info.bliki.wiki.template.expr.operator.InfixOperator;
import info.bliki.wiki.template.expr.operator.Operator;
import info.bliki.wiki.template.expr.operator.PostfixOperator;
import info.bliki.wiki.template.expr.operator.PrefixOperator;

/**
 * Create an expression of the <code>ASTNode</code> class-hierarchy from a
 * math formulas string representation
 * 
 * See <a
 * href="http://en.wikipedia.org/wiki/Operator-precedence_parser">Operator-precedence
 * parser</a> for the idea, how to parse the operators depending on their
 * precedence.
 */
public class Parser extends Scanner {
	/**
	 * Use '('...')' as brackets for arguments
	 */
	boolean fRelaxedSyntax;

	public Parser() {
		this(ASTNodeFactory.MMA_STYLE_FACTORY, false);
	}

	public Parser(final boolean relaxedSyntax) throws SyntaxError {
		this(ASTNodeFactory.MMA_STYLE_FACTORY, relaxedSyntax);
	}

	public Parser(IParserFactory factory, final boolean relaxedSyntax) throws SyntaxError {
		super();
		fRelaxedSyntax = relaxedSyntax;
		fFactory = factory;
	}

	public void setFactory(final IParserFactory factory) {
		this.fFactory = factory;
	}

	public IParserFactory getFactory() {
		return fFactory;
	}

	/**
	 * Determine the current PrefixOperator
	 * 
	 * @return <code>null</code> if no prefix operator could be determined
	 */
	private PrefixOperator determinePrefixOperator() {
		Operator oper = null;
		for (int i = 0; i < fOperList.size(); i++) {
			oper = (Operator) fOperList.get(i);
			if (oper instanceof PrefixOperator) {
				return (PrefixOperator) oper;
			}
		}
		return null;
	}

	/**
	 * Determine the current PostfixOperator
	 * 
	 * @return <code>null</code> if no postfix operator could be determined
	 */
	private PostfixOperator determinePostfixOperator() {
		Operator oper = null;
		for (int i = 0; i < fOperList.size(); i++) {
			oper = (Operator) fOperList.get(i);
			if (oper instanceof PostfixOperator) {
				return (PostfixOperator) oper;
			}
		}
		return null;
	}

	/**
	 * Determine the current BinaryOperator
	 * 
	 * @return <code>null</code> if no binary operator could be determined
	 */
	private InfixOperator determineBinaryOperator() {
		Operator oper = null;
		for (int i = 0; i < fOperList.size(); i++) {
			oper = (Operator) fOperList.get(i);
			if (oper instanceof InfixOperator) {
				return (InfixOperator) oper;
			}
		}
		return null;
	}

	private ASTNode parsePrimary() {
		if (fToken == TT_OPERATOR) {
			final Operator oper = determinePrefixOperator();

			if (oper instanceof PrefixOperator) {
				getNextToken();
				final ASTNode temp = parseLookaheadOperator(oper.getPrecedence());
				if (oper.getFunctionName().equals("PreMinus")) {
					// special cases for negative numbers
					if (temp instanceof NumberNode) {
						((NumberNode) temp).toggleSign();
						return temp;
					}
				}
				return ((PrefixOperator) oper).createFunction(fFactory, temp);
			}
			throwSyntaxError("Operator: " + fOperatorString + " is no prefix operator.");

		}
		return getFactor();
	}

	private ASTNode parseLookaheadOperator(final int min_precedence) {
		ASTNode rhs = parsePrimary();
		Operator operLookahead;
		InfixOperator binOper;
		while (true) {
			final int lookahead = fToken;
			if (lookahead != TT_OPERATOR) {
				break;
			}
			operLookahead = determineBinaryOperator();
			if (operLookahead instanceof InfixOperator) {
				binOper = (InfixOperator) operLookahead;
				if (binOper.getPrecedence() > min_precedence) {
					rhs = parseOperators(rhs, operLookahead.getPrecedence());
					continue;
				} else if ((binOper.getPrecedence() == min_precedence) && (binOper.getGrouping() == InfixOperator.RIGHT_ASSOCIATIVE)) {
					rhs = parseOperators(rhs, operLookahead.getPrecedence());
					continue;
				}
			} else {
				operLookahead = determinePostfixOperator();

				if (operLookahead instanceof PostfixOperator) {
					if (operLookahead.getPrecedence() > min_precedence) {
						getNextToken();
						rhs = ((PostfixOperator) operLookahead).createFunction(fFactory, rhs);
						continue;
					}
				}
			}
			break;
		}
		return rhs;
	}

	/**
	 * See <a
	 * href="http://en.wikipedia.org/wiki/Operator-precedence_parser">Operator-precedence
	 * parser</a> for the idea, how to parse the operators depending on their
	 * precedence.
	 * 
	 * @param lhs
	 *          the already parsed left-hand-side of the operator
	 * @param min_precedence
	 * @return
	 */
	private ASTNode parseOperators(ASTNode lhs, final int min_precedence) {
		ASTNode rhs = null;
		Operator oper;
		while (true) {
			if (fToken != TT_OPERATOR) {
				break;
			}
			oper = determineBinaryOperator();

			if (oper instanceof InfixOperator) {
				if (oper.getPrecedence() >= min_precedence) {
					getNextToken();
					rhs = parseLookaheadOperator(oper.getPrecedence());
					lhs = ((InfixOperator) oper).createFunction(fFactory, lhs, rhs);
					// lhs = parseArguments(lhs);
					continue;
				}
			} else {
				oper = determinePostfixOperator();

				if (oper instanceof PostfixOperator) {
					getNextToken();
					lhs = ((PostfixOperator) oper).createFunction(fFactory, lhs);
					// lhs = parseArguments(lhs);
					continue;
				}
				throwSyntaxError("Operator: " + fOperatorString + " is no infix or postfix operator.");
			}
			// }
			break;
		}
		return lhs;
	}

	/**
	 * Parse the given <code>expression</code> String into an ASTNode.
	 * 
	 * @param expression
	 *          a formula string which should be parsed.
	 * @return the parsed ASTNode representation of the given formula string
	 * @throws SyntaxError
	 */
	public ASTNode parse(final String expression) throws SyntaxError {
		initialize(expression);
		final ASTNode temp = parseOperators(parsePrimary(), 0);
		if (fToken != TT_EOF) {
			if (fToken == TT_PRECEDENCE_CLOSE) {
				throwSyntaxError("Too many closing ')'; End-of-file not reached.");
			}

			throwSyntaxError("End-of-file not reached.");
		}

		return temp;
	}

	/**
	 * Method Declaration.
	 * 
	 * @return
	 * @see
	 */
	private ASTNode getNumber(final boolean negative) throws SyntaxError {
		ASTNode temp = null;
		final Object[] result = getNumberString();
		String number = (String) result[0];
		final int numFormat = ((Integer) result[1]).intValue();
		try {
			if (negative) {
				number = '-' + number;
			}
			if (numFormat < 0) {
				temp = fFactory.createDouble(number);
			} else {
				temp = fFactory.createInteger(number, numFormat);
			}
		} catch (final Throwable e) {
			throwSyntaxError("Number format error: " + number, number.length());
		}
		getNextToken();
		return temp;
	}

	private int getIntegerNumber() throws SyntaxError {
		final Object[] result = getNumberString();
		final String number = (String) result[0];
		final int numFormat = ((Integer) result[1]).intValue();
		int intValue = 0;
		try {
			intValue = Integer.parseInt(number, numFormat);
		} catch (final NumberFormatException e) {
			throwSyntaxError("Number format error (not an int type): " + number, number.length());
		}
		getNextToken();
		return intValue;
	}

	// private ASTNode getString() throws SyntaxError {
	// final StringBuffer ident = getStringBuffer();
	//
	// getNextToken();
	//
	// return fFactory.createString(ident);
	// }

	private ASTNode getFactor() throws SyntaxError {
		ASTNode temp;

		if (fToken == TT_CONSTANT) {
			temp = fFactory.createSymbol(fOperatorString);
			getNextToken();
			return temp;
		}
		if (fToken == TT_DIGIT) {
			return getNumber(false);
		}
		if (fToken == TT_PRECEDENCE_OPEN) {
			getNextToken();

			temp = parseOperators(parsePrimary(), 0);

			if (fToken != TT_PRECEDENCE_CLOSE) {
				throwSyntaxError("\')\' expected.");
			}

			getNextToken();

			return temp;
		}

		// if (fToken == TT_STRING) {
		// return getString();
		// }

		switch (fToken) {

		case TT_PRECEDENCE_CLOSE:
			throwSyntaxError("Too much open ) in factor.");
			break;
		}

		throwSyntaxError("Error in factor at character: '" + fCurrentChar + "' (" + fToken + ")");
		return null;
	}

}