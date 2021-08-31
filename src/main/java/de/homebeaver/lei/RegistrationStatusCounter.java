package de.homebeaver.lei;

import org.gleif.data.schema.rr._2016.RegistrationStatusEnum;

public class RegistrationStatusCounter extends AbstractCounter<RegistrationStatusEnum> {

	public RegistrationStatusCounter() {
		super();
		m.put(RegistrationStatusEnum.ANNULLED, 0);
		m.put(RegistrationStatusEnum.DUPLICATE, 0);
		m.put(RegistrationStatusEnum.LAPSED, 0);
		m.put(RegistrationStatusEnum.PENDING_ARCHIVAL, 0);
		m.put(RegistrationStatusEnum.PENDING_TRANSFER, 0);
		m.put(RegistrationStatusEnum.PENDING_VALIDATION, 0);
		m.put(RegistrationStatusEnum.PUBLISHED, 0);
		m.put(RegistrationStatusEnum.RETIRED, 0);
		m.put(RegistrationStatusEnum.TRANSFERRED, 0);
	}
}
