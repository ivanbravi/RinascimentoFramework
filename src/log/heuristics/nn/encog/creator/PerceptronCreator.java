package log.heuristics.nn.encog.creator;

import log.heuristics.nn.NNInterface;
import log.heuristics.nn.decoder.PerceptronDecoder;
import log.heuristics.nn.encog.topology.EncogAdapter;
import org.encog.engine.network.activation.ActivationFunction;
import org.encog.neural.networks.layers.BasicLayer;

public class PerceptronCreator extends NNCreatorEncog {


	public PerceptronCreator(PerceptronDecoder decoder) {
		super(decoder);
	}

	@Override
	public NNInterface create() {
		EncogAdapter network = new EncogAdapter();
		PerceptronDecoder d = (PerceptronDecoder) this.decoder;
		ActivationFunction f = (ActivationFunction) decodeFunction(d.getFunction());
		ActivationFunction outF = (ActivationFunction) decodeFunction(LIN);
		network.addLayer(new BasicLayer(f,true,d.getInputs()));
		network.addLayer(new BasicLayer(outF,false,1));
		network.getStructure().finalizeStructure();
		return network;
	}
}
