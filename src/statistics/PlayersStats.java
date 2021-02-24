package statistics;

import statistics.player.PlayerNumericalStatistic;
import statistics.types.StatisticInterface;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import game.AbstractGameState;
import log.LoggableReady;

import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;

public class PlayersStats implements GameStats, LoggableReady {

	private HashMap<String,PlayerNumericalStatistic> playersStats = new HashMap<>();
	private transient PlayerNumericalStatistic source;

	public PlayersStats(PlayerNumericalStatistic source, String... agentNames){
		this.source = source;
		for(String s:agentNames){
			PlayerNumericalStatistic ps = (PlayerNumericalStatistic) source.clone();
			ps.setPlayer(s);
			playersStats.put(s,ps);
		}
	}

	private PlayersStats(PlayerNumericalStatistic source){
		this.source = source;
	}

	@Override
	public void add(StatisticInterface newGameStats) {
		if(newGameStats instanceof PlayersStats){
			PlayersStats s = (PlayersStats) newGameStats;

			for (String key: this.playersStats.keySet())
				this.playersStats.get(key).add(s.playersStats.get(key));
		}
	}

	@Override
	public void jsonReady(){
		for(String k: playersStats.keySet())
			playersStats.get(k).jsonReady();
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

		for(String key: playersStats.keySet()){
			builder.append("["+key+"]\n"+playersStats.get(key).toString()+"\n");
		}

		return builder.toString();
	}

	@Override
	public GameStats create(AbstractGameState gs, String player) {
		PlayersStats create = new PlayersStats(this.source, this.playersStats.keySet().toArray(new String[0]));
		for(String key: playersStats.keySet()){
			create.playersStats.put(key,(PlayerNumericalStatistic)source.create(gs,key));
		}

		return create;
	}

	@Override
	public void reset() {
		for(String key: playersStats.keySet())
			playersStats.get(key).reset();
	}

	@Override
	public GameStats clone() {
		PlayersStats clone = new PlayersStats(this.source);
		for (String key: this.playersStats.keySet()) {
			PlayerNumericalStatistic cloneStat = (PlayerNumericalStatistic) this.playersStats.get(key).clone();
			cloneStat.setPlayer(key);
			clone.playersStats.put(key, cloneStat);
		}
		return clone;
	}

	@Override
	public String getPlayer() {
		return null;
	}

	public boolean export(String path){

		try (Writer w = new FileWriter(path)){
			Gson writer = new GsonBuilder().setPrettyPrinting().create();
			writer.toJson(this, w);
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}

		return true;
	}

}
