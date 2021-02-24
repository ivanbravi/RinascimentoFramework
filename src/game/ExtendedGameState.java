package game;

import game.action.PlayableAction;

public interface ExtendedGameState extends AbstractGameState {

	PlayableAction getRandomAction(int playerId);

}
