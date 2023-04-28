package game;

import game.action.Action;
import game.action.ActionsEncoderDecoder;
import game.action.PlayableAction;
import game.action.StatelessDecEnc;
import game.action.active.buy.board.ATBuyBoardCard;
import game.action.active.buy.board.RandomBuyBoardCard;
import game.action.active.buy.reserved.ATBuyReservedCard;
import game.action.active.buy.reserved.RandomBuyReservedCard;
import game.action.active.pick.different.ATPickDifferentCoinsGeneric;
import game.action.active.pick.different.RandomPickDifferentCoins;
import game.action.active.pick.same.ATPickSameCoinsGeneric;
import game.action.active.pick.same.RandomPickSameCoins;
import game.action.active.reserve.board.ATReserveBoardCard;
import game.action.active.reserve.board.RandomReserveBoardCard;
import game.action.active.reserve.deck.ATReserveDeckCard;
import game.action.active.reserve.deck.RandomReserveDeckCard;
import game.action.passive.PassiveActionInterface;
import game.action.passive.TakeNoble;
import game.action.random.RandomActionFeeder;
import game.budget.ActionsBudget;
import game.budget.Budget;
import game.exceptions.StaleGameException;
import game.exceptions.TimedStaleGameException;
import game.log.RStateEventDispatcher;
import game.state.PlayerState;
import game.state.Result;
import game.state.State;
import players.GeneralPlayerInterface;
import players.BasePlayerInterface;
import players.ai.explicit.ExplicitPlayerInterface;
import utils.profiling.Profiler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Engine {

	public static boolean shufflePlayers = true;
	public static boolean detachLogger = true;
	private static Profiler profiler = new Profiler();

	private ArrayList<BasePlayerInterface> players = new ArrayList<>();

	private ActionsEncoderDecoder actionEncoder;
	private ActionsEncoderDecoder actionDecoder;

	private ArrayList<PassiveActionInterface> passiveActions = new ArrayList<>();

	private Budget budget = new ActionsBudget(1000);
	private Random globalRnd = new Random();
	private long seed;
	private RandomActionFeeder rndActCrt;

	public Engine(BasePlayerInterface... players){
		setPlayers(players);
	}

	public static void attatchProfiler(Profiler p){
		profiler = p;
		State.attatchProfiler(p);
		PlayableAction.attatchProfiler(p);
	}

	@Override
	public String toString() {
		return  "[Random Action Feeder]\n"+rndActCrt.toString()+"\n";//+"[Action Encoder Decoder]\n"+actionDecoder.toString();
	}

	@Override
	public Engine clone(){
		Engine clone = new Engine();
		clone.setBudget(budget.copy());
		clone.setSeed(seed);
		clone.rndActCrt =  rndActCrt.clone();

		for(int i=0; i<passiveActions.size(); i++){
			PassiveActionInterface pai = passiveActions.get(i);
			clone.passiveActions.add(pai.clone());
		}

		//TODO: clone encoders/decoders when supported
//		clone.actionEncoder = actionEncoder.clone();
//		clone.actionDecoder = actionDecoder.clone();

		return clone;
	}

	public void setPlayers(BasePlayerInterface[] players){
		this.players.clear();
		for(int i=0; i<players.length; i++){
			this.players.add(players[i]);
			players[i].reset();
			players[i].setId(i);
		}
	}

	public int getPlayerId(BasePlayerInterface player){
		for(int pId=0; pId<players.size(); pId++){
			if(players.get(pId)==player)
				return pId;
		}
		return -1;
	}

	public Engine setSeed(long seed){
		this.seed = seed;
		globalRnd.setSeed(seed);
		return this;
	}

	public Engine setActionSpaceEncoder(ActionsEncoderDecoder encDec){
		this.actionDecoder = encDec;
		this.actionEncoder= encDec;
		return this;
	}

	public Engine setBudget(Budget budget){
		this.budget = budget;
		return this;
	}

	public Engine setRandomActionFeeder(RandomActionFeeder rnd){
		this.rndActCrt = rnd;
		return this;
	}

	public void addPassiveAction(PassiveActionInterface action){
		this.passiveActions.add(action);
	}

	public int getPlayerActionSpace(State s, GeneralPlayerInterface player){
		return actionEncoder.countActions(s,getPlayerId(player));
	}

	private int getPlayerId(GeneralPlayerInterface player){
		return this.players.indexOf(player);
	}

	public int getPlayerId(String playerName){
		for(int i=0; i<players.size(); i++)
			if(players.get(i).getName().equals(playerName))
				return i;
		return -1;
	}

	public double[] getScore(State s){
		double[] scores = new double[players.size()];

		for(int i=0;i<scores.length;i++){
			scores[i] = s.getPlayerState(i).getPoints();
		}

		return scores;
	}

	public String[] getPlayerNames(){
		String[] names = new String[players.size()];
		for (int i=0; i<names.length; i++){
			String name = players.get(i).getName();
			if( name==null){
				name = "";
			}
			names[i] = name;
		}
		return names;
	}

	public Result stepPlay(State state){

		if(!state.isInitialised()) {
			decidePlayersOrder();
			state.initGame();
		}

		state.setNames(this.getPlayerNames());

		if(state.getTick() == TimedStaleGameException.timer){
			state.gameOver();
			state.stale();
			return new Result(state);
		}

		int playerId = state.getTick()%state.params.playerCount;
		BasePlayerInterface curr_player = players.get(playerId);
		BudgetExtendedGameState budgetState = new BudgetExtendedGameState(state.copyForPlayer(playerId),budget.copy());

		try {
			state.getRandomAction(playerId);
		}catch (StaleGameException e){
			state.gameOver();
			state.stale();
			return new Result(state);
		}

		RStateEventDispatcher logger = state.eventDispatcher();

		if(curr_player instanceof ExplicitPlayerInterface){
			ExplicitPlayerInterface cons = (ExplicitPlayerInterface) curr_player;
			if(detachLogger)
				state.detatchLogger();
			Action[]pas = cons.getActions(budgetState,playerId);
			if(detachLogger)
				state.setEventDispatcher(logger);
			this.perform(state,convertActions(pas),playerId);
		} else if(curr_player instanceof GeneralPlayerInterface){
			GeneralPlayerInterface aiPlayer = (GeneralPlayerInterface) curr_player;
			if(detachLogger)
				state.detatchLogger();
			int[] actions = aiPlayer.getActions(state.copyForPlayer(playerId),playerId);
			if(detachLogger)
				state.setEventDispatcher(logger);
			this.perform(state,actions,playerId);
		}

		return new Result(state);
	}

	private  PlayableAction[] convertActions(Action[] actions){
		PlayableAction[] pActions = new PlayableAction[actions.length];
		for(int i=0; i<pActions.length; i++)
			pActions[i] = (PlayableAction) actions[i];
		return pActions;
	}

	public Result playFullGame(State state){
		decidePlayersOrder();

		Result res = new Result(state);

		while(!res.isGameOver){
			res = stepPlay(state);
			//System.out.println(state);
		}

		return res;
	}

	public boolean simulate(State s, int actionId, int playerId){
		PlayableAction pAction = actionDecoder.decodeAction(actionId,s,playerId);
		return performAtomicAction(s,pAction);
	}

	private boolean performAtomicAction(State s, PlayableAction a){
		if(s.isGameOver()){
			return false;
		}
		if (!a.perform(s)){
			return false;
		}
		s.tick();
		passiveRules(s);
		gameOverCheck(s);
		profiler.stateEngineAdvanced();
		return true;
	}

	private void gameOverCheck(State s){
		for(PlayerState ps: s.playerStates){
			if (ps.points>=s.params.endGameScore){
				s.gameOver();
				return;
			}
		}
	}

	private void perform(State state, int[] actions, int playerId){
		for(int actionIndex = 0; actionIndex<actions.length; actionIndex++){
			int currActionId = actions[actionIndex];
			PlayableAction a = actionDecoder.decodeAction(currActionId,state,playerId);
			performAtomicAction(state,a);
		}
	}

	private void perform(State state, PlayableAction[] actions, int playerId){
		for(int aIndex=0; aIndex<actions.length; aIndex++){
			PlayableAction action = actions[aIndex];
			if(action.getPlayerId()==playerId){
				performAtomicAction(state,actions[aIndex]);
			}
		}
	}

	private void passiveRules(State state){
		for(PassiveActionInterface passiveAction: passiveActions){
			passiveAction.perform(state);
		}
	}

	private void decidePlayersOrder(){
		// default policy: AS IS

		// random policy
		if(shufflePlayers)
			Collections.shuffle(players);

		for(int i=0; i<players.size(); i++){
			players.get(i).reset();
			players.get(i).setId(i);
		}

	}

	public PlayableAction randomAction(State s, int playerId){
		if(rndActCrt==null){
			return null;
		}
		return this.rndActCrt.getAction(s,playerId);
	}

	public PlayableAction seededRandomAction(State s, int playerId, long seed){
		PlayableAction action;
		rndActCrt.setRandomSource(new Random(seed));
		action = rndActCrt.getAction(s,playerId);
		rndActCrt.setRandomSource(this.globalRnd);
		return action;
	}

	public static Engine defaultEngine(){
		Engine engine = new Engine();
		ActionsEncoderDecoder encoderDecoder = new StatelessDecEnc();

		encoderDecoder.addManagedActionType(new ATBuyBoardCard());
		encoderDecoder.addManagedActionType(new ATBuyReservedCard());
		encoderDecoder.addManagedActionType(new ATPickSameCoinsGeneric());
		encoderDecoder.addManagedActionType(new ATPickDifferentCoinsGeneric());
		encoderDecoder.addManagedActionType(new ATReserveBoardCard());
		encoderDecoder.addManagedActionType(new ATReserveDeckCard());

		engine.setActionSpaceEncoder(encoderDecoder);
		engine.addPassiveAction(new TakeNoble());

		RandomActionFeeder rndActCrt = new RandomActionFeeder();

		rndActCrt.addRandomActionGenerator(new RandomBuyBoardCard(),22.5);
		rndActCrt.addRandomActionGenerator(new RandomBuyReservedCard(),22.5);
		rndActCrt.addRandomActionGenerator(new RandomPickDifferentCoins(),22.5);
		rndActCrt.addRandomActionGenerator(new RandomPickSameCoins(),22.5);
		rndActCrt.addRandomActionGenerator(new RandomReserveBoardCard(),5);
		rndActCrt.addRandomActionGenerator(new RandomReserveDeckCard(),5);

		engine.setRandomActionFeeder(rndActCrt);

		return engine;
	}

	public static Engine defaultEngine(BasePlayerInterface[] players){
		Engine engine = new Engine(players);
		ActionsEncoderDecoder encoderDecoder = new StatelessDecEnc();

		encoderDecoder.addManagedActionType(new ATBuyBoardCard());
		encoderDecoder.addManagedActionType(new ATBuyReservedCard());
		encoderDecoder.addManagedActionType(new ATPickSameCoinsGeneric());
		encoderDecoder.addManagedActionType(new ATPickDifferentCoinsGeneric());
		encoderDecoder.addManagedActionType(new ATReserveBoardCard());
		encoderDecoder.addManagedActionType(new ATReserveDeckCard());

		engine.setActionSpaceEncoder(encoderDecoder);
		engine.addPassiveAction(new TakeNoble());

		RandomActionFeeder rndActCrt = new RandomActionFeeder();

		rndActCrt.addRandomActionGenerator(new RandomBuyBoardCard(),22.5);
		rndActCrt.addRandomActionGenerator(new RandomBuyReservedCard(),22.5);
		rndActCrt.addRandomActionGenerator(new RandomPickDifferentCoins(),22.5);
		rndActCrt.addRandomActionGenerator(new RandomPickSameCoins(),22.5);
		rndActCrt.addRandomActionGenerator(new RandomReserveBoardCard(),5);
		rndActCrt.addRandomActionGenerator(new RandomReserveDeckCard(),5);

		engine.setRandomActionFeeder(rndActCrt);

		return engine;
	}

}
