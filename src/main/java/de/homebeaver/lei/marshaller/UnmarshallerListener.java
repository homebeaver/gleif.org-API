package de.homebeaver.lei.marshaller;

import java.math.BigInteger;
import java.util.List;
import java.util.logging.Logger;

import org.gleif.data.schema.leidata._2016.LEIData;
import org.gleif.data.schema.leidata._2016.LEIHeaderType;
import org.gleif.data.schema.leidata._2016.LEIRecordType;
import org.gleif.data.schema.leidata._2016.LEIRecordsType;

import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.Unmarshaller.Listener;

public class UnmarshallerListener extends Listener {

	private static final Logger LOG = Logger.getLogger(UnmarshallerListener.class.getName());
	private static final int MAX_RECS = 9;
	
	private int count; // RecordCount
	private BigInteger recordCount;
	UnmarshallerListener() {
		count = 0;
	}
	@Override // abstract
	public void beforeUnmarshal(Object o, Object parent) {
		LOG.finer("o:"+o + (parent==null ? "parent is root" : ", parent:"+parent));
		
	}
	
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
			LOG.info("LEIHeader.RecordCount:"+recordCount);
		} else if(o instanceof LEIRecordType) {
			count++;
			LEIRecordType target = (LEIRecordType)o;
			LEIRecordsType leiRecords = (LEIRecordsType)parent;
			LOG.info("LEIRecord.LEI:"+target.getLEI()+ ", leiRecords#:"+leiRecords.getLEIRecord().size()+"/"+count+" of "+recordCount);
			if(leiRecords.getLEIRecord().size()>=MAX_RECS) {
				// nur wenige recs in heap List<LEIRecordType> halten
				leiRecords.getLEIRecord().clear();
			}
		} else if(o instanceof LEIData) {
			LEIData target = (LEIData)o;
			LOG.info("\n LEIData.LEIHeader:"+target.getLEIHeader());
		}
	}
}
