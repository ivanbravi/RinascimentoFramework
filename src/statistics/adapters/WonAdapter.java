package statistics.adapters;

import game.AbstractGameState;

public interface WonAdapter {

	boolean hasPlayerWon(AbstractGameState gs, String player);

}
