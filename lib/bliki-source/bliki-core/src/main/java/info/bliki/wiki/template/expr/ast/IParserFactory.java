package info.bliki.wiki.template.expr.ast;

import info.bliki.wiki.template.expr.operator.Operator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Factory for creating the ASTNodes from the parser
 * 
 */
public interface IParserFactory {
	/** 
	 * The default set of characters, which could form an operator
	 * 
	 */
	public final static String DEFAULT_OPERATOR_CHARACTERS = "-=<>*+/!^";

	/**
	 * The set of characters, which could form an operator
	 * 
	 */
	public String getOperatorCharacters();

	/**
	 * Get the identifier to operator map
	 * 
	 * @return the map which stores the Operators for a given head string like
	 *         Times, Plus, Sin,...
	 */
	public Map<String, Operator> getIdentifier2OperatorMap();

	/**
	 * Get the operator-string to possible operator-list map
	 * 
	 * @return the map which stores the operator-list for a given operator string
	 *         like *, +, ==...
	 */
	public Map<String, ArrayList<Operator>> getOperator2ListMap();

	/**
	 * Get the operator for a given identifier string like Times, Plus, Sin,...
	 * 
	 * @param identifier
	 * @return
	 */
	public Operator get(String identifier);

	/**
	 * Get the operator-list for a given operator-string
	 * 
	 * @return the operator-list for a given operator string like *, +, ==...
	 */
	public List<Operator> getOperatorList(String operatorString);

	public String getConstantSymbol(String symbolString);
	
	/**
	 * Creates a new function with head <code>head</code> and 0 arguments.
	 */
	public FunctionNode createFunction(SymbolNode head);

	/**
	 * Creates a new function with head <code>head</code> and 1 argument.
	 */
	public FunctionNode createFunction(SymbolNode head, ASTNode arg0);

	/**
	 * Creates a new function with head <code>head</code> and 2 arguments.
	 */
	public FunctionNode createFunction(SymbolNode head, ASTNode arg0, ASTNode arg1);

	/**
	 * Creates a new function with no arguments from the given header expression .
	 */
	public FunctionNode createAST(ASTNode headExpr);

	/**
	 * Create an double node from the given double value string
	 * 
	 * @param doubleString
	 * @return
	 */
	public ASTNode createDouble(String doubleString);

	/**
	 * Create an integer node from the given string
	 * 
	 * @param integerString
	 *          the integer number represented as a String
	 * @param numberFormat
	 *          the format of the number (usually 10)
	 * @return IInteger
	 */
	public IntegerNode createInteger(String integerString, int numberFormat);

	/**
	 * Create an integer node from the given value
	 * 
	 * @param integerValue
	 *          the integer number's value
	 * @return IInteger
	 */
	public IntegerNode createInteger(int integerValue);

	/**
	 * Create a "fractional" number
	 * 
	 * @param numerator
	 *          numerator of the fractional number
	 * @param denominator
	 *          denominator of the fractional number
	 * @return IFraction
	 */
	public abstract FractionNode createFraction(IntegerNode numerator, IntegerNode denominator);

	/**
	 * Create a string node from the scanned double quoted string
	 * 
	 * @param symbolName
	 * @return
	 */
	public StringNode createString(StringBuffer buffer);

	/**
	 * Create a symbol from the scanned identifier string
	 * 
	 * @param symbolName
	 * @return
	 */
	public SymbolNode createSymbol(String symbolName);

}