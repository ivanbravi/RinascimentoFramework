package log.heuristics.nn.encog.topology;

import java.util.*;

/**
 * This class allows for a sparse connection in a classic feed-forward neural network by subclassing BasicNetwork.
 */
public class SparseFFNetwork extends EncogAdapter {

	/**
	 * The map containing the connections to deactivate for a given source layer
	 */
	HashMap<Integer, List<Integer>>  inactiveIndices = new HashMap<>();

	/**
	 * This counter keeps track of the connections disabled.
	 */
	int inactiveCounter = 0;

	/**
	 * This method disables the connections from a source layer by specifying the weights indices.
	 * @param disabledIndices specifies which connections to the hidden layer are enabled, through their indices.
	 *                   Each index is encoded as (sourceNeuronIndex+destinationNeuronIndex*sourceNeuronSize)
	 * @param sourceLayer indicates the source layer of the connections to be disabled
	*/
	public void maskLayer(int sourceLayer, List<Integer> disabledIndices){
		if(inactiveIndices.containsKey(sourceLayer)){
			List<Integer> oldIndices = inactiveIndices.get(sourceLayer);
			this.bulkSetEnable(sourceLayer,oldIndices,true);
			inactiveCounter -= oldIndices.size();
		}
		this.inactiveIndices.put(sourceLayer, disabledIndices);
		this.bulkSetEnable(sourceLayer,disabledIndices,true);
		inactiveCounter += disabledIndices.size();
	}

	/**
	 * This method sets the enabled status of connections in bulk.
	 * @param sourceLayer is the the layer from which the connections start
	 * @param indices is the list of indices to enable/disable
	 * @param enable is the value to be set
	 */
	private void bulkSetEnable(int sourceLayer, List<Integer> indices, boolean enable){
		for(Integer i: indices){
			int fromNeuron = fromNeuron(sourceLayer, i);
			int toNeuron = toNeuron(sourceLayer,i);
			this.enableConnection(sourceLayer,fromNeuron,toNeuron,enable);
		}
	}

	/**
	 * This method matches the weights to the corresponding connection keeping track of the connections disabled.
	 * @param encoded contains the weights of the network, but only the ones of enabled connections.
	 */
	@Override
	public void decodeFromArray(double[] encoded) {
		int progressive=0;
		for(int layer=0; layer<this.getLayerCount(); layer++) {
			List<Integer> inactive = inactiveIndices.containsKey(layer) ? inactiveIndices.get(layer) : new ArrayList<>();
			for(int neuronIndex = 0; neuronIndex< connectionsFromLayer(layer); neuronIndex++){
				int fromNeuron = fromNeuron(layer,neuronIndex);
				int toNeuron = toNeuron(layer,neuronIndex);
				if(!inactive.contains(neuronIndex)){
					super.setWeight(layer, fromNeuron, toNeuron, encoded[progressive++]);
				}else{
					this.enableConnection(layer, fromNeuron, toNeuron, false);
				}
			}
		}
		if(progressive!=encoded.length){
			System.out.println("Encoding incoherency!");
		}
	}

	/**
	 * This method sets the weight of a specific connection but only if the connection isn't deactivated.
	 * @param fromLayer specifies where from what layer the connection starts from
	 * @param fromNeuron from what neuron the connection starts
	 * @param toNeuron to what neuron the connection ends
	 * @param value is the new weight of the connection
	 */
	@Override
	public void setWeight(int fromLayer, int fromNeuron, int toNeuron, double value) {
		if(inactiveIndices.containsKey(fromLayer))
			if(inactiveIndices.get(fromLayer).contains(index(fromLayer, fromNeuron, toNeuron)))
				return;
		super.setWeight(fromLayer, fromNeuron, toNeuron, value);
	}

	/**
	 * This method allows quick and short access to the number of neurons in the specified layer.
	 * @param layer what layer of the network to query
	 * @return the amount of neurons in the layer
	 */
	private int width(int layer){
		return this.getLayerTotalNeuronCount(layer);
	}

	/**
	 * This method is used to compute the amount of connections from a layer to the next.
	 * @param layer specifies the source layer of the connections to count
	 * @return counts how many connections spring from neurons of the layer specified
	 */
	public int connectionsFromLayer(int layer){
		// if is one of the non-output layers
		if(layer >=0 && layer<this.getLayerCount()-1)
				return this.getLayerTotalNeuronCount(layer)*this.getLayerNeuronCount(layer+1);
		return 0;
	}

	/**
	 * This method computes from what neuron of a specific layer a connection springs.
	 * @param layer used to select the source layer
	 * @param index is the connection index in the list of connections
	 * @return the source neuron of the connection
	 */
	private int fromNeuron(int layer, int index){
		return index%width(layer);
	}

	/**
	 * This method computes to what neuron of a specific layer+1 a certain connection reaches.
	 * @param layer used to select the source layer
	 * @param index is the connection index in the list of connections
	 * @return the destination neuron of the connection
	 */
	private int toNeuron(int layer, int index){
		return index/width(layer);
	}


	/**
	 * This methods combines source (fromNeuron) and destination (toNeuron) neuron in layers layer and layer+1 to compute the index of such connection.
	 * @param layer is the source layer
	 * @param fromNeuron the source of the connection
	 * @param toNeuron the destination of the connection
	 * @return the index of the connection
	 */
	private int index(int layer, int fromNeuron, int toNeuron){
		return fromNeuron+toNeuron*width(layer);
	}

	/**
	 * Implementation of the NNAdapter interface to get how many weights (enabled connections) the neural network has
	 * @return the number of weights of the neural network
	 */
	@Override
	public int weightsCount() {
		return super.weightsCount()-inactiveCounter;
	}

}
