package hyper.agents.featurebased;

import evodef.AnnotatedSearchSpace;
import game.heuristics.StateBasedVectorHeuristic;
import game.state.vectorise.Vectoriser;
import hyper.agents.eventbased.CombinedSearchSpace;
import hyper.agents.factory.AgentFactorySpace;
import hyper.agents.factory.HeuristicAgentFactorySpace;
import log.heuristics.WeightedHeuristic;
import players.BasePlayerInterface;

public class FeatureBasedAgentFactory extends AgentFactorySpace {

	private static final int agentSpaceIndex = 0;
	private static final int heuristicSpaceIndex = 1;

	private HeuristicAgentFactorySpace agentFactory;
	private WeightedHeuristic h;
	private Vectoriser v;

	public FeatureBasedAgentFactory(HeuristicAgentFactorySpace agentFactory, WeightedHeuristic h, Vectoriser v){
		this.agentFactory = agentFactory;
		this.h = h;
		this.v = v;
	}

	public void setAgentAndHeuristicType(String agentType, String heuristicType, String features){
		setAgentType("SVB-"+features+"-"+agentType+"-"+heuristicType);
	}

	@Override
	public AgentFactorySpace setSearchSpace(AnnotatedSearchSpace ass) {
		if(ass instanceof CombinedSearchSpace){
			CombinedSearchSpace css = (CombinedSearchSpace) ass;
			super.setSearchSpace(ass);
			if(h.dimensionality() != css.nDims(heuristicSpaceIndex))
				throw new RuntimeException("Inappropriate heuristic space dimensionality!");
			agentFactory.setSearchSpace(css.getSpace(agentSpaceIndex));
			return this;
		}
		return null;
	}

	@Override
	public BasePlayerInterface agent(int[] solution) {
		CombinedSearchSpace css = (CombinedSearchSpace) getSearchSpace();
		int[] agentSolution = css.splitSolution(solution, agentSpaceIndex);
		int[] weightsSolution = css.splitSolution(solution, heuristicSpaceIndex);

		WeightedHeuristic vh = h.clone();
		vh.setWeights(compileWeights(weightsSolution));

		StateBasedVectorHeuristic heuristic = new StateBasedVectorHeuristic(vh, v.clone());
		BasePlayerInterface agent = agentFactory.agent(agentSolution, heuristic);
		agent.setName(getAgentType());

		return agent;
	}

	private double[] compileWeights(int[] solution){
		AnnotatedSearchSpace ass = ((CombinedSearchSpace) getSearchSpace()).getSpace(heuristicSpaceIndex);
		double[] w =  new double[ass.nDims()];
		for(int i=0; i<ass.nDims(); i++){
			w[i] = (double) ass.getParams()[i].getValue(solution[i]);
		}
		return w;
	}
}
