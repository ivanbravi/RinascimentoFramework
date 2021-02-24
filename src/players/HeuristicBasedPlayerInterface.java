package players;

import game.BudgetExtendedGameState;
import game.heuristics.Heuristic;

public abstract class HeuristicBasedPlayerInterface implements BasePlayerInterface{

	protected int id;
	protected String name;
	protected Heuristic h;

	@Override
	public void setName(String name){
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setId(int id){
		this.id= id;
	}

	public void setHeuristic(Heuristic h){
		this.h = h;
	}

	public double utility(BudgetExtendedGameState s){
		return h.value(s,this.id);
	}

	@Override
	public String toString() {
		return "h:"+h.toString();
	}

	public abstract HeuristicBasedPlayerInterface clone();
	
}
