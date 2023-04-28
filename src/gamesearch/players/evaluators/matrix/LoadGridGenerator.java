package gamesearch.players.evaluators.matrix;

import com.google.gson.Gson;
import gamesearch.players.evaluators.grid.EllipseGrid;
import gamesearch.players.evaluators.grid.Gaussian;
import gamesearch.players.evaluators.grid.GaussianGrid;

import java.awt.geom.Point2D;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Map;

public class LoadGridGenerator {

	public static GridGenerator load(String file){
		GridGenerator grid = null;
		try (Reader r = new FileReader(file)) {
			Gson parser = new Gson();
			Map<Object, Object> params = parser.fromJson(r, Map.class);

			String type = (String) params.get("type");

			if (type.equals("ellipse")) {
				double[] center = ((ArrayList<Double>) params.get("ellipse/center")).stream().mapToDouble(a -> a.doubleValue()).toArray();
				double rx = (double) params.get("ellipse/rx");
				double ry = (double) params.get("ellipse/ry");
				double angle = (double) params.get("ellipse/angle");
				int size = ((Double) params.get("ellipse/size")).intValue();
				grid = new EllipseGrid(new Point2D.Double(center[0],center[1]), rx, ry, angle, size);
			} else if (type.equals("gaussian")) {
				double[] mean = (double[]) params.get("gaussian/mean");
				double varX = (double) params.get("gaussian/varX");
				double varY = (double) params.get("gaussian/varY");
				double covXY = (double) params.get("gaussian/covXY");
				double[][] cov = new double[][]{
						new double[]{varX, covXY},
						new double[]{covXY, varY}
				};
				Gaussian g = new Gaussian(mean, cov);
				grid = new GaussianGrid(g);
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		return grid;
	}
}
