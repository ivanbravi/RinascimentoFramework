package gamesearch;

import com.google.gson.JsonObject;
import game.Parameters;
import gamesearch.sampling.MutationExpanderSampler;
import gamesearch.sampling.ParametersGaussianSampler;
import gamesearch.sampling.ParametersSampler;
import gamesearch.sampling.ParametersUniformSampler;
import hyper.utilities.CompleteAnnotatedSearchSpace;
import log.LogGroup;
import utils.Pair;
import utils.loaders.EasyJSON;

import java.util.ArrayList;

public class NeighbourhoodTest {

	ParametersSampler sampler;
	int samples;
	RinascimentoGameEvaluator rge;
	ArrayList<Pair<int[], Double>> c;

	public static LogGroup lg = LogGroup.getGroupWithDatedFolder("NEIGHBOURHOOD");

	public NeighbourhoodTest(JsonObject params){
		String type = params.get("type").getAsString();
		samples = params.get("samples").getAsInt();

		rge = RinascimentoGameEvaluatorFactory.create(params, lg);
		CompleteAnnotatedSearchSpace gameSpace = (CompleteAnnotatedSearchSpace) rge.searchSpace();
		ParametersFactory gpf = new ParametersFactory(gameSpace);
		String gameFile = params.get("center").getAsString();
		Parameters p = Parameters.load(gameFile);

		if(type.equals("uniform")){
			sampler = new ParametersUniformSampler(gpf);
			lg.add("center",ParametersSpace.closestConfigIndices(gpf.getGameSpace(),p));
		}else if(type.equals("gaussian")) {
			double sigma = params.get("sigma").getAsDouble();
			sampler = new ParametersGaussianSampler(gpf, p, sigma);
			lg.add("center", ((ParametersGaussianSampler)sampler).centers());
		}else if(type.equals("expander")){
			sampler = new MutationExpanderSampler(gpf, p, samples);
			lg.add("center",ParametersSpace.closestConfigIndices(gpf.getGameSpace(),p));
		}

		lg.add("inputParams", params);
		lg.add("ActualGameSpace", rge.searchSpace());
	}

	public void run(){
		c = new ArrayList<>();
		for(int i=0; i<samples; i++){
			int[] config = sampler.sample();
			double eval = rge.evaluate(config);
			c.add(new Pair(config,eval));
		}
		shutdown();
	}

	private void shutdown(){
		lg.add("neighbourhood_sampling_out",c);
		lg.saveLog();
	}

	public static void main(String[] args) {
		if(args.length==0) {
			args = new String[]{"assets/game_search/sampling_config.json"};
			System.out.println("[WARNING]: No arguments! Running default test from \""+args[0]+"\"");
		}

		JsonObject params = EasyJSON.getObject(args[0]);
		NeighbourhoodTest test = new NeighbourhoodTest(params);

		Runtime.getRuntime().addShutdownHook(new Thread(() -> test.shutdown()));

		test.run();
	}
}
