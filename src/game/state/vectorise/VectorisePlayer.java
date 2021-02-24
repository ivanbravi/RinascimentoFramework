package game.state.vectorise;

import game.Parameters;
import game.state.PlayerState;
import game.state.State;

import java.util.Arrays;

public class VectorisePlayer implements Vectoriser {

	private int playerFeatures;

	// - - - - - - F E A T U R E S - - - - - -
	// 1 						points
	// suitCount				tokens
	// 1						joker token
	// suitCount				gems
	// 1						noble count
	// 1						reserved cards
	// - - - - - - - - - - - - - - - - - - - -

	public VectorisePlayer(Parameters p){
		playerFeatures = p.suitCount*2+4;
	}

	@Override
	public double[] vectorise(State s, int playerID) {
		double[] features = new double[size()];
		PlayerState p = s.playerStates[playerID];
		int i=0;

		// points
		features[i++] = p.points/ rescale(s.params.endGameScore);

		// tokens
		arraycopy(p.coins, 0, features, i, p.coins.length, s.params.coinCount);
		i += p.coins.length;

		// joker tokens
		features[i++] = p.gold / rescale(s.params.coinCount);

		// cost
		arraycopy(p.gems, 0, features, i, p.gems.length, Params.COST_SCALE);
		i += p.gems.length;

		// noble count
		features[i++] = p.nobles.size() / rescale(s.params.noblesCount);

		// reserved cards count
		features[i++] = Arrays.stream(p.reservedCardIds).filter(v -> v != -1).count() / rescale(s.params.maxReserveCards);

		if(size()!=i){
			throw new RuntimeException("["+this.getClass().getSimpleName()+"] Wrong feature size");
		}

		return features;
	}

	@Override
	public String toString() {
		return "VectorisePlayer{ ["+ playerFeatures +"] }";
	}

	@Override
	public int size() {
		return playerFeatures;
	}

	@Override
	public Vectoriser clone() {
		return this;
	}
}
