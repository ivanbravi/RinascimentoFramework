package gamesearch.players.samplers;

import hyper.agents.factory.AgentFactorySpace;
import players.BasePlayerInterface;

public class BasePlayerSampler extends PlayersSampler{

	private AgentFactorySpace afs;
	private int[] config;

	public BasePlayerSampler(AgentFactorySpace afs, int[] config){
		this.afs = afs;
		this.config = config;
	}

	@Override
	protected BasePlayerInterface createPlayer(double range) {
		return afs.agent(config);
	}

	@Override
	public void beginRound(int roundId) {

	}

	@Override
	public void endRound(int roundId) {

	}

	@Override
	public Object getLog() {
		return "[BasePlayerSampler]: no actual sampling going on!";
	}
}
