package andrew_huber.card.game.computer_game;

import java.util.ArrayList;

import andrew_huber.card.Card;
import andrew_huber.card.game.GoFishGame;
import andrew_huber.card.game.computer_game.computer_players.GoFishComputerPlayer;
import andrew_huber.card.player.CardPlayer;
import andrew_huber.exceptions.InvalidArgumentException;
import andrew_huber.exceptions.NoMoreCardsInDeckException;

public class GoFishComputerGame extends GoFishGame
{ 
	private GoFishComputerPlayer computer;
	private CardPlayer player;
	private GoFishInterface in;
	
	public GoFishComputerGame(GoFishInterface in, String name) 
		throws InvalidArgumentException, NoMoreCardsInDeckException
	{
		super(in, name);
		player = super.getPlayer(0);
		computer = (GoFishComputerPlayer) super.getPlayer(1);
		this.in = in;
	}
	
	public GoFishComputerPlayer getComputerPlayer()
	{
		return computer;
	}
	
	public CardPlayer getPlayer()
	{
		return player;
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
				if(super.getCurrentPlayer() == computer)
				{
					int faceValue = computer.findCandidate(player);
					submitGuess(player, faceValue);
				}
				else
					in.promptPlayer(player);
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
				
				if(super.getCurrentPlayer() == computer)
					computer.removeCandidate(faceValue);
				else
					computer.addCandidate(faceValue);
				
				super.nextPlayer();				
			}
			else
			{
				ArrayList<ArrayList<Card>> pairs = 
					new ArrayList<ArrayList<Card>>();
				pairs.add(temp);
				in.pairsFound(getCurrentPlayer(), pairs);
				computer.removeCandidate(faceValue);
			}
			
			if(player.isOut() || super.getCurrentPlayer().isOut())
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
}
