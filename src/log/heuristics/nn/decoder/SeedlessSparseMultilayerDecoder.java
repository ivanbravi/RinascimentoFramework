package log.heuristics.nn.decoder;

import java.util.Arrays;

public class SeedlessSparseMultilayerDecoder extends MultilayerNNDecoder{

	private static boolean VERBOSE=false;
	private int layersCount;
	protected int[] rndConnections;
	private int rndConnLayersCount;

	public SeedlessSparseMultilayerDecoder(String encoding) {
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
			}else if (!isSeed(s)){
				//must be number at this point
				this.layers[layerIndex] = Integer.parseInt(s);
				layerIndex++;
			}else{
				if (VERBOSE) System.out.println("Unknown: "+s);
			}
		}
	}

	private boolean isRndConnection(String s){
		return s.startsWith("(") && s.endsWith(")");
	}

	private boolean isSeed(String s){
		return s.equals("[*]");
	}

	public int[] getRndConnections() {
		return rndConnections;
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
			}
			if(VERBOSE) System.out.println(s+" lCount:"+layersCount+" rCount:"+rndConnLayersCount);
		}
		if(rndConnLayersCount > layersCount-2){
			throw new RuntimeException("Parsing error: "+ Arrays.toString(l));
		}
	}

	@Override
	public String toString() {
		return "SeedlessMultilayerDecoder{" +
				"super:" + super.toString() +
				", rndConnections=" + Arrays.toString(rndConnections) +
				", layersCount=" + layersCount +
				", rndConnLayersCount=" + rndConnLayersCount +
				'}';
	}

}
