package edu.ohiou.mfgresearch.impmas.semantics;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import edu.ohiou.mfgresearch.implanner.MfgConcept;

public class SimulationFile extends MfgConcept{
	
	

	LinkedList<SequenceFile> partSequences;
	String[] partNames;
	
	public SimulationFile(){
		
	}
	
	public SimulationFile(String[] partNames, LinkedList<SequenceFile> partSequences){
		this.partNames = partNames;
		this.partSequences = partSequences;
	}
	

	public LinkedList<SequenceFile> getPartSequences() {
		return partSequences;
	}



	public void setPartSequences(
			LinkedList<SequenceFile> partSequences) {
		this.partSequences = partSequences;
	}



	public String[] getPartNames() {
		return partNames;
	}

	public void setPartNames(String[] partNames) {
		this.partNames = partNames;
	}
	/*Map <String, LinkedList<MachineSequenceObject>> partSequenceMap = new HashMap<String,LinkedList<MachineSequenceObject> >();
	
	
	
	public SimulationFile(Map <String, LinkedList<MachineSequenceObject>> partSequenceMap){
		this.partSequenceMap = partSequenceMap;
	}

	public Map<String, LinkedList<MachineSequenceObject>> getPartSequenceMap() {
		return partSequenceMap;
	}

	public void setPartSequenceMap(LinkedList<String> partNames,
			LinkedList<LinkedList<MachineSequenceObject>> partSequenceMap) {
		for(int i=0; i< partNames.size(); i++){
			this.partSequenceMap.put(partNames.get(i), partSequenceMap.get(i));
		}
		
	}
	*/
	

}
