package utils;

import com.google.gson.*;
import ntbea.params.BooleanParam;
import ntbea.params.DoubleParam;
import ntbea.params.Param;

import java.lang.reflect.Type;

public class ParamsJsonDeserialiser implements JsonDeserializer {
	@Override
	public Object deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

		JsonArray array = (JsonArray) jsonElement.getAsJsonObject().get("a");
		JsonPrimitive name = (JsonPrimitive) jsonElement.getAsJsonObject().get("name");
		JsonPrimitive firstElement = (JsonPrimitive) array.get(0);
		Param read = null;
		if(firstElement.isBoolean()){
			read = readBooleanArray(array,name);
		}else if(firstElement.isNumber()){
			read = readNumberArray(array,name);
		}else if(firstElement.isString()){
			read = readStringArray(array,name);
		}

		return read;
	}

	private Param readBooleanArray(JsonArray a, JsonPrimitive name){
		boolean[] values = new boolean[a.size()];
		for(int i=0; i<values.length; i++){
			values[i] = a.get(i).getAsBoolean();
		}
		return new BooleanParam().setArray(values).setName(name.getAsString());
	}

	private Param readNumberArray(JsonArray a, JsonPrimitive name){
		double[] values = new double[a.size()];
		for(int i=0; i<values.length; i++){
			values[i] = a.get(i).getAsDouble();
		}
		return new DoubleParam().setArray(values).setName(name.getAsString());
	}

	private Param readStringArray(JsonArray a, JsonPrimitive name){
		String[] values = new String[a.size()];
		for(int i=0; i<values.length; i++){
			values[i] = a.get(i).getAsString();
		}
		//return new StringParam.setArray(values).setName(name.getAsString());
		return null;
	}

}
