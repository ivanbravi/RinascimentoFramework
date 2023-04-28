package utils.gson;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

public class GsonUtils {

	public static int[] fromJsonArrayToIntArray(JsonArray data){
		int[] ret = new int[data.size()];
		int i=0;
		for(JsonElement e: data)
			 ret[i++] = e.getAsInt();
		return ret;
	}

	public static double[] fromJsonArrayToDoubleArray(JsonArray data){
		double[] ret = new double[data.size()];
		int i=0;
		for(JsonElement e: data)
			ret[i++] = e.getAsDouble();
		return ret;
	}

	public static String[] fromJsonArrayToStringArray(JsonArray data){
		String[] ret = new String[data.size()];
		int i=0;
		for(JsonElement e: data)
			ret[i++] = e.getAsString();
		return ret;
	}

}
