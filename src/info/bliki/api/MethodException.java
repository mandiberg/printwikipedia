package info.bliki.api;

/**
 * this Exception is thrown, when the underlying HttpClient throws an Exception
 */
public class MethodException extends NetworkException {
	/**
	 * Constructs a new exception with null as its detail message.
	 */
	public MethodException() {
		super();
	}

	/**
	 * Constructs a new exception with the specified detail message.
	 */
	public MethodException(String message) {
		super(message);
	}

	/**
	 * Constructs a new exception with the specified detail message and cause.
	 */
	public MethodException(String message, Throwable cause) {
		super(message);
	}

	/**
	 * Constructs a new exception with the specified cause and a detail message of
	 * (cause==null ? null : cause.toString()) (which typically contains the class
	 * and detail message of cause).
	 */
	public MethodException(Throwable cause) {
		super();
	}
}
