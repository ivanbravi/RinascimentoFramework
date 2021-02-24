package players.ai.explicit;

import game.BudgetExtendedGameState;
import game.ExtendedGameState;
import game.action.Action;
import game.action.PlayableAction;
import players.BasePlayerInterface;

public class DoNothingAgent implements ExplicitPlayerInterface {

	String name;

	public DoNothingAgent(){
		name = "DoNothing";
	}
	public DoNothingAgent(String name){
		this.name = name;
	}

	@Override
	public void setName(String name){
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Action[] getActions(BudgetExtendedGameState gameState, int playerId) {
		return new PlayableAction[]{null};
	}

	@Override
	public BasePlayerInterface reset() {
		return this;
	}

	@Override
	public void setId(int id) {
	}

	@Override
	public double utility() {
		return 0;
	}

	@Override
	public BasePlayerInterface clone() {
		return new DoNothingAgent(name);
	}
}
