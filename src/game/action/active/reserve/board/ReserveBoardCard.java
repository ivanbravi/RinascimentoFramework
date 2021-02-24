package game.action.active.reserve.board;

import game.action.active.reserve.AbstractReserveCard;
import game.state.PlayerState;
import game.state.State;
import log.entities.action.Action;

import java.util.HashMap;

public class ReserveBoardCard extends AbstractReserveCard {

	private int deckId;
	private int cardPosition;

	public ReserveBoardCard(int playerId, int deckId, int cardPosition) {
		super(playerId);
		this.deckId = deckId;
		this.cardPosition = cardPosition;
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof ReserveBoardCard)){
			return false;
		}

		ReserveBoardCard r = (ReserveBoardCard) obj;
		if(r.cardPosition != this.cardPosition)
			return false;
		if(r.deckId != this.deckId)
			return false;

		return super.equals(obj);
	}

	public boolean canPerform(State gs){
		if(deckId<0 || deckId>=gs.params.deckCount){
			return false;
		}

		if(cardPosition<0 || cardPosition>=gs.params.cardsOnTableCount){
			return false;
		}

		PlayerState ps = gs.getPlayerState(playerId);
		int reservePosition = reservePosition(ps);

		if(reservePosition==-1){
			return false;
		}

		return true;
	}

	@Override
	public boolean perform(State gs) {

		if(!canPerform(gs)){
			return failedAction();
		}

		gs.eventDispatcher().begin(gs.getTick(), gs.getPlayerNames()[playerId], getActionLog());

		PlayerState ps = gs.getPlayerState(playerId);
		int cardId = gs.board[deckId][cardPosition];
		int reservePosition = reservePosition(ps);

		// take card
		ps.reserveCard(reservePosition,deckId,cardId);
//		ps.reservedDeckIds[reservePosition] = deckId;
//		ps.reservedCardIds[reservePosition] = cardId;


		// take gold
		if(gs.goldStack>0){
			int goldTaken = 1;
			if(gs.getPlayerState(playerId).getCoinsCount()+goldTaken<=gs.params.maxCoins) {
				gs.decreaseGold(goldTaken);
				//gs.goldStack -= goldTaken;
				ps.increaseGold(goldTaken);
				//ps.gold += goldTaken;
			}
		}

		// put back card
		gs.placeCard(deckId,cardPosition);
		//gs.board[deckId][cardPosition] = gs.deckStacks[deckId].drawCard();

		gs.eventDispatcher().done();

		return super.perform(gs);
	}

	@Override
	public Action getActionLog() {
		return new Action("ReserveBoardCard", "P"+playerId,
				new HashMap<String, Object>(){{
					put("deckId",deckId);
					put("cardPosition",cardPosition);}});
	}

	@Override
	public String toString() {
		return "[Reserve Board Card] "+super.toString()+"\n\t[" +deckId+ "," +cardPosition+ "]";
	}
}
