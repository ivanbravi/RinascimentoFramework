package game;

import game.log.RStateEventDispatcher;
import game.state.Deck;
import game.state.Noble;
import game.state.State;
import generators.decks.SplendorDecksGenerator;
import generators.nobles.SplendorNoblesGenerator;
import players.BasePlayerInterface;
import players.ai.explicit.RandomPlayer;

import java.util.ArrayList;
import java.util.List;

public class Factory {

	public static Engine createEngine(Parameters params, List<Class> players){
		BasePlayerInterface []playerInstances = new BasePlayerInterface[params.playerCount];

		if (players==null){
			players = new ArrayList<>();
		}

		for(int i=0; i<Math.max(params.playerCount,players.size()); i++){
			if(i<players.size()){
				try {
					playerInstances[i] = (BasePlayerInterface) players.get(i).getConstructor().newInstance((Object) null);
					System.out.println("Loaded player: "+ players.get(i).getName());
				}catch (Exception e){
					System.out.println("Too many players! Player"+players.get(i).getName()+" will NOT play.");
				}
			}else{
				System.out.println("Not enough players: creating random player");
				playerInstances[i] = new RandomPlayer();
			}
		}

		return Engine.defaultEngine(playerInstances);
	}

	public static State createStateWithLogger(Parameters params, Engine e, RStateEventDispatcher logger){
		State s = createState(params, e);
		s.setEventDispatcher(logger);
		return s;
	}

	public static State createState(Parameters params, Engine e){
		SplendorNoblesGenerator noblesGenerator = new SplendorNoblesGenerator();
		Noble[] nobles = noblesGenerator.getNobles(params);

		SplendorDecksGenerator decksGenerator = new SplendorDecksGenerator();
		Deck[] decks = decksGenerator.getDecks(params);

		return new State(params,decks,nobles,e);
	}

}
