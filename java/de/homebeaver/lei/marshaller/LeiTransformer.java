package de.homebeaver.lei.marshaller;

import java.io.InputStream;

import javax.inject.Named;

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;

@Named
/* Notice 
 * that there are two @Singleton annotations, 
 * one in javax.inject and the other in the javax.ebj package. 
 * I'm referring to them by their fully-qualified names to avoid confusion.
 * @see https://stackoverflow.com/questions/26832051/singleton-vs-applicationscope
 * @see https://github.com/javax-inject/javax-inject
 */
@javax.inject.Singleton
public class LeiTransformer extends AbstactTransformer {

	public static AbstactTransformer SINGLETON = new LeiTransformer();

	public static AbstactTransformer getInstance() {
		return SINGLETON;
	}
	
	/* xsd file name in the output folder started with "/" == project_loc
	 * 
	 * CDF   : LEI-Common Data File
	 */
	private static final String CDF_2_1_XSD = "/xsd/2017-03-21_lei-cdf-v2-1.xsd";
	// CONTENT_PATH aka package name
	private static final String CONTENT_PATH = "org.gleif.data.schema.leidata._2016"; 
	// CONTENT_SUPERTYPE_NAME aka class name
	private static final String CONTENT_SUPERTYPE_NAME = CONTENT_PATH+".LEIData"; 
	// CONTENT_TYPE_NAME aka class name
	public static final String CONTENT_TYPE_NAME = "de.homebeaver.lei.LeiData"; 
	
	private LeiTransformer() {
		super(CONTENT_PATH, SINGLETON);
	}
	
	@Override
	protected String getResource() {
		return CDF_2_1_XSD;
	}

	@Override
	protected Class<?> loadClass() {
		Class<?> type = null;
		try {
			// dynamisch die Super Klasse laden 
			type = Class.forName(CONTENT_SUPERTYPE_NAME);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return type;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Object> T unmarshal(InputStream xmlInputStream) {
		Class<?> type = loadClass();
		Object result = this.unmarshal(xmlInputStream, type);
		return (T) result;
	}
	
	@Override
	public NamespacePrefixMapper createNamespacePrefixMapper() {
//		return CioNamespacePrefixMapper.getNamespacePrefixMapper();
		return null;
	}


}
