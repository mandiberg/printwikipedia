package info.bliki.wiki.template.expr.eval;

import info.bliki.wiki.template.expr.ast.FunctionNode;

public interface IDoubleFunction {
  public double evaluate(DoubleEvaluator engine, FunctionNode function);
}
