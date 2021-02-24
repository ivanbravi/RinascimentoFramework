package statistics.player;

import statistics.GameStats;
import statistics.types.NumericalStatistic;
import statistics.types.StatisticInterface;
import game.AbstractGameState;
import game.adapters.ResultStatAdapter;
import log.LoggableReady;

import java.util.HashMap;

public class ResultStats extends PlayerNumericalStatistic {

	private ResultStatAdapter adaptor;

	public HashMap<String, StatisticInterface> stats;

	public int dataCount;
	public int staleCount;

	public ResultStats(ResultStatAdapter adaptor){
		super(0);
		this.adaptor = adaptor;
		stats = new HashMap<>();
	}

	@Override
	public void add(StatisticInterface newGameStats) {
		if(newGameStats instanceof ResultStats) {
			ResultStats r = (ResultStats) newGameStats;


			if(!this.getPlayer().equals(r.getPlayer())){
				System.out.println("Player names not matching."); // this shouldn't occur
			}

			int newDataCount = this.dataCount+r.dataCount;
			this.staleCount += r.staleCount;

			for(String key: r.stats.keySet()){
				if(this.stats.containsKey(key)){
					this.stats.get(key).add(r.stats.get(key));
				}else{
					this.stats.put(key,r.stats.get(key));
				}
			}

			this.dataCount = newDataCount;
		}
	}

	@Override
	public double value() {
		return 0;
	}

	@Override
	public double error() {
		return 0;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();

		builder.append("["+getPlayer()+"] (stale:"+staleCount+" | nonstale:"+dataCount+")");
		for(String k:this.stats.keySet()){
			builder.append("\n\t"+k+":"+this.stats.get(k).value()+"+/-"+this.stats.get(k).error());
		}

		return builder.toString();
	}

	@Override
	public GameStats create(AbstractGameState gs, String player) {
		ResultStats newStat = new ResultStats(adaptor);
		newStat.setPlayer(player);

		if(adaptor.isStalemate(gs, player)){
			newStat.staleCount=1;
		}else {
			HashMap<String, Double> sts = adaptor.getStats(gs,player);
			for(String key: sts.keySet()) {
				newStat.stats.put(key,new NumericalStatistic(sts.get(key)));
			}
			newStat.dataCount = 1;
		}

		return newStat;
	}

	@Override
	public void reset() {
		super.reset();
		stats.clear();
		dataCount=0;
		staleCount=0;
	}

	public void copy(ResultStats rs){
		super.copy(rs);
		for(String k: rs.stats.keySet())
			this.stats.put(k,rs.stats.get(k).clone());
		this.dataCount = rs.dataCount;
		this.staleCount = rs.staleCount;
	}


	@Override
	public void jsonReady() {
		super.jsonReady();
		for(String k: stats.keySet()){
			StatisticInterface v = stats.get(k);
			if(v instanceof LoggableReady){
				((LoggableReady)v).jsonReady();
			}
		}
	}

	@Override
	public GameStats clone() {
		ResultStats clone = new ResultStats(adaptor);
		clone.copy(this);
		return clone;
	}

}
