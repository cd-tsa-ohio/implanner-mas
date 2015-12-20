package edu.ohiou.mfgresearch.impmas.agents.vendor;

import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Done;
import jade.core.AID;
import jade.lang.acl.ACLMessage;

import java.util.HashSet;
import java.util.Set;

import edu.ohiou.mfgresearch.impmas.agents.service.AbstractService;
import edu.ohiou.mfgresearch.impmas.semantics.ServiceAction;


public class VendorService extends AbstractService {
	
	private Coalition coalition;

	public VendorService() {
		// TODO Auto-generated constructor stub
		super(null);
		setServiceDescription("Process Selection Default Service");
	}

	public VendorService(Coalition coalition){
		this();
		this.coalition = coalition;
	}
	
	@Override
	public Set<ACLMessage> performService(ACLMessage message) {
		Action a;
		ServiceAction action;
		Set<ACLMessage> messages = new HashSet<ACLMessage>();
		try {
			a = (Action) serviceAgent.manager.extractContent(message);

			if(a.getAction() instanceof ServiceAction){
				action = (ServiceAction) a.getAction(); 
				serviceAgent.logger.info("Request received from " + message.getSender().getName() + " with semantic " + action.getInputObject().getClassName());
				AID[] targets = coalition.getTargets(new CoalitionKey(message.getSender(),action.getInputObject()));
				
				for(AID t:targets){
					message.clearAllReceiver();
					message.addReceiver(t);
					messages.add(message);
				}
				
				
			}
			
		} catch (CodecException e) {
			// TODO Auto-generated catch block
			serviceAgent.logger.severe(e.getMessage());
		} catch (UngroundedException e) {
			// TODO Auto-generated catch block
			serviceAgent.logger.severe(e.getMessage());
		} catch (OntologyException e) {
			// TODO Auto-generated catch block
			serviceAgent.logger.severe(e.getMessage());
		}
		return messages;
	}

}
