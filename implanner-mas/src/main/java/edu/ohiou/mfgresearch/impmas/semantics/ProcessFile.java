package edu.ohiou.mfgresearch.impmas.semantics;

import java.util.LinkedList;
import java.util.List;

import edu.ohiou.mfgresearch.implanner.MfgConcept;
import edu.ohiou.mfgresearch.implanner.processes.MfgProcess;

public class ProcessFile extends MfgConcept{
	
	List<MfgProcess> processList = new LinkedList<MfgProcess>();

	public ProcessFile(){
		
	}
	
	public ProcessFile(List<MfgProcess> list){
		super();
		this.processList = list;
	}
	
	public List<MfgProcess> getProcessList() {
		return processList;
	}

	public void setProcessList(List<MfgProcess> processList) {
		this.processList = processList;
	}

}
