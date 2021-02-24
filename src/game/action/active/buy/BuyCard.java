package game.action.active.buy;

import game.action.PlayableAction;
import game.state.Deck;
import game.state.PlayerState;
import game.state.State;

public abstract class BuyCard extends PlayableAction {

	protected int deckId;
	protected int cardPosition;

	public BuyCard(int playerId, int deckId, int cardPosition){
		super(playerId);
		this.deckId = deckId;
		this.cardPosition = cardPosition;
	}

	protected abstract int getCardId(State gs);

	protected boolean isPositionValid(State gs){
		// check that the deck id is valid
		if(deckId<0 || deckId>=gs.params.deckCount){
			return false;
		}
		return true;
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof BuyCard))
			return false;

		BuyCard b = (BuyCard) obj;
		if(b.cardPosition!= this.cardPosition || b.deckId!=this.deckId)
			return false;

		return super.equals(obj);
	}

	public boolean canPerform(State gs){
		PlayerState ps = gs.getPlayerState(this.playerId);

		// check that the position is valid
		if(!isPositionValid(gs)){
			return false;
		}

		// check that the position contains an actual card
		int cardId = getCardId(gs);
		if(cardId==-1){
			return false;
		}

		// check that the player has enough resources
		int[] cost = gs.decks[deckId].getCardCost(cardId);

		if(!Deck.canBuy(cost,ps.coins,ps.gems,ps.gold)){
			return false;
		}

		return true;
	}

	protected abstract void afterBuyUpdate(State gs);

	@Override
	public boolean perform(State gs) {
		if(!canPerform(gs)){
			return failedAction();
		}

		gs.eventDispatcher().begin (gs.getTick(),gs.getPlayerNames()[playerId],this.getActionLog());

		// consume resources
		int cardId = getCardId(gs);
		int[] cost = gs.decks[deckId].getCardCost(cardId);
		int suitId = gs.decks[deckId].getCardSuit(cardId);
		int points = gs.decks[deckId].getCardPoints(cardId);
		PlayerState ps = gs.getPlayerState(this.playerId);

		for(int i=0; i<cost.length; i++){
			int resourcesMissing = Math.max(0,cost[i]-(ps.gems[i]+ps.coins[i]));
			int amt = Math.max(0,cost[i]-ps.gems[i]-resourcesMissing);
			if(amt!=0) {
				ps.decreaseTokens(i, amt);
				// ps.coins[i] -= amt;

				gs.increaseTokens(i, amt);
				// gs.coinStacks[i] += amt;
			}
			if(resourcesMissing!=0) {
				ps.decreaseGold(resourcesMissing);
				// ps.gold -= resourcesMissing;
				gs.increaseGold(resourcesMissing);
				// gs.goldStack += resourcesMissing;
			}
		}

		// update player's points and gems
		ps.receiveCardPoints(points);
		//ps.points += points;

		ps.receiveBonus(suitId,1);
		//ps.gems[suitId]++;

		afterBuyUpdate(gs);

		gs.eventDispatcher().done();

		if(isVerbose){
			System.out.println("{•••••••••••••••••••••••••••}");
			System.out.println("P"+playerId+" BOUGHT"+
					Deck.cardDescription(cardId,suitId, points,cost));
			System.out.println("{•••••••••••••••••••••••••••}");
		}

		return super.perform(gs);
	}
}
