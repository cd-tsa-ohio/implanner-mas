package edu.ohiou.mfgresearch.impmas.agents;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
//<<<<<<< MfgAgent.java
import java.util.logging.ConsoleHandler;
import java.util.logging.LogRecord;
//=======
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;

import edu.ohiou.mfgresearch.implanner.MfgConcept;
//import examples.content.eco.elements.Owns;
//>>>>>>> 1.8

import edu.ohiou.mfgresearch.impmas.agents.service.AbstractService;
import edu.ohiou.mfgresearch.impmas.agents.service.ServiceAgent;
import edu.ohiou.mfgresearch.impmas.mfgOntology.IMPlannerOntology;
import edu.ohiou.mfgresearch.impmas.semantics.ManagementData;
import edu.ohiou.mfgresearch.impmas.semantics.MfgDF;
import edu.ohiou.mfgresearch.impmas.semantics.ResultIs;
import jade.content.ContentManager;
import jade.content.abs.AbsConcept;
import jade.content.abs.AbsIRE;
import jade.content.abs.AbsPredicate;
import jade.content.abs.AbsVariable;
import jade.content.lang.Codec;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.lang.sl.SLVocabulary;
import jade.content.onto.BeanOntologyException;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.core.AID;
import jade.core.Agent;
import jade.core.ProfileImpl;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.FSMBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.util.Logger;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

/**
 * Abstract class for creating any agent
 * Provides wrapper method for searching and sending messages to other agents
 * It also provides a abstract method incomingMessageHandler to handle incoming messages
 * By default it registers the gui agent as gui agent should be recognized by every agents
 * @author as888211
 *
 */
public abstract class MfgAgent extends Agent {

	private static final long serialVersionUID = -2723621031192317736L;

	//Jade logger for logging messages
	public Logger logger = Logger.getJADELogger(this.getClass().getName());
	FileHandler fh;
	//Content manager for extracting Ontology
	public ContentManager manager = (ContentManager) getContentManager();
	// This agent "speaks" the SL language
	protected Codec codec = new SLCodec();
	// This agent must know the IMPLanner Ontology
	protected Ontology ontology;

	//messageQueue for unsent messages
	private Map<Date, MessageFormat> messageQueue = new TreeMap<Date, MessageFormat>();

	//Agent register(other known agents to this agent)
	private Map<String, AID> otherAgents = new HashMap<String, AID>();
	
	//Agent message bucket for part messages 
	protected Map<ManagementData, TreeMap<Integer, MfgConcept>> messageBucket = new HashMap<ManagementData, TreeMap<Integer, MfgConcept>>();

	//Register for agent requests
	protected Map<ManagementData, AID> requestRegister = new HashMap<ManagementData, AID>();
	
	//Random number
	protected Random rand = new Random();
	
	/**
	 * Launching without any services
	 * @throws FIPAException
	 */
	public MfgAgent() throws FIPAException{
		try {
			ontology = IMPlannerOntology.getInstance();
		} catch (BeanOntologyException e) {
			// TODO Auto-generated catch block
			logger.severe("IMPlannerOntology can not be instantiated \n"+e.getMessage());
		}
		manager.registerLanguage(codec);
		manager.registerOntology(ontology);
		
		//configure the logger 
		ConsoleHandler consoleHandler = new ConsoleHandler(){

			@Override
			public void publish(LogRecord record) {
				// TODO Auto-generated method stub
				super.publish(record);
				
			}
			
		};
		
	}

	@Override
	protected void setup() {
		// TODO Auto-generated method stub
		super.setup();

		//Register in the DF
		try {
			
			fh=new FileHandler("C:/logs/logfile.log");
			logger.addHandler(fh);
			SimpleFormatter formatter=new SimpleFormatter();
			fh.setFormatter(formatter);
			
			logger.info("my first log");
			MfgDF.getInstance().register(this, getServices());
			
			
			
		} catch (FIPAException e) {
			// TODO Auto-generated catch block
			logger.severe("Agent "+this+ " failed to register in Directory Facility \n"+e.getMessage());
		} catch (SecurityException e) {  
	        e.printStackTrace();  
		} catch (IOException e) {  
	        e.printStackTrace();  
	    }  

		//handle incoming messages 
		addBehaviour(new HandleIncomingMessages());

		//Register FSM Behavior
		FSMBehaviour fsm = getFSM();
		if(fsm != null)
			addBehaviour(fsm);
	}
	
	/**
	 * Return all services provided by this agent
	 * @return
	 */
	public abstract ServiceDescription[] getServices();

	/**
	 * Return Finite State Machine for the agent 
	 * FSM controls agent's actions
	 * @return
	 */
	public abstract FSMBehaviour getFSM();

	/**
	 * This method will search any agent by pattern and if not found
	 * it may store the message in queue and raise a notification in DF 
	 * for future registration...
	 * @param conversationID if kept blank agent is searched on basis of search pattern
	 * however already provided conversationID will directly try to send message to the 
	 * corresponding agent registered with the conversation ID
	 * @param searchPattern search pattern for finding agent by service
	 * @param message ACLMessage (Receiver will be set automatically if agent is found)
	 * @param searchDepth max number of results expected
	 * @param priority
	 * @return
	 * @throws FIPAException
	 */
	public String sendMessage(String conversationID, String searchPattern, String type, ACLMessage message, int searchDepth, IMPlannerProperties.MessagePriority priority) throws FIPAException{

		DFAgentDescription[] results = null;

		//Search for agent(s)
		if(conversationID == null){
			switch (priority) {
			//Try to find agent and store message in queue for future use
			case MUSTSEND:
				results = MfgDF.searchByService(this, searchPattern, type, searchDepth);
				if(results.length>1){
					int i=1;
					for(DFAgentDescription df:results){
						if(df.getName()!=null){
							//Conversation ID is created by <sender AID>^<Reciever AID>
							conversationID = this.getAID().toString()+"^"+df.getName().toString();
							message.addReceiver(df.getName());
							message.setConversationId(conversationID);
							//store conversation ID for future purpose
							registerAgent(conversationID, df.getName());
							if(i==searchDepth){
								break;
							}
							i++;
						}
						else{
							//generate temporary conversation ID
							Random rand = new Random();
							Long l = new Long(rand.nextLong());
							String tempConversationID = l.toString();
							SearchConstraints sc = new SearchConstraints();
							sc.setMaxDepth(new Long(searchDepth));
							//Send notification to DF
							ACLMessage dfMessage = DFService.createSubscriptionMessage(this, this.getDefaultDF(), df, sc);
							message.setConversationId(tempConversationID);
							send(dfMessage);
							//Store message in queue
							messageQueue.put(Calendar.getInstance().getTime(), new MessageFormat(tempConversationID,
									null, 2, message));
						}
					}
				}
				break;
				// Just try to send the message but no need to save message in queue if 
				//	any agent is not found
			case TRYTOSEND:
				results = MfgDF.searchByService(this, searchPattern, type, searchDepth);
				if(results.length>1){
					int i=1;
					for(DFAgentDescription df:results){
						//Conversation ID is created by <sender AID>^<Reciever AID>
						conversationID = this.getAID().toString()+"^"+df.getName().toString();
						message.addReceiver(df.getName());
						message.setConversationId(conversationID);
						if(i==searchDepth){
							break;
						}
						i++;
					}
				}
				break;
			default:
				break;
			}
		}
		else{
			message.addReceiver(otherAgents.get(conversationID));
		}
		send(message);

		return conversationID;
	}

	public void registerAgent(String conversationID, AID aid){
		otherAgents.put(conversationID, aid);
	}

	public void deregisterAgent(String conversationID){
		otherAgents.remove(conversationID);
	}

	/**
	 * This behavior receives message from DFAgent when some requested agent registers
	 * This behavior searches for any pending message in the message queue for that particular
	 * agent and resends them
	 * Turn this behavior off by setting setIncomingListener false
	 * If the message is not from DF then it is left to the abstract method incomingMessageHandler 
	 * to proces it
	 * @author Arko
	 *
	 */
	class HandleIncomingMessages extends CyclicBehaviour 
	{
		private static final long serialVersionUID = -6524327247921489138L;

		public void action() 
		{
			ACLMessage msg = receive();
			if (msg != null && msg.getSender().equals(getDefaultDF()))
			{
				try {
					DFAgentDescription[] dfds =	DFService.decodeNotification(msg.getContent());
					if (dfds.length > 0){
						//get all messages for the conversation ID
						ArrayList<ACLMessage> messages = new ArrayList<ACLMessage>();
						for(Date d:messageQueue.keySet()){
							if(messageQueue.get(d).conversationID.equals(msg.getConversationId()) 
									&& messageQueue.get(d).status == 2){
								messages.add(messageQueue.get(d).message);
								messageQueue.get(d).status = 1;
							}
						}
						//send all messages to all agents found
						for(ACLMessage m:messages){
							for(DFAgentDescription dfa:dfds){
								m.addReceiver(dfa.getName());
								String conversationID = m.getSender().toString()+"^"+dfa.getName().toString();
								m.setConversationId(conversationID);
								//store agent in the agent list with permanent conversation ID
								registerAgent(conversationID, dfa.getName());
							}
							send(m);
						}
					}                 
				}
				catch (Exception ex) {
					logger.severe(ex.getMessage());
				}
			}
			else{
				incomingMessageHandler(msg);
			}
			block(); 
		}
	}

	/**
	 * Implement this method to handle all incoming messages
	 * @param message
	 */
	public abstract void incomingMessageHandler(ACLMessage message);

	/**
	 * get all messages from message bucket for a particular ManagementData
	 * and send it to receiver specified
	 * @author arkopaul.sarkar
	 *
	 */
	public class HandleMessageBucket extends OneShotBehaviour{

		private static final long serialVersionUID = -4481625502314482375L;
		private AID receiver;
		private ManagementData managementData;
		
		public HandleMessageBucket(AID receiver, ManagementData managementData) {
			super();
			this.receiver = receiver;
			this.managementData = managementData;
		}
		
		@Override
		public void action() {
			// TODO Auto-generated method stub
			TreeMap<Integer, MfgConcept> packets = messageBucket.get(managementData);
			for(Integer key:packets.keySet()){
				ACLMessage msg = createMessage(ACLMessage.INFORM);
				msg.addReceiver(receiver);
				ResultIs result = new ResultIs();
				result.setManagementData(managementData);
				result.setConcept(packets.get(key));
				try {
					manager.fillContent(msg, result);
				} catch (CodecException | OntologyException e) {
					// TODO Auto-generated catch block
					logger.severe(e.getMessage());
				}
				send(msg);
			}
		}
	}
	
	/**
	 * Creates a skeleton of message with a performative
	 * @param perf
	 * @return
	 */
	public ACLMessage createMessage(int perf){
		ACLMessage msg = new ACLMessage(perf);
		msg.setLanguage(codec.getName());
		msg.setOntology(ontology.getName());
		return msg;
	}

	/**
	 * Add a new Behavior to this agent
	 * @param b
	 */
	public void addNewBehavior(Behaviour b){
		addBehaviour(b);
	}

	/**
	 * Utility method for launching a new agent
	 * @param agentClass 
	 * @param name
	 * @param services
	 * @param isLocal true if the agent is to be launched in main container
	 * @param host null if isLocal = true
	 * @param port 0 if isLocal=true
	 * @throws UnknownHostException
	 * @throws StaleProxyException
	 */
	public void launchAgent(Class agentClass, String name, Object[] arguments, boolean isLocal, String host, int port){

		AgentController a = null;

		ContainerController ac = getContainerController();
		//Get the runtime
		jade.core.Runtime rt = jade.core.Runtime.instance();

		//Get local host and post is the agent is local
		try{
			if(isLocal){
				a = ac.createNewAgent(name, agentClass.getName(), arguments);
			}
			else{
				//create the container and create agent in that
				ProfileImpl pContainer = new ProfileImpl(host, port, null);
				ContainerController containerRef = rt.createAgentContainer(pContainer);
				a=containerRef.createNewAgent(name, agentClass.getName(), arguments);
			}

			//Start agent
			a.start();
		}
		catch(StaleProxyException e){
			logger.severe("Agent "+name+" can not be launched \n"+e.getMessage());
		}

		logger.info(name+" Launched Successfully");
	}
	/**
	 * Utility method for launching a new agent
	 * @param market 
	 
	 * @throws UnknownHostException
	 * @throws StaleProxyException
	 * @author ghosal
	 */
	public void launchAgent(Object o){
		Markets m= (Markets)o;

		AgentController a = null;

		ContainerController ac = getContainerController();
		//Get the runtime
		jade.core.Runtime rt = jade.core.Runtime.instance();

		//Get local host and post is the agent is local
		try{
			if( m.isLocalMarket()){
				a = ac.createNewAgent(m.toString()+"Market", m.getText(), new Markets[]{m});
			}
			else{
				//create the container and create agent in that
				ProfileImpl pContainer = new ProfileImpl(m.getHost(), m.getPort(), null);
				ContainerController containerRef = rt.createAgentContainer(pContainer);
				a=containerRef.createNewAgent(m.toString()+"Market", m.getText(), new Markets[]{m});
			}

			//Start agent
			a.start();
		}
		catch(StaleProxyException e){
			logger.severe("Agent "+m.toString()+"Market"+" can not be launched \n"+e.getMessage());
		}

		logger.info(m.toString()+"Market"+" Launched Successfully");
	}
	
	public void launchAgent(ServiceClass s){
		try {
			AbstractService service = (AbstractService) s.getServiceClass().newInstance();
			launchAgent(ServiceAgent.class, s.toString()+"Service", new AbstractService[]{service}, false, "192.168.2.9", 1099);
			
		} catch (InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	/**
	 * Structure for messageQueue
	 * @author Arko
	 *
	 */
	class MessageFormat{

		public String conversationID;
		public AID receiver;
		public int status;
		public ACLMessage message;

		public MessageFormat(String conversationID, AID receiver, int status, ACLMessage message) {
			this.conversationID = conversationID;
			this.receiver = receiver;
			this.status = status;
			this.message = message;
		}

	}
	
	class RequestFormat{
		public ManagementData managementData;
		public String conversationID;
		public RequestFormat(ManagementData managementData,
				String conversationID) {
			super();
			this.managementData = managementData;
			this.conversationID = conversationID;
		}
		@Override
		public boolean equals(Object obj) {
			if(obj instanceof RequestFormat){
				RequestFormat request = (RequestFormat) obj;
				if(request.managementData.equals(this.managementData)&&
						request.conversationID.equals(this.conversationID)){
							return true;
				}
			}
			return false;
		}
	}
	
}
