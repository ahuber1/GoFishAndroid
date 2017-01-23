package andrew_huber.card;

import java.util.ArrayList;
import java.util.Random;

import andrew_huber.exceptions.InvalidArgumentException;
import andrew_huber.exceptions.NoMoreCardsInDeckException;

public class DeckOfCards
{
	/**
	 * Deck of {@link Card}s
	 */
	private ArrayList<Card> deck = new ArrayList<Card>();
	
	/**
	 * Number of cards drawn from the {@link #deck}
	 */
	private int numberOfCardsDrawn = 0;
	
	/**
	 * Array holding the face values from the {@link Card} class
	 */
	private final int FACES[] = Card.FACES;
	
	/**
	 * Array holding the suit values from the {@link Card} class
	 */
	private final int SUITS[] = Card.SUITS;
	
	/**
	 * Error message when there are no more cards to be drawn from the 
	 * {@link #deck}
	 */
	public static final String SIZE_ZERO_ERROR = "Size of deck is zero";
	
	/**
	 * Creates a new {@link #deck} of {@link Card}s
	 * @throws InvalidArgumentException In case an error occurred
	 */
	public DeckOfCards() throws InvalidArgumentException
	{		
		for(int s = 0; s < SUITS.length; s++)
		{
			for(int f = 0; f < FACES.length; f++)
				deck.add(new Card(FACES[f], SUITS[s]));
		}
		
		shuffle();
	}
	
	/**
	 * Shuffles the {@link #deck} of {@link Card}s
	 * @throws InvalidArgumentException
	 */
	public void shuffle() throws InvalidArgumentException
	{
		System.out.println("Shuffling...");
		Random rand = new Random(System.nanoTime());
		ArrayList<Card> anotherDeck = new ArrayList<Card>(deck.size());
		int currInt;
				
		while(anotherDeck.size() < deck.size())
		{
			currInt = rand.nextInt(deck.size());
			
			if(anotherDeck.contains(deck.get(currInt)))
				; // do nothing
			else
				anotherDeck.add(deck.get(currInt));
		}
		
		deck = anotherDeck;
		System.out.println("Shuffling complete!");
	}
	
	/**
	 * Deals a {@link Card} from the {@link #deck}
	 * @return The {@link Card} drawn from the {@link #deck}
	 * @throws InvalidArgumentException In case an error occurred
	 * @throws NoMoreCardsInDeckException 
	 */
	public Card dealCard() throws InvalidArgumentException, 
		NoMoreCardsInDeckException
	{
		if(!moreCards())
		{
			System.out.println("No more cards in deck");
			numberOfCardsDrawn = 0;
			shuffle();
			System.out.println("New deck has been created");
		}
		
		int nthCardDrawnFromDeckCopy = numberOfCardsDrawn;
		numberOfCardsDrawn++;
		return deck.get(nthCardDrawnFromDeckCopy);
	}
	
	/**
	 * Tests if there are more {@link Card}s to be drawn from the {@link #deck}
	 * @return {@link Boolean#TRUE true} if there are more {@link Card}s in the 
	 * {@link #deck}
	 * @throws InvalidArgumentException If the {@link deck} has no more cards
	 * to be drawn
	 * @see #SIZE_ZERO_ERROR
	 */
	public boolean moreCards() throws NoMoreCardsInDeckException 
	{
		if(deck.size() == 0)
			throw new NoMoreCardsInDeckException();
		else if(numberOfCardsDrawn >= deck.size())
			return false;
		else
			return true;
	}
	
	/**
	 * Deals a hand of {@link Card}s and tests for various poker hands
	 * @throws InvalidArgumentException In case an error occurred
	 * @throws NoMoreCardsInDeckException 
	 */
	public void dealPokerHand() throws InvalidArgumentException, 
		NoMoreCardsInDeckException
	{
		ArrayList<Card> hand = newHand(5);
		boolean sameSuits = sameSuit(hand);
		boolean fiveConsecutiveFaceValues = consecutiveFaceValues(hand, 5);
		boolean fourOfAKind = (xOfAKind(hand, 4).size() == 4);
		boolean threeOfAKind = (xOfAKind(hand, 3).size() == 3);
		boolean twoPair = xPair(hand, 2);
		boolean pair = xPair(hand, 1);
		System.out.println(hand.toString());
		
		if(fiveConsecutiveFaceValues)
			System.out.println("You have a straight!");
		else if(sameSuits)
			System.out.println("You have a flush!");
		else if(fourOfAKind)
			System.out.println("You have four of a kind!");
		else if(threeOfAKind)
			System.out.println("You have three of a kind!");
		else if(twoPair)
			System.out.println("You have two pair!");
		else if(pair)
			System.out.println("You have a pair!");
		else
			System.out.println("Sorry, but you don't have a poker hand.");
		
		discardHand(hand);
	}
	
	/**
	 * Tests for X Pair in a hand of {@link Card}s
	 * @param hand Hand of {@link Card}s to test
	 * @param x How many pairs you would like to test for. For example, if you
	 * want to test if a hand is a two pair, this value would equal 2. If you
	 * want to test if a hand has a pair, this value would equal 1.
	 * @return {@link Boolean#TRUE true} if an x pair has been found, 
	 * {@link Boolean#FALSE} if an x pair has not been found
	 */
	public boolean xPair(ArrayList<Card> hand, int x)
	{
		int counter = 0;
		
		for(int i = 0; i < FACES.length; i++)
		{
			if(findNumberOfOccurrencesOfAFaceValue(hand, FACES[i]).size() == 2)
				counter++;
		}
		
		return counter == x;
	}
	
	/**
	 * Tests for X of a Kind in a hand of {@link Card}s
	 * @param hand Hand of {@link Card}s to test
	 * @param x How much of a kind you would like to test for. For example, if
	 * you want to test if a hand contains four of a kind, this value would
	 * equal 4. If you want to test if a hand contains three of a kind, this
	 * value would equal 3.
	 * @return {@link Boolean#TRUE true} if the hand contains x of a kind, 
	 * {@link Boolean#FALSE} if the hand does not contain x of a kind
	 */
	public ArrayList<Card> xOfAKind(ArrayList<Card> hand, int x) 
	{
		ArrayList<Card> pile;
		
		for(int i = 0; i < FACES.length; i++)
		{
			pile = findNumberOfOccurrencesOfAFaceValue(hand, FACES[i]);
			
			if(pile.size() == x)
				return pile;
		}
		return new ArrayList<Card>();
	}
	
	public ArrayList<Card> findNumberOfOccurrencesOfAFaceValue(ArrayList<Card> hand, 
			int faceType) 
	{
		ArrayList<Card> pile = new ArrayList<Card>();
		
		for(int i = 0; i < hand.size(); i++)
		{
			if(hand.get(i).getFace() == faceType)
				pile.add(hand.get(i));
		}
		
		return pile;
	}
	
	/**
	 * Tests for a certain number of consecutive face values
	 * @param hand The hand to be tested
	 * @param numberOfConsecutiveCards The number of consecutive face values to
	 * be found
	 * @return {@link Boolean#TRUE true} if there are consecutive face values of
	 * a specific amount
	 */
	public boolean consecutiveFaceValues(ArrayList<Card> hand, 
			int numberOfConsecutiveCards) 
	{
		ArrayList<Card> sortedHand = new ArrayList<Card>();
		Card currCard;
		
		for(int s = 0; s < (hand.size() - 1); s++)
		{
			currCard = hand.get(s);
			
			for(int i = s + 1; i < hand.size(); i++)
			{
				if(currCard.compareTo(hand.get(i)) < 0)
					currCard = hand.get(i);
			}
			
			sortedHand.add(currCard);
		}
		
		for(int i = 0; i < hand.size(); i++)
		{
			if(!sortedHand.contains(hand.get(i)))
				sortedHand.add(hand.get(i));
		}
		
		ArrayList<Integer> list = new ArrayList<Integer>();
		
		for(int i = 0; i < sortedHand.size(); i++)
			list.add(sortedHand.get(i).getFace());
		
		for(int i = 0; i < FACES.length; i++)
		{
			if(testSpecificFaceValueForConsecutiveness(list, i, 0, 
					numberOfConsecutiveCards))
				return true;
		}
		
		return false;
	}
	
	/**
	 * Tests for a certain number of consecutive {@link Card}s
	 * @param list An {@link ArrayList} containing the face values in a 
	 * particular hand of {@link Card}s sorted by face value from least to 
	 * greatest
	 * @param index Index used to progress through the {@link #FACES faces}
	 * in a consecutive manner
	 * @param nthItemFromFaceValue Used to reach a base case. Must be 0 when
	 * initially calling this method
	 * @param numberOfConsecutiveCards Number of consecutive {@link Card}s to test. 
	 * For example, if you would like to test for five consecutive 
	 * {@link Card}s, this value would equal 5.
	 * @return {@link Boolean#TRUE true} if consecutive values have been found,
	 * {@link Boolean#FALSE false} if consecutive face values have not been 
	 * found
	 */
	private boolean testSpecificFaceValueForConsecutiveness(
			ArrayList<Integer> list, int index, int nthItemFromFaceValue,  
			int numberOfConsecutiveCards)
	{
		try
		{
			if(nthItemFromFaceValue == (numberOfConsecutiveCards - 1))
			{
				return list.contains(FACES[index]);
			}	
			else
			{
				return list.contains(FACES[index]) && 
					    testSpecificFaceValueForConsecutiveness(list, (index += 1),
					    		(nthItemFromFaceValue += 1), 
					    		numberOfConsecutiveCards);
			}
		}
		catch (ArrayIndexOutOfBoundsException e)
		{
			return false;
		}
	}
	
	/**
	 * Tests to see if the hand has cards all from the same suit
	 * @param hand The hand to be tested
	 * @return {@link Boolean#TRUE true} if the cards are all of the same suit,
	 * {@link Boolean#FALSE false} if the cards are not of the same suit
	 */
	public boolean sameSuit(ArrayList<Card> hand) 
	{
		ArrayList<Integer> list = new ArrayList<Integer>();
		
		for(int i = 0; i < hand.size(); i++)
		{
			if(!list.contains(hand.get(i).getSuit()))
				list.add(hand.get(i).getSuit());
		}
		
		return list.size() == 1;
	}
	
	/**
	 * Generates a new hand of {@link Card}s
	 * @param size Size of the hand
	 * @return The hand
	 * @throws InvalidArgumentException In case an error occurs
	 * @throws NoMoreCardsInDeckException 
	 */
	public ArrayList<Card> newHand(int size) throws InvalidArgumentException, 
		NoMoreCardsInDeckException
	{
		if(size <= 0)
		{
			throw new InvalidArgumentException("cannot make a hand of size " + 
					Integer.toString(size));
		}
		
		ArrayList<Card> hand = new ArrayList<Card>(size);
		Card currentCard;
		
		for(int i = 0; i < size; i++)
		{
			currentCard = dealCard();
			hand.add(currentCard);
			deck.remove(currentCard);
		}
		
		return hand;
	}
	
	/**
	 * Discards a hand of {@link Card}s by putting them back in the 
	 * {@link #deck}
	 * @param hand The hand to be discarded
	 * @throws InvalidArgumentException In case an error occurs
	 */
	public void discardHand(ArrayList<Card> hand) 
		throws InvalidArgumentException
	{
		for(int i = 0; i < hand.size(); i++)
			deck.add(deck.size(), hand.get(i));
		
		numberOfCardsDrawn -= hand.size();
		hand = null;
	}
}