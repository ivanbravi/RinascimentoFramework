package game.action.active.reserve.deck;

import game.action.ActionType;
import game.action.PlayableAction;
import game.state.State;

public class ATReserveDeckCard implements ActionType {

	@Override
	public String toString() {
		return "Reserve Deck Card";
	}

	@Override
	public int countActions(State gs, int playerId) {
		int nonEmptyDecks = 0;

		for(int i=0; i<gs.deckStacks.length; i++){
			if(!gs.deckStacks[i].isEmpty()){
				nonEmptyDecks++;
			}
		}

		return nonEmptyDecks ;
	}

	@Override
	public PlayableAction createAction(int actionId, State gs, int playerId) {
		int nonEmptyDecks = 0;
		for(int i=0; i<gs.deckStacks.length;i++){
			if(!gs.deckStacks[i].isEmpty()){
				if(nonEmptyDecks==actionId){
					return new ReserveDeckCard(playerId,i);
				}
				nonEmptyDecks++;
			}
		}

		return new ReserveDeckCard(playerId,actionId);
	}
}
