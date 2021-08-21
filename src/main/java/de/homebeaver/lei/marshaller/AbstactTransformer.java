package de.homebeaver.lei.marshaller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.logging.Logger;

import javax.inject.Named;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBContextFactory;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

/**
 * The transformer has two main features: 
 * <p>
 * - <a href="https://en.wikipedia.org/wiki/Marshalling_(computer_science)">marshal</a> Java objects into XML 
 * <br>
 * - and the inverse, unmarshal XML into Java objects
 *
 * @see https://en.wikipedia.org/wiki/Jakarta_XML_Binding
 * @see https://de.wikipedia.org/wiki/Jakarta_XML_Binding
 */
@Named
/* Notice 
 * that there are two @Singleton annotations, 
 * one in javax.inject and the other in the javax.ebj package. 
 * I'm referring to them by their fully-qualified names to avoid confusion.
 * @see https://stackoverflow.com/questions/26832051/singleton-vs-applicationscope
 * @see https://github.com/javax-inject/javax-inject
 */
@javax.inject.Singleton
public abstract class AbstactTransformer implements NamespacePrefixMapperFactory {

	private static final Logger LOG = Logger.getLogger(AbstactTransformer.class.getName());
	
	final JAXBContext jaxbContext;
	
	// this is a SINGLETON! Use getInstance() in subclasses
	@SuppressWarnings("unused")
	private AbstactTransformer() {
		this.jaxbContext = null;
	}
	
	// ctor
/*
 Unmarshalling from a File:

           JAXBContext jc = JAXBContext.newInstance( "com.acme.foo" );
           Unmarshaller u = jc.createUnmarshaller();
           Object o = u.unmarshal( new File( "nosferatu.xml" ) );
        

Unmarshalling from an InputStream:

           InputStream is = new FileInputStream( "nosferatu.xml" );
           JAXBContext jc = JAXBContext.newInstance( "com.acme.foo" );
           Unmarshaller u = jc.createUnmarshaller();
           Object o = u.unmarshal( is );
        


 */
	protected AbstactTransformer(String contentPath, AbstactTransformer instance) {
		LOG.info("contentPath:"+contentPath);
		if(instance==null) try {
////			System.setProperty(JAXBContext.JAXB_CONTEXT_FACTORY, "jakarta.xml.bind.JAXBContextFactory");
////			"jakarta.xml.bind.context.factory"
////			java.util.ServiceLoader.load(JAXBContextFactory.class);
//			Class<?> type = loadClass();
//			this.jaxbContext = JAXBContext.newInstance(type);
			this.jaxbContext = JAXBContext.newInstance(contentPath);
			LOG.finer("jaxbContext:\n"+jaxbContext.toString()); // displays path and Classes known to context
			instance = this;
		} catch (JAXBException ex) {
			LOG.warning(ex.getMessage());
			throw new TransformationException(TransformationException.JAXB_INSTANTIATE_ERROR, ex);
		} else {
			this.jaxbContext = instance.jaxbContext;
		}
	}

	public boolean isValid(File xmlfile) {
		String resource = getResource();
//		try {
//			Source xmlFile = new StreamSource(xmlfile);
//			Validator validator = this.getSchemaValidator(); // throws SAXException, Exception
//			validator.validate(xmlFile);
//			LOG.config("validate against "+resource+" passed.");
//		} catch (SAXException ex) {
//			LOG.warning("validate against "+resource+" failed, SAXException: "+ex.getMessage());
//			return false;
//		} catch (Exception ex) {
//			LOG.severe("validate "+ex.getMessage());
//		}
		return true;
	}
	
//	public Validator getSchemaValidator() throws SAXException {
//		return getSchemaValidator(getResource());
//	}
	
	public abstract <T> T unmarshal(InputStream xmlInputStream);
	
	protected <T extends Object> T unmarshal(InputStream xmlInputStream, Class<T> declaredType) {
		try {
			Unmarshaller unmarshaller = createUnmarshaller();
			LOG.info("try unmarshal to "+declaredType.getName());
			// StreamSource implements Source
			javax.xml.transform.Source source = new javax.xml.transform.stream.StreamSource(xmlInputStream);
//			javax.xml.transform.Source source
			return unmarshaller.unmarshal(source, declaredType).getValue();
		} catch (JAXBException ex) {
			throw new TransformationException(TransformationException.MARSHALLING_ERROR, ex);
		}
	}

	protected abstract Class<?> loadClass();
	
	public byte[] marshal(Object document) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(16000);
		Class<?> type = loadClass();
		
		try {
			Marshaller marshaller = createMarshaller();
			marshaller.marshal(type.cast(document), outputStream);
			// REMARK objects that doesn't have @XmlRootElement on it:
			// - UBL InvoiceType and CreditNoteType
			// - CrossIndustryInvoiceType
			// see 5.3.3. Marshalling a non-element in file:///C:/proj/jaxb-ri/docs/ch03.html#marshalling
		} catch (JAXBException ex) {
			throw new TransformationException(TransformationException.MARSHALLING_ERROR, ex);
		}
		
		return outputStream.toByteArray();
	}

	protected abstract String getResource();
	
//	Validator getSchemaValidator(String resource) throws SAXException {
//		LOG.fine("resource:"+resource + " Class:"+this.getClass());
//		URL schemaURL = this.getClass().getResource(resource);
//		SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
//		LOG.finer("schemaURL:"+schemaURL);
//		Schema schema = sf.newSchema(schemaURL);
//		return schema.newValidator();
//	}

	private Unmarshaller createUnmarshaller() throws JAXBException {
		return jaxbContext.createUnmarshaller();
	}

	/*
	 * some REMARKS on Properties:
	 * 
	 * https://stackoverflow.com/questions/277996/remove-standalone-yes-from-generated-xml
		marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE); 
		marshaller.setProperty("com.sun.xml.internal.bind.xmlDeclaration", Boolean.FALSE);
		marshaller.setProperty("com.sun.xml.internal.bind.xmlHeaders", "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
		
	 * https://stackoverflow.com/questions/2161350/jaxb-xjc-code-generation-schemalocation-missing-in-xml-generated-by-marshall
		marshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, "urn:oasis:names:specification:ubl:schema:xsd:Invoice-2 http://docs.oasis-open.org/ubl/os-UBL-2.1/xsd/maindoc/UBL-Invoice-2.1.xsd");
		
	 *
	 */
	protected Marshaller createMarshaller() throws JAXBException {
		Marshaller marshaller = jaxbContext.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, formatXmlOutput());
		
//		registerNamespacePrefixMapper(marshaller);

		return marshaller;
	}

	protected Boolean formatXmlOutput() {
		return Boolean.TRUE;
	}

}
