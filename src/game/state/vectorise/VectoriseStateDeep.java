package game.state.vectorise;

import game.Parameters;
import game.state.State;

public class VectoriseStateDeep implements Vectoriser{

	VectoriseCards vc;
	VectoriseState vs;
	VectoriseNobles vn;

	public VectoriseStateDeep(Parameters p) {
		vs = new VectoriseState(p);
		vc = new VectoriseCards(p);
		vn = new VectoriseNobles(p);
	}

	@Override
	public double[] vectorise(State s, int playerID) {
		double[] f = new double[size()];
		double[] stateFeatures = vs.vectorise(s, playerID);
		double[] cardsFeatures = vc.vectorise(s,playerID);
		double[] noblesFeatures = vn.vectorise(s,playerID);

		System.arraycopy(stateFeatures,0,f,0,stateFeatures.length);
		System.arraycopy(cardsFeatures,0,f,stateFeatures.length,cardsFeatures.length);
		System.arraycopy(noblesFeatures,0,f,stateFeatures.length+cardsFeatures.length, noblesFeatures.length);

		return f;
	}

	@Override
	public String toString() {
		return "VectoriseStateDeep{ " +"["+size()+"] "+ vc + " " + vs + " " + vn + " }";
	}

	@Override
	public int size() {
		return vs.size()+ vc.size()+vn.size();
	}

	@Override
	public Vectoriser clone() {
		return this;
	}
}
