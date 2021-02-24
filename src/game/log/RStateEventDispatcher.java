package game.log;

import game.state.*;
import log.entities.event.StatefulEventLogger;

import java.util.HashMap;

public abstract class RStateEventDispatcher extends StatefulEventLogger {

	abstract public void nobleTaken				(State s, Noble n);
	abstract public void increaseTokens			(State s, int stackId, int amount);
	abstract public void decreaseTokens			(State s, int stackId, int amount);
	abstract public void increaseGold			(State s, int amount);
	abstract public void decreaseGold			(State s, int amount);
	abstract public void drawCard				(State s, int deckId, int newCardId);
	abstract public void fillCard				(State s, int deckId, int position, int cardId);
	abstract public void fillNoble				(State s, Noble n);

	abstract public void increasePlayerTokens	(State s, PlayerState ps, int stackId, int amount);
	abstract public void decreasePlayerTokens	(State s, PlayerState ps, int stackId, int amount);
	abstract public void increasePlayerGold		(State s, PlayerState ps, int amount);
	abstract public void decreasePlayerGold		(State s, PlayerState ps, int amount);
	abstract public void reserveHiddenCard		(State s, PlayerState ps, int deckId, int cardId);
	abstract public void reserveBoardCard		(State s, PlayerState ps, int deckId, int cardId);
	abstract public void receiveNoble			(State s, PlayerState ps, int nId);
	abstract public void receiveCardBonus		(State s, PlayerState ps, int suitId, int amount);
	abstract public void receiveCardPoints		(State s, PlayerState ps, int points);
	abstract public void receiveNoblePoints		(State s, PlayerState ps, int points);

	void aux(State s, PlayerState ps, HashMap<String,Object> args){}
	public RStateEventDispatcher copy(){return this;}

}
