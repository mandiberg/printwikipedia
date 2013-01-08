package info.bliki.wiki.template.expr.util;

import info.bliki.wiki.template.expr.operator.ASTNodeFactory;
import info.bliki.wiki.template.expr.operator.DivideOperator;
import info.bliki.wiki.template.expr.operator.InfixOperator;
import info.bliki.wiki.template.expr.operator.Operator;
import info.bliki.wiki.template.expr.operator.PostfixOperator;
import info.bliki.wiki.template.expr.operator.PreMinusOperator;
import info.bliki.wiki.template.expr.operator.PrePlusOperator;
import info.bliki.wiki.template.expr.operator.PrefixOperator;
import info.bliki.wiki.template.expr.operator.SubtractOperator;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

/**
 * Utility for generating source codes for the
 * <code>info.bliki.wiki.template.expr.operator.ASTNodeFactory's HEADER_STRINGS, OPERATOR_STRINGS, OPERATORS</code>
 * arrays from the operators.txt textfile description
 * 
 */
public class GenerateOperatorArrays {

	/**
	 * Utility for generating source codes for the
	 * <code>ASTNodeFactory's HEADER_STRINGS, OPERATOR_STRINGS, OPERATORS</code>
	 * arrays from an operator's text file description
	 * 
	 * @param args
	 *          if <code>args.length==0</code> take the default
	 *          <code>/opertors.txt</code> file for generating the arrays; if
	 *          <code>args.length>=1</code> the <code>arg[0]</code> parameters
	 *          should contain the complete filename of the operator's description
	 *          file
	 */
	public static void main(final String[] args) {
		InputStream operatorDefinitions = null;
		try {
			if (args.length >= 1) {
				operatorDefinitions = new FileInputStream(args[0]);
			} else {
				operatorDefinitions = GenerateOperatorArrays.class.getResourceAsStream("/operators.txt");
			}
			final HashMap operatorMap = new HashMap();
			final HashMap operatorTokenStartSet = new HashMap();
			GenerateOperatorArrays.generateOperatorTable(operatorDefinitions, operatorMap, operatorTokenStartSet);

			final Iterator i1 = operatorMap.keySet().iterator();
			System.out.println("public static final String[] HEADER_STRINGS = {");
			while (i1.hasNext()) {
				final String headStr = (String) i1.next();
				System.out.println("    \"" + headStr + "\",");
			}
			System.out.println("};");

			final Iterator i2 = operatorMap.keySet().iterator();
			System.out.println("public static final String[] OPERATOR_STRINGS = {");
			while (i2.hasNext()) {
				final String headStr = (String) i2.next();
				final Operator oper = (Operator) operatorMap.get(headStr);
				if (oper == null) {
					System.out.println("    \" null-value-in-operator-map \",");
				} else {
					System.out.println("    \"" + oper.getOperatorString() + "\",");
				}
			}
			System.out.println("};");

			final Iterator i3 = operatorMap.keySet().iterator();
			System.out.println("public static final Operator[] OPERATORS = {");
			while (i3.hasNext()) {
				final String headStr = (String) i3.next();
				final Operator oper = (Operator) operatorMap.get(headStr);
				if (oper instanceof DivideOperator) {
					final InfixOperator iOper = (DivideOperator) oper;
					String grouping = null;
					if (iOper.getGrouping() == InfixOperator.NONE) {
						grouping = "InfixOperator.NONE";
					} else if (iOper.getGrouping() == InfixOperator.LEFT_ASSOCIATIVE) {
						grouping = "InfixOperator.LEFT_ASSOCIATIVE";
					} else if (iOper.getGrouping() == InfixOperator.RIGHT_ASSOCIATIVE) {
						grouping = "InfixOperator.RIGHT_ASSOCIATIVE";
					}
					System.out.println("    new DivideOperator(\"" + iOper.getOperatorString() + "\", \"" + iOper.getFunctionName() + "\", "
							+ iOper.getPrecedence() + ", " + grouping + "),");
				} else if (oper instanceof SubtractOperator) {
					final InfixOperator iOper = (SubtractOperator) oper;
					String grouping = null;
					if (iOper.getGrouping() == InfixOperator.NONE) {
						grouping = "InfixOperator.NONE";
					} else if (iOper.getGrouping() == InfixOperator.LEFT_ASSOCIATIVE) {
						grouping = "InfixOperator.LEFT_ASSOCIATIVE";
					} else if (iOper.getGrouping() == InfixOperator.RIGHT_ASSOCIATIVE) {
						grouping = "InfixOperator.RIGHT_ASSOCIATIVE";
					}
					System.out.println("    new SubtractOperator(\"" + iOper.getOperatorString() + "\", \"" + iOper.getFunctionName()
							+ "\", " + iOper.getPrecedence() + ", " + grouping + "),");
				} else if (oper instanceof InfixOperator) {
					final InfixOperator iOper = (InfixOperator) oper;
					String grouping = null;
					if (iOper.getGrouping() == InfixOperator.NONE) {
						grouping = "InfixOperator.NONE";
					} else if (iOper.getGrouping() == InfixOperator.LEFT_ASSOCIATIVE) {
						grouping = "InfixOperator.LEFT_ASSOCIATIVE";
					} else if (iOper.getGrouping() == InfixOperator.RIGHT_ASSOCIATIVE) {
						grouping = "InfixOperator.RIGHT_ASSOCIATIVE";
					}
					System.out.println("    new InfixOperator(\"" + iOper.getOperatorString() + "\", \"" + iOper.getFunctionName() + "\", "
							+ iOper.getPrecedence() + ", " + grouping + "),");
				} else if (oper instanceof PostfixOperator) {
					System.out.println("    new PostfixOperator(\"" + oper.getOperatorString() + "\", \"" + oper.getFunctionName() + "\", "
							+ oper.getPrecedence() + "),");
				} else if (oper instanceof PreMinusOperator) {
					System.out.println("    new PreMinusOperator(\"" + oper.getOperatorString() + "\", \"" + oper.getFunctionName() + "\", "
							+ oper.getPrecedence() + "),");
				} else if (oper instanceof PrePlusOperator) {
					System.out.println("    new PrePlusOperator(\"" + oper.getOperatorString() + "\", \"" + oper.getFunctionName() + "\", "
							+ oper.getPrecedence() + "),");
				} else if (oper instanceof PrefixOperator) {
					System.out.println("    new PrefixOperator(\"" + oper.getOperatorString() + "\", \"" + oper.getFunctionName() + "\", "
							+ oper.getPrecedence() + "),");
				}

			}
			System.out.println("};");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void generateOperatorTable(final InputStream is, final HashMap operatorMap, final HashMap operatorTokenStartSet) {
		String record = null;
		final BufferedReader r = new BufferedReader(new InputStreamReader(is));

		StringTokenizer tokenizer;
		Operator oper = null;
		String typeStr;
		String operatorStr;
		String headStr;
		String precedenceStr;
		String groupingStr;
		int precedence;
		int grouping;
		try {

			while ((record = r.readLine()) != null) {
				if (record.charAt(0) == '#') {
					continue;
				}
				tokenizer = new StringTokenizer(record, ",");
				typeStr = ((String) tokenizer.nextElement()).trim();
				operatorStr = ((String) tokenizer.nextElement()).trim();
				headStr = ((String) tokenizer.nextElement()).trim();
				precedenceStr = ((String) tokenizer.nextElement()).trim();
				precedence = Integer.valueOf(precedenceStr).intValue();
				oper = null;
				if (typeStr.equalsIgnoreCase("in")) {
					try {
						groupingStr = ((String) tokenizer.nextElement()).trim();
						grouping = InfixOperator.NONE;
						if (groupingStr.equalsIgnoreCase("left")) {
							grouping = InfixOperator.LEFT_ASSOCIATIVE;
						} else if (groupingStr.equalsIgnoreCase("right")) {
							grouping = InfixOperator.RIGHT_ASSOCIATIVE;
						}
						oper = ASTNodeFactory.createInfixOperator(operatorStr, headStr, precedence, grouping);
					} catch (final NoSuchElementException nsee) {
						oper = new InfixOperator(operatorStr, headStr, precedence, InfixOperator.NONE);
					}
					// if (operatorStr.equals("*")) {
					// TIMES_OPERATOR = (InfixOperator) oper;
					// } else if (operatorStr.equals("+")) {
					// PLUS_OPERATOR = (InfixOperator) oper;
					// }
				} else if (typeStr.equalsIgnoreCase("pre")) {
					oper = ASTNodeFactory.createPrefixOperator(operatorStr, headStr, precedence);
				} else if (typeStr.equalsIgnoreCase("post")) {
					oper = ASTNodeFactory.createPostfixOperator(operatorStr, headStr, precedence);
				} else {
					throw new ParseException("Wrong operator type: " + typeStr, 0);
				}
				// System.out.println(oper);
				ASTNodeFactory.addOperator(operatorMap, operatorTokenStartSet, operatorStr, headStr, oper);
			}

		} catch (final Exception e) {
			e.printStackTrace();
		}
	}
}
