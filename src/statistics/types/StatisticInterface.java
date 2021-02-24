package statistics.types;

import java.util.ArrayList;

public interface StatisticInterface extends Cloneable{

	void add(StatisticInterface newGameStats);
	double value();
	double error();
	void reset();
	StatisticInterface clone();

	default ArrayList<Double> history(){return null;}
	default StatisticInterface keepHistory(){return this;}
		
}
