package edu.ohiou.mfgresearch.impmas.semantics;

import jade.content.Concept;

public class BidValue implements Concept {

	private static final long serialVersionUID = -5104581286407950460L;

	public static final String name = "BidValue"; 
	private Double price = null;

	public BidValue(){
		
	}
	public BidValue(Double price) {
		super();
		this.price = price;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

}
