package utils.ops2D;

public class Init2D {

	public static int count(double[][] m){
		return m.length*m[0].length;
	}

	public static void init(double[][] m, double value){
		int xLen = m.length;
		int yLen = m[0].length;

		for(int x=0; x<xLen; x++)
			for(int y=0; y<yLen; y++)
				m[x][y] = value;

	}

	public static double[][] copy(double[][] m){
		double[][] r = copyShape(m);
		for(int x=0; x<r.length; x++)
			for(int y=0; y<r[x].length; y++)
				r[x][y] = m[x][y];
		return r;
	}

	public static double[][] copyLegalise(double[][] m){
		double[][] r = copyShape(m);

		for(int x=0; x<r.length; x++) {
			for (int y = 0; y < r[x].length; y++) {
				double value = m[x][y];
				if (Double.isNaN(value) || Double.isInfinite(value))
					r[x][y] = 0;
				else
					r[x][y] = value;
			}
		}

		return r;
	}

	public static double[][] copyShape(double[][] m){
		double[][] r = new double[m.length][];

		for(int i=0; i<r.length; i++)
			r[i] = new double[m[i].length];

		return r;
	}

}
