package edu.ohiou.mfgresearch.impmas.semantics;

import jade.core.Agent;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;

public class AgentAlias {

	private Agent agent = null;
	private DFAgentDescription agentDescription = null;

	/**
	 * @param a
	 * @param agentDescription
	 * @throws Exception 
	 */
	public AgentAlias(Agent a, DFAgentDescription agentDescription) throws Exception{
		this.agent = a;
		this.agentDescription = agentDescription;
		if(agentDescription.getName()!=null)
			throw new Exception("No AID is found");
	}

	/**
	 * Only to be used when only one particular agent is searched without any 
	 * notification sent to DF for further registry
	 * @param a
	 * @param searchPattern
	 * @throws Exception 
	 */
	public AgentAlias(Agent a, String searchPattern) throws Exception {
		this.agent = a; 
//		this.agentDescription = MfgDF.searchByService(agent, searchPattern, 1)[0];
		if(agentDescription.getName()!=null)
			throw new Exception("No AID is found");
	}

	public static AgentAlias[] searchAgent(Agent a, String searchPattern, int searchDepth, boolean doNotify) throws Exception{

		AgentAlias[] aliases = null;

//		DFAgentDescription[] results = MfgDF.searchByService(a, searchPattern, searchDepth);
//		if(results.length>1){
//			aliases = new AgentAlias[results.length];
//			int i = 0;
//			for(DFAgentDescription df:results){
//				if(df.getName()!=null){
//					aliases[i] = new AgentAlias(a, df);
//				}
//			}
//		}
		return aliases;
	}

	public DFAgentDescription getAgentDescription() {
		return agentDescription;
	}

	public void setAgentDescription(DFAgentDescription agentDescription) {
		this.agentDescription = agentDescription;
	}
	
	public void sendMessage(ACLMessage message){
		message.addReceiver(agentDescription.getName());
		agent.send(message);
	}

}
