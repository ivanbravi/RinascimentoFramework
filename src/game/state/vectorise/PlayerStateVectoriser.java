package game.state.vectorise;

import game.state.State;

public class PlayerStateVectoriser implements Vectoriser {

	protected Vectoriser vs;
	protected Vectoriser vp;

	public PlayerStateVectoriser(Vectoriser playerVectoriser, Vectoriser stateVectoriser){
		this.vs = stateVectoriser;
		this.vp = playerVectoriser;
	}

	@Override
	public double[] vectorise(State s, int playerID){
		double [] features = new double[size()];
		double [] stateFeatures = vs.vectorise(s,playerID);
		double [] playerFeatures = vp.vectorise(s,playerID);
		System.arraycopy(stateFeatures, 0, features, 0, stateFeatures.length);
		System.arraycopy(playerFeatures, 0, features, stateFeatures.length, playerFeatures.length);

		return features;
	}

	@Override
	public String toString() {
		return "PlayerStateVectoriser{ " +"["+size()+"] "+ vs + " " + vp + " }";
	}

	@Override
	public int size() {
		return vs.size()+vp.size();
	}

	@Override
	public Vectoriser clone() {
		return this;
	}
}
