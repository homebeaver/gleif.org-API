package de.homebeaver.lei.marshaller;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import org.glassfish.jaxb.runtime.v2.runtime.unmarshaller.Loader;
import org.glassfish.jaxb.runtime.v2.runtime.unmarshaller.SAXConnector;
import org.glassfish.jaxb.runtime.v2.runtime.unmarshaller.StructureLoader;
import org.glassfish.jaxb.runtime.v2.runtime.unmarshaller.UnmarshallingContext;
import org.gleif.data.schema.leidata._2016.EntityType;
import org.gleif.data.schema.leidata._2016.ExtensionType;
import org.gleif.data.schema.leidata._2016.LEIData;
import org.gleif.data.schema.leidata._2016.LEIHeaderType;
import org.gleif.data.schema.leidata._2016.LEIRecordType;
import org.gleif.data.schema.leidata._2016.LEIRecordsType;
import org.gleif.data.schema.leidata._2016.OtherAddressType;
import org.gleif.data.schema.leidata._2016.OtherEntityNameType;
import org.gleif.data.schema.leidata._2016.TransliteratedOtherAddressType;
import org.gleif.data.schema.leidata._2016.TransliteratedOtherEntityNameType;

import de.homebeaver.lei.AbstractCounter;
import de.homebeaver.lei.DistinctCountryCounter;
import de.homebeaver.lei.DistinctLangCounter;
import de.homebeaver.lei.DistinctStringCounter;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.bind.Unmarshaller.Listener;
import jakarta.xml.bind.UnmarshallerHandler;

public class UnmarshallerListener extends Listener {

	private static final Logger LOG = Logger.getLogger(UnmarshallerListener.class.getName());
	private static final int MAX_RECS = 9;
	
	private int count; // RecordCount
	private BigInteger recordCount;
	private Unmarshaller unmarshaller;
	private AbstractCounter<String> langCounter;
	private AbstractCounter<String> legalJurisdictionCounter; // getLegalJurisdiction
	
	UnmarshallerListener(Unmarshaller unmarshaller) {
		count = 0;
		this.unmarshaller = unmarshaller;
		langCounter = new DistinctLangCounter();
		legalJurisdictionCounter = new DistinctCountryCounter();
		getSAXConnector();
	}
	
	SAXConnector getSAXConnector() {
		SAXConnector ret = null;
		UnmarshallerHandler uh = unmarshaller.getUnmarshallerHandler();
		LOG.finer("UnmarshallerHandler/SAXConnector:"+uh);
		if(uh instanceof SAXConnector) {
			ret = (SAXConnector)uh;
			// ? org.glassfish.jaxb.runtime.v2.runtime.unmarshaller.StructureLoader
			// > Loads children of an element. with public JaxBeanInfo getBeanInfo()
			Loader loader = ret.getContext().getCurrentState().getLoader();
			if(loader!=null) {
				//Collection<QName> ea = loader.getExpectedAttributes(); //NPE
				StructureLoader sl = (StructureLoader)loader;
//				LOG.info("SAXConnector.CurrentState.Loader.BeanInfo:"+sl.getBeanInfo());
			}
//			LOG.info("SAXConnector.AllDeclaredPrefixes#:"+ret.getContext().getAllDeclaredPrefixes().length);		
		} else {
			LOG.warning("UnmarshallerHandler not SAXConnector:"+uh);
		}
		return ret;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override // implement abstract method
	public void beforeUnmarshal(Object target, Object parent) {
		LOG.finer("target:"+target + (parent==null ? "parent is root" : ", parent:"+parent));
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override // implement abstract method
	// called in org.glassfish.jaxb.runtime.v2.runtime.unmarshaller.Loader#fireAfterUnmarshal
	public void afterUnmarshal(Object o, Object parent) {
		LOG.finer("o:"+o + (parent==null ? "parent is root" : ", parent:"+parent));
		if(o instanceof JAXBElement) {
			JAXBElement target = (JAXBElement)o;
//			Class<?> declaredType = target.getDeclaredType();
			// xml element tag name.
			LOG.info("target.QName:"+target.getName()+", target.Value:"+target.getValue() + ", parent:"+parent);
		} else if(o instanceof LEIHeaderType) {
			LEIHeaderType target = (LEIHeaderType)o;
			recordCount = target.getRecordCount();
			ExtensionType extension = target.getExtension();
			LOG.info("LEIHeader.RecordCount:"+recordCount + ", extension:"+extension);
			if(extension!=null) {
				extension.getAny().forEach(any -> {
					LOG.info("any:"+any);
				});
			}
		} else if(o instanceof LEIRecordType) {
			count++;
			LEIRecordType target = (LEIRecordType)o;
			LEIRecordsType leiRecords = (LEIRecordsType)parent;
			LOG.info("LEIRecord.LEI:"+target.getLEI()+ ", leiRecords#:"+leiRecords.getLEIRecord().size()+"/"+count+" of "+recordCount);
			
			SAXConnector connector = getSAXConnector();
			if(connector!=null) {
				UnmarshallingContext uctx = connector.getContext();
				String[] pfx = uctx.getNewlyDeclaredPrefixes();
				if(pfx.length>0) {
					List<String> all = Arrays.asList(uctx.getAllDeclaredPrefixes());
//					for(int i=0;i<pfx.length;i++) {					
//						LOG.info("UnmarshallingContext.NewlyDeclaredPrefixes."+i+":"+pfx[i]+"="+uctx.getNamespaceURI(pfx[i]));
//					}
					LOG.info("UnmarshallingContext.AllDeclaredPrefixes:"+all 
						+ ", "+pfx.length+" NewlyDeclared "+pfx[0]+"="+uctx.getNamespaceURI(pfx[0]));
				}
			}
			
			if(leiRecords.getLEIRecord().size()>=MAX_RECS) {
				// nur wenige recs in heap List<LEIRecordType> halten
				leiRecords.getLEIRecord().clear();
			}
			
		} else if(o instanceof EntityType) {
			EntityType target = (EntityType)o;
			
			String lj = target.getLegalJurisdiction(); // optional
			legalJurisdictionCounter.count(lj);
			
			langCounter.count(target.getLegalName().getLang());
			if(target.getOtherEntityNames()!=null) { // opt
				List<OtherEntityNameType> oenl = target.getOtherEntityNames().getOtherEntityName();
				oenl.forEach(oen -> {
					langCounter.count(oen.getLang());
				});
			}
			if(target.getTransliteratedOtherEntityNames()!=null) { // opt
				List<TransliteratedOtherEntityNameType> oenl = target.getTransliteratedOtherEntityNames().getTransliteratedOtherEntityName();
				oenl.forEach(oen -> {
					langCounter.count(oen.getLang());
				});
			}
			langCounter.count(target.getHeadquartersAddress().getLang());
			langCounter.count(target.getLegalAddress().getLang());
//			 {=72, de-DE=3, en=72, fr=28, es=3}
//			 {=72, de-DE=3, en=72, fr=52, es=3} mit OtherAddresses, TransliteratedOtherAddresses
			if(target.getOtherAddresses()!=null) { // opt
				List<OtherAddressType> oenl = target.getOtherAddresses().getOtherAddress();
				oenl.forEach(oen -> {
					langCounter.count(oen.getLang());
				});
			}
			if(target.getTransliteratedOtherAddresses()!=null) { // opt
				List<TransliteratedOtherAddressType> oenl = target.getTransliteratedOtherAddresses().getTransliteratedOtherAddress();
				oenl.forEach(oen -> {
					langCounter.count(oen.getLang());
				});
			}
			
		} else if(o instanceof LEIData) {
			LEIData target = (LEIData)o;
//			LOG.info("\n LEIData.LEIHeader:"+target.getLEIHeader());
			LOG.info("\n count="+count +", "+ legalJurisdictionCounter);
			LOG.info("\n "+ langCounter);
		}
	}
}
