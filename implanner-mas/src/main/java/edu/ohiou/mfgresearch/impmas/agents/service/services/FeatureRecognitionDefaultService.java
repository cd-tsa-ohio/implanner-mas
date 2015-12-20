package edu.ohiou.mfgresearch.impmas.agents.service.services;

import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Done;
import jade.lang.acl.ACLMessage;
import jade.util.Logger;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import edu.ohiou.mfgresearch.implanner.MfgConcept;
import edu.ohiou.mfgresearch.implanner.parts.MfgPartModel;
import edu.ohiou.mfgresearch.impmas.semantics.PartFile;

public class FeatureRecognitionDefaultService extends AbstractFreelancerService {

	public FeatureRecognitionDefaultService() {
		// TODO Auto-generated constructor stub
		super(null);
		setServiceDescription("Feature Recognition Default Service");
	}
	
	@Override
	public Set<MfgConcept> performService(MfgConcept input) {
		// TODO Auto-generated method stub

		Set<MfgConcept> output = new HashSet<MfgConcept>();
//		HashSet<MfgConcept> inputSet = (HashSet<MfgConcept>) input;
		
		// get content from PartFile
		PartFile pf = (PartFile) input;
		//String inString = pf.getPartFile();
		System.out.println("DNS> string in performService ") ;
		File file = new File("c:/a.txt") ; // (File) input;
		output.add(new MfgPartModel().openUGFile(pf.getTempFile()));
		
		System.out.println("Shuvo: in service Feature Recognition performed");
		return output;
	}

	
}
