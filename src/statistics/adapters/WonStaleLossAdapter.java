package statistics.adapters;

import game.AbstractGameState;

public interface WonStaleLossAdapter {

	int STALE = 0;
	int WON = 1;
	int TIE = 2;

	int wonStaleLossReward(AbstractGameState gs, String player);

}
