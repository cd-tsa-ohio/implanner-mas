package sandbox;

import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class ServerAgent {
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			//launching main container in the local machine
			AgentLauncher launcher = new AgentLauncher(InetAddress.getLocalHost().getHostName(), 8000);

			//create another container 
			ContainerController solar = launcher.launchContainer(InetAddress.getLocalHost().getHostName(), 8000);

			//create a new agent in solar container
			AgentController a = launcher.launchAgent(solar, AgentVenus.class, "ServerAgent", null);
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
