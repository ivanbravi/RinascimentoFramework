package game.action.active.reserve.board;

import game.action.ActionType;
import game.action.PlayableAction;
import game.state.State;

public class ATReserveBoardCard implements ActionType{

	@Override
	public String toString() {
		return "Reserve Board Card";
	}

	@Override
	public int countActions(State gs, int playerId) {
		return gs.params.deckCount*gs.params.cardsOnTableCount;
	}

	@Override
	public PlayableAction createAction(int actionId, State gs, int playerId) {
		int deckId = actionId/gs.params.cardsOnTableCount;
		int cardPosition= actionId%gs.params.cardsOnTableCount;
		return new ReserveBoardCard(playerId,deckId,cardPosition);
	}
}
