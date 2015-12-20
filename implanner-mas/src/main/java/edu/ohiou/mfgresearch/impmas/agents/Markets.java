package edu.ohiou.mfgresearch.impmas.agents;

import java.util.ArrayList;
import java.util.List;



public class Markets {

	/**
	 * @param args
	 */

	//FeatureRecognition(properties.getProperty("market.name.1"), true, false, "192.168.1.3", 1099),
	/*FeatureRecognition("Feature Recognition Market", true, false, "192.168.1.3", 1099),
	ProcessPlanning(properties.getProperty("market.name.2"), false, true, null, 0),
	ActivityPlanning(properties.getProperty("market.name.3"), false, true, null, 0),
	SystemScheduling(properties.getProperty("market.name.4"), false, true, null, 0);*/
	
	public static List<Markets> marketList= new ArrayList<Markets>();
	public static List<Markets> marketAutoList = new ArrayList<Markets>();
	
	static{
		String classNames= IMPlannerProperties.properties.getProperty("Markets.agentClasses", "");
		String[] classNameArray=classNames.split(",");
		
		for(String s:classNameArray){
			String[] agentNamesArray= IMPlannerProperties.properties.getProperty(s+".agents","aD").split(",");
			for(String s1:agentNamesArray){
				String agentIP= IMPlannerProperties.properties.getProperty(s+"."+s1+".ip","localhost");
				boolean local=Boolean.parseBoolean(IMPlannerProperties.properties.getProperty(s+"."+s1+".local","true"));
				boolean auto=Boolean.parseBoolean(IMPlannerProperties.properties.getProperty(s+"."+s1+".auto","false"));
				int agentPort=Integer.parseInt(IMPlannerProperties.properties.getProperty(s+"."+s1+".port","1099"));
				if(auto){
					marketAutoList.add(new Markets(s,s1,auto,local,agentIP,agentPort));
				}else{
					marketList.add(new Markets(s,s1,auto,local,agentIP,agentPort));
				}
				
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


}
