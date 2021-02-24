package utils;

import java.util.ArrayList;

public class ArrayUtils {


	public static int[] add(int[]a, int[]b ){
		if(a == null || b == null)
			return null;

		if(a.length != b.length)
			return null;
		int[] c = new int[a.length];

		for(int i=0; i<c.length; i++){
			c[i] = a[i]+b[i];
		}

		return c;
	}

	public static ArrayList<Integer> filterIndicesGE(int[] v, int atLeast){
		ArrayList<Integer> indices = new ArrayList<>();

		for(int i=0; i<v.length; i++){
			if(v[i]>=atLeast){
				indices.add(i);
			}
		}
		return indices;
	}

	public static ArrayList<Integer> filterIndicesL(int[] v, int atLeast){
		ArrayList<Integer> indices = new ArrayList<>();

		for(int i=0; i<v.length; i++){
			if(v[i]<atLeast){
				indices.add(i);
			}
		}
		return indices;
	}

}
