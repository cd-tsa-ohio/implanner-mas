package edu.ohiou.mfgresearch.impmas.agents.service;

import jade.lang.acl.ACLMessage;

import java.util.Set;

public abstract class AbstractService {

	private String serviceDescription = null;
	protected ServiceAgent serviceAgent = null;

	public AbstractService(String serviceDescription) {
		super();
		this.serviceDescription = serviceDescription;
	}

	public String getServiceDescription() {
		return serviceDescription;
	}

	public void setServiceDescription(String serviceDescription) {
		this.serviceDescription = serviceDescription;
	}
	
	public void setServiceAgent(ServiceAgent serviceAgent) {
		this.serviceAgent = serviceAgent;
	}

	public abstract Set<ACLMessage> performService(ACLMessage message);
}
