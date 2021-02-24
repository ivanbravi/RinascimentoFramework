package game;

import game.action.PlayableAction;

public interface SeededExtendedGameState extends AbstractGameState {

	PlayableAction getRandomAction(int playerId, long seed);

}
