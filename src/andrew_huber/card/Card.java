package andrew_huber.card;

import andrew_huber.exceptions.InvalidArgumentException;

/**
 * Creates a particular card within a standard deck of 
 * cards
 * @author andrew_huber
 *
 */
public class Card implements Comparable<Card>
{
	/**
	 * Numeric constant representing the {@link #face} 
	 * value of two
	 */
	public static final int TWO        = 1;
	
	/**
	 * Numeric constant representing the {@link #face} 
	 * value of three
	 */
	public static final int THREE      = TWO    + 1;
	
	/**
	 * Numeric constant representing the {@link #face} 
	 * value of four
	 */
	public static final int FOUR       = THREE  + 1;
	
	/**
	 * Numeric constant representing the {@link #face} 
	 * value of five
	 */
	public static final int FIVE       = FOUR   + 1;
	
	/**
	 * Numeric constant representing the {@link #face} 
	 * value of six
	 */
	public static final int SIX        = FIVE   + 1;
	
	/**
	 * Numeric constant representing the {@link #face} 
	 * value of seven
	 */
	public static final int SEVEN      = SIX    + 1;
	
	/**
	 * Numeric constant representing the {@link #face} 
	 * value of eight
	 */
	public static final int EIGHT      = SEVEN  + 1;
	
	/**
	 * Numeric constant representing the {@link #face} 
	 * value of nine
	 */
	public static final int NINE       = EIGHT  + 1;
	
	/**
	 * Numeric constant representing the {@link #face} 
	 * value of ten
	 */
	public static final int TEN        = NINE   + 1;
	
	/**
	 * Numeric constant representing the {@link #face} 
	 * value of jack
	 */
	public static final int JACK       = TEN    + 1;
	
	/**
	 * Numeric constant representing the {@link #face} 
	 * value of queen
	 */
	public static final int QUEEN      = JACK   + 1;
	
	/**
	 * Numeric constant representing the {@link #face} 
	 * value of king
	 */
	public static final int KING       = QUEEN  + 1;
	
	/**
	 * Numeric constant representing the {@link #face} 
	 * value of ace
	 */
	public static final int ACE        = KING   + 1;
	
	/**
	 * Numeric constant representing the {@link #suit} 
	 * value of spades
	 */
	public static final int SPADES     = ACE    + 1;
	
	/**
	 * Numeric constant representing the {@link #suit} 
	 * of clubs
	 */
	public static final int CLUBS      = SPADES + 1;
	
	/**
	 * Numeric constant representing the {@link #suit} 
	 * of hearts
	 */
	public static final int HEARTS     = CLUBS  + 1;
	
	/**
	 * Numeric constant representing the {@link #suit}
	 * of diamonds
	 */
	public static final int DIAMONDS   = HEARTS + 1;
	
	/**
	 * Array holding the face values from least to greatest order
	 */
	public static final int FACES[] = {Card.ACE, Card.TWO, Card.THREE, Card.FOUR, 
			 Card.FIVE, Card.SIX, Card.SEVEN, Card.EIGHT, 
			 Card.NINE, Card.TEN, Card.JACK, Card.QUEEN, 
			 Card.KING};

	/**
	 * Array holding the suit values from least to greatest order
	 */
	public static final int SUITS[] = {Card.CLUBS, Card.DIAMONDS, Card.HEARTS, 
			 Card.SPADES};
	
	/**
	 * Integer value holding a face value of a Card. 
	 * It can only hold one of the values shown in 
	 * the SEE ALSO section
	 * @see #TWO
	 * @see #THREE
	 * @see #FOUR
	 * @see #FIVE
	 * @see #SIX
	 * @see #SEVEN
	 * @see #EIGHT
	 * @see #NINE
	 * @see #TEN
	 * @see #JACK
	 * @see #QUEEN
	 * @see #KING
	 * @see #ACE
	 */
	private int face;
	
	/**
	 * Integer value holding a suit value of a Card. 
	 * It can only hold one of the values shown in
	 * the SEE ALSO section
	 * @see #SPADES
	 * @see #CLUBS
	 * @see #HEARTS
	 * @see #DIAMONDS
	 */
	private int suit;
	
	/**
	 * Creates the Card
	 * @param face The {@link #face} value of the Card.
	 * @param suit The {@link #suit} value of the Card.
	 * @throws InvalidArgumentException In case an error 
	 * occurs
	 * @see #face
	 * @see #suit
	 * @see #TWO
	 * @see #THREE
	 * @see #FOUR
	 * @see #FIVE
	 * @see #SIX
	 * @see #SEVEN
	 * @see #EIGHT
	 * @see #NINE
	 * @see #TEN
	 * @see #JACK
	 * @see #QUEEN
	 * @see #KING
	 * @see #ACE
	 * @see #SPADES
	 * @see #CLUBS
	 * @see #HEARTS
	 * @see #DIAMONDS
	 */
	public Card(int face, int suit) 
		throws InvalidArgumentException
	{
		setFace(face); // sets the suit value for the 
					   // Card
		setSuit(suit); // sets the face value for the 
					   // Card
	}
	
	/**
	 * This creates a {@link Card} based on a {@link String} description that 
	 * follows the following syntax: 
	 * {@code  <faceAsArabicNumber><seperator><of><seperator><suit>}
	 * @param description {@link String} description that follows the following 
	 * syntax: {@code  <faceAsArabicNumber><seperator><of><seperator><suit>} 
	 * @param seperator Character on either side of the word {@code of} in the
	 * {@link String}
	 * @throws InvalidArgumentException In case an invalid {@link String} was
	 * passed
	 */
	public Card(String description, char seperator) throws InvalidArgumentException
	{
		description = description.replace((seperator + "of" + seperator), ",");
		int seperatorIndex = description.indexOf(',');
		String faceString = description.substring(0, seperatorIndex);
		String suitString = description.substring(seperatorIndex + 1, 
				description.length());
		faceString = faceString.trim();
		suitString = suitString.trim();
		String errorString = "Invalid String passed " + 
							"into Card constructor\nDescription = " + 
							 description + "\nSeperator = " + seperator;
		int face;
		int suit;
		
		try
		{
			face = Integer.parseInt(faceString);
			
			switch(face)
			{
			case 2:
				face = TWO;
				break;
			case 3:
				face = THREE;
				break;
			case 4:
				face = FOUR;
				break;
			case 5:
				face = FIVE;
				break;
			case 6:
				face = SIX;
				break;
			case 7:
				face = SEVEN;
				break;
			case 8:
				face = EIGHT;
				break;
			case 9:
				face = NINE;
				break;
			case 10:
				face = TEN;
				break;
			default:
				throw new InvalidArgumentException(errorString);
			}
		}
		catch (NumberFormatException e)
		{
			if(faceString.equalsIgnoreCase("ace"))
				face = ACE;
			else if(faceString.equalsIgnoreCase("jack"))
				face = JACK;
			else if(faceString.equalsIgnoreCase("queen"))
				face = QUEEN;
			else if(faceString.equalsIgnoreCase("king"))
				face = KING;
			else
				throw new InvalidArgumentException(errorString);
		}
		
		if(suitString.equalsIgnoreCase("clubs"))
			suit = CLUBS;
		else if(suitString.equalsIgnoreCase("spades"))
			suit = SPADES;
		else if(suitString.equalsIgnoreCase("diamonds"))
			suit = DIAMONDS;
		else if(suitString.equalsIgnoreCase("hearts"))
			suit = HEARTS;
		else
			throw new InvalidArgumentException(errorString);
		
		setFace(face);
		setSuit(suit);
	}
	
	@Override
	/**
	 * Returns a String representing the contents of 
	 * this Card 
	 * @return a String representing the contents of 
	 * this Card
	 */
	public String toString()
	{
		String face; // String storing the face value 
		             // of this card
		String suit; // String storing the suit value 
		             // of this card
		
		switch(this.face) // decides what face value has
						  // been assigned to this Card 
						  //and assigns 
						  // the appropriate value to 
						  //the String object called 
						  // face 
		{
			case TWO:
			{
				face = "2";
				break;
			}
			case THREE:
			{
				face = "3";
				break;
			}
			case FOUR:
			{
				face = "4";
				break;
			}
			case FIVE:
			{
				face = "5";
				break;
			}
			case SIX:
			{
				face = "6";
				break;
			}
			case SEVEN:
			{
				face = "7";
				break;
			}
			case EIGHT:
			{
				face = "8";
				break;
			}
			case NINE:
			{
				face = "9";
				break;
			}
			case TEN:
			{
				face = "10";
				break;
			}
			case JACK:
			{
				face = "Jack";
				break;
			}
			case QUEEN:
			{
				face = "Queen";
				break;
			}
			case KING:
			{
				face = "King";
				break;
			}
			case ACE:
			{
				face = "Ace";
				break;
			}
			default:
				face = "ERROR";
		}
		
		switch(this.suit) /* decides what suit value 
							 has been assigned to this 
							 Card and assigns the  
		  				     appropriate value to the 
		  				     String object called suit 
		  				  */ 
		{
			case HEARTS:
			{
				suit = "Hearts";
				break;
			}
			case DIAMONDS:
			{
				suit = "Diamonds";
				break;
			}
			case CLUBS:
			{
				suit = "Clubs";
				break;
			}
			case SPADES:
			{
				suit = "Spades";
				break;
			}
			default:
				suit = "ERROR";
		}
		
		return face + " of " + suit; /* returns the 
									    String
									 */
	}
	
	/**
	 * Sets the {@link #face} value of this Card
	 * @param face The {@link #face} value of this card
	 * @throws InvalidArgumentException In case an 
	 * invalid value was passed
	 * @see #face
	 */
	private void setFace(int face) 
		throws InvalidArgumentException
	{
		if((face == TWO) || (face == THREE) || 
				(face == FOUR) || (face == FIVE) ||
				(face == SIX) || (face == SEVEN) || 
				(face == EIGHT) || (face == NINE) || 
				(face == TEN) || (face == JACK) || 
				(face == QUEEN) || (face == KING) || 
				(face == ACE)) /* tests for a valid
								  value
							   */
																					  
	        this.face = face;
		else
			throw new InvalidArgumentException("Invalid " + 
					"face value."); /* if an invalid
					 					value was
					 				*/

	}
	
	/**
	 * Sets the {@link #suit} value of this Card
	 * @param suit The {@link #suit} value of this card
	 * @throws InvalidArgumentException In case an 
	 * invalid value was passed
	 * @see #suit
	 */
	private void setSuit(int suit) 
		throws InvalidArgumentException
	{
		if((suit == SPADES) || (suit == CLUBS) || 
				(suit == HEARTS) || 
				(suit == DIAMONDS)) /* tests for a valid
									   suit value
									*/
										
			this.suit = suit;
		else
			throw new InvalidArgumentException("Invalid " + 
					"suit value."); /* if an invalid 
									   value was
									*/
	}
	
	/**
	 * Gets the {@link #face} value of this Card
	 * @return {@link #face} value of this Card
	 * @see #face
	 */
	public int getFace()
	{
		return face;
	}
	
	/**
	 * Gets the {@link #suit} value of this Card
	 * @return {@link #suit} value of this Card
	 * @see #suit
	 */
	public int getSuit()
	{
		return suit;
	}
	
	@Override 
	public boolean equals(Object o)
	{
		Card card = (Card) o;
		
		return ((card.getFace() == this.getFace()) && 
			   (card.getSuit() == this.getSuit()));
	}

	@Override
	public int compareTo(Card card) 
	{
		int faceResult = indexOf(this.getFace(), true) - 
			indexOf(card.getFace(), true);
		int suitResult = indexOf(this.getSuit(), false) - 
			indexOf(card.getSuit(), false);
		
		if(faceResult != 0)
			return faceResult;
		else
		{
			if(suitResult != 0)
				return suitResult;
			else
				return 0;
		}
	}
	
	private int indexOf(int value, boolean face)
	{
		if(face)
		{
			for(int i = 0; i < FACES.length; i++)
			{
				if(FACES[i] == value)
					return i;
			}
			
			return -1;
		}
		else
		{
			for(int i = 0; i < SUITS.length; i++)
			{
				if(SUITS[i] == value)
					return i;
			}
			
			return -1;
		}
	}
}
