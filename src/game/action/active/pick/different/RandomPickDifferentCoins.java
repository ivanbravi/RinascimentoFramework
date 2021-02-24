package game.action.active.pick.different;

import game.action.PlayableAction;
import game.action.random.RandomActionGenerator;
import game.state.State;
import utils.ArrayUtils;

import java.util.ArrayList;
import java.util.Arrays;

public class RandomPickDifferentCoins extends RandomActionGenerator {

	@Override
	public String toString() {
		return "Pick Different Coins";
	}

	@Override
	public PlayableAction generate(State s, int playerId) {
		int currCoins = s.getPlayerState(playerId).getCoinsCount();
		int taking = 0;

		int[] pattern = new int[s.params.suitCount];
		Arrays.fill(pattern,0);

		int typeCount = Math.min(s.params.pickDifferentCount,ArrayUtils.filterIndicesGE(s.coinStacks,s.params.pickDifferentAmount).size());

		if(typeCount==0){
			return null;
		}

		int[] indices = potentialPattern(s.coinStacks,s.params.pickDifferentAmount,typeCount);
		for(int i=0; i<indices.length; i++){
			int index = indices[i];
			int thisAmount = Math.min(s.coinStacks[index],s.params.pickDifferentAmount);
			pattern[index] = thisAmount;
			taking+=thisAmount;
		}

		int give = taking+currCoins-s.params.maxCoins;


		if(give>0){
			int[] future = ArrayUtils.add(s.getPlayerState(playerId).coins,pattern);
			while(give>0){
				ArrayList<Integer> takeFrom = ArrayUtils.filterIndicesGE(future,1);

				if(takeFrom.isEmpty()){
					// take from gold
					System.out.println();
				}

				int rndIndex = rnd.nextInt(takeFrom.size());
				int index = takeFrom.get(rndIndex);

				takeFrom.remove(rndIndex);
				future[index] -= 1;
				pattern[index] -= 1;
				give -=1;
			}
		}

		return new PickDifferentCoins(playerId,pattern);
	}

	@Override
	public RandomActionGenerator clone() {
		return new RandomPickDifferentCoins();
	}


	private int[] potentialPattern(int[] v, int atLeast, int amount){
		ArrayList<Integer> indeces = ArrayUtils.filterIndicesGE(v,atLeast);

		while(indeces.size()>amount){
			indeces.remove(rnd.nextInt(indeces.size()));
		}

		if(indeces.size()<amount){
			ArrayList<Integer> extra = ArrayUtils.filterIndicesL(v,amount);
			while(indeces.size()+extra.size()>amount){
				extra.remove(rnd.nextInt(extra.size()));
			}
			indeces.addAll(extra);
		}

		int[] result = new int[indeces.size()];
		for(int i=0; i<result.length; i++)
			result[i]=indeces.get(i);

		return result;
	}

}
