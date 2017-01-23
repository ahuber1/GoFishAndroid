package andrew_huber.exceptions;

import andrew_huber.card.player.CardPlayer;

public class PasswordLengthException extends Throwable 
{
	private String message;
	
	public PasswordLengthException(int lengthFound, int lengthRequired, CardPlayer player)
	{
		message = "Password Length Exception: Length found = " + 
			Integer.toString(lengthFound) + ", Length required = " + 
			", Player = " + player.getName(); 
	}
	
	@Override
	public String getMessage()
	{
		return message;
	}
}
