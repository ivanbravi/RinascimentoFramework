package gamesearch.players.samplers.db;

import game.heuristics.LogBasedVectorHeuristic;
import game.log.converters.EventIdConverter;
import hyper.agents.factory.HeuristicAgentFactorySpace;
import log.entities.event.PlayerEventVectorLogger;
import log.heuristics.WeightedHeuristic;
import players.BasePlayerInterface;
import utils.loaders.LoadSearchSpace;

import java.util.ArrayList;
import java.util.List;

public class EventPlayersDB extends PlayersDB{

	protected ArrayList<double[]> weights;
	protected EventIdConverter idConverter;
	protected String heuristicType;
	protected int count=0;

	public EventPlayersDB(String playerName,
						  String playerType,
						  HeuristicAgentFactorySpace afs,
						  List<double[]> behaviours,
						  List<int[]> configs,
						  List<double[]> weights,
						  double[] performance,
						  ArrayList<String> metricCodes,
						  String heuristicType,
						  String converterType)
	{
		super(playerName, playerType, afs, behaviours, configs, performance, metricCodes);
		this.idConverter = LoadSearchSpace.loadConverter(converterType);
		this.heuristicType = heuristicType;
		this.weights = new ArrayList<>(weights);

		if(weights.size() != configs.size()){
			throw new RuntimeException("Incoherent counts between behaviours and configs.");
		}
	}

	@Override
	public BasePlayerInterface getAgent(int index) {
		PlayerEventVectorLogger eventLogger = new PlayerEventVectorLogger(idConverter);
		WeightedHeuristic heuristic = LoadSearchSpace.decodeWeightedHeuristicWithWeights(heuristicType, idConverter.idCount(),weights.get(index));
		LogBasedVectorHeuristic compatibleHeuristic = new LogBasedVectorHeuristic(heuristic, eventLogger);
		HeuristicAgentFactorySpace haf = ((HeuristicAgentFactorySpace) afs);
		BasePlayerInterface agent = haf.agent(config(index),compatibleHeuristic);
		agent.setName(this.playerName);
		return agent;
	}
}
