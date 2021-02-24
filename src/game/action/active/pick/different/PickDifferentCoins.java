package game.action.active.pick.different;

import game.action.active.pick.PickCoins;
import game.state.PlayerState;
import game.state.State;
import utils.ArrayUtils;

import java.util.Arrays;

public class PickDifferentCoins extends PickCoins {

	public PickDifferentCoins(int playerId, int[] pattern) {
		super(playerId, pattern);
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof PickDifferentCoins))
			return false;
		return super.equals(obj);
	}

	@Override
	public boolean canPerform(State gs){
		if(!super.canPerform(gs))
			return false;

		PlayerState ps = gs.getPlayerState(playerId);
		int currTotal = ps.getCoinsCount();
		int takeAmount = takeAmount();
		int differentCountLimit = Math.min(
				gs.params.pickDifferentCount,
				ArrayUtils.filterIndicesGE(gs.coinStacks,1).size());
		boolean hasToGive = currTotal+takeAmount>gs.params.maxCoins;

		int delta = Arrays.stream(pattern).sum();
		int takeCount = countTake();
		int giveCount = countGive();


		if(hasToGive){

			// can't take more than the limit which can be
			// - the default or
			// - the minimum amount of coin types available
			if(takeCount>differentCountLimit) {
				return false;
			}

			// If I overshoot I have to give back coins just to reach max
			if(currTotal+delta!=gs.params.maxCoins) {
				return false;
			}

		}else{
			// has to take exactly limit which can be
			// - the default or
			// - the minimum amount of coin types available
			if(takeCount>differentCountLimit)
				return false;

			// don't give if no need
			if(giveCount>0)
				return false;
		}

		for (int i=0; i<pattern.length; i++)
			//can't take more than the defined amount
			if(pattern[i]>gs.params.pickDifferentAmount)
				return false;

		return true;
	}

	@Override
	protected String actionName(){
		return "Pick Different Coins";
	}

	public static void main(String[] args){
		PickDifferentCoins a1 = new PickDifferentCoins(0,new int[]{0,1,1,0,0});
		PickDifferentCoins a2 = new PickDifferentCoins(0,new int[]{0,1,1,0,0});
		System.out.println(a1.equals(a2));
	}

}
