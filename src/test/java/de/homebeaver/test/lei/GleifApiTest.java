package de.homebeaver.test.lei;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import javax.net.ssl.HttpsURLConnection;
import javax.xml.datatype.XMLGregorianCalendar;

import org.gleif.data.schema.leidata._2016.EntityType;
import org.gleif.data.schema.leidata._2016.FileContentEnum;
import org.gleif.data.schema.leidata._2016.LEIData;
import org.gleif.data.schema.leidata._2016.LEIRecordType;
import org.gleif.data.schema.leidata._2016.OtherEntityNamesType;
import org.gleif.data.schema.leidata._2016.TransliteratedOtherAddressesType;
import org.gleif.data.schema.leidata._2016.TransliteratedOtherEntityNamesType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import de.homebeaver.lei.LeiRecord;
import de.homebeaver.lei.marshaller.AbstactTransformer;
import de.homebeaver.lei.marshaller.LeiTransformer;

public class GleifApiTest {

	private static final String LOG_PROPERTIES = "testLogging.properties";
	private static LogManager logManager = LogManager.getLogManager(); // Singleton
	private static Logger LOG = null;
//	private static final Logger LOG = Logger.getLogger(GleifApiTest.class.getName());
	private static void initLogger() {
    	URL url = GleifApiTest.class.getClassLoader().getResource(LOG_PROPERTIES);
    	if(url==null) {
			LOG = Logger.getLogger(GleifApiTest.class.getName());
			LOG.warning("keine "+LOG_PROPERTIES);
    	} else {
    		try {
    	        File file = new File(url.toURI()); //NPE wenn "testLogging.properties" nicht gefunden
    			logManager.readConfiguration(new FileInputStream(file));
    		} catch (IOException | URISyntaxException e) {
    			LOG = Logger.getLogger(GleifApiTest.class.getName());
    			LOG.warning(e.getMessage());
    		}
    	}
		LOG = Logger.getLogger(GleifApiTest.class.getName());		
	}

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
    	initLogger();
	}
	
//	private static final String LEI_VALID = "529900W18LQJJN6SJ336";
//	private static final String LEI_VALID = "353800023456EJMNXY28"; // jp
	private static final String LEI_VALID = "097900BEF50000000472"; // sk+transliteratedOtherNames
	private static final String LEI_OLD = "M07J9MTYHFCSVRBV2631"; // returns 0 recs
	
	private static final String GLEIF_EXPORT_V1 = "https://api.gleif.org/export/v1";
	private String getLeiData(String id) {
		if (!LeiRecord.isValid(id))
			return null;
		// for valid lei get the LeiRecord:
		String xmlString = null;
		try {
	        String urlParameters = "?filter[lei]="+id;
			URL url = new URL(GLEIF_EXPORT_V1+"/lei-records.xml"+urlParameters);
			HttpsURLConnection con = (HttpsURLConnection) url.openConnection(); // throws IOException
	        //add request header
			String requestMethod = "GET";
	        con.setRequestMethod(requestMethod);
	        // Send request
	        con.setDoOutput(true); // true: use the URL connection for output,
	        int responseCode = con.getResponseCode();
	        if(responseCode!=200) {
	        	// returns 404 : Page Not Found or File Not Found
		        LOG.warning("Sending '"+requestMethod+"' request to URL "+url + " returns "+responseCode);	
		        return null;
	        }
	        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
	        String inputLine;
	        StringBuffer response = new StringBuffer();
	 
	        while ((inputLine = in.readLine()) != null) {
	            response.append(inputLine);
	        }
	        in.close();
	        xmlString = response.toString();
	        LOG.info("response:\n"+xmlString); 
/* liefert:
<?xml version="1.0"?><lei:LEIData xmlns:lei="http://www.gleif.org/data/schema/leidata/2016" xmlns:gleif="http://www.gleif.org/data/schema/golden-copy/extensions/1.0">  <lei:LEIHeader>    <lei:ContentDate>2021-07-22T08:00:00+00:00</lei:ContentDate>    <lei:FileContent>QUERY_RESPONSE</lei:FileContent>    <lei:RecordCount>1</lei:RecordCount>  </lei:LEIHeader>  <lei:LEIRecords>    <lei:LEIRecord>      <lei:LEI>529900W18LQJJN6SJ336</lei:LEI>      <lei:Entity>        <lei:LegalName xml:lang="de-DE">Soci&#xE9;t&#xE9; G&#xE9;n&#xE9;rale Effekten GmbH</lei:LegalName>        <lei:LegalAddress xml:lang="de-DE">          <lei:FirstAddressLine>Neue Mainzer Stra&#xDF;e 46-50</lei:FirstAddressLine>          <lei:City>Frankfurt am Main</lei:City>          <lei:Region>DE-HE</lei:Region>          <lei:Country>DE</lei:Country>          <lei:PostalCode>60311</lei:PostalCode>        </lei:LegalAddress>        <lei:HeadquartersAddress xml:lang="de-DE">          <lei:FirstAddressLine>Neue Mainzer Stra&#xDF;e 46-50</lei:FirstAddressLine>          <lei:City>Frankfurt am Main</lei:City>          <lei:Region>DE-HE</lei:Region>          <lei:Country>DE</lei:Country>          <lei:PostalCode>60311</lei:PostalCode>        </lei:HeadquartersAddress>        <lei:RegistrationAuthority>          <lei:RegistrationAuthorityID>RA000242</lei:RegistrationAuthorityID>          <lei:RegistrationAuthorityEntityID>HRB 32283</lei:RegistrationAuthorityEntityID>        </lei:RegistrationAuthority>        <lei:LegalJurisdiction>DE</lei:LegalJurisdiction>        <lei:LegalForm>          <lei:EntityLegalFormCode>2HBR</lei:EntityLegalFormCode>        </lei:LegalForm>        <lei:EntityStatus>ACTIVE</lei:EntityStatus>      </lei:Entity>      <lei:Registration>        <lei:InitialRegistrationDate>2014-01-27T07:37:54+00:00</lei:InitialRegistrationDate>        <lei:LastUpdateDate>2021-07-19T05:07:28+00:00</lei:LastUpdateDate>        <lei:RegistrationStatus>ISSUED</lei:RegistrationStatus>        <lei:NextRenewalDate>2021-10-17T22:00:00+00:00</lei:NextRenewalDate>        <lei:ManagingLOU>5299000J2N45DDNE4Y28</lei:ManagingLOU>        <lei:ValidationSources>FULLY_CORROBORATED</lei:ValidationSources>        <lei:ValidationAuthority>          <lei:ValidationAuthorityID>RA000242</lei:ValidationAuthorityID>          <lei:ValidationAuthorityEntityID>HRB 32283</lei:ValidationAuthorityEntityID>        </lei:ValidationAuthority>      </lei:Registration>      <lei:Extension/>    </lei:LEIRecord>  </lei:LEIRecords></lei:LEIData>
 */
		} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		}
        return xmlString;
	}
	
	private InputStream getLeiXmlStream(String id) {
		if (!LeiRecord.isValid(id))
			return null;
		// for valid lei get the LeiRecord:
		try {
	        String urlParameters = "?filter[lei]="+id;
			URL url = new URL(GLEIF_EXPORT_V1+"/lei-records.xml"+urlParameters);
			HttpsURLConnection con = (HttpsURLConnection)url.openConnection(); // throws IOException
	        //add request header
			String requestMethod = con.getRequestMethod();
	        con.setRequestMethod(requestMethod);
	        // Send request
	        con.setDoOutput(true); // true: use the URL connection for output,
	        int responseCode = con.getResponseCode();
	        if(responseCode!=200) {
	        	// returns 404 : Page Not Found or File Not Found
		        LOG.warning("Sending '"+requestMethod+"' request to URL "+url + " returns "+responseCode);	
		        return null;
	        }
	        return con.getInputStream();
		} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		}
        return null;
	}

	Object object;
	
	@Test
	public void queryOldLei() {
		LOG.info(GLEIF_EXPORT_V1+ " Query LEI:"+LEI_OLD);
		String xml = getLeiData(LEI_OLD);

		AbstactTransformer transformer = LeiTransformer.getInstance();
		LOG.info("transformer:"+transformer);
		object = transformer.unmarshal(new ByteArrayInputStream(xml.getBytes()));
		LEIData lEIData = (LEIData)object;
	    XMLGregorianCalendar xgc = lEIData.getLEIHeader().getContentDate();
	    FileContentEnum queryRestponse = lEIData.getLEIHeader().getFileContent();
	    LOG.info("ContentDate:"+xgc + " " + queryRestponse);
	    assertEquals(0, lEIData.getLEIHeader().getRecordCount().intValue());
	    assertEquals(FileContentEnum.QUERY_RESPONSE, queryRestponse);
	}
	
	/*
	 * Query
	 */
	@Test
	public void queryValidLei() {
		LOG.info(GLEIF_EXPORT_V1+ " Query LEI:"+LEI_VALID);
		//String xml = getLeiData(LEI_VALID);
		AbstactTransformer transformer = LeiTransformer.getInstance();
		LOG.info("transformer:"+transformer);
		object = transformer.unmarshal(getLeiXmlStream(LEI_VALID));
		LEIData lEIData = (LEIData)object;
	    XMLGregorianCalendar xgc = lEIData.getLEIHeader().getContentDate();
	    FileContentEnum queryRestponse = lEIData.getLEIHeader().getFileContent();
	    LOG.info("ContentDate:"+xgc + " " + queryRestponse);
	    assertEquals(1, lEIData.getLEIHeader().getRecordCount().intValue());
	    assertEquals(FileContentEnum.QUERY_RESPONSE, queryRestponse);
	    
	    List<LEIRecordType> list = lEIData.getLEIRecords().getLEIRecord();
	    assertEquals(1, list.size());
	    list.forEach(leiRecord -> { 
	    	String lei = leiRecord.getLEI(); // "LEI", required = true
	    	EntityType entity = leiRecord.getEntity(); // "Entity", required = true
			Optional<OtherEntityNamesType> oen = Optional.ofNullable(entity.getOtherEntityNames());
			Optional<TransliteratedOtherEntityNamesType> toen = Optional.ofNullable(entity.getTransliteratedOtherEntityNames());
			Optional<TransliteratedOtherAddressesType> toa = Optional.ofNullable(entity.getTransliteratedOtherAddresses());
			LOG.info("LEIRecord.LEI=" + lei
			+ "\n .Entity.LegalName:" + entity.getLegalName().getLang()+":"+entity.getLegalName().getValue() 
			+ "\n .Entity.OtherEntityNames=" + (oen.isPresent() ? oen.get().getOtherEntityName().size() : "null")
			+ "\n .Entity.TransliteratedOtherAddress=" + (toa.isPresent() ? toa.get().getTransliteratedOtherAddress().size() : "null")
			+ "\n .Entity.EntityStatus=" + entity.getEntityStatus() 
			+ "\n .Registration.InitialRegistrationDate="
				+ leiRecord.getRegistration().getInitialRegistrationDate());
			oen.ifPresent(Consumers.logOtherEntityNamesConsumer());
			toen.ifPresent(Consumers.logTransliteratedOtherEntityNamesConsumer());
			toa.ifPresent(Consumers.logTransliteratedOtherAddressesConsumer());
		});
	}

}
