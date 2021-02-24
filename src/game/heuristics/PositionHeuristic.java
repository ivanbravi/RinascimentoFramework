package game.heuristics;

import game.AbstractGameState;
import game.BudgetExtendedGameState;
import game.state.Result;
import game.state.State;

public class PositionHeuristic implements Heuristic {

	private int targetPosition;

	public PositionHeuristic(int position){
		targetPosition = position;
	}

	@Override
	public double value(BudgetExtendedGameState state, int playerID) {
		State s = state.getState();
		Result r = new Result(s);
		if(r.isGameOver){
			if(r.position[playerID]==targetPosition){
				return 1;
			}
		}
		return 0;
	}

	@Override
	public Heuristic clone() {
		return new PositionHeuristic(targetPosition);
	}

	@Override
	public String toString() {
		return "[Heuristic]\n"+getClass().getSimpleName()+"\ntarget position: "+targetPosition;
	}

}
