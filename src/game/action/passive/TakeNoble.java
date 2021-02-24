package game.action.passive;

import game.state.Noble;
import game.state.PlayerState;
import game.state.State;
import log.entities.action.Action;

public class TakeNoble implements PassiveActionInterface{

	@Override
	public void perform(State gs) {

		for(int pId=0; pId<gs.playerStates.length; pId++){
			PlayerState ps = gs.getPlayerState(pId);
			for (int i=0;i<gs.nobles.length; i++){
				if(!gs.isNobleTaken[i]) {
					Noble noble = gs.nobles[i];
					if (canGetNoble(noble.getCost(), ps.gems)) {
						gs.eventDispatcher().begin(gs.getTick(),"Engine",getActionLog());
						gs.takeNoble(i);
						gs.eventDispatcher().done();

						gs.eventDispatcher().begin(gs.getTick(),gs.getPlayerNames()[pId],getActionLog());
						ps.receiveNoble(noble.getId());
						ps.receiveNoblePoints(noble.getPoints());
						gs.eventDispatcher().done();

						break;
					}
				}
			}
		}
	}

	@Override
	public PassiveActionInterface clone() {
		return new TakeNoble();
	}

	private boolean canGetNoble(int[] nobleCost, int[] playerGems){
		for(int i=0; i<nobleCost.length; i++){
			if(nobleCost[i]>playerGems[i]){
				return false;
			}
		}
		return true;
	}

	@Override
	public Action getActionLog() {
		return new Action("Take Noble", "Engine", null);
	}
}
