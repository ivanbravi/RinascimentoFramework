package game.state.vectorise;

import game.state.State;

import java.util.List;

public interface Vectoriser {

	class Params{
		public static boolean RESCALE = true;
		public static int COST_SCALE=10;
	}

	double[] vectorise(State s,int playerID);
	int size();
	Vectoriser clone();

	default double[] toPrimitive(List<Double> f){
		double[] ff = new double[f.size()];
		for(int i=0; i<f.size(); i++)
			ff[i] = f.get(i);
		return ff;
	}

	default void arraycopy(int[] source, int fromSource, double[] destination, int fromDestination, int values, double rescale){
		double scale = rescale(rescale);
		for(int i=0; i<values; i++)
			destination[fromDestination+i] = source[fromSource+i] / scale;
	}

	default void arraycopy(double[] source, int fromSource, double[] destination, int fromDestination, int values, double rescale){
		double scale = rescale(rescale);
		for(int i=0; i<values; i++)
			destination[fromDestination+i] = source[fromSource+i] / scale;
	}

	default double rescale(double factor){
		return Params.RESCALE ? factor: 1;
	}
}
