package edu.ohiou.mfgresearch.impmas.agents;

import java.util.function.BooleanSupplier;

import jade.content.lang.Codec;
import jade.content.onto.Ontology;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SimpleBehaviour;

public class JLAgent extends Agent {

	private static final long serialVersionUID = 1463175593473481876L;
	
	/**
	 * Before an agent can actually use the selected content language, they must
     * be registered to the content manager of the agent. 
	 * @param c
	 * @return
	 */
	public JLAgent addCodec(Codec c){
		getContentManager().registerLanguage(c);
		return this;
	}
	
	/**
	 * Before an agent can actually use the selected content language, they must
     * be registered to the content manager of the agent. 
	 * @param c
	 * @return
	 */
	public JLAgent addOntology(Ontology o){
		getContentManager().registerOntology(o);
		return this;
	}

	/**
	 * Adds a SimplaBehavior (to this agent) which runs the Runnable type object passed in its action method
	 * also accepts a boolean supplier to delegate done method implementation
	 * @param r 
	 */
	public JLAgent addSimpleBehaviour(Runnable r, BooleanSupplier b) {
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
	public JLAgent addCyclicBehavior(Runnable r){
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
	public JLAgent addOneShotBehavior(Runnable r){
		addBehaviour(new OneShotBehaviour() {
			
			@Override
			public void action() {
				r.run();
			}
		});
		return this;
	}

}
