package de.homebeaver.lei;

public class DistinctStringCounter extends AbstractCounter<String> {

	public DistinctStringCounter() {
		super();
	}
	
	public void count(String s) {
		super.count(s == null ? "" : s);
	}
	
}
