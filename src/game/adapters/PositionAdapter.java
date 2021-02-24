package game.adapters;

import game.AbstractGameState;
import game.state.State;
import game.state.Result;

public class PositionAdapter implements statistics.adapters.PositionAdapter {
	@Override
	public int position(AbstractGameState gs, String player) {
		if(gs instanceof State){
			State s = (State) gs;
			Result r = new Result(s);

			if(r.s== Result.STATE.STALE){
				return -1;
			}

			int playerID = s.getPlayerId(player);
			return r.position[playerID];
		}
		return 0;
	}
}
