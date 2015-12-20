package edu.ohiou.mfgresearch.impmas.agents.service.services;

import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Done;
import jade.lang.acl.ACLMessage;

import java.util.HashSet;
import java.util.Set;

import edu.ohiou.mfgresearch.implanner.MfgConcept;
import edu.ohiou.mfgresearch.impmas.agents.service.AbstractService;
import edu.ohiou.mfgresearch.impmas.semantics.ServiceAction;

public abstract class AbstractFreelancerService extends AbstractService {

	public AbstractFreelancerService(String serviceDescription) {
		super(serviceDescription);
		// TODO Auto-generated constructor stub
	}
	
	public abstract Set<MfgConcept> performService(MfgConcept input);

	@Override
	public Set<ACLMessage> performService(ACLMessage message) {
		
		Set<ACLMessage> messages = new HashSet<ACLMessage>();
		Set<MfgConcept> input = null; //should be extracted in isValidRequest() method
		ServiceAction action;
		if (message.getPerformative() == ACLMessage.REQUEST) {
			//analyze request
			Action a;
			try {
				a = (Action) serviceAgent.manager.extractContent(message);

				if(a.getAction() instanceof ServiceAction){
					action = (ServiceAction) a.getAction(); 

					//register valid request
					serviceAgent.logger.info("Request received from dealer " +action.toString());
					serviceAgent.requestRegister.put(message.getConversationId(), message.getSender());
					//perform the service
					Set<MfgConcept> outputs = performService(action.getInputObject());

					action.setInputObject(null);

					int size = outputs.size();
					int packetNum = 1;
					for(MfgConcept mc:outputs){
						//label packet
						mc.setPacketNumber(packetNum);
						if(size==packetNum){
							mc.setLastPacket(true);
						}
						size--;
						packetNum++;
						action.setOutputObject(mc);
						//send done information
						//Done d = new Done(action);
						Done d = new Done();
						d.setAction(a);

						/*ACLMessage msg = new ACLMessage(ACLMessage.INFORM); 
						manager.fillContent(msg, d);
						send(msg);*/
						ACLMessage msg = message.createReply();
						//msg.setPerformative(ACLMessage.CONFIRM);

						serviceAgent.manager.fillContent(msg, d);
						messages.add(msg);
					}
				}
			} catch (CodecException e) {
				// TODO Auto-generated catch block
				serviceAgent.logger.severe(e.getMessage());
			} catch (OntologyException e) {
				// TODO Auto-generated catch block
				serviceAgent.logger.severe(e.getMessage());
			}
			
		}
		return messages;
	}

}
