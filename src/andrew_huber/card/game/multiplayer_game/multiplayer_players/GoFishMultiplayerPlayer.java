package andrew_huber.card.game.multiplayer_game.multiplayer_players;

import andrew_huber.card.player.CardPlayer;
import andrew_huber.card.player.GroupOfCardPlayers;
import andrew_huber.exceptions.InvalidArgumentException;
import andrew_huber.exceptions.NoMoreCardsInDeckException;
import andrew_huber.exceptions.PasswordLengthException;

public class GoFishMultiplayerPlayer extends CardPlayer 
{
	private int password;
	public static final int PASSWORD_LENGTH = 4;
	
	public GoFishMultiplayerPlayer(GroupOfCardPlayers group, String name,
			int handSize, int password) throws InvalidArgumentException,
			NoMoreCardsInDeckException, PasswordLengthException 
	{
		super(group, name, handSize);
		setPassword(password);
	}
	
	private void setPassword(int password) throws PasswordLengthException
	{
		String passwordString = Integer.toString(password);
		
		if(passwordString.length() == PASSWORD_LENGTH)
			this.password = password;
		else
		{
			throw new PasswordLengthException(passwordString.length(), 
					PASSWORD_LENGTH, this);
		}
	}
	
	public boolean testPassword(int passwordToTest)
	{
		return passwordToTest == password;
	}
}
