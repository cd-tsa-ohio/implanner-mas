package edu.ohiou.mfgresearch.impmas.mfgOntology;

import jade.content.AgentAction;
import jade.core.AID;

public class RegisterIMPlanner implements AgentAction {

	private static final long serialVersionUID = 110377058784620878L;
	
	private Long instanceID = null;
	private AID impAgent = null;
	private String message = null;

	public RegisterIMPlanner() {
		// TODO Auto-generated constructor stub
	}

	public RegisterIMPlanner(Long instanceID, AID impAgent, String message) {
		super();
		this.instanceID = instanceID;
		this.impAgent = impAgent;
		this.message = message;
	}

	public Long getInstanceID() {
		return instanceID;
	}

	public void setInstanceID(Long instanceID) {
		this.instanceID = instanceID;
	}

	public AID getImpAgent() {
		return impAgent;
	}

	public void setImpAgent(AID impAgent) {
		this.impAgent = impAgent;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	
}
