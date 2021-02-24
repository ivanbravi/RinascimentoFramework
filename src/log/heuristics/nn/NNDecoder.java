package log.heuristics.nn;

public abstract class NNDecoder {

	public NNDecoder(String encoding){
		decode(encoding);
	}

	protected abstract void decode(String encode);

}
