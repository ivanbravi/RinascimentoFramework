package mapelites;

import mapelites.interfaces.Solution;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class BehavioursFunction implements mapelites.interfaces.BehavioursFunction {

	private int size;

	private Solution lastEvaluated;
	private double[] behaviours;
	private double[] unknownBehaviour;

	public BehavioursFunction(int size){
		this.size = size;
		unknownBehaviour = new double[size];
		Arrays.fill(unknownBehaviour,0.0);
	}

	public void updateLastBehavioursTested(Solution s, double[] bs){
		if(bs.length != size)
			throw new RuntimeException("Incorrect behaviour vector size");
		this.behaviours = Arrays.copyOf(bs,bs.length);
		this.lastEvaluated = s;
	}

	@NotNull
	@Override
	public double[] evaluate(@NotNull Solution solution) {
		if(!solution.equals(lastEvaluated))
			return unknownBehaviour;
		return behaviours;
	}


}
