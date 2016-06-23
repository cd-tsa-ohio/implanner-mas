package edu.ohiou.mfgresearch.impmas.agents;

import java.util.Optional;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.LogRecord;
import java.util.stream.Stream;

import edu.ohiou.mfgresearch.impmas.mfgOntology.IMPlannerOntology;
import jade.content.ContentManager;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.BeanOntologyException;
import jade.content.onto.Ontology;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.util.Logger;

public abstract class LambdaAgent extends Agent {
	
	//Jade logger for logging messages
	public Logger logger = Logger.getJADELogger(this.getClass().getName());
	FileHandler fh;
	//Content manager for extracting Ontology
	public ContentManager manager = (ContentManager) getContentManager();
	// This agent "speaks" the SL language
	protected Codec codec = new SLCodec();
	
	@Override
	protected void setup() {
		
		//set SL codec
		manager.registerLanguage(codec);
		//get the service description of this agent
		Stream<ServiceDescription> sd = getServices();
		//register the ontologies supplied with service description
		DFAgentDescription dfDesc = new DFAgentDescription();
		createAgentDescription().map(d->{
			getServices().map(s->{s.getAllOntologies().forEachRemaining(o -> manager.registerOntology((Ontology) o));
				                  return s;
			           }).forEach(s->d.addServices(s));
			                      return d;
		               }).map(d->{
		            	   try {
							DFService.register(this, d);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						   return d;
		               });		
		
	}
	
	/**
	 * Returns the service description of this agent provided by this agent
	 * @return
	 */
	public abstract Stream<ServiceDescription> getServices();
	
	/**
	 * utility function which returns an optional new instance of DFAgentDescription
	 * @return
	 */
	protected Optional<DFAgentDescription> createAgentDescription(){
		return Optional.of(new DFAgentDescription());
	}
	

}
