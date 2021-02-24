package players.ai.explicit;

import game.BudgetExtendedGameState;
import game.action.Action;
import game.action.PlayableAction;
import game.heuristics.PointsHeuristic;
import players.BudgetOverException;
import players.HeuristicBasedPlayerInterface;

import java.util.Arrays;
import java.util.Random;

public class HyperRHEA extends HeuristicBasedPlayerInterface implements ExplicitPlayerInterface {

	private static boolean VERBOSE = false;

	private Action[] solution = null;
	private double solutionFitness;

	/* AGENT PARAMETERS */
	public boolean useShiftBuffer = true;
	public int sequenceLength = 120;
	public int evals = 1000;
	public boolean flipAtLeastOneValue = true;
	public int mutationStyle = 0; //0->randomPoint 1->branching+uniform 2->branching+decay 3->branching+gaus default->no mutation
	public int opponentType=0;	  //0->DoNothing   1->Random
	public double opponentBudget = 0;
	/* - - - -NEW- - - - */
	public double espMutationProb = 0.5;
	public double gMean = 0.5;
	public double gStdDev = 0.5;
	/* - - - - - - - - - */

	private ExplicitPlayerInterface[] opponents;
	private Random rnd;

	public HyperRHEA(){
		rnd = new Random();
		name = "BM-RHEA";
	}

	public static HyperRHEA create(boolean usb, int sl, int e, boolean falov, int mutationStyle, int opponentType, double opponentBudget){
		HyperRHEA a = new HyperRHEA();
		a.useShiftBuffer = usb;
		a.sequenceLength = sl;
		a.evals = e;
		a.flipAtLeastOneValue = falov;
		a.mutationStyle = mutationStyle;
		a.opponentType = opponentType;
		a.opponentBudget = opponentBudget;
		return a;
	}

	private void log(String str){
		if(VERBOSE)
			System.out.println(str);
	}

	private void prepareOpponents(int nOpponents){
		opponents = new ExplicitPlayerInterface[nOpponents];
		for(int i=0; i<opponents.length; i++){
			if(i!=this.id) {
				if(opponentType==0){
					opponents[i] = new DoNothingAgent();
				}else if (opponentType==1){
					opponents[i] = new SafeRandomPlayer();
				}else if (opponentType==2){
					opponents[i] = new OneStepLookAheadAgent();
					((OneStepLookAheadAgent)opponents[i]).setHeuristic(new PointsHeuristic());
				}
				opponents[i].setId(-1);
			}
		}
	}

	@Override
	public Action[] getActions(BudgetExtendedGameState gameState, int playerId) {
		Action myAction;
		h.ground(gameState,playerId);
		this.id = playerId;
		try {
			int evalCount = evals;

			// check if opponents are initialised
			if (opponents == null) {
				prepareOpponents(gameState.getPlayersCount());
			}

			BudgetExtendedGameState solutionState = gameState.copy();
			//create solution or shift the previous
			if (solution == null) {
				solution = randomPlan(solutionState);
			} else if (useShiftBuffer) {
				shiftPlan(solution, solutionState);
			}

			// current solution evaluation
			h.ground(solutionState,playerId);
			playPlanWithOpponents(solution, solutionState);
			solutionFitness = utility(solutionState);

			// while it has evaluations left keep hill-climbing
			while (evalCount > 0) {
				BudgetExtendedGameState mutationState = gameState.copy();
				h.ground(mutationState,playerId);
				Action[] mutation = mutate(solution, mutationState);
				double mutationFitness = utility(mutationState);

				if (mutationFitness >= solutionFitness) {
					log(mutationFitness+">="+solutionFitness+" NEW SOLUTION");
					solutionFitness = mutationFitness;
					solution = mutation;
				}else{
					log(mutationFitness+"<"+solutionFitness);
				}

				evalCount--;
			}
		}catch (BudgetOverException e){

		}

		myAction = pickReturnAction(gameState);

		return new Action[]{myAction};
	}

	private int esponentialDecayMutationIndex(){
		int mutationPoint = 0;
		double decay = espMutationProb;
		for(; mutationPoint<sequenceLength; mutationPoint++){
			if(rnd.nextDouble() <= decay)
				break;
			decay *= decay;
		}
		return mutationPoint;
	}

	private int gaussianMutationPoint(){
		int point = (int) Math.round(rnd.nextGaussian()*gStdDev+gMean*sequenceLength);
		point = Math.max(point,0);
		point = Math.min(sequenceLength-1,point);
		return point;
	}

	private int uniformMutationPoint(){
		return rnd.nextInt(sequenceLength);
	}

	private Action[] mutate(Action[] currPlan, BudgetExtendedGameState state){
		if(mutationStyle==0){ // RANDOM POINT
			return randomPlan(state);
		}
		if(mutationStyle==1 || mutationStyle==2 || mutationStyle==3) { // BRANCHING MUTATION
			Action[] newPlan = Arrays.copyOf(currPlan, currPlan.length);
			int mutationPoint;
			switch (mutationStyle) {
				case 2: {
					mutationPoint = esponentialDecayMutationIndex();
				}
				break;
				case 3: {
					mutationPoint = gaussianMutationPoint();
				}
				break;
				default:{
					mutationPoint = uniformMutationPoint();
				}
			}

			int pId = this.id;
			for (int i = 0; i < currPlan.length; i++) {
				for (; pId < state.getPlayersCount(); pId++) {
					Action action;
					if (pId==this.id) {
						if(i >= mutationPoint) {
							newPlan[i] = state.getRandomAction(pId);
						}
						action = newPlan[i];
					} else {
						if (opponents[pId] != null) {
							action = opponents[pId].getActions(state, pId)[0];
						} else {
							action = null;
						}
					}
					state.perform(action);
				}
				pId = 0;
			}
			return newPlan;
		}
		// DEFAULT: no mutation
		return currPlan;
	}

	private void playPlanWithOpponents(Action[] plan, BudgetExtendedGameState state){
		int i=1;
		int pId = id;
		for(; i<plan.length; i++) {
			for (; pId < opponents.length; pId++) {
				Action action;
				if (pId == id) {
					action = plan[i];
				} else {
					if(opponents[pId]!=null){
						action = opponents[pId].getActions(state.copyForPlayerAndSplit(pId,opponentBudget), pId)[0];
					}else{
						action = null;
					}
				}
				state.perform(action);
				if(i<sequenceLength-1 && pId==id)
					break;
			}
			pId = 0;
		}
	}

	private Action[] shiftPlan(Action[] plan, BudgetExtendedGameState state){
		Action[] shiftedPlan = Arrays.copyOfRange(plan,0,plan.length);

		for(int i=1; i<shiftedPlan.length; i++){
			shiftedPlan[i - 1] = shiftedPlan[i];
		}

		playPlanWithOpponents(Arrays.copyOf(shiftedPlan,shiftedPlan.length-1),state);
		shiftedPlan[shiftedPlan.length-1] = state.getRandomAction(id);
		return shiftedPlan;
	}

	private Action pickReturnAction(BudgetExtendedGameState state){
		BudgetExtendedGameState s = state.copy();
		if(solution!=null) {
			for (int i = 0; i < solution.length; i++) {
				if(solution[i]!=null){
					if (s.canPerform(solution[i])) {
						return solution[i];
					}
				}
			}
		}
		return state.getRandomAction(id);
	}

	public Action[] randomPlan(BudgetExtendedGameState state) {
		Action[] plan = new PlayableAction[sequenceLength];

		int pId = id;
		for(int i=0; i<sequenceLength && !state.isGameOver(); i++){
			// play turns for all the players
			for(;pId<opponents.length && !state.isGameOver(); pId++){
				Action playerAction;
				if(pId==id){
					playerAction = state.getRandomAction(this.id);
					plan[i] = playerAction;
				}else{
					playerAction = opponents[pId].getActions(state.copyForPlayerAndSplit(pId,opponentBudget),pId)[0];
				}
				state.perform(playerAction);
				if(i==sequenceLength-1 && pId==id)
					break;
			}
			pId = 0;
		}
		return plan;
	}

	@Override
	public HeuristicBasedPlayerInterface reset() {
		solution = null;
		solutionFitness = 0;
		opponents = null;
		return this;
	}

	@Override
	public double utility() {
		return 0;
	}


	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();

		builder.append(super.toString()+"\n");

		builder.append("useShiftBuffer:").append(useShiftBuffer);
		builder.append(" sequenceLength:").append(sequenceLength);
		builder.append(" evals:").append(evals);
		builder.append(" flipAtLeastOneValue:").append(flipAtLeastOneValue);
		builder.append(" mutationStyle:").append(mutationStyle);
		builder.append(" opponentType:").append(opponentType);
		builder.append(" opponentBudget:").append(opponentBudget);
		builder.append(" espMutationProb:").append(espMutationProb);
		builder.append(" gMean:").append(gMean);
		builder.append(" gStdDev:").append(gStdDev);

		return builder.toString();
	}

	@Override
	public HeuristicBasedPlayerInterface clone() {
		HyperRHEA clone = new HyperRHEA();
		clone.setHeuristic(this.h.clone());
		clone.setName(this.name);
		clone.setId(this.id);

		clone.useShiftBuffer = this.useShiftBuffer;
		clone.sequenceLength = this.sequenceLength;
		clone.evals = this.evals;
		clone.flipAtLeastOneValue = this.flipAtLeastOneValue;
		clone.mutationStyle = this.mutationStyle;
		clone.opponentType = this.opponentType;
		clone.opponentBudget = this.opponentBudget;
		clone.espMutationProb = this.espMutationProb;
		clone.gMean = this.gMean;
		clone.gStdDev = this.gStdDev;

		return clone;
	}
}
