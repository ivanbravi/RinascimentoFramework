package gamesearch.players.samplers.db;

import gamesearch.players.samplers.PlayersSampler;
import gamesearch.players.samplers.sampling.Sampling;
import players.BasePlayerInterface;

public class PlayersDBSampler extends PlayersSampler {

	PlayersDB db;
	Sampling smpl;

	public PlayersDBSampler(PlayersDB db, Sampling smpl){
		this.db = db;
		this.smpl = smpl;
	}

	public double[] getBehaviour(double range){
		return db.behaviour(getIndex(range));
	}

	public double getPerformance(double range){
		return db.performance(getIndex(range));
	}

	private int getIndex(double range){
		return smpl.index(range,db.size());
	}

	private int getIndexWithLog(double range){
		return smpl.indexWithLog(range,db.size());
	}

	@Override
	protected BasePlayerInterface createPlayer(double range) {
		return db.getAgent(getIndexWithLog(range));
	}

	@Override
	public void beginRound(int roundId) {
		smpl.beginRound(roundId);
	}

	@Override
	public void endRound(int roundId) {
		smpl.endRound(roundId);
	}

	@Override
	public Object getLog() {
		return smpl.getLog();
	}
}
