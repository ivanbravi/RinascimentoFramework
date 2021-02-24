package players.ai.factory;

import players.BasePlayerInterface;
import players.ai.explicit.OneStepLookAheadAgent;

public class OSLAFactory implements SimpleAgentFactory{
	@Override
	public BasePlayerInterface agent() {
		return new OneStepLookAheadAgent();
	}
}
