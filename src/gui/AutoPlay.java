package gui;

import game.Engine;
import game.Factory;
import game.Parameters;
import game.state.Result;
import game.state.State;
import players.BasePlayerInterface;
import players.ai.explicit.RandomEventLogTestPlayer;
import players.ai.explicit.RandomPlayer;

import processing.core.PApplet;

public class AutoPlay extends RinascimentoUI {

	private boolean isStartingUp = true;
	private boolean pauseBetweenTurns = true;

	private int pauseTime = 500;
	private int pauseBetweenGames = 10000;

	private Engine engine;
	private State s = null;
	private Result r = null;

	@Override
	public void settings(){
		//fullScreen();
		super.settings();
	}

	public String gameDirectory(){
		String gameVersion = "default";
		return "assets/"+gameVersion+"/";
	}

	private BasePlayerInterface[] players(){
		 /* LOAD PLAYERS FROM CONFIG FILE*/
//		String pData = "agents/agents.json";
//		AgentsConfig playerData = AgentsConfig.readJson(pData);
//		BasePlayerInterface[] players = PlayRinascimento.decodePlayers(playerData);

		/* META TUNING AGENT USING RHEA FACTORY */
//		AgentFactorySpace afs = new RHEAAgentFactory().
//				setHeuristic(new PointsHeuristic()).
//				setSearchSpace(CompleteAnnotatedSearchSpace.load("agents/RHEAParams.json"));
//
//		BasePlayerInterface[] a = new BasePlayerInterface[]{
//				new MetaTuningAgent(afs),
//				new RandomPlayer(),
//				new RandomPlayer(),
//				new RandomPlayer()
//		};
//		a[0].setName("META RHEA");
//		a[1].setName("Player_2");
//		a[2].setName("Player_3");
//		a[3].setName("Player_4");

		BasePlayerInterface[] a = new BasePlayerInterface[]{
				new RandomEventLogTestPlayer(),
				new RandomPlayer(),
				new RandomPlayer(),
				new RandomPlayer()
		};

		return a;
	}

//	private List<Class> players(){
//		return Arrays.asList(
//				RandomPlayer.class,
//				RandomPlayer.class,
//				RandomPlayer.class,
//				RandomPlayer.class
//		);
//	}

	public State nextState(){

		if(r!=null){
			if (r.isGameOver){
				System.out.println(r.toString());
				pauseBetweenTurns();
				isStartingUp = true;
				r = null;
			}
		}

		if(isStartingUp) {
			Parameters params = Parameters.load(gameDirectory());
			engine.shufflePlayers = true;
			engine = Engine.defaultEngine(players());
			s = Factory.createState(params,engine);
			isStartingUp = false;
		}else{
			if(pauseBetweenTurns){
				pausePlay();
			}
			r = engine.stepPlay(s);
		}
		return s;
	}

	@Override
	public String playerName(int pID) {
		return engine.getPlayerNames()[pID];
	}

	public void pausePlay(){
		delay(pauseTime);
	}

	public void pauseBetweenTurns(){
		delay(pauseBetweenGames);
	}

	public static void main(String[] args){
		String[] processingArgs = {"Rinascimento Automatic Play"};
		Engine.shufflePlayers = false;
		AutoPlay ui = new AutoPlay();
		PApplet.runSketch(processingArgs, ui);
	}
}
