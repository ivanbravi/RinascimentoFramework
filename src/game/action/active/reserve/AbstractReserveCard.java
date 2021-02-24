package game.action.active.reserve;

import game.action.PlayableAction;
import game.state.PlayerState;

public abstract class AbstractReserveCard extends PlayableAction {


	public AbstractReserveCard(int playerId) {
		super(playerId);
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof AbstractReserveCard))
			return false;
		return super.equals(obj);
	}

	protected int reservePosition(PlayerState ps){

		for(int i=0; i<ps.reservedCardIds.length; i++){
			if(ps.reservedCardIds[i] == -1){
				return i;
			}
		}
		return -1;
	}
}
