package game.state.vectorise;

import game.Parameters;
import game.state.Noble;
import game.state.State;

import java.util.Arrays;

public class VectoriseNobles implements Vectoriser {

	private int maxNobles;
	private int nobleEncodingSize;

	public VectoriseNobles(Parameters p){
		this.maxNobles = p.noblesCount;
		this.nobleEncodingSize = p.suitCount+1;
	}

	@Override
	public double[] vectorise(State s, int playerID) {
		double[] f = new double[size()];
		double[] nobleFeatures;
		Arrays.fill(f,0);
		for(int i=0; i<maxNobles; i++){
			if(!s.isNobleTaken[i]){
				nobleFeatures = encodeNoble(s.nobles[i],s);
				arraycopy(nobleFeatures, 0, f, i*nobleEncodingSize,nobleEncodingSize, 1);
			}
		}
		return f;
	}

	private double[] encodeNoble(Noble n, State s){
		double[] f = new double[nobleEncodingSize];
		int[] cost = n.getCost();

		// noble "cost" vector
		arraycopy(cost,0,f,1,cost.length, Params.COST_SCALE);

		// noble points
		f[0] = n.getPoints() / rescale(s.params.endGameScore);

		return f;
	}

	@Override
	public String toString() {
		return "VectoriseNobles{ " +"["+size()+"] "+ maxNobles + " }";
	}

	@Override
	public int size() {
		return maxNobles*nobleEncodingSize;
	}

	@Override
	public Vectoriser clone() {
		return this;
	}
}
