package hyper.utilities;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import evodef.AnnotatedSearchSpace;
import ntbea.params.Param;
import utils.ParamsJsonDeserialiser;

import java.io.*;
import java.util.Arrays;

public class CompleteAnnotatedSearchSpace implements AnnotatedSearchSpace {

	protected Param[] params;
	protected int[] dimensions;

	@Override
	public Param[] getParams(){
		return params;
	}

	@Override
	public int nDims() {
		return dimensions.length;
	}

	@Override
	public int nValues(int i) {
		return dimensions[i];
	}

	@Override
	public String toString() {
		return paramsToString()+"\n"+ Arrays.toString(dimensions);
	}

	private String paramsToString(){
		String[] names = new String[params.length];
		for(int i=0; i<names.length; i++)
			names[i] = getParams()[i].getName();
		return Arrays.toString(names).replaceAll("\\[","").replaceAll("\\]","");
	}

	public static boolean save(String path, CompleteAnnotatedSearchSpace cass){

		try(Writer w = new FileWriter(path)){
			Gson writer = new GsonBuilder().setPrettyPrinting().create();
			writer.toJson(cass, w);
		}catch (IOException e){
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public static CompleteAnnotatedSearchSpace load(String path){
		CompleteAnnotatedSearchSpace read = null;
		try (Reader r = new FileReader(path)) {
			Gson parser = new GsonBuilder()
					.registerTypeAdapter(Param.class, new ParamsJsonDeserialiser())
					.create();
			read = parser.fromJson(r, CompleteAnnotatedSearchSpace.class);
		}catch (Exception e){
			e.printStackTrace();
		}
		return read;
	}

}
