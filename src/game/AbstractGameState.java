package game;

/*
* Simon's game state interface
*/

import java.util.List;
import players.*;

public interface AbstractGameState {

	AbstractGameState copy();

	// the ith entry of the actions array is the action for the ith player
	// next is used to advance the state of the game given the current
	// set of actions
	// this can either be for the 'real' game
	// or for a copy of the game to use in statistical forward planning, for example
	AbstractGameState  next(int[][] actions);

	boolean simulate(int action, int playerId);

	// the number of actions available to a player in the current state
	int[] nActions(GeneralPlayerInterface player);

	// the score for each player from the perspective of _player_
	double[] getScore();

	int getPlayersCount();
	String[] getPlayerNames();

	boolean isTerminal();

	List<Object> getGameState(GeneralPlayerInterface player);

}