package players.ai.explicit;

import game.BudgetExtendedGameState;
import game.action.Action;
import players.BasePlayerInterface;

public class RandomPlayer implements ExplicitPlayerInterface{

	int playerId;
	String name;

	public RandomPlayer(){
		name = "RandomPlayer";
	}

	public RandomPlayer(String name) {
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
		Action action = gameState.getRandomAction(playerId);
		return new Action[]{action};
	}

	@Override
	public BasePlayerInterface reset() {
		return this;
	}

	@Override
	public void setId(int id) {
		this.playerId = id;
	}

	@Override
	public double utility() {
		return 0;
	}

	@Override
	public BasePlayerInterface clone() {
		return new RandomPlayer(name);
	}

}
