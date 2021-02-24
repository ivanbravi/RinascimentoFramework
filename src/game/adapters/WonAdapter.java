package game.adapters;

import game.AbstractGameState;
import game.state.State;
import game.state.Result;

public class WonAdapter implements statistics.adapters.WonAdapter {
	@Override
	public boolean hasPlayerWon(AbstractGameState gs, String player) {
		if(gs instanceof State){
			State s = (State) gs;
			Result r = new Result(s);
			int playerID = s.getPlayerId(player);
			return r.position[playerID]==1;
		}

		return false;
	}
}
