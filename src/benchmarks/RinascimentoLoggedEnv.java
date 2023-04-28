package benchmarks;

import game.Factory;
import game.Parameters;
import game.log.RStateEventDispatcher;

public class RinascimentoLoggedEnv extends RinascimentoEnv {


	public RinascimentoLoggedEnv(String paramFile){
		super(paramFile);
	}

	public RinascimentoLoggedEnv(Parameters p){
		super(p);
	}

	public RinascimentoLoggedEnv(String paramFile, RStateEventDispatcher dispatcher){
		super(paramFile);
		startingState = Factory.createStateWithLogger(parameters,engine,dispatcher);
	}

	public void setDispatcher(RStateEventDispatcher dispatcher){
		startingState.setEventDispatcher(dispatcher);
	}
	public RStateEventDispatcher getDispatcher(){ return startingState.eventDispatcher();}

}
