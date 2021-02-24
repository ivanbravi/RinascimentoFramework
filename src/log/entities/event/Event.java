package log.entities.event;

import log.entities.action.Action;

import java.util.HashMap;

public class Event{

	public static final String INSTANT = "Instant";
	public static final String DELAYED = "Delayed";
	public static final String DURATIVE = "Durative";

	private String name;
	private double tick;
	private String actuator;
	private int type;
	private double duration;
	private String durationType;
	private HashMap<String,Object> attributes;
	private EventSignature signature;
	private Action triggeredBy;

	public Event(String name,double tick, String actuator, int type, double duration, String durationType, HashMap<String,Object> attributes, EventSignature signature, Action triggeredBy){
		this.name = name;
		this.tick = tick;
		this.actuator = actuator;
		this.type = type;
		this.duration = duration;
		this.durationType = durationType;
		this.attributes = attributes==null?null:(HashMap<String, Object>) attributes.clone();
		this.signature = signature;
		this.triggeredBy = triggeredBy;
	}

	public Action triggeredBy(){
		return triggeredBy;
	}

	public String name(){
		return name;
	}

	public EventSignature signature(){
		return signature;
	}

	public double duration() {
		return duration;
	}

	public double tick() {
		return tick;
	}

	public int type() {
		return type;
	}

	public HashMap<String,Object> attributes() {
		return attributes;
	}

	public String actuator() {
		return actuator;
	}

	public String durationType() {
		return durationType;
	}

}
