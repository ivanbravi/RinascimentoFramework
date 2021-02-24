package gui.compilers;

import game.state.Deck;
import gui.UIStandards;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PFont;
import processing.core.PGraphics;

import java.io.File;

public class CardCompiler {

	private static boolean forceCompiling = true;
	private static int cardWidth = 250;
	private static int cardHeight = 350;
	private static String exportType = PConstants.JAVA2D;

	public static String format(){
		return exportType;
	}

	public static float cardRatio(){
		return (1.0f*cardWidth)/cardHeight;
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
	public static String compiledCardPath(String path, String deckId, int cardId){
		return path+deckId+"/"+cardId+"."+extension();
	}

	public static void drawCard(Deck d, int card, float w, float h, PGraphics renderer, PApplet a){
		PFont costFont = a.createFont(UIStandards.font(),38);
		PFont pointFont = a.createFont(UIStandards.font(),72);
		float scale = Math.min(w/cardWidth,h/cardWidth);
		int [] costs = d.getCardCost(card);
		int points = d.getCardPoints(card);
		int suit = d.getCardSuit(card);

		renderer.pushMatrix();
		renderer.pushStyle();
		renderer.scale(scale);

		//setup
		renderer.ellipseMode(PConstants.CENTER);
		renderer.rectMode(PConstants.CORNER);

		//draw
		renderer.noFill();
		renderer.strokeWeight(UIStandards.assetStrokeWeight());
		renderer.stroke(0);
		renderer.rect(0,0, cardWidth, cardHeight, 14);
		renderer.line(0,86,cardWidth,86);

		// draw gem
		renderer.fill(UIStandards.suitColor(suit));
		renderer.stroke(UIStandards.lightGrey());
		renderer.ellipse(202,43,60,60);

		// draw point
		if(points>0) {
			renderer.fill(UIStandards.fontColorDark() );
			renderer.textFont(pointFont);
			renderer.textAlign(PConstants.LEFT);
			renderer.text(points, 13, 70);
		}

		// draw costs
		int cCounter = 0;
		for(int j=0; j<costs.length; j++){
			int c = costs[j];
			if(c>0) {
				// draw cost circle
				renderer.fill(UIStandards.suitColor(j));
				renderer.stroke(UIStandards.lightGrey());
				renderer.ellipse(37,313-cCounter*54,48,48); //cost circle

				// draw cost text
				renderer.fill(UIStandards.fontColor(j));
				renderer.textFont(costFont);
				renderer.textAlign(PConstants.CENTER);
				renderer.text(costs[j], 37,327-cCounter*54); //cost text
				cCounter++;
			}
		}
		renderer.popStyle();
		renderer.popMatrix();
	}

	public static void compileDeck(Deck d, String deckId, String destination, PApplet a){
		for(int i=0; i<d.getCardCount(); i++) {
			String cardFile = compiledCardPath(destination,deckId,i);

			if(isCardCompiled(cardFile) && !forceCompiling){
				continue;
			}

			PGraphics renderer = a.createGraphics(cardWidth, cardHeight, exportType, cardFile);
			renderer.beginDraw();

			drawCard(d,i,cardWidth, cardHeight, renderer,a);

			renderer.endDraw();
			renderer.dispose();
		}

	}

	public static boolean isCardCompiled(String cardPath){
		return  (new File(cardPath).exists());
	}


}
