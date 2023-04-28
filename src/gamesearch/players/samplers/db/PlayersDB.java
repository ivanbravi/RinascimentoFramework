package gamesearch.players.samplers.db;

import hyper.agents.factory.AgentFactorySpace;
import players.BasePlayerInterface;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class PlayersDB {

	protected String playerType;
	protected String playerName;
	protected AgentFactorySpace afs;

	protected ArrayList<double[]> behaviours;
	protected ArrayList<int[]> configurations;
	protected double[] performance;
	protected ArrayList<String> metricCodes;

	private HashMap<String, Integer> metricIndices;

	public PlayersDB(String playerName,
					 String playerType,
					 AgentFactorySpace afs,
					 List<double[]> behaviours,
					 List<int[]> configs,
					 double[] performance,
					 ArrayList<String> metricCodes
					 ){
		this.playerName = playerName;
		this.playerType = playerType;
		this.behaviours = new ArrayList<>(behaviours);
		this.configurations = new ArrayList<>(configs);
		this.performance = performance;
		this.afs = afs;
		this.metricCodes = metricCodes;

		metricIndices = new HashMap<>();
		for(int i = 0; i< this.metricCodes.size(); i++)
			metricIndices.put(this.metricCodes.get(i), i);

		if(behaviours.size()!=configs.size() || behaviours.size()!=performance.length){
			throw new RuntimeException("Incoherent counts between behaviours and configs.");
		}
	}

	public BasePlayerInterface getAgent(int index){
		return afs.agent(configurations.get(index));
	}

//	public String getPlayerType(){ return playerType; }
	public String getPlayerName() { return playerName; }
	public int size(){ return behaviours.size(); }
	public int[] config(int index){ return configurations.get(index); }
	public double[] behaviour(int index){ return behaviours.get(index); }
	public double performance(int index){ return performance[index]; }

	public double[] behaviourFilter(int index, String[] metrics){
		double[] r = new double[metrics.length];
		double[] behaviour = behaviour(index);
		for(int i=0; i<metrics.length; i++)
			r[i] = behaviour[metricIndices.get(metrics[i])];
		return r;
	}


	@Override
	public String toString() {
		return "[PlayersDB]" +
				"\n\tplayerType = " + playerType +
				"\n\tplayerName = " + playerName +
				"\n\tafs = " + afs.getAgentType() +
				"\n\t|behaviours| = " + behaviours.size() +
				"\n\t|behaviours[0]| = " + behaviours.get(0).length +
				"\n\t|configurations| = " + configurations.size() +
				"\n\t|configurations[0]| = " + configurations.get(0).length +
				"\n\t|performance| = " + performance.length +
				"\n\tmetricNames = " + Arrays.toString(metricCodes.toArray());
	}
}
