package game.log;

import game.state.Noble;
import game.state.PlayerState;
import game.state.State;

public class DummyStateEventDispatcher extends RStateEventDispatcher {

	private static DummyStateEventDispatcher dummy = new DummyStateEventDispatcher();

	private DummyStateEventDispatcher(){

	}

	public static DummyStateEventDispatcher get(){
		return dummy;
	}

	@Override
	public void nobleTaken(State s, Noble n) {}

	@Override
	public void increaseTokens(State s, int stackId, int amount) {}

	@Override
	public void decreaseTokens(State s, int stackId, int amount) {}

	@Override
	public void increaseGold(State s, int amount) {}

	@Override
	public void decreaseGold(State s, int amount) {}

	@Override
	public void drawCard(State s, int deckId, int newCardId) {}

	@Override
	public void fillCard(State s, int deckId, int position, int cardId) {}

	@Override
	public void fillNoble(State s, Noble n) {}

	@Override
	public void increasePlayerTokens(State s, PlayerState ps, int stackId, int amount) {	}

	@Override
	public void decreasePlayerTokens(State s, PlayerState ps, int stackId, int amount) {}

	@Override
	public void increasePlayerGold(State s, PlayerState ps, int amount) {}

	@Override
	public void decreasePlayerGold(State s, PlayerState ps, int amount) {}

	@Override
	public void reserveHiddenCard(State s, PlayerState ps, int deckId, int cardId) {}

	@Override
	public void reserveBoardCard(State s, PlayerState ps, int deckId, int cardId) {}

	@Override
	public void receiveNoble(State s, PlayerState ps, int nId) {}

	@Override
	public void receiveCardBonus(State s, PlayerState ps, int suitId, int amount) {}

	@Override
	public void receiveCardPoints(State s, PlayerState ps, int points) {}

	@Override
	public void receiveNoblePoints(State s, PlayerState ps, int points) {}
}
