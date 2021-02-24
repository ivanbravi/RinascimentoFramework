package utils.math;

import java.math.BigInteger;
import java.util.HashMap;

public class Factorial {

	static HashMap<Integer,BigInteger> storedFactorials = new HashMap<Integer,BigInteger>(){{put(0, BigInteger.valueOf(1));
																							 put(1, BigInteger.valueOf(1));}};

	static public BigInteger f(int n){
		if(storedFactorials.containsKey(n)){
			return storedFactorials.get(n);
		}

		BigInteger factorial = BigInteger.valueOf(1);

		for(int i=0; i<=n; i++){
			if(storedFactorials.containsKey(i)){
				factorial = storedFactorials.get(i);
			} else {
				factorial = factorial.multiply(BigInteger.valueOf(i));
				storedFactorials.put(i,factorial);
			}
		}

		return factorial;
	}


	public static void main(String[] args){
		System.out.println(Factorial.f(1000));
	}


}
