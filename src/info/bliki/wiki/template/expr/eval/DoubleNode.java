package info.bliki.wiki.template.expr.eval;

import info.bliki.wiki.template.expr.ast.ASTNode;

/**
 *
 */
public class DoubleNode extends ASTNode {

    private final double value;

    public DoubleNode(double value) {
        super("DoubleNode");
        this.value = value;
    }

    public double doubleValue() {
        return value;
    }

    @Override
    public String toString() {
        return Double.toString(value);
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof DoubleNode) && value == ((DoubleNode) obj).value;
    }

    @Override
    public int hashCode() {
        long bits = Double.doubleToLongBits(value);
        return (int) (bits ^ (bits >>> 32));
    }
}
