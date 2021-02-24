package players.ai.factory;

import players.ai.explicit.ExplicitPlayerInterface;
import players.ai.explicit.SafeRandomPlayer;

public class SafeRandomFactory implements SimpleAgentFactory {
	@Override
	public ExplicitPlayerInterface agent() {
		return new SafeRandomPlayer();
	}
}
