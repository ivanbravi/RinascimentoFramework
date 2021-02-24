package mapelites.pointbased;

import hyper.agents.factory.HeuristicAgentFactory;
import mapelites.interfaces.Solution;
import org.jetbrains.annotations.NotNull;
import java.util.Arrays;
import java.util.Random;

public class PBSolutionSpace implements mapelites.interfaces.SolutionSpace {
    private String agentName;
    private Random rnd = new Random();
    private HeuristicAgentFactory agentSpace;

    public PBSolutionSpace(HeuristicAgentFactory agentSpace){
        this.agentSpace = agentSpace;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    @NotNull
    @Override
    public Solution crossover(@NotNull Solution solution, @NotNull Solution solution1) {
        return null;
    }

    @NotNull
    @Override
    public Solution mutate(@NotNull Solution solution) {
        if(!(solution instanceof PBAgentSolution))
            return null;
        PBAgentSolution currSolution = (PBAgentSolution) solution;
        int[] rndAgent = currSolution.getAgentConfig();
        int[] mtdAgent = mutateAgent(rndAgent);
        return new PBAgentSolution(agentName,mtdAgent,agentSpace);
    }

    @NotNull
    @Override
    public Solution rndPoint() {
        return new PBAgentSolution(agentName,rndAgent(),agentSpace);
    }

    private int[] mutateAgent(int[] currAgent){
        int[] mutation = Arrays.copyOf(currAgent,currAgent.length);
        int mPosition = rnd.nextInt(currAgent.length);
        int mValue = rnd.nextInt(agentSpace.getSearchSpace().nValues(mPosition));
        mutation[mPosition] = mValue;
        return mutation;
    }

    private int[] rndAgent(){
        int d = agentSpace.getSearchSpace().nDims();
        int[] config = new int[d];

        for(int i=0; i<d; i++)
            config[i] = rnd.nextInt(agentSpace.getSearchSpace().nValues(i));

        return config;
    }

}
