package utils.math.combination.repeated;

import utils.math.Factorial;
import utils.math.combination.C;
import utils.math.combination.Combinations;
import utils.math.combination.Container;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;

public class MultiCombination extends Combinations {

	Container container;

	public MultiCombination(int n, int k) {
		super(n, k);
		container = new Container(new C(n,k),this);
	}

	@Override
	public long combinations(int n, int k) {
		BigInteger v = Factorial.f(n+k-1);
		v = v.divide(Factorial.f(n-1));
		v = v.divide(Factorial.f(k));
		return v.longValue();
	}

	@Override
	public int[] combination(int n, int k, int index) {
		int[] c;

		if(container.containsCombination(index)){
			c = container.getCombination(index);
		}else{
			c = new int[n];
			ArrayList<Integer> symbs = recursiveComputeSymbols(n,k,index,0);
			symbs.stream().forEach(s->c[s]++);
			container.addCombinationSafe(index,c);
		}

		return c;
	}

	public int[] combinationSymbols(int n, int k, int index){
		int[] symbols = new int[k];
		int[] c;
		if(container.containsCombination(index)){
			c=container.getCombination(index);
		}else{
			c=combination(n,k,index);
		}
		int mIndex = 0;
		for(int i=0; i<n; i++){
			for(int j=0; j<c[i]; j++){
				symbols[mIndex] = i;
				mIndex++;
			}
		}
		return symbols;
	}

	private ArrayList<Integer> recursiveComputeSymbols(int n, int k, int index, int start){
		int acc = 0;
		k--;
		if(k==0) {
			return new ArrayList<Integer>(){{add(start+index);}};
		}

		for(int i=0; i<n; i++){
			long accDelta = combinations(n-i,k);
			if(index<acc+accDelta){
				ArrayList<Integer> symbols = recursiveComputeSymbols(n-i,k,index-acc,start+i);
				symbols.add(start+i);
				return symbols;
			}else{
				acc+=accDelta;
			}
		}

		return null;
	}

	public static void main(String[] args){
		int nTrials = 1;
		int n=8;
		int k=2;
		MultiCombination c = new MultiCombination(n,k);
		long tot = c.combinations(n,k);
		long t = System.currentTimeMillis();
		for(int trial=0; trial<nTrials; trial++)
			for(int i=0;i<tot; i++) {
				c = new MultiCombination(n, k);
				System.out.println(i + " " + Arrays.toString(c.combination(n, k, i)) + " " + Arrays.toString(c.combinationSymbols(n, k, i)));
			}
		long e = System.currentTimeMillis()-t;
		t = System.currentTimeMillis();
//		for(int i=0;i<tot; i++)
//			System.out.println(i+" "+Arrays.toString(c.combination(n,k,i))+" "+Arrays.toString(c.combinationSymbols(n,k,i)));

	}

}
