package gamesearch.players.samplers;

import hyper.agents.factory.AgentFactory;
import players.BasePlayerInterface;

public class AgentFactorySampler extends PlayersSampler{

	private AgentFactory af;

	public AgentFactorySampler(AgentFactory af){
		this.af = af;
	}

	@Override
	protected BasePlayerInterface createPlayer(double range) {
		return af.agent();
	}

	@Override
	public void beginRound(int roundId) {

	}

	@Override
	public void endRound(int roundId) {

	}

	@Override
	public Object getLog() {
		return null;
	}
}
