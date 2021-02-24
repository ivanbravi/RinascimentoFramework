package benchmarks;

import game.Factory;
import game.log.RStateEventDispatcher;

public class RinascimentoLoggedEnv extends RinascimentoEnv {


	public RinascimentoLoggedEnv(String paramFile){
		super(paramFile);
	}

	public RinascimentoLoggedEnv(String paramFile, RStateEventDispatcher dispatcher){
		super(paramFile);
		startingState = Factory.createStateWithLogger(parameters,engine,dispatcher);
	}

	public void setDispatcher(RStateEventDispatcher dispatcher){
		startingState.setEventDispatcher(dispatcher);
	}

}
