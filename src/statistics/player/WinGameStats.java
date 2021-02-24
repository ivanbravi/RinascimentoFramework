package statistics.player;


import statistics.GameStats;
import statistics.adapters.WonAdapter;
import game.AbstractGameState;

public class WinGameStats extends PlayerNumericalStatistic {

	private WonAdapter adapter;

	public WinGameStats(WonAdapter adapter){
		super();
		this.adapter=adapter;
	}

	public WinGameStats(double value) {
		super(value);
	}

	@Override
	public GameStats clone() {
		WinGameStats clone = new WinGameStats(adapter);
		clone.copy(this);
		return clone;
	}

//	@Override
//	public String toString() {
//		return "win rate:"+value()+" err: +/-"+error();
//	}

	@Override
	public GameStats create(AbstractGameState gs, String player) {
		int r = adapter.hasPlayerWon(gs,player)?1:0;
		WinGameStats stats = new WinGameStats(r);
		stats.setPlayer(player);
		stats.adapter = this.adapter;
		return stats;
	}

}
