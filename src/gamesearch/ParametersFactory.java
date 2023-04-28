package gamesearch;

import game.Parameters;
import hyper.utilities.CompleteAnnotatedSearchSpace;
import utils.CopyToClipboard;

import java.util.Arrays;

public class ParametersFactory {

	CompleteAnnotatedSearchSpace gameSpace;

	public ParametersFactory(CompleteAnnotatedSearchSpace gameSpace){
		this.gameSpace = gameSpace;
	}

	public CompleteAnnotatedSearchSpace getGameSpace() {
		return gameSpace;
	}

	public Parameters getParameters(int[] solution){
		Parameters p = Parameters.defaultParameters();

		p.playerCount 			= ParametersSpace.extract(gameSpace.getParams()[0 ],solution[0 ]);
		p.suitCount				= ParametersSpace.extract(gameSpace.getParams()[1 ],solution[1 ]);
		p.coinCount				= ParametersSpace.extract(gameSpace.getParams()[2 ],solution[2 ]);
		p.goldCount				= ParametersSpace.extract(gameSpace.getParams()[3 ],solution[3 ]);
		p.maxCoins				= ParametersSpace.extract(gameSpace.getParams()[4 ],solution[4 ]);
		p.deckCount				= ParametersSpace.extract(gameSpace.getParams()[5 ],solution[5 ]);
		p.cardsOnTableCount		= ParametersSpace.extract(gameSpace.getParams()[6 ],solution[6 ]);
		p.noblesCount			= ParametersSpace.extract(gameSpace.getParams()[7 ],solution[7 ]);
		p.endGameScore			= ParametersSpace.extract(gameSpace.getParams()[8 ],solution[8 ]);
		p.maxReserveCards		= ParametersSpace.extract(gameSpace.getParams()[9 ],solution[9 ]);
		p.pickDifferentCount	= ParametersSpace.extract(gameSpace.getParams()[10],solution[10]);
		p.pickDifferentAmount	= ParametersSpace.extract(gameSpace.getParams()[11],solution[11]);
		p.pickSameAmount		= ParametersSpace.extract(gameSpace.getParams()[12],solution[12]);
		p.pickSameMinCoins		= ParametersSpace.extract(gameSpace.getParams()[13],solution[13]);

		return p;
	}

	public ParametersFactory resizeAround(double rate, Parameters center){
		ParametersSpace resised = ParametersSpace.resizeAround(this.gameSpace, rate, center);
		return new ParametersFactory(resised);
	}

	public static void main(String[] args) {
		CompleteAnnotatedSearchSpace cass = CompleteAnnotatedSearchSpace.load("assets/RinascimentoParamsTight.json");
		Parameters p = Parameters.load("assets/defaultx2/");
		ParametersFactory gpf = new ParametersFactory(cass);
		StringBuilder builder = new StringBuilder();

		builder.append("Center: ").append(Arrays.toString(Parameters.vectorise(p))).append("\n");
		builder.append("Original: ").append(gpf).append("\n");

		for(double r=0.1; r<=1.0; r+=0.1){
			ParametersFactory newGpf = gpf.resizeAround(r,p);
			builder.append("Resizing factor: ").append(r).append("\n").append(newGpf.toString()).append("\n");
		}

		CopyToClipboard.copy(builder.toString());
		System.out.println(builder);
	}

	@Override
	public String toString() {
		return ParametersSpace.toString(this.gameSpace);
	}
}
