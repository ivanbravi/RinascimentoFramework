package game.budget;

public class ActionsBudget extends Budget {

	private int residual;
	private int original;

	public ActionsBudget(int amount){
		this.original = amount;
		this.residual = amount;
	}

	@Override
	protected void actualDecrease() {
		residual--;
	}

	@Override
	protected boolean isOver() {
		return residual<=0;
	}

	@Override
	public Budget copy() {
		ActionsBudget copy = new ActionsBudget(this.residual);
		return copy;
	}

	@Override
	public double used() {
		return ((double)original-residual)/original;
	}

	@Override
	public Budget getSplit(double perc) {
		perc = Math.min(1,perc);
		perc = Math.max(0,perc);
		int thAmount = (int) (original*perc);
		int splitAmount = Math.min(thAmount,residual);
		Budget split = new ActionsBudget(splitAmount);
		return split;
	}

	@Override
	public String toString() {
		return Integer.toString(residual);
	}
}
