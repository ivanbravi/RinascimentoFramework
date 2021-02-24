package game.action.active.pick.same;

import game.action.PlayableAction;
import game.action.random.RandomActionGenerator;
import game.state.State;
import utils.ArrayUtils;

import java.util.ArrayList;
import java.util.Arrays;

public class RandomPickSameCoins extends RandomActionGenerator {

	@Override
	public String toString() {
		return "Pick Same Coins";
	}

	@Override
	public PlayableAction generate(State s, int playerId) {
		int currCoins = s.getPlayerState(playerId).getCoinsCount();
		int taking = 0;

		int[] pattern = new int[s.params.suitCount];
		Arrays.fill(pattern,0);

		int typeCount = Math.min(1,ArrayUtils.filterIndicesGE(
				s.coinStacks,
				Math.max(s.params.pickSameAmount,s.params.pickSameMinCoins)).size());

		if(typeCount==0){
			return null;
		}

		int[] indices = potentialPattern(s.coinStacks,s.params.pickSameAmount, s.params.pickSameMinCoins);
		for(int i=0; i<indices.length; i++){
			int index = indices[i];
			int thisAmount = Math.min(s.coinStacks[index],s.params.pickSameAmount);
			pattern[index] = thisAmount;
			taking+=thisAmount;
		}

		int give = taking+currCoins;

		int[] future = ArrayUtils.add(s.getPlayerState(playerId).coins,pattern);

		if(give>s.params.maxCoins){
			while(give>s.params.maxCoins){
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
				give -= 1;
			}
		}

		return new PickSameCoins(playerId,pattern);
	}

	@Override
	public RandomActionGenerator clone() {
		return new RandomPickSameCoins();
	}


	private int[] potentialPattern(int[] v, int atLeast, int minCoins){
		ArrayList<Integer> indices = ArrayUtils.filterIndicesGE(v,Math.max(atLeast,minCoins));
		int amount = 1;

		while(indices.size()>amount){
			indices.remove(rnd.nextInt(indices.size()));
		}

		if(indices.size()<amount){
			ArrayList<Integer> extra = ArrayUtils.filterIndicesL(v,amount);
			while(indices.size()+extra.size()>amount){
				extra.remove(rnd.nextInt(extra.size()));
			}
			indices.addAll(extra);
		}

		int[] result = new int[indices.size()];
		for(int i=0; i<result.length; i++)
			result[i]=indices.get(i);

		return result;
	}
}
