package hyper.agents.factory;

import players.BasePlayerInterface;
import players.ai.explicit.OneStepLookAheadAgent;
import players.ai.explicit.SafeRandomPlayer;

public class BasicAgentFactory implements AgentFactory{

	private Class classToInstantiate;

	public BasicAgentFactory(String type){
		if(!isBasicAgent(type)){
			throw new RuntimeException("INVALID BASE AGENT FACTORY TYPE: "+type);
		}
		classToInstantiate = getClassToInstantiate(type);
	}

	@Override
	public BasePlayerInterface agent() {
		BasePlayerInterface player = null;
		try {
			player = (BasePlayerInterface) classToInstantiate.getConstructor().newInstance();
		}catch (Exception e){
			e.printStackTrace();
		}
		return player;
	}

	private static Class getClassToInstantiate(String type){
		if(type.equals("Random") || type.equals("RND"))
			return SafeRandomPlayer.class;

		if(type.equals("OSLA"))
			return OneStepLookAheadAgent.class;

		return null;
	}

	public static boolean isBasicAgent(String type){
		return type.equals("Random") || type.equals("RND") || type.equals("OSLA");
	}
}
