package statistics.player;

import statistics.GameStats;
import statistics.adapters.WonStaleLossAdapter;
import game.AbstractGameState;

public class WinStaleLostStats extends PlayerNumericalStatistic {

	transient WonStaleLossAdapter adapter;

	public WinStaleLostStats(WonStaleLossAdapter adapter){
		super();this.adapter = adapter;
	}

	public WinStaleLostStats(double value) {
		super(value);
	}

	@Override
	public GameStats clone() {
		WinStaleLostStats clone = new WinStaleLostStats(adapter);
		clone.copy(this);
		return clone;
	}

	@Override
	public GameStats create(AbstractGameState gs, String player) {
		double value = adapter.wonStaleLossReward(gs,player);
		WinStaleLostStats stat = new WinStaleLostStats(value);
		stat.setPlayer(player);
		return stat;
	}
}
