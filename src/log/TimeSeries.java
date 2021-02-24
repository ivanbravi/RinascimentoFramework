package log;

import java.util.ArrayList;

public class TimeSeries extends Logger{

	ArrayList<Object> data = new ArrayList<>();

	public TimeSeries(String loggerName){
		super(loggerName);
	}

	@Override
	public void logObject(Object o) {
		data.add(o);
	}

	@Override
	Object logContent() {
		return this;
	}

	@Override
	public void jsonReady() {
		for(Object o: data){
			if(o instanceof LoggableReady)
				((LoggableReady)o).jsonReady();
		}
	}

	@Override
	protected void reset() {
		data.clear();
	}

	public static void main(String[] args){
		TimeSeries l = new TimeSeries("time_series_test");
		l.save();
	}

}
