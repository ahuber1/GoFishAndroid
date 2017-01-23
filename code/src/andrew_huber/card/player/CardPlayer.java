package andrew_huber.card.player;

import java.util.ArrayList;

import andrew_huber.card.Card;
import andrew_huber.exceptions.InvalidArgumentException;
import andrew_huber.exceptions.NoMoreCardsInDeckException;

public class CardPlayer 
{
	
	private GroupOfCardPlayers group;
	private ArrayList<Card> hand;
	private ArrayList<ArrayList<Card>> pairs = new ArrayList<ArrayList<Card>>();
	private String name;
	private int playerNumber;
	
	public CardPlayer(ArrayList<Card> hand, String name, int playerNumber)
	{
		this.hand = hand;
		this.name = name;
		this.playerNumber = playerNumber;
	}
	
	public CardPlayer(GroupOfCardPlayers group, String name, int handSize) 
		throws InvalidArgumentException, NoMoreCardsInDeckException
	{
		this.group = group;
		this.name = name;
		playerNumber = group.nextPlayerNumber();
		newHand(handSize);
	}
	
	/**
	 * Adds a new hand in collection of {@link #hands} 
	 * @throws NoMoreCardsInDeckException 
	 */
	public ArrayList<Card> newHand(int size) throws InvalidArgumentException, 
		NoMoreCardsInDeckException
	{
		hand = group.newHand(size);
		return hand;
	}

	public void discardHand(ArrayList<Card> hand) 
		throws InvalidArgumentException
	{
		group.discardHand(hand);
	}
	
	public void addCardToHand() throws InvalidArgumentException, 
		NoMoreCardsInDeckException
	{
		hand.add(group.dealCard());
	}
	
	public void removeCardFromHand(Card card) throws InvalidArgumentException
	{
		if(hand.contains(card))
			hand.remove(card);
		else
		{
			throw new InvalidArgumentException("this card (" + card.toString() +
					" was not found\n\n" + this.toString());
		}
	}
	
	public void removeCardFromHand(int index) throws InvalidArgumentException
	{
		if((index < 0) || (index >= hand.size()))
		{
			throw new InvalidArgumentException("no card was found at " + 
					"position " + Integer.toString(index) + "\n\n" + 
					this.toString());
		}
		else
			hand.remove(index);
	}
	
	public int indexOfFaceValue(int faceValue)
	{
		for(int i = 0; i < hand.size(); i++)
		{
			if(hand.get(i).getFace() == faceValue)
				return i;
		}
		
		return -1;
	}
	
	public Card getCardFromHand(int index)
	{
		return hand.get(index);
	}
	
	public ArrayList<Card> getHand()
	{
		return hand;
	}
	
	public ArrayList<ArrayList<Card>> findPairs() 
		throws InvalidArgumentException
	{
		return findPairs(new ArrayList<ArrayList<Card>>());
	}
	
	private ArrayList<ArrayList<Card>> 
		findPairs(ArrayList<ArrayList<Card>> pairs) 
			throws InvalidArgumentException
	{
		int index;
		boolean reanalyze = false;
		
		// finds if a pair exists with a preexisting pair
		for(int i = 0; i < hand.size(); i++)
		{
			// finds the index of a matching pair
			index = this.indexOfPair(hand.get(i).getFace());
			reanalyze = false;
			
			// if a matching pair has been found
			if(index != -1)
			{
				// remove the out-of-date pair from our local list
				pairs.remove(this.pairs.get(index));
				// adds the current card in the hand to the pair
				this.pairs.get(index).add(hand.get(i));
				// removes the current card in the hand
				hand.remove(i);
				// adds this to the local list holding the pairs changed
				pairs.add(this.pairs.get(index));
				reanalyze = true;	
			}
			
			if(reanalyze)
				return findPairs(pairs);
		}
		
		// finds a new pair in a hand
		for(int a = 0; a < hand.size(); a++)
		{			
			reanalyze = false;
			
			for(int b = (a + 1); b < hand.size(); b++)
			{
				// if the search came across matching pairs
				if(hand.get(a).getFace() == hand.get(b).getFace())
				{
					// creates a temporary ArrayList to store the pair
					ArrayList<Card> temp = new ArrayList<Card>();
					// adds the cards to the pair
					temp.add(hand.get(a));
					temp.add(hand.get(b));
					// removes the cards from the hand
					hand.remove(temp.get(0));
					hand.remove(temp.get(1));
					// adds the pair to the player's stash of pairs
					this.pairs.add(temp);
					// adds the pair to the local ArrayList
					pairs.add(temp);
					reanalyze = true;
					break;
				}
			}
			
			if(reanalyze)
				return findPairs(pairs);
		}
		
		return pairs;
	}
	
	public void newPair(ArrayList<Card> pair) throws InvalidArgumentException
	{
		if(pair.size() == 1)
		{
			throw new InvalidArgumentException("the pair only contains " + 
					"one Card " + pair.toString());
		}
		else if(group.xOfAKind(pair, pair.size()).size() == pair.size())
			pairs.add(pair);
		else
		{
			throw new InvalidArgumentException("no pair was found " + 
					pair.toString() + "\n\n" + this.toString());
		}
	}
	
	public void removePair(ArrayList<Card> pair, boolean putPairBackInHand) 
		throws InvalidArgumentException
	{
		if(pairs.contains(pair))
			pairs.remove(pair);
		else
		{
			throw new InvalidArgumentException("no matching pair was found; " + 
					"cannot remove " + pair.toString() + "\n\n" + 
					this.toString());
		}
		
		if(putPairBackInHand)
		{
			for(int i = 0; i < pair.size(); i++)
				hand.add(pair.get(i));
		}
	}
	
	public void removePair(int index, boolean putPairBackInHand)
		throws InvalidArgumentException
	{
		if((index < 0) || (index >=pairs.size()))
		{
			throw new InvalidArgumentException("index out of bounds: " + 
					Integer.toString(index));
		}
		else
			pairs.remove(index);
	}
	
	public ArrayList<ArrayList<Card>> getPairs()
	{
		return pairs;
	}
	
	public ArrayList<Card> getPair(int index) throws InvalidArgumentException
	{
		if((index < 0) || (index >= pairs.size()))
		{
			throw new InvalidArgumentException("no pair was found at " + 
					"position " + Integer.toString(index) + "\n\n" + 
					this.toString()); 
		}
		else
			return pairs.get(index);
	}
	
	public Card getCardFromPair(int location, int index) 
		throws InvalidArgumentException
	{
		if((location < 0) && (location >= pairs.size()))
		{
			throw new InvalidArgumentException("invalid pair location (" + 
					Integer.toString(location) + ")" + "\n(" + printPairs() + 
					")");
		}
		else
		{
			if((pairs.get(location).size() < 0) && 
					(pairs.get(location).size() >= pairs.get(location).size()))
			{
				throw new InvalidArgumentException("invalid index (" + 
						Integer.toString(index) + ") for location " + 
						Integer.toString(location) + "\n(" + printPairs() + 
						")");
			}
			else
				return pairs.get(location).get(index);
		}
	}
	
	public String printPairs()
	{
		String message = "";
		
		for (int i = 0; i < pairs.size() ; i++)
			message += pairs.get(i).toString() + "\n";
		
		return message;
	}
	
	public int indexOfPair(int faceValue) 
		throws InvalidArgumentException
	{
		for(int i = 0; i < getPairs().size(); i++)
		{
			if(getPair(i).get(0).getFace() == faceValue)
				return i;
		}
	
		return -1;
	}

	public String getName()
	{
		return name;
	}
	
	public int getPlayerNumber()
	{
		return playerNumber;
	}
	
	public GroupOfCardPlayers getGroup()
	{
		return group;
	}
	
	@Override
	public String toString()
	{
		String message = "Player Name: " + name + 
						" (Player " + Integer.toString(playerNumber) + "\n\n" + 
						"Hand is " + hand.toString() + "\n\n" + 
						"Pairs:\n" + printPairs();
		
		
		
		return message;
	}

	public boolean isOut() 
	{
		return hand.size() == 0;
	}
}