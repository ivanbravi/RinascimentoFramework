package gamesearch.players.samplers;

import hyper.agents.factory.AgentFactory;
import players.BasePlayerInterface;

public class TwoPlayersSampler extends PlayersSampler{

	boolean isFirstTurn = false;

	private PlayersSampler firstFactory;
	private PlayersSampler secondFactory;


	public TwoPlayersSampler(PlayersSampler firstFactory,PlayersSampler secondFactory){
		this.firstFactory = firstFactory;
		this.secondFactory = secondFactory;
	}

	@Override
	public BasePlayerInterface getPlayer(double range) {
		return createPlayer(range);
	}

	@Override
	protected BasePlayerInterface createPlayer(double range) {
		flipTurn();
		if(isFirstTurn)
			return firstFactory.getPlayer(range);
		else
			return secondFactory.getPlayer(range);
	}

	private void flipTurn(){
		isFirstTurn = !isFirstTurn;
	}

	@Override
	public void beginRound(int roundId) {

	}

	@Override
	public void endRound(int roundId) {

	}

	@Override
	public Object getLog() {
		return "Not actual sampling happening: alternating between two players!";
	}
}
