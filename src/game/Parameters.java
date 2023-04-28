package game;


import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.Arrays;
import com.google.gson.*;
import utils.gson.GsonUtils;
import utils.loaders.EasyJSON;

/*
* The object also provides the default destinations and names for decks and nobles files
* */

public class Parameters{

	public int playerCount = 4;
	public int suitCount = 5;
	public int[] cardSuitUsed = {1,1,1,1,1};
	public int[] coinSuitUsed = {1,1,1,1,1};
	public int coinCount = 7;
	public int goldCount = 5;
	public int maxCoins = 10;
	public int deckCount = 3;
	public int cardsOnTableCount = 4;
	public int noblesCount = playerCount+1;
	public int endGameScore = 15;
	public int maxReserveCards = 3;
	public int pickDifferentCount = 3;
	public int pickDifferentAmount = 1;
	public int pickSameAmount = 2;
	public int pickSameMinCoins= 4;

	private transient String path;

	private Parameters(){

	}

	public Parameters clone() {
		Parameters new_p = new Parameters();

		new_p.playerCount 			= this.playerCount;
		new_p.suitCount				= this.suitCount;
		new_p.cardSuitUsed 			= Arrays.copyOf(this.cardSuitUsed,this.cardSuitUsed.length);
		new_p.coinSuitUsed 			= Arrays.copyOf(this.coinSuitUsed,this.coinSuitUsed.length);
		new_p.coinCount 			= this.coinCount;
		new_p.goldCount 			= this.goldCount;
		new_p.maxCoins 				= this.maxCoins;
		new_p.deckCount 			= this.deckCount;
		new_p.cardsOnTableCount 	= this.cardsOnTableCount;
		new_p.noblesCount 			= this.noblesCount;
		new_p.endGameScore 			= this.endGameScore;
		new_p.maxReserveCards 		= this.maxReserveCards;
		new_p.pickDifferentCount	= this.pickDifferentCount;
		new_p.pickDifferentAmount	= this.pickDifferentAmount;
		new_p.pickSameAmount		= this.pickSameAmount;
		new_p.pickSameMinCoins		= this.pickSameMinCoins;

		return new_p;
	}

	public static Parameters defaultParameters() {
		Parameters dParams = Parameters.load("assets/default/");
		return dParams;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj==null)
			return false;

		if(!(obj instanceof Parameters))
			return false;

		Parameters castObj = (Parameters) obj;

		if(!(castObj.playerCount== this.playerCount &&
				castObj.suitCount== this.suitCount &&
				Arrays.equals(castObj.cardSuitUsed,this.cardSuitUsed)&&
				Arrays.equals(castObj.coinSuitUsed,this.coinSuitUsed) &&
				castObj.coinCount== this.coinCount &&
				castObj.goldCount== this.goldCount &&
				castObj.maxCoins== this.maxCoins &&
				castObj.deckCount== this.deckCount &&
				castObj.cardsOnTableCount== this.cardsOnTableCount &&
				castObj.noblesCount== this.noblesCount &&
				castObj.endGameScore== this.endGameScore &&
				castObj.maxReserveCards== this.maxReserveCards &&
				castObj.pickDifferentCount== this.pickDifferentCount &&
				castObj.pickDifferentAmount	== this.pickDifferentAmount &&
				castObj.pickSameAmount== this.pickSameAmount &&
				castObj.pickSameMinCoins== this.pickSameMinCoins)){
			return false;
		}

		return true;
	}

	public static String defaultName(){
		return "parameters.json";
	}

	public String noblesFolder(){
		return path()+"nobles/";
	}

	public String noblesFile(){
		return noblesFolder()+"nobles.csv";
	}

	public String deckPath(int deckId){
		if(deckId-1<0 || deckId-1>=deckCount){
			return null;
		}
		return decksPath()+deckId+".csv";
	}

	public String decksPath(){
		return path()+"decks/";
	}

	public String path(){
		return path;
	}

	private static Parameters actualLoad(String path){
		Parameters read = null;
		try (Reader r = new FileReader(path)) {
			Gson parser = new Gson();
			read = parser.fromJson(r, Parameters.class);
		}catch (Exception e){
			e.printStackTrace();
		}
		return read;
	}

	public static Parameters load(String path){
		Parameters p = actualLoad(path+defaultName());
		p.path = path;
		return p;
	}

	public static Parameters load(String path,String fileName){
		Parameters p = actualLoad(path+fileName);
		p.path = path;
		return p;
	}

	private static void actualSave(String path, Parameters p){
		try (Writer w = new FileWriter(path)){
			Gson writer = new GsonBuilder().setPrettyPrinting().create();
			writer.toJson(p, w);
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	public static void save(String path, String fileName, Parameters p){
		actualSave(path+fileName,p);
	}

	public static void save(String fileDirectory, Parameters p){
		actualSave(fileDirectory+defaultName(),p);
	}

	public static void main(String[] args){
//		String path = "assets/default/";
//		String fileName = "parameters.json";
//		Parameters saved = Parameters.defaultParameters();
//
//		Parameters.save(path,fileName, saved);
//		Parameters loaded = Parameters.load(path,fileName);
//
//		System.out.println(saved.equals(loaded)?"WORKS":"S*IT");
	}

	public static int[] vectorise(Parameters p){
		return new int[]{
				p.playerCount,
				p.suitCount,
				p.coinCount,
				p.goldCount,
				p.maxCoins,
				p.deckCount,
				p.cardsOnTableCount,
				p.noblesCount,
				p.endGameScore,
				p.maxReserveCards,
				p.pickDifferentCount,
				p.pickDifferentAmount,
				p.pickSameAmount,
				p.pickSameMinCoins
		};
	}

	public static Parameters unpack(int[] v){
		Parameters p = defaultParameters();

		p.playerCount = v[0];
		p.suitCount = v[1];
		p.coinCount = v[2];
		p.goldCount = v[3];
		p.maxCoins = v[4];
		p.deckCount = v[5];
		p.cardsOnTableCount = v[6];
		p.noblesCount = v[7];
		p.endGameScore = v[8];
		p.maxReserveCards = v[9];
		p.pickDifferentCount = v[10];
		p.pickDifferentAmount = v[11];
		p.pickSameAmount = v[12];
		p.pickSameMinCoins =v[13];

		return p;
	}

}
