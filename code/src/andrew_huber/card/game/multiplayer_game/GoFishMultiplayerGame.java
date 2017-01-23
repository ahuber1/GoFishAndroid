package andrew_huber.card.game.multiplayer_game;

import java.util.ArrayList;

import andrew_huber.card.Card;
import andrew_huber.card.game.GoFishGame;
import andrew_huber.card.player.CardPlayer;
import andrew_huber.exceptions.InvalidArgumentException;
import andrew_huber.exceptions.NoMoreCardsInDeckException;

public class GoFishMultiplayerGame extends GoFishGame
{
	private GoFishInterface in;
	
	public GoFishMultiplayerGame(GoFishInterface in, String[] names) 
	throws InvalidArgumentException, 
			NoMoreCardsInDeckException
	{
		super(in, names);
		this.in = in;
	}
	
	@Override
	public void playGame() throws Exception
	{		
		try
		{
			findPairs();
			
			for(int i = 0; i < super.getNumberOfPlayers(); i++)
			{
				if(super.getPlayer(i).isOut())
					super.endGame();
			}
		}
		catch (InvalidArgumentException e)
		{
			in.handleException(e);
		}
		
		executeTurn();
	}
	
	private void executeTurn()
	{
		if(super.playing())
		{
			try
			{
				in.promptPlayer(super.getCurrentPlayer());
			}
			catch (Throwable e)
			{
				in.handleException(e);
			} 
		}
	}
	
	@Override
	public void submitGuess(CardPlayer playerSelected, int faceValue) 
		throws InvalidArgumentException
	{
		if(super.playing())
		{
			in.displayQuestion(playerSelected, faceValue);
			ArrayList<Card> temp = askPlayer(playerSelected, faceValue);
			
			if(temp.size() == 0)
			{
				in.goFish(super.getCurrentPlayer());
				super.goFish();
				super.findPairs(getCurrentPlayer());
				super.nextPlayer();
			}
			else
			{
				ArrayList<ArrayList<Card>> pairs = 
					new ArrayList<ArrayList<Card>>();
				pairs.add(temp);
				in.pairsFound(getCurrentPlayer(), pairs);
			}
			
			if(playerSelected.isOut() || super.getCurrentPlayer().isOut())
			{
				in.declareWinner(super.findWinner());
				super.endGame();
			}
			else
			{
				executeTurn();
			}
		}
	}
	
	@Override
	public void nextPlayer() throws InvalidArgumentException
	{
		super.nextPlayer();
	}
}
