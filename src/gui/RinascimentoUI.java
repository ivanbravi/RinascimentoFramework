package gui;

import game.state.Noble;
import game.state.PlayerState;
import game.state.State;
import gui.compilers.CardCompiler;
import gui.compilers.NobleCompiler;
import processing.core.PApplet;
import processing.core.PFont;

public abstract class RinascimentoUI extends PApplet {

	private volatile State s;

	private static float baseWidth = 800;
	private static float baseHeight = 600;

	private float relativeScale = 1;

	private static float boardHeightSpace = 350;

	public void settings() {
		float scale = 950/baseHeight;
		size((int) (800*scale), (int) (600*scale), JAVA2D);
		UIStandards.setup(this);
		relativeScale = Math.min(width/baseWidth,height/baseHeight);
	}

	protected abstract State nextState();
	public abstract String playerName(int pID);

	public State getState() {
		return s;
	}

	public void setup(){
		s = nextState();
		drawState();
	}

	public void draw(){
		s = nextState();
		drawState();
	}

	private void drawState(){
		background(255);
		translate((width-baseWidth*relativeScale)/2.0f,(height-baseHeight*relativeScale)/2.0f);
		scale(relativeScale);

		pushMatrix();
		for(int i=0; i<s.playerStates.length; i++){
			drawPlayerState(s.playerStates[i]);
		}
		popMatrix();

//		stroke(0);
//		strokeWeight(UIStandards.strokeWeight());
//		line(0,350,baseWidth,350);

		drawBoard();
	}

	private void drawBoard(){
		drawTicks();
		drawCoins();
		if(s.isInitialised())
			drawNobles();
		drawCards();
	}

	private void drawTicks(){
		PFont font = createFont(UIStandards.font(), 20);
		textAlign(LEFT);
		textFont(font);
		fill(UIStandards.fontColorDark());
		text("["+s.getTick()+"]:"+(s.getTick()%s.params.playerCount),10,30);
	}

	private void drawCards(){

		int cardsDisplacement = 530;
		int cardsSpaceWidth = 274;
		int cardsSpaceHeight = 350;
		int nCards=s.params.cardsOnTableCount;
		int nDecks=s.params.deckCount;
		float cardRatio = CardCompiler.cardRatio();
		PFont font = createFont(UIStandards.font(), 25);

		float cw = 55;
		float ch = 55/cardRatio;
		float dw,dh;

		if(cardsSpaceWidth<nCards*cw+1/5*cw*(nCards+1)){
			cw = (5*cardsSpaceWidth)/(6*nCards+1);
		}

		if(cardsSpaceHeight<nDecks*ch+1/5*ch*(nDecks+1)){
			ch = (5*cardsSpaceHeight)/(6*nDecks+1);
		}

		float tmpRatio = cw/ch;

		if(tmpRatio > cardRatio){
			// width bigger -> use height
			cw = ch*cardRatio;
		}else{
			// height bigger -> use width
			ch = cw/cardRatio;
		}

		dw = (cardsSpaceWidth-cw*nCards)/(nCards+1);
		dh = (cardsSpaceHeight-ch*nDecks)/(nDecks+1);

		pushMatrix();
		pushStyle();
		translate(cardsDisplacement,0);
		translate(-cardsSpaceWidth/2.0f,0);
		translate(dw,dh);

		rectMode(CORNER);

		for(int deckId=0; deckId<s.params.deckCount; deckId++){
			pushMatrix();
			for(int cardPosition=0; cardPosition<s.params.cardsOnTableCount; cardPosition++){
				int cardId = s.board[deckId][cardPosition];
				if(cardId>=0) {
					CardCompiler.drawCard(s.decks[deckId], cardId, cw, ch, this.getGraphics(), this);
				}
				translate(cw + dw, 0);
			}
			popMatrix();
			translate(0,ch+dh);
		}

		popMatrix();

		int deckDisplacement = 350;

		pushMatrix();

		translate(deckDisplacement-45,dh);
		stroke(UIStandards.lightGrey());
		strokeWeight(UIStandards.strokeWeight());


		textFont(font);
		textAlign(CENTER);

		for(int deckId=0; deckId<s.params.deckCount; deckId++){
			if(s.deckStacks[deckId].isEmpty()) {
				fill(UIStandards.lightGrey());
				rect(0,0, cw, ch, 0.06f*cw);
			} else {
				fill(UIStandards.lightGrey());
				text(s.deckStacks[deckId].size(),cw/2.0f, ch/2.0f+10);
				noFill();
				rect(0,0, cw, ch, 0.06f*cw);
			}
			translate(0,ch+dh);
		}

		popStyle();
		popMatrix();
	}

	private void drawNobles(){
		int n = s.nobles.length;
		float d;
		float sz;

		float nobleDisplacement = 240;

		sz = dispSize(n,0.2f,boardHeightSpace,55);
		d = dispEmpty(n,0.2f,boardHeightSpace,55);

		pushMatrix();
		pushStyle();

		translate(nobleDisplacement, 0);
		translate(-sz/2,d);

		for(int i=0; i<n; i++){
			noStroke();
			if(s.isNobleTaken[i]){
				rectMode(CORNER);
				stroke(UIStandards.lightGrey());
				fill(UIStandards.lightGrey());
				rect(0,0,sz,sz,sz*7.0f/43.0f);
			}else{
				NobleCompiler.drawNoble(s.nobles[i], sz, sz, this.getGraphics(), this);
			}
			translate(0,sz+d);

		}

		popStyle();
		popMatrix();

	}

	private void drawCoins(){
		// |-0----0-0-0-0-0-0-|
		float maxSize = 42;
		float cSize;
		float divSpace;
		float n=s.params.suitCount;

		float disp = 168;

		PFont font = createFont(UIStandards.font(),34);

		if(boardHeightSpace <(n+1)*(4/3*maxSize)+4/3*maxSize){
			cSize = (3* boardHeightSpace)/(4*(n+2));
			divSpace = 1.0f/3.0f*cSize;
		}else{
			cSize = maxSize;
			divSpace = (boardHeightSpace -maxSize*(n+1))/(n+5);
		}

		int fontColor;
		pushMatrix();
		ellipseMode(CENTER);
		textAlign(CENTER);
		translate(disp,0);
		translate(0,divSpace+cSize/2);
		strokeWeight(UIStandards.strokeWeight());
		for(int i=0; i<s.params.suitCount; i++){
			if(s.coinStacks[i]==0){
				fontColor = UIStandards.fontColorBright();
				fill(UIStandards.lightGrey());
				stroke(UIStandards.suitColor(i));
				ellipse(0,0, cSize, cSize);
			}else{
				fontColor = UIStandards.fontColor(i);
				stroke(UIStandards.lightGrey());
				fill(UIStandards.suitColor(i));
				ellipse(0,0, cSize, cSize);
			}

			fill(fontColor);
			textFont(font);
			text(s.coinStacks[i],0,cSize*12.0f/42.0f);

			translate(0,divSpace+cSize);
		}
		translate(0,2*divSpace);

		pushMatrix();
		if(s.goldStack==0){
			fontColor = UIStandards.fontColorBright();
			stroke(UIStandards.goldColor());
			fill(UIStandards.lightGrey());
		}else{
			fontColor = UIStandards.fontColorDark();
			stroke(UIStandards.lightGrey());
			fill(UIStandards.goldColor());
		}
		ellipse(0,0, cSize, cSize);
		fill(fontColor);
		textFont(font);
		text(s.goldStack,0,cSize*12.0f/42.0f);
		popMatrix();

		popMatrix();
	}

	private float dispSize(int n, float p, float x, float maxS){
		if(x<maxS*n+p*maxS*(n+1)){
			return x/(n+p*(n+1));
		}else{
			return maxS;
		}
	}

	private float dispEmpty(int n, float p, float x, float maxS){
		float s = dispSize(n,p,x,maxS);
		if(s!=maxS){
			return s*p;
		}
		return (x-s*n)/(n+1);
	}

	private void drawPlayerState(PlayerState ps){
		float displ = Math.min(200.0f,baseWidth/s.params.playerCount);
		PFont f = createFont(UIStandards.font(),16);
		pushMatrix();
		translate(0,350);
		translate(displ*ps.getId(),0); // horizontal displacement

		fill(0);
		textAlign(LEFT);
		textFont(f);
		text(playerName(ps.getId()),9.0f,28.5f);

		textAlign(RIGHT);
		text("["+ps.points+"]",186f,28.5f);

		float cSize=dispSize(s.params.suitCount,0.2f, 135,24);
		float d=dispEmpty(s.params.suitCount,0.2f, 135,24);

		ellipseMode(CENTER);
		rectMode(CENTER);
		strokeWeight(UIStandards.strokeWeight());
		stroke(UIStandards.lightGrey());

		pushMatrix();
		translate(0,60);
		pushMatrix();
		translate(d+cSize/2.0f,0);
		textAlign(CENTER);
		int fontColor;
		for(int i=0; i<s.params.suitCount; i++){

			if(ps.coins[i]==0){
				fontColor = UIStandards.fontColorBright();
				stroke(UIStandards.suitColor(i));
				fill(UIStandards.lightGrey());
			}else{
				fontColor = UIStandards.fontColor(i);
				fill(UIStandards.suitColor(i));
				stroke(UIStandards.lightGrey());
			}

			ellipse(0,0,cSize,cSize);
			fill(fontColor);
			text(ps.coins[i],0-1,cSize*5.5f/24.0f);

			pushMatrix();
			if(ps.gems[i]==0){
				fontColor = UIStandards.fontColorBright();
				stroke(UIStandards.suitColor(i));
				fill(UIStandards.lightGrey());
			}else{
				fontColor = UIStandards.fontColor(i);
				fill(UIStandards.suitColor(i));
				stroke(UIStandards.lightGrey());
			}
			translate(0,36.0f);
			rect(0,0,cSize,cSize,5.0f);
			fill(fontColor);
			text(ps.gems[i],0-1,cSize*5.5f/24.0f);
			popMatrix();

			translate( cSize+d,0);
		}
		popMatrix();

		if(ps.gold==0){
			fontColor = UIStandards.fontColorBright();
			stroke(UIStandards.goldColor());
			fill(UIStandards.lightGrey());
		}else{
			fontColor = UIStandards.fontColorDark();
			stroke(UIStandards.lightGrey());
			fill(UIStandards.goldColor());
		}

		ellipse(170,0,cSize,cSize);
		fill(fontColor);
		text(ps.gold,170,cSize*5.5f/24.0f);
		popMatrix();

		// reserved
		if(s.params.maxReserveCards>0) {
			pushMatrix();
			translate(0, 150);
			cSize = dispSize(s.params.maxReserveCards, 0.1f, 200, 43);
			d = dispEmpty(s.params.maxReserveCards, 0.1f, 200, 43);

			translate(cSize / 2.0f + d, 0);
			for (int i = 0; i < s.params.maxReserveCards; i++) {
				if (ps.reservedCardIds[i] != -1) {
					pushMatrix();
					translate(-cSize/2.0f,-cSize / (250.0f / 350.0f)/2.0f);
					CardCompiler.drawCard(s.decks[ps.reservedDeckIds[i]],
							ps.reservedCardIds[i],
							cSize,
							cSize / (250.0f / 350.0f),
							this.getGraphics(),
							this);
					popMatrix();
				} else {
					noFill();
					stroke(UIStandards.lightGrey());
					strokeWeight(1.0f);
					rect(0, 0, cSize, cSize / (250.0f / 350.0f),cSize*(3.5f/43.0f));
				}
				translate(cSize + d, 0);
			}
			popMatrix();
		}


		// nobles

		pushMatrix();
		pushStyle();

		translate(0,218);

		if(!ps.nobles.isEmpty()){
			cSize = dispSize(ps.nobles.size(),0.2f,200, 43);
			d = dispEmpty(ps.nobles.size(),0.2f,200, 43);

			translate(cSize / 2.0f + d, 0);
			for(int i=0; i<ps.nobles.size(); i++){
				pushMatrix();
				translate(-cSize/2.0f,-cSize/2.0f);
				Noble n = Noble.find(s.nobles,ps.nobles.get(i));
						// s.nobles[ps.nobles.get(i)];
				NobleCompiler.drawNoble(n,cSize,cSize, getGraphics(), this);
				popMatrix();
				translate(cSize+d,0);
			}
		}

		popStyle();
		popMatrix();

		popMatrix();
	}

//	public static void main(String[] args){
//		String[] processingArgs = {"Rinascimento"};
//		RinascimentoUI ui = new RinascimentoUI();
//		PApplet.runSketch(processingArgs, ui);
//	}

}
