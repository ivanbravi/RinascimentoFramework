package game.log;

import game.state.Deck;
import game.state.Noble;
import game.state.PlayerState;
import game.state.State;

import java.util.HashMap;

public class ConsoleStateEventDispatcher extends RStateEventDispatcher {

	private static ConsoleStateEventDispatcher logger = new ConsoleStateEventDispatcher();

	private ConsoleStateEventDispatcher(){

	}

	public static ConsoleStateEventDispatcher logger(){
		return logger;
	}


	private void head(){
		System.out.println();
	}

	@Override
	public void nobleTaken(State s, Noble n) {
		head();
		System.out.println("Noble taken "+n.getId());
	}

	@Override
	public void increaseTokens(State s, int stackId, int amount) {
		if(amount!=0) {
			head();
			System.out.println("Token [" + stackId + "] +" + amount);
		}
	}

	@Override
	public void decreaseTokens(State s, int stackId, int amount) {
		if(amount!=0) {
			head();
			System.out.println("Token [" + stackId + "] -" + amount);
		}
	}

	@Override
	public void increaseGold(State s, int amount) {
		if(amount!=0) {
			head();
			System.out.println("Gold +" + amount);
		}
	}

	@Override
	public void decreaseGold(State s, int amount) {
		if(amount!=0) {
			head();
			System.out.println("Gold -" + amount);
		}
	}

	@Override
	public void fillCard(State s, int deckId, int position, int cardId) {
		if(cardId == Deck.voidId) {
			head();
			System.out.println("Card id " + cardId + " placed" + "[" + deckId + "][" + position + "]");
		}
	}

	@Override
	public void drawCard(State s, int deckId, int newCardId){
		if(newCardId == Deck.voidId) {
			head();
			System.out.println("Card " + newCardId + " drawn from deck " + deckId);
		}
	}

	@Override
	public void fillNoble(State s, Noble n) {
		head();
		System.out.println("Noble on table "+n.getId());
	}

	public void head(PlayerState ps){
		head();
		System.out.print("[P"+(ps!=null?ps.getId():" ")+"] ");
	}

	@Override
	public void increasePlayerTokens(State s, PlayerState ps, int stackId, int amount) {
		if (amount != 0) {
			head(ps);
			System.out.println("Token [" + stackId + "] +" + amount);
		}
	}

	@Override
	public void decreasePlayerTokens(State s, PlayerState ps, int stackId, int amount) {
		if(amount!=0) {
			head(ps);
			System.out.println("Token [" + stackId + "] -" + amount);
		}
	}

	@Override
	public void increasePlayerGold(State s, PlayerState ps, int amount) {
		if(amount!=0) {
			head(ps);
			System.out.println("Gold +" + amount);
		}
	}

	@Override
	public void decreasePlayerGold(State s, PlayerState ps, int amount) {
		if(amount!=0) {
			head(ps);
			System.out.println("Gold -" + amount);
		}
	}

	@Override
	public void reserveHiddenCard(State s, PlayerState ps, int deckId, int cardId) {
		if(cardId == Deck.voidId) {
			head(ps);
			System.out.println("Reserve Hidden (" + deckId + "," + cardId + ")");
		}
	}

	@Override
	public void reserveBoardCard(State s, PlayerState ps, int deckId, int cardId) {
		if(cardId == Deck.voidId) {
			head(ps);
			System.out.println("Reserve Board (" + deckId + "," + cardId + ")");
		}
	}

	@Override
	public void receiveNoble(State s, PlayerState ps, int nId) {
		head(ps);
		System.out.println("Noble "+nId+" received");
	}

	@Override
	public void receiveCardBonus(State s, PlayerState ps, int suitId, int amount) {
		if(amount!=0) {
			head(ps);
			System.out.println("Bonus [" + suitId + "] +" + amount);
		}
	}

	@Override
	public void receiveCardPoints(State s, PlayerState ps, int points) {
		if(points!=0) {
			head(ps);
			System.out.println("PrestigePoints +" + points + " from card");
		}
	}

	@Override
	public void receiveNoblePoints(State s, PlayerState ps, int points) {
		if(points!=0) {
			head(ps);
			System.out.println("PrestigePoints +" + points + " from noble");
		}
	}

	@Override
	public void aux(State s, PlayerState ps, HashMap<String,Object> args) {
		head(ps);
		System.out.print("AUX: "+args.toString());
	}

}
