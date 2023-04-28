package hyper.agents.factory;

import game.heuristics.Heuristic;
import players.BasePlayerInterface;

public abstract class HeuristicAgentFactorySpace extends AgentFactorySpace {

	private Heuristic h;

	public HeuristicAgentFactorySpace setHeuristic(Heuristic h){
		this.h = h;
		return this;
	}

	public Heuristic getHeuristic() {
		return h;
	}

	public abstract BasePlayerInterface agent(int[] solution, Heuristic h);

	public String toString(){
		return super.toString()+"\n"+h.toString();
	}

}
