package utils.ops2D;

public class Math2D {

	public static double[][] add(double[][] a, double[][] b){
		double[][] r = Init2D.copyShape(a);
		int xLen = a.length;
		int yLen = a[0].length;

		for(int x=0; x<xLen; x++)
			for(int y=0; y<yLen; y++)
				r[x][y] = a[x][y]+b[x][y];

		return r;
	}

	public static double[][] subtract(double[][] a, double[][] b){
		double[][] r = Init2D.copyShape(a);
		int xLen = a.length;
		int yLen = a[0].length;

		for(int x=0; x<xLen; x++)
			for(int y=0; y<yLen; y++)
				r[x][y] = a[x][y]-b[x][y];

		return r;
	}

	public static boolean contains(double[][]m, double value){
		int xLen = m.length;
		int yLen = m[0].length;

		for(int x=0; x<xLen; x++)
			for(int y=0; y<yLen; y++)
				if(m[x][y]==value)
					return true;
		return false;
	}

	public static boolean areNotCompatible(double[][] a, double[][] b){
		return !areCompatible(a,b);
	}

	public static boolean areCompatible(double[][] a, double[][] b){
		if(a.length != b.length){
			return false;
		}
		if(a[0].length != b[0].length){
			return false;
		}
		return true;
	}

	public static boolean areEqual(double[][] a, double[][] b){
		if(!areCompatible(a,b))
			return false;

		int xLen = a.length;
		int yLen = a[0].length;
		for(int x=0; x<xLen; x++)
			for(int y=0; y<yLen; y++)
				if(a[x][y] != b[x][y])
					return false;

		return true;
	}

	public static double min(double[][] m){
		double min = Double.POSITIVE_INFINITY;

		for(int x=0; x<m.length; x++)
			for(int y=0; y<m[x].length; y++)
				if(m[x][y]<min)
					min = m[x][y];

		return min;
	}

	public static double sum(double[][] m){
		double sum = 0;
		for(int x=0; x<m.length; x++)
			for(int y=0; y<m[x].length; y++)
				sum += m[x][y];
		return sum;
	}

	public static double max(double[][] m){
		double max = Double.NEGATIVE_INFINITY;

		for(int x=0; x<m.length; x++)
			for(int y=0; y<m[x].length; y++)
				if(m[x][y]>max)
					max = m[x][y];

		return max;
	}

}
