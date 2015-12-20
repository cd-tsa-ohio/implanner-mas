package sandbox;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

import jade.core.AID;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.util.Logger;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

public class AgentLauncher {

	jade.core.Runtime rt = null;
	Profile mainContainerProfile = null;
	AgentContainer mainContainer = null;
	Logger logger = Logger.getJADELogger(this.getClass().getName());
	static ArrayList<AID> agents = new ArrayList<AID>();

	/**
	 * Jade agents live on top of a platform. 
	 * A platform can have one or many containers residing in same or different machines
	 * A main container is the first container launched in the platform
	 * A main container is mandatory to be launched before creating other containers
	 * @param host
	 * @param port
	 */
	public AgentLauncher(String host, int port) {

		super();

		//get jade runtime 
		rt = jade.core.Runtime.instance();

		//create a profile with location parameter for running the main container
		mainContainerProfile = new ProfileImpl(host, port, null);

		//create main container
		mainContainer = rt.createMainContainer(mainContainerProfile);
		
		//Start RMA agent
		AgentController rmaController;
		try {
			rmaController = mainContainer.createNewAgent("rma-1", "jade.tools.rma.rma", null);
			rmaController.start();
		} catch (StaleProxyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public static void main(String[] args) {
		AgentLauncher launcher = null;
		ContainerController solar = null;
		System.out.println("If you want to join in a conversation type your avatar name");
		Scanner scanIn = new Scanner(System.in);
		String name = scanIn.nextLine();
		try{
			//launching main container in the local machine
			launcher = new AgentLauncher(InetAddress.getLocalHost().getHostName(), 8000);

			//create another container 
			solar = launcher.launchContainer(InetAddress.getLocalHost().getHostName(), 8000);

			//create a new agent in solar container
			AgentController a = launcher.launchAgent(solar, AgentVenus.class, name, null);

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("If you want to launch another chat window anytime, type a new avatar name");
		name = scanIn.nextLine();
		//create a new agent in solar container
		try {
			launcher.launchAgent(launcher.launchContainer(InetAddress.getLocalHost().getHostName(), 8000), AgentVenus.class, name, null);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		scanIn.close();


	}

	public void launchLocalAgent(String name){
		try{
			//launching main container in the local machine
			AgentLauncher launcher = new AgentLauncher(InetAddress.getLocalHost().getHostName(), 8000);

			//create another container 
			ContainerController solar = launcher.launchContainer(InetAddress.getLocalHost().getHostName(), 8000);

			//create a new agent in solar container
			launcher.launchAgent(solar, AgentVenus.class, name, null);

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Create any non-main container in the current platform.
	 * @param agentClass
	 * @param name
	 * @param host
	 * @param port
	 */
	public ContainerController launchContainer(String host, int port){

		//create profile 
		Profile containerProfile = new ProfileImpl(host, port, null);

		//create container
		return rt.createAgentContainer(containerProfile);
	}

	/**
	 * method for launching a new agent
	 * @param Container in which agent will be launched
	 * @param agentClass 
	 * @param name
	 * @param arguments to pass to agent
	 * @param host null if isLocal = true
	 * @param port 0 if isLocal=true
	 * @return 
	 * @throws UnknownHostException
	 * @throws StaleProxyException
	 */
	public AgentController launchAgent(ContainerController container, Class agentClass, String name, Object[] arguments){

		AgentController a = null;
		
		try{
			//create the agent
			a=container.createNewAgent(name, agentClass.getName(), arguments);

			//Start agent
			a.start();
		}
		catch(StaleProxyException e){
			logger.severe("Agent "+name+" can not be launched \n"+e.getMessage());
		}

		logger.info(name+" Launched Successfully");
		
		return a;
	}
}
