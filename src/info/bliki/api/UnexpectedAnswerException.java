package info.bliki.api;

/**
 * this Exception is thrown, when the connection to a server returns unexpected
 * values
 */
public class UnexpectedAnswerException extends NetworkException {

    /**
     *
     */
    private static final long serialVersionUID = -2001762874991269623L;

    private ErrorData errorData;

    /** Constructs a new exception with null as its detail message. */
    public UnexpectedAnswerException() {
        super();
    }

    /** Constructs a new exception with the specified detail message. */
    public UnexpectedAnswerException(String message) {
        super(message);
    }

    /** Constructs a new exception with the specified detail message and cause. */
    public UnexpectedAnswerException(String message, Throwable cause) {
        super(message);
    }

    /**
     * Constructs a new exception with the specified cause and a detail message of
     * (cause==null ? null : cause.toString()) (which typically contains the class
     * and detail message of cause).
     */
    public UnexpectedAnswerException(Throwable cause) {
        super();
    }

    public ErrorData getErrorData() {
        return errorData;
    }

    public void setErrorData(ErrorData errorData) {
        this.errorData = errorData;
    }
}
