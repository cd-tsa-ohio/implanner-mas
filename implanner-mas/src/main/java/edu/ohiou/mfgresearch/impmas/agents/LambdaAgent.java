package edu.ohiou.mfgresearch.impmas.agents;

import java.util.Properties;
import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.stream.Stream;

import edu.ohiou.mfgresearch.implanner.MfgConcept;
import edu.ohiou.mfgresearch.impmas.agents.IMPlannerProperties.AgentType;
import edu.ohiou.mfgresearch.impmas.semantics.ServiceAction;
import edu.ohiou.mfgresearch.lambda.Uni;
import edu.ohiou.mfgresearch.lambda.functions.Cons;
import edu.ohiou.mfgresearch.lambda.functions.Func;
import edu.ohiou.mfgresearch.lambda.functions.Pred;
import edu.ohiou.mfgresearch.lambda.functions.Suppl;
import jade.content.ContentManager;
import jade.content.abs.AbsContentElement;
import jade.content.abs.AbsIRE;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.basic.Action;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SimpleBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.Property;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.util.Logger;

public abstract class LambdaAgent extends Agent {
	
	//Jade logger for logging messages
	public Logger logger = Logger.getJADELogger(this.getClass().getName());
	
	//agent property supplied by Property file
	protected Properties property;
	
	//Function to get property values 
	Function<String, Function<Cons<String>, Func<Properties, Uni<Properties>>>> getProperty =
			x -> c -> y -> {
				Uni.of(y.getProperty(x))
				  .filter(s1->s1.contains(","))
				  .fMap2Stream(s2->Uni.of(s2.split(",")))
				  .flatMap(s3->Stream.of(s3.set(c)))
				  .forEach(s4->s4.onFailure(e->logger.severe("Error while configuring from property key " + x + "/n" + e.getMessage())));;
				return Uni.of(y);
			};	
			
	/**
	 * MEssage Processor to be supplied by concrete agent 	
	 */
	protected Func<ACLMessage, Uni<MfgConcept>> messageProcessor;			
	
	protected Func<MfgConcept, MfgConcept> service;
			
	/**
	 * default behavior for state less agent.
	 * it processes the incoming message by applying the supplied message processor
	 * then it applies the service providing the extracted concept as input		
	 */
//	Function<ACLMessage, Func<ACLMessage, ACLMessage>> behavior =
//		om->im->{
//			Uni.of(im)
////					.fMap(messageProcessor)
////					.set(i->getContentManager().fillContent(om, (AbsContentElement) service.apply(i)))
//					.onFailure(e->logger.severe("Error while configuring agent service from agent properties " + "\n" + e.getMessage()))
//					.onSuccess(t->logger.info("Agent "+ getName()+ "performed service successfully"));
//			return om;
//	};		
	
	@Override
	protected void setup() {
		
		//Configure agent from supplied property file
		//create a new DFAgentDescription
//		Algo.of(DFAgentDescription::new)
//				//create a new ServiceDescription
//				.set(df->Algo.of(ServiceDescription::new)
//								 //set ServiceDescription name	
//								 .set(sd->sd.setName(getLocalName()))
//								 //Set Service Type (one of AgentType)
//								 .set(sd->sd.setType(AgentType.Service.toString()))
//								 //create a new Property table
//								 .set(sd->Algo.of(Properties::new)
//										 		  //load the property table from the agent property file supplied as argument to this agent
//												  .set(p->p.load(getClass().getResourceAsStream(getArg(1))))
//												  //get the ontology supplied in property file
//												  .fMap(getProperty.apply(getClass().getName()+".ontology")
//														  			//register the ontology for this agent after instantiating by reflection 
//																   .apply(s->Algo.of(s).set(s1->getContentManager().registerOntology((Ontology) Class.forName(s1).getConstructor().newInstance()))
//																		 				   //also add the ontology to Service Description
//																		   				   .onSuccess(s2->sd.addOntologies(s2))))
//												  //get the language specified in property file (one of SL family of languages) 
//												  .fMap(getProperty.apply(getClass().getName()+".codec")
//														  			//register the language for this agent after instantiating by reflection 
//																   .apply(s->Algo.of(s).set(s1->getContentManager().registerLanguage((Codec) Class.forName(s1).getConstructor().newInstance()))
//																		 				   //also add the language to Service Description
//																		   				   .onSuccess(s2->sd.addLanguages(s2))))
//												  //add all properties read from property file to property field of Service Description (this should also include IOPR of the service)
//												  .onSuccess(p->{
//													  //BTW, save the property for future use
//													  this.property = p;
//													  p.forEach((k,v)->sd.addProperties(new Property((String) k, v)));
//												  }))
//								 //add the service to DFAgentDescription
//								.onSuccess(sd->df.addServices(sd)))
//				  //register the DFServiceDescription to this agent via DFService
//				  .set(df->DFService.register(this, df))
//				  //on failure log the exception message
//				  .onFailure(e->logger.severe("Error while configuring agent service from agent properties " + "\n" + e.getMessage()))
//				  //also delete this agent
//				  .onFailure(e->this.doDelete())
//				  //on success log a success message and save the property for future use
//				  .onSuccess(df->{					  
//					  logger.info("Agent "+ getName()+ "is ready");
//				  });
		
		//add default cyclic behavior listening to incoming messages
//		addCyclicBehavior(()->{
//			//receive the incoming message
//			Anything.of(()->receive())
//					//applied the behavior in curried fashion, first the incoming message is supplied and then 
//					//outgoing message is supplied by generating a new ACLMessage from the template of incoming message using 
//					//JADE ACLMessage.createReply
//					.map(m->behavior.apply(m).apply(m.createReply()))
//					//if the message is successful then send the message 
//					.onSuccess(m->send(m));
//		});
		
	}
	
	/**
	 * Retrieve the argument passed with the agent launcher with supplied index
	 * @param index
	 * @return
	 */
	private String getArg(int index){
		return (String) getArguments()[index];
	}
	
	/**
	 * Adds a SimplaBehavior (to this agent) which runs the Runnable type object passed in its action method
	 * also accepts a boolean supplier to delegate done method implementation
	 * @param r 
	 */
	public LambdaAgent addSimpleBehaviour(Runnable r, BooleanSupplier b) {
		// TODO Auto-generated method stub
		addBehaviour(new SimpleBehaviour() {
			
			@Override
			public boolean done() {
				// TODO Auto-generated method stub
				return b.getAsBoolean();
			}
			
			/**
			 * delegate to Runnable type object's implementation
			 */
			@Override
			public void action() {				
				r.run();
			}
		});
		
		return this;
	}
	
	/**
	 * Adds a CyclicBehavior (to this agent) which runs the Runnable type object passed in its action method
	 * @param r 
	 */
	public LambdaAgent addCyclicBehavior(Runnable r){
		addBehaviour(new CyclicBehaviour() {
			
			@Override
			public void action() {
				r.run();
			}
		});
		return this;
	}
	
	/**
	 * Adds a CyclicBehavior (to this agent) which runs the Runnable type object passed in its action method
	 * @param r 
	 */
	public LambdaAgent addOneShotBehavior(Runnable r){
		addBehaviour(new OneShotBehaviour() {
			
			@Override
			public void action() {
				r.run();
			}
		});
		return this;
	}
	
}
