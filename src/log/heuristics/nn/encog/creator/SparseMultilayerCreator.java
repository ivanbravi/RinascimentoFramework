package log.heuristics.nn.encog.creator;

import log.heuristics.nn.decoder.SparseMultilayerDecoder;
import log.heuristics.nn.encog.topology.EncogAdapter;
import log.heuristics.nn.encog.topology.SparseFFNetwork;

import java.util.*;

public class SparseMultilayerCreator extends MultilayerCreator {

	private HashMap<Integer,List<Integer>> disabledLists = null;

	public SparseMultilayerCreator(SparseMultilayerDecoder decoder) {
		super(decoder);
	}

	@Override
	public EncogAdapter create() {
		SparseFFNetwork network = new SparseFFNetwork();
		super.setupNetwork(network);

		if(disabledLists == null){
			SparseMultilayerDecoder d = (SparseMultilayerDecoder) this.decoder;
			long seed = d.getSeed();
			int[] rndConnections = d.getRndConnections();
			this.disabledLists = connectionLists(network, seed, rndConnections);
		}

		for(Integer l: disabledLists.keySet()){
			List<Integer> list = disabledLists.get(l);
			network.maskLayer(l, list);
		}

		return network;
	}

	public static HashMap<Integer,List<Integer>> connectionLists(SparseFFNetwork network, long seed, int[] rndConnections){
		HashMap<Integer,List<Integer>> disabledLists;
		Random r = new Random(seed);
		disabledLists = new HashMap<>();

		for(int sourceLayer=0; sourceLayer<rndConnections.length; sourceLayer++) {
			int connectionsToDisable = rndConnections[sourceLayer];
			if (connectionsToDisable != 0) {
				int cCount = network.connectionsFromLayer(sourceLayer);
				List<Integer> disabledIndices = createConnectionsList(cCount,r,connectionsToDisable);
				disabledLists.put(sourceLayer,disabledIndices);
			}
		}
		return disabledLists;
	}

	private static List<Integer> createConnectionsList(int cCount, Random r, int selection){
		ArrayList<Integer> l = new ArrayList<>();
		for(int i=0; i<cCount; i++)
			l.add(i);
		Collections.shuffle(l,r);
		return l.subList(0,selection);
	}

//
//
//	private void createConnectionLists(SparseFFNetwork network){
//		SparseMultilayerDecoder d = (SparseMultilayerDecoder) this.decoder;
//		long seed = d.getSeed();
//		Random r = new Random(seed);
//		int[] rndConnections = d.getRndConnections();
//
//		disabledLists = new HashMap<>();
//
//		for(int sourceLayer=0; sourceLayer<rndConnections.length; sourceLayer++) {
//			int connectionsToDisable = rndConnections[sourceLayer];
//			if (connectionsToDisable != 0) {
//				int cCount = network.connectionsFromLayer(sourceLayer);
//				List<Integer> disabledIndices = createConnectionsList(cCount,r,connectionsToDisable);
//				disabledLists.put(sourceLayer,disabledIndices);
//			}
//		}
//	}
//
//	private List<Integer> createConnectionsList(int cCount, Random r, int selection){
//		ArrayList<Integer> l = new ArrayList<>();
//		for(int i=0; i<cCount; i++)
//			l.add(i);
//		Collections.shuffle(l,r);
//		return l.subList(0,selection);
//	}


}
