package hyper.environment;

import benchmarks.RinascimentoEnv;
import hyper.agents.factory.AgentFactorySpace;
import hyper.utilities.FullNoisySolutionEvaluator;
import statistics.GameStats;
import statistics.player.PlayerNumericalStatistic;
import evodef.AnnotatedSearchSpace;
import evodef.EvolutionLogger;
import players.BasePlayerInterface;
import statistics.types.StatisticInterface;

import java.util.Arrays;

public class RinascimentoAgentEvaluator implements FullNoisySolutionEvaluator {

	public static boolean VERBOSE = false;

	public int trueFitnessSamples = 1000;
	private int evalCount=0;

	public double lastTrueFitness;
	public double lastTrueFitnessError;

	// MUST SET
	protected RinascimentoEnv env;
	protected AgentFactorySpace factory;
	protected BasePlayerInterface[] opponents;
	protected PlayerNumericalStatistic agentQuality;

	private EvolutionLogger logger = new EvolutionLogger();

	public RinascimentoAgentEvaluator setEnvironment(RinascimentoEnv env){
		this.env = env;
		return this;
	}

	public RinascimentoAgentEvaluator setOpponents(BasePlayerInterface[] opponents){
		this.opponents = opponents;
		return this;
	}

	public RinascimentoAgentEvaluator setAgentFactory(AgentFactorySpace factory){
		this.factory = factory;
		return this;
	}

	public RinascimentoAgentEvaluator setAgentQuality(PlayerNumericalStatistic agentQuality){
		this.agentQuality = agentQuality;
		return this;
	}

	@Override
	public Double trueFitness(int[] solution) {
		return trueFitnessComplete(solution).value();
	}

	private BasePlayerInterface[] createPlayersList(BasePlayerInterface agent){
		BasePlayerInterface[] players;

		players = Arrays.copyOfRange(opponents,0,env.players());
		players[env.players()-1]=agent;
		return players;
	}

	@Override
	public double evaluate(int[] solution) {
		evalCount++;
		if(VERBOSE)System.out.print("Evaluation #"+evalCount+" ");
		BasePlayerInterface agent = factory.agent(solution);
		env.setPlayers(createPlayersList(agent));

		agentQuality.setPlayer(factory.getAgentType());
		env.setStats(agentQuality);

		GameStats stats = env.runOnce();
		if(VERBOSE)System.out.println(" s:"+Arrays.toString(solution)+"\n v:"+stats.value());
		return stats.value();
	}
	@Override
	public void reset() {
		evalCount = 0;
	}

	@Override
	public AnnotatedSearchSpace searchSpace() {
		return factory.getSearchSpace();
	}

	@Override
	public int nEvals() {
		return evalCount;
	}

	@Override
	public EvolutionLogger logger() {
		return logger;
	}

	@Override
	public boolean optimalFound() {
		return false;
	}

	@Override
	public Double optimalIfKnown() {
		return null;
	}

	@Override
	public Boolean isOptimal(int[] ints) {
		return null;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();

		builder.append(factory.toString()+"\n");
		builder.append(env.toString()+"\n");
		builder.append("\n[Opponent(s)]\n");
		for(int i=0; i<opponents.length; i++){
			BasePlayerInterface a = opponents[i];
			builder.append("["+i+"]"+a.toString()+"\n");
		}

		builder.append("[True Fitness Samples ]\n");
		builder.append(trueFitnessSamples+"\n");

		return builder.toString();
	}

	@Override
	public StatisticInterface trueFitnessComplete(int[] solution) {
		BasePlayerInterface agent = factory.agent(solution);
		agentQuality.setPlayer(factory.getAgentType());
		env.setPlayers(createPlayersList(agent));
		env.setStats(agentQuality);
		GameStats s = env.runMultiple(trueFitnessSamples);

		lastTrueFitness = s.value();
		lastTrueFitnessError = s.error();

		return s;
	}
}
