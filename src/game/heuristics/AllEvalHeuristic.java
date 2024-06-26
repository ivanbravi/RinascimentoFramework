package game.heuristics;

import game.BudgetExtendedGameState;
import game.state.PlayerState;
import game.state.Result;
import game.state.State;
import java.util.stream.IntStream;

public class AllEvalHeuristic implements Heuristic {

    @Override
    public double value(BudgetExtendedGameState state, int playerID) {
        State s = state.getState();
        Result r = new Result(s);
        PlayerState playerState = s.playerStates[playerID];
        int gemCount = IntStream.of(playerState.coins).sum() + playerState.gold;
        int cardCount = playerState.getCardsCount();
        double nobleCount = playerState.nobles.stream().mapToDouble(a->a).sum();
        if (r.s== Result.STATE.RUNNING){
            return (1.5*r.points[playerID] + gemCount + cardCount + 2.5*nobleCount) * Math.pow(0.9, s.getTick());
        }
        if(r.s == Result.STATE.STALE){
            return 0;
        }
        if(r.s == Result.STATE.OVER){
            double winBonus = r.position[playerID] == 0 ? 100 : -100;
            return (winBonus + 1.5*r.points[playerID] + gemCount + cardCount + 2.5*nobleCount) * Math.pow(0.9, s.getTick());
        }
        return 0;
    }

    @Override
    public Heuristic clone() {
        return new AllEvalHeuristic();
    }
}
