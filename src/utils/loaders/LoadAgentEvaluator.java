package utils.loaders;

import benchmarks.PlayRinascimento;
import benchmarks.RinascimentoEnv;
import benchmarks.RinascimentoLoggedEnv;
import game.Parameters;
import game.adapters.WonAdapter;
import game.budget.ActionsBudget;
import game.log.RinascimentoEventDispatcher;
import game.log.converters.EventIdConverter;
import game.log.converters.IdentityConverter;
import game.log.converters.SimpleConverter;
import hyper.agents.factory.AgentFactorySpace;
import hyper.environment.RinascimentoAgentEvaluator;
import players.BasePlayerInterface;
import players.ai.explicit.OneStepLookAheadAgent;
import statistics.player.WinGameStats;
import utils.AgentsConfig;

import java.util.Arrays;

public class LoadAgentEvaluator {

	/*		PARAMS		*/
	String gameVersion;
	String agentType;
	String agentParametersFile;
	int agentSimulationBudget;
	String opponentsFile;

	private AgentFactorySpace agentFactory;

	public LoadAgentEvaluator(String gameVersion, String agentType, String agentParametersFile,
							  int agentSimulationBudget, String opponentsFile){
		this.gameVersion = gameVersion;
		this.agentType = agentType;
		this.agentParametersFile = agentParametersFile;
		this.agentSimulationBudget = agentSimulationBudget;
		this.opponentsFile = opponentsFile;
	}

	public RinascimentoAgentEvaluator load(){
		RinascimentoAgentEvaluator.VERBOSE = false;
		RinascimentoEnv.VERBOSE = true;

		agentFactory = LoadSearchSpace.loadFactorySpace(agentType,agentParametersFile, Parameters.load(gameVersion));

		RinascimentoEventDispatcher logger = new RinascimentoEventDispatcher();

		RinascimentoLoggedEnv env = new RinascimentoLoggedEnv(gameVersion);
		env.setPlayersBudget(new ActionsBudget(agentSimulationBudget));
		env.setDispatcher(logger);

		BasePlayerInterface[] opponents = PlayRinascimento.decodePlayers(AgentsConfig.readJson(opponentsFile), Parameters.load(gameVersion));

		return new RinascimentoAgentEvaluator().
				setAgentFactory(agentFactory).
				setEnvironment(env).
				setOpponents(opponents).
				setAgentQuality(new WinGameStats(new WonAdapter()));
	}

	public AgentFactorySpace getAgentFactory() {
		return agentFactory;
	}
}
