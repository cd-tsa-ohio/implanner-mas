package edu.ohiou.mfgresearch.impmas.agents;



import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class IMPlannerProperties {

	public static final String ontology = "IMPlannerOntology";
	public static Properties properties = new Properties();
	//@author Suvo
	static{
		 try {
		      URL resourcePropertyURL = IMPlannerProperties.class.getResource(
		    		  "/META-INF/agent.properties");
		      properties.load(resourcePropertyURL.openStream());
		      System.out.println(
		      "Agent properties loaded from IMPlanner resource, " + resourcePropertyURL);
		    }
		    catch (Exception ex) {
		      System.err.println(
		      "Agent properties not loaded from IMPAgent resource file.");
		    }
	}
	
	public static enum MessagePriority{
		MUSTSEND,  //wait future registration if not found now
		TRYTOSEND, //just try to find, don;t bother if not found
	}
	
	//Types of Agents in IMPlanner-MAS System
	public static enum AgentType{
		System,    //not used for registration
		Market,   
		Vendor,     //Vendor agent can act as a service
		Service,   //new lambda type service agents
        ImPlanner
	}
	
	/**
	 * Markets to be created with a dealer agent for each market
	 * @author Arko
	 *
	 */
	/*public static class Markets{
		//FeatureRecognition(properties.getProperty("market.name.1"), true, false, "192.168.1.3", 1099),
		FeatureRecognition("Feature Recognition Market", true, false, "192.168.1.3", 1099),
		ProcessPlanning(properties.getProperty("market.name.2"), false, true, null, 0),
		ActivityPlanning(properties.getProperty("market.name.3"), false, true, null, 0),
		SystemScheduling(properties.getProperty("market.name.4"), false, true, null, 0);
		
		public static List<Markets> marketList= new ArrayList<Markets>();
		
		static{
			String classNames= properties.getProperty("Markets.agentClasses", "");
			String[] classNameArray=classNames.split(",");
			
			for(String s:classNameArray){
				String[] agentNamesArray= properties.getProperty(s+".agents","aD").split(",");
				for(String s1:agentNamesArray){
					String agentIP= properties.getProperty(s+"."+s1+".ip","localhost");
					boolean local=Boolean.parseBoolean(properties.getProperty(s+"."+s1+".local","true"));
					boolean auto=Boolean.parseBoolean(properties.getProperty(s+"."+s1+".auto","false"));
					int agentPort=Integer.parseInt(properties.getProperty(s+"."+s1+".port","1099"));
					marketList.add(new Markets(s,s1,auto,local,agentIP,agentPort));
				}
				
			}
			
		}
		
		public String getText() {
			return className;
		}
		
		@Override
		public String toString(){
			String className=this.className+ ":" + name;
			return className;
		}

		public String getHost() {
			return host;
		}

		public int getPort() {
			return port;
		}

		public boolean isAutomaticCreation() {
			return automaticCreation;
		}

		public boolean isLocalMarket() {
			return localMarket;
		}
		
		private final String name;
		private final String className;
		private final String host;
		private final int port;
		private final boolean automaticCreation;
		private final boolean localMarket;

		public Markets(String text,String name, boolean createAutomatically, boolean localMarket, String host, int port) {
			this.className = text;
			this.host = host;
			this.port = port;
			this.name= name;
			this.automaticCreation = createAutomatically;
			this.localMarket = localMarket;
		}
	}*/

	public static final String guiAgent = "GUI";
	
	public static enum IMPlannerStatus{
		unloaded, //file not loaded, agent initialized
		loaded,   //file loaded
		FR,
		FR_DONE,
		PP,
		PP_DONE,
		AP,
		AP_DONE,
		SP,
		SP_DONE;
	}
	
	/*public static class ServiceClass{
		//FEATURE_RECOGNITION_DEFAULT(agents.service.services.FeatureRecognitionDefaultService.class);
		
		public static List<ServiceClass> servicesList=new ArrayList<ServiceClass>();
		
		
		
		
		static{
			String classNames= properties.getProperty("Services.agentClasses", "");
			String[] classNamesArray= classNames.split(",");
			
			for(String s:classNamesArray){
				String[] servicesArray= properties.getProperty(s + ".services","").split(",");
				for(String s1:servicesArray){
					Class className;
					try {
						className = Class.forName(s);
						String name=s1;
						servicesList.add(new ServiceClass(className,name));
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			}
			
		}
	
		private Class serviceClass;
		private String name;
		
		@Override
		public String toString(){
			String className=this.serviceClass.toString()+ ":" + name;
			return className;
		}
		
		
		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		private ServiceClass(Class serviceClass, String name ){
			this.serviceClass = serviceClass;
			this.name=name;
		}

		public Class getServiceClass() {
			return serviceClass;
		}

		public void setServiceClass(Class serviceClass) {
			this.serviceClass = serviceClass;
		}

	}*/
}
