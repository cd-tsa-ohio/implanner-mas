package edu.ohiou.mfgresearch.impmas.agents.vendor;

import jade.core.AID;

import java.util.HashMap;
import java.util.Map;

import edu.ohiou.mfgresearch.implanner.MfgConcept;


public class Coalition {
	
	private Map<CoalitionKey, AID[]> transitions = new HashMap<CoalitionKey, AID[]>();
	private AID leader;
	
	public Coalition(AID leader) {
		super();
		this.leader = leader;
	}

	public void addTargets(CoalitionKey key, AID[] targets){
		transitions.put(key, targets);
	}

	public void addTarget(CoalitionKey key, AID target){
		AID[] targets = transitions.get(key);
		AID[] newTargets = new AID[targets.length+1];
		newTargets[targets.length] = target;
		transitions.put(key, newTargets);
	}
	
	
	public AID[] getTargets(CoalitionKey key){
		return transitions.get(key);
	}

	public AID getLeader() {
		return leader;
	}
	
}

class CoalitionKey{
	
	private AID sender;
	private MfgConcept semantic;
	
	public CoalitionKey(AID sender, MfgConcept semantic) {
		super();
		this.sender = sender;
		this.semantic = semantic;
	}
	
	
	
	public AID getSender() {
		return sender;
	}



	public MfgConcept getSemantic() {
		return semantic;
	}



	@Override
	public boolean equals(Object arg0) {
		// TODO Auto-generated method stub
		if(arg0 instanceof CoalitionKey){
			CoalitionKey c = (CoalitionKey) arg0;
			if(c.sender==this.sender ){
				if(c.semantic.getClassName().equals(this.semantic.getClassName())){
					return true;
				}
			}
			else
				return false;
		}
		return false;
	}

	@Override
	public int hashCode() {
		return sender.hashCode() + semantic.getClassName().hashCode(); 
	}
	
}