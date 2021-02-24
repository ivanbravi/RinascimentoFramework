package game.action;

import game.state.State;

public abstract class  PlayableAction implements Action {

	public static boolean isVerbose = false;

	protected int playerId;

	public PlayableAction(int playerId){
		this.playerId = playerId;
	}

	public boolean perform(State gs){
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
