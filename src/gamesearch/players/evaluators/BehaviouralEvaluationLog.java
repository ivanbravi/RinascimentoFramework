package gamesearch.players.evaluators;

public class BehaviouralEvaluationLog {

	String experimentalData;
	String filteredData;
	double preFilterEvaluation;
	double filterEvaluation;

	public BehaviouralEvaluationLog(String experimentalData, String filteredData, double preFilterEvaluation, double filterEvaluation){
		this.experimentalData = experimentalData;
		this.filteredData = filteredData;
		this.preFilterEvaluation = preFilterEvaluation;
		this.filterEvaluation = filterEvaluation;
	}

	@Override
	public String toString() {
		return
				"-------------------- ORIGINAL DATA --------------------\n"+
				experimentalData+
				"-------------------- FILTERED DATA --------------------\n"+
				filteredData+
				"--------------------- EVALUATION ---------------------\n"+
				"DIFF( expData,target) = "+preFilterEvaluation+"\n"+
				"DIFF(filtered,target) = "+filterEvaluation+"\n";
	}
}
