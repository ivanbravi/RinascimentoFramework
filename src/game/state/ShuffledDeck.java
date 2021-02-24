package game.state;


import java.util.Collections;
import java.util.Stack;

public class ShuffledDeck {

	private Stack<Integer> cardIDs;

	public ShuffledDeck(Deck d){
		this.cardIDs = new Stack<>();
		for(int i=0; i<d.getCardCount(); i++){
			cardIDs.push(i);
		}
		Collections.shuffle(cardIDs);
	}

	private ShuffledDeck(){
		cardIDs=new Stack<>();
	}

	public ShuffledDeck clone(){
		ShuffledDeck clone = new ShuffledDeck();
		clone.cardIDs = (Stack<Integer>) this.cardIDs.clone();
		return clone;
	}

	public int size(){
		return cardIDs.size();
	}

	public void shuffle(){
		Collections.shuffle(cardIDs);
	}

	public boolean isEmpty(){
		return cardIDs.isEmpty();
	}

	public int drawCard(){
		if(cardIDs.empty()){
			return -1;
		}
		return this.cardIDs.pop();
	}
}
