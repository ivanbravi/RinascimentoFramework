package game.adapters;

import game.AbstractGameState;
import game.state.State;
import game.state.Result;

public class WonStaleLossAdapter implements statistics.adapters.WonStaleLossAdapter {
	@Override
	public int wonStaleLossReward(AbstractGameState gs, String player) {

		if(gs instanceof State){
			State s = (State) gs;
			Result r = new Result(s);
			int playerID = s.getPlayerId(player);
			int position = r.position[playerID];

			if(r.s== Result.STATE.STALE)
				return 0;

			if(position==1)
				return 1;

			return -r.position[playerID]+1;
		}

		return 0;
	}
}
