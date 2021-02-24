package statistics.adapters;

import game.AbstractGameState;
import java.util.HashMap;

public interface ResultStatAdapter {

	HashMap<String, Double> getStats(AbstractGameState gs, String player);
	boolean isStalemate(AbstractGameState gs, String player);

}
