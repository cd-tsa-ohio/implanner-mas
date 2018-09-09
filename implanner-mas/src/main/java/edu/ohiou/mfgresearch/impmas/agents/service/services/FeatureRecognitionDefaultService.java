package edu.ohiou.mfgresearch.impmas.agents.service.services;

import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Done;
import jade.lang.acl.ACLMessage;
import jade.util.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Set;

import edu.ohiou.mfgresearch.implanner.MfgConcept;
import edu.ohiou.mfgresearch.implanner.parts.MfgPartModel;
import edu.ohiou.mfgresearch.impmas.semantics.PartFile;
import edu.ohiou.mfgresearch.labimp.draw.DrawWFApplet;

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
		
		if (pf.getName().endsWith("prt")) {
			MfgPartModel mfgPartModel = new MfgPartModel().openUGFile(pf.getTempFile());
			DrawWFApplet dfa = new DrawWFApplet(mfgPartModel.getPartModel());
			mfgPartModel.getPartModel().display("Part Model in FR Agent");
		output.add(mfgPartModel);
		}
		else {
			try {
				output.add(new MfgPartModel().parseFeatures(new FileReader(pf.getTempFile())));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		System.out.println("Shuvo: in service Feature Recognition performed");
		return output;
	}

	
}
