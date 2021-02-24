package mapelites.search;

import com.google.gson.JsonObject;
import hyper.agents.factory.HeuristicAgentFactory;
import hyper.agents.rhea.RHEAAgentFactory;
import hyper.utilities.CompleteAnnotatedSearchSpace;
import log.LogGroup;
import mapelites.FitnessFunction;
import mapelites.core.summary.SummariseSolution;
import mapelites.interfaces.SolutionSpace;
import mapelites.pointbased.PBSolutionSpace;
import mapelites.pointbased.PBSolutionSummary;

import java.util.Arrays;

public class PointBasedSearch extends SearchCreator {

    // Agent
    private String agentSearchSpace;
    private HeuristicAgentFactory agentSpace;

    public void init(JsonObject mapArgs, LogGroup lg) {
        super.init(mapArgs,lg);

        agentSearchSpace = mapArgs.get("PB/agentspace").getAsString();
        agentSpace = new RHEAAgentFactory();
        agentSpace.setSearchSpace(CompleteAnnotatedSearchSpace.load(agentSearchSpace));

        lg.add("agentSpace",agentSpace);
    }

    public HeuristicAgentFactory getAgentSpace() {
        return agentSpace;
    }

    @Override
    public SolutionSpace getSolutionSpace() {
        PBSolutionSpace ss = new PBSolutionSpace(agentSpace);
        ss.setAgentName(FitnessFunction.playerName);
        return ss;
    }

    @Override
    public SummariseSolution summariseSolution() {
        return new PBSolutionSummary();
    }

    @Override
    public String summary() {
        StringBuilder builder = new StringBuilder();
        builder.append(super.summary());
        builder.append("[agent space: "+agentSearchSpace+"]\n");
        return builder.toString();
    }

}
