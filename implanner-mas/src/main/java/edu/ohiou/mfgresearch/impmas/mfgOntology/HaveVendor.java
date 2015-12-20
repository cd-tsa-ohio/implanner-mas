package edu.ohiou.mfgresearch.impmas.mfgOntology;

import java.io.File;
import java.io.FileInputStream;
import java.util.Date;
import java.util.Set;
import java.util.Vector;

import edu.ohiou.mfgresearch.implanner.MfgConcept;
import edu.ohiou.mfgresearch.impmas.semantics.BidValue;
import edu.ohiou.mfgresearch.impmas.semantics.ManagementData;
import edu.ohiou.mfgresearch.impmas.semantics.Vendor;
import jade.content.Concept;
import jade.content.Predicate;

/**
 * Predicate Implanner needs feature recognizer management data of the design
 * opening bid price and other constraint on vendors
 * This concept is used in the AC from IMPlanner coalition to dealers
 * @author as888211
 *
 */
public class HaveVendor implements Predicate {

	private static final long serialVersionUID = 711507794790098600L;

	public static final String NAME = "HaveVendor";
	
	private ManagementData managementData;
	private BidValue openingBid;
	private Long timeValid;
	private MfgConcept input;
	private MfgConcept result;

	public HaveVendor() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ManagementData getManagementData() {
		return managementData;
	}

	public void setManagementData(ManagementData managementData) {
		this.managementData = managementData;
	}
	
	public BidValue getOpeningBid() {
		return openingBid;
	}

	public void setOpeningBid(BidValue openingBid) {
		this.openingBid = openingBid;
	}

	public Long getTimeValid() {
		return timeValid;
	}

	public void setTimeValid(Long timeValid) {
		this.timeValid = timeValid;
	}

	public MfgConcept getInput() {
		return input;
	}

	public void setInput(MfgConcept input) {
		this.input = input;
	}

	public MfgConcept getResult() {
		return result;
	}

	public void setResult(MfgConcept result) {
		this.result = result;
	}

	
}
