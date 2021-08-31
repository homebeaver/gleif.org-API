package de.homebeaver.lei;

import org.gleif.data.schema.rr._2016.RelationshipTypeEnum;

public class RelationshipTypeCounter extends AbstractCounter<RelationshipTypeEnum> {

	public RelationshipTypeCounter() {
		super();
		m.put(RelationshipTypeEnum.IS_DIRECTLY_CONSOLIDATED_BY, 0);
		m.put(RelationshipTypeEnum.IS_INTERNATIONAL_BRANCH_OF, 0);
		m.put(RelationshipTypeEnum.IS_ULTIMATELY_CONSOLIDATED_BY, 0);
	}
	
}
