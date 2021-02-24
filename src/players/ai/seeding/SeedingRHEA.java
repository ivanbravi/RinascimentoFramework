package players.ai.seeding;

import game.BudgetExtendedGameState;
import game.action.Action;
import players.BasePlayerInterface;
import players.BudgetOverException;
import players.HeuristicBasedPlayerInterface;
import players.ai.explicit.DoNothingAgent;
import players.ai.explicit.ExplicitPlayerInterface;
import players.ai.explicit.OneStepLookAheadAgent;
import players.ai.explicit.RandomPlayer;

import java.util.Random;

public class SeedingRHEA extends HeuristicBasedPlayerInterface implements ExplicitPlayerInterface {
	Random random = new Random();

	// these are all the parameters that control the agent
	public boolean flipAtLeastOneValue = true;
	public double mutationRate = 0.2;
	public int sequenceLength = 200;
	public int nEvals = 20;
	public boolean useShiftBuffer = true;
	public int opponentModelType = 0;
	public double opponentBudgetShare = 0.0;

	long[] solution;
	ExplicitPlayerInterface []opponents;

	public BasePlayerInterface reset() {
		solution = null;
		opponents = null;
		return this;
	}

	private void setupOpponents(int opponentCount){
		opponents = new ExplicitPlayerInterface[opponentCount];
		for(int pId=0; pId<opponents.length; pId++){
			if(pId!=this.id) {
				if (opponentModelType == 0) {
					opponents[pId] = new DoNothingAgent();
				} else if (opponentModelType == 1) {
					opponents[pId] = new RandomPlayer();
				}else if(opponentModelType == 2){
					opponents[pId] = new OneStepLookAheadAgent();
				}
			}
		}
	}

	public Action[] getActions(BudgetExtendedGameState gameState, int playerId) {
		if(opponents==null)
			setupOpponents(gameState.getPlayersCount());
		try {
			if (useShiftBuffer && solution != null) {
				solution = shiftLeftAndRandomAppend(solution);
			} else {
				solution = randomPoint();
			}

			for (int i = 0; i < nEvals; i++) {
				// evaluate the current one
				long[] mut = mutate(solution);
				double curScore = evalSeq(gameState.copy(), solution, playerId);
				double mutScore = evalSeq(gameState.copy(), mut, playerId);
				if (mutScore >= curScore) {
					solution = mut;
				}
			}
		}catch (BudgetOverException e){}

		Action action = gameState.getRandomAction(playerId,solution[0]);
		// nullify if not using a shift buffer
		if (!useShiftBuffer) solution = null;
		return new Action[]{action};
	}

	private long[] mutate(long[] v) {

		int n = v.length;
		long[] x = new long[n];
		// point-wise probability of additional mutations
		// choose element of vector to mutate
		int ix = random.nextInt(n);
		if (!flipAtLeastOneValue) {
			// setting this to -1 means it will never match the first clause in the if statement in the loop
			// leaving it at the randomly chosen value ensures that at least one bit (or more generally value) is always flipped
			ix = -1;
		}
		// copy all the values faithfully apart from the chosen one
		for (int i = 0; i < n; i++) {
			if (i == ix || random.nextDouble() < mutationRate) {
				x[i] = mutateValue(v[i]);
			} else {
				x[i] = v[i];
			}
		}
		return x;
	}

	private long mutateValue(long cur) {
		// possible use curr just with a "neighbor" mutation
		return random.nextLong();
	}

	private long[] randomPoint() {
		long[] p = new long[sequenceLength];
		for (int i=0; i<p.length; i++) {
			p[i] = random.nextLong();
		}
		return p;
	}

	private long[] shiftLeftAndRandomAppend(long[] v) {
		long[] p = new long[v.length];
		for (int i = 0; i < p.length - 1; i++) {
			p[i] = v[i + 1];
		}
		p[p.length - 1] = random.nextLong();
		return p;
	}


	Double discountFactor = null;


	private double evalSeq(BudgetExtendedGameState gameState, long[] seq, int playerId) {
		if (discountFactor == null) {
			return evalSeqNoDiscount(gameState, seq, playerId);
		} else {
			return evalSeqDiscounted(gameState, seq, playerId, discountFactor);
		}
	}

	private double evalSeqNoDiscount(BudgetExtendedGameState gameState, long[] seq, int playerId) {
		double current = utility(gameState);
		int pId = playerId;
		for (int i=0; i<seq.length; i++) {

			for(; pId<gameState.getPlayersCount(); pId++) {
				Action action;
				if(pId==playerId){
					long actionSeed = seq[i];
					action = gameState.getRandomAction(playerId,actionSeed);
				}else{
					action = opponents[pId].getActions(gameState.copyForPlayerAndSplit(pId,opponentBudgetShare), pId)[0];
				}
				gameState = gameState.perform(action);
			}
			pId=0;
		}
		double delta = utility(gameState) - current;
		return delta;
	}

	private double evalSeqDiscounted(BudgetExtendedGameState gameState, long[] seq, int playerId, double discountFactor) {
		double currentScore = utility(gameState);
		double delta = 0;
		double discount = 1;
		int pId = playerId;

		for (int i=0; i<seq.length; i++) {
			for(; pId<gameState.getPlayersCount(); pId++) {
				Action action;
				if(pId==playerId){
					long actionSeed = seq[i];
					action = gameState.getRandomAction(playerId,actionSeed);
				}else{
					action = opponents[pId].getActions(gameState.copyForPlayerAndSplit(pId,opponentBudgetShare), pId)[0];
				}
				gameState = gameState.perform(action);
			}
			double nextScore = utility(gameState);
			double tickDelta = nextScore - currentScore;
			currentScore = nextScore;
			delta += tickDelta * discount;
			discount *= discountFactor;

			pId=0;
		}

		return delta;
	}

	public double utility(){
		return 0;
	}

	public String toString() {
		return "SEA: " + nEvals + " : " + sequenceLength + " : " + opponents;
	}

	@Override
	public HeuristicBasedPlayerInterface clone() {
		SeedingRHEA clone = new SeedingRHEA();

		clone.flipAtLeastOneValue = this.flipAtLeastOneValue;
		clone.mutationRate = this.mutationRate;
		clone.sequenceLength = this.sequenceLength;
		clone.nEvals = this.nEvals;
		clone.useShiftBuffer = this.useShiftBuffer;
		clone.opponentModelType = this.opponentModelType;
		clone.opponentBudgetShare = this.opponentBudgetShare;

		clone.setHeuristic(this.h.clone());
		clone.setName(this.name);
		clone.setId(this.id);

		return clone;
	}
}
