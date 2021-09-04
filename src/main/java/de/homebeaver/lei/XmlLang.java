package de.homebeaver.lei;

import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The language in which all of the string- valued components of this address are expressed.
 * An IETF Language Code conforming to the latest RFC from IETF BCP 47. 
 * Note that the first characters of an IETF Language Code, up to the hyphen (if any), 
 * are all lowercase, and those following the hyphen (if any) are all uppercase.
 */
// see https://www.w3.org/International/questions/qa-when-xmllang.de
// XmlLang extends String geht nicht, da final class String
public class XmlLang {

	private static final Logger LOG = Logger.getLogger(XmlLang.class.getName());

	private static final String regex = "^([a-z]{2})(-.*)?$";
	static Pattern p = Pattern.compile(regex);

	// see https://stackoverflow.com/questions/65620673/detect-non-latin-characters-with-regex-pattern-in-java
	static Pattern latinPattern = Pattern.compile("^[\\p{Print}\\p{IsLatin}]*$");

    static public boolean isLatin(String input) {
    	Matcher matcher = latinPattern.matcher(input);
    	return matcher.find();
    }

	String lang;
	
	public XmlLang(String s) {
		lang = mapTo(s);
	}
	
	static String mapTo(String s) {
		if(s==null) return s;
		
		Matcher m = p.matcher(s.equals("GB") ? "en" : s.toLowerCase());
		if(m.matches()) {
			return m.group(1);
		}
		LOG.warning("invalid language "+ s);
		return s;
	}

    public String toString() {
        return lang;
    }

}

