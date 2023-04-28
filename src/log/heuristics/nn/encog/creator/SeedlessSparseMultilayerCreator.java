package log.heuristics.nn.encog.creator;

import log.heuristics.nn.decoder.SeedlessSparseMultilayerDecoder;
import log.heuristics.nn.encog.topology.EncogAdapter;
import log.heuristics.nn.encog.topology.SparseFFNetwork;

import java.util.*;

public class SeedlessSparseMultilayerCreator extends MultilayerCreator{


	public SeedlessSparseMultilayerCreator(SeedlessSparseMultilayerDecoder decoder){
		super(decoder);
	}

	@Override
	public EncogAdapter create() {
		return createWithSeed(0);
	}

	public EncogAdapter createWithSeed(long seed){
		SparseFFNetwork network = new SparseFFNetwork();
		super.setupNetwork(network);
		SeedlessSparseMultilayerDecoder creator = (SeedlessSparseMultilayerDecoder) this.decoder;
		HashMap<Integer,List<Integer>> disabled = SparseMultilayerCreator.connectionLists(network, seed, creator.getRndConnections());

		for(Integer l: disabled.keySet()) {
			List<Integer> list = disabled.get(l);
			network.maskLayer(l, list);
		}
		return network;
	}



}
