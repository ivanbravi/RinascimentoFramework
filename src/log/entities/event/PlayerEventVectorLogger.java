package log.entities.event;

import game.log.converters.EventIdConverter;

public class PlayerEventVectorLogger extends EventVectorLogger {
	private String player = "";

	public PlayerEventVectorLogger(EventIdConverter converter){
		super(converter);
	}

	private PlayerEventVectorLogger(PlayerEventVectorLogger ref){
		super(ref);
		this.player = ref.player;
	}

	public PlayerEventVectorLogger copy(){
		return new PlayerEventVectorLogger(this);
	}

	public void setPlayer(String player) {
		this.player = player;
	}

	@Override
	public void manageEvent(Event e) {
		if(e.actuator().equals(player))
			super.manageEvent(e);
	}
}
