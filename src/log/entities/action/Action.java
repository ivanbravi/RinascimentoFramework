package log.entities.action;

import java.util.HashMap;

public class Action {
	private String type;
	private String player;
	private HashMap<String, Object> parameters;

	public Action(String type, String player, HashMap<String, Object> parameters){
		this.type = type;
		this.player = player;
		this.parameters = parameters==null?null:(HashMap<String, Object>) parameters.clone();
	}

	public HashMap<String, Object> parameters() {
		return parameters;
	}

	public String player() {
		return player;
	}

	public String type() {
		return type;
	}
}
