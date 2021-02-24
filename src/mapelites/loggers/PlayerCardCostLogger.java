package mapelites.loggers;

import game.log.converters.EventIdConverter;
import game.log.converters.IdentityConverter;
import log.entities.event.Event;
import log.entities.event.EventLogger;
import statistics.types.NumericalStatistic;

public class PlayerCardCostLogger implements EventLogger {
	private static EventIdConverter converter = new IdentityConverter();

	private String player;
	private NumericalStatistic costs;
	private double tick;
	private int costAcc;

	public PlayerCardCostLogger(String player){
		this.player = player;
		costs = new NumericalStatistic().keepHistory();
	}

	@Override
	public void logEvent(Event e) {
		if(!e.actuator().equals(player))
			return;

		if(e.tick()!=this.tick) {
			costAcc = 0;
			this.tick = e.tick();
		}

		if(converter.isOutGoingCoin(e)){
			costAcc += (Integer) e.attributes().get("amount");
		}else if(converter.isGetCard(e)){
			costs.add(new NumericalStatistic(costAcc));
		}
	}

	public double getCost(){
		return this.costs.value();
	}

	@Override
	public void reset() {
		this.costs.reset();
		this.tick = 0;
		this.costAcc = 0;
	}

	@Override
	public EventLogger copy() {
		PlayerCardCostLogger copy = new PlayerCardCostLogger(this.player);
		copy.costAcc = this.costAcc;
		copy.tick = this.tick;
		copy.costs.copy(this.costs);
		return copy;
	}

	@Override
	public void setMaxEventId(int maxEventId) {}
}
