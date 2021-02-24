package mapelites.search;

import com.google.gson.JsonObject;
import game.log.converters.EventIdConverter;
import log.LogGroup;
import log.heuristics.LinearHeuristic;
import log.heuristics.PolynomialHeuristic;
import log.heuristics.WeightedHeuristic;
import mapelites.FitnessFunction;
import mapelites.core.summary.SummariseSolution;
import mapelites.eventbased.EBAgentSolutionSpace;
import mapelites.eventbased.EBAgentSolutionSummary;
import mapelites.interfaces.SolutionSpace;
import utils.loaders.LoadSearchSpace;

import java.util.Arrays;

public class EventBasedSearch extends PointBasedSearch {

    // Heuristic
    private String heuristicType;
    private String converterType;
    private WeightedHeuristic h;

    @Override
    public void init(JsonObject mapArgs, LogGroup lg) {
        super.init(mapArgs,lg);
        // EB/heuristic [linear poly,2 poly,3 nn,TANH,5 nn,TANH,18,5,2]
        // EB/converter [simple, id]
        heuristicType = mapArgs.get("EB/heuristic").getAsString();
        converterType = mapArgs.get("EB/converter").getAsString();

        setConverter(LoadSearchSpace.loadConverter(converterType));
        h = (new LoadSearchSpace()).decodeWeightedHeuristic(heuristicType, getConverter().idCount());

        lg.add("heuristicSpace",h);
    }

    @Override
    public SummariseSolution summariseSolution() {
        return new EBAgentSolutionSummary();
    }

    @Override
    public SolutionSpace getSolutionSpace() {
        EBAgentSolutionSpace ss = new EBAgentSolutionSpace(getAgentSpace(),h,getConverter());
        ss.setAgentName(FitnessFunction.playerName);
        return ss;
    }

    @Override
    public String summary() {
        StringBuilder builder = new StringBuilder();
        builder.append(super.summary());
        builder.append("[heuristic type: "+heuristicType+"]\n");
        return builder.toString();
    }

}
