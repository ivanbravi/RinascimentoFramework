package game.log.converters;

import log.entities.event.Event;

public class IdentityConverter implements EventIdConverter {

	public final int count = 18;
	public final String[] names = new String[]{"nobleTaken","increaseTokens",
			"decreaseTokens","increaseGold","decreaseGold","drawCard","fillCard",
			"fillNoble","increasePlayerTokens","decreasePlayerTokens",
			"increasePlayerGold","decreasePlayerGold","reserveHiddenCard",
			"reserveBoardCard","receiveNoble","receiveCardBonus","receiveCardPoints",
			"receiveNoblePoints"};

	@Override
	public int convert(int id) {
		return id;
	}

	@Override
	public String convertName(int id) {
		if(id<0 || id>=count)
			return "";
		return names[id];
	}

	@Override
	public int idCount() {
		return count;
	}

	@Override
	public boolean isIncomingCoin(Event e) {
		return e.type()==8 || e.type()==10;
	}

	@Override
	public boolean isReserve(Event e) {
		return e.type()==12 || e.type()==13;
	}

	@Override
	public boolean isGetCard(Event e) {
		return e.type()==15;
	}

	@Override
	public boolean isOutGoingCoin(Event e) {
		return e.type()==9 || e.type()==11;
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

