package de.homebeaver.lei.marshaller;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import org.glassfish.jaxb.runtime.v2.runtime.unmarshaller.Loader;
import org.glassfish.jaxb.runtime.v2.runtime.unmarshaller.SAXConnector;
import org.glassfish.jaxb.runtime.v2.runtime.unmarshaller.StructureLoader;
import org.glassfish.jaxb.runtime.v2.runtime.unmarshaller.UnmarshallingContext;
import org.gleif.data.schema.rr._2016.ExtensionType;
import org.gleif.data.schema.rr._2016.NodeType;
import org.gleif.data.schema.rr._2016.RRHeaderType;
import org.gleif.data.schema.rr._2016.RegistrationStatusEnum;
import org.gleif.data.schema.rr._2016.RelationshipContainerType;
import org.gleif.data.schema.rr._2016.RelationshipRecordType;
import org.gleif.data.schema.rr._2016.RelationshipRecordsType;
import org.gleif.data.schema.rr._2016.RelationshipStatusEnum;
import org.gleif.data.schema.rr._2016.RelationshipTypeEnum;

import de.homebeaver.lei.AbstractCounter;
import de.homebeaver.lei.RegistrationStatusCounter;
import de.homebeaver.lei.RelationshipStatusCounter;
import de.homebeaver.lei.RelationshipTypeCounter;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.bind.Unmarshaller.Listener;
import jakarta.xml.bind.UnmarshallerHandler;

public class RRUnmarshallerListener extends Listener {

	private static final Logger LOG = Logger.getLogger(RRUnmarshallerListener.class.getName());
	private static final int MAX_RECS = 9;
	
	private int count; // RecordCount
	private BigInteger recordCount;
	private Unmarshaller unmarshaller;
	private AbstractCounter<RelationshipTypeEnum> relationshipTypeCounter;
	private AbstractCounter<RelationshipStatusEnum> relationshipStatusCounter;
	private AbstractCounter<RegistrationStatusEnum> registrationStatusCounter;
	private AbstractCounter<String> managingLOUCounter;
	
	RRUnmarshallerListener(Unmarshaller unmarshaller) {
		count = 0;
		relationshipTypeCounter = new RelationshipTypeCounter();
		relationshipStatusCounter = new RelationshipStatusCounter();
		registrationStatusCounter = new RegistrationStatusCounter();
		managingLOUCounter = new AbstractCounter<String>();
		this.unmarshaller = unmarshaller;
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
		} else if(o instanceof RRHeaderType) {
			RRHeaderType target = (RRHeaderType)o;
			recordCount = target.getRecordCount();
			ExtensionType extension = target.getExtension();
			LOG.info("LEIHeader.RecordCount:"+recordCount + ", extension:"+extension);
			if(extension!=null) {
				extension.getAny().forEach(any -> {
					LOG.info("any:"+any);
				});
			}
		} else if(o instanceof RelationshipRecordType) { // RelationshipRecord
			count++;
			RelationshipRecordType target = (RelationshipRecordType)o;
			RelationshipRecordsType rrRecords = (RelationshipRecordsType)parent;
			// "Relationship", required = true
			RelationshipContainerType relationship = target.getRelationship();
			// "StartNode", required = true
			// "EndNode", required = true
			NodeType start = relationship.getStartNode();
			NodeType end = relationship.getEndNode();
			RelationshipTypeEnum relationshipType = relationship.getRelationshipType();
			relationshipTypeCounter.count(relationshipType);
			relationshipStatusCounter.count(relationship.getRelationshipStatus());
			registrationStatusCounter.count(target.getRegistration().getRegistrationStatus());
			managingLOUCounter.count(target.getRegistration().getManagingLOU());
			// es gibt keine:
			//if(start.getNodeIDType()!=NodeIDTypeEnum.LEI || end.getNodeIDType()!=NodeIDTypeEnum.LEI) {
			if(relationshipType==RelationshipTypeEnum.IS_INTERNATIONAL_BRANCH_OF) {
				LOG.info("RRecord.Relationship:"
						+start.getNodeID()+"/"+start.getNodeIDType()
						+" >is an international branch of> " 
						+end.getNodeID()+"/"+end.getNodeIDType()
						+ ", rrRecords#:"+rrRecords.getRelationshipRecord().size()+"/"+count+" of "+recordCount);
			}
//			LOG.info("RRecord.Relationship:"
//				+start.getNodeID()+"/"+start.getNodeIDType()
//				+" >"+relationship.getRelationshipType()+"> " 
//				+end.getNodeID()+"/"+end.getNodeIDType()
//				+ ", rrRecords#:"+rrRecords.getRelationshipRecord().size()+"/"+count+" of "+recordCount);
/* Bsp:
213800B7574JY87RFX94/LEI >IS_DIRECTLY_CONSOLIDATED_BY> MP6I5ZYZBEU3UXPYFY54/LEI
213800Q3QHVGPX5TND72/LEI >IS_DIRECTLY_CONSOLIDATED_BY> MP6I5ZYZBEU3UXPYFY54/LEI, rrRecords#:7/18116 of 246452
        <lei:LegalName>HSBC CORPORATE TRUSTEE COMPANY (UK) LIMITED</lei:LegalName>		
        <lei:LegalName>HSBC INSURANCE SERVICES HOLDINGS LIMITED</lei:LegalName>
   >    <lei:LegalName>HSBC BANK PLC</lei:LegalName>
 */
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
			
			if(rrRecords.getRelationshipRecord().size()>=MAX_RECS) {
				// nur wenige recs in heap List<LEIRecordType> halten
				rrRecords.getRelationshipRecord().clear();
			}
		} else if(o instanceof RelationshipRecordsType) {
			LOG.info("\n count="+count +", "+ relationshipTypeCounter.toString());
			LOG.info("\n count="+count +", "+ relationshipStatusCounter.toString());
			LOG.info("\n count="+count +", "+ registrationStatusCounter.toString());
			LOG.info("\n count="+count +", "+ managingLOUCounter);
//count=246452, {IS_INTERNATIONAL_BRANCH_OF=862, IS_DIRECTLY_CONSOLIDATED_BY=117531, IS_ULTIMATELY_CONSOLIDATED_BY=128059}
//count=246452, {ACTIVE=215515, INACTIVE=30937}
//count=246452, {TRANSFERRED=0 // transferred to a different LOU
//			   , PENDING_ARCHIVAL=23
//			   , LAPSED=43923 // A relationship data report that has not been renewed by the NextRenewalDate
//			   , PUBLISHED=163680, RETIRED=21849 // The relationship is considered to have ended, ...
//			   , ANNULLED=16961 // A relationship data report that was marked as erroneous or invalid after it was published.
//			   , PENDING_VALIDATION=0, DUPLICATE=0, PENDING_TRANSFER=16}
// TODO weitere Zählkandidaten: ValidationDocumentsTypeEnum, ValidationSourcesTypeEnum
/*
 count=246452, {969500Q2MA9VBQ8BG884=18032
              , 506700LOLO7M6V0E4247=753
              , 815600EAD78C57FCE690=12420
              , 213800WAVVOPS85N2205=31204
              , 353800279ADEFGKNTV65=410
              , 558600FNC30A8J9EGQ54=230
              , 222100T6ICDIY8V4VX70=904
              , 9884008RRMX1X5HV6625=383
              , 635400DZBUIMTBCXGA12=693
              , 48510000JZ17NWGUA510=625
              , 253400M18U5TB02TW421=666
              , 254900PMALKJRL1YGQ18=20
              , 789000TVSB96MCOKSB52=2168
              , 259400L3KBYEVNHEJF55=2893
              , 815600F58F7382929F40=4
              , 485100001PLJJ09NZT59=30
              , 894500IIP432AHQ64V02=457
              , 39120001KULK7200U106=2581
              , 4469000001AVO26P9X86=688
              , 5299000J2N45DDNE4Y28=27774
              , 5493001KJTIIGC8Y1R12=18984
              , 529900F6BNUR3RJ2WH29=3857
              , 097900BEFH0000000217=286
              , 378900F4A0A690EA6735=454
              , 335800FVH4MOKZS9VH40=10783
              , 315700LK78Z7C0WMIL03=3870
              , 029200067A7K6CH0H586=22
              , 529900T8BM49AURSDO55=2410
              , 52990034RLKT0WSOAM90=1156
              , 7478000050A040C0D041=612
              , 743700OO8O2N3TQKJC81=2681
              , 529900MPT6BHOJRPB746=78
              , EVK05KS7XY1DEII3R011=79917
              , 300300EJ5S25BWX0QJ66=1795
              , 9504003253F6C21EE978=25
              , 724500A93Z8V1MJK5349=6587
              , 254900LXHEVKYGERER05=245
              , 959800R2X69K6Y6MX775=9755
              }
			
 */
		}
	}
}
