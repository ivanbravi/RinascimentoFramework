package gamesearch.players.samplers;

import players.BasePlayerInterface;

public abstract class PlayersSampler {

	String playerName;

	public BasePlayerInterface getPlayer(double range){
		BasePlayerInterface player = createPlayer(range);
		player.setName(playerName);
		return player;
	}

	abstract protected BasePlayerInterface createPlayer(double range);
	abstract public void beginRound(int roundId);
	abstract public void endRound(int roundId);
	abstract public Object getLog();


	public void setPlayerNameName(String name){
		this.playerName = name;
	}

}
