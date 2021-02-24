package players.ai.meta;

import game.BudgetExtendedGameState;
import game.heuristics.Heuristic;
import game.heuristics.PointsHeuristic;
import hyper.agents.factory.AgentFactorySpace;
import ntbea.NTupleBanditEA;
import ntbea.NTupleSystem;
import players.BudgetOverException;

public class LiveNTBEA {

	GameStateEnvironment env;
	NTupleBanditEA banditEA;
	Heuristic h = new PointsHeuristic();
	double kExplore;
	double epsilon;

	LiveNTBEA(AgentFactorySpace afs, int playerID){
		env = new GameStateEnvironment(afs,h,playerID);
		banditEA = new NTupleBanditEA().setKExplore(kExplore).setEpsilon(epsilon);
		NTupleSystem model = new NTupleSystem();

		model.use1Tuple = true;
		model.use2Tuple = true;
		model.use3Tuple = false;
		model.useNTuple = true;
		banditEA.setModel(model);
	}

	public void tune(BudgetExtendedGameState state){
		try {
			env.setGameState(state);
			banditEA.runTrial(env, Integer.MAX_VALUE);
		}catch (BudgetOverException e){}
		env.reset();
	}

	public int[] getBestSetting(){
		return banditEA.getModel().getBestSolution();
	}


}
