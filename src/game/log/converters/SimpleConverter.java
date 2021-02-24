package game.log.converters;

import log.entities.event.Event;

public class SimpleConverter implements EventIdConverter {

	@Override
	public int convert(int id) {
		switch (id){
			case 8:
			case 10:
				return 0;
			case 12: return 1;
			case 13: return 2;
			case 14: return 3;
			case 16:
			case 17:
				return 4;
			default: return -1;
		}
	}

	public String convertName(int id) {
		switch (id){
			case 8:
			case 10:
				return "take token";
			case 12: return "reserve hidden";
			case 13: return "reserve board";
			case 14: return "receive noble";
			case 16:
			case 17:
				return "receive points";
			default: return "";
		}
	}

	@Override
	public int idCount() {
		return 5;
	}

	@Override
	public boolean isIncomingCoin(Event e) {
		return e.type()==0;
	}

	@Override
	public boolean isReserve(Event e) {
		return e.type()==1 || e.type()==2;
	}

	@Override
	public boolean isGetCard(Event e) {
		return false;
	}

	@Override
	public boolean isOutGoingCoin(Event e) {
		return false;
	}
}

/*
0	nobleTaken				: -1
1	increaseTokens			: -1
2	decreaseTokens			: -1
3	increaseGold			: -1
4	decreaseGold			: -1
5	drawCard				: -1
6	fillCard				: -1
7	fillNoble				: -1
8	increasePlayerTokens	: 0
9	decreasePlayerTokens	: -1
10	increasePlayerGold		: 0
11	decreasePlayerGold		: -1
12	reserveHiddenCard		: 1
13	reserveBoardCard		: 2
14	receiveNoble			: 3
15	receiveCardBonus		: -1
16	receiveCardPoints		: 4
17	receiveNoblePoints		: 4
*/
