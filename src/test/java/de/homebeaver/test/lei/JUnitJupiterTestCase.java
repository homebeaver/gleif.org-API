package de.homebeaver.test.lei;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.gleif.data.schema.leidata._2016.LEIData;
import org.gleif.data.schema.leidata._2016.LEIRecordType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;

class JUnitJupiterTestCase {

	private static final String LOG_PROPERTIES = "testLogging.properties";
	private static LogManager logManager = LogManager.getLogManager(); // Singleton
	private static Logger LOG = null;
	private static void initLogger() {
    	URL url = JUnitJupiterTestCase.class.getClassLoader().getResource(LOG_PROPERTIES);
    	if(url==null) {
			LOG = Logger.getLogger(JUnitJupiterTestCase.class.getName());
			LOG.warning("keine "+LOG_PROPERTIES);
    	} else {
    		try {
    	        File file = new File(url.toURI()); //NPE wenn "testLogging.properties" nicht gefunden
    			logManager.readConfiguration(new FileInputStream(file));
    		} catch (IOException | URISyntaxException e) {
    			LOG = Logger.getLogger(TransformerTest.class.getName());
    			LOG.warning(e.getMessage());
    		}
    	}
		LOG = Logger.getLogger(TransformerTest.class.getName());		
	}

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
    	initLogger();
	}

//	@Test
	void test() {
		LOG.info(LOG_PROPERTIES);
		fail("Not yet implemented");
	}

	// Unmarshalling, see https://jakarta.ee/specifications/platform/9/apidocs/jakarta/xml/bind/jaxbcontext
/*
      JAXBContext jc = JAXBContext.newInstance( "com.acme.foo:com.acme.bar" );
      Unmarshaller u = jc.createUnmarshaller();
      FooObject fooObj = (FooObject)u.unmarshal( new File( "foo.xml" ) ); // ok
      BarObject barObj = (BarObject)u.unmarshal( new File( "bar.xml" ) ); // ok
      BazObject bazObj = (BazObject)u.unmarshal( new File( "baz.xml" ) ); // error, "com.acme.baz" not in contextPath
 */
	private static final String TESTDIR = "src/test/resources/"; // mit Daten aus xrechnung-1.2.0-testsuite-2018-12-14.zip\instances\
	private static final String NOT_EXISTING_FILE = "NOT_EXISTING_FILE.xml";
	private static final String EXISTING_FILE = "lei_data.xml";

	// CONTENT_PATH aka package name
	private static final String CONTENT_PATH = "org.gleif.data.schema.leidata._2016"; 
	// CONTENT_SUPERTYPE_NAME aka class name
	private static final String CONTENT_SUPERTYPE_NAME = CONTENT_PATH+".LEIData"; 
	// CONTENT_TYPE_NAME aka class name
	public static final String CONTENT_TYPE_NAME = "de.homebeaver.lei.LeiData"; 

	@Test
	void unmarshall() {
		LOG.info("start");
	    try {
//			System.setProperty(JAXBContext.JAXB_CONTEXT_FACTORY, "jakarta.xml.bind.JAXBContext");
// non-standard property: System.setProperty("jakarta.xml.bind.context.factory", JAXBContext.JAXB_CONTEXT_FACTORY);
			JAXBContext jc = JAXBContext.newInstance( CONTENT_PATH );
		    Unmarshaller u = jc.createUnmarshaller();
		    assertNotNull(u);
		    
			File file = new File(TESTDIR+EXISTING_FILE);
		    Object o = u.unmarshal(file);
		    LOG.info(""+o);
		    
		    JAXBElement je = (JAXBElement)o;
		    Object oLEIData = je.getValue();
		    LOG.info(""+oLEIData);
		    
		    LEIData lEIData = (LEIData)oLEIData;
		    assertEquals(2, lEIData.getLEIHeader().getRecordCount().intValue());
		    List<LEIRecordType> list = lEIData.getLEIRecords().getLEIRecord();
		    // TODO list abarbeiten
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
