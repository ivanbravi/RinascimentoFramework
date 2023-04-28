package game.exceptions;

import game.state.State;

public class TimedStaleGameException extends StaleGameException {

	public static int timer = 300;

	public TimedStaleGameException(State s) {
		super(s);
	}

}
