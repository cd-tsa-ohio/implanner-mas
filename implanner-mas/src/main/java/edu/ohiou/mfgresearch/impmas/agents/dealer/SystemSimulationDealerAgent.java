package edu.ohiou.mfgresearch.impmas.agents.dealer;

import java.util.TreeMap;

import jade.content.ContentElement;
import jade.content.abs.AbsConcept;
import jade.content.abs.AbsIRE;
import jade.content.abs.AbsPredicate;
import jade.content.abs.AbsTerm;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLVocabulary;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Done;
import jade.core.AID;
import jade.core.behaviours.FSMBehaviour;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.Property;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import edu.ohiou.mfgresearch.implanner.MfgConcept;
import edu.ohiou.mfgresearch.impmas.agents.IMPlannerProperties;
import edu.ohiou.mfgresearch.impmas.agents.IMPlannerProperties.AgentType;
import edu.ohiou.mfgresearch.impmas.agents.Markets;
import edu.ohiou.mfgresearch.impmas.agents.MfgAgent;
import edu.ohiou.mfgresearch.impmas.mfgOntology.HaveVendor;
import edu.ohiou.mfgresearch.impmas.semantics.ManagementData;
import edu.ohiou.mfgresearch.impmas.semantics.MfgDF;
import edu.ohiou.mfgresearch.impmas.semantics.PartFile;
import edu.ohiou.mfgresearch.impmas.semantics.ServiceAction;
import edu.ohiou.mfgresearch.impmas.semantics.SimulationFile;

public class SystemSimulationDealerAgent extends MfgAgent{
	
	Markets m = null; 
	AID returnImpAgent;
	public SystemSimulationDealerAgent() throws FIPAException {
		super();
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void setup() {
		super.setup();
	}


	@Override
	public ServiceDescription[] getServices() {
		// TODO Auto-generated method stub
				Markets[] ms = (Markets[]) getArguments();
				this.m = ms[0];
				ServiceDescription[] services = new ServiceDescription[]{new ServiceDescription(){{
					setName(m.getText());
					setType(AgentType.Market.toString());
					Integer performative = new Integer(ACLMessage.QUERY_REF);
					addProperties(new Property(performative.toString(), new HaveVendor()));
				}
				}};
				return services;
	}

	@Override
	public FSMBehaviour getFSM() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void incomingMessageHandler(ACLMessage message) {
		// TODO Auto-generated method stub
		if(message!=null){
			if (message.getPerformative() == ACLMessage.QUERY_REF) {
				incomingQueryMessageHandler(message);
			}
			else if(message.getPerformative() == ACLMessage.REQUEST){
				incomingRequestMessageHandler(message);
			}
		}
		
	}
	
	private void incomingRequestMessageHandler(ACLMessage message) {
		// TODO Auto-generated method stub
		ContentElement ce;
		try {
			logger.info("Following message is recieved from Vendor \n"+message);
			ce = manager.extractContent(message);
			if(ce instanceof Done){
				Done d = (Done) ce;

//				ServiceAction s = (ServiceAction)d.getAction();

				
				Action a = (Action) d.getAction();
				ServiceAction s = (ServiceAction)(a.getAction());

				MfgConcept mfgConcept = s.getOutputObject();
				//save the message content as part in the message bucket
				TreeMap<Integer, MfgConcept> packets = messageBucket.get(s.getManagementData());
				if(packets!=null){
					packets.put(mfgConcept.getPacketNumber(), mfgConcept);
				}
				else{
					packets = new TreeMap<Integer, MfgConcept>();
					packets.put(mfgConcept.getPacketNumber(), mfgConcept);
					messageBucket.put(s.getManagementData(), packets);
				}
				//check if last packet is received
				if(mfgConcept.isLastPacket()){
					addBehaviour(new HandleMessageBucket(requestRegister.get(s.getManagementData()), s.getManagementData()));
				}
				
				ACLMessage msg = createMessage(ACLMessage.CONFIRM);
				/*DFAgentDescription[] customers = MfgDF.searchByService(this, "ImplannerAgent", IMPlannerProperties.AgentType.ImPlanner.toString(), 0);
				for(DFAgentDescription ad:customers){
					msg.addReceiver(ad.getName());
				}*/
				msg.addReceiver(returnImpAgent);
				manager.fillContent(msg, d);
				send(msg);
			}
		} catch (CodecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OntologyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	
		
		
	}

	private void incomingQueryMessageHandler(ACLMessage message) {
		// TODO Auto-generated method stub
		
		logger.info("Following message is recieved from IMPLanner \n"+message);
		//first check the IRE type
		returnImpAgent = message.getSender();
		AbsIRE ire = null;
		try {
			ire = (AbsIRE) manager.extractAbsContent(message);
		} catch (CodecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OntologyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(ire.getTypeName().equals(SLVocabulary.IOTA)){
			//next extract the predicate
			AbsPredicate predicate = ire.getProposition();
			AbsTerm term = predicate.getAbsTerm("input");
			try {
				//log into requestRegister
				AbsConcept concept= (AbsConcept) predicate.getAbsObject("managementData");
				ManagementData managementData = (ManagementData) ontology.toObject(concept);
				requestRegister.put(managementData, message.getSender());
				concept= (AbsConcept) predicate.getAbsObject("input");
				logger.info("concept: " +  concept.getClass().getName() + ";" + concept.toString());
				SimulationFile factFile = (SimulationFile) ontology.toObject(concept);
				ServiceAction s = new ServiceAction();
				s.setInputObject(factFile);
				s.setManagementData(managementData);
				ACLMessage msg = createMessage(ACLMessage.REQUEST);
				
				//Search all vendors 
				DFAgentDescription[] vendors = MfgDF.searchByService(this, "Process Simulation Service", IMPlannerProperties.AgentType.Vendor.toString(), 0);
				for(DFAgentDescription ad:vendors){
					msg.addReceiver(ad.getName());
				}
				Action a = new Action(getAID(), s);
				manager.fillContent(msg, a);
				send(msg);
				logger.info("Following message is sent to vendors "+"\n"+msg);
			} catch (UngroundedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (OntologyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (CodecException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FIPAException e) {
				// TODO Auto-generated catch block
				logger.warning("No Vendor is found for feature recognition "+ e.getMessage());
			}
		}
	}

}
