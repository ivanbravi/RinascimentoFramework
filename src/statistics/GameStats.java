package statistics;

import statistics.types.StatisticInterface;
import game.AbstractGameState;

public interface GameStats extends StatisticInterface {

	GameStats create(AbstractGameState gs, String player);
	//GameStats clone();
	String getPlayer();

}
