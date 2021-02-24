package generators.decks;

import game.Parameters;
import game.state.Deck;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class SplendorDecksGenerator implements DecksGenerator {

	private static String commentSymbol = "#";
	private static String cvsSplitSymbol = ",";

	@Override
	public Deck[] getDecks(Parameters p) {
		Deck[] decks = new Deck[p.deckCount];
		for(int i=1; i<=p.deckCount; i++){
			decks[i-1] = loadDeck(p.deckPath(i));
		}
		return decks;
	}

	private static Deck loadDeck(String path, String deck){
		return loadDeck(path+deck+".csv");
	}

	private static Deck loadDeck(String source){
		Deck d = null;
		String line;

		try (BufferedReader br = new BufferedReader(new FileReader(source))){

			line = readSkippingComments(br);
			int cardsCount = Integer.parseInt(line.split(cvsSplitSymbol)[0]);
			int suitCount = Integer.parseInt(line.split(cvsSplitSymbol)[1]);

			d = new Deck(cardsCount,suitCount);

			int cardId=0, suit, points;
			int[] cost = new int[suitCount];

			while ((line = readSkippingComments(br)) != null) {
				String[] slices = line.split(cvsSplitSymbol);

				suit = Integer.parseInt(slices[0]);
				points = Integer.parseInt(slices[1]);

				fillCost(slices,2,suitCount,cost);

				try {
					d.addCard(cardId,suit,cost,points);
				}catch (Exception e){
					System.out.println("Invalid cardId");
					break;
				}

				cardId++;
			}

			if(cardId!=cardsCount){
				System.out.println("Invalid number of decks: "+cardId+" vs "+cardsCount);
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return d;
	}

	private static String readSkippingComments(BufferedReader b) throws IOException{
		String s;

		do{
			s = b.readLine();
			if(s==null){
				break;
			}
		}while (commentSymbol.equals(s.substring(0,1)));

		return s;
	}

	private static void fillCost(String[] values, int start, int length, int[] cost){
		for(int i=start;i<start+length; i++){
			cost[i-start] = Integer.parseInt(values[i]);
		}
	}

	public static void main(String[] args){
		Deck d;
		String gameVersion = "default";
		String path = "assets/"+gameVersion+"/";

		Parameters p = Parameters.load(path,Parameters.defaultName());
		SplendorDecksGenerator generator = new SplendorDecksGenerator();
		d = generator.loadDeck(path+"decks/","1");
		System.out.println(d.toString());

		d = generator.loadDeck(path+"decks/","2");
		System.out.println(d.toString());

		d = generator.loadDeck(path+"decks/","3");
		System.out.println(d.toString());

		Deck[] decks = generator.getDecks(p);
		for(Deck deck: decks){
			System.out.println(deck.toString());
		}
	}

}
