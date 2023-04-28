package utils.ops2D;

import java.text.DecimalFormat;

public class ToString2D {

	// The following DecimalFormat works correctly provided values in [0.0,1.0]
	// and the method String format(double value) below
	public static DecimalFormat df = new DecimalFormat("0.0");

	public final static Format defaultFormat = new Format("", "", "\t", "\n");
	public final static Format numPyFormat = new Format("[","]", ", ", "\n");

	public static class Format{
		String open;
		String close;
		String sep;
		String lineSep;

		public Format(String open, String close, String sep, String lineSep){
			this.open = open;
			this.close = close;
			this.sep = sep;
			this.lineSep = lineSep;
		}
	}

	public static String matrixToString(double[][] grid, Format f, boolean flip){
		return matrixToString(grid, f.open, f.close, f.sep, f.lineSep, flip);
	}

	public static String matrixToString(double[][] grid, String open, String close, String sep, String lineSep, boolean flip){
		if(flip)
			return transposedMatrixToString(grid,open,close,sep,lineSep);

		return memoryMatrixToString(grid,open,close,sep,lineSep);
	}

	public static String transposedMatrixToString(double[][] grid, String open, String close, String sep, String lineSep){
		StringBuilder builder = new StringBuilder();
		int xLen = grid.length;
		int yLen = grid[0].length;
		builder.append(open);

		for(int y=0; y<yLen; y++){
			builder.append(open);
			for(int x=0; x<xLen; x++){
				builder.append(format(grid[x][y])).append(sep);			}
			builder.delete(builder.length()-sep.length(),builder.length());
			builder.append(close+lineSep);
		}
		builder.delete(builder.length()-lineSep.length(),builder.length());
		builder.append(close);
		return builder.toString();
	}

	public static String memoryMatrixToString(double[][] grid, String open, String close, String sep, String lineSep){
		StringBuilder builder = new StringBuilder();
		int xLen = grid.length;
		int yLen = grid[0].length;
		builder.append(open);

		for(int x=0; x<xLen; x++){
			builder.append(open);
			for(int y=0; y<yLen; y++){
				builder.append(format(grid[x][y])).append(sep);
			}
			builder.delete(builder.length()-sep.length(),builder.length());
			builder.append(close+lineSep);
		}
		builder.delete(builder.length()-lineSep.length(),builder.length());
		builder.append(close);
		return builder.toString();
	}

	private static String format(double value){
		String r = df.format(value);
		if(r.length()==2)
			r = " "+r;
		return r;
	}

}
