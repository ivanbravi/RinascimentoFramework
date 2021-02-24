package game.state.vectorise;

import game.Parameters;
import game.state.Deck;
import game.state.State;

import java.util.Arrays;

public class VectoriseCards implements Vectoriser {

	Parameters p;

	VectoriseCards(Parameters p){
		this.p = p;
	}

	@Override
	public double[] vectorise(State s, int playerID) {
		double[] f = new double[size()];
		double[] cardEncoding = new double[cardEncodingSize()];
		int counter=0;
		for(int deckId=0; deckId < p.deckCount; deckId++){
			for(int cardPosition=0; cardPosition < p.cardsOnTableCount; cardPosition++) {
				int displace = counter * cardEncodingSize();
				int cardId = s.board[deckId][cardPosition];
				encodeCard(s, cardEncoding, deckId, cardId);
				arraycopy(cardEncoding, 0, f, displace, cardEncodingSize(),1);
				counter++;
			}
		}
		return f;
	}

	private void encodeCard(State s, double[] encoding, int deckId, int cardId){
		Deck d = s.decks[deckId];
		Arrays.fill(encoding, 0);
		if(cardId!=-1) {
			encoding[0] = d.getCardPoints(cardId) / rescale(s.params.endGameScore);
			encoding[1 + d.getCardSuit(cardId)] = 1;
			arraycopy(d.getCardCost(cardId), 0, encoding, 1 + p.suitCount, p.suitCount, Params.COST_SCALE);
		}
	}

	@Override
	public String toString() {
		return "CardsVectoriser{ [" + this.size() + "] }";
	}

	@Override
	public int size() {
		return p.deckCount*p.cardsOnTableCount*cardEncodingSize();
	}

	private int cardEncodingSize(){
		return 1+p.suitCount*2;
		// 1 -> points;
		// suitCount -> one hot encoding for the suit;
		// suitCount -> cost per suit
	}

	@Override
	public Vectoriser clone() {
		return this;
	}
}
