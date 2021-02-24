package game.state.vectorise;

import game.Parameters;
import game.state.State;

public class AllPlayersVectoriser implements Vectoriser {

	VectorisePlayer vp;
	protected int players;

	public AllPlayersVectoriser(Parameters p){
		vp = new VectorisePlayer(p);
		players = p.playerCount;
	}

	@Override
	public double[] vectorise(State s, int playerID) {
		double [] features = new double[size()];
		double [] playerFeatures = vp.vectorise(s,playerID);
		int i=0;

		System.arraycopy(playerFeatures, 0, features, i, playerFeatures.length);
		i += playerFeatures.length;

		for(int pId=0; pId<s.playerStates.length; pId++) {
			if(pId!=playerID) {
				playerFeatures = vp.vectorise(s, pId);
				System.arraycopy(playerFeatures, 0, features, i, playerFeatures.length);
				i += playerFeatures.length;
			}
		}

		return features;
	}

	@Override
	public String toString() {
		return "AllPlayersVectoriser{ " +"["+size()+"] "+ vp + " " + players +" }";
	}

	@Override
	public int size() {
		return vp.size()*players;
	}

	@Override
	public Vectoriser clone() {
		return this;
	}
}
