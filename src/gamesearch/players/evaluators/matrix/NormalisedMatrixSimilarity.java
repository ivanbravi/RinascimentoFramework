package gamesearch.players.evaluators.matrix;

import statistics.types.NumericalStatistic;
import statistics.types.StatisticInterface;
import utils.filters2D.infinity.InfinityHandler;
import utils.filters2D.infinity.NoDataHandler;
import utils.ops2D.Data;

public class NormalisedMatrixSimilarity implements MatrixEvaluator{

	InfinityHandler handler = new NoDataHandler();

	@Override
	public NumericalStatistic evaluateWithStatistics(double[][] a, double[][] b) {
		double e = 0;
		double ceiling = a.length*a[0].length;
		for(int x=0; x<a.length;x++){
			for(int y=0; y<a[x].length;y++){
				double handledA = handler.handle(a[x][y]);
				double handledB = handler.handle(b[x][y]);
				e += Math.abs(handledA-handledB);
			}
		}
		return new NumericalStatistic(1-e/ceiling);
	}


	public static void main(String[] args) {
		String diff = "\t[Supposed to be different between 5x5 and 10x10]";
		NormalisedMatrixSimilarity evaluator = new NormalisedMatrixSimilarity();

		System.out.println("--------- [ 5 x 5 ] ---------");
		System.out.println("| square , square | = "+evaluator.evaluate(Data.square5x5,Data.square5x5));
		System.out.println("| square , rect   | = "+evaluator.evaluate(Data.square5x5,  Data.rect5x5));
		System.out.println("| square , zeros  | = "+evaluator.evaluate(Data.square5x5, Data.zeros5x5));
		System.out.println("|   rect , zeros  | = "+evaluator.evaluate(  Data.rect5x5, Data.zeros5x5));
		System.out.println("|   rect , inf    | = "+evaluator.evaluate(  Data.rect5x5,   Data.inf5x5));
		System.out.println("|   rect , oneinf | = "+evaluator.evaluate(  Data.rect5x5,Data.oneInf5x5));
		System.out.println("|   rect , sqInf  | = "+evaluator.evaluate(  Data.rect5x5, Data.sqInf5x5)+diff);

		System.out.println("-------- [ 10 x 10 ] --------");
		System.out.println("| square , square | = "+evaluator.evaluate(Data.square10x10,Data.square10x10));
		System.out.println("| square , rect   | = "+evaluator.evaluate(Data.square10x10,  Data.rect10x10));
		System.out.println("| square , zeros  | = "+evaluator.evaluate(Data.square10x10, Data.zeros10x10));
		System.out.println("|   rect , zeros  | = "+evaluator.evaluate(  Data.rect10x10, Data.zeros10x10));
		System.out.println("|   rect , inf    | = "+evaluator.evaluate(  Data.rect10x10,   Data.inf10x10));
		System.out.println("|   rect , oneinf | = "+evaluator.evaluate(  Data.rect10x10,Data.oneInf10x10));
		System.out.println("|   rect , sqInf  | = "+evaluator.evaluate(  Data.rect10x10, Data.sqInf10x10)+diff);

		System.out.println("-----------------------------");
	}
}
