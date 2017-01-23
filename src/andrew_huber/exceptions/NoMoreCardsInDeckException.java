package andrew_huber.exceptions;

public class NoMoreCardsInDeckException extends Throwable 
{
	String message = "No more cards in deck!";
	
	@Override
	public String getMessage()
	{
		return message;
	}
}
