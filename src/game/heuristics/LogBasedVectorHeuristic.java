package game.heuristics;

import game.BudgetExtendedGameState;
import game.log.converters.EventIdConverter;
import log.entities.event.PlayerEventVectorLogger;
import log.heuristics.WeightedHeuristic;

public class LogBasedVectorHeuristic implements Heuristic {

	protected WeightedHeuristic h;
	protected String name = "LogTester";
	protected String loggerName;
	protected PlayerEventVectorLogger evl;

	public LogBasedVectorHeuristic(WeightedHeuristic h, PlayerEventVectorLogger evl){
		this.h = h;
		this.evl = evl;
		loggerName = name+super.toString().split("@")[1];
	}

	@Override
	public void ground(BudgetExtendedGameState state, int playerID) {
		evl.reset();
		evl.setPlayer(state.getState().getPlayerNames()[playerID]);
		state.addLogger(loggerName,evl);
	}

	@Override
	public Heuristic clone() {
		LogBasedVectorHeuristic clone = new LogBasedVectorHeuristic(h.clone(),evl.copy());
		clone.name = this.name;
		return clone;
	}

	@Override
	public double value(BudgetExtendedGameState state, int playerID) {
		double[] v = evl.vector();
		return h.value(v);
	}

	@Override
	public String toString() {
		return h.toString();
	}
}
