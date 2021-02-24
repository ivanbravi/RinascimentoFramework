package game.action;

import game.state.State;

import java.util.ArrayList;
import java.util.Arrays;

public class StatelessDecEnc implements ActionsEncoderDecoder{

	ArrayList<ActionType> types;

	public StatelessDecEnc(){
		types = new ArrayList<>();
	}

	@Override
	public String toString() {
		String[] typeNames = new String[types.size()];
		for(int i=0; i<typeNames.length; i++)
			typeNames[i] = types.get(i).toString();
		return Arrays.toString(typeNames).replaceAll("\\[|\\]","");
	}

	@Override
	public void addManagedActionType(ActionType at) {
		types.add(at);
	}

	@Override
	public int countActions(State gs, int playerId) {
		int counter = 0;

		for(int i=0; i<types.size(); i++){
			ActionType at = types.get(i);
			counter += at.countActions(gs,playerId);
		}

		return counter;
	}

	@Override
	public PlayableAction decodeAction(int actionId, State gs, int playerId) {

		ActionType type = types.get(0);
		int typeSpecificActionId=actionId;

		for(int i=0; i<types.size(); i++) {
			ActionType currType = types.get(i);
			int lastTypeSize = currType.countActions(gs,playerId);
			if (typeSpecificActionId - lastTypeSize < 0) {
				break;
			} else {
				type = currType;
				typeSpecificActionId -= lastTypeSize;
			}
		}

		return type.createAction(typeSpecificActionId,gs,playerId);
	}




}
