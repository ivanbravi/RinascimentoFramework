package game.action.active.buy.reserved;

import game.action.PlayableAction;
import game.action.random.RandomActionGenerator;
import game.state.Deck;
import game.state.PlayerState;
import game.state.State;

import java.util.ArrayList;

public class RandomBuyReservedCard extends RandomActionGenerator{

	@Override
	public String toString() {
		return "Buy Reserved Card";
	}

	@Override
	public PlayableAction generate(State s, int playerId) {
		ArrayList<Integer> availableCards = new ArrayList<>();
		PlayerState ps = s.getPlayerState(playerId);

		for (int cardPos=0; cardPos<s.params.maxReserveCards; cardPos++){
			int cardId = ps.reservedCardIds[cardPos];
			int deckId = ps.reservedDeckIds[cardPos];
			if(cardId!=-1){
				int[] cost = s.decks[deckId].getCardCost(cardId);
				if(Deck.canBuy(cost,ps.coins,ps.gems,ps.gold)) {
					availableCards.add(cardPos);
				}
			}
		}

		if(availableCards.isEmpty())
			return null;


		int rndSelection = rnd.nextInt(availableCards.size());
		int cardPosition = availableCards.get(rndSelection);

		return new BuyReservedCard(playerId,cardPosition);
	}

	@Override
	public RandomActionGenerator clone() {
		return new RandomBuyReservedCard();
	}
}
