package mapelites;

import benchmarks.RinascimentoLoggedEnv;
import mapelites.interfaces.Solution;
import org.jetbrains.annotations.NotNull;
import players.BasePlayerInterface;
import players.ai.explicit.RandomPlayer;
import statistics.PlayerStatsWrapper;
import statistics.player.PlayerNumericalStatistic;

import java.util.ArrayList;

public class FitnessFunction implements mapelites.interfaces.FitnessFunction {

	public final static String playerName = "RFFSimulation";
	private final static String fitnessStatName = "fitness";
	private final static String behaviourPrefix = "behaviour";
	private final static String supportPrefix = "support";

	private RinascimentoLoggedEnv env;
	private BehavioursFunction bf;
	private int experiments = 20;
	public ArrayList<BasePlayerInterface> opponents = new ArrayList<BasePlayerInterface>(){{
		add(new RandomPlayer());
	}};

	private int behaviours;

	public FitnessFunction(String gameVersion, PlayerNumericalStatistic fitness,
					PlayerNumericalStatistic[] metrics, PlayerNumericalStatistic[] support){
		behaviours = metrics.length;
		bf = new BehavioursFunction(behaviours);
		fitness.setPlayer(playerName);

		for(PlayerNumericalStatistic pns:metrics)
			pns.setPlayer(playerName);

		for(PlayerNumericalStatistic pns:support)
			pns.setPlayer(playerName);

		String[] metricsNames = behaviourStrings(behaviours);
		String[] supportNames = supportStrings(support.length);

		String[] allNames = new String[metricsNames.length+supportNames.length];
		System.arraycopy(metricsNames,0,allNames, 0, metricsNames.length);
		System.arraycopy(supportNames,0,allNames, metricsNames.length, supportNames.length);

		PlayerNumericalStatistic[] allMetrics = new PlayerNumericalStatistic[metricsNames.length+supportNames.length];
		System.arraycopy(metrics, 0, allMetrics, 0, metrics.length);
		System.arraycopy(support, 0, allMetrics, metrics.length, support.length);

		PlayerStatsWrapper wrapper = new PlayerStatsWrapper(playerName,
				fitnessStatName,fitness,allNames, allMetrics);

		env = new RinascimentoLoggedEnv(gameVersion);
		env.setStats(wrapper);
	}

	public RinascimentoLoggedEnv getEnvironment(){
		return env;
	}

	public void setExperiments(int experiments) {
		this.experiments = experiments;
	}

	@Override
	public double evaluate(@NotNull Solution solution) {

		if(!(solution instanceof PlayerSolution)){
			throw new RuntimeException("Solution object not adequate");
		}

		PlayerSolution player = (PlayerSolution) solution;
		ArrayList players = new ArrayList<BasePlayerInterface>();

		players.add(player.getPlayer());
		players.addAll(opponents);
		BasePlayerInterface[] playersArray = (BasePlayerInterface[]) players.toArray(new BasePlayerInterface[players.size()]);
		env.setPlayers(playersArray);

		PlayerStatsWrapper results = (PlayerStatsWrapper) env.runMultiple(experiments);
		double[] behavioursResults = new double[behaviours];
		for(int i=0; i<behaviours; i++)
			behavioursResults[i] = results.value(behaviourString(i));

		player.setStatsWithHistory(results);
		this.bf.updateLastBehavioursTested(solution,behavioursResults);
		return results.value(fitnessStatName);
	}

	public BehavioursFunction getBehaviourFunction(){
		return bf;
	}

	private static String[] supportStrings(int size){
		String[] bs = new String[size];
		for(int i=0; i<size; i++)
			bs[i] = supportString(i);
		return bs;
	}

	private static String[] behaviourStrings(int size){
		String[] bs = new String[size];
		for(int i=0; i<size; i++)
			bs[i] = behaviourString(i);
		return bs;
	}

	private static String supportString(int index){
		return supportPrefix+"["+index+"]";
	}

	private static String behaviourString(int index){
		return behaviourPrefix+"["+index+"]";
	}
}
