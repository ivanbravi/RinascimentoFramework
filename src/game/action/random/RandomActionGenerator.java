package game.action.random;

import game.action.PlayableAction;
import game.state.State;
import utils.WeightedElement;

public abstract class RandomActionGenerator extends WeightedElement {

	public abstract PlayableAction generate(State s, int playerId);
	public abstract RandomActionGenerator clone();

}
