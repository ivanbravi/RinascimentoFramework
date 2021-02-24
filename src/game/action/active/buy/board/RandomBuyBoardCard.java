package game.action.active.buy.board;

import game.action.PlayableAction;
import game.action.random.RandomActionGenerator;
import game.state.Deck;
import game.state.PlayerState;
import game.state.State;
import utils.Pair;

import java.util.ArrayList;

public class RandomBuyBoardCard extends RandomActionGenerator {

	@Override
	public String toString() {
		return "Buy Board Card";
	}

	@Override
	public PlayableAction generate(State s, int playerId) {
		ArrayList<Pair<Integer,Integer>> availableCards = new ArrayList<>();
		PlayerState ps = s.getPlayerState(playerId);

		for(int dId=0; dId<s.params.deckCount; dId++){
			for (int cardPos=0; cardPos<s.params.cardsOnTableCount; cardPos++){
				int cardId = s.board[dId][cardPos];
				if(cardId!=-1){
					int[] cost = s.decks[dId].getCardCost(cardId);
					if(Deck.canBuy(cost,ps.coins,ps.gems,ps.gold)) {
						availableCards.add(new Pair<>(dId, cardPos));
					}
				}
			}
		}

		if(availableCards.isEmpty())
			return null;


		int rndSelection = rnd.nextInt(availableCards.size());

		int deckId = availableCards.get(rndSelection).first();
		int cardPosition = availableCards.get(rndSelection).second();

		return new BuyBoardCard(playerId,deckId,cardPosition);
	}

	@Override
	public RandomActionGenerator clone() {
		return new RandomBuyBoardCard();
	}
}
