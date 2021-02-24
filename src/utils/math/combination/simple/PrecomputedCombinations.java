package utils.math.combination.simple;

import utils.math.Factorial;
import utils.math.combination.C;
import utils.math.combination.Combinations;
import utils.math.combination.Container;

import java.lang.reflect.Array;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;


/*
* This is unfinished work
*/

public class PrecomputedCombinations extends Combinations {

	private long[][] counters;
	private HashMap<C, Container> combContainers;

	public PrecomputedCombinations(int n, int k){
		super(n,k);
		combContainers = new HashMap<>();

		checkParameters(n,k);

		counters = new long[n][];

		for(int i=0; i<n; i++){
			counters[i] = new long[k];
			Arrays.fill(counters[i],0);
		}

		precompute();
	}

	private void precompute(){

		for(int local_n=1; local_n<=n; local_n++) {
			for (int local_k = 1; local_k <= local_n; local_k++) {
				long combCounter = computeCombinations(local_n, local_k);
				setCounter(local_n, local_k, combCounter);
				for(int c_index = 0; c_index<combCounter; c_index++){
					int[] tmp = combination(local_n,local_k,c_index);
					System.out.println("n: "+n+" k: "+k+" index: "+c_index+Arrays.toString(tmp));
				}
			}
		}

	}

	private long computeCombinations(int n, int k){
		BigInteger f_k = Factorial.f(k);
		BigInteger f_nk = Factorial.f(n-k);
		BigInteger f_n = Factorial.f(n);

		return f_n.divide(f_k).divide(f_nk).longValue();
	}

	private boolean checkParameters(int n, int k){
		if(n<k || n<0 || k<0){
			throw new RuntimeException("Invalid combination values! [n:"+n+" k:"+k);
		}
		return true;
	}

	private void setCounter(int n, int k, long counter){
		checkParameters(n,k);
		counters[n-1][k-1] = counter;
	}

	@Override
	public long combinations(int n, int k) {
		checkParameters(n,k);
		if(!isCombinationStored(n,k)){
			return -1;
		}
		return counters[n-1][k-1];
	}

	private boolean isCombinationStored(int n, int k){
		return (n<=this.n && k<=this.k);
	}

	@Override
	public int[] combination(int n, int k, int index) {
		int[] combination = null;
		C c = C.c(n,k);

		if(combContainers.containsKey(c)){
			Container container = this.combContainers.get(c);
			if(container.containsCombination(index)){
				combination = container.getCombination(index);
			}
		}else{
			combContainers.put(c,new Container(c,this));
		}

		if(combination==null) {
			combination = buildCombination(n, k, index);
			combContainers.get(c).addCombinationSafe(index,combination);
		}

		return combination;
	}

	public int[] combinationSymbols(int n, int k, int index){
		int[] symbols = new int[k];
		int[] c = combination(n,k,index);
		int mIndex = 0;
		for(int i=0; i<n; i++){
			if(c[i]!=0){
				symbols[mIndex] = i;
				mIndex++;
			}
		}
		return symbols;
	}

	private int[] buildCombination(int n, int k, int index){
		int local_n=n;
		int local_k=k;
		long local_index = index;
		int[] combination = new int[n];

		int p=0;

		while(local_n-local_k!=1 && local_n!=1){

			long first_half = combinations(local_n-1,local_k-1);

			if(local_index<first_half){
				combination[p] = 1;
				p++;
				local_n = local_n-1;
				local_k = local_k-1;
			}else{
				combination[p] = 0;
				p++;
				local_n = local_n-1;
				local_index = local_index-first_half;
			}

		}

		if(local_n-local_k==1){
			for(;p<n;p++){
				if(p==index){
					combination[p] = 0;
				}else{
					combination[p] = 1;
				}
			}
		}else if(local_n==1){
			for(;p<n;p++){
				if(p==index){
					combination[p] = 1;
				}else{
					combination[p] = 0;
				}
			}
		}

		return combination;
	}

	public static void main(String[] args){
		int n=8;
		int k=2;
		PrecomputedCombinations test = new PrecomputedCombinations(8,2);

		long c_count = test.combinations(n,k);

		for(int i=0; i<c_count; i++){
			System.out.println(i + " " + Arrays.toString(test.combination(n,k,i)) + " " + Arrays.toString(test.combinationSymbols(n,k,i)));
		}


	}

}
