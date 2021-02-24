package gui;

import game.Engine;
import game.Parameters;
import game.state.Deck;
import game.state.Noble;
import game.state.Result;
import game.state.State;
import generators.decks.SplendorDecksGenerator;
import generators.nobles.SplendorNoblesGenerator;
import players.human.ConsolePlayer;
import players.BasePlayerInterface;

public class ConsolePlay {

	public static void main(String[] args){
		String gameVersion = "test"; // default test
		Parameters params = Parameters.load("assets/"+gameVersion+"/");

		SplendorDecksGenerator decksGenerator = new SplendorDecksGenerator();
		Deck[] decks = decksGenerator.getDecks(params);

		SplendorNoblesGenerator noblesGenerator = new SplendorNoblesGenerator();
		Noble[] nobles = noblesGenerator.getNobles(params);

		BasePlayerInterface[] players = players(params);
		Engine engine = Engine.defaultEngine(players);

		State s = new State(params, decks, nobles, engine);
		System.out.println(s.toString());

		Result r =  engine.playFullGame(s);
		System.out.println(r);
	}

	private static BasePlayerInterface[] players(Parameters p){
		BasePlayerInterface[] players = new BasePlayerInterface[p.playerCount];
		for(int i=0; i<players.length; i++)
			players[i] = new ConsolePlayer();
		return players;
	}

}
