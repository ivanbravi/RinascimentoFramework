package hyper.environment;

import players.BasePlayerInterface;
import players.ai.factory.SimpleAgentFactory;

import java.util.List;
import java.util.Random;

public class RAEPooledEnemies extends RinascimentoAgentEvaluator {

	protected List<SimpleAgentFactory> opponentsPool;
	protected Random rnd;

	public RAEPooledEnemies(){
		rnd = new Random();
	}

	public RAEPooledEnemies setOpponentsPool(List<SimpleAgentFactory> opponentsPool){
		this.opponentsPool = opponentsPool;
		return this;
	}

	private BasePlayerInterface[] selectOpponents(){
		int nOpponents = this.env.players()-1;
		BasePlayerInterface[] opp = new BasePlayerInterface[nOpponents];
		for(int i=0; i<opp.length; i++){
			opp[i] = opponentsPool.get(rnd.nextInt(opponentsPool.size())).agent();
		}
		return opp;
	}

	@Override
	public double evaluate(int[] solution){
		this.opponents = selectOpponents();
 		return super.evaluate(solution);
	}

}
