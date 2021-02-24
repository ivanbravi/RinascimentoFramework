package log.heuristics.nn.encog.creator;

import log.heuristics.nn.decoder.MultilayerNNDecoder;
import log.heuristics.nn.encog.topology.EncogAdapter;
import org.encog.engine.network.activation.ActivationFunction;
import org.encog.neural.networks.layers.BasicLayer;

public class MultilayerCreator extends NNCreatorEncog {


	public MultilayerCreator(MultilayerNNDecoder decoder) {
		super(decoder);
	}

	@Override
	public EncogAdapter create() {
		EncogAdapter network = new EncogAdapter();
		setupNetwork(network);

		return network;
	}

	protected void setupNetwork(EncogAdapter network){
		MultilayerNNDecoder d = (MultilayerNNDecoder) this.decoder;
		ActivationFunction f = (ActivationFunction) decodeFunction(d.getFunction());
		ActivationFunction outF = (ActivationFunction) decodeFunction(LIN);
		for(int nCount : d.getLayers()){
			network.addLayer(new BasicLayer(f,true, nCount));
		}
		if(d.getLayers()[d.getLayers().length-1]!=1)
			network.addLayer(new BasicLayer(outF,false,1));
		network.getStructure().finalizeStructure();
	}

}
