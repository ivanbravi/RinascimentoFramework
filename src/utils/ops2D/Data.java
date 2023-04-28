package utils.ops2D;

public class Data {

	private static double INF = Double.NEGATIVE_INFINITY;

	public static double[][] inf10x10 = new double[][]{
				new double[]{INF, INF, INF, INF, INF, INF, INF, INF, INF, INF},
				new double[]{INF, INF, INF, INF, INF, INF, INF, INF, INF, INF},
				new double[]{INF, INF, INF, INF, INF, INF, INF, INF, INF, INF},
				new double[]{INF, INF, INF, INF, INF, INF, INF, INF, INF, INF},
				new double[]{INF, INF, INF, INF, INF, INF, INF, INF, INF, INF},
				new double[]{INF, INF, INF, INF, INF, INF, INF, INF, INF, INF},
				new double[]{INF, INF, INF, INF, INF, INF, INF, INF, INF, INF},
				new double[]{INF, INF, INF, INF, INF, INF, INF, INF, INF, INF},
				new double[]{INF, INF, INF, INF, INF, INF, INF, INF, INF, INF},
				new double[]{INF, INF, INF, INF, INF, INF, INF, INF, INF, INF}
		};

	public static double[][] elInf10x10 = new double[][]{
			new double[]{INF, INF, INF, INF, INF, INF, INF, INF, INF, INF},
			new double[]{INF, INF, INF, INF, INF, INF, INF, INF, INF, INF},
			new double[]{INF, INF, 0.4, INF, INF, INF, INF, INF, INF, INF},
			new double[]{INF, INF, INF, INF, INF, INF, INF, INF, INF, INF},
			new double[]{1.0, INF, 0.6, INF, INF, INF, INF, INF, INF, INF},
			new double[]{INF, INF, INF, INF, INF, INF, INF, INF, INF, INF},
			new double[]{0.8, INF, INF, INF, INF, INF, INF, INF, INF, INF},
			new double[]{INF, INF, INF, INF, INF, INF, INF, INF, INF, INF},
			new double[]{INF, INF, INF, INF, INF, INF, INF, INF, INF, INF},
			new double[]{INF, INF, INF, INF, INF, INF, INF, INF, INF, INF}
	};

	public static double[][] sqInf10x10 = new double[][]{
			new double[]{INF, INF, INF, INF, INF, INF, INF, INF, INF, INF},
			new double[]{INF, INF, INF, INF, INF, INF, INF, INF, INF, INF},
			new double[]{INF, INF, 0.7, 0.3, 0.0, 0.1, INF, INF, INF, INF},
			new double[]{INF, INF, 0.8, 0.6, 0.1, 0.2, INF, INF, INF, INF},
			new double[]{INF, INF, 0.9, 0.5, 0.5, 0.7, INF, INF, INF, INF},
			new double[]{INF, INF, 0.2, 0.1, 0.2, 0.9, INF, INF, INF, INF},
			new double[]{INF, INF, INF, INF, INF, INF, INF, INF, INF, INF},
			new double[]{INF, INF, INF, INF, INF, INF, INF, INF, INF, INF},
			new double[]{INF, INF, INF, INF, INF, INF, INF, INF, INF, INF},
			new double[]{INF, INF, INF, INF, INF, INF, INF, INF, INF, INF}
	};

	public static double[][] sq2Inf10x10 = new double[][]{
			new double[]{INF, INF, INF, INF, INF, INF, INF, INF, INF, INF},
			new double[]{INF, INF, INF, INF, INF, INF, INF, INF, INF, INF},
			new double[]{INF, INF, 0.7, 0.3, 0.0, 0.1, INF, INF, INF, INF},
			new double[]{INF, INF, 0.8, 0.6, 0.1, 0.2, INF, INF, INF, INF},
			new double[]{INF, INF, 0.9, 0.5, 0.5, 0.7, INF, INF, INF, INF},
			new double[]{INF, INF, 0.2, 0.1, 0.2, 0.9, INF, INF, INF, INF},
			new double[]{INF, INF, INF, INF, INF, INF, INF, INF, INF, INF},
			new double[]{INF, INF, INF, INF, INF, INF, INF, INF, INF, INF},
			new double[]{INF, INF, INF, INF, INF, INF, INF, INF, 0.6, 0.6},
			new double[]{INF, INF, INF, INF, INF, INF, INF, INF, 0.6, 0.6}
	};

	public static double[][] oneInf10x10 = new double[][]{
			new double[]{INF, INF, INF, INF, INF, INF, INF, INF, INF, INF},
			new double[]{INF, INF, INF, INF, INF, INF, INF, INF, INF, INF},
			new double[]{INF, INF, 1.0, 1.0, 1.0, 1.0, INF, INF, INF, INF},
			new double[]{INF, INF, 1.0, 1.0, 1.0, 1.0, INF, INF, INF, INF},
			new double[]{INF, INF, 1.0, 1.0, 1.0, 1.0, INF, INF, INF, INF},
			new double[]{INF, INF, 1.0, 1.0, 1.0, 1.0, INF, INF, INF, INF},
			new double[]{INF, INF, INF, INF, INF, INF, INF, INF, INF, INF},
			new double[]{INF, INF, INF, INF, INF, INF, INF, INF, INF, INF},
			new double[]{INF, INF, INF, INF, INF, INF, INF, INF, INF, INF},
			new double[]{INF, INF, INF, INF, INF, INF, INF, INF, INF, INF}
	};

	public static double[][] square10x10 = new double[][]{
			new double[]{1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0},
			new double[]{1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0},
			new double[]{1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0},
			new double[]{1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0},
			new double[]{1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0},
			new double[]{1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0},
			new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
			new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
			new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
			new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0}
	};

	public static double[][] rect10x10 = new double[][]{
			new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
			new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
			new double[]{0.0, 0.0, 1.0, 1.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0},
			new double[]{0.0, 0.0, 1.0, 1.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0},
			new double[]{0.0, 0.0, 1.0, 1.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0},
			new double[]{0.0, 0.0, 1.0, 1.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0},
			new double[]{0.0, 0.0, 1.0, 1.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0},
			new double[]{0.0, 0.0, 1.0, 1.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0},
			new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
			new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0}
	};

	public static double[][] zeros10x10 = new double[][]{
			new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
			new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
			new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
			new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
			new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
			new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
			new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
			new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
			new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
			new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0}
	};

	public static double[][] inf5x5 = new double[][]{
			new double[]{INF, INF, INF, INF, INF},
			new double[]{INF, INF, INF, INF, INF},
			new double[]{INF, INF, INF, INF, INF},
			new double[]{INF, INF, INF, INF, INF},
			new double[]{INF, INF, INF, INF, INF}
	};

	public static double[][] sqInf5x5 = new double[][]{
			new double[]{INF, INF, INF, INF, INF},
			new double[]{INF, 0.7, 0.0, INF, INF},
			new double[]{INF, 0.9, 0.5, INF, INF},
			new double[]{INF, INF, INF, INF, INF},
			new double[]{INF, INF, INF, INF, INF}
	};

	public static double[][] sq2Inf5x5 = new double[][]{
			new double[]{INF, INF, INF, INF, INF},
			new double[]{INF, 0.7, 0.0, INF, INF},
			new double[]{INF, 0.9, 0.5, INF, INF},
			new double[]{INF, INF, INF, INF, INF},
			new double[]{INF, INF, INF, INF, 0.6}
	};

	public static double[][] oneInf5x5 = new double[][]{
			new double[]{INF, INF, INF, INF, INF},
			new double[]{INF, 1.0, 1.0, INF, INF},
			new double[]{INF, 1.0, 1.0, INF, INF},
			new double[]{INF, INF, INF, INF, INF},
			new double[]{INF, INF, INF, INF, INF}
	};


	public static double[][] square5x5 = new double[][]{
			new double[]{1.0, 1.0, 1.0, 0.0, 0.0},
			new double[]{1.0, 1.0, 1.0, 0.0, 0.0},
			new double[]{1.0, 1.0, 1.0, 0.0, 0.0},
			new double[]{0.0, 0.0, 0.0, 0.0, 0.0},
			new double[]{0.0, 0.0, 0.0, 0.0, 0.0}
	};

	public static double[][] rect5x5 = new double[][]{
			new double[]{0.0, 0.0, 0.0, 0.0, 0.0},
			new double[]{0.0, 1.0, 1.0, 0.0, 0.0},
			new double[]{0.0, 1.0, 1.0, 0.0, 0.0},
			new double[]{0.0, 1.0, 1.0, 0.0, 0.0},
			new double[]{0.0, 0.0, 0.0, 0.0, 0.0}
	};

	public static double[][] zeros5x5 = new double[][]{
			new double[]{0.0, 0.0, 0.0, 0.0, 0.0},
			new double[]{0.0, 0.0, 0.0, 0.0, 0.0},
			new double[]{0.0, 0.0, 0.0, 0.0, 0.0},
			new double[]{0.0, 0.0, 0.0, 0.0, 0.0},
			new double[]{0.0, 0.0, 0.0, 0.0, 0.0}
	};

}
