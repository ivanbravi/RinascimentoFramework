package game.adapters;

import game.AbstractGameState;
import game.state.State;
import game.state.Result;
import statistics.adapters.WonStaleLossAdapter;

public class RWonStaleLossAdapter implements statistics.adapters.WonStaleLossAdapter {
	@Override
	public int wonStaleLossReward(AbstractGameState gs, String player) {

		if(gs instanceof State){
			State s = (State) gs;
			Result r = new Result(s);
			int playerID = s.getPlayerId(player);
			int position = r.position[playerID];

			if(r.s== Result.STATE.STALE)
				return WonStaleLossAdapter.STALE;

			if(position==1){

				boolean otherWinner = false;
				for(int i=0; i<r.position.length; i++){
					int p = r.position[i];
					if(i!=playerID && p==position)
						otherWinner = true;
				}

				if(otherWinner)
					return WonStaleLossAdapter.TIE;
				return WonStaleLossAdapter.WON;
			}

			// if it didn't win it returns the negative
			// displacement from the first position
			return -position+1;
		}

		return 0;
	}
}
