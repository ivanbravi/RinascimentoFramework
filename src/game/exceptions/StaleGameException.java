package game.exceptions;

import game.state.State;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class StaleGameException extends RuntimeException {

	public static boolean verbose = true;
	public static boolean doSaveState = false;

	private static Calendar calendar = Calendar.getInstance();
	private static SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss");
	private static String errorOut = "errors/";
	private static String ext = ".json";

	private static int id=0;

	private State s;

	public StaleGameException(State s){
		this.s = (State) s.copy();
		if(doSaveState) {
			String timeStamp = format.format(calendar.getTime());
			State.save(errorOut + timeStamp + "_" + id + ext, this.s);
		}
		id++;
	}

	@Override
	public void printStackTrace() {
		if(verbose) {
			System.out.println("• [STALE STATE BEGIN]");
			System.out.println(s.toString());
			System.out.println("• [STALE STATE END]");
		}
		super.printStackTrace();
	}
}
