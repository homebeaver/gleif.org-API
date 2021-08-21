package de.homebeaver.test.lei;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.logging.Logger;

import org.gleif.data.schema.leidata._2016.OtherEntityNamesType;

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

//	public static Consumer<? super OtherEntityNamesType> logConsumer(Optional<OtherEntityNamesType> oen) {
	public static Consumer<? super OtherEntityNamesType> logOtherEntityNamesConsumer() {
//		if(!oen.isPresent()) return null;
		return t -> {
			t.getOtherEntityName().forEach(oename -> {
				Logger.getAnonymousLogger().info(oename.getType()+" "+oename.getLang()+":"+oename.getValue());
			});
		};
	}

}
