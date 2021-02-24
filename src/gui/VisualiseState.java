package gui;

import game.state.State;
import processing.core.PApplet;

import java.util.ArrayList;

public class VisualiseState extends RinascimentoUI {

	private String path = "errors/"+"not_really_error"+".json";

	private ArrayList<String> playtrace;
	@Override
	public void draw(){
		super.draw();
		for(String a: playtrace){
			System.out.println(a);
		}
	}

	@Override
	public State nextState() {
		noLoop();
		State s = State.load(path);
		playtrace = s.getPlaytrace();
		return s;
	}

	@Override
	public String playerName(int pID) {
		return Integer.toString(pID);
	}

	public static void main(String[] args){
		String[] processingArgs = {"Rinascimento State Visualiser"};
		RinascimentoUI ui = new VisualiseState();
		PApplet.runSketch(processingArgs, ui);
	}

}
