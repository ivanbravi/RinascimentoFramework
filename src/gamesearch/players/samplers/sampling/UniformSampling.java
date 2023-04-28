package gamesearch.players.samplers.sampling;

import java.util.Random;

public class UniformSampling extends Sampling{

	Random rnd;

	public UniformSampling(){
		rnd = new Random();
	}

	@Override
	protected int getIndex(double range, int max) {
		rnd.setSeed((long) (Long.MAX_VALUE*range));
		return rnd.nextInt(max);
	}
}
