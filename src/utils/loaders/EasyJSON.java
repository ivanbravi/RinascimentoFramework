package utils.loaders;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class EasyJSON {

	public static JsonArray getArray(String filePath){
		try {
			BufferedReader br = new BufferedReader(new FileReader(filePath));
			JsonParser parser = new JsonParser();
			return parser.parse(br).getAsJsonArray();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static JsonObject getObject(String filePath){
		try {
			BufferedReader br = new BufferedReader(new FileReader(filePath));
			JsonParser parser = new JsonParser();
			return parser.parse(br).getAsJsonObject();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

}
