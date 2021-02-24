package game.state.vectorise;

import game.Engine;
import game.Factory;
import game.Parameters;
import game.state.State;
import players.BasePlayerInterface;
import players.ai.explicit.SafeRandomPlayer;

import java.util.Arrays;
import java.util.List;

public class Test {

	public static void main(String[] args) {
		double[] f;
		Parameters parameters = Parameters.load("assets/defaultx2/");
//		Engine engine = Engine.defaultEngine();
//		State state = Factory.createState(parameters,engine);

		Vectoriser.Params.RESCALE = true;

		VectoriseState vs = new VectoriseState(parameters);
		VectorisePlayer vp  = new VectorisePlayer(parameters);
		VectoriseCards vc = new VectoriseCards(parameters);
		VectoriseNobles vn = new VectoriseNobles(parameters);
		AllPlayersVectoriser vpp  = new AllPlayersVectoriser(parameters);
		VectoriseStateDeep vss = new VectoriseStateDeep(parameters);
		PlayerStateVectoriser psv = new PlayerStateVectoriser(vp,vs);
		PlayerStateVectoriser all_psv = new PlayerStateVectoriser(vpp,vs);
		PlayerStateVectoriser complete_psv = new PlayerStateVectoriser(vp,vss);
		PlayerStateVectoriser all_complete_psv = new PlayerStateVectoriser(vpp,vss);


//		Vectoriser v = vpp;
//		OneStepLookAheadAgent osla = new OneStepLookAheadAgent();
//		LinearHeuristic lh = new LinearHeuristic(v.size());
//		osla.setHeuristic(new StateBasedVectorHeuristic(lh, v));

//		engine.setPlayers(new BasePlayerInterface[]{
//				new SafeRandomPlayer(),
//				//osla,
//				new SafeRandomPlayer()});
//
//		for(int i=0; i<10; i++){
//			engine.stepPlay(state);
//		}
//		State.save("errors/not_really_error.json",state);

		State state = State.load("errors/not_really_error.json");

		List<Vectoriser> vvv = Arrays.asList(vs,vp,vn,vpp,vss,psv,all_psv,complete_psv,all_complete_psv,vc);
		for(Vectoriser v: vvv) {
			System.out.println(v);

			Vectoriser.Params.RESCALE = true;
			f = v.vectorise(state, 0);
			System.out.println(Arrays.toString(f));

			Vectoriser.Params.RESCALE = false;
			f = v.vectorise(state, 0);
			System.out.println(Arrays.toString(f)+"\n");
		}

	}

}
