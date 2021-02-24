package mapelites.pointbased;

import mapelites.core.summary.SummariseSolution;
import mapelites.interfaces.Solution;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public class PBSolutionSummary implements SummariseSolution {
    @NotNull
    @Override
    public String summarise(@Nullable Solution solution) {
        if(solution instanceof PBAgentSolution) {
            PBAgentSolution s = (PBAgentSolution) solution;
            StringBuilder builder = new StringBuilder();

            builder.append("Agent: "+ Arrays.toString(s.getAgentConfig())).append("\n");

            return builder.toString();
        }
        return "";
    }
}
