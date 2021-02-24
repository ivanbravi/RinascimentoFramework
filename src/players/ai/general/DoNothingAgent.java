package players.ai.general;

import game.AbstractGameState;
import players.BasePlayerInterface;

public class DoNothingAgent implements GeneralPlayerInterface {

    String name;

    public DoNothingAgent(){ name = "General Do Nothing";}
    public DoNothingAgent(String name){ this.name = name; }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int[] getAction(AbstractGameState gameState, int playerId) {
        return new int[0];
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
