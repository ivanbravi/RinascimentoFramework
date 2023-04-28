package players.ai.meta;

import game.BudgetExtendedGameState;
import game.action.Action;
import hyper.agents.factory.AgentFactorySpace;
import hyper.agents.factory.HeuristicAgentFactorySpace;
import players.BasePlayerInterface;
import players.HeuristicBasedPlayerInterface;
import players.ai.explicit.ExplicitPlayerInterface;

import java.util.Arrays;

public class MetaTuningAgent extends HeuristicBasedPlayerInterface implements BasePlayerInterface, ExplicitPlayerInterface{

	String name;
	int myID;

	/*---- PARAMETERS ----*/
	public int tuningTurns = 10;
	public int rollsDepth = 2;
	public double eps = 0.7;
	public double exp = 20;
	public double ratio = 0.02;
	/*--------------------*/

	AgentFactorySpace afs;
	LiveNTBEA tuner = null;
	ExplicitPlayerInterface betterMe;
	int turnsCount;

	int[] bestConfig;

	public MetaTuningAgent(AgentFactorySpace afs){
		this.afs = afs;
		this.turnsCount = 0;
		if(afs instanceof HeuristicAgentFactorySpace){
			((HeuristicAgentFactorySpace) afs).setHeuristic(this.h);
		}
	}

	private void setup(){
		this.tuningTurns = tuningTurns>0?tuningTurns:1;
		tuner.epsilon = eps;
		tuner.kExplore = exp;
		tuner.env.setDepth(rollsDepth);
		tuner.env.setBudgetRatio(ratio);
	}

	@Override
	public Action[] getActions(BudgetExtendedGameState gameState, int playerId) {
		Action[] actions;

		setup();
		turnsCount++;

		if(turnsCount<=tuningTurns){
			tuner.tune(gameState);
			bestConfig = tuner.getBestSetting();
			actions = new Action[]{gameState.getRandomAction(playerId)};
			betterMe = (ExplicitPlayerInterface) afs.agent(bestConfig);
			if(turnsCount==tuningTurns){
				System.out.println(Arrays.toString(bestConfig));
			}
		}else{
			actions = betterMe.getActions(gameState,playerId);
		}
		return actions;
	}

	@Override
	public BasePlayerInterface reset() {
		tuner = null;
		this.turnsCount = 0;
		return this;
	}

	@Override
	public void setId(int id) {
		if(this.myID!=id || tuner==null){
			this.tuner = new LiveNTBEA(afs,id);
		}
		this.myID = id;
	}

	@Override
	public HeuristicBasedPlayerInterface clone() {

		MetaTuningAgent clone = new MetaTuningAgent(this.afs);

		clone.name = this.name;
		clone.tuningTurns = this.tuningTurns;
		clone.rollsDepth = this.rollsDepth;
		clone.eps = this.eps;
		clone.exp = this.exp;
		clone.ratio = this.ratio;
		clone.turnsCount = this.turnsCount;
		clone.betterMe = (ExplicitPlayerInterface) this.betterMe.clone();
		clone.bestConfig = Arrays.copyOf(this.bestConfig, this.bestConfig.length);

		// TODO: uncomment when NTBEA becomes clonable
		//clone.tuner = this.tuner.clone();

		return clone;
	}

	@Override
	public double utility() {
		return 0;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}
}
