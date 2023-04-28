package game.action;

import game.state.State;
import utils.profiling.Profiler;

public abstract class  PlayableAction implements Action {

	public static boolean isVerbose = false;
	public static Profiler profiler;

	protected int playerId;

	public PlayableAction(int playerId){
		this.playerId = playerId;
	}

	public static void attatchProfiler(Profiler p){
		profiler = p;
	}

	public boolean perform(State gs){
		if(profiler != null)
			profiler.actionPerformed();
		gs.addActionPerformed(this);
		return true;
	}

	@Override
	public int getPlayerId(){
		return playerId;
	}

	@Override
	public abstract boolean canPerform(State gs);

	protected boolean failedAction(){
		if(isVerbose){
			System.out.println("(ERROR) couldn't perform action\n"+toString());
		}
		return false;
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof PlayableAction))
			return false;

		PlayableAction pa = (PlayableAction) obj;
		if(pa.playerId != this.playerId)
			return false;

		return true;
	}

	@Override
	public String toString() {
		return "P"+playerId;
	}
}
