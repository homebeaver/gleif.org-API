package de.homebeaver.test.lei;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.logging.Logger;

import org.gleif.data.schema.leidata._2016.EntityType;
import org.gleif.data.schema.leidata._2016.LEIRecordType;
import org.gleif.data.schema.leidata._2016.OtherEntityNameType;
import org.gleif.data.schema.leidata._2016.OtherEntityNamesType;
import org.gleif.data.schema.leidata._2016.TransliteratedOtherAddressesType;
import org.gleif.data.schema.leidata._2016.TransliteratedOtherEntityNamesType;

import de.homebeaver.lei.XmlLang;

/*
 * Quelle: https://openbook.rheinwerk-verlag.de/javainsel/11_007.html#u11.7.1
 */
public class Consumers {

/*
// Folgender Aufruf zeigt die Nutzung:
Consumer<Void> wrap = measuringConsumer( Void -> System.out.println( "Test" ) );
wrap.accept( null );

 */
	public static <T> Consumer<T> measuringConsumer(Consumer<T> block) {
		return t -> {
			long start = System.nanoTime();
			block.accept(t);
			long duration = System.nanoTime() - start;
			Logger.getAnonymousLogger().info("Ausführungszeit (ns): " + duration);
		};
	}

	public static Consumer<? super OtherEntityNamesType> logOtherEntityNamesConsumer() {
		return t -> {
			t.getOtherEntityName().forEach(oename -> {
				Logger.getAnonymousLogger().info(oename.getType()+" "+oename.getLang()+":"+oename.getValue());
			});
		};
	}

	public static Consumer<? super TransliteratedOtherEntityNamesType> logTransliteratedOtherEntityNamesConsumer() {
		return t -> {
			t.getTransliteratedOtherEntityName().forEach(oename -> {
				Logger.getAnonymousLogger().info(oename.getType()+" "+oename.getLang()+":"+oename.getValue());
			});
		};
	}

	public static Consumer<? super TransliteratedOtherAddressesType> logTransliteratedOtherAddressesConsumer() {
		return t -> {
			t.getTransliteratedOtherAddress().forEach(adr -> {
				Logger.getAnonymousLogger().info(adr.getType()+" "+adr.getLang()+":"+adr.getCountry());
			});
		};
	}

	public static Consumer<? super LEIRecordType> logLEIRecordConsumer() {
		Logger LOG = Logger.getAnonymousLogger();
		return t -> {
	    	String lei = t.getLEI(); // "LEI", required = true
	    	EntityType entity = t.getEntity(); // "Entity", required = true
	    	String eName = entity.getLegalName().getValue();
	    	XmlLang nameLang = new XmlLang(entity.getLegalName().getLang());
			Optional<OtherEntityNamesType> oen = Optional.ofNullable(entity.getOtherEntityNames());
	    	if(XmlLang.isLatin(eName)) {
	    		// name is printalbe
	    	} else {
	    		if(oen.isPresent()) {
	    			OtherEntityNameType oName = oen.get().getOtherEntityName().get(0);
	    			if(XmlLang.isLatin(oName.getValue())) {
	    				eName = oName.getValue();
	    				nameLang = new XmlLang(oName.getLang());
	    			}
	    		}
	    	}
			LOG.info("LEIRecord.LEI=" + lei
			+ "\n .Entity.LegalName:" + nameLang+":"+eName 
			+ (oen.isPresent() ? "\n .Entity.OtherEntityNames=" + oen.get().getOtherEntityName().size() : "")
			+ "\n .Entity.EntityStatus=" + entity.getEntityStatus() 
			+ "\n .Registration.InitialRegistrationDate="
				+ t.getRegistration().getInitialRegistrationDate());
			oen.ifPresent(Consumers.logOtherEntityNamesConsumer());
		};
	}

}
