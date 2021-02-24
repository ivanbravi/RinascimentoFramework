package log.entities.event;

import log.entities.action.Action;

public class StatefulEventLogger {

	private double when;
	private String who;
	private Action how;

	public void begin(double when, String who, Action how){
		this.when = when;
		this.who = who;
		this.how = how;
	}

	public void done(){
		this.when = -1;
		this.who = "Unknown";
		this.how = null;
	}

	public double when(){
		return when;
	}

	public String who() {
		return who;
	}

	public Action how() {
		return how;
	}



}
