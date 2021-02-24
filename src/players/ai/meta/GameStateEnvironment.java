package players.ai.meta;

import evodef.EvolutionLogger;
import evodef.NoisySolutionEvaluator;
import evodef.SearchSpace;
import game.BudgetExtendedGameState;
import game.action.Action;
import game.heuristics.Heuristic;
import hyper.agents.factory.AgentFactorySpace;
import players.ai.explicit.ExplicitPlayerInterface;

public class GameStateEnvironment implements NoisySolutionEvaluator {

	private EvolutionLogger logger = new EvolutionLogger();
	private AgentFactorySpace afs;
	private BudgetExtendedGameState gs;
	private Heuristic eval;
	private int playerID;
	private int depth;
	private double ratio;

	private int evalsCount =0;

	GameStateEnvironment(AgentFactorySpace afs, Heuristic eval, int playerID){
		this.afs = afs;
		this.eval = eval;
		this.playerID = playerID;
	}

	public void setGameState(BudgetExtendedGameState gs) {
		this.gs = gs;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}
	public void setBudgetRatio(double ratio){this.ratio=ratio;}

	@Override
	public Double trueFitness(int[] ints) {
		return null;
	}

	@Override
	public double evaluate(int[] ints) {
		ExplicitPlayerInterface p = (ExplicitPlayerInterface) afs.agent(ints);
		BudgetExtendedGameState thisState = gs.copy();
		int counter = 0;
		double delta = eval.value(thisState,playerID);

		while(!thisState.isGameOver() && counter++<depth){
			Action a = p.getActions(thisState.copyAndSplit(ratio),playerID)[0];
			thisState.perform(a);
		}
		delta = eval.value(thisState,playerID)-delta;
		evalsCount++;
		return delta;
	}

	@Override
	public SearchSpace searchSpace() {
		return afs.getSearchSpace();
	}

	@Override
	public EvolutionLogger logger() {
		return logger;
	}

	@Override
	public Boolean isOptimal(int[] ints) {
		return null;
	}

	@Override
	public void reset() {
		evalsCount = 0;
	}

	@Override
	public boolean optimalFound() {
		return false;
	}

	@Override
	public int nEvals() {
		return evalsCount;
	}

	@Override
	public Double optimalIfKnown() {
		return null;
	}
}
