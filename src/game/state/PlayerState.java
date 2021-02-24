package game.state;

import game.Parameters;
import game.log.DummyStateEventDispatcher;
import game.log.RStateEventDispatcher;

import java.util.ArrayList;
import java.util.Arrays;

public class PlayerState {

	private int playerId;
	private String name;

	public int points;

	public int gold;
	public int[] coins;
	public int[] gems;
	public int[] reservedCardIds;
	public int[] reservedDeckIds;
	public boolean[] reservedIsVisible;
	public ArrayList<Integer> nobles;

	private RStateEventDispatcher eventLog;
	private transient State ownerState;

	private PlayerState(){
		nobles = new ArrayList<>();
	}

	public PlayerState(State ownerState, int id, Parameters params){
		this.ownerState = ownerState;
		playerId = id;
		coins = new int[params.suitCount];
		gems = new int[params.suitCount];
		reservedCardIds = new int[params.maxReserveCards];
		reservedDeckIds = new int[params.maxReserveCards];
		reservedIsVisible = new boolean[params.maxReserveCards];
		nobles = new ArrayList<>();
		Arrays.fill(reservedCardIds,-1);
		Arrays.fill(reservedDeckIds,-1);
//		Arrays.fill(coins,0);
//		Arrays.fill(gems,0);
//		this.gold = 0;
	}

	public PlayerState clone(){
		PlayerState newPS = new PlayerState();

		newPS.ownerState = this.ownerState;
		newPS.playerId = this.playerId;
		newPS.points = this.points;
		newPS.gold = this.gold;
		newPS.coins = Arrays.copyOf(this.coins,this.coins.length);
		newPS.gems= Arrays.copyOf(this.gems,this.gems.length);
		newPS.reservedCardIds = Arrays.copyOf(this.reservedCardIds,this.reservedCardIds.length);
		newPS.reservedDeckIds = Arrays.copyOf(this.reservedDeckIds,this.reservedDeckIds.length);
		newPS.reservedIsVisible = Arrays.copyOf(this.reservedIsVisible,this.reservedIsVisible.length);
		newPS.nobles = (ArrayList<Integer>) nobles.clone();

		return newPS;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();

		builder.append("PLAYER [").append(playerId).append(":").append(name).append("]").append("\n");
		builder.append("\tpoints: ").append(points).append("\n");
		builder.append("\tcoins: ").append(Arrays.toString(coins)).append("[").append(gold).append("]").append("\n");
		builder.append("\tdecks: ").append(Arrays.toString(gems)).append("\n");

		boolean hasReserved = false;
		builder.append("\trsrvd: ");

		for (int i=0; i<reservedCardIds.length; i++){
			if(reservedCardIds[i]!=-1){
				hasReserved = true;
				builder.append(" [").append(reservedDeckIds[i]).append(",")
						.append(reservedCardIds[i]).append("]")
						.append(reservedIsVisible[i]?"":"(H)");
			}
		}

		if(!hasReserved){
			builder.append(" NONE\n");
		}else{
			builder.append("\n");
		}

		builder.append("\tnoble ids:");
		for(Integer nId:nobles){
			builder.append(" ").append(nId);
		}

		if(nobles.isEmpty()){
			builder.append(" NONE\n");
		}else{
			builder.append("\n");
		}

		return builder.toString();
	}

	public void filterReservedCards(){
		for(int i=0; i<reservedIsVisible.length; i++){
			if(!reservedIsVisible[i]){
				reservedCardIds[i] = -1;
				reservedDeckIds[i] = -1;
				reservedIsVisible[i] = true;
			}
		}
	}

	public void setEventLog(RStateEventDispatcher eventLog){
		this.eventLog = eventLog;
	}

	public void detatchEventLogger(){
		this.eventLog = DummyStateEventDispatcher.get();
	}

	public int getCoinsCount(){
		return Arrays.stream(coins).sum()+gold;
	}

	public int getCardsCount(){
		return Arrays.stream(gems).sum();
	}

	public int getId(){
		return playerId;
	}

	public String getName(){return name;}

	public void setName(String name) {
		this.name = name;
	}

	public int getPoints() {
		return points;
	}

	public void decreaseTokens(int stackId, int amount){
		eventLog.decreasePlayerTokens(ownerState,this, stackId,amount);
		this.coins[stackId] -= amount;
	}

	public void increaseTokens(int stackId, int amount){
		eventLog.increasePlayerTokens(ownerState,this, stackId,amount);
		this.coins[stackId] += amount;
	}

	public void decreaseGold(int amount){
		eventLog.decreasePlayerGold(ownerState,this,amount);
		this.gold -= amount;
	}

	public void increaseGold(int amount){
		eventLog.increasePlayerGold(ownerState,this,amount);
		this.gold += amount;
	}

	public void reserveCard(int position, int deckId, int cardId){
		eventLog.reserveBoardCard(ownerState,this,deckId,cardId);
		reservedIsVisible[position] = true;
		this.reservedDeckIds[position] = deckId;
		this.reservedCardIds[position] = cardId;
	}

	public void reserveHiddenCard(int position, int deckId, int cardId){
		eventLog.reserveHiddenCard(ownerState,this,deckId,cardId);
		reservedIsVisible[position] = true;
		this.reservedDeckIds[position] = deckId;
		this.reservedCardIds[position] = cardId;
	}

	public void receiveNoble(int nId){
		eventLog.receiveNoble(ownerState,this, nId);
		this.nobles.add(nId);
	}

	public void receiveBonus(int suitId, int amount){
		eventLog.receiveCardBonus(ownerState,this,suitId,amount);
		this.gems[suitId]+=amount;
	}

	public void receiveCardPoints(int points){
		eventLog.receiveCardPoints(ownerState,this,points);
		this.points += points;
	}

	public void receiveNoblePoints(int points){
		eventLog.receiveNoblePoints(ownerState,this,points);
		this.points+=points;
	}
}
