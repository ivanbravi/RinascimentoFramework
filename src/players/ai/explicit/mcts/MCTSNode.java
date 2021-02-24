package players.ai.explicit.mcts;

import game.action.Action;

import java.util.ArrayList;

public class MCTSNode {

	Action action;
	MCTSNode parent;
	ArrayList<MCTSNode> children = new ArrayList<>();

	protected int n;
	protected int depth;
	protected double value;

	public MCTSNode(Action action, MCTSNode parent){
		this.action = action;
		this.parent = parent;
		if(parent==null){
			this.depth = 0;
		} else {
			this.depth = parent.depth + 1;
		}
	}

	public boolean nodeContainsAction(Action a){

		if(a==null)
			return false;

		for(int i=0; i<children.size(); i++)
			if(children.get(i).action.equals(a))
				return true;

		return false;
	}

	public void addValue(double value){
		n++;
		this.value+=value;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		String indent = "";

		for(int i=0; i<depth; i++){
			indent += "\t";
		}

		if (parent == null){
			builder.append("root\n");
		}else{
			String actionString = action.toString().replaceAll("\t"," ").replaceAll("\n","");
			builder.append(indent+"action: "+actionString+"\n");
			builder.append(indent+"value: "+value+"\tn: "+n+"\n");
		}

		if(children.size()>0) {
			builder.append(indent + "•••••••••••••••••••••••••••••••••" + "\n");
			for (MCTSNode child : children) {
				builder.append(child.toString());
			}
			builder.append(indent + "•••••••••••••••••••••••••••••••••" + "\n");
		}

		return builder.toString();
	}
}
