package game.heuristics;

import game.AbstractGameState;
import game.BudgetExtendedGameState;
import game.state.Result;
import game.state.State;

public class PointsPositionHeuristic implements Heuristic {

	private final int winBonus = 100;

	@Override
	public double value(BudgetExtendedGameState state, int playerID) {
		State s = state.getState();
		Result r = new Result(s);
		if (r.s== Result.STATE.RUNNING){
			return r.points[playerID];
		}
		if(r.s == Result.STATE.STALE){
			return 0;
		}
		if(r.s == Result.STATE.OVER){
			return winBonus+r.position.length-r.position[playerID];
		}
		return 0;
	}

	@Override
	public Heuristic clone() {
		return new PointsPositionHeuristic();
	}

	@Override
	public String toString() {
		return "[Heuristic]\n"+getClass().getSimpleName()+"\nwin bonus: "+winBonus;
	}

}
