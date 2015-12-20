package edu.ohiou.mfgresearch.impmas.mfgOntology;

import edu.ohiou.mfgresearch.impmas.semantics.BidValue;
import edu.ohiou.mfgresearch.impmas.semantics.ManagementData;
import edu.ohiou.mfgresearch.impmas.semantics.Vendor;
import jade.content.AgentAction;
import jade.content.Concept;
import jade.content.Predicate;
import jade.core.AID;

/**
 * Predicate "Vendor" AID "proposes quote value" bid
 * @author as888211
 *
 */
public class Send_Quotes implements AgentAction {
	
	private static final long serialVersionUID = -6635329373100554216L;
	private ManagementData managementData;
	private Vendor vendor; //contains the vendor AID. Should be AID of the vendor lead agent
	private BidValue bid;
	
	public Send_Quotes(){
		
	}
	
	public Send_Quotes(Vendor vendorAID, BidValue bid, ManagementData managementData) {
		super();
		this.vendor = vendorAID;
		this.bid = bid;
		this.managementData = managementData;
	}

	public Vendor getVendorAID() {
		return vendor;
	}

	public void setVendorAID(Vendor vendor) {
		this.vendor = vendor;
	}

	public BidValue getBid() {
		return bid;
	}

	public void setBid(BidValue bid) {
		this.bid = bid;
	}

	public ManagementData getManagementData() {
		return managementData;
	}

	public void setManagementData(ManagementData managementData) {
		this.managementData = managementData;
	}  
	
}
