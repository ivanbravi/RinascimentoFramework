package game.action.active.buy.board;

import game.action.ActionType;
import game.action.PlayableAction;
import game.state.Deck;
import game.state.PlayerState;
import game.state.State;

public class ATBuyBoardCard implements ActionType {

	@Override
	public String toString() {
		return "Buy Board Card";
	}

	@Override
	public int countActions(State gs, int playerId) {
		int count = 0;
		PlayerState ps = gs.getPlayerState(playerId);
		for(int deckId=0; deckId<gs.params.deckCount; deckId++){
			for(int cardPosition=0; cardPosition<gs.params.cardsOnTableCount; cardPosition++){
				int cardId = gs.board[deckId][cardPosition];
				if(cardId!=-1){
					int[] cost = gs.decks[deckId].getCardCost(cardId);
					if(Deck.canBuy(cost, ps.coins, ps.gems, ps.gold)) {
						count++;
					}
				}
			}
		}
		return count;
	}

	@Override
	public PlayableAction createAction(int actionId, State gs, int playerId) {
		int count = -1;
		PlayerState ps = gs.getPlayerState(playerId);
		for(int deckId=0; deckId<gs.params.deckCount; deckId++){
			for(int cardPosition=0; cardPosition<gs.params.cardsOnTableCount; cardPosition++){
				int cardId = gs.board[deckId][cardPosition];
				if(cardId!=-1){
					int[] cost = gs.decks[deckId].getCardCost(cardId);
					if(Deck.canBuy(cost, ps.coins, ps.gems, ps.gold)) {
						count++;
						if(count==actionId){
							return new BuyBoardCard(playerId,deckId,cardPosition);
						}
					}
				}
			}
		}
		return null;
	}
}
