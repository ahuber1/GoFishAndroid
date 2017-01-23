package andrew_huber.card.game.computer_game.computer_players;

import java.util.ArrayList;
import java.util.Random;

import andrew_huber.card.game.GoFishGame;
import andrew_huber.card.player.CardPlayer;
import andrew_huber.card.player.GroupOfCardPlayers;
import andrew_huber.exceptions.InvalidArgumentException;
import andrew_huber.exceptions.NoMoreCardsInDeckException;

public class GoFishComputerPlayer extends CardPlayer
{
	ArrayList<Integer> candidateList = new ArrayList<Integer>();
	
	public GoFishComputerPlayer(GroupOfCardPlayers group, String name)
		throws InvalidArgumentException, NoMoreCardsInDeckException
	{
		super(group, name, GoFishGame.CARDS_PER_HAND);
	}
	
	public void addCandidate(Integer candidate)
	{
		candidateList.add(candidate);
		System.out.println("Candidate list: " + candidateList.toString());
	}
	
	public void removeCandidate(Integer candidate)
	{		
		candidateList.remove(candidate);
		System.out.println("Candidate list: " + candidateList.toString());
	}
	
	public int findCandidate(CardPlayer otherPlayer) 
			throws InvalidArgumentException
	{		
		for(int o = 0; o < otherPlayer.getPairs().size(); o++)
		{
			for(int m = 0; m < super.getPairs().size(); m++)
			{
				if(super.getPairs().get(m).get(0).getFace() == 
					otherPlayer.getPairs().get(o).get(0).getFace())
					return super.getCardFromPair(m, 0).getFace();
			}
		}
		
		for(int c = (candidateList.size() - 1); c > -1; c--)
		{
			for(int i = 0; i < super.getHand().size(); i++)
			{
				if(candidateList.contains(super.getCardFromHand(i).getFace()))
					return super.getCardFromHand(i).getFace();
			}
		}
		
		System.out.println("Candidate list: " + candidateList.toString());
		
		Random rand = new Random(System.nanoTime());
		return super.getCardFromHand(rand.nextInt(super.getHand().size())).getFace();
	}
}
