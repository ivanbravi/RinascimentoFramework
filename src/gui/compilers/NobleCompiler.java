package gui.compilers;

import game.state.Noble;
import gui.UIStandards;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PFont;
import processing.core.PGraphics;

import java.io.File;

public class NobleCompiler {

	private static boolean forceCompiling = true;
	private static int nobleSize = 250;
	private static String exportType = PConstants.JAVA2D;

	public static String format(){
		return exportType;
	}

	private static String extension(){
		if(exportType==PConstants.SVG){
			return "svg";
		}
		if(exportType==PConstants.PDF){
			return "pdf";
		}
		if(exportType==PConstants.JAVA2D){
			return "png";
		}

		return "";
	}

	public static String compiledNoblePath(String path, int nobledId){
		return path+nobledId+"."+extension();
	}

	public static void drawNoble(Noble n, float w, float h, PGraphics renderer, PApplet a){
		int costs[] = n.getCost();
		int points = n.getPoints();
		float scale = Math.min(w/nobleSize,h/nobleSize);
		PFont costFont = a.createFont(UIStandards.font(),40);
		PFont pointFont = a.createFont(UIStandards.font(),70);
		//renderer.textMode(PConstants.SHAPE);

		renderer.pushMatrix();
		renderer.pushStyle();
		renderer.scale(scale);
		renderer.ellipseMode(PConstants.CENTER);
		renderer.textAlign(PConstants.CENTER);
		renderer.rectMode(PConstants.CORNER);

		// draw
		renderer.noFill();
		renderer.strokeWeight(UIStandards.assetStrokeWeight());
		renderer.stroke(0);
		renderer.rect(0,0, nobleSize, nobleSize, 11);
		renderer.line(68,0,68,nobleSize);

		// draw points
		renderer.fill(UIStandards.fontColorDark());
		renderer.noStroke();
		renderer.textFont(pointFont);
		renderer.text(points, 34,68);

		// draw costs
		int cCounter = 0;
		renderer.rectMode(PConstants.CENTER);
		for(int j=0; j<costs.length; j++){
			int c = costs[j];
			if(c>0) {
				// draw cost circle
				renderer.fill(UIStandards.suitColor(j));
				renderer.stroke(UIStandards.lightGrey());
				renderer.rect(34,213-cCounter*54,32,44,6); //cost rect

				// draw cost text
				renderer.fill(UIStandards.fontColor(j));
				renderer.textFont(costFont);
				renderer.textAlign(PConstants.CENTER);
				renderer.text(costs[j], 34,227-cCounter*54); //cost text
				cCounter++;
			}
		}
		renderer.popStyle();
		renderer.popMatrix();
	}

	public static void compileNobles(Noble[] nobles, String destination, PApplet a){
		for(int i=0; i<nobles.length; i++){
			String nobleFile = compiledNoblePath(destination,nobles[i].getId());

			if(isNobleCompiled(nobleFile) && !forceCompiling){
				continue;
			}

			PGraphics renderer = a.createGraphics(nobleSize, nobleSize, exportType, nobleFile);
			renderer.beginDraw();
			drawNoble(nobles[i],nobleSize,nobleSize, renderer, a);
			renderer.endDraw();
			renderer.save(nobleFile);
			renderer.dispose();
		}
	}

	private static boolean isNobleCompiled(String path){
		return  (new File(path).exists());
	}

}
