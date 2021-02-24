package log.heuristics.nn.encog.topology;

import log.heuristics.nn.NNInterface;
import org.encog.neural.networks.BasicNetwork;

public class EncogAdapter extends BasicNetwork implements NNInterface {

	/**
	 * Implementation of the NNAdapter interface to set the weights of the network
	 * @param w is the array of weights for the network
	 */
	@Override
	public void setWeights(double[] w) {
		this.decodeFromArray(w);
	}

	/**
	 * Implementation of the NNAdapter interface to get how many weights (enabled connections) the neural network has
	 * @return the number of weights of the neural network
	 */
	@Override
	public int weightsCount() {
		return this.getStructure().calculateSize();
	}
}
