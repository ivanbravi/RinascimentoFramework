package log.entities.event;


import game.log.converters.EventIdConverter;

import java.util.HashMap;
import java.util.NoSuchElementException;

public class EventVectorLogger extends EventConvertedLogger {

	private HashMap<Integer, Integer> counters = new HashMap<>();
	private int maxEventId;

	public EventVectorLogger(EventIdConverter converter){
		super(converter);
		this.setMaxEventId(converter.idCount());
	}

	protected EventVectorLogger(EventVectorLogger ref){
		super(ref.converter);
		this.counters.putAll(ref.counters);
		this.setMaxEventId(ref.converter.idCount());
	}

	public void setMaxEventId(int maxEventId) {
		if(maxEventId>0)
			this.maxEventId = maxEventId;
		else
			throw new RuntimeException("Max event id MUST BE positive.");
	}


	@Override
	public void manageEvent(Event e) {
		int counter = 0;
		if(counters.containsKey(e.type())){
			counter = counters.get(e.type());
		}
		counters.put(e.type(),counter+1);
	}

	@Override
	public void reset() {
		counters.clear();
	}

	@Override
	public EventLogger copy() {
		EventVectorLogger newLogger = new EventVectorLogger(converter);
		newLogger.counters.putAll(this.counters);
		return newLogger;
	}

	public double[] vector(){
		int vectorSize = maxEventId;

		if (vectorSize <= 0) {
			vectorSize = counters.keySet().stream().max(Integer::compare).get().intValue();
		}

		double[] v = new double[vectorSize];

		for(Integer k: counters.keySet())
			if(k < vectorSize && k>=0)
				v[k]=counters.get(k);

		return v;
	}

}
