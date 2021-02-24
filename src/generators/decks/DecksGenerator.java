package generators.decks;

import game.Parameters;
import game.state.Deck;

public interface DecksGenerator {

	Deck[] getDecks(Parameters p);

}
