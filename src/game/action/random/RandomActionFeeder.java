package game.action.random;

import game.exceptions.StaleGameException;
import game.action.PlayableAction;
import game.state.State;
import utils.RandomBased;
import utils.WeightedRandomSelection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class RandomActionFeeder extends RandomBased {

	ArrayList<RandomActionGenerator> generators;
	WeightedRandomSelection weightedRnd;

	public RandomActionFeeder(){
		generators = new ArrayList<>();
		weightedRnd = new WeightedRandomSelection();
	}

	public void addRandomActionGenerator(RandomActionGenerator generator, double weight){
		generators.add(generator);
		generator.setWeight(weight);
	}

	@Override
	public RandomActionFeeder clone(){
		RandomActionFeeder clone =  new RandomActionFeeder();
		for(RandomActionGenerator rag: generators)
			clone.generators.add(rag.clone());
		clone.weightedRnd = weightedRnd.clone();
		return clone;
	}

	@Override
	public String toString() {
		String[] gens = new String[generators.size()];
		for (int i=0; i<gens.length; i++)
			gens[i] = generators.get(i).toString()+"("+generators.get(i).weight()+")";
		return Arrays.toString(gens).replaceAll("\\[|\\]|,","");
	}

	@Override
	public void setRandomSource(Random rnd) {
		super.setRandomSource(rnd);
		weightedRnd.setRandomSource(rnd);
		for(RandomActionGenerator rag:generators)
			rag.setRandomSource(rnd);
	}

	public PlayableAction getAction(State s, int playerId){
		PlayableAction action = null;
		ArrayList<RandomActionGenerator> localGenerators = (ArrayList<RandomActionGenerator>) generators.clone();
		while (action==null && !localGenerators.isEmpty()) {
			RandomActionGenerator generator = (RandomActionGenerator) weightedRnd.next(localGenerators);
			action = generator.generate(s,playerId);
			localGenerators.remove(generator);
		}

		if(localGenerators.isEmpty() && action==null){
			throw new StaleGameException(s);
		}

		return action;
	}

}
