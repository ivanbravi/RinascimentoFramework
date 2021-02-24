package game.adapters;

import game.AbstractGameState;
import game.state.State;
import game.state.Result;

import java.util.HashMap;

public class ResultStatAdapter implements statistics.adapters.ResultStatAdapter {
	@Override
	public HashMap<String, Double> getStats(AbstractGameState gs, String player) {
		if(gs instanceof State){
			State s = (State) gs;
			Result r = new Result(s);
			int playerID = s.getPlayerId(player);
			HashMap<String,Double> stats = new HashMap<>();

			stats.put("position", (double) r.position[playerID]);
			stats.put("points", (double) r.points[playerID]);
			stats.put("decks", (double) r.cardsCount[playerID]);

			return stats;
		}

		return null;
	}

	@Override
	public boolean isStalemate(AbstractGameState gs, String player) {
		if(gs instanceof State){
			State s = (State) gs;
			return s.isStale();
		}
		return false;
	}
}
