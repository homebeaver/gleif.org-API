package de.homebeaver.lei;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * count distinct countries
 * 
 * US-AK, US-AL is mapped to US
 * PT-01, PT-03 is mapped to PT
 * XX is mapped to XX although XX is not valid country
 * XK is mapped to XK - a temporary country code for Kosovo
 * XX- is mapped to "invalid"
 * xx is mapped to "invalid"
 */
public class DistinctCountryCounter extends DistinctStringCounter {
	
	private static final String regex = "^([A-Z]{2})(-.*)?$";

	Pattern p;
	public DistinctCountryCounter() {
		super();
		p = Pattern.compile(regex);
	}
	
	public void count(String s) {
		super.count(mapTo(s));
	}
	
	private String mapTo(String s) {
		if(s==null) return s;
		
		Matcher m = p.matcher(s);
		if(m.matches()) {
			return m.group(1);
		}
		return "invalid";
	}
}
