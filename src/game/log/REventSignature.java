package game.log;

import log.entities.event.EventSignature;

public class REventSignature implements EventSignature {
	@Override
	public String[] getArguments() {
		return new String[]{"points","amount","nobleId","stackId", "deckId", "cardId", "position"};
	}
}
