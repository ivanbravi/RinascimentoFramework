package hyper.agents.factory;

import players.BasePlayerInterface;
import players.ai.factory.SimpleAgentFactory;

import java.util.Arrays;

public class SearchSpaceBasedFactory implements SimpleAgentFactory {

	int[] config;
	AgentFactorySpace afs;

	String suffix;

	public SearchSpaceBasedFactory(AgentFactorySpace afs, int[] config){
		this.afs = afs;
		this.config = Arrays.copyOf(config,config.length);
		suffix = "";
	}

	public SearchSpaceBasedFactory(AgentFactorySpace afs, int[] config, String suffix){
		this.afs = afs;
		this.config = Arrays.copyOf(config,config.length);
		this.suffix = suffix;
	}

	@Override
	public BasePlayerInterface agent() {
		BasePlayerInterface agent = afs.agent(config);
		agent.setName(agent.getName()+suffix);
		return agent;
	}
}
