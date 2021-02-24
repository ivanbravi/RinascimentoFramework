package hyper.agents.factory;

import game.heuristics.Heuristic;
import players.BasePlayerInterface;

public abstract class HeuristicAgentFactory extends AgentFactorySpace {

	private Heuristic h;

	public HeuristicAgentFactory setHeuristic(Heuristic h){
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
