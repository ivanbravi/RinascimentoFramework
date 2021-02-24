package statistics;

import statistics.adapters.PositionAdapter;
import statistics.types.StatisticInterface;
import game.AbstractGameState;

import java.util.HashMap;

public class PositionStats implements GameStats {

	HashMap<Integer,Integer> positionCounters;
	String player;
	PositionAdapter adapter;

	public PositionStats(PositionAdapter adapter){
		this.adapter = adapter;
		positionCounters = new HashMap<>();
	}

	@Override
	public void add(StatisticInterface newGameStats) {

		if(!(newGameStats instanceof PositionStats)){
			return;
		}
		PositionStats stats = (PositionStats)newGameStats;
		for(Integer pos: (stats).positionCounters.keySet()){
			int updatedCounter = stats.positionCounters.get(pos);
			if(positionCounters.keySet().contains(pos)){
				updatedCounter += positionCounters.get(pos);
			}
			this.positionCounters.put(pos,updatedCounter);
		}

	}

	@Override
	public String toString() {
		return positionCounters.toString()+"\nValue doesn't consider stale games.";
	}

	@Override
	public double value() {
		double avgPosition = 0;
		int totEval = 0;
		for(Integer pos:positionCounters.keySet()){
			if (pos>0) {
				int count = positionCounters.get(pos);
				avgPosition += count * pos;
				totEval += count;
			}
		}

		avgPosition = avgPosition/totEval;

		return avgPosition;
	}

	@Override
	public double error() {
		return 0;
	}

	@Override
	public GameStats create(AbstractGameState gs, String player) {
		PositionStats stats = new PositionStats(adapter);

		stats.player = player;
		stats.positionCounters.put(adapter.position(gs,player),1);

		return stats;
	}

	@Override
	public void reset() {
		positionCounters.clear();
	}

	@Override
	public GameStats clone() {
		PositionStats clone = new PositionStats(adapter);
		clone.positionCounters = (HashMap<Integer, Integer>) this.positionCounters.clone();
		return clone;
	}

	@Override
	public String getPlayer() {
		return player;
	}
}
