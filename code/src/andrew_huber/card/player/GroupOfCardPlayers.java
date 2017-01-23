package andrew_huber.card.player;

import andrew_huber.card.Card;
import andrew_huber.card.DeckOfCards;
import andrew_huber.card.game.GoFishGame;
import andrew_huber.exceptions.InvalidArgumentException;
import java.util.ArrayList;

public class GroupOfCardPlayers extends DeckOfCards 
{
	/**
	 * Stores all the players that one may draw
	 */
	private ArrayList<CardPlayer> players = 
		new ArrayList<CardPlayer>();
	
	/**
	 * Creates a {@link DeckOfCards} that will be used for all the 
	 * {@link #players} that one may use in a {@link Card} game
	 * @throws InvalidArgumentException In case an error occurred
	 */
	public GroupOfCardPlayers() throws InvalidArgumentException
	{
		super();
	}
	
	/**
	 * Gets a particular hand
	 * @param index Index of the hand requested to be retrieved in accordance
	 * to where the hand is stored in our {@link ArrayList} of {@link #players}
	 * @return The hand requested
	 * @throws InvalidArgumentException 
	 */
	public CardPlayer getPlayer(int index) throws InvalidArgumentException
	{
		if((index < 0) || (index >= players.size()))
		{
			throw new InvalidArgumentException("no player was found at " + 
					"position " + Integer.toString(index) + "\n\n" + 
					this.toString()); 
		}
		
		return players.get(index);
	}
	
	public int indexOf(CardPlayer player) throws InvalidArgumentException
	{
		if(players.contains(player))
			return players.indexOf(player);
		else
		{
			throw new InvalidArgumentException("this player:\n\n" + 
					player.toString() + "\n\nwas not found\n\n" + 
					this.toString()); 
		}
	}
	
	public void addPlayer(CardPlayer player)
	{
		players.add(player);
	}
	
	public void removePlayer(CardPlayer player) throws InvalidArgumentException
	{
		if(players.contains(player))
			players.remove(player);
		else
		{
			throw new InvalidArgumentException("this player:\n\n" + 
					player.toString() + "\n\nwas not found\n\n" + 
					this.toString()); 
		}
	}
	
	public int nextPlayerNumber()
	{
		if((players == null) || (players.size() == 0))
			return 1;
		else
			return players.size() + 1;
	}
	
	public int getNumberOfPlayers()
	{
		return players.size();
	}
	
	@Override
	public String toString()
	{
		String message = "Players: ";
		
		for(int i = 0; i < players.size(); i++)
			message += players.get(i).toString() + "\t";
		
		return message;
	}
	
	public static boolean playerNameIsValid(String playerName)
	{
		int counter = 0;
		char currentChar;
		
		if(playerName.length() == 0)
			return false;
		
		for(int i = 0; i < playerName.length(); i++)
		{
			currentChar = playerName.charAt(i);
			
			if((Character.isLetter(currentChar)) || 
					(Character.isDigit(currentChar)))
				counter++;
		}
		
		return counter == playerName.length();
	}
	
	public static boolean playerNamesAreValid(String playerNames[])
	{		
		for(int b = 0; b < playerNames.length; b++)
		{
			if(!playerNameIsValid(playerNames[b]))
				return false;
			
			
			for(int p = b + 1; p < playerNames.length; p++)
			{
				if(playerNames[b].equals(playerNames[p]))
					return false;
				else if(!playerNameIsValid(playerNames[p]))
					return false;
			}
		}
		
		return true;
	}
	
	private boolean duplicatePlayerNumbers(CardPlayer player)
	{
		
	}
}
