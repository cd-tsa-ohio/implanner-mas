package edu.ohiou.mfgresearch.impmas.semantics;

import jade.content.Concept;
import jade.content.onto.annotations.Slot;
import jade.core.AID;

public class Vendor implements Concept {
	
	private static final long serialVersionUID = 6092193998081603868L;

	public static final String NAME = "Vendor";
	
	private AID vendorAID = null;
	
	public Vendor(){
		
	}
	
	public Vendor(AID aID) {
		super();
		vendorAID = aID;
	}

	public AID getAID() {
		return vendorAID;
	}

	public void setAID(AID aID) {
		vendorAID = aID;
	}
			
	
}
