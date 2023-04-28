package log;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public abstract class Logger implements LoggableReady {

	public static final String rootDirectory = "logs/";
	private String subpath = "";
	protected String fileName;
	private boolean isEnabled = true;

	public Logger(String fileName) {
		this.fileName = fileName;
	}

	public void log(Object o){
		if(isEnabled){
			logObject(o);
		}
	}

	protected abstract void reset();
	protected abstract void logObject(Object o);

	abstract Object logContent();

	public boolean save(){
		return saveAtom(this.logContent(),filePath());
	}

	public void setSubpath(String subpath){
		if(subpath.charAt(subpath.length()-1)!='/')
			subpath = subpath+"/";

		this.subpath=subpath;
	}

	public void enable(){
		isEnabled = true;
	}
	public void disable(){
		isEnabled = false;
	}

	protected String filePath(){
		return rootDirectory+subpath+fileName+logExtention();
	}

	public static String atomDestination(String path){
		return rootDirectory+path+logExtention();
	}

	public static String logExtention(){
		return ".json";
	}

	public static boolean saveAtom(Object o, String destinationPath){
		File f = new File(destinationPath);
		if(f!=null)
			if(f.getParentFile()!=null)
				f.getParentFile().mkdirs();

		if(o instanceof LoggableReady)
			((LoggableReady)o).jsonReady();

		try (FileWriter w = new FileWriter(f)){
			Gson writer = new GsonBuilder().enableComplexMapKeySerialization().setPrettyPrinting().create();
			writer.toJson(o,w);
		}catch (IOException e){
			e.printStackTrace();
			return false;
		}catch (Exception e){
			System.out.println("Unable to save object:\n"+o.toString());
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static void print(Object... objs){
		for(Object o: objs)
			System.out.println(o.toString());
	}

}


