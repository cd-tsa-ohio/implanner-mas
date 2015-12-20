package edu.ohiou.mfgresearch.impmas.agents;

import java.util.ArrayList;
import java.util.List;



public class ServiceClass {

	/**
	 * @param args
	 */

	//FEATURE_RECOGNITION_DEFAULT(agents.service.services.FeatureRecognitionDefaultService.class);
	
	public static List<ServiceClass> servicesList=new ArrayList<ServiceClass>();
	
	
	
	
	static{
		String classNames= IMPlannerProperties.properties.getProperty("Services.agentClasses");
		String[] classNamesArray= classNames.split(",");
		
		for(String s:classNamesArray){
			String[] servicesArray=  IMPlannerProperties.properties.getProperty(s + ".services").split(",");
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



}
