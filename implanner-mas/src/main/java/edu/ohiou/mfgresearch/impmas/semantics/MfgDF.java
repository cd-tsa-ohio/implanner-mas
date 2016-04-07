package edu.ohiou.mfgresearch.impmas.semantics;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.Property;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.util.leap.Serializable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.TreeMap;

import edu.ohiou.mfgresearch.impmas.agents.LevenshteinDistance;
import edu.ohiou.mfgresearch.impmas.agents.SearchPattern;
import edu.ohiou.mfgresearch.impmas.mfgOntology.HaveVendor;

/**
 * This is a wrapper singleton class for DF service 
 * Lexical matching applied to find an agent based on 
 * the service description. Other matching criteria can be
 * applied later. 
 * Remember to change the registration and search logic 
 * based on the new parameter.
 * @author as888211
 *
 */
public class MfgDF implements Serializable{

	private static final long serialVersionUID = 2083788271830950120L;
	/**
	 * This data structure stores the agent service description
	 * Key is the agent service name (DFServiceDescription.name) 
	 * which is automatically generated
	 * String is the service description
	 */
	//private static HashMap<Long, String> register = new HashMap<Long, String>();

	private static MfgDF instance = null;
	private static Random rand = null;
	
	private static AID systemAgent = null;

	public static MfgDF getInstance(){
		if(instance == null){
			instance = new MfgDF();
			return instance;
		}
		else
			return instance;
	}

	private MfgDF(){
		rand  = new Random();
	}

	/**
	 * Register Agent in the DF
	 * @param a Agent to be registered
	 * @param services services provided by agent
	 * @throws FIPAException
	 */
	public void register(Agent a, ServiceDescription[] services) throws FIPAException{
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(a.getAID());
		if(services != null){
			for(ServiceDescription s:services){
//				Long l = rand.nextLong();
				//register.put(l, s.getName());
//				s.setName(l.toString());
				dfd.addServices(s);
			}
		}
		DFService.register(a, dfd);  
	}
	
	/**
	 * Register Agent in the DF
	 * @param a Agent to be registered
	 * @param services services provided by agent
	 * @throws FIPAException
	 */
	public void register(Agent a, String name) throws FIPAException{
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(a.getAID());
		Long l = rand.nextLong();
		//register.put(l, name);
		DFService.register(a, dfd);  
	}

	/**
	 * Search Agent by service with pattern
	 * @param a Agent searching the DF
	 * @param pattern string to be searched 
	 * @param searchDepth Max number of results to be found. 0 gives all results
	 * @return
	 * @throws FIPAException
	 */
	public DFAgentDescription[] searchByService(Agent a, String pattern, String type, int searchDepth) throws FIPAException{

		SearchPattern sp = new SearchPattern();
		TreeMap<Integer, DFAgentDescription> results = new TreeMap<Integer, DFAgentDescription>();
		DFAgentDescription[] result = null;
		DFAgentDescription dfd = null;
		ServiceDescription sd  = null;

		//Search all agents of the type from directory
		dfd = new DFAgentDescription();
		sd  = new ServiceDescription();
		sd.setType(type);
		dfd.addServices(sd);
		DFAgentDescription[] res = DFService.search(a, dfd);
		
//		for(DFAgentDescription ad:res){
//			Iterator it = ad.getAllServices();
//			while(it.hasNext()){
//				ServiceDescription sds = (ServiceDescription) it.next();
//				if(sp.match_main(register.get(Long.parseLong(sds.getName())), pattern, 0)!=-1){
//					//Match found
//					results.put(LevenshteinDistance.computeLevenshteinDistance(register.get(Long.parseLong(sds.getName())), pattern), ad);
//				}
//			}
//		}
		
		if(searchDepth>0)
			result = new DFAgentDescription[searchDepth];
		else{
			result = new DFAgentDescription[results.size()];
			searchDepth = res.length;
		}
		
		int counter = 0;
		DFAgentDescription lastAgent = null; 
		for(Integer key:results.descendingKeySet()){
			if(lastAgent == null){
				result[counter] = results.get(key);
				lastAgent = results.get(key);
				counter++;
			}
			else{
				if(!lastAgent.equals(results.get(key))){
					result[counter] = results.get(key);
					lastAgent = results.get(key);
					counter++;
				}
			}
			if(counter==searchDepth){
				break;
			}
		}
		
//		for(Long key:register.keySet()){
//			if(sp.match_main(register.get(key), pattern, 0)!=-1){
//				//Match found
//				results.put(LevenshteinDistance.computeLevenshteinDistance(register.get(key), pattern), key);
//			}			
//		}
//
//		//Now search DF for all agents in order of perfect match
//		if(results.size()>0){
//			if(searchDepth>0)
//				result = new DFAgentDescription[searchDepth];
//			else{
//				result = new DFAgentDescription[results.size()];
//				searchDepth = results.size();
//			}
//			int i= 0;
//			for(Integer key:results.keySet()){
//				dfd = new DFAgentDescription();
//				sd  = new ServiceDescription();
//				sd.setName(results.get(key).toString());
////				sd.setType(type);
//				dfd.addServices(sd);
//				DFAgentDescription res = DFService.search(a, dfd)[0];
//				result[i] = DFService.search(a, dfd)[0];
//				i++;
//				if(i==searchDepth){
//					break;
//				}
//			}
//		}
		return result;
	}
	
	public static AID getSystemAgent() {
		return systemAgent;
	}

	public static void setSystemAgent(AID systemAgent) {
		MfgDF.systemAgent = systemAgent;
	}
}
