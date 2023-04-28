package mapelites.pointbased;

import game.heuristics.Heuristic;
import game.heuristics.PointsHeuristic;
import hyper.agents.factory.HeuristicAgentFactorySpace;
import mapelites.PlayerSolution;
import players.BasePlayerInterface;

import java.util.Arrays;

public class PBAgentSolution extends PlayerSolution {

    private int[] agentConfig;
    transient private HeuristicAgentFactorySpace factory;
    private String playerName;

    public PBAgentSolution(String playerName, int[] agentConfig, HeuristicAgentFactorySpace factory){
        this.playerName = playerName;
        this.agentConfig = agentConfig;
        this.factory = factory;
    }

    public int[] getAgentConfig() {
        return agentConfig;
    }

    @Override
    public BasePlayerInterface getPlayer() {
        Heuristic h = new PointsHeuristic();
        BasePlayerInterface agent = factory.agent(agentConfig,h);
        agent.setName(playerName);
        return agent;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(factory.getClass().getSimpleName()).
                append(" ").append(Arrays.toString(agentConfig));
        return builder.toString();
    }
}
