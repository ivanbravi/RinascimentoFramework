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
import hyper.agents.factory.HeuristicAgentFactorySpace;
import hyper.agents.featurebased.FeatureBasedAgentFactory;
import hyper.agents.featurebased.FeatureBasedFixedAgentFactory;
import hyper.agents.mcts.MCTSAgentFactorySpace;
import hyper.agents.meta.MetaAgentFactorySpace;
import hyper.agents.mixed.MixerFactorySpaceSpace;
import hyper.agents.rhea.RHEAAgentFactorySpace;
import hyper.agents.seededrhea.SeededRHEAAgentFactorySpace;
import hyper.utilities.CompleteAnnotatedSearchSpace;
import log.heuristics.*;
import log.heuristics.nn.*;
import log.heuristics.nn.decoder.MultilayerNNDecoder;
import log.heuristics.nn.decoder.PerceptronDecoder;
import log.heuristics.nn.decoder.SeedlessSparseMultilayerDecoder;
import log.heuristics.nn.decoder.SparseMultilayerDecoder;
import log.heuristics.nn.encog.creator.MultilayerCreator;
import log.heuristics.nn.encog.creator.PerceptronCreator;
import log.heuristics.nn.encog.creator.SeedlessSparseMultilayerCreator;
import log.heuristics.nn.encog.creator.SparseMultilayerCreator;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.Arrays;
import java.util.regex.Pattern;

public class LoadSearchSpace {

	private LoadSearchSpace(){}

	private static HeuristicAgentFactorySpace loadSimpleFactorySpace(String type, String paramFile){
		HeuristicAgentFactorySpace afs = null;

		if(type.equals("RHEA")){
			afs = new RHEAAgentFactorySpace();
		} else if(type.equals("SeededRHEA") || type.equals("SRHEA")){
			afs = new SeededRHEAAgentFactorySpace();
		} else if(type.equals("MCTS")){
			afs = new MCTSAgentFactorySpace();
		}

		afs.setHeuristic(new PointsHeuristic()).
				setSearchSpace(CompleteAnnotatedSearchSpace.load(paramFile));

		return afs;
	}

	public static String defaultSpace(String type){
		if(type.equals("RHEA")){
			return "agents/RHEAParams.json";
		} else if(type.equals("SeededRHEA") || type.equals("SRHEA")){
			return "agents/SeededRHEAParams.json";
		} else if(type.equals("MCTS")){
			return "agents/MCTSParams.json";
		}
		return null;
	}

	public static WeightedHeuristic decodeWeightedHeuristicWithWeights(String type,int size, double[] weights){
		WeightedHeuristic wh = decodeWeightedHeuristic(type,size);
		wh.setWeights(weights);
		return wh;
	}

	public static WeightedHeuristic decodeWeightedHeuristic(String type,int size){
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
				if(code.contains("*")){
					SeedlessSparseMultilayerDecoder decoder = new SeedlessSparseMultilayerDecoder(code);
					return new HyperNNHeuristic(new SeedlessSparseMultilayerCreator(decoder));
				}else {
					SparseMultilayerDecoder decoder = new SparseMultilayerDecoder(code);
					creator = new SparseMultilayerCreator(decoder);
				}
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

	private static AgentFactorySpace loadFacorySpaceMeta(String type){
		/*			META AGENT			*/
		String metaType = type.replaceAll("META-", "");
		String metaParamFile = defaultSpace(metaType);
		AgentFactorySpace meta = loadSimpleFactorySpace(metaType, metaParamFile);
		return new MetaAgentFactorySpace(meta);
	}

	private static AgentFactorySpace loadFactorySpaceMix(String type){
		// e.g.: MIX-0.8-MCTS
		String deMix = type.substring(4);
		String mixed_agent = deMix.substring(deMix.indexOf("-")+1);
		double weight = Double.parseDouble(deMix.substring(0,deMix.indexOf("-")));
		//here check if default mixed_agent needs to be further filtered
		AgentFactorySpace mixed_afs = loadFactorySpace(mixed_agent,defaultSpace(mixed_agent),null);
		return new MixerFactorySpaceSpace(mixed_afs,weight);
	}

	private static AgentFactorySpace loadFactorySpaceEventBased(String type){
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
		HeuristicAgentFactorySpace agentSpace = loadSimpleFactorySpace(agentType,defaultSpace(agentType));
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

	private static AgentFactorySpace loadFactorySpaceStateBased(String type, Parameters p){
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
		HeuristicAgentFactorySpace agentSpace = loadSimpleFactorySpace(agentType,defaultSpace(agentType));
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

	public static boolean isSimpleFactory(String type){
		return type.equals("RHEA") || type.equals("SeededRHEA") || type.equals("SRHEA") || type.equals("MCTS");
	}

	public static AgentFactorySpace loadFactorySpace(String type, String paramFile, Parameters p){

		if(isSimpleFactory(type)){
			if(paramFile==null)
				return loadSimpleFactorySpace(type,defaultSpace(type));
			else
				return loadSimpleFactorySpace(type,paramFile);
		} else if(isTypeMeta(type)) { // META-
			return loadFacorySpaceMeta(type);
		}else if(isTypeMixture(type)){ // MIX-
			return loadFactorySpaceMix(type);
		}else if(isTypeEventBased(type)){ // EHB-
			return loadFactorySpaceEventBased(type);
		}else if(isTypeStateBased(type)){ // SVB-
			return loadFactorySpaceStateBased(type,p);
		}

		System.out.println("WRONG AGENT TYPE: "+type);
		return null;
	}

	public static boolean isFreeWeightsHeuristicAgent(String type){ return type.equals("EHB-any") || type.equals("SVB-any");}

	public static boolean isTypeEventBased(String type){
		return checkIfIsSubtype(type, "EHB");
	}

	public static boolean isTypeStateBased(String type){
		return checkIfIsSubtype(type, "SVB");
	}

	public static boolean isTypeMixture(String type){
		return checkIfIsSubtype(type, "MIX");
	}

	public static boolean isTypeMeta(String type){
		return checkIfIsSubtype(type, "META");
	}

	private static boolean checkIfIsSubtype(String type, String wantedSubtype){
		if(!isSubTyped(type))
			return false;
		if(getSubType(type).equals(wantedSubtype)){
			return true;
		}
		return false;
	}

	private static boolean isSubTyped(String type){
		return type.contains("-");
	}

	private static String getSubType(String type){
		return type.split("-")[0];
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

	public static int[] rangeToIndex(double[] w, AnnotatedSearchSpace ass){
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

	private static boolean isPositiveIntNumber(String s){
		Pattern p = Pattern.compile("\\d+");
		return p.matcher(s).matches();
	}

	public static void main(String[] args){
		LoadSearchSpace lss = new LoadSearchSpace();

		// <DECODE AGENT SEARCH SPACE>
//		String code = "EHB-simple-RHEA-nn,TANH,5,(3),2,1,[0]-11";
//		AgentFactorySpace afs = lss.loadFactorySpaceEventBased(code);
//		System.out.println(afs);
//		int[] rndPoint = afs.randomPoint(new Random(0));
//		BasePlayerInterface p = afs.agent(rndPoint);
//		System.out.println(p);

		// <DECODE WEIGHTED HEURISTIC SEARCH SPACE>
		String type = "nn,TANH,192,(5184),30,1,[*]";
		Vectoriser v = LoadSearchSpace.decodeVectoriser("SSPP", Parameters.load("assets/defaultx2/"));
		WeightedHeuristic wh = lss.decodeWeightedHeuristic(type, v.size());
		System.out.println(wh.toString());

		// <READ ANN WEIGHTS FILE>
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
