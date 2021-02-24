package game.state;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Noble {

	private int id;
	private int[] cost;
	private int points;

	public Noble(int id, int points, int[] cost){
		this.id = id;
		this.points = points;
		this.cost = Arrays.copyOf(cost,cost.length);
	}

	public int getPoints(){
		return points;
	}

	public int getId(){
		return id;
	}

	public int[] getCost(){
		return cost;
	}

	@Override
	public String toString() {
		return "["+id+"]"+
				"\n\t cost: "+Arrays.toString(cost)+
				"\n\t points: "+points;
	}

	public static Noble find(Noble[] nobles, int id){
		Noble n = null;
		for(int i=0; i<nobles.length; i++){
			if(nobles[i].getId()==id) {
				n = nobles[i];
				break;
			}
		}
		assert(n!=null);
		return n;
	}

	public static Noble[] pickNobles(Noble[] all, int amount){
		if(amount<=0){
			return null;
		}

		if(amount>all.length){
			amount = all.length;
			System.out.println("NOT ENOUGH NOBLES: resizing nobles");
		}

		Noble[] selection = new Noble[amount];
		ArrayList<Noble> c = new ArrayList<>(Arrays.asList(all));
		Collections.shuffle(c);

		c.subList(0,amount).toArray(selection);

		return selection;
	}

}
