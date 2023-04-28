package statistics.types;

import java.util.HashMap;

public class PayloadNumericalStatistic extends NumericalStatistic{

	HashMap<String,Object> payload = new HashMap<>();

	public PayloadNumericalStatistic(double value){
		super(value);
	}

	public void addPayload(String s, Object o){
		payload.put(s,o);
	}

}
