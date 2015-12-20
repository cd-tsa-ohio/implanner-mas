package edu.ohiou.mfgresearch.impmas.semantics;

import edu.ohiou.mfgresearch.implanner.MfgConcept;

public class FactFile extends MfgConcept{

	private static final long serialVersionUID = -7451020654719476564L;
	
	public static final String NAME = "FactFile";
	
	private String factFile  = null;
	
	public FactFile(){
		
	}
	
	public FactFile(String factFile) {
		super();
		this.factFile = factFile;
	}
	
	public String getFactFile() {
		return factFile;
	}

	public void setFactFile(String factFile) {
		this.factFile = factFile;
	}


}
