package gamesearch.players.extractor;

import statistics.GameStats;

import java.util.OptionalDouble;
import java.util.stream.IntStream;

public class WrapperExtractors {

	WrapperExtractor[] extractors;

	WrapperExtractors(String[] metrics){
		extractors = new WrapperExtractor[metrics.length];
		for(int i=0; i<metrics.length; i++){
			extractors[i] = new WrapperExtractor(metrics[i]);
		}
	}

	public OptionalDouble[] extract(GameStats s){
		return IntStream.
				range(0,extractors.length).
				mapToObj(index -> extractors[index].extract(s)).
				toArray(OptionalDouble[]::new);
	}

}
