package utils;


import game.Engine;
import game.Factory;
import game.Parameters;
import game.exceptions.StaleGameException;
import game.state.Result;
import game.state.State;

import java.util.ArrayList;

public class TestSpeed {

	private static boolean PRINT_INTERIM = false;

	public static void main(String[] args){
		Parameters p = Parameters.defaultParameters();
		Engine e = Factory.createEngine(p,null);

		ArrayList<Double> avgSpeed= new ArrayList<>();

		long start,end;
		double elapsedMS;
		int totalStates = 0;
		long totalElapsed = 0;

		int gameSimulations = 100000;
		StaleGameException.doSaveState = false;

		int staleCounter = 0;

		for(int gameCounter=1;gameCounter<=gameSimulations; gameCounter++) {
			Result r = null;
			State s = Factory.createState(p, e);

			start = System.nanoTime();

			printInterim("Playing game #"+gameCounter);
			try {
				while (!s.isGameOver()) {
					r = e.stepPlay(s);
				}

			}catch (StaleGameException ex){
				ex.printStackTrace();
			}

			if (r.s== Result.STATE.STALE) {
				staleCounter++;
				//printInterim(s.toString());
			}
			end=System.nanoTime();
			elapsedMS = (end-start)/1000000.0;

			totalElapsed += elapsedMS;
			totalStates += s.getTick();

			avgSpeed.add(elapsedMS);

			printInterim("Game #"+gameCounter+" ["+r.s+"] in "+s.getTick());
			printInterim("\t\ttime: "+elapsedMS+" ms");
		}

		double avgSpeedMS = 0;
		for(Double speed: avgSpeed){
			avgSpeedMS+=speed;
		}
		avgSpeedMS = avgSpeedMS/gameSimulations;

		System.out.println("Stale games: "+staleCounter+"/"+gameSimulations);
		System.out.println("Speed: "+((double)totalStates)/totalElapsed+"[states/ms]");
		System.out.println("Avg Speed: "+avgSpeedMS+"[ms]");

	}

	private static void printInterim(String message){
		if(!PRINT_INTERIM)
			return;
		System.out.println(message);
	}

}
