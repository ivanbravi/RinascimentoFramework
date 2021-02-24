package log.heuristics.nn.decoder;

import log.heuristics.nn.NNDecoder;

import java.util.Arrays;

public class MultilayerNNDecoder extends NNDecoder {

	protected int[] layers;
	protected String function;

	public MultilayerNNDecoder(String encoding) {
		super(encoding);
	}

	@Override
	protected void decode(String encoding){
		String[] splits = encoding.split(",");
		function = splits[0];
		layers = new int[splits.length-1];

		for(int i=1; i<splits.length; i++)
			layers[i-1] = Integer.parseInt(splits[i]);
	}

	public int[] getLayers() {
		return layers;
	}

	public String getFunction() {
		return function;
	}

	@Override
	public String toString() {
		return "MultilayerNNDecoder{" +
				"layers=" + Arrays.toString(layers) +
				", function='" + function + '\'' +
				'}';
	}

}
