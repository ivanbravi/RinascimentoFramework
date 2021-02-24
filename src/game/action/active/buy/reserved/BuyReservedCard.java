package game.action.active.buy.reserved;

import game.action.PlayableAction;
import game.action.active.buy.BuyCard;
import game.action.active.reserve.board.ReserveBoardCard;
import game.state.State;
import log.entities.action.Action;

import java.util.HashMap;

public class BuyReservedCard extends BuyCard {

	public BuyReservedCard(int playerId, int cardPosition) {
		super(playerId, 0,cardPosition);
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof BuyReservedCard))
			return false;
		return super.equals(obj);
	}

	@Override
	protected int getCardId(State gs) {
		if(!isPositionValid(gs)){
			return -1;
		}
		this.deckId = gs.getPlayerState(this.playerId).reservedDeckIds[this.cardPosition];
		return gs.getPlayerState(this.playerId).reservedCardIds[this.cardPosition];
	}

	@Override
	protected void afterBuyUpdate(State gs) {
		gs.playerStates[playerId].reservedCardIds[cardPosition] = -1;
		gs.playerStates[playerId].reservedDeckIds[cardPosition] = -1;
	}

	@Override
	protected boolean isPositionValid(State gs) {
		if(!super.isPositionValid(gs)){
			return false;
		}

		if(cardPosition<0 || cardPosition >= gs.params.maxReserveCards){
			return false;
		}

		return true;
	}

	@Override
	public Action getActionLog() {
		return new Action("BuyBoardCard", "P"+playerId,
				new HashMap<String, Object>(){{
					put("deckId",deckId);
					put("cardPosition",cardPosition);}});
	}

	@Override
	public String toString() {
		return "[Buy reserved card] "+super.toString()+"\n\t["+cardPosition+"]";
	}

	public static void main(String... args){
		State s = State.testState(null);
		s.getPlayerState(0).coins = new int[]{7,7,7,7,7};

		new ReserveBoardCard(0,0,0).perform(s);

		PlayableAction a = new BuyReservedCard (0,0);

		System.out.println(s.toString());
		a.perform(s);
		System.out.println(s.toString());
	}

}
