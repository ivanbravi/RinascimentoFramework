package game;

import game.state.State;
import java.util.concurrent.Callable;

public class PlayFullGameThreaded implements Callable<State> {

	Engine engine;
	State start;

	public PlayFullGameThreaded(Engine engine, State start){
		this.engine = engine;
		this.start = start;
	}

	@Override
	public State call() throws Exception {
		State thisGame = (State) start.copy();
		engine.playFullGame(thisGame);
		return thisGame;
	}
}
