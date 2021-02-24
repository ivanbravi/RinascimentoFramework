package statistics;

import game.AbstractGameState;
import statistics.player.PlayerNumericalStatistic;
import statistics.types.StatisticInterface;

import java.util.HashMap;

public class PlayerStatsWrapper extends PlayerNumericalStatistic {
	HashMap<String, PlayerNumericalStatistic> stats = new HashMap();
	String main;

	public PlayerStatsWrapper(String player, String main, PlayerNumericalStatistic mainStat,
							  String[] other, PlayerNumericalStatistic[] otherStats){
		this.setPlayer(player);
		this.main = main;
		this.stats.put(main, mainStat);
		for(int i=0; i<other.length; i++)
			this.stats.put(other[i], otherStats[i]);
	}

	private PlayerStatsWrapper(String player, String main){
		this.setPlayer(player);
		this.main = main;
	}

	@Override
	public GameStats create(AbstractGameState gs, String player) {
		PlayerStatsWrapper p = new PlayerStatsWrapper(player, this.main);

		for(String k:this.stats.keySet()){
			PlayerNumericalStatistic newCreate = (PlayerNumericalStatistic) this.stats.get(k).create(gs,player);
			if(newCreate==null){
				System.out.print("");
			}
			p.stats.put(k, newCreate);
		}

		return p;
	}

	@Override
	public void add(StatisticInterface newGameStats) {

		if(newGameStats instanceof PlayerStatsWrapper) {
			PlayerStatsWrapper addNew = (PlayerStatsWrapper) newGameStats;
			if(this.getPlayer().equals(addNew.getPlayer()) && this.main.equals(addNew.main)) {
				for (String k : stats.keySet()) {
					GameStats curr = stats.get(k);
					GameStats addCurr = addNew.stats.get(k);
					curr.add(addCurr);
				}
			}
		}
	}

	@Override
	public double value() {
		if(stats.containsKey(main))
			stats.get(main).value();
		return 0;
	}

	public double value(String k){
		if(stats.containsKey(k))
			return stats.get(k).value();
		return 0;
	}

	@Override
	public double error() {
		if (stats.containsKey(main))
			stats.get(main).error();
		return 0;
	}

	@Override
	public void reset() {
		for(String k: stats.keySet()){
			stats.get(k).reset();
		}
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();

		for(String key: this.stats.keySet()){
			builder.append("[").append(key).append("] ");
			builder.append(this.stats.get(key).toString()).append("\n");
		}

		return builder.toString();
	}

	@Override
	public StatisticInterface clone() {
		PlayerStatsWrapper cloned = new PlayerStatsWrapper(this.getPlayer(), this.main);

		for(String k:this.stats.keySet()){
			PlayerNumericalStatistic newCreate = (PlayerNumericalStatistic) this.stats.get(k).clone();
			newCreate.setPlayer(this.getPlayer());
			cloned.stats.put(k, newCreate);
		}
		return cloned;
	}
}
