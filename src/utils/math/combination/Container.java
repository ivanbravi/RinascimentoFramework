package utils.math.combination;

public class Container {

	private C params;

	private int[][] combinations;
	private long size;

	public Container(C params, Combinations combCounter){
		this.params = params;
		this.size = combCounter.combinations(params.n(),params.k());
		this.combinations = new int[(int) size][];
	}

	private boolean isIndexValid(int index){
		return index>=0 && index<size;
	}

	public boolean containsCombination(int index){
		if(!isIndexValid(index)){
			return false;
		}
		return combinations[index]!=null;
	}

	public int[] getCombination(int index){
		if(!isIndexValid(index)){
			return null;
		}
		return combinations[index];
	}

	public boolean isCombinationValid(int[] c){
		if(c.length!=params.n())
			return false;
		int count = 0;
		for(int i=0; i<c.length; i++)
			count += c[i];

		return count==params.k();
	}

	public void addCombinationSafe(int index, int[] c){
		if(!isIndexValid(index)){
			throw new RuntimeException("Invalid combination index");
		}
		if(!isCombinationValid(c)){
			throw new RuntimeException("Invalid combination");
		}
		this.combinations[index] = c;
	}

	public void addCombination(int index, int[] c){
		this.combinations[index] = c;
	}

}
