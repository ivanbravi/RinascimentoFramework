package game.heuristics;

import game.AbstractGameState;
import game.BudgetExtendedGameState;
import game.state.State;

public class PointsHeuristic implements Heuristic {
	@Override
	public double value(BudgetExtendedGameState state, int playerID) {
		State s = state.getState();
		return s.getPlayerState(playerID).points;
	}

	@Override
	public Heuristic clone() {
		return new PointsHeuristic();
	}

	@Override
	public String toString() {
		return getClass().getSimpleName();
	}

}
