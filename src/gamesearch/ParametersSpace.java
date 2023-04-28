package gamesearch;

import game.Parameters;
import hyper.utilities.CompleteAnnotatedSearchSpace;
import ntbea.params.DoubleParam;
import ntbea.params.IntegerParam;
import ntbea.params.Param;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.IntStream;

public class ParametersSpace extends CompleteAnnotatedSearchSpace {

	private ParametersSpace(int[][] pList){
		params = createParams(pList[0], pList[1], pList[2], pList[3], pList[4], pList[5], pList[6], pList[7], pList[8], pList[9], pList[10], pList[11], pList[12], pList[13]);
		dimensions = createDimensions(pList[0], pList[1], pList[2], pList[3], pList[4], pList[5], pList[6], pList[7], pList[8], pList[9], pList[10], pList[11], pList[12], pList[13]);
	}

	public ParametersSpace(){
//		int[] playerCount 			= new int[]{2};
//		int[] suitCount 			= new int[]{5};
//		int[] coinCount 			= IntStream.rangeClosed(3, 30).toArray();
//		int[] goldCount 			= IntStream.rangeClosed(3, 30).toArray();
//		int[] maxCoins 				= IntStream.rangeClosed(1, 150).toArray();
//		int[] deckCount 			= new int[]{3};
//		int[] cardsOnTableCount 	= IntStream.rangeClosed(1, 10).toArray();
//		int[] noblesCount 			= IntStream.rangeClosed(0, 10).toArray();
//		int[] endGameScore 			= IntStream.rangeClosed(1, 30).toArray();
//		int[] maxReservedCards 		= IntStream.rangeClosed(0, 10).toArray();
//		int[] pickDifferentCount 	= IntStream.rangeClosed(2, 5).toArray();
//		int[] pickDifferentAmount 	= IntStream.rangeClosed(1, 3).toArray();
//		int[] pickSameAmount 		= IntStream.rangeClosed(2, 5).toArray();
//		int[] pickSameMinCoins 		= IntStream.rangeClosed(1, 10).toArray();

		int[] playerCount 			= new int[]{2};
		int[] suitCount 			= new int[]{5};
		int[] coinCount 			= ParametersSpace.range(3,30,10);
		int[] goldCount 			= ParametersSpace.range(3,30,10);
		int[] maxCoins 				= ParametersSpace.range(1,150,10);
		int[] deckCount 			= new int[]{3};
		int[] cardsOnTableCount 	= IntStream.rangeClosed(1, 10).toArray();
		int[] noblesCount 			= IntStream.rangeClosed(0, 10).toArray();
		int[] endGameScore 			= ParametersSpace.range(1,30,10);
		int[] maxReservedCards 		= IntStream.rangeClosed(0, 10).toArray();
		int[] pickDifferentCount 	= IntStream.rangeClosed(2, 5).toArray();
		int[] pickDifferentAmount 	= IntStream.rangeClosed(1, 3).toArray();
		int[] pickSameAmount 		= IntStream.rangeClosed(2, 5).toArray();
		int[] pickSameMinCoins 		= IntStream.rangeClosed(1, 10).toArray();

		params = createParams(playerCount,suitCount,coinCount,goldCount,maxCoins,deckCount,cardsOnTableCount,noblesCount,endGameScore,maxReservedCards,pickDifferentCount,pickDifferentAmount,pickSameAmount,pickSameMinCoins);
		dimensions = createDimensions(playerCount,suitCount,coinCount,goldCount,maxCoins,deckCount,cardsOnTableCount,noblesCount,endGameScore,maxReservedCards,pickDifferentCount,pickDifferentAmount,pickSameAmount,pickSameMinCoins);
	}

	private static int[] range(int min, int max, int count){
		int[] values = new int[count];
		IntStream.
				range(0,count).
				forEach(index ->{
					double range = (double) index/(count-1);
					values[index] = (int) (min+(max-min)*range);
						});

		return values;
	}

	private Param[] createParams(int[] a, int[] b, int[] c, int[] d, int[] e, int[] f, int[] g, int[] h, int[] i, int[] j, int[] k, int[] l, int[] m , int[] n){
		return new Param[]{
				new IntegerParam().setArray(a).setName("Player Count"),
				new IntegerParam().setArray(b).setName("Suit Count"),
				new IntegerParam().setArray(c).setName("Coin Count"),
				new IntegerParam().setArray(d).setName("Gold Count"),
				new IntegerParam().setArray(e).setName("Max Coins"),
				new IntegerParam().setArray(f).setName("Deck Count"),
				new IntegerParam().setArray(g).setName("Cards On Table Count"),
				new IntegerParam().setArray(h).setName("Nobles Count"),
				new IntegerParam().setArray(i).setName("End Game Score"),
				new IntegerParam().setArray(j).setName("Max Reserved Cards"),
				new IntegerParam().setArray(k).setName("Pick Different Count"),
				new IntegerParam().setArray(l).setName("Pick Different Amount"),
				new IntegerParam().setArray(m).setName("Pick Same Amount"),
				new IntegerParam().setArray(n).setName("Pick Same Min Coins")
		};
	}

	public static final String[] names = new String[]{"Player Count", "Suit Count", "Coin Count", "Gold Count", "Max Coins", "Deck Count", "Cards On Table Count", "Nobles Count", "End Game Score", "Max Reserved Cards", "Pick Different Count", "Pick Different Amount", "Pick Same Amount", "Pick Same Min Coins"};

	private int[] createDimensions(int[] a, int[] b, int[] c, int[] d, int[] e, int[] f, int[] g, int[] h, int[] i, int[] j, int[] k, int[] l, int[] m , int[] n){
		return new int[]{
				a.length,
				b.length,
				c.length,
				d.length,
				e.length,
				f.length,
				g.length,
				h.length,
				i.length,
				j.length,
				k.length,
				l.length,
				m.length,
				n.length
		};
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

	public static int extract(Param p, int solutionIndex){
		int value;
		if(p instanceof DoubleParam)
			value = ((Double) p.getValue(solutionIndex)).intValue();
		else if(p instanceof IntegerParam)
			value = ((Integer) p.getValue(solutionIndex)).intValue();
		else
			throw new RuntimeException("Unknown params extraction: "+p.getName());
		return value;
	}

	private static int[] fromParamToArray(Param p, int size){
		int[] v = new int[size];
		for(int i=0; i<size; i++)
			v[i] = extract(p, i);
		return v;
	}

	private static int closestParamIndex(Param p, int size, int value){
		for(int i=0; i<size-1; i++){
			int curr = extract(p,i);
			int next = extract(p,i+1);
			if(value<=next && value>=curr){
				if(Math.abs(value-next) > Math.abs(value-curr)){
					return i;
				}else{
					return i+1;
				}
			}
		}
		if(value > extract(p,size-1))
			return size-1;
		return 0;
	}

	public static int[] closestConfigIndices(CompleteAnnotatedSearchSpace cass, Parameters p){
		int[] values = vectorise(p);
		int[] indices = new int[values.length];

		for(int i=0; i<values.length; i++){
			int size = cass.nValues(i);
			Param param = cass.getParams()[i];
			indices[i] = closestParamIndex(param, size, values[i]);
		}

		return indices;
	}

	public static ParametersSpace resizeAround(CompleteAnnotatedSearchSpace cass, double rate, Parameters pCenter){

		Param[] ps = cass.getParams();
		int[] centers = closestConfigIndices(cass,pCenter);
		ArrayList<int[]> valueList = new ArrayList<>();

		for(int i=0; i<ps.length; i++){
			int size = cass.nValues(i);
			Param p = ps[i];
			int newSize = (int) (size*rate);
			int[] oldValues = fromParamToArray(p, size);
			ArrayList<Integer> newValues = new ArrayList<>();
			int hNewSize = newSize/2;
			for(int j=-hNewSize; j<=hNewSize; j++){
				int oldIndex = j+centers[i];
				boolean okOldArray = oldIndex<oldValues.length && oldIndex>=0;
				if(okOldArray){
					newValues.add(oldValues[oldIndex]);
				}
			}
			int[] values = newValues.stream().mapToInt(a -> a).toArray();
			valueList.add(values);
		}

		return create(valueList);
	}

	private static ParametersSpace create(ArrayList<int[]> p){
		return new ParametersSpace(p.toArray(new int[p.size()][]));
	}

	public static void main(String[] args){
		String path = "assets/RinascimentoParamsTight.json";
		CompleteAnnotatedSearchSpace.save(path,new ParametersSpace());
	}


	public static String toString(CompleteAnnotatedSearchSpace cass) {
		StringBuilder builder = new StringBuilder();

		builder.append("#params = "+cass.nDims()+"\n");
		for(int i=0; i<cass.nDims(); i++){
			Param p = cass.getParams()[i];
			int size = cass.nValues(i);
			int[] v = fromParamToArray(p,size);
			builder.append(p.getName()+"["+size+"] : "+Arrays.toString(v)+"\n");
		}

		return builder.toString();
	}
}
