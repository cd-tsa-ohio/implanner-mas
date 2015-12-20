package edu.ohiou.mfgresearch.impmas.semantics;

import java.util.LinkedList;
import java.util.Set;

import edu.ohiou.mfgresearch.implanner.MfgConcept;
import jade.content.AgentAction;

public class ServiceAction implements AgentAction {

	private static final long serialVersionUID = -7388486334536267307L;

	public static final String NAME = "Service";
	
	private ManagementData managementData = null;
	private MfgConcept inputObject = null;
	private MfgConcept outputObject = null;
	
	public MfgConcept getInputObject() {
		return inputObject;
	}
	public void setInputObject(MfgConcept inputObject) {
		this.inputObject = inputObject;
	}
	public MfgConcept getOutputObject() {
		return outputObject;
	}
	public void setOutputObject(MfgConcept outputObject) {
		this.outputObject = outputObject;
	}
	public ManagementData getManagementData() {
		return managementData;
	}
	public void setManagementData(ManagementData managementData) {
		this.managementData = managementData;
	}
}
