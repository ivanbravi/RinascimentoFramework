package statistics.types;

import log.LoggableReady;

import java.util.ArrayList;

public class NumericalStatistic implements StatisticInterface, LoggableReady {
	private transient boolean uptodate = false;
	private int count=0;

	private ArrayList<Double> history = null;

	private double avg;
	private double sd;
	private double err;

	private transient double sum;
	private transient double sqrsum;

	private transient double max = Double.NEGATIVE_INFINITY;
	private transient double min = Double.POSITIVE_INFINITY;


	public NumericalStatistic(){

	}

	public NumericalStatistic(double v){
		uptodate = true;
		count=1;
		avg = v;
		sd = 0;
		err = 0;
		sum = v;
		sqrsum = v*v;
		max = v;
		min = v;
	}

	@Override
	public void add(StatisticInterface newGameStats) {
		if(newGameStats instanceof NumericalStatistic) {
			if (this.count == 0) {
				copy(newGameStats);
			} else {
				NumericalStatistic gs = (NumericalStatistic) newGameStats;
				this.uptodate = false;
				this.count+=gs.count;
				this.sum += gs.sum;
				this.sqrsum += gs.sqrsum;
				this.max = Math.max(this.max, gs.max);
				this.min = Math.min(this.min, gs.min);
				if(history!=null){
					if(gs.history!=null) {
						this.history.addAll(gs.history);
					}else{
						this.history.add(gs.value());
					}
				}
			}
			this.uptodate = false;
		}
	}

	public void copy(StatisticInterface copyFrom){
		if(copyFrom instanceof NumericalStatistic){
			NumericalStatistic sCopy = (NumericalStatistic) copyFrom;
			if(sCopy.history!=null){
				this.history  = new ArrayList<>();
				this.history.addAll(sCopy.history);
			}else if(this.history!=null){
				this.history.add(sCopy.avg);
			}
			this.uptodate = sCopy.uptodate;
			this.count = sCopy.count;
			this.avg = sCopy.avg;
			this.sd = sCopy.sd;
			this.err = sCopy.err;
			this.sum = sCopy.sum;
			this.sqrsum = sCopy.sqrsum;
			this.max= sCopy.max;
			this.min = sCopy.min;
		}
	}

	@Override
	public double value() {
		if(!uptodate){
			updateStats();
		}
		return avg;
	}

	@Override
	public double error() {
		if(!uptodate){
			updateStats();
		}
		return err;
	}

	public void updateStats(){
		if(uptodate)
			return;

		if(count>0) {
			avg = sum / count;

			double diff = sqrsum - (avg * avg * count);
			diff = diff > 0 ? diff : 0;
			sd = Math.sqrt((diff) / count);

			err = sd / Math.sqrt(count);
		}
		uptodate = true;
	}

	@Override
	public void reset() {
		this.copy(new NumericalStatistic());
	}

	@Override
	public StatisticInterface clone() {
		NumericalStatistic clone = new NumericalStatistic();
		if(history!=null){
			clone.history = (ArrayList<Double>) this.history.clone();
		}
		clone.copy(this);
		return clone;
	}

	@Override
	public ArrayList<Double> history() {
		return history;
	}

	@Override
	public NumericalStatistic keepHistory() {
		this.history = new ArrayList<>();
		return this;
	}

	@Override
	public String toString() {
		return this.value()+"+/-"+this.error();
	}

	public static void main(String [] args){
		NumericalStatistic a = new NumericalStatistic(1);
		a.add(new NumericalStatistic(3));
		a.add(new NumericalStatistic(0));

		for(int i=0; i<1000; i++)
			a.add(new NumericalStatistic(3));

		System.out.println(a.value());
		System.out.println(a.error());
	}


	@Override
	public void jsonReady() {
		this.updateStats();
	}
}
