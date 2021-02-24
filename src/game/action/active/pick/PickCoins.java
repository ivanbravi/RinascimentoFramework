package game.action.active.pick;

import game.action.PlayableAction;
import game.state.PlayerState;
import game.state.State;

import java.util.Arrays;

public class PickCoins extends PlayableAction {

	protected int[] pattern;

	public PickCoins(int playerId, int[] pattern){
		super(playerId);
		this.pattern = pattern;
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof PickCoins))
			return false;
		PickCoins p = (PickCoins) obj;

		for(int i=0; i<this.pattern.length; i++){
			if(this.pattern[i]!=p.pattern[i])
				return false;
		}

		return super.equals(obj);
	}

	public boolean canPerform(State gs){
		// no coins no party
		if(Arrays.stream(gs.coinStacks).sum()==0)
			return false;

		// can't give back more coins than I'm taking
		int deltaAmount = Arrays.stream(pattern).sum();
		if(deltaAmount<0)
			return false;

		int currAmount = gs.getPlayerState(playerId).getCoinsCount();

		// can't take more coins than allowed
		if(currAmount+deltaAmount>gs.params.maxCoins)
			return false;

		for(int i=0; i<pattern.length; i++){
			if(pattern[i]>0){
				// can't take more coins than available
				if(gs.coinStacks[i]<pattern[i])
					return false;
			}else{
				// can't give more coins than owned
				if(pattern[i]<0 && gs.getPlayerState(playerId).coins[i]<-pattern[i])
					return false;
			}
		}

		return true;
	}

	@Override
	public boolean perform(State gs) {

		if(!canPerform(gs)){
			return failedAction();
		}

		gs.eventDispatcher().begin(gs.getTick(),gs.getPlayerNames()[playerId], getActionLog());

		PlayerState ps = gs.getPlayerState(playerId);

		for(int i=0; i<pattern.length; i++){
			if(pattern[i]>0) {
				gs.decreaseTokens(i, pattern[i]);
				//gs.coinStacks[i]-=pattern[i];
				ps.increaseTokens(i, pattern[i]);
				//ps.coins[i]+=pattern[i];
			}else if(pattern[i]<0){
				gs.increaseTokens(i, -pattern[i]);
				ps.decreaseTokens(i, -pattern[i]);
			}
		}

		gs.eventDispatcher().done();

		return super.perform(gs);
	}

	protected int countGive(){
		int c = 0;
		for(int i=0; i<pattern.length; i++){
			if(pattern[i]<0)
				c++;
		}
		return c;
	}

	protected int countTake(){
		int c = 0;
		for(int i=0; i<pattern.length; i++){
			if(pattern[i]>0)
				c++;
		}
		return c;
	}

	protected int takeAmount(){
		int count = 0;
		for(int i=0; i<pattern.length; i++)
			if(pattern[i]>0)
				count+=pattern[i];
		return count;
	}

	protected String actionName(){
		return "Arbitrary Pick";
	}

	public String toString() {
		return "["+actionName()+"] " + super.toString() + "\n\t" + Arrays.toString(pattern);
	}

}
