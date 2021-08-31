package de.homebeaver.lei;

import org.gleif.data.schema.rr._2016.RelationshipStatusEnum;

public class RelationshipStatusCounter extends AbstractCounter<RelationshipStatusEnum> {

	public RelationshipStatusCounter() {
		super();
		m.put(RelationshipStatusEnum.ACTIVE, 0);
		m.put(RelationshipStatusEnum.INACTIVE, 0);
	}
	
}
