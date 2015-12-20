package edu.ohiou.mfgresearch.impmas.semantics;

import java.util.LinkedList;
import java.util.List;

import edu.ohiou.mfgresearch.implanner.MfgConcept;
import edu.ohiou.mfgresearch.implanner.network.MachineSequenceObject;


public class SequenceFile extends MfgConcept{
	
	LinkedList<MachineSequenceObject> processList = new LinkedList<MachineSequenceObject>();
	
	public SequenceFile(){
		
	}

	public SequenceFile(LinkedList<MachineSequenceObject> processList) {
		super();
		this.processList = processList;
	}

	public LinkedList<MachineSequenceObject> getProcessList() {
		return processList;
	}

	public void setProcessList(LinkedList<MachineSequenceObject> processList) {
		this.processList = processList;
	}

}
