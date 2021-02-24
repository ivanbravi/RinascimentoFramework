package log;

import utils.Pair;

import java.util.ArrayList;

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


}
