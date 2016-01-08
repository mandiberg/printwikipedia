package info.bliki.wiki.template.expr.ast;

/**
 * The basic node for a parsed expression string
 *
 */
public abstract class ASTNode {

    protected final String fStringValue;

    protected ASTNode(final String value) {
        fStringValue = value;
    }

    /**
     * Returns the parsed string of this node.
     *
     * @return <code>null</code> if there's another representation in the
     *         derived class
     */
    public String getString() {
        return fStringValue;
    }

    @Override
    public String toString() {
        return fStringValue;
    }

    public boolean dependsOn(String variableName) {
        return false;
    }

    public ASTNode derivative(String variableName) {
        return new IntegerNode("0");
    }

    @Override
    public abstract boolean equals(Object obj);

    @Override
    public int hashCode() {
        return fStringValue.hashCode();
    }
}
