package statistics.adapters;

import game.AbstractGameState;

public interface WonStaleLossAdapter {

	int wonStaleLossReward(AbstractGameState gs, String player);

}
