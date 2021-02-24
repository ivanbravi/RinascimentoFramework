package players.ai.explicit.mixer;

import players.ai.explicit.ExplicitPlayerInterface;
import utils.WeightedElement;

public class WeightedPlayer extends WeightedElement {

	private ExplicitPlayerInterface player;

	public WeightedPlayer(ExplicitPlayerInterface player, double weight){
		this.player = player;
		this.setWeight(weight);
	}

	public ExplicitPlayerInterface getPlayer() {
		return player;
	}

	@Override
	protected WeightedPlayer clone() {
		return new WeightedPlayer((ExplicitPlayerInterface) this.getPlayer().clone(), this.weight());
	}
}
