package game.action.active.buy.board;

import game.action.PlayableAction;
import game.action.active.buy.BuyCard;
import game.state.State;
import log.entities.action.Action;

import java.util.HashMap;

public  class BuyBoardCard extends BuyCard {

	public BuyBoardCard(int playerId, int deckId, int cardPosition) {
		super(playerId,deckId,cardPosition);
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof BuyBoardCard))
			return false;
		return super.equals(obj);
	}

	@Override
	protected int getCardId(State gs) {
		return gs.board[this.deckId][cardPosition];
	}

	@Override
	protected boolean isPositionValid(State gs) {
		if(!super.isPositionValid(gs)){
			return false;
		}

		// check that the card position is valid
		if(cardPosition<0 || cardPosition>=gs.params.cardsOnTableCount){
			return false;
		}
		return true;
	}

	@Override
	protected void afterBuyUpdate(State gs) {
		gs.board[deckId][cardPosition] = gs.deckStacks[deckId].drawCard();
	}

	public String toString() {
		return "[Buy Board Card] "+super.toString()+"\n\t["+deckId+","+cardPosition+"]";
	}

	@Override
	public Action getActionLog() {
		return new Action("BuyBoardCard", "P"+playerId,
				new HashMap<String, Object>(){{
					put("deckId",deckId);
					put("cardPosition",cardPosition);}});
	}

	public static void main(String... args){
		State s = State.testState(null);
		s.getPlayerState(0).coins = new int[]{7,7,7,7,7};

		PlayableAction a = new BuyBoardCard(0,0,0);

		System.out.println(s.toString());
		a.perform(s);
		System.out.println(s.toString());
	}
}
