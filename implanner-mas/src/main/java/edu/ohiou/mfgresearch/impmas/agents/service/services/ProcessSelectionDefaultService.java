package edu.ohiou.mfgresearch.impmas.agents.service.services;

import jade.lang.acl.ACLMessage;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import edu.ohiou.mfgresearch.implanner.MfgConcept;
import edu.ohiou.mfgresearch.implanner.processes.MfgProcess;
import edu.ohiou.mfgresearch.impmas.semantics.FactFile;
import edu.ohiou.mfgresearch.impmas.semantics.ProcessFile;
import edu.ohiou.mfgresearch.labimp.basis.ViewObject;
import jess.Defglobal;
import jess.Fact;
import jess.JessException;
import jess.RU;
import jess.Rete;
import jess.Value;
import jess.WatchConstants;
import jess.awt.TextAreaWriter;

public class ProcessSelectionDefaultService extends AbstractFreelancerService{
	
	private Rete engine;
	private String ruleFolder = null;
	private String dataFile = null;

	public ProcessSelectionDefaultService() {
	
		// TODO Auto-generated constructor stub
		super(null);
		setServiceDescription("Process Selection Member Service");
		ruleFolder = ViewObject.getProperties().getProperty("JESS_RULE_FOLDER", "");
		dataFile = ViewObject.getProperties().getProperty("JESS_DATA_FILE", "");
	}

	/**
	 * @param args
	 */
	
	

	@Override
	public Set<MfgConcept> performService(MfgConcept input) {
		// TODO Auto-generated method stub
		Set<MfgConcept> output = new HashSet<MfgConcept>();
		FactFile ff = (FactFile) input;
		
			try {
				MfgProcess.mfgProcesses = new LinkedList<MfgProcess>();
				System.out.println(">> Making new engine...");
				engine = new Rete();
		
				engine.addDefglobal(new Defglobal("*ruleFolder*", new Value(ruleFolder,RU.STRING)));
				engine.executeCommand("(batch \""+ruleFolder+"rbpp.clp\")");
				engine.executeCommand("(batch \""+ruleFolder + dataFile + "\")");
				engine.executeCommand("(assert (feature (name f1) )) (assert (feature (name f2))) (facts)");
				System.out.println(">> Creating facts...");
				engine.executeCommand(ff.getFactFile());
				System.out.println(">> Engine made.");
				
				System.out.println(">> Running engine...");
				engine.watch(WatchConstants.RULES);
				engine.run();

				// Print facts
				System.out.println(">> Printing facts...");
				int i=1;
				for (Iterator itr = engine.listFacts(); itr.hasNext();) {
					Fact f = (Fact) itr.next();
					System.out.println("f-"+i+" "+f);
					i++;
				}
				
				List<MfgProcess> mfgProcesses = MfgProcess.mfgProcesses;
				System.out.println("process selection done");
				ProcessFile processFile = new ProcessFile(mfgProcesses);
				output.add(processFile);
				
			
				
			} catch (JessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//			engine.watch(WatchConstants.RULES);
			
		
		
		return output;
	}


}
