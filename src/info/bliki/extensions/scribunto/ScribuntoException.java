package info.bliki.extensions.scribunto;

public class ScribuntoException extends Exception {
    public ScribuntoException(Exception e) {
        super(e);
    }

    public ScribuntoException(String s) {
        super(s);
    }
}
