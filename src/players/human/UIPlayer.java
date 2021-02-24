package players.human;

import game.BudgetExtendedGameState;
import game.action.Action;
import gui.UIPlayerInput;
import players.BasePlayerInterface;
import players.ai.explicit.ExplicitPlayerInterface;

public class UIPlayer implements ExplicitPlayerInterface {

	UIPlayerInput input;
	int id;
	String name = "Human";

	public void setInput(UIPlayerInput input) {
		this.input = input;
	}

	@Override
	public Action[] getActions(BudgetExtendedGameState gameState, int playerId){
		input.setPlayerId(playerId);
		Action a = null;
		while (a==null){
			try {
				Thread.sleep(100);
			}catch (InterruptedException e){}
			a = input.dequeueAction();
		}
		return new Action[]{a};
	}

	@Override
	public BasePlayerInterface reset() {
		return this;
	}

	@Override
	public void setId(int id) {
		this.id=id;
	}

	@Override
	public double utility() {
		return 0;
	}

	@Override
	public BasePlayerInterface clone() {
		UIPlayer clone = new UIPlayer();
		clone.setName(name);
		clone.setInput(input);
		return clone;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}
}
