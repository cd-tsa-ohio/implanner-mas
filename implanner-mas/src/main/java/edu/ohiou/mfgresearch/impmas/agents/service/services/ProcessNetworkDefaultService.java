package edu.ohiou.mfgresearch.impmas.agents.service.services;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import edu.ohiou.mfgresearch.implanner.MfgConcept;
import edu.ohiou.mfgresearch.implanner.activity.PartActivity;
import edu.ohiou.mfgresearch.implanner.features.MfgFeature;
import edu.ohiou.mfgresearch.implanner.network.MachineSequenceObject;
import edu.ohiou.mfgresearch.implanner.network.Network;
import edu.ohiou.mfgresearch.implanner.network.TestExamples;
import edu.ohiou.mfgresearch.implanner.parts.MfgPartModel;
import edu.ohiou.mfgresearch.implanner.processes.MfgProcess;
import edu.ohiou.mfgresearch.implanner.processes.Tool;
import edu.ohiou.mfgresearch.implanner.resources.Machine;
import edu.ohiou.mfgresearch.impmas.semantics.SequenceFile;
import nxopen.TaggedObjectCollection.Iterator;


public class ProcessNetworkDefaultService  extends AbstractFreelancerService{
	
	MfgPartModel partModel;
	private LinkedList<MachineSequenceObject> machineSequence;


	public ProcessNetworkDefaultService() {
		super(null);
		setServiceDescription("Process Network Default Service");
	}

	@Override
	public Set<MfgConcept> performService(MfgConcept input) {
		// TODO Auto-generated method stub
		Set<MfgConcept> output = new HashSet<MfgConcept>();
		partModel = (MfgPartModel) input;
		System.out.println("Recieved MFGPart model for performig process planning");
		
		List <MfgFeature> featureList = partModel.getFeatureList();
		
		for(ListIterator<MfgFeature> itr_feat = featureList.listIterator(); itr_feat.hasNext();){
			
			List<MfgProcess> processList = itr_feat.next().getFeatureProcesses();
			for(ListIterator<MfgProcess> itr_proc = processList.listIterator(); itr_proc.hasNext();){
				
				MfgProcess process = itr_proc.next();
				process.setTool((Tool)Tool.toolMap.get(process.getToolName()));
				process.setMachine((Machine)Machine.machineMap.get(process.getMachineName()));
			}
			
		}
		
		 TestExamples example = new TestExamples();
		 PartActivity pAct = new PartActivity();
		 pAct.setActivityForPart(example);
		 example.addAltPlan(pAct);
		 this.populatePPNData(example);
		 example.net = new Network(example, example.useGhosal);
		 example.net.display();
		 this.machineSequence = example.net.getMachineSequence();
		 SequenceFile sequenceFile = new SequenceFile(this.machineSequence);
		 output.add(sequenceFile);
		 
		return output;
	}

	/**
	 * @param args
	 */
	private void populatePPNData( TestExamples testExample){
testExample.setFeatureList(this.partModel.getFeatureList());
		
		LinkedList <MfgFeature> featureList = testExample.getFeatureList();
		LinkedList <Machine> machineList = new LinkedList<Machine>();
		double cost =2.0;
		for(int i=0; i< featureList.size(); i++){
			MfgFeature currentFeature = featureList.get(i);
			LinkedList <MfgProcess> processList = currentFeature.getProcesses();
			for (int j=0;j< processList.size(); j++){
				MfgProcess currentProcess = ((MfgProcess)processList.get(j));
				Machine currentMachine = currentProcess.getMachine();
				if(!machineList.contains(currentMachine)){
					machineList.add(currentMachine); 
				}
				currentMachine.addMachiningCost(currentFeature, currentProcess.getProcessTime());
				//cost = cost+.75;
				
			}
			//cost = cost -1.0 ;
		}
		testExample.useGhosal =true;
		
		System.out.println("done PPN");
		
	}
	
}
