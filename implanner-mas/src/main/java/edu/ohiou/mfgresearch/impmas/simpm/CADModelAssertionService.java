package edu.ohiou.mfgresearch.impmas.simpm;

import java.util.Set;

import edu.ohiou.mfgresearch.implanner.MfgConcept;
import edu.ohiou.mfgresearch.impmas.agents.service.services.AbstractFreelancerService;

public class CADModelAssertionService extends AbstractFreelancerService  {

	public CADModelAssertionService(String serviceDescription) {
		super(serviceDescription);
		setServiceDescription("CADModel Assersion Service");
	}

	@Override
	public Set<MfgConcept> performService(MfgConcept input) {
		return null;
		
		
		
	}

}
