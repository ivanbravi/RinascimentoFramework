package game.state;

import java.util.Arrays;

public class Deck {

	public static final int voidId = -1;

	private int [][]costs;
	private int [] suits;
	private int [] points;
	private int count;
	private int cardSuitCount;

	public Deck(int cards, int cardSuitCount){
		costs = new int[cards][];
		suits = new int[cards];
		points = new int[cards];
		this.cardSuitCount = cardSuitCount;
		this.count = cards;
	}

	public void addCard(int cardId, int suit, int[] costs, int points)throws Exception{
		if(costs.length!=this.cardSuitCount){
			throw new Exception("Invalid cost vector");
		}
		if(cardId>=count){
			throw new Exception("Invalid card id");
		}
		this.suits[cardId] = suit;
		this.costs[cardId] = Arrays.copyOf(costs,costs.length);
		this.points[cardId] = points;
	}


	public int getCardSuit(int cardId) {
		if (!isCardIdValid(cardId))
			return voidId;
		return this.suits[cardId];
	}

	public int[] getCardCost(int cardId){
		if(!isCardIdValid(cardId))
			return null;
		return this.costs[cardId];
	}

	public int getCardPoints(int cardId){
		if(!isCardIdValid(cardId))
			return 0;
		return this.points[cardId];
	}

	public int getCardCount(){
		return count;
	}

	private boolean isCardIdValid(int cardId){
		if(cardId<0 || cardId>= count){
			return false;
		}
		return true;
	}

	public static ShuffledDeck shuffle(Deck d){
		return new ShuffledDeck(d);
	}

	public static boolean canBuy(int[] cost, int[] coins, int[] gems, int gold){

		for(int i=0; i<cost.length; i++){
			int missing = Math.max(0,cost[i]-(coins[i]+gems[i]));
			gold -= missing;
			if(gold<0){
				return false;
			}
		}

		return true;
	}

	public static String cardDescription(int cardId, int suit, int points, int[] cost){
		return "["+cardId+"]"+
				"\n\tsuit: "+suit+
				"\n\tpoints: "+points+
				"\n\tcost: "+Arrays.toString(cost);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for(int i=0; i<this.count; i++){
			builder.append("["+i+"]"+
					"\n\tsuit: "+suits[i]+
					"\n\tpoints: "+points[i]+
					"\n\tcost: "+Arrays.toString(costs[i])+"\n");
		}
		return builder.toString();
	}
}
