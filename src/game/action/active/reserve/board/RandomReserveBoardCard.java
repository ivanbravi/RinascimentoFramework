package game.action.active.reserve.board;

import game.action.PlayableAction;
import game.action.random.RandomActionGenerator;
import game.state.State;
import utils.ArrayUtils;
import utils.Pair;

import java.util.ArrayList;

public class RandomReserveBoardCard extends RandomActionGenerator {

	@Override
	public String toString() {
		return "Reserve Board Card";
	}

	@Override
	public PlayableAction generate(State s, int playerId) {
		ArrayList<Pair<Integer,Integer>> availableCards = new ArrayList<>();

		int cardsReserved = ArrayUtils.filterIndicesGE(s.getPlayerState(playerId).reservedDeckIds,0).size();

		if(cardsReserved==s.params.maxReserveCards){
			return null;
		}

		for(int dId=0; dId<s.params.deckCount; dId++){
			for (int cPos=0; cPos<s.params.cardsOnTableCount; cPos++){
				if(s.board[dId][cPos]!=-1){
					availableCards.add(new Pair<>(dId,cPos));
				}
			}
		}

		if(availableCards.isEmpty())
			return null;

		int selection = rnd.nextInt(availableCards.size());
		int deckId = availableCards.get(selection).first();
		int cardPosition = availableCards.get(selection).second();

		return new ReserveBoardCard(playerId,deckId,cardPosition);
	}

	@Override
	public RandomActionGenerator clone() {
		return new RandomReserveBoardCard();
	}
}
