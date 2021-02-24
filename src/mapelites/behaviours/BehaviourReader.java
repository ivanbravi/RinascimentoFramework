package mapelites.behaviours;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import utils.loaders.EasyJSON;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class BehaviourReader {

	JsonArray rootObject;
	int metricsCount = 0;

	public BehaviourReader(String path) {
		rootObject = EasyJSON.getArray(path);

		for(int i=0; i<size(); i++)
			if(getIsMetric(i))
				metricsCount++;

	}

	public int size(){
		return rootObject.size();
	}

	public int metricsCount(){
		return metricsCount;
	}

	public boolean getIsMetric(int index){
		return rootObject.get(index).getAsJsonObject().get("isMetric").getAsBoolean();
	}

	public double getMin(int index){
		return rootObject.get(index).getAsJsonObject().get("min").getAsDouble();
	}
	public double getMax(int index){
		return rootObject.get(index).getAsJsonObject().get("max").getAsDouble();
	}
	public int getBreaks(int index){
		return rootObject.get(index).getAsJsonObject().get("breaks").getAsInt();
	}
	public String getName(int index){
		return rootObject.get(index).getAsJsonObject().get("name").getAsString();
	}
	public boolean getSaveHistory(int index){
		return rootObject.get(index).getAsJsonObject().get("history").getAsBoolean();
	}

	public String getCode(int index){
		return rootObject.get(index).getAsJsonObject().get("code").getAsString();
	}

}
