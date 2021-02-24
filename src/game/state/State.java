package game.state;

import com.google.gson.*;
import game.*;
import game.action.PlayableAction;
import game.log.DummyStateEventDispatcher;
import game.log.RStateEventDispatcher;
import generators.decks.SplendorDecksGenerator;
import generators.nobles.SplendorNoblesGenerator;
import log.entities.event.EventLogger;
import log.entities.event.EventLoggerDispatcher;
import players.GeneralPlayerInterface;
import utils.profiling.Profiler;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class State implements ExtendedGameState, SeededExtendedGameState {
	private static boolean reshuffleOnCopy = true;
	private static Profiler profiler = new Profiler();

	public Parameters params;
	private transient Engine engine;

	private boolean storePlaytrace = false;
	private ArrayList<String> playtrace;

	public PlayerState[] playerStates;

	public Deck[] decks;
	public ShuffledDeck[] deckStacks;
	public Noble[] nobles;
	public transient Noble[] allNobles;
	public boolean [] isNobleTaken;
	public int [][] board;
	public int [] coinStacks;
	public int goldStack;

	private int tick;
	private boolean isGameOver = false;
	private boolean isGameStale = false;
	private boolean hasToInit = true;

	private String myName = "Engine";

	private transient RStateEventDispatcher eventDispatcher = DummyStateEventDispatcher.get();

	private State(){

	}

	public State(Parameters params, Deck[] decks, Noble[] nobles, Engine engine){
		this.engine = engine;
		this.params = params;
		this.playerStates = new PlayerState[params.playerCount];
		this.coinStacks = new int[params.suitCount];
		this.goldStack = params.goldCount;
		this.playtrace = new ArrayList<>();

		for(int playerId = 0; playerId<params.playerCount; playerId++){
			this.playerStates[playerId] = new PlayerState(this, playerId, params);
		}

		for(int i=0; i<coinStacks.length; i++){
			this.coinStacks[i] = params.coinSuitUsed[i]*params.coinCount;
		}

		this.decks = new Deck[params.deckCount];
		this.deckStacks = new ShuffledDeck[params.deckCount];
		this.board = new int[params.deckCount][];

		this.allNobles = nobles;

		for(int deckId=0; deckId<params.deckCount; deckId++){
			this.board[deckId] = new int[params.cardsOnTableCount];
			for(int cardPosition=0; cardPosition<params.cardsOnTableCount; cardPosition++){
				this.board[deckId][cardPosition] = -1;
			}
		}

		this.setDecks(decks);
	}

	public static void attatchProfiler(Profiler p){
		profiler = p;
	}

	public void setEventDispatcher(RStateEventDispatcher eventDispatcher){
		this.eventDispatcher = eventDispatcher;
		for (PlayerState ps: playerStates)
			ps.setEventLog(eventDispatcher);
	}

	public RStateEventDispatcher eventDispatcher(){
		return eventDispatcher;
	}

	public void detatchLogger(){
		this.eventDispatcher = DummyStateEventDispatcher.get();
		for (PlayerState ps: playerStates)
			ps.detatchEventLogger();
	}

	public void stale() {
		isGameStale = true;
	}

	public boolean isStale(){
		return isGameStale;
	}

	public void addActionPerformed(PlayableAction a){
		if(storePlaytrace) {
			this.playtrace.add(a.toString());
		}
	}

	public State copyForPlayer(int playerID){
		if(playerID<0 || playerID>= params.playerCount){
			return null;
		}
		State copy = (State) this.copy();

		for(int i=0; i<params.playerCount; i++){
			if(playerID!=i){
				copy.getPlayerState(i).filterReservedCards();
			}
		}

		return copy;
	}

	//region Events

	public void takeNoble(int index){
		eventDispatcher.nobleTaken(this, nobles[index]);
		isNobleTaken[index] = true;
	}

	public void increaseTokens(int stackId, int amount){
		eventDispatcher.increaseTokens(this,stackId,amount);
		this.coinStacks[stackId] += amount;
	}

	public void decreaseTokens(int stackId, int amount){
		eventDispatcher.decreaseTokens(this,stackId,amount);
		this.coinStacks[stackId] -= amount;
	}

	public void decreaseGold(int amount){
		eventDispatcher.decreaseGold(this,amount);
		this.goldStack -= amount;
	}

	public void increaseGold(int amount){
		eventDispatcher.increaseGold(this,amount);
		this.goldStack += amount;
	}

	public int drawCard(int deckId){
		int newCardId = deckStacks[deckId].drawCard();
		eventDispatcher.drawCard(this,deckId, newCardId);
		return newCardId;
	}

	public void placeCard(int deckId, int position){
		int newCardId = drawCard(deckId);
		eventDispatcher.fillCard(this, deckId,position, newCardId);
		this.board[deckId][position] = newCardId;
	}

	public void placeNoble(int index, Noble n){
		eventDispatcher.fillNoble(this, n);
		this.nobles[index] = n;
	}

	//endregion

	//region INITIALISATION

	public boolean isInitialised(){
		return !hasToInit;
	}

	public void initGame(){
		if(!hasToInit)
			return;

		eventDispatcher.begin(tick,myName,null);
		placeCards();
		placeNobles();
		eventDispatcher.done();

		hasToInit=false;
	}

	private void placeNobles(){
		Noble[] selected = Noble.pickNobles(allNobles,params.noblesCount);
		this.nobles = new Noble[selected.length];
		this.isNobleTaken = new boolean[selected.length];
		for(int i=0; i<nobles.length;i++){
			placeNoble(i,selected[i]);
		}
	}

	private void placeCards(){
		for(int deckId = 0; deckId<params.deckCount; deckId++){
			for(int cardPosition=0; cardPosition<params.cardsOnTableCount; cardPosition++){
				if(board[deckId][cardPosition]==-1){
					placeCard(deckId,cardPosition);
					//board[deckId][cardPosition] = this.deckStacks[deckId].drawCard();
				}
			}
		}
	}

	private void setDecks(Deck[] decks){

		if(decks.length!=this.decks.length){
			throw new RuntimeException("Invalid number of decks");
		}

		for(int deckId=0; deckId<this.decks.length; deckId++){
			this.decks[deckId] = decks[deckId];
			this.deckStacks[deckId] = Deck.shuffle(decks[deckId]);
		}
	}

	//endregion

	public PlayerState getPlayerState(int playerId){
		if(playerId<0 || playerId>=params.playerCount){
			return null;
		}
		return playerStates[playerId];
	}


	public ArrayList<String> getPlaytrace(){
		return playtrace;
	}

	public void gameOver(){
		this.isGameOver = true;
	}

	public boolean isGameOver(){
		return isGameOver;
	}

	public void tick(){
		tick++;
	}

	public int getTick(){
		return tick;
	}

	@Override
	public double[] getScore() {
		return engine.getScore(this);
	}

	@Override
	public int getPlayersCount() {
		return params.playerCount;
	}

	public void setNames(String[] names){
		if(names.length!=playerStates.length){
			throw new RuntimeException("Player names mismatch!");
		}
		for(int i=0; i<names.length; i++)
			playerStates[i].setName(names[i]);
	}

	@Override
	public String[] getPlayerNames(){
		String[] names = new String[playerStates.length];
		for(int i=0; i<names.length; i++){
			String name = playerStates[i].getName();
			if(name == null)
				name = engine.getPlayerNames()[i];
			names[i] = name;
		}
		return names;
	}

	public int getPlayerId(String pName){
		for(int i=0; i<playerStates.length; i++) {
			if (playerStates[i].getName().equals(pName))
				return i;
		}
		return -1;
	}

	@Override
	public boolean isTerminal() {
		return isGameOver;
	}

	@Override
	public List<Object> getGameState(GeneralPlayerInterface player) {
		return null;
	}

	@Override
	public AbstractGameState next(int[][] actions){
		for(int i=0; i<actions.length; i++){
			for(int aId=0; aId<actions[i].length; i++){
				this.engine.simulate(this,actions[i][aId],i);
			}
		}
		return this;
	}

	//region ACTIONSPACE

	@Override
	public PlayableAction getRandomAction(int playerId) {
		return engine.randomAction(this,playerId);
	}

	@Override
	public PlayableAction getRandomAction(int playerId, long seed) {
		return engine.seededRandomAction(this,playerId,seed);
	}

	//endregion

	//region DEPRECATED

	@Override
	public boolean simulate(int action, int playerId){
		return this.engine.simulate(this,action,playerId);
	}

	@Override
	public int[] nActions(GeneralPlayerInterface player) {
		return new int[]{engine.getPlayerActionSpace(this,player)};
	}

	//endregion

	//region IO
	public static void save(String path, State s){
		try (Writer w = new FileWriter(path)){
			Gson writer = new GsonBuilder().setPrettyPrinting().create();
			writer.toJson(s, w);
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	public static State load(String statePath){
		State read = null;
		try (Reader r = new FileReader(statePath)) {
			JsonDeserializer<RStateEventDispatcher> deserializer = new JsonDeserializer<RStateEventDispatcher>(){

				@Override
				public RStateEventDispatcher deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
					return DummyStateEventDispatcher.get();
				}
			};
			GsonBuilder gsonBuilder = new GsonBuilder();
			gsonBuilder.registerTypeAdapter(RStateEventDispatcher.class, deserializer);
			Gson parser = gsonBuilder.create();
			read = parser.fromJson(r, State.class);
		}catch (Exception e){
			e.printStackTrace();
		}
		return read;
	}

	//endregion

	//region Object

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		String deckSymbol;

		builder.append("\n(").append(tick).append(")\n");
		builder.append("BOARD\n");
		builder.append("\tcoins:  ").append(Arrays.toString(this.coinStacks)).append("\n");
		builder.append("\tdecks:");
		String spacing = "\t";

		for(int i=0; i<deckStacks.length; i++){
			if(deckStacks[i].isEmpty()){
				deckSymbol = "x";
			}else{
				deckSymbol="•";
			}
			builder.append(spacing).append("[").append(deckSymbol).append("]").append(Arrays.toString(board[i])).append("\n");
			spacing = "\t        ";
		}

		if(nobles!=null){
			if(nobles.length>0) {
				builder.append("\tnobles:");
				for (int i = 0; i < nobles.length; i++) {
					if(nobles[i]!=null)
						builder.append(" [").append(nobles[i].getId()).append(",").append((isNobleTaken[i] ? "0" : "1")).append("]");
					else
						builder.append(" [loading]");
				}
				builder.append("\n");
			}else{
				builder.append("\tnobles: NONE\n");
			}
		}else{
			builder.append("\tnobles: NONE\n");
		}

		for (PlayerState playerState : playerStates)
			builder.append(playerState.toString());

		return builder.toString();
	}

	@Override
	public AbstractGameState copy() {
		State copy = new State();

		//shallow copies
		copy.engine = this.engine;
		copy.params = this.params;
		copy.decks = Arrays.copyOf(this.decks,this.decks.length);

		//implicit deep copies
		copy.coinStacks = Arrays.copyOf(this.coinStacks,this.coinStacks.length);
		copy.goldStack = this.goldStack;
		copy.tick = this.tick;

		//explicit deep copies
		copy.playtrace = (ArrayList<String>) this.playtrace.clone();
		copy.deckStacks = new ShuffledDeck[this.params.deckCount];
		copy.board = new int[this.params.deckCount][];

		for(int deckId=0; deckId<this.params.deckCount; deckId++){
			copy.deckStacks[deckId] = this.deckStacks[deckId].clone();
			copy.board[deckId] = Arrays.copyOf(this.board[deckId],this.board[deckId].length);
			if(reshuffleOnCopy)
				copy.deckStacks[deckId].shuffle();
		}

		copy.allNobles = this.allNobles;
		copy.nobles = this.nobles;
		if(isNobleTaken!=null) {
			copy.isNobleTaken = Arrays.copyOf(this.isNobleTaken, this.isNobleTaken.length);
		}

		copy.playerStates = new PlayerState[this.params.playerCount];
		for(int playerId=0; playerId<this.params.playerCount; playerId++){
			copy.playerStates[playerId] = this.playerStates[playerId].clone();
		}

		//propagating the eventLog
		copy.setEventDispatcher(this.eventDispatcher.copy());

		profiler.stateCopy();

		return copy;
	}

	//endregion

	public static State testState(Engine e){
		Parameters params = Parameters.defaultParameters();

		SplendorDecksGenerator decksGenerator = new SplendorDecksGenerator();
		Deck[] decks = decksGenerator.getDecks(params);

		SplendorNoblesGenerator noblesGenerator = new SplendorNoblesGenerator();
		Noble[] nobles = noblesGenerator.getNobles(params);

		return new State(params,decks,nobles,e);
	}

	public void plugLogger(String name, EventLogger l){
		if(eventDispatcher instanceof EventLoggerDispatcher)
			((EventLoggerDispatcher) eventDispatcher).addLogger(name,l);
	}

	public EventLogger eventLogger(String name) {
		if(eventDispatcher instanceof EventLoggerDispatcher)
			return ((EventLoggerDispatcher) eventDispatcher).getLogger(name);
		return null;
	}
}
