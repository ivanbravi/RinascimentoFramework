package game.action.active.reserve.deck;

import game.action.active.reserve.AbstractReserveCard;
import game.state.PlayerState;
import game.state.State;
import log.entities.action.Action;

import java.util.HashMap;

public class ReserveDeckCard extends AbstractReserveCard {

	private int deckId;

	public ReserveDeckCard(int playerId, int deckId) {
		super(playerId);
		this.deckId = deckId;
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof ReserveDeckCard))
			return false;

		ReserveDeckCard r = (ReserveDeckCard) obj;
		if(r.deckId != this.deckId)
			return false;

		return super.equals(obj);
	}

	public boolean canPerform(State gs){
		PlayerState ps = gs.getPlayerState(playerId);
		int position = reservePosition(ps);

		if(position==-1)
			return false;

		if(gs.deckStacks[deckId].isEmpty())
			return false;

		return true;
	}

	@Override
	public boolean perform(State gs) {

		if(!canPerform(gs)){
			return failedAction();
		}

		gs.eventDispatcher().begin(gs.getTick(),gs.getPlayerNames()[playerId], getActionLog());

		PlayerState ps = gs.getPlayerState(playerId);
		int position = reservePosition(ps);
		int cardId = gs.deckStacks[deckId].drawCard();

		ps.reserveHiddenCard(position,deckId,cardId);
//		ps.reservedCardIds[position] = cardId;
//		ps.reservedDeckIds[position] = deckId;
//		ps.reservedIsVisible[position] = false;

		if(gs.goldStack>0){
			int goldTaken = 1;
			if(gs.getPlayerState(playerId).getCoinsCount()+goldTaken<=gs.params.maxCoins) {
				gs.decreaseGold(goldTaken);
				//gs.goldStack -= goldTaken;
				ps.increaseGold(goldTaken);
				//ps.gold += goldTaken;
			}
		}

		gs.eventDispatcher().done();

		return super.perform(gs);
	}

	@Override
	public Action getActionLog() {
		return new Action("ReserveBoardCard", "P"+playerId,
				new HashMap<String, Object>(){{
					put("deckId",deckId); }});
	}

	@Override
	public String toString() {
		return "[Reserve Deck Card] "+super.toString()+"\n\t[" +deckId+ "]";
	}


}
