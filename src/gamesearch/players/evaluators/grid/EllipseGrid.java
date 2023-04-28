package gamesearch.players.evaluators.grid;

import gamesearch.players.evaluators.matrix.GridGenerator;
import gamesearch.players.evaluators.matrix.LoadGridGenerator;
import mapelites.behaviours.BehaviourReader;
import mapelites.core.binning.Binning;
import utils.CopyToClipboard;
import utils.filters2D.Filter2D;
import utils.filters2D.KernelBasedFilter2D;
import utils.filters2D.Normaliser;
import utils.filters2D.WeightedMean;
import utils.ops2D.ToString2D;

import java.awt.*;
import java.awt.geom.Point2D;

public class EllipseGrid implements GridGenerator {

	Point2D.Double center;
	double rx, ry;
	double angle;
	int size;
	KernelBasedFilter2D filter;
	Filter2D normaliser;

	public EllipseGrid(Point2D.Double center, double rx, double ry, double angle, int size){
		this.center = center;
		this.rx = rx;
		this.ry = ry;
		this.angle = angle;
		this.size = size;
		filter = new WeightedMean(size);
		normaliser = new Normaliser();
	}

	@Override
	public double[][] samplesGrid(double[] xAxis, double[] yAxis) {
		double[][] r = new double[xAxis.length][];
		Point highPoint = null;

		for(int x=0; x<xAxis.length; x++){
			r[x] = new double[yAxis.length];
			for(int y=0; y<yAxis.length; y++){
				Point2D.Double point = new Point2D.Double(xAxis[x], yAxis[y]);
				if(isInside(point)) {
					r[x][y] = 1.0;
					if(highPoint==null){
						highPoint = new Point(x,y);
					}
				}
			}
		}

		System.out.println(ToString2D.matrixToString(r,ToString2D.numPyFormat,false));
		System.out.println();
		r = filter.apply(r);
		System.out.println(ToString2D.matrixToString(r,ToString2D.numPyFormat,false));
		System.out.println();
		r = normaliser.apply(r);
		System.out.println(ToString2D.matrixToString(r,ToString2D.numPyFormat,false));
		System.out.println();

		return r;
	}

	private boolean isInside(Point2D.Double p){
		double a = rx;
		double b = ry;
		double alpha = angle;
		double xx = (a*a*Math.sin(alpha)*Math.sin(alpha) + b*b*Math.cos(alpha)*Math.cos(alpha))*Math.pow(p.x-center.x,2);
		double xy = 2*(b*b-a*a)*Math.sin(alpha)*Math.cos(alpha)*(p.x-center.x)*(p.y-center.y);
		double yy = (a*a*Math.cos(alpha)*Math.cos(alpha) + b*b*Math.sin(alpha)*Math.sin(alpha))*Math.pow(p.y-center.y,2);
		return xx+xy+yy <= a*a*b*b;
	}

	private static String toNumPyMatrix(double[][] grid){
		return ToString2D.matrixToString(grid, ToString2D.numPyFormat,true);
	}

	public static void main(String[] args) {
		String gridGeneratorFile = "assets/game_search/grid.json";
		String behavioursFile = "assets/game_search/behaviours.json";
		GridGenerator eg = LoadGridGenerator.load(gridGeneratorFile);
		BehaviourReader br = new BehaviourReader(behavioursFile);
		Binning[] bins = br.getLinearBins();

		double[] xAxis = BehaviourReader.getAxis(bins[0]);
		double[] yAxis = BehaviourReader.getAxis(bins[1]);
		double[][] grid = eg.samplesGrid(xAxis,yAxis);

		String gridString = ToString2D.matrixToString(grid, ToString2D.defaultFormat,true);

//		System.out.println(gridString);
//		System.out.println(toNumPyMatrix(grid));

		CopyToClipboard.copy(gridString);
	}

}
