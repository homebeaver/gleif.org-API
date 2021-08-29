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
import java.util.Optional;
import java.util.function.Consumer;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import javax.xml.datatype.XMLGregorianCalendar;

import org.gleif.data.schema.leidata._2016.EntityType;
import org.gleif.data.schema.leidata._2016.LEIData;
import org.gleif.data.schema.leidata._2016.LEIRecordType;
import org.gleif.data.schema.leidata._2016.OtherEntityNamesType;
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
	private static final String EXISTING_FILE1 = "lei_record.xml";
	private static final String EXISTING_FILE = "lei_data.xml";
	private static final String GLEIF_FULL_FILE = "C:\\proj\\LEI/20210625-gleif-concatenated-file-lei2.xml";

	@Test
	public void notExistingFileTest() {
		LOG.info(NOT_EXISTING_FILE);
		File file = new File(TESTDIR+NOT_EXISTING_FILE);
		assertFalse(file.canRead());

		Consumer<Void> wrap = Consumers.measuringConsumer( Void -> {
			AbstactTransformer transformer = LeiTransformer.getInstance();
			LOG.info("transformer:"+transformer);
		});
		wrap.accept( null );
	}
	
	Object object;
	@Test
	void test1() {
		LOG.info("EXISTING_FILE with 1 record");
		File file = new File(TESTDIR+EXISTING_FILE1);
		assertTrue(file.canRead());

		AbstactTransformer transformer = LeiTransformer.getInstance();
		LOG.info("transformer:"+transformer);
		try {
			InputStream is = new FileInputStream(file);
			Consumer<Void> wrap = Consumers.measuringConsumer( Void -> {
				object = transformer.unmarshal(is);
				LOG.info("object:"+object);
				assertNotNull(object);
			});
			wrap.accept( null );
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("FileNotFoundException");
		}
		LEIData lEIData = (LEIData)object;
	    assertEquals(1, lEIData.getLEIHeader().getRecordCount().intValue());
	    // "ContentDate", required = true, expected: 2021-07-22T08:00:00+00:00
	    XMLGregorianCalendar xgc = lEIData.getLEIHeader().getContentDate();
	    LOG.info("ContentDate:"+xgc);
	    assertEquals(2021, xgc.getYear());
	    assertEquals(7, xgc.getMonth());
	    assertEquals(22, xgc.getDay());
	    assertEquals(8, xgc.getHour());
	    
	    List<LEIRecordType> list = lEIData.getLEIRecords().getLEIRecord();
//	    assertEquals(1, list.size());
	    list.forEach(leiRecord -> { 
	    	String lei = leiRecord.getLEI(); // "LEI", required = true
	    	EntityType entity = leiRecord.getEntity(); // "Entity", required = true
			Optional<OtherEntityNamesType> oen = Optional.ofNullable(entity.getOtherEntityNames());
			LOG.info("LEIRecord.LEI=" + lei
			+ "\n .Entity.LegalName:" + entity.getLegalName().getLang()+":"+entity.getLegalName().getValue() 
			+ "\n .Entity.OtherEntityNames=" + (oen.isPresent() ? oen.get().getOtherEntityName().size() : "null")
			+ "\n .Entity.EntityStatus=" + entity.getEntityStatus() 
			+ "\n .Registration.InitialRegistrationDate="
				+ leiRecord.getRegistration().getInitialRegistrationDate());
			oen.ifPresent(Consumers.logOtherEntityNamesConsumer());
//		    assertEquals("529900W18LQJJN6SJ336", lei);
	    });
	}

	@Test
	void test2() {
		LOG.info(EXISTING_FILE);
		File file = new File(TESTDIR+EXISTING_FILE);
		assertTrue(file.canRead());

		AbstactTransformer transformer = LeiTransformer.getInstance();
		LOG.info("transformer:"+transformer);
		try {
			InputStream is = new FileInputStream(file);
			Consumer<Void> wrap = Consumers.measuringConsumer( Void -> {
				object = transformer.unmarshal(is);
				LOG.info("object:"+object);
				assertNotNull(object);
			});
			wrap.accept( null );
//			object = transformer.unmarshal(is);
//			LOG.info("object:"+object);
//			assertNotNull(object);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("FileNotFoundException");
		}
		LEIData lEIData = (LEIData)object;
	    assertEquals(2, lEIData.getLEIHeader().getRecordCount().intValue());
	    // "ContentDate", required = true, expected: 2021-07-23T16:00:00+00:00
	    XMLGregorianCalendar xgc = lEIData.getLEIHeader().getContentDate();
	    LOG.info("ContentDate:"+xgc); // 2021-07-23T16:00:00Z
//	    LOG.info("ContentDate:"+xgc.getYear()+"-"+xgc.getMonth()+"-"+xgc.getDay()+"T"+xgc.getHour());
	    assertEquals(2021, xgc.getYear());
	    assertEquals(7, xgc.getMonth());
	    assertEquals(23, xgc.getDay());
	    assertEquals(16, xgc.getHour());
	    
	    List<LEIRecordType> list = lEIData.getLEIRecords().getLEIRecord();
	    assertEquals(2, list.size());
//	    list.forEach(// Consumer<? super LEIRecordType> action)
	    list.forEach(leiRecord -> { 
	    	String lei = leiRecord.getLEI(); // "LEI", required = true
	    	EntityType entity = leiRecord.getEntity(); // "Entity", required = true
			Optional<OtherEntityNamesType> oen = Optional.ofNullable(entity.getOtherEntityNames());
//		    LOG.info("LegalName:"+entity.getLegalName().getLang()+":"+entity.getLegalName().getValue());
			LOG.info("LEIRecord.LEI=" + lei
			+ "\n .Entity.LegalName:" + entity.getLegalName().getLang()+":"+entity.getLegalName().getValue() 
			+ "\n .Entity.OtherEntityNames=" + (oen.isPresent() ? oen.get().getOtherEntityName().size() : "null")
			+ "\n .Entity.EntityStatus=" + entity.getEntityStatus() 
			+ "\n .Registration.InitialRegistrationDate="
				+ leiRecord.getRegistration().getInitialRegistrationDate());
			oen.ifPresent(Consumers.logOtherEntityNamesConsumer());
	    });
	}

//	@Test
	void test3() {
		LOG.info(GLEIF_FULL_FILE);
		File file = new File(GLEIF_FULL_FILE);
		assertTrue(file.canRead());

		AbstactTransformer transformer = LeiTransformer.getInstance();
		LOG.info("transformer:"+transformer);
		try {
			InputStream is = new FileInputStream(file);
			object = transformer.unmarshal(is);
			// glassfish v2 TransformerFactory Speicher + Laufzeit!!!!!!
			LOG.info("object:"+object);
			assertNotNull(object);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("FileNotFoundException");
		}
		LEIData lEIData = (LEIData)object;
	    List<LEIRecordType> list = lEIData.getLEIRecords().getLEIRecord();
	    list.forEach(leiRecord -> { 
	    	String lei = leiRecord.getLEI(); // "LEI", required = true
	    	EntityType entity = leiRecord.getEntity(); // "Entity", required = true
			Optional<OtherEntityNamesType> oen = Optional.ofNullable(entity.getOtherEntityNames());
			LOG.info("LEIRecord.LEI=" + lei
			+ "\n .Entity.LegalName:" + entity.getLegalName().getLang()+":"+entity.getLegalName().getValue() 
			+ "\n .Entity.OtherEntityNames=" + (oen.isPresent() ? oen.get().getOtherEntityName().size() : "null")
			+ "\n .Entity.EntityStatus=" + entity.getEntityStatus() 
			+ "\n .Registration.InitialRegistrationDate="
				+ leiRecord.getRegistration().getInitialRegistrationDate());
			oen.ifPresent(Consumers.logOtherEntityNamesConsumer());
	    });
	}

}
