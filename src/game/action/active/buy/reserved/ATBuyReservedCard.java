package game.action.active.buy.reserved;

import game.action.ActionType;
import game.action.PlayableAction;
import game.state.Deck;
import game.state.PlayerState;
import game.state.State;

public class ATBuyReservedCard implements ActionType {

	@Override
	public String toString() {
		return "Buy Reserved Card";
	}

	@Override
	public int countActions(State gs, int playerId) {
		int count = 0;
		PlayerState ps = gs.getPlayerState(playerId);
		for(int cardPosition=0; cardPosition<gs.params.maxReserveCards; cardPosition++){
			int deckId = ps.reservedDeckIds[cardPosition];
			int cardId = ps.reservedCardIds[cardPosition];

			if(cardId!=-1 && deckId!=-1){
				int[] cost = gs.decks[deckId].getCardCost(cardId);
				if(Deck.canBuy(cost, ps.coins, ps.gems, ps.gold)) {
					count++;
				}
			}
		}
		return count;
	}

	@Override
	public PlayableAction createAction(int actionId, State gs, int playerId) {
		int count = -1;
		PlayerState ps = gs.getPlayerState(playerId);
		for(int cardPosition=0; cardPosition<gs.params.maxReserveCards; cardPosition++){
			int deckId = ps.reservedDeckIds[cardPosition];
			int cardId = ps.reservedCardIds[cardPosition];

			if(cardId!=-1 && deckId!=-1){
				int[] cost = gs.decks[deckId].getCardCost(cardId);
				if(Deck.canBuy(cost, ps.coins, ps.gems, ps.gold)) {
					count++;
					if(count==actionId){
						return new BuyReservedCard(playerId,cardPosition);
					}
				}
			}
		}
		return null;
	}
}
