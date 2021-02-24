package game.state;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;


public class Result {

	public int[] position;
	public int[] points;
	public int[] cardsCount;

	public boolean isGameOver = false;
	public int ticks;

	public STATE s;

	public enum STATE{
		RUNNING,OVER,STALE
	}

	public Comparator<Integer> comparator = new Comparator<Integer>() {
		@Override
		public int compare(Integer o1, Integer o2) {

			if(points[o1]==points[o2]){
				return cardsCount[o1]-cardsCount[o2];
			}else{
				return -(points[o1]-points[o2]);
			}
		}
	};

	public Result(State s){
		if(s.isStale()){
			staleGameInit(s);
		}else {
			nonStaleInit(s);
		}

	}

	private void initAttributes(State s){
		position = new int[s.params.playerCount];
		points = new int[s.params.playerCount];
		cardsCount = new int[s.params.playerCount];
	}

	private void nonStaleInit(State s){
		initAttributes(s);
		ticks = s.getTick();
		fillStats(s);
		orderPlayers();
		if(s.isGameOver()){
			this.s = STATE.OVER;
		}else{
			this.s = STATE.RUNNING;
		}
	}

	private void staleGameInit(State gs){
		initAttributes(gs);
		fillStats(gs);
		s = STATE.STALE;
		ticks = gs.getTick();
		isGameOver = true;
		Arrays.fill(position,-1);
	}

	private void fillStats(State s){
		for(int playerId=0; playerId<s.params.playerCount; playerId++){
			PlayerState plState = s.playerStates[playerId];
			points[playerId] = plState.points;
			isGameOver = isGameOver | (plState.points>=s.params.endGameScore);
			cardsCount[playerId] = plState.getCardsCount();
		}
	}


	private void orderPlayers(){
		ArrayList<Integer> playerPosition = new ArrayList<>();
		for(int i=0; i<points.length; i++){
			playerPosition.add(i);
		}

		//order indices by the rules specified in the comparator
		Collections.sort(playerPosition,comparator);

		int stepPosition = 1;
		int prevPlayerId = playerPosition.get(0);

		position[prevPlayerId] = 1;

		for(int index=1; index<playerPosition.size(); index++){
			int playerId = playerPosition.get(index);
			if(comparator.compare(prevPlayerId,playerId)!=0){
				prevPlayerId = playerId;
				stepPosition = index+1;
			}
			position[playerId] = stepPosition;
		}
	}

	@Override
	public String toString() {
		return "["+ticks+"] "+s.toString()+"\n"+
				"scores: "+ Arrays.toString(this.points)+"\n"+
				"positions: "+ Arrays.toString(this.position)+"\n"+
				"decks counters: "+ Arrays.toString(this.cardsCount)+"\n";
	}
}
