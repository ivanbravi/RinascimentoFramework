package players.ai.explicit.mixer;

import game.BudgetExtendedGameState;
import game.action.Action;
import game.heuristics.Heuristic;
import players.BasePlayerInterface;
import players.HeuristicBasedPlayerInterface;
import players.ai.explicit.ExplicitPlayerInterface;
import utils.WeightedRandomSelection;

import java.util.ArrayList;

public class MixerAgent extends HeuristicBasedPlayerInterface implements ExplicitPlayerInterface{

	ArrayList<WeightedPlayer> players = new ArrayList<>();
	WeightedRandomSelection agentSelection = new WeightedRandomSelection();

	public MixerAgent(ExplicitPlayerInterface[] players, double[] weights){
		for(int i=0; i<players.length; i++){
			WeightedPlayer wp = new WeightedPlayer(players[i], weights[i]);
			this.players.add(wp);
		}
	}

	public MixerAgent(ArrayList<WeightedPlayer> players){
		this.players = players;
	}

	@Override
	public void setHeuristic(Heuristic h) {
		super.setHeuristic(h);
		for(WeightedPlayer player: players){
			if(player.getPlayer() instanceof HeuristicBasedPlayerInterface){
				HeuristicBasedPlayerInterface hPlayer = (HeuristicBasedPlayerInterface) player.getPlayer();
				hPlayer.setHeuristic(h);
			}
		}
	}

	@Override
	public BasePlayerInterface reset() {
		for( WeightedPlayer p : players)
			p.getPlayer().reset();
		return this;
	}

	@Override
	public void setId(int id) {
		super.setId(id);
		for(WeightedPlayer wi: this.players)
			wi.getPlayer().setId(id);
	}

	@Override
	public double utility() {
		return 0;
	}

	@Override
	public HeuristicBasedPlayerInterface clone() {
		ArrayList<WeightedPlayer> clonedPlayers = new ArrayList<>();
		for(WeightedPlayer wi: this.players)
			clonedPlayers.add(wi.clone());
		HeuristicBasedPlayerInterface clone = new MixerAgent(clonedPlayers);
		clone.setName(getName());
		return clone;
	}

	@Override
	public Action[] getActions(BudgetExtendedGameState gameState, int playerId) {
		return ((WeightedPlayer)agentSelection.next(players)).getPlayer().getActions(gameState,playerId);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for(WeightedPlayer p: this.players)
			builder.append(p.getPlayer().toString()).append("\n");
		return "MixerAgent{"+builder.toString()+"}";
	}
}
