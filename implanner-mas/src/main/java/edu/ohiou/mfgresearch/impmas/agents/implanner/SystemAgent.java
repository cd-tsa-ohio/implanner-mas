package edu.ohiou.mfgresearch.impmas.agents.implanner;

import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.behaviours.FSMBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.tools.logging.LogManagerAgent;
import jade.wrapper.AgentContainer;
import jade.wrapper.StaleProxyException;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyVetoException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import javax.swing.BoxLayout;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

import edu.ohiou.mfgresearch.impmas.agents.Markets;
import edu.ohiou.mfgresearch.impmas.agents.MfgAgent;
import edu.ohiou.mfgresearch.impmas.agents.ServiceClass;
import edu.ohiou.mfgresearch.impmas.agents.dealer.FeatureRecognitionDealerAgent;
import edu.ohiou.mfgresearch.impmas.agents.dealer.ProcessNetworkDealerAgent;
import edu.ohiou.mfgresearch.impmas.agents.dealer.ProcessSelectionDealerAgent;
import edu.ohiou.mfgresearch.impmas.agents.dealer.SystemSimulationDealerAgent;
import edu.ohiou.mfgresearch.impmas.agents.vendor.Coalition;
import edu.ohiou.mfgresearch.impmas.agents.vendor.VendorService;
import edu.ohiou.mfgresearch.impmas.mfgOntology.RegisterIMPlanner;
import edu.ohiou.mfgresearch.impmas.semantics.MfgDF;
import edu.ohiou.mfgresearch.labimp.basis.ViewObject;

public class SystemAgent extends MfgAgent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7119325652734093189L;
	private static AgentContainer ac = null;
	public static JFileChooser fileChooser;
	Map<Long, InstanceTable> instanceList = null;
	static Long activeInstanceID = 0L;
	
	
	/*Markets FeatureRecognition = new Markets("Feature Recognition market", true, false, "192.168.1.3", 1099);
	Markets ProcessPlanning = new Markets("Process Planning market", false, true, null, 0);
	Markets ActivityPlanning = new Markets("Activity Planning market", false, true, null, 0);
	Markets SystemScheduling = new Markets("System Scheduling market", false, true, null, 0);*/

	//List <Markets> marketList= new ArrayList<Markets>();
	//Markets[] marketList=new Markets[]{FeatureRecognition,ProcessPlanning};
	
	//@author Suvo
		
		

	public SystemAgent() throws FIPAException {
		// TODO Auto-generated constructor stub
		super();
		instanceList = new HashMap<Long, InstanceTable>();
	}

	@Override
	protected void setup() {
		// TODO Auto-generated method stub
		super.setup();
		//register in DF
		MfgDF.setSystemAgent(this.getAID());
		//Create a System frame and display
		SystemFrame frame =  new SystemFrame("IMPlanner-MAS");
		JFrame.setDefaultLookAndFeelDecorated(true);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent winEvt) {
				// Perhaps ask user if they want to save any unsaved files first.
				System.exit(0); 
			}
		});
		frame.setSize(new Dimension(1000, 800));
		//Display the window.
		frame.setVisible(true);

		//create markets and launch dealers
//		for(Markets m:Markets.marketAutoList){
//				launchAgent(m);
//		}
		
		//create services by default
//		for(ServiceClass s:ServiceClass.servicesList){
//			launchAgent(s);
//		}
		
		//create vendor serivce agent
//		try {
////			Coalition processCoalition = new Coalition();
////			VendorService service =  new VendorService()
////			launchAgent(ServiceAgent.class, "Process SelectionVendorService", new AbstractService[]{service}, false, "192.168.2.9", 1099);
//			
//		} catch (InstantiationException | IllegalAccessException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
	} 

	@Override
	public void incomingMessageHandler(ACLMessage message) {
		// TODO Auto-generated method stub
		if (message != null) {
			if(message.getPerformative()==ACLMessage.REQUEST){
				try {
					Action a = (Action) manager.extractContent(message);
					RegisterIMPlanner register = (RegisterIMPlanner) a.getAction();
					InstanceTable instance = instanceList.get(register.getInstanceID());
					//register IMPLanner if launch is successful
					if(register.getMessage().equals("0")){
						instance.setImplannerAgent(register.getImpAgent());
						instance.getImplannerFrame().revalidate();
						instance.getImplannerFrame().repaint();
					}
					else{
						//kill internal frame and delete instance
						instance.getImplannerFrame().setVisible(false);
						instanceList.remove(register.getInstanceID());
					}
				} catch (CodecException e ) {
					// TODO Auto-generated catch block
					logger.severe("Following incoming message cannot be understood \n"+message+"\n"+e.getMessage());
				} catch (UngroundedException e) {
					// TODO Auto-generated catch block
					logger.severe("Following incoming message cannot be understood \n"+message+"\n"+e.getMessage());
				} catch (OntologyException e) {
					// TODO Auto-generated catch block
					logger.severe("Following incoming message cannot be understood \n"+message+"\n"+e.getMessage());
				}
			}
		}
	}


	/**
	 * The parent frame of all implnner internal frame
	 * it contains desktop where all implanner frame is added
	 * as internal frame
	 * @author Arko
	 *
	 */
	class SystemFrame extends javax.swing.JFrame {

		/**
		 * 
		 */
		private static final long serialVersionUID = 7147236630410345228L;
		JDesktopPane desktop;
		private int numActiveFrames = 0;
		//title list stores two different titles for two different states
		Map<JComponent, String[]> titleList = new HashMap<JComponent, String[]>();

		public SystemFrame(String title) {
			super(title);
			setJMenuBar(createMenuBar());
			desktop = new JDesktopPane();
			add(desktop);
		}

		protected JMenuBar createMenuBar() {

			JMenuBar menuBar = new JMenuBar();
			menuBar.add(new JMenu("File"){/**
				 * 
				 */
				private static final long serialVersionUID = -8892584747553079724L;

			{
				add(createMenuItem("Open Part File", KeyEvent.VK_P, ActionEvent.CTRL_MASK, 
						new OpenPrtFileListener(), null, "Upload .prt file containing product design", 
						new String[]{"Open Part File", "Change Part File"})); 
				add(createMenuItem("Open Stock File", KeyEvent.VK_S, ActionEvent.CTRL_MASK, 
						new OpenStockFileListener(), null, "Upload .prt file containing Stock Design",
						new String[]{"Open Stock File", "Change Stock File"}));
				addSeparator();
				JMenu subMenu = new JMenu("Export");
				subMenu.add(createMenuItem("XML", -1, ActionEvent.CTRL_MASK, 
						new ExportXMLListener(), null, "Export current status in XML format", null));
				subMenu.add(createMenuItem("CSV", -1, ActionEvent.CTRL_MASK, 
						new ExportCSVListener(), null, "Export current status in CSV format", null));
				add(subMenu);
				add(createMenuItem("Import", KeyEvent.VK_O, ActionEvent.CTRL_MASK, 
						new ImportListener(), null, "Load an existing planner", null));
				addSeparator();
				add(createMenuItem("Properties", -1, ActionEvent.CTRL_MASK, 
						new OpenPropertyListener(), null, "View or Edit settings", null));
				addSeparator();
				add(createMenuItem("Exit", KeyEvent.VK_P, ActionEvent.CTRL_MASK, 
						new ExitListener(), null, null, null));
			}});
			menuBar.add(new JMenu("Operations"){/**
				 * 
				 */
				private static final long serialVersionUID = -4015867223961292020L;

			{
				add(createMenuItem("Feature Recognition", -1, ActionEvent.CTRL_MASK, 
						new OperationSelectionListener(), null, "Perform Feature Recognition (need .prt file)", 
						new String[]{"Feature Recognition"}));
				add(createMenuItem("Process Selection", -1, ActionEvent.CTRL_MASK, 
						new OperationSelectionListener(), null, "Perform Process Selection (need stock file)", 
						new String[]{"Process Selection"}));
				add(createMenuItem("Process Network", -1, ActionEvent.CTRL_MASK, 
						new OperationSelectionListener(), null, "Perform Process Planning Network (select activity first)", 
						new String[]{"Process Network"}));
				add(createMenuItem("Process Simulation", -1, ActionEvent.CTRL_MASK, 
						new OperationSelectionListener(), null, "Perform Process Simlation",
						new String[]{"Process Simulation"}));
				add(createMenuItem("Save in Excel", -1, ActionEvent.CTRL_MASK, 
						new OperationSelectionListener(), null, "Perform Save in Excel",
						new String[]{"Save in Excel"}));
				add(createMenuItem("Save process Excel", -1, ActionEvent.CTRL_MASK, 
						new OperationSelectionListener(), null, "Perform Save process in Excel",
						new String[]{"Save process Excel"}));
			}});
			menuBar.add(new JMenu("Launch Service"){/**
				 * 
				 */
				private static final long serialVersionUID = 2236795956911430298L;

			{
				add(createMenuItem("Create Market", -1, ActionEvent.CTRL_MASK, 
						new CreateMarketListener(), null, "Create Market and Dealer", null));
				add(createMenuItem("Create Service", -1, ActionEvent.CTRL_MASK, 
						new CreateServiceListener(), null, "Create and launch new service agent", null));
				add(createMenuItem("Load Service", -1, ActionEvent.CTRL_MASK, 
						new LoadServiceListener(), null, "Launch existing service agent", null));
				add(createMenuItem("Stop Service", -1, ActionEvent.CTRL_MASK, 
						new StopServiceListener(), null, "Stop operational service agent", null));
			}});
			menuBar.add(new JMenu("Reports"){/**
				 * 
				 */
				private static final long serialVersionUID = 3780454362806593609L;

			{
				add(createMenuItem("Process Graph", -1, ActionEvent.CTRL_MASK, 
						new DisplayReportListener(), "1", "Show Process Precedence Graph", null));
				add(createMenuItem("Activity Graph", -1, ActionEvent.CTRL_MASK, 
						new DisplayReportListener(), "2", "Show Activity Graph", null));
				add(createMenuItem("Agent log", -1, ActionEvent.CTRL_MASK, 
						new LaunchAgentLogListener(), "3", "Show Agent Log", null));
				add(createMenuItem("Error log", -1, ActionEvent.CTRL_MASK, 
						new DisplayReportListener(), "4", "Show Error Log", null));
			}});
			menuBar.add(new JMenu("About"){/**
				 * 
				 */
				private static final long serialVersionUID = 506437681643495890L;

			{
				addActionListener(new ShowAboutListener());
			}});
			return menuBar;
		}

		/**
		 * Creates a new menu item
		 * @param text 
		 * @param vkN 
		 * @param modifier
		 * @param listener
		 * @param actionCommand command to pass in ActionCommand of ActionEvent
		 * @param description
		 * @return
		 */
		private JMenuItem createMenuItem(String text, int vkN, int modifier, ActionListener listener, String actionCommand, String description, String[] titles) {
			// TODO Auto-generated method stub
			JMenuItem menuItem = new JMenuItem(text);
			if(vkN >= 0) 
				menuItem.setAccelerator(KeyStroke.getKeyStroke(
						vkN, modifier));
			menuItem.addActionListener(listener);
			if(actionCommand!=null)
				menuItem.setActionCommand(actionCommand);
			if(description!=null)
				menuItem.getAccessibleContext().setAccessibleDescription(description);
			if(titles!=null){
				if(titles.length==2){
					titleList.put(menuItem, titles);
				}
			}

			return menuItem;
		}

		/**
		 * CheckBox MenuItem
		 * @param text
		 * @param listener
		 * @return
		 */
		private JMenuItem createCheckboxItem(String text, ItemListener listener) {
			// TODO Auto-generated method stub
			JCheckBoxMenuItem menuItem = new JCheckBoxMenuItem(text);
			menuItem.addItemListener(listener);
			return menuItem;
		}


		class OpenPrtFileListener implements ActionListener{
			

			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				try {
					openPrtFile();
				} catch (PropertyVetoException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (UnknownHostException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (StaleProxyException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}

			protected void openPrtFile() throws PropertyVetoException, UnknownHostException, StaleProxyException {
				// TODO Auto-generated method stub
				addNewBehavior(new LaunchIMPlanner());
				/*if (fileChooser == null) {
					fileChooser = new JFileChooser(properties.getProperty("UG_FILE_FOLDER", "."));
					
			    fileChooser.setFileFilter(new UGFileFilter());*/
				
			}
		}

		/**
		 * This behavior launches a new instance of IMPLanner agent 
		 * creates an internal frame with embeded integration panel
		 * @author Arko
		 *
		 */
		public class LaunchIMPlanner extends OneShotBehaviour{

			/**
			 * 
			 */
			private static final long serialVersionUID = -5846040018458920351L;

			@Override
			public void action() {
				// TODO Auto-generated method stub
				//create an instanceID
				Random rand =  new Random();
				SystemAgent.activeInstanceID = rand.nextLong();
				try {
					//Create a new internal frame and add to desktop
					JInternalFrame frame = new JInternalFrame("IMPlanner");
					frame.setSize(new Dimension(800,600));
					frame.setLocation(30*numActiveFrames, 30*numActiveFrames);
					frame.setClosable(true);
					frame.setMaximizable(true);
					frame.setIconifiable(true);
					frame.setVisible(true);
					//frame.addInternalFrameListener(new IMPlannerFrameListener());
					desktop.add(frame);
					numActiveFrames++;
					frame.setSelected(true);
					//save in frame list
					instanceList.put(SystemAgent.activeInstanceID, new InstanceTable(frame)); 
					//launch new IMPlanner agent
					launchAgent(IMPlannerAgent.class, "IMPlanner"+SystemAgent.activeInstanceID, new Object[]{frame, SystemAgent.activeInstanceID}, true, null, 0);
				} catch (PropertyVetoException e) {
					// TODO Auto-generated catch block
					logger.severe("Internal IMPlanner window can not be created \n"+e.getMessage());
				}
			}
		}

		class IMPlannerFrameListener implements InternalFrameListener{

			public void internalFrameOpened(InternalFrameEvent e) {
				// TODO Auto-generated method stub

			}

			public void internalFrameClosing(InternalFrameEvent e) {
				// TODO Auto-generated method stub

			}

			public void internalFrameClosed(InternalFrameEvent e) {
				// TODO Auto-generated method stub

			}

			public void internalFrameIconified(InternalFrameEvent e) {
				// TODO Auto-generated method stub
				numActiveFrames--;
			}

			public void internalFrameDeiconified(InternalFrameEvent e) {
				// TODO Auto-generated method stub
				numActiveFrames++;
			}

			public void internalFrameActivated(InternalFrameEvent e) {
				// TODO Auto-generated method stub
				JInternalFrame implannerFrame = e.getInternalFrame();
				for(Long key:instanceList.keySet()){
					if(instanceList.get(key).getImplannerFrame().equals(implannerFrame)){
						activeInstanceID = key;
						changeTitles(true);
						break;
					}
				}
			}

			public void internalFrameDeactivated(InternalFrameEvent e) {
				// TODO Auto-generated method stub
				JInternalFrame implannerFrame = e.getInternalFrame();
				for(Long key:instanceList.keySet()){
					if(instanceList.get(key).getImplannerFrame().equals(implannerFrame)&&
							activeInstanceID==key){
						activeInstanceID = 0L;
						changeTitles(false);
						break;
					}
				}
			}
		}

		private void changeTitles(boolean someFrameActive){
			if(someFrameActive){
				for(JComponent key:titleList.keySet()){
					JMenuItem item = (JMenuItem) key;
					item.setText(titleList.get(key)[1]);
				}
			}
			else{
				for(JComponent key:titleList.keySet()){
					JMenuItem item = (JMenuItem) key;
					item.setText(titleList.get(key)[0]);
				}
			}
			revalidate();
			repaint();
		}

		class OpenStockFileListener implements ActionListener{

			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub

			}

		}

		class ExportXMLListener implements ActionListener{

			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub

			}

		}

		class ExportCSVListener implements ActionListener{

			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub

			}

		}

		class ImportListener implements ActionListener{

			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub

			}

		}

		class OpenPropertyListener implements ActionListener{

			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String currentDir = ViewObject.getProperties().getProperty("UG_FILE_FOLDER", "");
				String ruleFolder = ViewObject.getProperties().getProperty("JESS_RULE_FOLDER", "");
			}

		}

		class ExitListener implements ActionListener{

			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub

			}

		}

		/**
		 * This behavior send request to IMPlanner Agent to perform an operation
		 * @author sarkara1
		 *
		 */
		class OperationSelectionListener implements ActionListener{
			

			public void actionPerformed(ActionEvent e) {
				
				int operation = 0;
				if (e.getActionCommand().equals("Feature Recognition")){
					operation = 1;
				}
				else if(e.getActionCommand().equals("Process Selection")){
					operation = 2;
				}
				else if(e.getActionCommand().equals("Process Network")){
					operation = 3;
				}
				else if(e.getActionCommand().equals("Process Simulation")){
					operation = 4;
				}
				else if(e.getActionCommand().equals("Save in Excel")){
					operation = 5;
				}
				
				else if(e.getActionCommand().equals("Save process Excel")){
					operation = 6;
				}
					
				ACLMessage message = createMessage(ACLMessage.REQUEST);
				message.addReceiver(instanceList.get(SystemAgent.activeInstanceID).getImplannerAgent()); //get the implanner agent linked to active instanceID
				message.setContent(Integer.toString(operation));
				send(message);
			}

		}

		class CreateMarketListener implements ActionListener{

			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				try {
					createMarket();
				} catch (UnknownHostException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (StaleProxyException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}

		}

		protected void createMarket() throws UnknownHostException, StaleProxyException{

			
			Markets m = (Markets)JOptionPane.showInputDialog(
					this,
					"Choose the market type",
					"Create Market",
					JOptionPane.PLAIN_MESSAGE,
					null,
					Markets.marketList.toArray(),
					(Object)Markets.marketList.get(0));
			if(m!=null){
				launchAgent(m);
				Markets.marketList.remove(m);
			}


		}

		class CreateServiceListener implements ActionListener{

			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				displayServiceManagementDialog();
			}

		}

		protected void displayServiceManagementDialog() {
			// TODO Auto-generated method stub
			ServiceClass s = (ServiceClass)JOptionPane.showInputDialog(
					this,
					"Choose Service",
					"Create Service",
					JOptionPane.PLAIN_MESSAGE,
					null,
					ServiceClass.servicesList.toArray(),
					ServiceClass.servicesList.get(0));
			if(s!=null){
				
					//AbstractService service = (AbstractService) s.getServiceClass().newInstance();
					//launchAgent(ServiceAgent.class, s.toString()+"Service", new AbstractService[]{service}, true, null, 0);
					launchAgent(s);
					
				}
			}
		

		class LoadServiceListener implements ActionListener{

			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub

			}

		}

		class StopServiceListener implements ActionListener{

			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub

			}

		}

		class DisplayReportListener implements ActionListener{


			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub

			}

		}

		class LaunchAgentLogListener implements ActionListener{

			public void actionPerformed(ActionEvent e) {
				launchAgent(LogManagerAgent.class, "Agent Log", null, true, null, 0);
			}

		}

		class ShowAboutListener implements ActionListener{


			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub

			}

		}

	}

	class InstanceTable{
		private JInternalFrame implannerFrame;
		private AID implannerAgent;
		public InstanceTable() {
			super();
		}
		public InstanceTable(JInternalFrame implannerFrame) {
			super();
			this.implannerFrame = implannerFrame;
		}
		public JInternalFrame getImplannerFrame() {
			return implannerFrame;
		}
		public void setImplannerFrame(JInternalFrame implannerFrame) {
			this.implannerFrame = implannerFrame;
		}
		public AID getImplannerAgent() {
			return implannerAgent;
		}
		public void setImplannerAgent(AID aid) {
			this.implannerAgent = aid;
		}
	}

	@Override
	public FSMBehaviour getFSM() {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public ServiceDescription[] getServices() {
		// TODO Auto-generated method stub
		return null;
	}

	public class ServiceManager extends JDialog{

		private static final long serialVersionUID = 2700258771917912760L;
		private ServiceManager(Frame owner, String title, boolean modal, int type) {
			super(owner, title, modal);
			JPanel root = new JPanel();
			root.setLayout(new BoxLayout(root, BoxLayout.Y_AXIS));
			switch (type) {
			case 1:
				root.add(getCreateServicePanel());
				break;
			case 2:
				root.add(getLoadServicePanel());
				break;
			case 3:
				root.add(getStopServicePanel());
				break;
			default:
				break;
			}
		}

		private Component getCreateServicePanel() {
			JPanel mainPanel = new JPanel(new BorderLayout());
//			JComboBox services = new JComboBox<>(ServiceClass.values());
//			mainPanel.add(services, BorderLayout.CENTER);
			//			mainPanel.add(new JButton("Create Service"))
			return mainPanel;
		}

		private Component getStopServicePanel() {
			// TODO Auto-generated method stub
			return null;
		}

		private Component getLoadServicePanel() {
			// TODO Auto-generated method stub
			return null;
		}

	}

}
