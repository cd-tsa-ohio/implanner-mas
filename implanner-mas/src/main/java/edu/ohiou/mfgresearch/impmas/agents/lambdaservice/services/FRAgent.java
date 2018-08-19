package edu.ohiou.mfgresearch.impmas.agents.lambdaservice.services;

import edu.ohiou.mfgresearch.implanner.parts.MfgPartModel;
import edu.ohiou.mfgresearch.impmas.agents.LambdaAgent;
import edu.ohiou.mfgresearch.impmas.semantics.PartFile;
import edu.ohiou.mfgresearch.impmas.semantics.ServiceAction;
import edu.ohiou.mfgresearch.lambda.Uni;
import jade.content.onto.basic.Action;
import jade.lang.acl.ACLMessage;

@SuppressWarnings("serial")
public class FRAgent extends LambdaAgent {	

	public FRAgent() {
		super();
		/**
		 * this predicate function is default for a service agent
		 * accepted protocol: QUERY_REF
		 * accepted term: Action
		 * accepted concepts: ServiceAction
		 * accepted parent concepts: MfgConcept (slot input)
		 * accepted concrete concept: property .input
		 *   	
		 */
//		messageProcessor = s->{
////			
////			 return Algo.of(s)
////			 		 .filter(m->m.getPerformative()==ACLMessage.QUERY_REF?true:false)
////			 		 //.fMap(m->Algo.of(getContentManager().extractContent(m)))
////			 		 //.fMap(a->Algo.of(((Action) a).getAction()))
////			 		 //.fMap(q->Algo.of(((ServiceAction) q).getInputObject()))
////			 		 .filter(c->Class.forName((String) property.get(getClass().getName()+".input")).isInstance(c));
//			 
//		};
		
		/**
		 * FeatureMapping service
		 */
		service = i->{
			
			return Uni.of(i)
					.map(s->((PartFile) i).getTempFile())
					.map(MfgPartModel::openUGFile)
					.get();
			
		};
		
	}		
	
}
