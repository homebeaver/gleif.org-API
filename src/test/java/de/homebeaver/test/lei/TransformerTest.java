package de.homebeaver.test.lei;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.gleif.data.schema.leidata._2016.LEIData;
import org.gleif.data.schema.leidata._2016.LEIRecordType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import de.homebeaver.lei.marshaller.AbstactTransformer;
import de.homebeaver.lei.marshaller.LeiTransformer;

public class TransformerTest {

	private static final String LOG_PROPERTIES = "testLogging.properties";
	private static LogManager logManager = LogManager.getLogManager(); // Singleton
	private static Logger LOG = null;
//	private static final Logger LOG = Logger.getLogger(TransformerTest.class.getName());
	private static void initLogger() {
    	URL url = TransformerTest.class.getClassLoader().getResource(LOG_PROPERTIES);
    	if(url==null) {
			LOG = Logger.getLogger(TransformerTest.class.getName());
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

	private static final String TESTDIR = "src/test/resources/"; // mit Daten aus xrechnung-1.2.0-testsuite-2018-12-14.zip\instances\
	private static final String NOT_EXISTING_FILE = "NOT_EXISTING_FILE.xml";
	private static final String EXISTING_FILE = "lei_data.xml";

	@Test
	public void notExistingFileTest() {
		LOG.info(NOT_EXISTING_FILE);
		File file = new File(TESTDIR+NOT_EXISTING_FILE);
		assertFalse(file.canRead());

		AbstactTransformer transformer = LeiTransformer.getInstance();
		LOG.info("transformer:"+transformer);
	}
	
	Object object;
	@Test
	void test() {
		LOG.info(EXISTING_FILE);
		File file = new File(TESTDIR+EXISTING_FILE);
		assertTrue(file.canRead());

		AbstactTransformer transformer = LeiTransformer.getInstance();
		LOG.info("transformer:"+transformer);
		try {
			InputStream is = new FileInputStream(file);
			object = transformer.unmarshal(is);
			LOG.info("object:"+object);
			assertNotNull(object);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("FileNotFoundException");
		}
		LEIData lEIData = (LEIData)object;
	    assertEquals(2, lEIData.getLEIHeader().getRecordCount().intValue());
	    List<LEIRecordType> list = lEIData.getLEIRecords().getLEIRecord();
	    assertEquals(2, list.size());
	    // TODO list abarbeiten
	}

}
