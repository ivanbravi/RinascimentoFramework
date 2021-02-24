package gui;

import benchmarks.PlayRinascimento;
import game.Engine;
import game.Factory;
import game.Parameters;
import game.log.RStateEventDispatcher;
import game.log.RinascimentoEventDispatcher;
import game.state.State;
import log.entities.event.EventVectorLogger;
import players.BasePlayerInterface;
import players.ai.explicit.RandomPlayer;
import players.human.ConsolePlayer;
import players.human.UIPlayer;
import processing.core.PApplet;
import utils.AgentsConfig;

import java.util.Arrays;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class ConsolePlayUI extends RinascimentoUI {

	private boolean isStartingUp = true;
	private boolean pauseBetweenTurns = false;

	private Engine engine;
	private State s = null;

	protected String gameVersion = "iggiconf";

	protected BufferedReader console;

	public String gameDirectory(){
		return "assets/"+gameVersion+"/";
	}

	protected List<Class> players(){
		return Arrays.asList(
				ConsolePlayer.class,
				RandomPlayer.class,
				RandomPlayer.class,
				RandomPlayer.class
		);
	}

	protected State nextState(){
		if(isStartingUp) {
			Parameters params = Parameters.load(gameDirectory());
			engine = Factory.createEngine(params, players());
			//CUSTOM
			UIPlayer uiPlayer = new UIPlayer();
			UIPlayerInput uiInput = new UIPlayerInput(this);
			uiPlayer.setInput(uiInput);

			AgentsConfig playerData = AgentsConfig.readJson("agents/iggi19.json");
			BasePlayerInterface[] players = PlayRinascimento.decodePlayers(playerData, params);
			BasePlayerInterface opponent = players[0];
			opponent.setName("derorriMSTCM");

			engine.setPlayers(new BasePlayerInterface[]{opponent, uiPlayer});
			Engine.shufflePlayers = false;
			//END CUSTOM
			s = Factory.createState(params,engine);
			isStartingUp = false;
		}else{
			if(pauseBetweenTurns){
				pausePlay();
			}
			engine.stepPlay(s);
		}
		return s;
	}

	@Override
	public String playerName(int pID) {
		return engine.getPlayerNames()[pID];
	}

	public void pausePlay(){
		if(console == null){
			console = new BufferedReader(new InputStreamReader(System.in));
		}
		System.out.println("[PAUSED] Enter to unpause");
		try{
			console.readLine();
		}catch (IOException e){
			e.printStackTrace();
		}
	}

	public static void main(String[] args){
		String[] processingArgs = {"Rinascimento"};
		RinascimentoUI ui = new ConsolePlayUI();
		PApplet.runSketch(processingArgs, ui);
	}

}
