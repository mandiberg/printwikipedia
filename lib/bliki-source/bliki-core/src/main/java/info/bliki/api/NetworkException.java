package info.bliki.api;

/** 
 * Base class for Exceptions thrown when something has gone wrong with the network
 * 
 */ 
public abstract class NetworkException extends Exception {
	/** Constructs a new exception with null as its detail message. */
	public NetworkException() {
		super();
	}
	
	/** Constructs a new exception with the specified detail message. */
	public NetworkException(String message) {
		super(message);
	}
	
	/** Constructs a new exception with the specified detail message and cause. */
	public NetworkException(String message, Throwable cause) {
		super(message, cause);
	}

	/** Constructs a new exception with the specified cause and a detail 
		message of (cause==null ? null : cause.toString()) (which 
		typically contains the class and detail message of cause). */
 	public NetworkException(Throwable cause) {
		super(cause);	
	} 
}
