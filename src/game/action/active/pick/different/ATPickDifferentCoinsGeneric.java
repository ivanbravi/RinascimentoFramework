package game.action.active.pick.different;

import game.action.ActionType;
import game.action.PlayableAction;
import game.state.State;

import java.util.Arrays;

public class ATPickDifferentCoinsGeneric implements ActionType {

	@Override
	public String toString() {
		return "Pick Different Coins";
	}

	@Override
	public int countActions(State gs, int playerId) {
		return (int) Math.pow(getEncodingBase(gs,playerId),gs.params.suitCount);
	}

	@Override
	public PlayableAction createAction(int actionId, State gs, int playerId) {
		int base = getEncodingBase(gs,playerId);
		int give = extraCoins(gs,playerId);
		int[] pickPattern = new int[gs.params.suitCount];

		decodeActionId(actionId,pickPattern,base);
		transpose(pickPattern,give);

		return new PickDifferentCoins(playerId,pickPattern);
	}

	private int getEncodingBase(State gs, int playerId){
		int extra = extraCoins(gs,playerId);
		int base = gs.params.pickDifferentAmount+extra+1;
		return base;
	}

	private int extraCoins(State gs, int playerId){
		int futureCoins = gs.getPlayerState(playerId).getCoinsCount()+gs.params.pickDifferentCount*gs.params.pickDifferentAmount;
		int extra = futureCoins-gs.params.maxCoins;
		return  extra>0?extra:0;
	}

	private static void transpose(int[] v, int delta){
		for(int i=0; i<v.length; i++){
			v[i] = v[i]-delta;
		}
	}

	public static void decodeActionId(int actionId, int[] v, int base){
		String idInCoinBase = Integer.toString(actionId,base);

		Arrays.fill(v,0);

		for(int i=0; i<idInCoinBase.length(); i++){
			v[i] = Integer.parseInt(idInCoinBase.charAt(i)+"");
		}
	}

	public static void main(String[] args){

		int take = 1; // 1
		int give = 0; // 2
		int vLen = 10; // 5
		int base = take+give+1;
		int delta = (take+give)-take;

		int v;
		int []n = new int[vLen];

		v = (int)Math.pow(base,vLen)-1;
		decodeActionId(v,n,base);
		System.out.println(v);
		System.out.println(Arrays.toString(n));
		transpose(n,delta);
		System.out.println(Arrays.toString(n)+"\n");

		v = (int)Math.pow(base,vLen-1)-1;
		decodeActionId(v,n,base);
		System.out.println(v);
		System.out.println(Arrays.toString(n));
		transpose(n,delta);
		System.out.println(Arrays.toString(n)+"\n");

		v = 123;
		decodeActionId(v,n,base);
		System.out.println(v);
		System.out.println(Arrays.toString(n));
		transpose(n,delta);
		System.out.println(Arrays.toString(n)+"\n");

		v = 542;
		decodeActionId(v,n,base);
		System.out.println(v);
		System.out.println(Arrays.toString(n));
		transpose(n,delta);
		System.out.println(Arrays.toString(n)+"\n");

	}

}
