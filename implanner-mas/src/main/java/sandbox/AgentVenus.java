package sandbox;

import java.awt.Color;
import java.util.ArrayList;

import javax.swing.JFrame;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.util.Logger;

public class AgentVenus extends Agent {

	private static final long serialVersionUID = -7960635551205511751L;
	protected Logger logger = Logger.getJADELogger(this.getClass().getName());
	AID agent = null;
	ChatApp chatWin = null;

	public AgentVenus() {

	}

	@Override
	protected void setup() {
		super.setup();
		logger.info("Hi! I'm chat agent "+getLocalName()+" Here is my badge... "
				+"\nID: "+ getAID()
				+"\nLocation: " + here().getAddress());
		AgentLauncher.agents.add(getAID());
		logger.info("Agent Venus is trying to create the chat window...");
		JFrame.setDefaultLookAndFeelDecorated(true);
		chatWin = new ChatApp();
		chatWin.setVisible(true);
		logger.info("Chat window created...");
		chatWin.appendText(getLocalName()+" joined the conversation.", Color.MAGENTA);

		//create DF registration
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName( getAID() ); 
		ServiceDescription sd  = new ServiceDescription();
		sd.setType( "chat" );
		sd.setName( getLocalName() );
		dfd.addServices(sd);

		try {  
			DFService.register(this, dfd );  
		}
		catch (FIPAException fe) { fe.printStackTrace(); }

		//create DF subscription
		DFAgentDescription dfd1 = new DFAgentDescription();
		ServiceDescription sd1 = new ServiceDescription();
		sd1.setType("chat");
		dfd1.addServices(sd1);
		SearchConstraints sc = new SearchConstraints();
		sc.setMaxResults(new Long(5));
		try {
			DFAgentDescription[] result = DFService.search(this, getDefaultDF(), dfd1, sc);
			if(result.length>0){
				for(DFAgentDescription df:result){
					if(!df.getName().equals(getAID())){
						chatWin.appendText(df.getName().getLocalName()+" joined the conversation.", Color.GREEN);
						agent = df.getName();
					}
				}
			}

		} catch (FIPAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		addBehaviour(new ListenerBehavior());
	}
	
	protected void takeDown() {
		
		chatWin.setVisible(false);
	}

	public void sendMessage(String text){
		ACLMessage message = new ACLMessage(ACLMessage.PROPAGATE);
		for(AID aid:AgentLauncher.agents){
			message.addReceiver(aid);
		}
		message.setContent(text);
		send(message);
	}

	class ListenerBehavior extends CyclicBehaviour{

		@Override
		public void action() {

//			ACLMessage msg = receive(MessageTemplate.MatchSender(getDefaultDF()));
//			//check new agent
//			if (msg != null)
//			{
//				try {
//					DFAgentDescription[] dfds =    
//							DFService.decodeNotification(msg.getContent());
//					if (dfds.length > 0){
//						AgentLauncher.agents.add(dfds[0].getName());
//						chatWin.appendText(dfds[0].getName().getLocalName()+" joined the conversation.");
//					}
//				}
//				catch (Exception ex) {}
//			}

			//check incoming message 
			ACLMessage inMsg = receive(MessageTemplate.MatchPerformative(ACLMessage.PROPAGATE));
			if(inMsg!=null){
				chatWin.appendText(inMsg.getSender().getLocalName()+": "+inMsg.getContent(), Color.GREEN);
				if(agent==null){
					chatWin.appendText(inMsg.getSender().getLocalName()+" joined the conversation", Color.GREEN);
					agent = inMsg.getSender();
				}
			}
		}
	}

	/**
	 * Chat Window frame
	 * @author arkopaul.sarkar
	 *
	 */
	class ChatApp extends javax.swing.JFrame {

		/**
		 * Creates new form ChatWindow
		 */
		public ChatApp() {
			initComponents();
		}

		/**
		 * This method is called from within the constructor to initialize the form.
		 * WARNING: Do NOT modify this code. The content of this method is always
		 * regenerated by the Form Editor.
		 */
		@SuppressWarnings("unchecked")
		// <editor-fold defaultstate="collapsed" desc="Generated Code">                          
		private void initComponents() {

			jPanel1 = new javax.swing.JPanel();
			jSplitPane1 = new javax.swing.JSplitPane();
			jScrollPane1 = new javax.swing.JScrollPane();
			jTextArea1 = new javax.swing.JTextArea();
			jScrollPane2 = new javax.swing.JScrollPane();
			jTextArea2 = new javax.swing.JTextArea();
			jButton1 = new javax.swing.JButton();

			setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
			setAlwaysOnTop(true);
			setName("mainFrame"); // NOI18N

			jSplitPane1.setDividerLocation(310);
			jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
			jSplitPane1.setLastDividerLocation(350);

			jTextArea1.setColumns(20);
			jTextArea1.setRows(5);
			jScrollPane1.setViewportView(jTextArea1);

			jSplitPane1.setTopComponent(jScrollPane1);

			jTextArea2.setColumns(20);
			jTextArea2.setRows(5);
			jScrollPane2.setViewportView(jTextArea2);

			jSplitPane1.setBottomComponent(jScrollPane2);

			javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
			jPanel1.setLayout(jPanel1Layout);
			jPanel1Layout.setHorizontalGroup(
					jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
					.addGap(0, 294, Short.MAX_VALUE)
					.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
							.addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 294, Short.MAX_VALUE))
					);
			jPanel1Layout.setVerticalGroup(
					jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
					.addGap(0, 407, Short.MAX_VALUE)
					.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
							.addGroup(jPanel1Layout.createSequentialGroup()
									.addContainerGap()
									.addComponent(jSplitPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 364, javax.swing.GroupLayout.PREFERRED_SIZE)
									.addContainerGap(30, Short.MAX_VALUE)))
					);

			jButton1.setText("Send Message");
			jButton1.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					jButton1ActionPerformed(evt);
				}
			});

			javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
			getContentPane().setLayout(layout);
			layout.setHorizontalGroup(
					layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
					.addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 294, Short.MAX_VALUE)
					.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
							.addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
					);
			layout.setVerticalGroup(
					layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
					.addGroup(layout.createSequentialGroup()
							.addContainerGap(388, Short.MAX_VALUE)
							.addComponent(jButton1))
							.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
									.addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
											.addGap(0, 6, Short.MAX_VALUE)
											.addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
					);

			pack();
		}// </editor-fold>                        
                                    

		private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {                                         
			String text = jTextArea2.getText();
			appendText(getAID().getLocalName()+": "+text, Color.MAGENTA);
			jTextArea2.setText("");

			if(agent!= null){
				ACLMessage message = new ACLMessage(ACLMessage.PROPAGATE);

				message.addReceiver(agent);
				message.setContent(text);
				send(message);
			}
		}    


		public void appendText(String text, Color c){
			jTextArea1.setForeground(Color.BLUE);
			jTextArea1.append("\n"+text);
		}


		public void startChat() {




			/* Set the Nimbus look and feel */
			//<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
			/* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
			 * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
			 */
			//	        try {
			//	            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
			//	                if ("Nimbus".equals(info.getName())) {
			//	                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
			//	                    break;
			//	                }
			//	            }
			//	        } catch (ClassNotFoundException ex) {
			//	            java.util.logging.Logger.getLogger(ChatApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
			//	        } catch (InstantiationException ex) {
			//	            java.util.logging.Logger.getLogger(ChatApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
			//	        } catch (IllegalAccessException ex) {
			//	            java.util.logging.Logger.getLogger(ChatApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
			//	        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
			//	            java.util.logging.Logger.getLogger(ChatApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
			//	        }
			//</editor-fold>

			/* Create and display the form */
			//	        java.awt.EventQueue.invokeLater(new Runnable() {
			//	            public void run() {
			//	                new ChatApp().setVisible(true);
			//	            }
			//	        });

		}
		// Variables declaration - do not modify                     
		private javax.swing.JButton jButton1;
		private javax.swing.JPanel jPanel1;
		private javax.swing.JScrollPane jScrollPane1;
		private javax.swing.JScrollPane jScrollPane2;
		private javax.swing.JSplitPane jSplitPane1;
		private javax.swing.JTextArea jTextArea1;
		private javax.swing.JTextArea jTextArea2;
		// End of variables declaration                   
	}


}
