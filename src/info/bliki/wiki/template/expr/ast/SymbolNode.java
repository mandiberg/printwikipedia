package info.bliki.wiki.template.expr.ast;


/**
 * A node for a parsed symbol string (i.e. Sin, Cos, Pi, $x,...)
 *
 */
public class SymbolNode extends ASTNode {

    public SymbolNode(final String value) {
        super(value);
    }

    @Override
    public boolean dependsOn(String variableName) {
        return fStringValue.equals(variableName);
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof SymbolNode) && fStringValue == ((SymbolNode) obj).fStringValue;
    }
}
