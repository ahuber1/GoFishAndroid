package andrew_huber.card.game;

import java.util.ArrayList;

import andrew_huber.card.Card;
import andrew_huber.card.game.computer_game.computer_players.GoFishComputerPlayer;
import andrew_huber.card.game.multiplayer_game.multiplayer_players.GoFishMultiplayerPlayer;
import andrew_huber.card.player.CardPlayer;
import andrew_huber.card.player.GroupOfCardPlayers;
import andrew_huber.exceptions.InvalidArgumentException;
import andrew_huber.exceptions.NoMoreCardsInDeckException;
import andrew_huber.exceptions.PasswordLengthException;

public class GoFishGame extends GroupOfCardPlayers
{
	public static final int CARDS_PER_HAND = 7;
	public static final String COMPUTER_PLAYER_NAME = "Computer";
	private CardPlayer currentPlayer;
	private GoFishInterface in;
	private boolean playing = true;
	private int failedFaceValue;
	
	public GoFishGame(GoFishInterface in, String[] names) 
		throws InvalidArgumentException, NoMoreCardsInDeckException 
	{
		super();
		this.in = in;
		addPlayers(names);
		currentPlayer = super.getPlayer(0);
	}
	
	public GoFishGame(GoFishInterface in, String name) 
		throws InvalidArgumentException, NoMoreCardsInDeckException
	{
		super();
		this.in = in;
		super.addPlayer(new CardPlayer(this, name, CARDS_PER_HAND));
		super.addPlayer(new GoFishComputerPlayer(this, COMPUTER_PLAYER_NAME));
		currentPlayer = super.getPlayer(0);
	}
	
	private void addPlayer(String name) throws InvalidArgumentException, 
		NoMoreCardsInDeckException
	{
		super.addPlayer(new CardPlayer(this, name, CARDS_PER_HAND));
	}
	
	private void addPlayers(String[] names) throws InvalidArgumentException, 
		NoMoreCardsInDeckException
	{
		if(names.length < 2)
		{
			throw new InvalidArgumentException("needs to be at least two " + 
					"players to play a game of Go Fish");
		}
		else
		{
			for(int i = 0; i < names.length; i++)
				addPlayer(names[i]);
		}
	}
	
	public CardPlayer getCurrentPlayer()
	{
		return currentPlayer;
	}
	
	public void findPairs() throws InvalidArgumentException
	{
		CardPlayer player;
		
		for(int i = 0; i < super.getNumberOfPlayers(); i++)
		{
			player = super.getPlayer(i);
			findPairs(player);
		}
	}
	
	public ArrayList<ArrayList<Card>> findPairs(CardPlayer player) 
		throws InvalidArgumentException
	{
		ArrayList<ArrayList<Card>> pairs = player.findPairs();;

		if(pairs.size() != 0)
			in.pairsFound(player, pairs);
		
		return pairs;
	}
	
	public ArrayList<Card> askPlayer(CardPlayer playerToBeAsked, int faceValue) 
		throws InvalidArgumentException
	{
		return askPlayer(playerToBeAsked, faceValue, new ArrayList<Card>());
	}
	
	private ArrayList<Card> askPlayer(CardPlayer playerToBeAsked, int faceValue, 
			ArrayList<Card> changes) 
		throws InvalidArgumentException
	{		
		// TODO Change code to not require obtaining the below. Use
		//      static final variables to represent 
		//      currentHasCardInPairOtherHasCardInPair and others
		int indexOfCurrentPair = currentPlayer.indexOfPair(faceValue);
		int indexOfCurrentHand = currentPlayer.indexOfFaceValue(faceValue);
		int indexOfOtherPair = playerToBeAsked.indexOfPair(faceValue);
		int indexOfOtherHand = playerToBeAsked.indexOfFaceValue(faceValue);
		boolean currentHasCardInPair = indexOfCurrentPair != -1;
		boolean currentHasCardInHand = indexOfCurrentHand != -1;
		boolean otherHasCardInPair = indexOfOtherPair != -1;
		boolean otherHasCardInHand = indexOfOtherHand != -1;
		boolean currentHasCardInPairOtherHasCardInPair = 
			currentHasCardInPair && otherHasCardInPair;
		boolean currentHasCardInHandOtherHasCardInPair = 
			currentHasCardInHand && otherHasCardInPair;
		boolean currentHasCardInPairOtherHasCardInHand = 
			currentHasCardInPair && otherHasCardInHand;
		boolean currentHasCardInHandOtherHasCardInHand = 
			currentHasCardInHand && otherHasCardInHand;
		
		if((currentHasCardInPairOtherHasCardInPair == false) && 
				(currentHasCardInHandOtherHasCardInPair == false) && 
				(currentHasCardInPairOtherHasCardInHand == false) && 
				(currentHasCardInHandOtherHasCardInHand == false))
		{
			failedFaceValue = faceValue;
			return changes;
		}
		else if(currentHasCardInPairOtherHasCardInPair)
		{
			// retrieves other player's pair
			changes = playerToBeAsked.getPair(indexOfOtherPair);
			// removes the pair from the other player
			playerToBeAsked.removePair(indexOfOtherPair, false); 
			// makes an appropriate addition to the current player's 
			// prexisting pair
			currentPlayer.getPairs().get(indexOfCurrentPair).addAll(changes);
			changes = currentPlayer.getPairs().get(indexOfCurrentPair);
		}
		else if(currentHasCardInHandOtherHasCardInPair)
		{
			// retrieves the other player's pair
			changes = playerToBeAsked.getPair(indexOfOtherPair);
			// removes the pair from the other player
			playerToBeAsked.removePair(indexOfOtherPair, false);
			// adds the current player's matching card to the new pair
			changes.add(currentPlayer.getCardFromHand(indexOfCurrentHand));
			// removes the current player's matching card from their hand
			currentPlayer.removeCardFromHand(indexOfCurrentHand);
			// adds the new pair to the new current player's stash of pairs
			currentPlayer.newPair(changes);
		}
		else if(currentHasCardInPairOtherHasCardInHand)
		{
			// gets matching card from other player's hand
			Card matchingCard = playerToBeAsked.getCardFromHand(indexOfOtherHand);
			// removes matching card from other player's hand
			playerToBeAsked.removeCardFromHand(indexOfOtherHand);
			// adds the matching card to the matching pair of the current player
			currentPlayer.getPairs().get(indexOfCurrentPair).add(matchingCard);
			// adds the change
			changes = currentPlayer.getPairs().get(indexOfCurrentPair);
		}
		else
		{
			// gets matching card from other player's hand
			Card matchingCardOther = playerToBeAsked.getCardFromHand(indexOfOtherHand);
			// removes matching card from other player's hand
			playerToBeAsked.removeCardFromHand(indexOfOtherHand);
			// gets matching card from current player's hand
			Card matchingCardCurrent = currentPlayer.getCardFromHand(indexOfCurrentHand);
			// removes matching card from the current player's hand
			currentPlayer.removeCardFromHand(indexOfCurrentHand);
			// adds the cards to the ArrayList
			changes.add(matchingCardCurrent);
			changes.add(matchingCardOther);
			// adds the pair to the current player's stash of pairs
			currentPlayer.newPair(changes);
		}
		
		return askPlayer(playerToBeAsked, faceValue, changes);
	}
	
	public void playGame() throws Exception
	{
		throw new Exception("cannot call method playGame in GoFishGame."+
		" it must be called in one of GoFishGame's subclasses");
	}
	
	public void submitGuess(CardPlayer playerSelected, int faceValue)
		throws Exception, InvalidArgumentException
	{
		throw new Exception("cannot call method submitGuess in GoFishGame."+
				"it must be called in one of GoFishGame's subclasses");
	}
	
	public void endGame()
	{
		playing = false;
	}
	
	public void goFish() throws InvalidArgumentException 
	{
		try 
		{
			currentPlayer.addCardToHand();
		}
		catch (NoMoreCardsInDeckException e) 
		{
			in.noMoreCardsInDeck();
		}
	}
	
	public boolean playing()
	{
		return playing;
	}
	
	public void nextPlayer() throws InvalidArgumentException
	{
		int index = super.indexOf(currentPlayer);
		
		if((index + 1) >= super.getNumberOfPlayers())
			index = 0;
		else
			index++;
		
		currentPlayer = super.getPlayer(index);
	}

	public int getFailedFaceValue() 
	{
		return failedFaceValue;
	}
	
	public CardPlayer findWinner() throws InvalidArgumentException
	{
		CardPlayer winner = super.getPlayer(0);
		
		for(int i = 0; i < super.getNumberOfPlayers(); i++)
		{
			if(super.getPlayer(i).getPairs().size() > winner.getPairs().size())
				winner = super.getPlayer(i);
		}
		
		return winner;
	}

	public interface GoFishInterface
	{
		public void promptPlayer(CardPlayer playerToBePrompted);
		public void pairsFound(CardPlayer player, ArrayList<ArrayList<Card>> pairs);
		public void goFish(CardPlayer playerToFish) throws InvalidArgumentException;
		public void declareWinner(CardPlayer winner);
		public void handleException(Throwable t);
		public void noMoreCardsInDeck();
		public void displayQuestion(CardPlayer playerToAsk, int faceValue) 
			throws InvalidArgumentException;
	}
}
