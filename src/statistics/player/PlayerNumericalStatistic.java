package statistics.player;

import statistics.GameStats;
import statistics.types.NumericalStatistic;
import statistics.types.StatisticInterface;
import game.AbstractGameState;

public abstract class PlayerNumericalStatistic extends NumericalStatistic implements GameStats {

	private String name;

	public PlayerNumericalStatistic(){
	}

	@Override
	public void copy(StatisticInterface c){
		if(c instanceof PlayerNumericalStatistic) {
			PlayerNumericalStatistic ps = (PlayerNumericalStatistic) c;
			super.copy(ps);
			this.name = ps.name;
		}
	}

	public PlayerNumericalStatistic(double value){
		super(value);
	}

	public abstract StatisticInterface clone();

	@Override
	public abstract GameStats create(AbstractGameState gs, String player);

	public String getPlayer(){
		return name;
	}

	public void setPlayer(String player){
		this.name = player;
	}

}
