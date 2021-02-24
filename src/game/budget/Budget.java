package game.budget;

import players.BudgetOverException;

public abstract class Budget {

	private Budget parent;

	protected abstract void actualDecrease();
	protected abstract boolean isOver();

	public abstract Budget copy();
	public abstract double used();

	protected abstract Budget getSplit(double perc);

	public Budget split(double perc){
		Budget split = getSplit(perc);
		split.parent = this;
		return split;
	}

	private void parentDecrease(){
		if(parent==null)
			return;
		parent.decrease();
	}

	public void decrease(){
		actualDecrease();
		parentDecrease();
		if(isOver()){
			throw new BudgetOverException();
		}
	}

}
