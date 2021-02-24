package game.action.active.reserve.deck;

import game.action.PlayableAction;
import game.action.random.RandomActionGenerator;
import game.state.State;
import utils.ArrayUtils;

import java.util.ArrayList;

public class RandomReserveDeckCard extends RandomActionGenerator {

	@Override
	public String toString() {
		return "Reserve Deck Card";
	}

	@Override
	public PlayableAction generate(State s, int playerId) {
		ArrayList<Integer> availableDeckIds = new ArrayList<>();
		int cardsReserved = ArrayUtils.filterIndicesGE(s.getPlayerState(playerId).reservedDeckIds,0).size();

		if(cardsReserved==s.params.maxReserveCards){
			return null;
		}

		for(int i=0; i<s.params.deckCount; i++){
			if(!s.deckStacks[i].isEmpty()){
				availableDeckIds.add(i);
			}
		}

		if(availableDeckIds.isEmpty())
			return null;

		int deckId = availableDeckIds.get(rnd.nextInt(availableDeckIds.size()));
		return new ReserveDeckCard(playerId,deckId);
	}

	@Override
	public RandomActionGenerator clone() {
		return new RandomReserveDeckCard();
	}
}
