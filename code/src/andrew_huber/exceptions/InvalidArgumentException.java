package andrew_huber.exceptions;

/**
 * Exceptions handling invalid arguments
 * @author andrew_huber
 *
 */
public class InvalidArgumentException extends Throwable 
{
	/**
	 * Message describing the reason why this exception was thrown
	 */
	private String message;
	
	/**
	 * Creates an InvalidArgumentException
	 * @param message The message describing the reason why this exception was thrown
	 */
	public InvalidArgumentException(String message)
	{
		this.message = message;
	}
	
	/**
	 * Gets the message passed into the constructor ({@link #InvalidArgumentException(String)})
	 * @see InvalidArgumentException#InvalidArgumentException(String)
	 */
	@Override
	public String getMessage()
	{
		return message;
	}
}
