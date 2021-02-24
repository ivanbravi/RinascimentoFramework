package log.heuristics.nn.decoder;


import log.heuristics.nn.NNDecoder;

public class PerceptronDecoder extends NNDecoder {

	protected int inputs;
	protected String function;

	public PerceptronDecoder(String encoding){
		super(encoding);
	}

	@Override
	protected void decode(String encoding) {
		String[] splits = encoding.split(",");
		this.function = splits[0];
		this.inputs = Integer.parseInt(splits[1]);
	}

	public int getInputs() {
		return inputs;
	}

	public String getFunction() {
		return function;
	}

}
