package gamesearch.players.samplers.db;

import com.google.gson.Gson;
import hyper.agents.factory.AgentFactorySpace;
import hyper.agents.factory.HeuristicAgentFactorySpace;
import statistics.StatsLoader;
import utils.loaders.LoadSearchSpace;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Map;

public class PlayersDBLoader {

	public static ArrayList<int[]> extractIntVector(ArrayList configTuples, int position){
		ArrayList<int[]> al = new ArrayList<>();
		for(Object o: configTuples){
			ArrayList oo = (ArrayList) o;
			int[] o1 = ((ArrayList<Double>) oo.get(position)).stream().mapToInt(a -> a.intValue()).toArray();
			al.add(o1);
		}
		return al;
	}

	public static ArrayList<double[]> extractDoubleVector(ArrayList configTuples, int position){
		ArrayList<double[]> al = new ArrayList<>();
		for(Object o: configTuples){
			ArrayList oo = (ArrayList) o;
			double[] o1 = ((ArrayList<Double>) oo.get(position)).stream().mapToDouble(a -> a.doubleValue()).toArray();
			al.add(o1);
		}
		return al;
	}

	public static double[] extractFirstValue(ArrayList configTuples, int position){
		double[] da = new double[configTuples.size()];
		for(int i=0; i<da.length; i++)
			da[i] = ((Double) ((ArrayList) configTuples.get(i)).get(position));
		return da;
	}

	public static PlayersDB loadPlayerDB(String dbFile){
		try (Reader r = new FileReader(dbFile)) {
			PlayersDB db = null;
			Gson parser = new Gson();
			Map<?,?> map = parser.fromJson(r,Map.class);

			String playerName = (String) map.get("name");
			String playerType = (String) map.get("agent");
			ArrayList<Object> configTuples = (ArrayList<Object>) map.get("data");

			ArrayList<int[]> agentConfig = extractIntVector(configTuples,0);
			ArrayList<double[]> behaviours = extractDoubleVector(configTuples,1);
			double[] performance = extractFirstValue(configTuples,2);

			AgentFactorySpace afs = LoadSearchSpace.loadFactorySpace(
					playerType,
					null,
					null);

			ArrayList<String> metricNames = (ArrayList<String>) map.get("metrics");
			ArrayList<String> metricCodes = new ArrayList<>();

			for(String name: metricNames)
				metricCodes.add(StatsLoader.pickCodeForStatistic(name));



			if(map.containsKey("heuristic") && map.containsKey("converter")){
				String heuristicType = (String) map.get("heuristic");
				String converterType = (String) map.get("converter");
				ArrayList<double[]> weights = extractDoubleVector(configTuples,3);

				db = new EventPlayersDB(
						playerName,
						playerType,
						(HeuristicAgentFactorySpace) afs,
						behaviours,
						agentConfig,
						weights,
						performance,
						metricCodes,
						heuristicType,
						converterType
				);
			}else{
				db = new PlayersDB(
						playerName,
						playerType,
						afs,
						behaviours,
						agentConfig,
						performance,
						metricCodes
				);
			}

			return db;
		}catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}


	public static void main(String[] argv){
		PlayersDB db = loadPlayerDB("agents/db/eb_id_db.json");
		System.out.println(db);
	}

}
