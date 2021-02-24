package log.heuristics.nn.decoder;

import java.util.Arrays;

public class SparseMultilayerDecoder extends MultilayerNNDecoder {

	protected long seed;
	protected int[] rndConnections;
	private int layersCount;
	private int rndConnLayersCount;
	private int seedsCount;


	public SparseMultilayerDecoder(String encoding) {
		super(encoding);
	}

	@Override
	protected void decode(String encoding) {
		String[] splits = encoding.split(",");

		countFeatures(splits);

		this.function = splits[0];
		this.layers = new int[layersCount];

		// N layers have N-1 connections
		// The last layer must be fully connected -1
		// -> layersCount - 2
		this.rndConnections = new int[layersCount-2];
		int layerIndex = 0;

		for(int i=1; i<splits.length; i++) {
			String s = splits[i];
			if(isRndConnection(s)){
					rndConnections[layerIndex-1] = Integer.parseInt(s.substring(1, s.length() - 1));
			}else if (isSeed(s)){
					this.seed = Long.parseLong(s.substring(1, s.length() - 1));
			}else{
				//must be number at this point
				this.layers[layerIndex] = Integer.parseInt(s);
				layerIndex++;
			}
		}
	}

	private void countFeatures(String[] l){
		layersCount = 0;
		rndConnLayersCount = 0;

		for(int i=1; i<l.length; i++){
			String s = l[i];
			if(isRndConnection(s)){
				rndConnLayersCount++;
			}else if (!isSeed(s)){
				layersCount++;
			}else{
				seedsCount++;
			}
			System.out.println(s+" lCount:"+layersCount+" rCount:"+rndConnLayersCount+" sCount:"+seedsCount);
		}
		if(rndConnLayersCount > layersCount-2){
			throw new RuntimeException("Parsing error: "+ Arrays.toString(l));
		}

	}

	private boolean isRndConnection(String s){
		return s.startsWith("(") && s.endsWith(")");
	}

	private boolean isSeed(String s){
		return s.startsWith("[") && s.endsWith("]");
	}

	public static void main(String[] args){
		SparseMultilayerDecoder smd = new SparseMultilayerDecoder("TANH,15,(10),10,1,[2]");
		System.out.println(smd);
	}

	public long getSeed() {
		return seed;
	}

	public int[] getRndConnections() {
		return rndConnections;
	}

	@Override
	public String toString() {
		return "SparseMultilayerDecoder{" +
				"super:" + super.toString()+
				", seed=" + seed +
				", rndConnections=" + Arrays.toString(rndConnections) +
				", layersCount=" + layersCount +
				", rndConnLayersCount=" + rndConnLayersCount +
				", seedsCount=" + seedsCount +
				'}';
	}

//		 mask = new boolean[layers.length-1][layers[0]*layers[1]];

//		BasicNetwork network = new BasicNetwork();
//		for(int nCount : this.layers){
//			network.addLayer(new BasicLayer(function,true,nCount));
//		}
//		network.addLayer(new BasicLayer(new ActivationLinear(),false,1));
//
//		for(int fromLayer=0; fromLayer<layers.length-1; fromLayer++) {
//			int fromLayerNeurons = network.getLayerNeuronCount(fromLayer);
//			int toLayerNeurons = network.getLayerNeuronCount(fromLayer + 1);
//			Random rnd = new Random();
//
//			network.decodeFromArray(new double[network.encodedArrayLength()]);
//
//			for (int fromNeuron = 0; fromNeuron < fromLayerNeurons; fromNeuron++) {
//				for (int toNeuron = 0; toNeuron < toLayerNeurons; toNeuron++) {
//					boolean isEnabled = rnd.nextDouble() > 0.5 ? false : true;
//					network.enableConnection(fromLayer, fromNeuron, toNeuron, isEnabled);
//					if (isEnabled)
//						network.setWeight(fromLayer, fromNeuron, toNeuron, rnd.nextDouble());
//					else
//						network.setWeight(fromLayer, fromNeuron, toNeuron, 0);
//				}
//			}
//
//		}
//		network.getStructure().finalizeStructure();
//
//		return network;
}
