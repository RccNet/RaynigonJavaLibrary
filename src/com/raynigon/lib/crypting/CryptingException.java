package com.raynigon.lib.crypting;


/**Generated on 09.09.2015 by Simon Schneider in Project <b>RaynigonJavaLib</b><p>
 * The Exception is thrown if an error occurs during a cryption action
 * @author Simon Schneider
 */
public class CryptingException extends RuntimeException {

	private static final long serialVersionUID = 8932087080620579632L;
	
	
	/**Constructs a new CryptingException with the specified detail message and cause. 
	 * Note that the detail message associated with cause is not automatically incorporated in this exception's detail message.
	 * @param message 	the detail message (which is saved for later retrieval by the Throwable.getMessage() method).
	 * @param cause 	the cause (which is saved for later retrieval by the Throwable.getCause() method). (A null value is permitted, and indicates that the cause is nonexistent or unknown.)
	 */
	public CryptingException(String message, Throwable cause) {
		super(message, cause);
	}


	/**Constructs a new CryptingException with the specified detail message. 
	 * Note that the detail message associated with cause is not automatically incorporated in this exception's detail message.
	 * @param message 	the detail message (which is saved for later retrieval by the Throwable.getMessage() method).
	 */
	public CryptingException(String message) {
		super(message);
	}
	

}
