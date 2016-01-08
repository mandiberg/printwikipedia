package info.bliki.wiki.template.expr.operator;

import info.bliki.wiki.template.expr.ast.ASTNode;
import info.bliki.wiki.template.expr.ast.FloatNode;
import info.bliki.wiki.template.expr.ast.FractionNode;
import info.bliki.wiki.template.expr.ast.FunctionNode;
import info.bliki.wiki.template.expr.ast.IParserFactory;
import info.bliki.wiki.template.expr.ast.IntegerNode;
import info.bliki.wiki.template.expr.ast.SymbolNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ASTNodeFactory implements IParserFactory {
    public final static int PLUS_PRECEDENCE = 2900;

    public final static int TIMES_PRECEDENCE = 3800;

    public static final String[] HEADER_STRINGS = { "Mod", "Plus", "GreaterEqual", "Round", "Times", "Less", "Subtract", "Unequal",
            "Unequal", "Not", "Sin", "Cos", "Tan", "ASin", "ACos", "ATan", "Ln", "Exp", "Abs", "Ceil", "Floor", "Trunc", "LessEqual",
            "Or", "PrePlus", "Equal", "Divide", "Divide", "Greater", "PreMinus", "Pow", "And" };

    public static final String[] OPERATOR_STRINGS = { "mod", "+", ">=", "round", "*", "<", "-", "<>", "!=", "not", "sin", "cos",
            "tan", "asin", "acos", "atan", "ln", "exp", "abs", "ceil", "floor", "trunc", "<=", "or", "+", "=", "div", "/", ">", "-", "^",
            "and" };

    public static final Operator[] OPERATORS = { new InfixOperator("mod", "Mod", 3800, InfixOperator.NONE),
            new InfixOperator("+", "Plus", 2900, InfixOperator.NONE), new InfixOperator(">=", "GreaterEqual", 2600, InfixOperator.NONE),
            new InfixOperator("round", "Round", 2800, InfixOperator.NONE), new InfixOperator("*", "Times", 3800, InfixOperator.NONE),
            new InfixOperator("<", "Less", 2600, InfixOperator.NONE), new InfixOperator("-", "Subtract", 2900, InfixOperator.NONE),
            new InfixOperator("<>", "Unequal", 2600, InfixOperator.NONE), new InfixOperator("!=", "Unequal", 2600, InfixOperator.NONE),
            new PrefixOperator("not", "Not", 4600),

            new PrefixOperator("sin", "Sin", 4600), new PrefixOperator("cos", "Cos", 4600), new PrefixOperator("tan", "Tan", 4600),
            new PrefixOperator("asin", "ASin", 4600), new PrefixOperator("acos", "ACos", 4600), new PrefixOperator("atan", "ATan", 4600),
            new PrefixOperator("ln", "Ln", 4600), new PrefixOperator("exp", "Exp", 4600), new PrefixOperator("abs", "Abs", 4600),

            new PrefixOperator("ceil", "Ceil", 4600), new PrefixOperator("floor", "Floor", 4600),
            new PrefixOperator("trunc", "Trunc", 4600), new InfixOperator("<=", "LessEqual", 2600, InfixOperator.NONE),
            new InfixOperator("or", "Or", 1900, InfixOperator.NONE), new PrePlusOperator("+", "PrePlus", 4600),
            new InfixOperator("=", "Equal", 2600, InfixOperator.NONE), new InfixOperator("div", "Divide", 3800, InfixOperator.NONE),
            new InfixOperator("/", "Divide", 3800, InfixOperator.NONE), new InfixOperator(">", "Greater", 2600, InfixOperator.NONE),
            new PreMinusOperator("-", "PreMinus", 4600), new InfixOperator("^", "Pow", 3700, InfixOperator.NONE),
            new InfixOperator("and", "And", 2000, InfixOperator.NONE) };

    public final static ASTNodeFactory MMA_STYLE_FACTORY = new ASTNodeFactory();

    /**
     * private HashMap<String, Operator> fOperatorMap = new HashMap<String,
     * Operator>();
     */
    private static HashMap<String, Operator> fOperatorMap = new HashMap<>();

    /**
     * private HashMap<String, ArrayList<Operator>> fOperatorTokenStartSet = new
     * HashMap<String, ArrayList<Operator>>();
     */
    private static HashMap<String, ArrayList<Operator>> fOperatorTokenStartSet = new HashMap<>();

    private static HashMap<String, String> fConstantSymbols = new HashMap<>();

    static {
        fOperatorMap = new HashMap<>();
        fOperatorTokenStartSet = new HashMap<>();
        fConstantSymbols.put("e", "E");
        fConstantSymbols.put("pi", "Pi");
        for (int i = 0; i < HEADER_STRINGS.length; i++) {
            addOperator(fOperatorMap, fOperatorTokenStartSet, OPERATOR_STRINGS[i], HEADER_STRINGS[i], OPERATORS[i]);
        }
    }

    /**
     * Create a default ASTNode factory
     *
     */
    public ASTNodeFactory() {
    }

    static public void addOperator(final HashMap<String, Operator> operatorMap,
            final HashMap<String, ArrayList<Operator>> operatorTokenStartSet, final String operatorStr, final String headStr,
            final Operator oper) {
        ArrayList<Operator> list;
        operatorMap.put(headStr, oper);
        list = operatorTokenStartSet.get(operatorStr);
        if (list == null) {
            list = new ArrayList<>(2);
            list.add(oper);
            operatorTokenStartSet.put(operatorStr, list);
        } else {
            list.add(oper);
        }
    }

    @Override
    public String getOperatorCharacters() {
        return DEFAULT_OPERATOR_CHARACTERS;
    }

    /**
     * public Map<String, Operator> getIdentifier2OperatorMap()
     */
    @Override
    public Map<String, Operator> getIdentifier2OperatorMap() {
        return fOperatorMap;
    }

    @Override
    public Operator get(final String identifier) {
        return (Operator) fOperatorMap.get(identifier);
    }

    /**
     * public Map<String, ArrayList<Operator>> getOperator2ListMap()
     */
    @Override
    public Map<String, ArrayList<Operator>> getOperator2ListMap() {
        return fOperatorTokenStartSet;
    }

    /**
     * public List<Operator> getOperatorList(final String key)
     */
    @Override
    public List<Operator> getOperatorList(final String key) {
        return fOperatorTokenStartSet.get(key);
    }

    @Override
    public String getConstantSymbol(final String key) {
        return fConstantSymbols.get(key);
    }

    static public InfixOperator createInfixOperator(final String operatorStr, final String headStr, final int precedence,
            final int grouping) {
        InfixOperator oper;
        if (headStr.equals("Divide")) {
            oper = new DivideOperator(operatorStr, headStr, precedence, grouping);
        } else if (headStr.equals("Subtract")) {
            oper = new SubtractOperator(operatorStr, headStr, precedence, grouping);
        } else {
            oper = new InfixOperator(operatorStr, headStr, precedence, grouping);
        }
        return oper;
    }

    static public PrefixOperator createPrefixOperator(final String operatorStr, final String headStr, final int precedence) {
        PrefixOperator oper;
        if (headStr.equals("PreMinus")) {
            oper = new PreMinusOperator(operatorStr, headStr, precedence);
        } else if (headStr.equals("PrePlus")) {
            oper = new PrePlusOperator(operatorStr, headStr, precedence);
        } else {
            oper = new PrefixOperator(operatorStr, headStr, precedence);
        }
        return oper;
    }

    static public PostfixOperator createPostfixOperator(final String operatorStr, final String headStr, final int precedence) {
        return new PostfixOperator(operatorStr, headStr, precedence);
    }

    @Override
    public ASTNode createDouble(final String doubleString) {
        return new FloatNode(doubleString);
    }

    @Override
    public FunctionNode createFunction(final SymbolNode head) {
        return new FunctionNode(head);
    }

    @Override
    public FunctionNode createFunction(final SymbolNode head, final ASTNode arg0) {
        return new FunctionNode(head, arg0);
    }

    @Override
    public FunctionNode createFunction(final SymbolNode head, final ASTNode arg0, final ASTNode arg1) {
        return new FunctionNode(head, arg0, arg1);
    }

    /**
     * Creates a new list with no arguments from the given header object .
     */
    @Override
    public FunctionNode createAST(final ASTNode headExpr) {
        return new FunctionNode(headExpr);
    }

    @Override
    public IntegerNode createInteger(final String integerString, final int numberFormat) {
        return new IntegerNode(integerString, numberFormat);
    }

    @Override
    public IntegerNode createInteger(final int intValue) {
        return new IntegerNode(intValue);
    }

    @Override
    public FractionNode createFraction(final IntegerNode numerator, final IntegerNode denominator) {
        return new FractionNode(numerator, denominator);
    }

    @Override
    public SymbolNode createSymbol(final String symbolName) {
        return new SymbolNode(symbolName);
    }

}
