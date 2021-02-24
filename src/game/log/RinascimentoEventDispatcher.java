package game.log;

import game.log.converters.EventIdConverter;
import game.log.converters.IdentityConverter;
import game.state.Noble;
import game.state.PlayerState;
import game.state.State;
import log.entities.event.Event;
import log.entities.event.EventLogger;
import log.entities.event.EventLoggerDispatcher;
import log.entities.event.EventSignature;

import java.util.HashMap;

public class RinascimentoEventDispatcher extends RStateEventDispatcher implements EventLoggerDispatcher {

	private HashMap<String, EventLogger> loggers = new HashMap();
	private EventSignature signature = new REventSignature();
	EventIdConverter converter = new IdentityConverter();
	private boolean propagates = false;

	public RinascimentoEventDispatcher(){
	}

	@Override
	public void addLogger(String owner, EventLogger e){
		this.loggers.put(owner,e);
	}

	@Override
	public EventLogger getLogger(String owner) {
		return loggers.get(owner);
	}

	public void setPropagates(boolean propagates) {
		this.propagates = propagates;
	}

	private void logEvent(Event e){
		for(String k : loggers.keySet()){
			loggers.get(k).logEvent(e);
		}
	}

	//region interface

	@Override
	public void nobleTaken(State s, Noble n) {
		logEvent(new Event(
				converter.convertName(0),
				this.when(),
				this.who(),
				converter.convert(0),
				0,
				Event.INSTANT,
				new HashMap<String, Object>(){{put("nobleId", n.getId());}},
				signature,
				this.how()));
	}

	@Override
	public void increaseTokens(State s, int stackId, int amount) {
		logEvent(new Event(
				converter.convertName(1),
				this.when(),
				this.who(),
				converter.convert(1),
				0,
				Event.INSTANT,
				new HashMap<String, Object>(){{
					put("amount", amount);
					put("stackId", stackId);}},
				signature,
				this.how()));
	}

	@Override
	public void decreaseTokens(State s, int stackId, int amount) {
		logEvent(new Event(
				converter.convertName(2),
				this.when(),
				this.who(),
				converter.convert(2),
				0,
				Event.INSTANT,
				new HashMap<String, Object>(){{
					put("amount", amount);
					put("stackId", stackId);}},
				signature,
				this.how()));
	}

	@Override
	public void increaseGold(State s, int amount) {
		logEvent(new Event(
				converter.convertName(3),
				this.when(),
				this.who(),
				converter.convert(3),
				0,
				Event.INSTANT,
				new HashMap<String, Object>(){{
					put("amount", amount);}},
				signature,
				this.how()));
	}

	@Override
	public void decreaseGold(State s, int amount) {
		logEvent(new Event(
				converter.convertName(4),
				this.when(),
				this.who(),
				converter.convert(4),
				0,
				Event.INSTANT,
				new HashMap<String, Object>(){{
					put("amount", amount);}},
				signature,
				this.how()));
	}

	@Override
	public void drawCard(State s, int deckId, int newCardId) {
		logEvent(new Event(
				converter.convertName(5),
				this.when(),
				this.who(),
				converter.convert(5),
				0,
				Event.INSTANT,
				new HashMap<String, Object>(){{
					put("deckId", deckId);
					put("cardId", newCardId);}},
				signature,
				this.how()));
	}

	@Override
	public void fillCard(State s, int deckId, int position, int cardId) {
		logEvent(new Event(
				converter.convertName(6),
				this.when(),
				this.who(),
				converter.convert(6),
				0,
				Event.INSTANT,
				new HashMap<String, Object>(){{
					put("deckId", deckId);
					put("cardId", cardId);
					put("position", position);}},
				signature,
				this.how()));
	}

	@Override
	public void fillNoble(State s, Noble n) {
		logEvent(new Event(
				converter.convertName(7),
				this.when(),
				this.who(),
				converter.convert(7),
				0,
				Event.INSTANT,
				new HashMap<String, Object>(){{put("nobleId", n);}},
				signature,
				this.how()));
	}

	@Override
	public void increasePlayerTokens(State s, PlayerState ps, int stackId, int amount) {
		logEvent(new Event(
				converter.convertName(8),
				this.when(),
				this.who(),
				converter.convert(8),
				0,
				Event.INSTANT,
				new HashMap<String, Object>(){{
					put("amount", amount);
					put("stackId", stackId);}},
				signature,
				this.how()));
	}

	@Override
	public void decreasePlayerTokens(State s, PlayerState ps, int stackId, int amount) {
		logEvent(new Event(
				converter.convertName(9),
				this.when(),
				this.who(),
				converter.convert(9),
				0,
				Event.INSTANT,
				new HashMap<String, Object>(){{
					put("amount", amount);
					put("stackId", stackId);}},
				signature,
				this.how()));
	}

	@Override
	public void increasePlayerGold(State s, PlayerState ps, int amount) {
		logEvent(new Event(
				converter.convertName(10),
				this.when(),
				this.who(),
				converter.convert(10),
				0,
				Event.INSTANT,
				new HashMap<String, Object>(){{
					put("amount", amount);}},
				signature,
				this.how()));
	}

	@Override
	public void decreasePlayerGold(State s, PlayerState ps, int amount) {
		logEvent(new Event(
				converter.convertName(11),
				this.when(),
				this.who(),
				converter.convert(11),
				0,
				Event.INSTANT,
				new HashMap<String, Object>(){{
					put("amount", amount);}},
				signature,
				this.how()));
	}

	@Override
	public void reserveHiddenCard(State s, PlayerState ps, int deckId, int cardId) {
		logEvent(new Event(
				converter.convertName(12),
				this.when(),
				this.who(),
				converter.convert(12),
				0,
				Event.INSTANT,
				new HashMap<String, Object>(){{
					put("deckId", deckId);
					put("cardId", cardId);}},
				signature,
				this.how()));
	}

	@Override
	public void reserveBoardCard(State s, PlayerState ps, int deckId, int cardId) {
		logEvent(new Event(
				converter.convertName(13),
				this.when(),
				this.who(),
				converter.convert(13),
				0,
				Event.INSTANT,
				new HashMap<String, Object>(){{
					put("deckId", deckId);
					put("cardId", cardId);}},
				signature,
				this.how()));
	}

	@Override
	public void receiveNoble(State s, PlayerState ps, int nId) {
		logEvent(new Event(
				converter.convertName(14),
				this.when(),
				this.who(),
				converter.convert(14),
				0,
				Event.INSTANT,
				new HashMap<String, Object>(){{ put("nobleId", nId);}},
				signature,
				this.how()));
	}

	@Override
	public void receiveCardBonus(State s, PlayerState ps, int suitId, int amount) {
		logEvent(new Event(
				converter.convertName(15),
				this.when(),
				this.who(),
				converter.convert(15),
				0,
				Event.INSTANT,
				new HashMap<String, Object>(){{
					put("suitId", suitId);
					put("amount", amount);}},
				signature,
				this.how()));
	}

	@Override
	public void receiveCardPoints(State s, PlayerState ps, int points) {
		logEvent(new Event(
				converter.convertName(16),
				this.when(),
				this.who(),
				converter.convert(16),
				0,
				Event.INSTANT,
				new HashMap<String, Object>(){{
					put("points", points);}},
				signature,
				this.how()));
	}

	@Override
	public void receiveNoblePoints(State s, PlayerState ps, int points) {
		logEvent(new Event(
				converter.convertName(17),
				this.when(),
				this.who(),
				converter.convert(17),
				0,
				Event.INSTANT,
				new HashMap<String, Object>(){{
					put("points", points);}},
				signature,
				this.how()));
	}

	//endregion

	@Override
	public RStateEventDispatcher copy() {
		if(propagates){
			return this;
		}
		RinascimentoEventDispatcher c = new RinascimentoEventDispatcher();
		c.converter = this.converter;
		for(String k : loggers.keySet()){
			c.addLogger(k,loggers.get(k).copy());
		}
		return c;
	}
}

/*
0	nobleTaken
1	increaseTokens
2	decreaseTokens
3	increaseGold
4	decreaseGold
5	drawCard
6	fillCard
7	fillNoble
8	increasePlayerTokens
9	decreasePlayerTokens
10	increasePlayerGold
11	decreasePlayerGold
12	reserveHiddenCard
13	reserveBoardCard
14	receiveNoble
15	receiveCardBonus
16	receiveCardPoints
17	receiveNoblePoints
*/