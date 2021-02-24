package utils;

import java.util.ArrayList;
import java.util.List;

public class History<T> {

	private int limit;
	private ArrayList<T> data = new ArrayList<>();

	public History(){
		limit=-1;
	}

	public List<T> asList(){
		return data;
	}

	public History(int limit){
		if(limit==0)
			limit = -1;
		this.limit = limit;
	}

	public int size(){
		return data.size();
	}

	public T get(int index){
		return data.get(index);
	}

	public void add(T e){
		if(data.size() == limit) {
			data.remove(0);
		}
		data.add(e);
	}

}
