package utils;

import java.util.Random;

public class RandomBased {
	protected Random rnd;

	public RandomBased(){
		rnd = new Random();
	}

	public void setRandomSource(Random rnd){
		this.rnd = rnd;
	}

	public void setSeed(long seed){
		if(rnd!=null)
			this.rnd.setSeed(seed);
	}

}
