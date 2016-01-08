package info.bliki.wiki.template.expr.ast;


/**
 * A node for a parsed floating number string
 *
 */
public class FloatNode extends NumberNode {
    public FloatNode(final String value) {
        super(value);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof FloatNode) {
            return fStringValue.equals(((NumberNode) obj).fStringValue) && sign == ((NumberNode) obj).sign;
        }
        return false;
    }
}
