package game.heuristics;

import game.BudgetExtendedGameState;

public interface Heuristic {

	double value(BudgetExtendedGameState state, int playerID);
	default void ground(BudgetExtendedGameState state, int playerID){}

	Heuristic clone();

}
