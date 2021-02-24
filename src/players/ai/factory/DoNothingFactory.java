package players.ai.factory;

import players.BasePlayerInterface;
import players.ai.explicit.DoNothingAgent;

public class DoNothingFactory implements SimpleAgentFactory {
	@Override
	public BasePlayerInterface agent() {
		return new DoNothingAgent();
	}
}
