package mapelites;

import benchmarks.RinascimentoEnv;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import mapelites.eventbased.EBAgentSolutionSummary;
import log.LogGroup;
import mapelites.core.summary.SimpleSummary;
import mapelites.core.summary.SummariseSearch;
import mapelites.rainbow.MapEliteUISource;
import mapelites.search.EventBasedSearch;
import mapelites.search.PointBasedSearch;
import mapelites.search.SearchCreator;
import mapelites.search.StateFeatureBasedSearch;
import time.MilliTimer;
import ui.ConsoleUI;
import ui.elements.RectElement;
import ui.elements.RectSourced;
import utils.loaders.EasyJSON;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MAPElitesLauncher {

	// SEARCH
	private static Search s;
	private static SearchCreator sc;
	private static int searchBootIterations = 100;
	private static int searchTotalIterations = 2000;

	// UI
	private static String setup = "assets/_setup/ui.json";
	private static JsonObject setupParams;
	private static ArrayList<int[]> projections = new ArrayList<>();
	private static ArrayList<MapEliteUISource> sources = new ArrayList<>();
	private static SummariseSearch summary;
	private static RectElement summaryRect;

	//LOGGING
	private static LogGroup lg = new LogGroup("MAP-Elites - "+(new SimpleDateFormat("yy:MM:dd:HH:mm:ss")).format(new Date())+"/");

	public static void main(String[] args) {

		if(args.length<1) {
			System.out.println("Missing parameter config file!");
			return;
		}
		JsonObject mapArgs = EasyJSON.getObject(args[0]);

		RinascimentoEnv.setTHREADS(mapArgs.get("search/threads").getAsInt());
		searchBootIterations = mapArgs.get("search/bootiterations").getAsInt();
		searchTotalIterations = mapArgs.get("search/totaliterations").getAsInt();
		String searchType = mapArgs.get("search/type").getAsString();
		if(searchType.equals("EB")) {
			sc = new EventBasedSearch();
		}else if (searchType.equals("PB")){
			sc = new PointBasedSearch();
		}else if (searchType.equals("SB")){
			sc = new StateFeatureBasedSearch();
		}

		sc.init(mapArgs, lg);
		s = sc.create();
		summary = new SummariseSearch(new SimpleSummary(new EBAgentSolutionSummary()));

		createProjections(sc.behaviourNames.length);

		setupParams = EasyJSON.getObject(setup);

		runSearch();

		RinascimentoEnv.shutDown();
	}

	private static void createProjections(int l){
		for(int i=0; i<l; i++)
			for(int j=i+1; j<l; j++)
				projections.add(new int[]{j,i});
	}

	private static void runSearch(){
		String framePath  = "frames/";
		MilliTimer timer = new MilliTimer();
		int interactiveSteps = setupParams.get("search/interactivesteps").getAsInt();
		s.startInteractiveSearch(searchTotalIterations, searchBootIterations);

		setupUI();

		(new File("frames/")).mkdirs();

		timer.start();
		while(!s.searchIsOver()){
			s.interactiveSearch(interactiveSteps);
			showUI();
			ConsoleUI.UI.INSTANCE.saveFrame(framePath);
		}
		timer.stop();

		lg.add("timing", new String[]{
				"total: "+timer.seconds()+" seconds",
				"'time per eval': "+timer.seconds()/searchTotalIterations+" seconds/eval"
		});
		lg.add("summary",sc.summary().split("\\n"));
		lg.add("space",s.getBs().getSpace());
		lg.add("spaceHistory",s.getSolutionHistory());
		lg.add("spaceSize",s.getBs().getDimensions());

		lg.saveLog();
	}

	private static void showUI(){
		String summaryText = summary.summarise(s);
		summaryRect.update(summaryText);
		summary.setProgressSize(setupParams.get("ui/progress/length").getAsInt());

		for(MapEliteUISource s:sources){
			s.update();
		}

		ConsoleUI.UI.INSTANCE.draw();
	}

	private static void setupUI(){
		int r = 0;
		int c = 0;
		int pad = setupParams.get("ui/graphs/padding").getAsInt();
		int graphX = setupParams.get("ui/graphs/x").getAsInt();
		int graphY = setupParams.get("ui/graphs/y").getAsInt();
		int projectionSize = setupParams.get("ui/graphs/size").getAsInt();
		int projectionColumns = setupParams.get("ui/graphs/columns").getAsInt();

		RectSourced.Companion.setMinSymbol("x");

		for(int[] p: projections){
			int dim1 = p[0];
			int dim2 = p[1];
			MapEliteUISource source = new MapEliteUISource(dim1,dim2,s.getBs());
			ConsoleUI.UI.INSTANCE.sourcedGraph(
					graphX +c*(projectionSize+pad),
					graphY+r*(projectionSize+pad),
					s.getBs().getDimensions()[dim1],
					s.getBs().getDimensions()[dim2],
					source).alignCenter();
			sources.add(source);
			c++;
			if(c>=projectionColumns) {
				c = 0;
				r++;
			}
		}

		summaryRect = ConsoleUI.UI.INSTANCE.rect(
				setupParams.get("ui/summary/x").getAsInt(),
				setupParams.get("ui/summary/y").getAsInt(),
				setupParams.get("ui/summary/width").getAsInt(),
				setupParams.get("ui/summary/height").getAsInt(),
				""
		);

		ConsoleUI.UI.INSTANCE.rect(0,0,200,5, sc.summary());

		ConsoleUI.UI.INSTANCE.setFrameRate(15);
	}
}
