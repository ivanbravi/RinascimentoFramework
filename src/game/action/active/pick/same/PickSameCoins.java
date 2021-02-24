package game.action.active.pick.same;

import game.action.active.pick.PickCoins;
import game.state.PlayerState;
import game.state.State;
import log.entities.action.Action;

import java.util.Arrays;
import java.util.HashMap;

public class PickSameCoins extends PickCoins {

	public PickSameCoins(int playerId, int[] pattern) {
		super(playerId, pattern);
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof PickSameCoins))
			return false;

		return super.equals(obj);
	}

	@Override
	public boolean canPerform(State gs){

		// base conditions for picking
		if(!super.canPerform(gs)){
			return false;
		}

		PlayerState ps = gs.getPlayerState(playerId);
		int currTotal = ps.getCoinsCount();
		int takeAmount = takeAmount();
		boolean hasToGive = currTotal+takeAmount>gs.params.maxCoins;
		int delta = Arrays.stream(pattern).sum();
		int takeCount = countTake();
		int giveCount = countGive();

		// can't take more than 1 kind
		if(takeCount>1){
			return false;
		}

		boolean isOneStackUsable = false;
		for(int i=0; i<gs.coinStacks.length; i++) {
			if (gs.coinStacks[i]>=gs.params.pickSameMinCoins) {
				isOneStackUsable = true;
				break;
			}
		}
		// is there any stack at all with the minimum amount?
		if(!isOneStackUsable){
			return false;
		}

		if(hasToGive){

			// If I overshoot I have to give back coins just to reach max
			if(currTotal+delta!=gs.params.maxCoins)
				return false;

			for(int i=0; i<pattern.length; i++) {
				// can't take more than allowed
				if (pattern[i] > gs.params.pickSameAmount)
					return false;
				if(pattern[i] > 0)
					// can't take if coins are less than limit
					if(gs.coinStacks[i]<gs.params.pickSameMinCoins)
						return false;
			}
		}else{
			// can't give if shouldn't
			if(giveCount>0)
				return false;

			// (A) can pick just the amount specified
			if(delta!=Math.min(gs.params.pickSameAmount,gs.params.maxCoins-currTotal))
				return false;

			for(int i=0; i<pattern.length; i++) {
				if (pattern[i] > 0) {
					// can't take from stack if there aren't enough coins
					if (gs.coinStacks[i] < gs.params.pickSameMinCoins)
						return false;
					//(B) can't take more/less than the defined amount or the available amount
					if (pattern[i] > Math.min(gs.coinStacks[i],gs.params.pickSameAmount)) {
						return false;
					}
				}
			}

			// (A)+(B) guarantees a single stack
		}

		return true;
	}

	@Override
	protected String actionName(){
		return "Pick Same Coins";
	}

	@Override
	public Action getActionLog() {
		return new Action(actionName(), "P"+playerId, new HashMap<String, Object>(){{put("pattern",pattern);}});
	}

	public static void main(String... args){
		State s = State.testState(null);

		System.out.println(s.toString());
		new PickSameCoins(0,new int[]{2,0,0,0,0}).perform(s);
		new PickSameCoins(0,new int[]{2,0,0,0,0}).perform(s);
		new PickSameCoins(0,new int[]{2,0,0,0,0}).perform(s);
		new PickSameCoins(0,new int[]{0,2,0,0,0}).perform(s);
		new PickSameCoins(0,new int[]{0,2,0,0,0}).perform(s);
		new PickSameCoins(0,new int[]{-3,2,0,0,0}).perform(s);
		System.out.println(s.toString());
	}
}
