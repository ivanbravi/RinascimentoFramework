package utils.math.permutation;

import java.util.Arrays;

public class P {

	private int[] elements;

	public P(int[] elements){
		this.elements = Arrays.copyOf(elements,elements.length);
		Arrays.sort(this.elements);
	}

	public int[] getConfiguration(){
		return elements;
	}

	public boolean equals(Object o){
		if(o==null)
			return false;

		if (getClass() != o.getClass())
			return false;

		P p = (P) o;
		return checkEquality(p);
	}

	private boolean checkEquality(P p){
		if(p.elements.length !=this.elements.length){
			return false;
		}
		for(int i=0; i<this.elements.length; i++){
			if(this.elements[i]!=p.elements[i]){
				return false;
			}
		}
		return true;
	}

}
