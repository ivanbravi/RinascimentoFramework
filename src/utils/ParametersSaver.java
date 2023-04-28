package utils;

import com.google.gson.JsonArray;
import game.Parameters;
import gamesearch.ParametersFactory;
import hyper.utilities.CompleteAnnotatedSearchSpace;
import utils.gson.GsonUtils;
import utils.loaders.EasyJSON;

public class ParametersSaver {

	public static void main(String[] args) {
		if(args.length==0){
			System.out.println("\n[WARNING]: running with default parameters\n");
			args = new String[]{
					"/Users/ivanbravi/Desktop/GAME SEARCH/ParametersSaver/0/",
					"ActualGameSpace.json",
					"config.json",
					"parameters.json"

			};
		}
		String rootPath = args[0];
		String spacePath = rootPath+args[1];
		String configPath = rootPath+args[2];
		String outName = args[3];

		JsonArray configJson = EasyJSON.getArray(configPath);
		CompleteAnnotatedSearchSpace space = CompleteAnnotatedSearchSpace.load(spacePath);

		if(space==null){
			System.out.println("Remember the space json file may need to be cleaned: keep only the content of the \"gameSpace\" field.");
			return;
		}

		int[] configArray = GsonUtils.fromJsonArrayToIntArray(configJson);
		ParametersFactory factory = new ParametersFactory(space);
		Parameters gameParameters = factory.getParameters(configArray);
		Parameters.save(rootPath, outName, gameParameters);
	}
}
