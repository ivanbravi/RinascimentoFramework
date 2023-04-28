package gamesearch.players.samplers.sampling;

public class LinearSampling extends Sampling{
	@Override
	protected int getIndex(double range, int max) {
		return (int) Math.round(range*(max-1));
	}
}
