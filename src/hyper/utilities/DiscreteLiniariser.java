package hyper.utilities;

import evodef.SearchSpace;

import java.util.Arrays;

public class DiscreteLiniariser {

	public static int pack(SearchSpace ss, int[] config){
		int packedConfig = 0;

		for(int i=ss.nDims()-1; i>=0; i--){
			int mult = ss.nValues(i);
			packedConfig *=mult;
			packedConfig += config[i];
		}

		return packedConfig;
	}

	public static int[] unpack(SearchSpace ss, int packedConfig){
		int[] config = new int[ss.nDims()];

		for(int i=0; i<config.length; i++){
			int mod = ss.nValues(i);
			config[i] = packedConfig%mod;
			packedConfig = packedConfig/mod;
		}

		return config;
	}

	public static void main(String[] args){
		SearchSpace ss = new SearchSpace() {
			int[] d = new int[]{7,3,9};
			@Override
			public int nDims() {
				return d.length;
			}

			@Override
			public int nValues(int i) {
				return d[i];
			}
		};

		System.out.println("size:"+ss.nValues(0)*ss.nValues(1)*ss.nValues(2));

		for(int i=0; i<ss.nValues(0); i++){
			for(int j=0; j<ss.nValues(1);j++) {
				for (int k = 0; k < ss.nValues(2); k++) {
					int packed = DiscreteLiniariser.pack(ss, new int[]{i,j,k});
					int[] unpacked = DiscreteLiniariser.unpack(ss, packed);
					System.out.println();
					System.out.println("i:" + i + " j:" + j + " k:" + k);
					System.out.println("packed:" + packed);
					System.out.println("unpacked:" + Arrays.toString(unpacked));
				}
			}
		}

	}

}
