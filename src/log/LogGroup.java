package log;

import utils.Pair;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class LogGroup {

	private String folder;

	private ArrayList<LogGroup> groups = new ArrayList<>();
	private ArrayList<Logger> loggers = new ArrayList<>();
	private ArrayList<Pair<String,Object>> atoms = new ArrayList<>();

	public LogGroup(String folder){
		this.folder = folder;
	}

	public void add(LogGroup g){
		if(g!=null)
			groups.add(g);
	}

	public String folderPath(){
		return this.folder;
	}

	public void add(Logger l){
		if(l!=null)
			loggers.add(l);
	}

	public void add(String name, Object atom){
		if(atom!=null)
			atoms.add(new Pair<>(name,atom));
	}

	public boolean saveLog(){
		boolean r = true;

		for(LogGroup g: groups){
			String  originalFolder = g.folder;
			g.folder = this.folder+g.folder;
			r &= g.saveLog();
			g.folder = originalFolder;
		}

		for(Logger l: loggers){
			l.setSubpath(folder);
			r &= l.save();
		}

		for(Pair<String,Object> a: atoms){
			String path = Logger.atomDestination(folder+a.first());
			r &= Logger.saveAtom(a.second(),path);
		}

		return r;
	}

	public static LogGroup getGroupWithDatedFolder(String name){
		return new LogGroup(name+" - "+
				(new SimpleDateFormat("yy-MM-dd-HH-mm-ss")).format(new Date())+"/");
	}

	public static LogGroup getGroupWithFolder(String name){
		return new LogGroup(name+"/");
	}

}
