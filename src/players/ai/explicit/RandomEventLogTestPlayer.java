package players.ai.explicit;

import game.BudgetExtendedGameState;
import game.action.Action;
import game.log.converters.SimpleConverter;
import log.entities.event.EventVectorLogger;
import players.BasePlayerInterface;

import java.util.Arrays;

public class RandomEventLogTestPlayer implements ExplicitPlayerInterface {

	private int id;
	protected String name = "LogTester";
	protected String loggerName;
	protected EventVectorLogger evl = new EventVectorLogger(new SimpleConverter());

	public RandomEventLogTestPlayer(){
		loggerName = name+this.toString().split("@")[1];
	}

	@Override
	public BasePlayerInterface clone() {
		RandomEventLogTestPlayer clone = new RandomEventLogTestPlayer();
		clone.name = this.name;
		clone.loggerName = this.loggerName;
		return clone;
	}

	@Override
	public Action[] getActions(BudgetExtendedGameState gameState, int playerId) {
		evl.reset();
		gameState.addLogger(loggerName,evl);

		Action a = gameState.getRandomAction(playerId);
		gameState.perform(a);

		double[] v = evl.vector();

		System.out.println(Arrays.toString(v));

		return new Action[]{a};
	}

	@Override
	public BasePlayerInterface reset() {
		return null;
	}

	@Override
	public void setId(int id) {
		this.id = id;
	}

	@Override
	public double utility() {
		return 0;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return null;
	}
}
