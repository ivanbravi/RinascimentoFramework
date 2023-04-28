package utils.filters2D;

import com.google.gson.JsonObject;

public class FilterFactory {

	public static Filter2D create(JsonObject parameters){
		Filter2D r = null;
		String type = parameters.get("type").getAsString();

		if(type.equals("mean")){
//		EXAMPLE
//		{
//	    	"type": "mean",
//	    	"size": 1
//	    }
			int size = parameters.get("size").getAsInt();
			r = new Mean(size);
		}else if(type.equals("median")){
//		EXAMPLE
//		{
//	    	"type": "median",
//	    	"size": 1
//	    }
			int size = parameters.get("size").getAsInt();
			r = new Median(size);
		}else if(type.equals("normaliser")){
//		EXAMPLE
//		{
//	    	"type": "normaliser"
//	    }
			r = new Normaliser();
		}else if(type.equals("weightedmean")){
//		EXAMPLE
//		{
//	    	"type": "weightedmean",
//	    	"size": 1
//	    }
			int size = parameters.get("size").getAsInt();
			r = new WeightedMean(size);
		}else if(type.equals("filler")) {
//		EXAMPLE
//		{
//	    	"type": "filler",
//	    	"filter": {
//	    		"type": "mean",
//	    		"size": 1
//			},
//	    	"decay": 0.8
//	    }
			KernelBasedFilter2D filter = (KernelBasedFilter2D) create((JsonObject) parameters.get("filter"));
			double decay = parameters.get("decay").getAsDouble();
			r = new InfFiller(filter,decay);
		}

		return r;
	}

}
