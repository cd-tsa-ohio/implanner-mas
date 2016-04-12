package edu.ohiou.mfgresearch.impmas.agents.service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import edu.ohiou.mfgresearch.implanner.MfgConcept;
import edu.ohiou.mfgresearch.impmas.agents.IMPlannerProperties.AgentType;
import edu.ohiou.mfgresearch.impmas.agents.MfgAgent;
import edu.ohiou.mfgresearch.impmas.semantics.ServiceAction;
import sun.font.EAttribute;
import jade.content.ContentElement;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Done;
import jade.core.AID;
import jade.core.behaviours.FSMBehaviour;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.Property;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

public class ServiceAgent extends MfgAgent {

	private static final long serialVersionUID = -5408084721824240847L;

	public Map<String, AID> requestRegister = new HashMap<String, AID>();
	private AbstractService service = null;
	public void setup(){
		
		super.setup();
		//MfgDF.setSystemAgent(this.getAID());
		
		/*for(ServiceClass s:ServiceClass.servicesList){
			launchAgent(s);
		}*/
	}

	public ServiceAgent() throws FIPAException {
		// TODO Auto-generated constructor stub
	}

	@Override
	public FSMBehaviour getFSM() {
		return null;
	}

	@Override
	public void incomingMessageHandler(ACLMessage message) {
		Set<MfgConcept> input = null; //should be extracted in isValidRequest() method
		ServiceAction action;
		if(message!=null){
			Set<ACLMessage> messages = service.performService(message);
			for(ACLMessage msg:messages){
				send(msg);
			}
		}
	}

	@Override
	public ServiceDescription[] getServices(){
		// TODO Auto-generated method stub
		String serviceClass = (String) getArguments()[0];
		try {
			this.service = (AbstractService) Class.forName(serviceClass).newInstance();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		service.setServiceAgent(this);
		ServiceDescription[] services = new ServiceDescription[]{new ServiceDescription(){{
			setName(service.getServiceDescription());
			setType(AgentType.Vendor.toString());
			Integer performative = new Integer(ACLMessage.REQUEST);
			addProperties(new Property(performative.toString(), new ServiceAction()));
		}
		}};
		return services;
	}
}
