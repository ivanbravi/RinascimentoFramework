package utils.loaders;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import evodef.AnnotatedSearchSpace;
import game.Parameters;
import game.heuristics.PointsHeuristic;
import game.log.converters.EventIdConverter;
import game.log.converters.IdentityConverter;
import game.log.converters.SimpleConverter;
import game.state.vectorise.*;
import hyper.agents.eventbased.*;
import hyper.agents.factory.AgentFactorySpace;
import hyper.agents.factory.HeuristicAgentFactory;
import hyper.agents.featurebased.FeatureBasedAgentFactory;
import hyper.agents.featurebased.FeatureBasedFixedAgentFactory;
import hyper.agents.mcts.MCTSAgentFactory;
import hyper.agents.meta.MetaAgentFactory;
import hyper.agents.mixed.MixerFactorySpace;
import hyper.agents.rhea.RHEAAgentFactory;
import hyper.agents.seededrhea.SeededRHEAAgentFactory;
import hyper.utilities.CompleteAnnotatedSearchSpace;
import log.heuristics.LinearHeuristic;
import log.heuristics.NNHeuristic;
import log.heuristics.PolynomialHeuristic;
import log.heuristics.WeightedHeuristic;
import log.heuristics.nn.*;
import log.heuristics.nn.decoder.MultilayerNNDecoder;
import log.heuristics.nn.decoder.PerceptronDecoder;
import log.heuristics.nn.decoder.SparseMultilayerDecoder;
import log.heuristics.nn.encog.creator.MultilayerCreator;
import log.heuristics.nn.encog.creator.PerceptronCreator;
import log.heuristics.nn.encog.creator.SparseMultilayerCreator;
import players.BasePlayerInterface;
import sun.management.Agent;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.Arrays;
import java.util.Random;
import java.util.regex.Pattern;

public class LoadSearchSpace {

	public LoadSearchSpace(){

	}

	private HeuristicAgentFactory loadSimpleFactorySpace(String type, String paramFile){
		HeuristicAgentFactory afs = null;

		if(type.equals("RHEA")){
			afs = new RHEAAgentFactory();
		} else if(type.equals("SeededRHEA") || type.equals("SRHEA")){
			afs = new SeededRHEAAgentFactory();
		} else if(type.equals("MCTS")){
			afs = new MCTSAgentFactory();
		}

		afs.setHeuristic(new PointsHeuristic()).
				setSearchSpace(CompleteAnnotatedSearchSpace.load(paramFile));

		return afs;
	}

	public String defaultSpace(String type){
		if(type.equals("RHEA")){
			return "agents/RHEAParams.json";
		} else if(type.equals("SeededRHEA") || type.equals("SRHEA")){
			return "agents/SeededRHEAParams.json";
		} else if(type.equals("MCTS")){
			return "agents/MCTSParams.json";
		}
		return null;
	}

	public WeightedHeuristic decodeWeightedHeuristic(String type,int size){
		if(type.equals("linear")){
			return new LinearHeuristic(size);
		}else if(type.split(",")[0].equalsIgnoreCase("poly")){
			int order = Integer.parseInt(type.split(",")[1]);
			return new PolynomialHeuristic(size,order);
		}else if(type.split(",")[0].equalsIgnoreCase("nn")){
			String code = type.substring(type.indexOf(",")+1);
			NNCreator creator = null;
			if(code.split(",").length==2){
				PerceptronDecoder decoder = new PerceptronDecoder(code);
				creator = new PerceptronCreator(decoder);
			}else if(code.contains("(") && code.contains("[")){
				SparseMultilayerDecoder decoder = new SparseMultilayerDecoder(code);
				creator = new SparseMultilayerCreator(decoder);
			}else{
				MultilayerNNDecoder decoder = new MultilayerNNDecoder(code);
				creator =  new MultilayerCreator(decoder);
			}
			return new NNHeuristic(creator);
		}
		return null;
	}

	public static EventIdConverter loadConverter(String converter){
		if(converter.equalsIgnoreCase("Simple")){
			return new SimpleConverter();
		}
		return new IdentityConverter();
	}

	private AgentFactorySpace loadFacorySpaceMeta(String type){
		/*			META AGENT			*/
		String metaType = type.replaceAll("META-", "");
		String metaParamFile = defaultSpace(metaType);
		AgentFactorySpace meta = loadSimpleFactorySpace(metaType, metaParamFile);
		return new MetaAgentFactory(meta);
	}

	private AgentFactorySpace loadFactorySpaceMix(String type){
		// e.g.: MIX-0.8-MCTS
		String deMix = type.substring(4);
		String mixed_agent = deMix.substring(deMix.indexOf("-")+1);
		double weight = Double.parseDouble(deMix.substring(0,deMix.indexOf("-")));
		//here check if default mixed_agent needs to be further filtered
		AgentFactorySpace mixed_afs = loadFactorySpace(mixed_agent,defaultSpace(mixed_agent),null);
		return new MixerFactorySpace(mixed_afs,weight);
	}

	private AgentFactorySpace loadFactorySpaceEventBased(String type){
		/*			EVENT-BASED HEURISTIC AGENT			*/
		// format: EHB-CONVERTER-TYPE-HEURISTIC-SAMPLING
		// e.g.: EHB-simple-RHEA-linear-11
		// e.g.: EHB-id-RHEA-poly,2-11
		// e.g.: EHB-simple-RHEA-nn,TANH,5-pesi
		// e.g.: EHB-id-RHEA-nn,TANH,18,5,1-pesi
		// e.g.: EHB-simple-RHEA-nn,TANH,5-11
		// e.g.: EHB-id-RHEA-nn,TANH,18,5,1-11
		String[] splits = type.split("-");
		String converterType = splits[1];
		String agentType = splits[2];
		String heuristicType = splits[3];
		String sampling = splits[4];
		boolean isWeightSpace = isPositiveIntNumber(sampling);

		EventIdConverter converter = loadConverter(converterType);
		HeuristicAgentFactory agentSpace = loadSimpleFactorySpace(agentType,defaultSpace(agentType));
		WeightedHeuristic wh = decodeWeightedHeuristic(heuristicType, converter.idCount());
		CompleteAnnotatedSearchSpace heuristicSpace;
		EventBasedAgentFactory afs;

		if(isWeightSpace) {
			heuristicSpace = new WeightsSearchSpace(wh.dimensionality(), Integer.parseInt(sampling));
			afs = new EventBasedAgentFactory(agentSpace,wh,converter);
		}else{
			double[] weights = loadWeightsArray(sampling+".json");
			if(wh.dimensionality() + agentSpace.getSearchSpace().nDims() ==  weights.length){
				double[] agentWeights = Arrays.copyOf(weights, agentSpace.getSearchSpace().nDims());
				weights = Arrays.copyOfRange(weights, agentSpace.getSearchSpace().nDims(), weights.length);
				int[] config = rangeToIndex(agentWeights, agentSpace.getSearchSpace());
				afs = new EventBasedFixedAgentFactory(agentSpace,config, wh, converter);
			}else{
				afs = new EventBasedAgentFactory(agentSpace,wh,converter);
			}
			heuristicSpace = new SinglePointWeightsBasedSpace(weights);
		}

		afs.setSearchSpace(new CombinedSearchSpace(new CompleteAnnotatedSearchSpace[]{
				CompleteAnnotatedSearchSpace.load(defaultSpace(agentType)),
				heuristicSpace}
		));
		afs.setAgentAndHeuristicType(agentType,heuristicType,converterType);
		return afs;
	}

	private AgentFactorySpace loadFactorySpaceStateBased(String type, Parameters p){
		/*			STATE-BASED HEURISTIC AGENT			*/
		// format: SVB-VECTORISER-TYPE-HEURISTIC-SAMPLING
		// e.g.: SVB-S-RHEA-linear-11
		// e.g.: SVB-P-RHEA-poly,2-11
		// e.g.: SVB-SP-RHEA-nn,TANH,5-pesi
		// e.g.: SVB-SPP-RHEA-nn,TANH,18,30,1-pesi
		// e.g.: SVB-SSPP-RHEA-nn,TANH,18,30,1-pesi

		String[] splits = type.split("-");
		String vectoriserCode = splits[1];
		String agentType = splits[2];
		String heuristicType = splits[3];
		String sampling = splits[4];
		boolean isWeightSpace = isPositiveIntNumber(sampling);

		Vectoriser v = decodeVectoriser(vectoriserCode,p);
		HeuristicAgentFactory agentSpace = loadSimpleFactorySpace(agentType,defaultSpace(agentType));
		WeightedHeuristic wh = decodeWeightedHeuristic(heuristicType, v.size());
		CompleteAnnotatedSearchSpace heuristicSpace;
		FeatureBasedAgentFactory afs;

		if(isWeightSpace) {
			heuristicSpace = new WeightsSearchSpace(wh.dimensionality(), Integer.parseInt(sampling));
			afs = new FeatureBasedAgentFactory(agentSpace,wh,v);
		}else{
			double[] weights = loadWeightsArray(sampling+".json");
			if(wh.dimensionality() + agentSpace.getSearchSpace().nDims() ==  weights.length){
				double[] agentWeights = Arrays.copyOf(weights, agentSpace.getSearchSpace().nDims());
				weights = Arrays.copyOfRange(weights, agentSpace.getSearchSpace().nDims(), weights.length);
				int[] config = rangeToIndex(agentWeights, agentSpace.getSearchSpace());
				afs = new FeatureBasedFixedAgentFactory(agentSpace,config, wh, v);
			}else{
				afs = new FeatureBasedAgentFactory(agentSpace,wh,v);
			}
			heuristicSpace = new SinglePointWeightsBasedSpace(weights);
		}

		afs.setSearchSpace(new CombinedSearchSpace(new CompleteAnnotatedSearchSpace[]{
				CompleteAnnotatedSearchSpace.load(defaultSpace(agentType)),
				heuristicSpace}
		));
		afs.setAgentAndHeuristicType(agentType,heuristicType,vectoriserCode);
		return afs;
	}

	public AgentFactorySpace loadFactorySpace(String type, String paramFile, Parameters p){

		String subType = type.split("-")[0];

		if(type.equals("RHEA") || type.equals("SeededRHEA") || type.equals("SRHEA") || type.equals("MCTS")){
			return loadSimpleFactorySpace(type,paramFile);
		} else if(type.substring(0,Math.min(type.length(),5)).equals("META-")) {
			return loadFacorySpaceMeta(type);
		}else if(subType.equals("MIX")){
			return loadFactorySpaceMix(type);
		}else if(subType.equals("EHB")){
			return loadFactorySpaceEventBased(type);
		}else if(subType.equals("SVB")){
			return loadFactorySpaceStateBased(type,p);
		}

		System.out.println("WRONG AGENT TYPE: "+type);
		return null;
	}

	public static Vectoriser decodeVectoriser(String vectoriserCode, Parameters p){
		Vectoriser v;
		switch (vectoriserCode){
			case "P":   {v = new VectorisePlayer(p);}break;
			case "PP":  {v = new AllPlayersVectoriser(p);} break;
			case "S":   {v = new VectoriseState(p);}break;
			case "SS":  {v = new VectoriseStateDeep(p);}break;
			case "SP":  {v = new PlayerStateVectoriser(new VectorisePlayer(p), new VectoriseState(p));}break;
			case "SSP": {v = new PlayerStateVectoriser(new VectorisePlayer(p), new VectoriseStateDeep(p));}break;
			case "SPP": {v = new PlayerStateVectoriser(new AllPlayersVectoriser(p), new VectoriseState(p));}break;
			case "SSPP":{v = new PlayerStateVectoriser(new AllPlayersVectoriser(p), new VectoriseStateDeep(p));}break;
			default:throw  new RuntimeException("Unrecognised Vectoriser.");
		}
		return v;
	}

	public int[] rangeToIndex(double[] w, AnnotatedSearchSpace ass){
		if(w.length!=ass.nDims())
			return null;
		int[] i = new int[w.length];

		for(int wi=0; wi<w.length; wi++) {
			double filteredW = Math.max(0, Math.min(1, w[wi]));
			int size = ass.nValues(wi);
			i[wi] = Math.min(size-1, (int)(filteredW*size));
		}
		return i;
	}

	public static double[] loadWeightsArray(String file){
		double[] weights = null;
		try (Reader r = new FileReader(file)) {
			Gson parser = new Gson();
			weights = parser.fromJson(r, double[].class);
		}catch (Exception e){
			e.printStackTrace();
		}
		return weights;
	}

	public static void saveWeightsArray(String path, double[] weights){
		try (Writer w = new FileWriter(path)){
			Gson writer = new GsonBuilder().setPrettyPrinting().create();
			writer.toJson(weights, w);
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	private boolean isPositiveIntNumber(String s){
		Pattern p = Pattern.compile("\\d+");
		return p.matcher(s).matches();
	}

	public static void main(String[] args){
		LoadSearchSpace lss = new LoadSearchSpace();
		AgentFactorySpace afs = lss.loadFactorySpaceEventBased("EHB-simple-RHEA-nn,TANH,5,(3),2,1,[0]-11");
		System.out.println(afs);
		int[] rndPoint = afs.randomPoint(new Random(0));
		BasePlayerInterface p = afs.agent(rndPoint);
		System.out.println(p);

//		Random rnd = new Random();
//		String file = "agents/nn-106.json";
//		int size = 106;
//		double[] ws = new double[size];
//		for(int i=0; i<size; i++)
//			ws[i] = rnd.nextDouble()*2-1;
//		saveWeightsArray(file,ws);
//		double [] w = loadWeightsArray(file);
//		System.out.println(Arrays.toString(w));
	}

}
