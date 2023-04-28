package benchmarks;

import game.PlayFullGameThreaded;
import statistics.GameStats;
import game.Engine;
import game.Factory;
import game.Parameters;
import game.budget.Budget;
import game.state.Result;
import game.state.State;
import players.BasePlayerInterface;
import utilities.ElapsedTimer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.*;

public class RinascimentoEnv {

	public static boolean VERBOSE = false;
	private static boolean MULTITHREADED = false;
	private static int THREADS = 1 ;
	private static ExecutorService pool;

	BasePlayerInterface[] players;

	Parameters parameters;
	Engine engine;
	State startingState;
	GameStats stats;

	public RinascimentoEnv(Parameters parameters){
		this.parameters = parameters;
		engine = Engine.defaultEngine();
		startingState = Factory.createState(parameters, engine);
		setTHREADS(THREADS);
	}

	public RinascimentoEnv(String paramFile){
		parameters = Parameters.load(paramFile);
		engine = Engine.defaultEngine();
		startingState = Factory.createState(parameters,engine);
		setTHREADS(THREADS);
	}

	public static void setTHREADS(int THREADS) {
		if(THREADS>1){
			RinascimentoEnv.THREADS = THREADS;
			MULTITHREADED = true;
			if(pool!=null){
				pool.shutdown();
			}
			pool = Executors.newFixedThreadPool(THREADS);
		}else{
			MULTITHREADED = false;
		}
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();

		builder.append("[Engine]\n");
		builder.append(engine.toString());

		return builder.toString();
	}

	public int players(){
		return startingState.params.playerCount;
	}

	public RinascimentoEnv setPlayers(BasePlayerInterface[] players){
		this.players = players;
		engine.setPlayers(this.players);
		return this;
	}

	public RinascimentoEnv setStats(GameStats stats){
		this.stats = (GameStats) stats.clone();
		return this;
	}

	public RinascimentoEnv setPlayersBudget(Budget b){
		engine.setBudget(b);
		return this;
	}

	private void setupEngine(Engine engine){
		BasePlayerInterface[] p = new BasePlayerInterface[players.length];
		for(int i=0; i<p.length; i++)
			p[i] = players[i].clone();
		engine.setPlayers(p);
	}

	public GameStats runOnce(){
		ElapsedTimer timer = new ElapsedTimer();
		setupEngine(engine);
		State currGame = (State) startingState.copy();
		Result r =  engine.playFullGame(currGame);
		if(VERBOSE){
			System.out.println("Duration: "+timer.elapsed()+"ms ticks: "+currGame.getTick());
		}
		return stats.create(currGame,stats.getPlayer());
	}

	public GameStats runMultiple(int times){
		if(MULTITHREADED)
			return runMultipleThreaded(times);
		return runMultipleSequential(times);
	}

	private GameStats runMultipleSequential(int times){
		GameStats multipleStats = (GameStats) this.stats.clone();
		boolean preVERBOSE = VERBOSE;
		VERBOSE=false;
		ElapsedTimer timer = new ElapsedTimer();
		for(int i=0; i<times; i++){
			GameStats result = runOnce();
			multipleStats.add(result);
		}
		VERBOSE = preVERBOSE;
		if(VERBOSE){
			System.out.println("Duration: "+timer.elapsed()+"ms games: "+times);
		}
		return multipleStats;
	}

	private GameStats runMultipleThreaded(int times){
		GameStats multipleStats = (GameStats) this.stats.clone();
		Set<Future<State>> set = new HashSet<>();
		for (int i=0; i<times; i++) {
			Engine clonedEngine = engine.clone();
			setupEngine(clonedEngine);
			State clonedState = (State) startingState.copy();
			Callable<State> callable = new PlayFullGameThreaded(clonedEngine,clonedState);
			Future<State> future = pool.submit(callable);
			set.add(future);
		}
		for (Future<State> future : set) {
			State resultState = null;
			try {
				resultState = future.get();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
			GameStats result = stats.create(resultState, stats.getPlayer());
			multipleStats.add(result);
		}

		return multipleStats;
	}

	public static void shutDown(){
		if(pool!=null)
			pool.shutdown();
	}

}
