package info.bliki.wiki.template.expr.eval;

import info.bliki.wiki.template.expr.Parser;
import info.bliki.wiki.template.expr.SyntaxError;
import info.bliki.wiki.template.expr.ast.ASTNode;
import info.bliki.wiki.template.expr.ast.FunctionNode;
import info.bliki.wiki.template.expr.ast.NumberNode;
import info.bliki.wiki.template.expr.ast.SymbolNode;

import java.util.HashMap;
import java.util.Map;

/**
 * Evaluate math expressions to <code>double</code> numbers.
 * 
 * Use the expression syntax describe here: <a
 * href="http://meta.wikimedia.org/wiki/Help:ParserFunctions#.23expr:">http://meta.wikimedia.org/wiki/Help:ParserFunctions#.23expr:</a>
 */
public class DoubleEvaluator {
	/**
	 * A "close to zero" double epsilon value for use to compare with 0.0
	 * 
	 */
	public static double EPSILON = 1.0e-17d;

	/**
	 * A "close to zero" double epsilon value for use in the Round function
	 * 
	 */
	public static double EPSILON_ROUND = 1.0e-15d;

	Double bd;

	private static Map<String, Double> SYMBOL_DOUBLE_MAP;

	private static Map<String, Object> FUNCTION_DOUBLE_MAP;

	private static Map<String, Object> FUNCTION_BOOLEAN_MAP;

	static class PlusFunction implements IDoubleFunction, IDouble2Function {
		public double evaluate(double arg1, double arg2) {
			return arg1 + arg2;
		}

		public double evaluate(DoubleEvaluator engine, FunctionNode function) {
			double result = 0.0;
			for (int i = 1; i < function.size(); i++) {
				result += engine.evaluateNode((ASTNode) function.get(i));
			}
			return result;
		}
	}

	static class SubtractFunction implements IDoubleFunction, IDouble2Function {
		public double evaluate(double arg1, double arg2) {
			return arg1 - arg2;
		}

		public double evaluate(DoubleEvaluator engine, FunctionNode function) {
			double result = 0.0;
			for (int i = 1; i < function.size(); i++) {
				result += engine.evaluateNode((ASTNode) function.get(i));
			}
			return result;
		}
	}

	static class TimesFunction implements IDoubleFunction, IDouble2Function {
		public double evaluate(double arg1, double arg2) {
			return arg1 * arg2;
		}

		public double evaluate(DoubleEvaluator engine, FunctionNode function) {
			double result = 1.0;
			for (int i = 1; i < function.size(); i++) {
				result *= engine.evaluateNode((ASTNode) function.get(i));
			}
			return result;
		}
	}

	static class DivideFunction implements IDouble2Function {
		public double evaluate(double arg1, double arg2) {
			if (Math.abs(arg2 - 0.0) < DoubleEvaluator.EPSILON) {
				throw new ArithmeticException("Division by zero");
			}
			return arg1 / arg2;
		}
	}

	static class ModFunction implements IDouble2Function {
		public double evaluate(double arg1, double arg2) {
			if (Math.abs(arg2 - 0.0) < DoubleEvaluator.EPSILON) {
				throw new ArithmeticException("Division by zero");
			}
			return arg1 % arg2;
		}
	}

	static class PowFunction implements IDouble2Function {
		public double evaluate(double arg1, double arg2) {
			return Math.pow(arg1, arg2);
		}

	}

	static {
		SYMBOL_DOUBLE_MAP = new HashMap<String, Double>();
		FUNCTION_BOOLEAN_MAP = new HashMap<String, Object>();
		SYMBOL_DOUBLE_MAP.put("E", new Double(Math.E));
		SYMBOL_DOUBLE_MAP.put("Pi", new Double(Math.PI));

		FUNCTION_BOOLEAN_MAP.put("And", new IBooleanBoolean2Function() {
			public boolean evaluate(boolean arg1, boolean arg2) {
				return arg1 && arg2;
			}
		});
		FUNCTION_BOOLEAN_MAP.put("Not", new IBooleanBoolean1Function() {
			public boolean evaluate(boolean arg1) {
				return !arg1;
			}
		});
		FUNCTION_BOOLEAN_MAP.put("Or", new IBooleanBoolean2Function() {
			public boolean evaluate(boolean arg1, boolean arg2) {
				return arg1 || arg2;
			}
		});

		FUNCTION_BOOLEAN_MAP.put("Equal", new IBooleanDouble2Function() {
			public boolean evaluate(double arg1, double arg2) {
				return Math.abs(arg1 - arg2) < EPSILON;
			}
		});
		FUNCTION_BOOLEAN_MAP.put("Greater", new IBooleanDouble2Function() {
			public boolean evaluate(double arg1, double arg2) {
				return arg1 > arg2;
			}
		});
		FUNCTION_BOOLEAN_MAP.put("GreaterEqual", new IBooleanDouble2Function() {
			public boolean evaluate(double arg1, double arg2) {
				return arg1 >= arg2;
			}
		});
		FUNCTION_BOOLEAN_MAP.put("Less", new IBooleanDouble2Function() {
			public boolean evaluate(double arg1, double arg2) {
				return arg1 < arg2;
			}
		});
		FUNCTION_BOOLEAN_MAP.put("LessEqual", new IBooleanDouble2Function() {
			public boolean evaluate(double arg1, double arg2) {
				return arg1 <= arg2;
			}
		});
		FUNCTION_BOOLEAN_MAP.put("Unequal", new IBooleanDouble2Function() {
			public boolean evaluate(double arg1, double arg2) {
				return !(Math.abs(arg1 - arg2) < EPSILON);
			}
		});

		FUNCTION_DOUBLE_MAP = new HashMap<String, Object>();
		FUNCTION_DOUBLE_MAP.put("Sin", new IDouble1Function() {
			public double evaluate(double arg1) {
				return Math.sin(arg1);
			}
		});
		FUNCTION_DOUBLE_MAP.put("Cos", new IDouble1Function() {
			public double evaluate(double arg1) {
				return Math.cos(arg1);
			}
		});
		FUNCTION_DOUBLE_MAP.put("Tan", new IDouble1Function() {
			public double evaluate(double arg1) {
				return Math.tan(arg1);
			}
		});
		FUNCTION_DOUBLE_MAP.put("ASin", new IDouble1Function() {
			public double evaluate(double arg1) {
				return Math.asin(arg1);
			}
		});
		FUNCTION_DOUBLE_MAP.put("ACos", new IDouble1Function() {
			public double evaluate(double arg1) {
				return Math.acos(arg1);
			}
		});
		FUNCTION_DOUBLE_MAP.put("ATan", new IDouble1Function() {
			public double evaluate(double arg1) {
				return Math.atan(arg1);
			}
		});
		FUNCTION_DOUBLE_MAP.put("Ln", new IDouble1Function() {
			public double evaluate(double arg1) {
				return Math.log(arg1);
			}
		});
		FUNCTION_DOUBLE_MAP.put("Exp", new IDouble1Function() {
			public double evaluate(double arg1) {
				return Math.exp(arg1);
			}
		});
		FUNCTION_DOUBLE_MAP.put("Abs", new IDouble1Function() {
			public double evaluate(double arg1) {
				return Math.abs(arg1);
			}
		});
		FUNCTION_DOUBLE_MAP.put("Ceil", new IDouble1Function() {
			public double evaluate(double arg1) {
				return Math.ceil(arg1);
			}
		});
		FUNCTION_DOUBLE_MAP.put("Floor", new IDouble1Function() {
			public double evaluate(double arg1) {
				return Math.floor(arg1);
			}
		});
		FUNCTION_DOUBLE_MAP.put("Trunc", new IDouble1Function() {
			public double evaluate(double arg1) {
				if (arg1 < 0.0d) {
					return Math.ceil(arg1);
				} else {
					return Math.floor(arg1);
				}
			}
		});
		FUNCTION_DOUBLE_MAP.put("Plus", new PlusFunction());
		FUNCTION_DOUBLE_MAP.put("Subtract", new SubtractFunction());
		FUNCTION_DOUBLE_MAP.put("Times", new TimesFunction());
		FUNCTION_DOUBLE_MAP.put("Divide", new DivideFunction());
		FUNCTION_DOUBLE_MAP.put("Mod", new ModFunction());
		FUNCTION_DOUBLE_MAP.put("Pow", new PowFunction());

		FUNCTION_DOUBLE_MAP.put("Round", new IDouble2Function() {
			public double evaluate(double arg1, double arg2) {
				if (arg1 > 0) {
					return Math.round((arg1 + EPSILON_ROUND) * Math.pow(10, arg2)) / Math.pow(10, arg2);
				} else {
					return Math.round((arg1 - EPSILON_ROUND) * Math.pow(10, arg2)) / Math.pow(10, arg2);
				}
			}
		});
	}

	private ASTNode fNode;

	public DoubleEvaluator() {
		this(null);
	}

	public DoubleEvaluator(ASTNode node) {
		fNode = node;
	}

	/**
	 * Parse the given <code>expression String</code> and store the resulting
	 * ASTNode in this DoubleEvaluator
	 * 
	 * @param expression
	 * @return
	 * @throws SyntaxError
	 */
	public ASTNode parse(String expression) {
		Parser p = new Parser();
		fNode = p.parse(expression);
		if (fNode instanceof FunctionNode) {
			fNode = optimizeFunction((FunctionNode) fNode);
		}
		return fNode;
	}

	/**
	 * Parse the given <code>expression String</code> and return the resulting
	 * ASTNode
	 * 
	 * @param expression
	 * @return
	 * @throws SyntaxError
	 */
	public static ASTNode parseNode(String expression) {
		DoubleEvaluator doubleEvaluator = new DoubleEvaluator();
		return doubleEvaluator.parse(expression);
	}

	/**
	 * Parse the given <code>expression String</code> and evaluate it to a
	 * double value
	 * 
	 * @param expression
	 * @return
	 * @throws SyntaxError
	 */
	public double evaluate(String expression) {
		Parser p = new Parser();
		fNode = p.parse(expression);
		if (fNode instanceof FunctionNode) {
			fNode = optimizeFunction((FunctionNode) fNode);
		}
		return evaluateNode(fNode);
	}

	/**
	 * Reevaluate the <code>expression</code> (possibly after a new Variable
	 * assignment)
	 * 
	 * @param expression
	 * @return
	 * @throws SyntaxError
	 */
	public double evaluate() {
		if (fNode == null) {
			throw new SyntaxError(0, 0, 0, " ", "No parser input defined", 1);
		}
		return evaluateNode(fNode);
	}

	/**
	 * Evaluate an already parsed in abstract syntax tree node into a
	 * <code>double</code> number value.
	 * 
	 * @param node
	 *          abstract syntax tree node
	 * 
	 * @return the evaluated double number
	 * 
	 * @throws ArithmeticException
	 *           if the <code>node</code> cannot be evaluated.
	 */
	public double evaluateNode(final ASTNode node) {
		if (node instanceof DoubleNode) {
			return ((DoubleNode) node).doubleValue();
		}
		if (node instanceof FunctionNode) {
			return evaluateFunction((FunctionNode) node);
		}
		if (node instanceof SymbolNode) {
			Double dbl = SYMBOL_DOUBLE_MAP.get(node.toString());
			if (dbl != null) {
				return dbl.doubleValue();
			}
		}
		if (node instanceof NumberNode) {
			return ((NumberNode) node).doubleValue();
		}

		throw new ArithmeticException("EvalDouble#evaluate(ASTNode) not possible for: " + node.toString());
	}

	/**
	 * Evaluate an already parsed in <code>FunctionNode</code> into a
	 * <code>souble</code> number value.
	 * 
	 * @param functionNode
	 * @return
	 * 
	 * @throws ArithmeticException
	 *           if the <code>functionNode</code> cannot be evaluated.
	 */
	public double evaluateFunction(final FunctionNode functionNode) {
		if (functionNode.size() > 0 && functionNode.get(0) instanceof SymbolNode) {
			String symbol = functionNode.get(0).toString();
			if (functionNode.size() == 1) {
				Object obj = FUNCTION_DOUBLE_MAP.get(symbol);
				if (obj instanceof IDouble0Function) {
					return ((IDouble0Function) obj).evaluate();
				}
			} else if (functionNode.size() == 2) {
				Object obj = FUNCTION_DOUBLE_MAP.get(symbol);
				if (obj instanceof IDouble1Function) {
					return ((IDouble1Function) obj).evaluate(evaluateNode((ASTNode) functionNode.get(1)));
				}
				return evaluateNodeLogical(functionNode) ? 1.0 : 0.0;
			} else if (functionNode.size() == 3) {
				Object obj = FUNCTION_DOUBLE_MAP.get(symbol);
				if (obj instanceof IDouble2Function) {
					return ((IDouble2Function) obj).evaluate(evaluateNode((ASTNode) functionNode.get(1)), evaluateNode((ASTNode) functionNode
							.get(2)));
				}
				return evaluateNodeLogical(functionNode) ? 1.0 : 0.0;
			} else {
				Object obj = FUNCTION_DOUBLE_MAP.get(symbol);
				if (obj instanceof IDoubleFunction) {
					return ((IDoubleFunction) obj).evaluate(this, functionNode);
				}
			}
		}
		throw new ArithmeticException("EvalDouble#evaluateFunction(FunctionNode) not possible for: " + functionNode.toString());
	}

	public boolean evaluateNodeLogical(final ASTNode node) {
		if (node instanceof FunctionNode) {
			return evaluateFunctionLogical((FunctionNode) node);
		}
		if (node instanceof DoubleNode) {
			double d = ((DoubleNode) node).doubleValue();
			if (Math.abs(d - 0.0) < DoubleEvaluator.EPSILON) {
				return false;
			}
			return true;
		}
		if (node instanceof NumberNode) {
			double d = ((NumberNode) node).doubleValue();
			if (Math.abs(d - 0.0) < DoubleEvaluator.EPSILON) {
				return false;
			}
			return true;
		}

		throw new ArithmeticException("EvalDouble#evaluateNodeLogical(ASTNode) not possible for: " + node.toString());
	}

	public boolean evaluateFunctionLogical(final FunctionNode functionNode) {
		if (functionNode.size() > 0 && functionNode.get(0) instanceof SymbolNode) {
			String symbol = functionNode.get(0).toString();
			if (functionNode.size() == 2) {
				Object obj = FUNCTION_BOOLEAN_MAP.get(symbol);
				if (obj instanceof IBooleanBoolean1Function) {
					return ((IBooleanBoolean1Function) obj).evaluate(evaluateNodeLogical((ASTNode) functionNode.get(1)));
				}
			} else if (functionNode.size() == 3) {
				Object obj = FUNCTION_BOOLEAN_MAP.get(symbol);
				if (obj instanceof IBooleanDouble2Function) {
					return ((IBooleanDouble2Function) obj).evaluate(evaluateNode((ASTNode) functionNode.get(1)),
							evaluateNode((ASTNode) functionNode.get(2)));
				} else if (obj instanceof IBooleanBoolean2Function) {
					return ((IBooleanBoolean2Function) obj).evaluate(evaluateNodeLogical((ASTNode) functionNode.get(1)),
							evaluateNodeLogical((ASTNode) functionNode.get(2)));
				}
			}
		}
		throw new ArithmeticException("EvalDouble#evaluateFunctionLogical(FunctionNode) not possible for: " + functionNode.toString());

	}

	/**
	 * Optimize an already parsed in <code>functionNode</code> into an
	 * <code>ASTNode</code>.
	 * 
	 * @param functionNode
	 * @return
	 * 
	 */
	public ASTNode optimizeFunction(final FunctionNode functionNode) {
		if (functionNode.size() > 0) {
			boolean doubleOnly = true;
			ASTNode node;
			for (int i = 1; i < functionNode.size(); i++) {
				node = functionNode.get(i);
				if (node instanceof NumberNode) {
					functionNode.set(i, new DoubleNode(((NumberNode) functionNode.get(i)).doubleValue()));
				} else if (functionNode.get(i) instanceof FunctionNode) {
					ASTNode optNode = optimizeFunction((FunctionNode) functionNode.get(i));
					if (!(optNode instanceof DoubleNode)) {
						doubleOnly = false;
					}
					functionNode.set(i, optNode);
				} else {
					doubleOnly = false;
				}
			}
			if (doubleOnly) {
				try {
					return new DoubleNode(evaluateFunction(functionNode));
				} catch (Exception e) {

				}
			}
		}
		return functionNode;
	}

}
