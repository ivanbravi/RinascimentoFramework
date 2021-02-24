package game.action.active.pick.same;

import game.action.ActionType;
import game.action.PlayableAction;
import game.action.active.pick.different.ATPickDifferentCoinsGeneric;
import game.state.State;

public class ATPickSameCoinsGeneric implements ActionType {

	@Override
	public String toString() {
		return "Pick Same Coins";
	}

	@Override
	public int countActions(State gs, int playerId) {
		int base = getEncodingBase(gs,playerId,extraCoins(gs,playerId)>0);
		return (int) Math.pow(base, gs.params.suitCount);
	}

	@Override
	public PlayableAction createAction(int actionId, State gs, int playerId) {
		int [] pattern = new int[gs.params.suitCount];
		boolean hasToGive = extraCoins(gs,playerId)>0;
		int base = getEncodingBase(gs,playerId,hasToGive);

		ATPickDifferentCoinsGeneric.decodeActionId(actionId,pattern,base);

		if(hasToGive) {
			transpose(pattern, 1+gs.params.pickSameAmount);
		}else{
			transpose(pattern,2);
			for(int i=0; i<pattern.length; i++)
				if(pattern[i]==1)
					pattern[i] = gs.params.pickSameAmount;
		}

		return new PickSameCoins(playerId, pattern);
	}

	private int extraCoins(State gs, int playerId){
		int futureCoins = gs.getPlayerState(playerId).getCoinsCount()+gs.params.pickSameAmount;
		int extra = futureCoins-gs.params.maxCoins;
		return extra>0?extra:0;
	}

	private void transpose(int []v, int delta){
		for(int i=0; i<v.length;i++)
			v[i] = -v[i]+delta;
	}

	private int getEncodingBase(State gs, int playerId, boolean hasToGive){
		if(hasToGive)
			return extraCoins(gs,playerId)+1+gs.params.pickSameAmount;
		return extraCoins(gs,playerId)+2;
	}
}
