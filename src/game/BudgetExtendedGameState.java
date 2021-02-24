package game;

import game.action.Action;
import game.action.PlayableAction;
import game.budget.Budget;
import game.exceptions.StaleGameException;
import game.state.State;
import log.entities.event.Event;
import log.entities.event.EventLogger;
import log.entities.event.EventLoggerDispatcher;

public class BudgetExtendedGameState implements EventLoggerDispatcher {

	protected State state;
	private Budget budget;

	public BudgetExtendedGameState(State state, Budget budget){
		this.state = state;
		this.budget = budget;
	}

	public BudgetExtendedGameState perform(Action a){
		if(a!=null) {
			PlayableAction pa = (PlayableAction) a;
			pa.perform(state);
			state.tick();
			decrease();
		}
		return this;
	}

	private void decrease(){
		budget.decrease();
	}

	public boolean canPerform(Action a){
		if(a==null){
			return false;
		}
		return a.canPerform(this.state);
	}

	public Action getRandomAction(int playerId, long seed){
		Action a = null;
		try{
			a = state.getRandomAction(playerId,seed);
		}catch (StaleGameException e){}

		if(a==null){
			state.stale();
			state.gameOver();
		}

		return a;
	}

	public Action getRandomAction(int playerId){
		Action a = null;
		try{
			a = state.getRandomAction(playerId);
		}catch (StaleGameException e){}

		if(a==null){
			state.stale();
			state.gameOver();
		}

		return a;
	}

	public int getPlayersCount(){
		return state.getPlayersCount();
	}

	public BudgetExtendedGameState copy(){
		return new BudgetExtendedGameState((State)this.state.copy(),budget);
	}

	public BudgetExtendedGameState copyForPlayer(int playerID){
		return new BudgetExtendedGameState(this.state.copyForPlayer(playerID),budget);
	}

	public BudgetExtendedGameState copyAndSplit(double ratio){
		BudgetExtendedGameState split = new BudgetExtendedGameState(
				(State)this.state.copy(),
				this.budget.split(ratio)
				);
		return split;
	}

	public BudgetExtendedGameState copyForPlayerAndSplit(int playerID, double ratio){
		BudgetExtendedGameState split = new BudgetExtendedGameState(
				this.state.copyForPlayer(playerID),
				this.budget.split(ratio)
		);
		return split;
	}

	public boolean isGameOver(){
		return state.isTerminal();
	}

	public State getState(){
		return state;
	}

	@Override
	public EventLogger getLogger(String name) {
		return this.state.eventLogger(name);
	}

	@Override
	public void addLogger(String o, EventLogger e) {
		this.state.plugLogger(o,e);
	}

}
