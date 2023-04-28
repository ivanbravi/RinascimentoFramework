package utils.profiling;

import time.MilliTimer;
import time.Timer;

import java.util.HashMap;

public class CountersProfiler extends Profiler{
	public static final String ADVANCE = "State advance";
	public static final String COPY = "State copy";
	public static final String ACTION = "Action performed";

	private HashMap<String, Double> counters = new HashMap<>();
	private boolean isProfiling = false;
	private Timer timer = new MilliTimer();

	public CountersProfiler start(){
		isProfiling =true;
		timer.start();
		return this;
	}

	public CountersProfiler stop(){
		isProfiling =false;
		timer.stop();
		return this;
	}

	@Override
	public void stateEngineAdvanced(){
		updateCounter(ADVANCE);
	}

	@Override
	public void stateCopy(){
		updateCounter(COPY);
	}

	@Override
	public void actionPerformed() { updateCounter(ACTION); }

	synchronized private void updateCounter(String key){
		if(isProfiling) {
			if (counters.containsKey(key))
				counters.put(key, counters.get(key)+1);
			else
				counters.put(key, 1.0);
		}
	}

	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		double time = timer.seconds();
		b.append("time:"+time+" seconds\n");
		for(String k: counters.keySet())
			b.append(k).append(" : ").append(counters.get(k)).append("\n");
		for(String k: counters.keySet())
			b.append(k).append(" per second : ").append(counters.get(k)/time).append("\n");
		return b.toString();
	}
}
