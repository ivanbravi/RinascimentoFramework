package utils;


import java.util.List;
import java.util.Random;

public class WeightedRandomSelection extends RandomBased{

	@Override
	public WeightedRandomSelection clone(){
		return new WeightedRandomSelection();
	}

	public WeightedElement next(List list){
		if(list==null)
			return null;

		if(list.size()==0)
			return null;
		List<WeightedElement> l = list;
		WeightedElement prev, next;
		double total = l.stream().mapToDouble(b->b.weight()).sum();
		double threshold = rnd.nextDouble();
		prev = l.get(0);
		for(int i=1; i<list.size(); i++) {
			double currWeight = prev.weight()/total;
			next = l.get(i);
			if(currWeight>=threshold){
				break;
			}else{
				threshold -=currWeight;
				prev = next;
			}
		}

		return  prev;
	}

}
