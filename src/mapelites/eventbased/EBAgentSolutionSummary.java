package mapelites.eventbased;

import mapelites.core.summary.SummariseSolution;
import mapelites.interfaces.Solution;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public class EBAgentSolutionSummary implements SummariseSolution {
	@NotNull
	@Override
	public String summarise(@Nullable Solution solution) {
		if(solution instanceof EBAgentSolution) {
			EBAgentSolution s = (EBAgentSolution) solution;
			StringBuilder builder = new StringBuilder();

			builder.append("Agent: "+ Arrays.toString(s.getAgentConfig())).append("\n");
			builder.append("Heuristic: "+ Arrays.toString(s.getWeights())).append("\n");

			return builder.toString();
		}
		return "";
	}
}
