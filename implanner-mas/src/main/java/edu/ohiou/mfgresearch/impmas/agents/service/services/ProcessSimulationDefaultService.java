package edu.ohiou.mfgresearch.impmas.agents.service.services;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import edu.ohiou.mfgresearch.implanner.MfgConcept;
import edu.ohiou.mfgresearch.implanner.network.MachineSequenceObject;
import edu.ohiou.mfgresearch.impmas.semantics.SequenceFile;
import edu.ohiou.mfgresearch.impmas.semantics.SimulationFile;


public class ProcessSimulationDefaultService extends AbstractFreelancerService{

	public ProcessSimulationDefaultService() {
		super(null);
		setServiceDescription("process Simulation Default Service");
	}

	@Override
	public Set<MfgConcept> performService(MfgConcept input) {
		// TODO Auto-generated method stub
		
		Set<MfgConcept> output = new HashSet<MfgConcept>();
		SimulationFile sf = (SimulationFile) input;
		
		Map<String, LinkedList<MachineSequenceObject>> partMap = new HashMap<String, LinkedList<MachineSequenceObject>>();
		for(int i =0; i< sf.getPartNames().length; i++){
			partMap.put(sf.getPartNames()[i], ((SequenceFile)sf.getPartSequences().get(i)).getProcessList());
		}
		
		//ImplannerSimulator simulator = new ImplannerSimulator(partMap);
		//simulator.display("My Simulation");
		
		return null;
	}

	/**
	 * @param args
	 */
	
}
