package de.homebeaver.lei;

import java.util.HashMap;
import java.util.Map;

public class AbstractCounter<T> {

	Map<T, Integer> m;
	
	public AbstractCounter() {
		m = new HashMap<T, Integer>();
	}
	
	public void count(T kenum) {
//		m.compute(kenum, (key, val) -> val+1);
		m.compute(kenum, (key, val) -> (val == null) ? 1 : val+1);
	}
	
	public String toString() {
		return m.toString();
	}

}
