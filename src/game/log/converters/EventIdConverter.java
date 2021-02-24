package game.log.converters;

import log.entities.event.Event;

public interface EventIdConverter {
	int convert(int id);
	String convertName(int id);
	int idCount();

	boolean isIncomingCoin(Event e);
	boolean isReserve(Event e);
	boolean isGetCard(Event e);
	boolean isOutGoingCoin(Event e);



}


/*
public int convert(int id) {

		switch (id){
			case 0:  return 0;
			case 1:  return 0;
			case 2:  return 0;
			case 3:  return 0;
			case 4:  return 0;
			case 5:  return 0;
			case 6:  return 0;
			case 7:  return 0;
			case 8:  return 0;
			case 9:  return 0;
			case 10: return 0;
			case 11: return 0;
			case 12: return 0;
			case 13: return 0;
			case 14: return 0;
			case 15: return 0;
			case 16: return 0;
			case 17: return 0;
		}

		return -1;
	}
*/