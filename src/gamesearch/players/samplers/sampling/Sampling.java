package gamesearch.players.samplers.sampling;

import log.LogGroup;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class Sampling {

	private int currRoundId;
	private HashMap<Integer, ArrayList<Integer>> roundIndices = new HashMap<>();

	abstract protected int getIndex(double range, int max);

	public int indexWithLog(double range, int max){
		int index = getIndex(range,max);
		logIndex(index);
		return index;
	}

	public int index(double range, int max){
		return getIndex(range,max);
	}

	private void logIndex(int index){
		ArrayList<Integer> indices = roundIndices.get(currRoundId);
		indices.add(index);
	}

	public void beginRound(int roundId){
		currRoundId = roundId;
		roundIndices.put(roundId,new ArrayList<>());
	}

	public void endRound(int roundId){}

	public Object getLog(){
		return roundIndices;
	}
}


