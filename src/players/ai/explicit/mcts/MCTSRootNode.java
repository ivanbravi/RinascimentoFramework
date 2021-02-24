package players.ai.explicit.mcts;

import game.BudgetExtendedGameState;
import game.action.Action;
import game.heuristics.Heuristic;
import players.BasePlayerInterface;
import players.BudgetOverException;
import players.ai.explicit.ExplicitPlayerInterface;
import players.ai.factory.DoNothingFactory;
import players.ai.factory.OSLAFactory;
import players.ai.factory.SafeRandomFactory;
import players.ai.factory.SimpleAgentFactory;

import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

public class MCTSRootNode extends MCTSNode {

	public static boolean VERBOSE = true;
	public static int V_DETAIL = 1;

	/*--------- PARAMS ---------*/
	private double c;
	private int rolloutDepth;
	private int opponentModel;
	private double opponentBudgetRatio;
	private double expansionProbability;
	private int progressionSize;
	private double epsilon;
	private int recommendationType;
	private boolean rollWithOpponents;
	/*--------------------------*/

	Heuristic h;
	ExplicitPlayerInterface[] opponents;
	SimpleAgentFactory[] factories = new SimpleAgentFactory[]{
			new DoNothingFactory(),
			new SafeRandomFactory(),
			new OSLAFactory()
	};

	Random rnd = new Random();

	Comparator<MCTSNode> comparator;

	public MCTSRootNode(Heuristic h){
		super(null,null);
		this.h = h;
	}

	public MCTSRootNode setExploration(double c){
		this.c = c;
		return this;
	}

	public MCTSRootNode setDepth(int rolloutDepth){
		this.rolloutDepth = rolloutDepth;
		return this;
	}

	public MCTSRootNode setOpponentModel(int opponentModel){
		this.opponentModel = (opponentModel>factories.length-1 || opponentModel<0)?0:opponentModel;
		opponents = null;
		return this;
	}

	public MCTSRootNode setOpponentBudgetRatio(double opponentBudgetRatio){
		this.opponentBudgetRatio = (opponentBudgetRatio>1 || opponentBudgetRatio<0)?0.05:opponentBudgetRatio;
		return this;
	}

	public MCTSRootNode setExpansionProbability(double expansionProbability){
		this.expansionProbability = (expansionProbability>1 || opponentModel<0)?0.5:expansionProbability;
		return this;
	}

	public MCTSRootNode setProgressionSize(int progressionSize){
		this.progressionSize = (progressionSize<0)?1:progressionSize;
		return this;
	}

	public MCTSRootNode setUCBEpsilon(double epsilon){
		this.epsilon = (epsilon<=0)?1e-6:epsilon;
		return this;
	}

	public MCTSRootNode setRecommendationType(int recommendationType){
		this.recommendationType = (recommendationType<0 || recommendationType>2)?0:recommendationType;
		switch (this.recommendationType){
			// MAX CHILD
			case 0:{comparator = (MCTSNode o1, MCTSNode o2)->(int) Math.signum(o1.value-o2.value+noise());}break;
			// ROBUST CHILD
			case 1:{comparator = (MCTSNode o1, MCTSNode o2)->(int) Math.signum(o1.n-o2.n+noise());}break;
			// SECURE CHILD
			case 2:{comparator = (MCTSNode o1, MCTSNode o2)->(int) Math.signum(ucb(o1)-ucb(o2));}break;
		}
		return this;
	}

	public MCTSRootNode setRollWithOpponents(boolean rollWithOpponents){
		this.rollWithOpponents = rollWithOpponents;
		return this;
	}

	public void search(BudgetExtendedGameState originalState, int playerID){
		setupOpponents(originalState.getPlayersCount(),playerID);
		try{
			while(true){
				BudgetExtendedGameState state = originalState.copy();
				MCTSNode sNode = treePolicy(state, playerID);
				rollout(state,rolloutDepth-sNode.depth,playerID);
				double delta = h.value(state,playerID)-h.value(originalState,playerID);
				if(VERBOSE && V_DETAIL==2)System.out.println("DELTA:"+delta);
				backUp(sNode,delta);
			}
		}catch (BudgetOverException e){}
		if(VERBOSE && V_DETAIL==1)System.out.println(this.toString());
	}

	private void backUp(MCTSNode node, double delta){
		while(node!=null){
			node.addValue(delta);
			node = node.parent;
		}
	}

	private void rollout(BudgetExtendedGameState state, int actions, int playerID){
		while(actions>0 && !state.isGameOver()){
			Action rndAction =  state.getRandomAction(playerID);
			if(rollWithOpponents){
				rollFullTurn(state,rndAction,playerID);
			}else{
				state.perform(rndAction);
			}
			actions--;
		}
	}

	private MCTSNode treePolicy(BudgetExtendedGameState state, int playerID){
		MCTSNode currNode = this;

		while(!state.isGameOver() && currNode.depth <=rolloutDepth){
			boolean nodeIsEmpty = currNode.children.size()==0;
			boolean stepExpansion = !nodeIsEmpty && rnd.nextDouble()<expansionProbability;
			MCTSNode nextNode;

			if(nodeIsEmpty || stepExpansion){
				if(VERBOSE && V_DETAIL==2)System.out.println("EXPAND");
				nextNode = expand(currNode,state,playerID);

				if(nextNode==null && nodeIsEmpty){
					if(VERBOSE && V_DETAIL==3)System.out.println("Stale game");
					return currNode;
				}

				if(nextNode!=null){
					rollFullTurn(state,nextNode.action,playerID);
					return nextNode;
				}
			}

			if(VERBOSE && V_DETAIL==2)System.out.println("UCB");
			nextNode = uct(currNode);
			rollFullTurn(state,nextNode.action,playerID);

			currNode = nextNode;
		}
		return currNode;
	}

	private MCTSNode expand(MCTSNode toExpand, BudgetExtendedGameState state, int playerID){
		double best = -1;
		MCTSNode nextNode = null;
		for(int i=0; i<progressionSize; i++) {
			Action newAction = state.getRandomAction(playerID);

			if(newAction==null){
				// stale game
				return null;
			}

			if (!toExpand.nodeContainsAction(newAction)) {
				MCTSNode newNode = new MCTSNode(newAction, toExpand);
				toExpand.children.add(newNode);
				double v = rnd.nextDouble();
				if(v>best) {
					nextNode = newNode;
					best = v;
				}
			}
		}
		return nextNode;
	}

	private MCTSNode uct(MCTSNode node){
		MCTSNode child = null;

		double bestUcb = Double.NEGATIVE_INFINITY;

		for(MCTSNode currChild: node.children){
			double currUcb = ucb(currChild)+noise();
			if(currUcb>bestUcb)
				bestUcb = currUcb;
				child = currChild;
		}

		return child;
	}

	private double ucb(MCTSNode node){
		return node.value/(node.n+this.epsilon) +
				c * Math.sqrt(Math.log(
						(node.parent.n) / (node.n+this.epsilon)
				));
	}

	private void setupOpponents(int players, int playerID){
		if(opponents!=null)
			return;
		opponents = new ExplicitPlayerInterface[players];
		for(int i=0; i<players; i++)
			if(playerID!=i) {
				BasePlayerInterface opp = factories[opponentModel].agent();
				if(opp instanceof ExplicitPlayerInterface)
				opponents[i] = (ExplicitPlayerInterface) opp;
			}
	}

	public Action suggestedAction(){
		MCTSNode bestChild = Collections.max(children,comparator);
		return bestChild.action;
	}

	public void rollFullTurn(BudgetExtendedGameState state,Action myAction, int playerID){

		int pID = (playerID+1)%state.getPlayersCount();
		state.perform(myAction);

		while (pID!=playerID){
			Action a = opponents[pID].getActions(state.copyForPlayerAndSplit(pID,opponentBudgetRatio),pID)[0];
			state.perform(a);
			pID=(pID+1)%state.getPlayersCount();
		}


	}

	private double noise(){
		return rnd.nextGaussian()*0.0002;
	}

}
