package hyper.agents.factory;

import players.BasePlayerInterface;

public class SpaceBasedAgentFactory implements AgentFactory{
	private int[] config;
	private AgentFactorySpace afs;

	public SpaceBasedAgentFactory(AgentFactorySpace afs, int[] config){
		this.config = config;
		this.afs = afs;
	}

	@Override
	public BasePlayerInterface agent() {
		return afs.agent(config);
	}
}
