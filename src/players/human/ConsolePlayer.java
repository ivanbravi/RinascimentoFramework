package players.human;

import game.BudgetExtendedGameState;
import game.Parameters;
import game.action.Action;
import game.action.PlayableAction;
import game.action.active.buy.board.BuyBoardCard;
import game.action.active.buy.reserved.BuyReservedCard;
import game.action.active.pick.different.PickDifferentCoins;
import game.action.active.pick.same.PickSameCoins;
import game.action.active.reserve.board.ReserveBoardCard;
import game.action.active.reserve.deck.ReserveDeckCard;
import game.state.State;
import players.BasePlayerInterface;
import players.ai.explicit.ExplicitPlayerInterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ConsolePlayer implements ExplicitPlayerInterface {

	private double myScore;
	private int id;
	private Parameters params;
	private BufferedReader console;

	String name = "Human Console";

	public ConsolePlayer(){
		InputStreamReader streamReader = new InputStreamReader(System.in);
		console = new BufferedReader(streamReader);
	}

	public Action[] getActions(BudgetExtendedGameState gameState, int playerId) {
		boolean isActionOk = false;
		PlayableAction pa = null;
		State gs = gameState.getState();
		State testState = (State) gs.copy();

		params = gs.params;
		myScore = gs.getScore()[playerId];
		id = playerId;

		while (!isActionOk) {
			pa = readAction();
			isActionOk = pa.canPerform(testState);
		}

		return new PlayableAction[]{pa};
	}

	private PlayableAction readAction(){
		int actionSelection;
		PlayableAction action = null;
		while (action==null) {
			actionSelection=-1;
			while (actionSelection == -1) {
				System.out.println("(PLAYER "+id+")");
				System.out.println("\tSelect action type: ");
				System.out.println("\t1 Buy card");
				System.out.println("\t2 Buy reserved card");
				System.out.println("\t3 Reserve card");
				System.out.println("\t4 Reserve deck card");
				System.out.println("\t5 Pick 3 different coins");
				System.out.println("\t6 Pick 2 same coins");
				try {
					String reply = console.readLine();
					actionSelection = Integer.parseInt(reply);

					if (actionSelection <= 0 || actionSelection >6) {
						actionSelection = -1;
					}
				}catch (NumberFormatException e){
					System.out.println("Invalid selection");
				}catch (IOException e){
					e.printStackTrace();
				}
			}


			try {
				switch (actionSelection) {
					case 1: { //	1 Buy card
						action = buyCard();
					}break;
					case 2: { //	2 Buy reserved card
						action = buyReservedCard();
					}break;
					case 3: { //	3 Reserve card
						action = reserveCard();
					}break;
					case 4: { //	4 Reserve deck card
						action = reserveDeckCard();
					}break;
					case 5: { //	5 Pick 3 different coins
						action = pickDifferent();
					}break;
					case 6: { //	6 Pick 2 same coins
						action = pickSame();
					}break;
				}
			}catch(Exception e) {
				e.printStackTrace();
			}
		}

		return action;
	}


	private PlayableAction buyCard(){
		int deck=-1, position=-1;

		while(deck==-1 || position==-1){
			System.out.println("BUY BOARD CARD");
			printAvailableDeck();
			printBoardPositions();

			System.out.println("\t(Format: <deck>,<position> "+outInstruction()+")");

			try {
				String input = console.readLine();

				if(isOut(input)){
					return null;
				}

				String[] split = input.split(",");
				if(split.length==2){
					try {
						deck = Integer.parseInt(split[0]);
						position = Integer.parseInt(split[1]);
					}catch (NumberFormatException e){
						System.out.println("Wrong format!");
						deck=-1;
						position=-1;
					}
				}

				if(isDeckPositionInvalid(deck)){
					deck = -1;
				}
				if(isBoardPositionInvalid(position)){
					position=-1;
				}
			}catch (IOException e){
				e.printStackTrace();
			}

		}

		return new BuyBoardCard(this.id,deck-1,position-1);
	}

	private PlayableAction buyReservedCard(){
		int position=-1;

		while(position==-1){
			System.out.println("BUY RESERVED CARD");

			printReservedPosition();
			System.out.println("\t(Format: <reserved position> "+outInstruction()+")");

			try{
				String input = console.readLine();

				if(isOut(input)){
					return null;
				}

				try {
					position = Integer.parseInt(input);
				}catch (NumberFormatException e){
					System.out.println("Wrong format!");
					position=-1;
				}

				if(isReservedPositionInvalid(position)){
					position=-1;
				}
			}catch (IOException e){
				e.printStackTrace();
			}

		}
		return new BuyReservedCard(this.id,position-1);
	}

	private PlayableAction reserveCard(){

		int deck=-1, position=-1;

		while(deck==-1 || position==-1){
			System.out.println("RESERVE BOARD CARD");

			printAvailableDeck();
			printBoardPositions();

			System.out.println("\t(Format: <deck>,<position> "+outInstruction()+")");

			try {

				String input = console.readLine();

				if(isOut(input)){
					return null;
				}

				String[] split = input.split(",");
				if(split.length==2){
					try {
						deck = Integer.parseInt(split[0]);
						position = Integer.parseInt(split[1]);
					}catch (NumberFormatException e){
						System.out.println("Wrong format!");
						deck=-1;
						position=-1;
					}
				}

				if(isDeckPositionInvalid(deck)){
					deck = -1;
				}
				if(isBoardPositionInvalid(position)){
					position=-1;
				}

			}catch (IOException e){
				e.printStackTrace();
			}
		}

		return new ReserveBoardCard(this.id,deck-1,position-1);
	}

	private PlayableAction reserveDeckCard(){
		int deck=-1;

		while(deck==-1){
			System.out.println("RESERVE DECK CARD");

			printAvailableDeck();
			System.out.println("\t(Format: <deck> "+outInstruction()+")");

			try {
				String input = console.readLine();

				if(isOut(input)){
					return null;
				}

				try {
					deck = Integer.parseInt(input);
				}catch (NumberFormatException e){
					System.out.println("Wrong format!");
					deck=-1;
				}
				if(isDeckPositionInvalid(deck)){
					deck=-1;
				}
			}catch (IOException e){
				e.printStackTrace();
			}

		}
		return new ReserveDeckCard(this.id,deck-1);
	}

	private PlayableAction pickDifferent(){
		boolean isPickOk = false;
		int[] picks = new int[params.suitCount];

		while(!isPickOk){
			System.out.println("PICK DIFFERENT COINS");

			printPickInstructions();

			try {
				String input = console.readLine();

				if(isOut(input)){
					return null;
				}
				isPickOk = readPick(input,picks);
			}catch (IOException e){
				e.printStackTrace();
			}
		}

		return new PickDifferentCoins(this.id,picks);
	}

	private void printPickInstructions(){
		System.out.print("\t (Format: ");
		for(int i=1; i<=params.suitCount; i++){
			System.out.print("<amount"+i+">");
			if(i<params.suitCount){
				System.out.print(",");
			}
		}
		System.out.print(" "+outInstruction()+")\n");
	}

	private boolean readPick(String input, int[] picks){

		try {
			String[] amounts = input.split(",");
			if(amounts.length!=params.suitCount){
				throw new NumberFormatException();
			}

			for (int i=0; i<amounts.length; i++){
				picks[i] = Integer.parseInt(amounts[i]);
			}

			return true;
		}catch (NumberFormatException e){
			System.out.println("Wrong format!");
		}
		return false;
	}


	private PlayableAction pickSame(){
		boolean isPickOk = false;
		int[] picks = new int[params.suitCount];

		while(!isPickOk){
			System.out.println("PICK SAME COINS");

			printPickInstructions();

			try{
				String input = console.readLine();

				if(isOut(input)){
					return null;
				}
				isPickOk = readPick(input,picks);
			}catch (IOException e){
				e.printStackTrace();
			}
		}

		return new PickSameCoins(this.id,picks);
	}

	@Override
	public BasePlayerInterface reset() {
		myScore = 0;
		params = null;
		this.id=-1;
		return this;
	}

	@Override
	public void setId(int id) {
		this.id = id;
	}

	@Override
	public double utility() {
		return myScore;
	}

	@Override
	public BasePlayerInterface clone() {
		ConsolePlayer clone = new ConsolePlayer();

		clone.myScore = this.myScore;
		clone.id = this.id;
		clone.params = this.params;
		clone.console = this.console;
		clone.name = this.name;

		return clone;
	}

	private boolean isOut(String input){
		return input.equals("b");
	}

	private void printAvailableDeck(){
		System.out.print("Select deck: { ");
		for(int i=1; i<=params.deckCount; i++){
			System.out.print(i+" ");
		}
		System.out.print("}\n");
	}

	private void printBoardPositions(){
		System.out.print("Select card: { ");
		for(int i=1; i<=params.cardsOnTableCount; i++){
			System.out.print(i+" ");
		}
		System.out.print("}\n");
	}

	private void printReservedPosition(){
		System.out.print("Select reserved card: { ");
		for(int i=1; i<=params.maxReserveCards; i++){
			System.out.print(i+" ");
		}
		System.out.print("}\n");
	}

	private String outInstruction(){
		return "or type 'b' to go back";
	}

	private boolean isDeckPositionInvalid(int deckId){
		boolean notValid = deckId<1 || deckId>params.deckCount;
		if(notValid)
			System.out.println("Invalid deck id");
		return notValid;
	}

	private boolean isBoardPositionInvalid(int position){
		boolean notValid = position<1 || position>params.cardsOnTableCount;
		if(notValid)
			System.out.println("Invalid card board position");
		return notValid;
	}

	private boolean isReservedPositionInvalid(int position){
		boolean notValid = position<1 || position>params.maxReserveCards;
		if(notValid)
			System.out.println("Invalid reserved card position");
		return notValid;
	}

	@Override
	public void setName(String name){
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	public static void main(String[] args){
		PlayableAction pa;
		ConsolePlayer p = new ConsolePlayer();
		p.params = Parameters.defaultParameters();
		while(true){
			pa = p.readAction();
			System.out.println(pa.toString());
		}
	}
}
