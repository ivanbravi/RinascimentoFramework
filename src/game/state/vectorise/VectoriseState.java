package game.state.vectorise;

import game.Parameters;
import game.exceptions.TimedStaleGameException;
import game.state.State;

import java.util.Arrays;

public class VectoriseState implements Vectoriser {

	// - - - - - - F E A T U R E S - - - - - -
	// 1						tick
	// deckCount 				cards left
	// deckCount				cards face up
	// suitCount				common tokens left
	// 1						gold tokens left
	// 1						nobles left
	// - - - - - - - - - - - - - - - - - - - -

	private int features;

	public VectoriseState(Parameters p){
		features = p.suitCount+p.deckCount*2+3;
	}

	@Override
	public double[] vectorise(State s, int playerID) {
		double[] f = new double[size()];
		int i=0, c=0;

		// game tick
		f[i++] = s.getTick() / rescale(TimedStaleGameException.timer);

		// decks card counters
		for(int deckId=0; deckId<s.deckStacks.length; deckId++) {
			f[i++] = s.deckStacks[deckId].size() / rescale(s.decks[deckId].getCardCount());
		}

		// cards on the table per deck
		for(int deckId=0; deckId<s.board.length; deckId++) {
			f[i++] = Arrays.stream(s.board[deckId]).filter(id -> id!= -1).count() / rescale(s.params.cardsOnTableCount);
		}

		// coins on stacks
		arraycopy(s.coinStacks, 0, f, i, s.coinStacks.length, s.params.coinCount);
		i += s.coinStacks.length;

		// gold coins on stack
		f[i++] = s.goldStack / rescale(s.params.goldCount);


		// nobles left
		for(int j=0; j<s.isNobleTaken.length; j++)
			if(!s.isNobleTaken[j])
				c++;
		f[i++] = c / rescale(s.nobles.length);

		if(size()!=i){
			throw new RuntimeException("["+this.getClass().getSimpleName()+"] Wrong feature size");
		}

		return f;
	}

	@Override
	public String toString() {
		return "VectoriseState{ ["+features+"] }";
	}

	@Override
	public int size() {
		return features;
	}

	@Override
	public Vectoriser clone() {
		return this;
	}
}
