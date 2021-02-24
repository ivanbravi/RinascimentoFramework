package log;

import java.util.HashMap;
import java.util.Stack;

public class LoggingStation {

	private static LoggingStation station;

	private HashMap<String, Stack<Logger>> loggers;

	private LoggingStation(){

	}

	public static LoggingStation getStation(){
		if(station==null)
			station = new LoggingStation();
		return station;
	}

	public void pushLogger(String name, Logger l){
		if(!loggers.containsKey(name)){
			loggers.put(name,new Stack<>());
		}
		loggers.get(name).push(l);
	}

	public Logger peekLogger(String name){
		return loggers.containsKey(name)?loggers.get(name).peek():null;
	}

	public Logger popLogger(String name){
		return loggers.containsKey(name)?loggers.get(name).pop():null;
	}

	public Logger getLogger(String name){
		if(loggers.containsKey(name))
			return loggers.get(name).peek();
		return null;
	}


}
