package hyper.agents.eventbased;

import evodef.AnnotatedSearchSpace;
import hyper.utilities.CompleteAnnotatedSearchSpace;
import ntbea.params.Param;

import java.util.Arrays;

public class CombinedSearchSpace extends CompleteAnnotatedSearchSpace {

	private CompleteAnnotatedSearchSpace[] spaces;

	public CombinedSearchSpace(CompleteAnnotatedSearchSpace[] spaces){
		this.spaces = spaces;
		int size = 0;
		for(CompleteAnnotatedSearchSpace s :spaces)
			size += s.nDims();

		params = new Param[size];
		dimensions = new int[size];
		int start=0;
		for(CompleteAnnotatedSearchSpace s :spaces) {
			System.arraycopy(s.getParams(), 0, params, start, s.nDims());
			for(int i=0; i<s.nDims(); i++){
				dimensions[start+i] = s.nValues(i);
			}
			 start += s.nDims();
		}

	}

	public AnnotatedSearchSpace getSpace(int spaceIndex){
		return spaces[spaceIndex];
	}

	public int nDims(int spaceIndex) {
		return spaces[spaceIndex].nDims();
	}

	public int[] splitSolution(int[] solution, int spaceIndex){
		int start = 0;
		int end;
		for(int i=0; i<spaceIndex; i++){
			start+=spaces[i].nDims();
		}
		end = start+spaces[spaceIndex].nDims();
		return Arrays.copyOfRange(solution,start,end);
	}

}
