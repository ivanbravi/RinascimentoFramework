package gamesearch;

import statistics.types.NumericalStatistic;

public class EvaluationLogEntry {

	public NumericalStatistic phenotypicFitness;
	public double genotypicFitness;
	public NumericalStatistic fitness;

	public EvaluationLogEntry(NumericalStatistic phenotypicFitness, double genotypicFitness, NumericalStatistic fitness
	){
		this.phenotypicFitness = phenotypicFitness;
		this.genotypicFitness = genotypicFitness;
		this.fitness = fitness;
	}
}
