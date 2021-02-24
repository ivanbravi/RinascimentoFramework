package hyper.agents.factory;

import evodef.AnnotatedSearchSpace;
import players.BasePlayerInterface;
import players.ai.factory.SimpleAgentFactory;

import java.util.Random;

public abstract class AgentFactorySpace {

	private AnnotatedSearchSpace ass;
	private String agentType;

	public String getAgentType() {
		return agentType;
	}

	protected AgentFactorySpace setAgentType(String agentType){
		this.agentType = agentType;
		return this;
	}

	public AgentFactorySpace setSearchSpace(AnnotatedSearchSpace ass){
		this.ass = ass;
		return this;
	}

	public SimpleAgentFactory getSimpleFactory(int[] solution, String suffix){
		return new SearchSpaceBasedFactory(this,solution,suffix);
	}

	public AnnotatedSearchSpace getSearchSpace(){
		return ass;
	}

	public abstract BasePlayerInterface agent(int[] solution);

	@Override
	public String toString(){
		return "[AgentFactory: "+agentType+"]\n"+ass.toString();
	}

	public int[] randomPoint(Random rnd){
		int d = getSearchSpace().nDims();
		int[] config = new int[d];

		for(int i=0; i<d; i++)
			config[i] = rnd.nextInt(getSearchSpace().nValues(i));

		return config;
	}

}
