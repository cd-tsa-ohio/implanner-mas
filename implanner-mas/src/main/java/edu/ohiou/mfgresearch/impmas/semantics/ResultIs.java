package edu.ohiou.mfgresearch.impmas.semantics;
import edu.ohiou.mfgresearch.implanner.MfgConcept;
import jade.content.Predicate;

public class ResultIs implements Predicate {

	private static final long serialVersionUID = -751791989125543510L;

	public static final String NAME = "SingleMfgConcept";
	
	private ManagementData managementData;
	private MfgConcept concept;
	
	public ResultIs() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public ManagementData getManagementData() {
		return managementData;
	}
	
	public void setManagementData(ManagementData managementData) {
		this.managementData = managementData;
	}
	
	public MfgConcept getConcept() {
		return concept;
	}
	
	public void setConcept(MfgConcept concept) {
		this.concept = concept;
	}
	
	
	
}
