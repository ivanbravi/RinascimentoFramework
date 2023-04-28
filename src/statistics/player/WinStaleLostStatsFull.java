package statistics.player;

import game.AbstractGameState;
import statistics.GameStats;
import statistics.adapters.WonStaleLossAdapter;
import statistics.types.StatisticInterface;

public class WinStaleLostStatsFull extends PlayerNumericalStatistic{

	transient WonStaleLossAdapter adapter;

	int winCount = 0;
	int lossCount = 0;
	int tieCount = 0;
	int staleCount = 0;
	int totalCount = 0;

	public enum WinStaleLostStatsFullMode{
		WIN, LOSS, TIE, STALE, WIN_TIE, LOSS_TIE, TIE_STALE, WIN_TIE_STALE, LOSS_TIE_STALE;
	}

	WinStaleLostStatsFullMode mode;

	public WinStaleLostStatsFull(WonStaleLossAdapter adapter, WinStaleLostStatsFullMode mode){
		super();
		this.adapter = adapter;
		this.mode = mode;
	}

	private WinStaleLostStatsFull(){}

	public WinStaleLostStatsFull(double value, WinStaleLostStatsFullMode mode) {
		super(value);
		this.mode = mode;
		if(value==WonStaleLossAdapter.TIE){
			this.tieCount++;
		}else if(value<0){
			this.lossCount++;
		}else if(value==WonStaleLossAdapter.STALE){
			this.staleCount++;
		}else if(value==WonStaleLossAdapter.WON){
			this.winCount++;
		}
		totalCount++;
	}

	@Override
	public StatisticInterface clone() {
		WinStaleLostStatsFull clone = new WinStaleLostStatsFull(adapter, mode);
		clone.copy(this);
		return clone;
	}

	@Override
	public void add(StatisticInterface newGameStats) {
		super.add(newGameStats);
		if(newGameStats instanceof WinStaleLostStatsFull) {
			this.winCount += ((WinStaleLostStatsFull) newGameStats).winCount;
			this.lossCount += ((WinStaleLostStatsFull) newGameStats).lossCount;
			this.tieCount += ((WinStaleLostStatsFull) newGameStats).tieCount;
			this.staleCount += ((WinStaleLostStatsFull) newGameStats).staleCount;
			this.totalCount += ((WinStaleLostStatsFull) newGameStats).totalCount;
		}
	}

	@Override
	public double value() {
		double value = 0;
		switch (mode){
			case WIN:{
				value = winCount;
			} break;
			case LOSS:{
				value =  lossCount;
			} break;
			case TIE:{
				value = tieCount;
			}break;
			case STALE:{
				value =  staleCount;
			}break;
			case WIN_TIE: {
				value = winCount+tieCount/2.0;
			} break;
			case LOSS_TIE:{
				value =  lossCount + tieCount/2.0;
			}break;
			case TIE_STALE:{
				value =  tieCount+staleCount;
			}break;
			case WIN_TIE_STALE:{
				value =  winCount+(tieCount+staleCount)/2.0;
			}break;
			case LOSS_TIE_STALE:{
				value =  lossCount+(tieCount+staleCount)/2.0;
			}break;
		}
		return value/totalCount;
	}

	public void copy(WinStaleLostStatsFull c) {
		super.copy(c);
		this.winCount = c.winCount;
		this.lossCount = c.lossCount;
		this.tieCount = c.tieCount;
		this.staleCount = c.staleCount;
		this.totalCount = c.totalCount;
	}

	@Override
	public String toString() {
		return "winCount=" + winCount +
				", lossCount=" + lossCount +
				", tieCount=" + tieCount +
				", staleCount=" + staleCount +
				", totalCount=" + totalCount +
				", mode=" + mode;
	}

	@Override
	public GameStats create(AbstractGameState gs, String player) {
		double value = adapter.wonStaleLossReward(gs,player);
		WinStaleLostStatsFull stat = new WinStaleLostStatsFull(value, mode);
		stat.setPlayer(player);
		return stat;
	}


	public static void main(String[] args) {
		WinStaleLostStatsFull a = new WinStaleLostStatsFull();
		a.winCount = 1;
		a.lossCount = 1;
		a.tieCount = 1;
		a.staleCount = 1;
		a.totalCount = 4;

		for(WinStaleLostStatsFullMode mode: WinStaleLostStatsFullMode.values()){
			a.mode = mode;
			System.out.println(a.mode.name()+"->"+a.value());
		}

		System.out.println();
		for(WinStaleLostStatsFullMode mode: WinStaleLostStatsFullMode.values())
			System.out.print("\""+mode.name().toLowerCase()+"\""+",");
	}
}
