package edu.ohiou.mfgresearch.impmas.agents.implanner;



import edu.ohiou.mfgresearch.implanner.MfgConcept;
import edu.ohiou.mfgresearch.implanner.cam.UGFileFilter;
import edu.ohiou.mfgresearch.implanner.features.Hole;
import edu.ohiou.mfgresearch.implanner.features.MfgFeature;
import edu.ohiou.mfgresearch.implanner.features.Pocket;
import edu.ohiou.mfgresearch.implanner.features.Slot;
import edu.ohiou.mfgresearch.implanner.geometry.PartModel;
import edu.ohiou.mfgresearch.implanner.geometry.Stock;
import edu.ohiou.mfgresearch.implanner.network.MachineSequenceObject;
import edu.ohiou.mfgresearch.implanner.parts.MfgPartModel;
import edu.ohiou.mfgresearch.implanner.processes.MfgProcess;
import edu.ohiou.mfgresearch.implanner.resources.Machine;
import edu.ohiou.mfgresearch.impmas.agents.IMPlannerProperties;
import edu.ohiou.mfgresearch.impmas.agents.IMPlannerProperties.AgentType;
import edu.ohiou.mfgresearch.impmas.agents.IMPlannerProperties.IMPlannerStatus;
import edu.ohiou.mfgresearch.impmas.agents.MfgAgent;
import edu.ohiou.mfgresearch.impmas.gui.IMPlannerMAS_launcher;
import edu.ohiou.mfgresearch.impmas.mfgOntology.HaveVendor;
import edu.ohiou.mfgresearch.impmas.mfgOntology.RegisterIMPlanner;
import edu.ohiou.mfgresearch.impmas.semantics.BidValue;
import edu.ohiou.mfgresearch.impmas.semantics.FactFile;
import edu.ohiou.mfgresearch.impmas.semantics.ManagementData;
import edu.ohiou.mfgresearch.impmas.semantics.MfgDF;
import edu.ohiou.mfgresearch.impmas.semantics.PartFile;
import edu.ohiou.mfgresearch.impmas.semantics.ProcessFile;
import edu.ohiou.mfgresearch.impmas.semantics.ResultIs;
import edu.ohiou.mfgresearch.impmas.semantics.SequenceFile;
import edu.ohiou.mfgresearch.impmas.semantics.ServiceAction;
import edu.ohiou.mfgresearch.impmas.semantics.SimulationFile;
import edu.ohiou.mfgresearch.labimp.basis.ViewObject;
import edu.ohiou.mfgresearch.labimp.graphmodel.DefaultGraphModel;
import edu.ohiou.mfgresearch.labimp.table.SquareTableModel;
import edu.ohiou.mfgresearch.labimp.table.TableCellGenerator;
import jade.content.ContentElement;
import jade.content.abs.AbsIRE;
import jade.content.abs.AbsPredicate;
import jade.content.abs.AbsTerm;
import jade.content.abs.AbsVariable;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLVocabulary;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Done;
import jade.core.Agent;
import jade.core.behaviours.FSMBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.util.leap.Iterator;

import java.awt.BorderLayout;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Vector;

import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * This is the core agent to be launched in the main container
 * IMPlanner is responsible for the front end handling of 
 * Manufacturing objects
 * @author Arko
 *
 */
public class IMPlannerAgent extends MfgAgent {


	private static final long serialVersionUID = 5089468572782686898L;

	private JInternalFrame internalFrame = null;
	private long instanceID;
	private IMPlannerMAS_launcher implanner = null;
	private StatusRegister statusRegister = new StatusRegister();
	private File partFile;

	public MfgPartModel partModel;
	public static Map <String, SequenceFile> partSequenceMap = new HashMap<String,SequenceFile>();
	public static Map <String, MfgPartModel> partModelMap = new HashMap<String ,MfgPartModel>();
	



	int exitValueStateA = 0;

	public IMPlannerAgent() throws FIPAException {
		super();
		// TODO Auto-generated constructor stub
	}

	protected void setup() {
		super.setup();		
		//MfgDF.re;
		//handle arguments
		Object[] args = getArguments();
		internalFrame = (JInternalFrame) args[0];
		instanceID = (Long) args[1];

	}

	@Override
	public FSMBehaviour getFSM() {
		// TODO Auto-generated method stub
		FSMBehaviour fsm  = new FSMBehaviour(this);
		fsm.registerLastState(new NotifySystemBehavior(this), "C");
		fsm.registerFirstState(new OpenFileBehavior(), "A");
		fsm.registerState(new LaunchIMPlannerBehavior(), "B");
		
		//fsm.registerLastState(new FeatureRecognitionBehavior(), "D");  //added for test //should be decided from other state
		fsm.registerTransition("A", "B", 0);
		fsm.registerTransition("A", "C", 1);
		fsm.registerTransition("B", "C",0);
		return fsm;
	}
	
	
	public void incomingInformMessageHandler(ACLMessage message){

		ContentElement ce;
		try {
			logger.info("Following message is recieved from DealerAgent \n"+message);
			ce = manager.extractContent(message);
			if(ce instanceof ResultIs){
				ResultIs result = (ResultIs) ce;
				ManagementData managementData = result.getManagementData();
				if(managementData.getRequestID().equals(IMPlannerStatus.FR)){
					MfgPartModel parModel = (MfgPartModel) result.getConcept();
					if(partModel.isLastPacket()){
						statusRegister.statusLog.put(managementData, IMPlannerStatus.FR_DONE);
					}
				}
				//other results for other types of requests will be handled in future
			}
			else if (ce instanceof Done){
				Done d = (Done) ce;
				Action a = (Action) d.getAction();
				ServiceAction s = (ServiceAction)(a.getAction());

				MfgConcept mfgConcept = s.getOutputObject();
				partModel = (MfgPartModel) mfgConcept;
			}
		}
		catch (Exception e) {
			// TODO: handle exception
		}
	
	}
	
	public void incomingConfirmMessageHandler(ACLMessage message){

		ContentElement ce;
		try {
			logger.info("Following message is recieved from DealerAgent \n"+message);
			ce = manager.extractContent(message);
			
			if(ce instanceof ResultIs){
				ResultIs result = (ResultIs) ce;
				ManagementData managementData = result.getManagementData();
				if(managementData.getRequestID().equals(IMPlannerStatus.FR)){
					//got result for Feature Recognition
					MfgPartModel parModel = (MfgPartModel) result.getConcept();
					//Feature Recognition is sending a single MfgPartModel object 
					//which has all features 
					//no broken message maintenance is needed
					if(partModel.isLastPacket()){
						//how to update Features in the IMPlanner?? code here in this block
						//Update status 
						statusRegister.statusLog.put(managementData, IMPlannerStatus.FR_DONE);
					}
				}
				//other results for other types of requests will be handled in future
			}
			else if (ce instanceof Done){
				Done d = (Done) ce;

//				ServiceAction s = (ServiceAction)d.getAction();

				
				Action a = (Action) d.getAction();
				ServiceAction s = (ServiceAction)(a.getAction());

				MfgConcept mfgConcept = s.getOutputObject();
				if(mfgConcept instanceof MfgPartModel){
					partModel = (MfgPartModel) mfgConcept;
				}else if (mfgConcept instanceof ProcessFile){
					ProcessFile processFile = (ProcessFile) mfgConcept;
					List<MfgProcess> processList = processFile.getProcessList();
					for(ListIterator<MfgFeature> itr_feat = partModel.getFeatureList().listIterator(); itr_feat.hasNext();){
						MfgFeature feature = itr_feat.next();
						for(ListIterator<MfgProcess> itr_proc = processList.listIterator(); itr_proc.hasNext();){
							MfgProcess process = itr_proc.next();
							if(process.getFeatureName().equals(feature.getFeatureName())){
								feature.addProcess(process);
							}
						}
					}
					
					IMPlannerAgent.partModelMap.put(this.partModel.getPartName(), this.partModel);
					
					System.out.println("");
				}else if (mfgConcept instanceof SequenceFile){
					SequenceFile sequenceFile = (SequenceFile) mfgConcept;
					for(ListIterator<MachineSequenceObject> itr = sequenceFile.getProcessList().listIterator(); itr.hasNext();){
						MachineSequenceObject machineSequence = itr.next();
						String machineName = machineSequence.getMachineName();
						double newTotalProcessTime = Machine.processTimeMap.get(machineName)+ machineSequence.getMachineTime();
						Machine.processTimeMap.put(machineName, newTotalProcessTime);
						double priorityValue = Machine.priorityList.get(machineName);
						double priorityIndex = priorityValue + Machine.processTimeMap.get(machineName)*(0) ;
						Machine.priorityList.put(machineName, priorityIndex);
					}
					partSequenceMap.put(this.partModel.getPartName(), sequenceFile);
					System.out.println("Process Sequence Obtained from Process Network Service");
					for(Map.Entry entry : Machine.priorityList.entrySet()){
						System.out.println(entry.getKey() + "" + entry.getValue());
					}
				}
				
				implanner.updateUI(this.partModel);
				
				
			}
		}
		catch (Exception e) {
			// TODO: handle exception
		}
	
	}
	
	


	public void incomingRequestMessageHandler(ACLMessage message) {

		try {
			
			logger.info("Following message is recieved from SystemAgent \n"+message);
			int event = Integer.parseInt(message.getContent());
			switch (event) {
			case 1:
				addBehaviour(new FeatureRecognitionBehavior());
				break;
			case 2:
				addBehaviour(new ProcessSelectionBehavior(this));
				break;
			case 3:
				addBehaviour(new ProcessNetworkBehavior());
				break;
			case 4:
				addBehaviour(new ProcessSimulationBehavior());
				break;
			case 5:
				addBehaviour(new SavetoExcelBehavior());
				break;
			case 6:
				addBehaviour(new SaveProcesstoExcelBehavior());
				break;
			default:
				break;
			}
		}
		catch (Exception e) {
			// TODO: handle exception
		}
	
	}
	@Override
	public void incomingMessageHandler(ACLMessage message) {
		// TODO Auto-generated method stub
		if(message!=null){
			if(message.getPerformative() == ACLMessage.INFORM){ this.incomingInformMessageHandler(message);}
			if(message.getPerformative() == ACLMessage.CONFIRM){this.incomingConfirmMessageHandler(message);}
			if(message.getPerformative() == ACLMessage.REQUEST){this.incomingRequestMessageHandler(message);}
		}
		
	}

	/**
	 * Behavior state A 
	 * try to open file; if successful return exitvalue 0 otherwise 1
	 * @author sarkara1
	 *
	 */
	public class OpenFileBehavior extends OneShotBehaviour {

		int exitValue = 0;

		@Override
		public void action() {
			MfgPartModel.configureFileChooser();
			if (MfgPartModel.fileChooser.showOpenDialog(internalFrame) == JFileChooser.APPROVE_OPTION) {
				partFile = MfgPartModel.fileChooser.getSelectedFile();

			}
			else{
				exitValue = 1;
			}
		}

		@Override
		public int onEnd() {
			// TODO Auto-generated method stub
			exitValueStateA = exitValue;
			return exitValue;
		}
	}
	public class SaveProcesstoExcelBehavior extends OneShotBehaviour {

		@Override
		public void action() {
			
			this.addFeatureListToExcel(IMPlannerAgent.partModelMap);
		}
		
		private LinkedList canHaveCurrFeat(LinkedList compFeat,LinkedList featList){
	        LinkedList result = new LinkedList();
	        for(ListIterator itr = featList.listIterator();itr.hasNext();){
	          MfgFeature feature = (MfgFeature)itr.next();
	          if( !compFeat.contains(feature) && compFeat.containsAll(feature.gettPrevious())){
	            result.add(feature);
	          }
	        }
		 return result;
		}
		
		private void addFeatureListToExcel(Map<String, MfgPartModel> partModelMap) {
			// TODO Auto-generated method stub

			Workbook wb = new XSSFWorkbook();
	       
	        
	        Set<Entry<String,MfgPartModel>> entrySet = partModelMap.entrySet();
	        int t =0;
	       
	        for(Map.Entry<String,MfgPartModel> entry: entrySet){
	        	Sheet sheet = wb.createSheet("Feaute Based PPN sheet " + (t++));
	 	        int rowNumber = 0;
	 	        int columnNumber = 0;
	        	
	        	MfgPartModel partModel = entry.getValue();
	        	
	        	List<MfgFeature> featureList = partModel.getFeatureList();
		        Row namerow = sheet.createRow(rowNumber);
		        Cell namecell = namerow.createCell(columnNumber);
		        namecell.setCellValue((String)partModel.getPartName());
		        LinkedList <MfgFeature> completedFeatures = new LinkedList<MfgFeature>();
		        
		        for(int i = 0; i< featureList.size();i++){
		        	MfgFeature currFeat = ((MfgFeature)featureList.get(i));
					
					if(currFeat.gettPrevious().isEmpty() ){
						completedFeatures.add(currFeat);
						Row featureNamerow = sheet.createRow(++rowNumber);
			            Cell featureNamecell = featureNamerow.createCell(columnNumber);
			            featureNamecell.setCellValue(currFeat.getFeatureName());
			            LinkedList<MfgProcess> processList = new LinkedList<MfgProcess>();
		 	            processList.addAll(currFeat.getFeatureProcesses());
			            LinkedList<MfgProcess> approvedProcessList = new LinkedList<MfgProcess>();
			            while(!processList.isEmpty()){
			            	String machineName = ((MfgProcess)processList.get(0)).getMachineName();
			            	MfgProcess approvedProcess = (MfgProcess)processList.get(0);
			            	LinkedList<MfgProcess> newProcessList = new LinkedList<MfgProcess>();
			            	newProcessList.add(approvedProcess);
			            	for(int k = 1; k< processList.size();k++){
			            		if(machineName.equalsIgnoreCase(((MfgProcess)processList.get(k)).getMachineName())){
			            			newProcessList.add((MfgProcess)processList.get(k));
			            		}
			            		
			            	}
			            	
			            	for(int k = 0; k< newProcessList.size();k++){
			            		processList.remove((MfgProcess)newProcessList.get(k));
			            		if(approvedProcess.getProcessTime()> ((MfgProcess)newProcessList.get(k)).getProcessTime()){
			            			approvedProcess = (MfgProcess)newProcessList.get(k);
			            		}
			            	}
			            	approvedProcessList.add(approvedProcess);
			            	
			            }
			            for(int j = 0; j< approvedProcessList.size();j++){
			            	Row processNamerow = sheet.createRow(++rowNumber);
			                Cell processNamecell = processNamerow.createCell(columnNumber);
			                processNamecell.setCellValue(((MfgProcess)approvedProcessList.get(j)).getMachineName());
			                Cell processTimecell = processNamerow.createCell(columnNumber + 1);
			                processTimecell.setCellValue(((MfgProcess)approvedProcessList.get(j)).getProcessTime());
			            }
					}
		        	
		        }
		        
		        while(partModel.getFeatureList().size()!= completedFeatures.size()){
		        	 LinkedList<MfgFeature> currentFeature = this.canHaveCurrFeat(completedFeatures, partModel.getFeatureList());
		             for(int i = 0; i< currentFeature.size();i++){
		             	MfgFeature currFeat = ((MfgFeature)currentFeature.get(i));
		     			
		     				completedFeatures.add(currFeat);
		     				Row featureNamerow = sheet.createRow(++rowNumber);
		     	            Cell featureNamecell = featureNamerow.createCell(columnNumber);
		     	            featureNamecell.setCellValue(currFeat.getFeatureName());
		     	            LinkedList<MfgProcess> processList = new LinkedList<MfgProcess>();
		     	            processList.addAll(currFeat.getFeatureProcesses());
		     	           LinkedList<MfgProcess> approvedProcessList = new LinkedList<MfgProcess>();
		   	            while(!processList.isEmpty()){
			            	String machineName = ((MfgProcess)processList.get(0)).getMachineName();
			            	MfgProcess approvedProcess = (MfgProcess)processList.get(0);
			            	LinkedList<MfgProcess> newProcessList = new LinkedList<MfgProcess>();
			            	newProcessList.add(approvedProcess);
			            	for(int k = 1; k< processList.size();k++){
			            		if(machineName.equalsIgnoreCase(((MfgProcess)processList.get(k)).getMachineName())){
			            			newProcessList.add((MfgProcess)processList.get(k));
			            		}
			            		
			            	}
			            	
			            	for(int k = 0; k< newProcessList.size();k++){
			            		processList.remove((MfgProcess)newProcessList.get(k));
			            		if(approvedProcess.getProcessTime()> ((MfgProcess)newProcessList.get(k)).getProcessTime()){
			            			approvedProcess = (MfgProcess)newProcessList.get(k);
			            		}
			            	}
			            	approvedProcessList.add(approvedProcess);
			            	
			            }
		     	            for(int j = 0; j< approvedProcessList.size();j++){
		     	            	Row processNamerow = sheet.createRow(++rowNumber);
		     	                Cell processNamecell = processNamerow.createCell(columnNumber);
		     	                processNamecell.setCellValue(((MfgProcess)approvedProcessList.get(j)).getMachineName());
		     	                Cell processTimecell = processNamerow.createCell(columnNumber + 1);
		     	                processTimecell.setCellValue(((MfgProcess)approvedProcessList.get(j)).getProcessTime());
		     	            }
		             	
		             }
		        }
		        
		        rowNumber++;
	        }
	        try {
	            FileOutputStream out = 
	                    new FileOutputStream(new File("C:/Test/featureBased.xlsx"));
	            wb.write(out);
	            out.close();
	            System.out.println("Excel written successfully..");
	            
	        } catch (FileNotFoundException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
			// TODO Auto-generated method stub
	        
	        
			
		}
		
	}
	public class SavetoExcelBehavior extends OneShotBehaviour {

		@Override
		public void action() {
			Workbook wb = new XSSFWorkbook();
	        Sheet sheet = wb.createSheet("Sample sheet");
	         
	        
	        Set<String> keyset = IMPlannerAgent.partSequenceMap.keySet();
	        int i=0;
	        LinkedList <Row> rowList = new LinkedList<Row> ();
	        Row namerow = sheet.createRow(0);
	        for (String key : keyset) {
	            
	            SequenceFile sequenceFile = IMPlannerAgent.partSequenceMap.get(key);
	            LinkedList <MachineSequenceObject> sequenceList = sequenceFile.getProcessList();
	            
	            Cell namecell = namerow.createCell(i);
	            namecell.setCellValue((String)key.toString());
	           
	            int rownum = 1;
            	int cellnum = i;
            	
	            for (int j =0; j<sequenceList.size();j++) {
	            	Row row = null;
	            	if(rowList.size()<j+1){
	            	    row = sheet.createRow(rownum++);
	            		rowList.add(row);
	            	}else{
	            		row = rowList.get(j);
	            		rownum++;
	            	}
	            	
	                Cell cell1 = row.createCell(i);
	                Cell cell2 = row.createCell(i+1);
	                Object obj = sequenceList.get(j);
	                if(obj instanceof MachineSequenceObject) 
	                    cell1.setCellValue(((MachineSequenceObject)obj).getMachineName());
	                	cell2.setCellValue(((MachineSequenceObject)obj).getMachineTime());
	               
	            }
	            
	            i=i+2;
	        }
	         
	        try {
	            FileOutputStream out = 
	                    new FileOutputStream(new File("C:/Test/new.xlsx"));
	            wb.write(out);
	            out.close();
	            System.out.println("Excel written successfully..");
	            
	        } catch (FileNotFoundException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
			// TODO Auto-generated method stub
		}
		
	}

	/**
	 * Behavior state B 
	 * launch internal frame for IMplanner 
	 * this only fires when State A exitvalue is 0
	 * @author sarkara1
	 *
	 */
	public class LaunchIMPlannerBehavior extends OneShotBehaviour {

		int exitValue = 0;

		@Override
		public void action() {
			//Create a new internal frame and add to desktop
			internalFrame.setLayout(new BorderLayout());
			internalFrame.setTitle("IMPlanner:"+partFile.getName());
			implanner = new IMPlannerMAS_launcher();
			//JPanel panel = implanner;
			internalFrame.add(implanner, BorderLayout.CENTER);
			internalFrame.revalidate();
			internalFrame.repaint();
		}

		@Override
		public int onEnd() {
			// TODO Auto-generated method stub
			return exitValue;
		}
	}

	/**
	 * Behavior state C
	 * One shot behavior to notify System agent after successful 
	 * launch of IMPlanner agent with exitvalue 0
	 * otherwise notify system of the deletion of agent. 
	 * @author sarkara1
	 *
	 */
	public class NotifySystemBehavior extends OneShotBehaviour {

		private static final long serialVersionUID = -5211335712129473875L;
		Agent agent = null;

		public NotifySystemBehavior(Agent a) {
			super(a);
			this.agent = a;
		}

		int exitValue = 0;

		@Override
		public void action() {
			//Inform SystemAgent about itself
			ACLMessage message = createMessage(ACLMessage.REQUEST);
			message.addReceiver(MfgDF.getSystemAgent()); //need to know System Agent
			Integer exitValueA = exitValueStateA;
			RegisterIMPlanner register = new RegisterIMPlanner(instanceID, getAID(), exitValueA.toString());
			try {
				Action a = new Action(getAID(), register);
				manager.fillContent(message, a);
				send(message);
			} catch (CodecException | OntologyException e) {
				// TODO Auto-generated catch block
				logger.severe(e.getMessage());
			}
			if(exitValueStateA==1){
				myAgent.doDelete();
			}
		}

		@Override
		public int onEnd() {
			// TODO Auto-generated method stub
			return exitValue;
		}
	}
	/**
	 * This behavior is one of the operational behavior triggeered by SystemAgent sending request to this agent
	 * @author sarkara1
	 *
	 */
	public class ProcessNetworkBehavior extends OneShotBehaviour {

		ManagementData managementData;
		private JFileChooser fileChooser;
		private String currentDir = null;
		private String ruleFolder =null;
		private MfgPartModel mfgPartModel ;
		@Override
		public void action() {
			mfgPartModel =  partModel;

			System.out.println("Inside Process Network Behavior");
				
				try {
					DFAgentDescription[] dealers = MfgDF.searchByService(myAgent, "Process Network Dealer", IMPlannerProperties.AgentType.Market.toString(), 1);
					//for testing only one dealer is chosen, in future we need to take care of all dealers
					if(dealers!=null){
						for(DFAgentDescription ad:dealers){
							ACLMessage message = createMessage(ACLMessage.QUERY_REF);
						try{
							AbsVariable x = new AbsVariable("x", PartModel.NAME); 
							//Create a management data of the design before passing it as term
							AbsPredicate haveVendor = new AbsPredicate(HaveVendor.NAME); //Predicate should be read from properties of service description in the future
							managementData = createManagementData();
							Long conversationID = new Long(rand.nextLong());
							managementData.setRequestID(statusRegister.status);
							haveVendor.set("managementData", (AbsTerm) ontology.fromObject(createManagementData()));
							haveVendor.set("openingBid", (AbsTerm)ontology.fromObject(new BidValue(500.00)));
							haveVendor.set("expiryDate", (AbsTerm)ontology.fromObject(Long.MAX_VALUE));
							
							
							AbsTerm term = (AbsTerm) ontology.fromObject(mfgPartModel);
							haveVendor.set("input", term);
							haveVendor.set("result", x);

							AbsIRE iota = new AbsIRE(SLVocabulary.IOTA);
							iota.setVariable(x);
							iota.setProposition(haveVendor);
							message.addReceiver(ad.getName());
							manager.fillContent(message, iota);
							send(message);
							
						}catch (CodecException e) {
							logger.severe(e.toString()+"Query to Dealer "+ ad.getName()+" cannot be created \n"+e.getMessage());
						}catch(OntologyException e){
							logger.severe(e.toString()+"Query to Dealer "+ ad.getName()+" cannot be created \n"+e.getMessage());
						}finally{
							//log status register
							statusRegister.setStatus(IMPlannerStatus.FR);;
						}
						}
					}
				}catch (FIPAException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				
			
		}
		
	}
	/**
	 * This behavior is one of the operational behavior triggeered by SystemAgent sending request to this agent
	 * @author ghosal
	 *
	 */
	public class ProcessSimulationBehavior extends OneShotBehaviour {

		ManagementData managementData;

		@Override
		public void action() {

			logger.info("in FeatureRecognitionBehavior.action" );
			// TODO Auto-generated method stub
			try {
				DFAgentDescription[] dealers = MfgDF.searchByService(myAgent, "systemSimulationDealerAgent", IMPlannerProperties.AgentType.Market.toString(), 1);
				//for testing only one dealer is chosen, in future we need to take care of all dealers
				if(dealers!=null){
					for(DFAgentDescription ad:dealers){
						ACLMessage message = createMessage(ACLMessage.QUERY_REF);
						try {
							AbsVariable x = new AbsVariable("x", PartModel.NAME); 
							//Create a management data of the design before passing it as term
							AbsPredicate haveVendor = new AbsPredicate(HaveVendor.NAME); //Predicate should be read from properties of service description in the future
							managementData = createManagementData();
							Long conversationID = new Long(rand.nextLong());
							managementData.setRequestID(statusRegister.status);
							haveVendor.set("managementData", (AbsTerm) ontology.fromObject(createManagementData()));
							haveVendor.set("openingBid", (AbsTerm)ontology.fromObject(new BidValue(500.00)));
							haveVendor.set("expiryDate", (AbsTerm)ontology.fromObject(Long.MAX_VALUE));
							LinkedList<SequenceFile> partSequences = new LinkedList<SequenceFile>();
							String[] partNames = new String[partSequenceMap.size()];
							int i=0;
							for(Map.Entry<String, SequenceFile> entry : partSequenceMap.entrySet()){
								
								
								partNames[i] = (String)entry.getKey();
								partSequences.add((SequenceFile)entry.getValue());
								i++;
							}
							SimulationFile mySimulationFile = new SimulationFile(partNames, partSequences);
							mySimulationFile.setPacketNumber(1);
							mySimulationFile.setLastPacket(true);
							AbsTerm term = (AbsTerm) ontology.fromObject(mySimulationFile);
							haveVendor.set("input", term);
							haveVendor.set("result", x);

							AbsIRE iota = new AbsIRE(SLVocabulary.IOTA);
							iota.setVariable(x);
							iota.setProposition(haveVendor);
							message.addReceiver(ad.getName());
							manager.fillContent(message, iota);
							send(message);
						}catch (CodecException e) {
							logger.severe(e.toString()+"Query to Dealer "+ ad.getName()+" cannot be created \n"+e.getMessage());
						}catch(OntologyException e){
							logger.severe(e.toString()+"Query to Dealer "+ ad.getName()+" cannot be created \n"+e.getMessage());
						}finally{
							//log status register
							statusRegister.setStatus(IMPlannerStatus.FR);;
						}

						logger.info("Following message is sent to Dealer "+ad.getName()+"\n"+message);
					}
				}
				else{
					logger.warning("No market is open for feature recognition");
				}
			} catch (FIPAException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}
	/**
	 * This behavior is one of the operational behavior triggeered by SystemAgent sending request to this agent
	 * @author sarkara1
	 *
	 */
	public class FeatureRecognitionBehavior extends OneShotBehaviour {

		ManagementData managementData;

		@Override
		public void action() {

			logger.info("in FeatureRecognitionBehavior.action" );
			// TODO Auto-generated method stub
			try {
				DFAgentDescription[] dealers = MfgDF.searchByService(myAgent, "featureRecognitionDealerAgent", IMPlannerProperties.AgentType.Market.toString(), 1);
				//for testing only one dealer is chosen, in future we need to take care of all dealers
				if(dealers!=null){
					for(DFAgentDescription ad:dealers){
						ACLMessage message = createMessage(ACLMessage.QUERY_REF);
						try {
							AbsVariable x = new AbsVariable("x", PartModel.NAME); 
							//Create a management data of the design before passing it as term
							AbsPredicate haveVendor = new AbsPredicate(HaveVendor.NAME); //Predicate should be read from properties of service description in the future
							managementData = createManagementData();
							Long conversationID = new Long(rand.nextLong());
							managementData.setRequestID(statusRegister.status);
							haveVendor.set("managementData", (AbsTerm) ontology.fromObject(createManagementData()));
							haveVendor.set("openingBid", (AbsTerm)ontology.fromObject(new BidValue(500.00)));
							haveVendor.set("expiryDate", (AbsTerm)ontology.fromObject(Long.MAX_VALUE));
							String bstream = getByteStream(new FileInputStream(partFile));
							//System.out.println(partFile.getName());
							logger.info("bstream length: " + bstream.length());
							PartFile mypartFile = new PartFile(bstream);
							mypartFile.setPacketNumber(1);
							mypartFile.setLastPacket(true);
							mypartFile.setName(partFile.getName());
							AbsTerm term = (AbsTerm) ontology.fromObject(mypartFile);
							haveVendor.set("input", term);
							haveVendor.set("result", x);

							AbsIRE iota = new AbsIRE(SLVocabulary.IOTA);
							iota.setVariable(x);
							iota.setProposition(haveVendor);
							message.addReceiver(ad.getName());
							manager.fillContent(message, iota);
							send(message);
						}catch (CodecException e) {
							logger.severe(e.toString()+"Query to Dealer "+ ad.getName()+" cannot be created \n"+e.getMessage());
						}catch(OntologyException e){
							logger.severe(e.toString()+"Query to Dealer "+ ad.getName()+" cannot be created \n"+e.getMessage());
						}catch( IOException e){
							logger.severe(e.toString()+"Query to Dealer "+ ad.getName()+" cannot be created \n"+e.getMessage());
						}finally{
							//log status register
							statusRegister.setStatus(IMPlannerStatus.FR);;
						}

						logger.info("Following message is sent to Dealer "+ad.getName()+"\n"+message);
					}
				}
				else{
					logger.warning("No market is open for feature recognition");
				}
			} catch (FIPAException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}
	
	/**
	 * This behavior is one of the operational behavior triggered by SystemAgent sending request to this agent
	 * @author sarkara1
	 *
	 */
	public class ProcessSelectionBehavior extends OneShotBehaviour {
		IMPlannerAgent myImplannerAgent;
		ManagementData managementData;
		
		public ProcessSelectionBehavior(IMPlannerAgent imPlannerAgent){
			System.out.println(imPlannerAgent);
			this.myImplannerAgent = imPlannerAgent;
		}

		private JFileChooser fileChooser;
		private String currentDir = null;
		private String ruleFolder =null;
		private MfgPartModel mfgPartModel ;
		@Override
		public void action() {
			mfgPartModel =  partModel;

			System.out.println("Inside Process Selection Behavior");
			fileChooser = new JFileChooser(new File("."));
			currentDir = ViewObject.getProperties().getProperty("UG_FILE_FOLDER", "");
			//ruleFolder = ViewObject.getProperties().getProperty("JESS_RULE_FOLDER", "");
			fileChooser.setFileFilter(new UGFileFilter());
			fileChooser.setCurrentDirectory(new File(currentDir));
			
			if (fileChooser.showOpenDialog(internalFrame) == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				mfgPartModel.setStock(new Stock(file));
				mfgPartModel.generateFeaturePrecedence();
				SquareTableModel precedenceModel;
				precedenceModel = new SquareTableModel(mfgPartModel.getFeatureList().toArray(), new MfgFeaturePrecedenceGenerator());

				
				DefaultGraphModel precedenceGraph = new DefaultGraphModel(precedenceModel,true,true);
				precedenceGraph.display();
				StringBuffer buffer = new StringBuffer();
				buffer.append(makePartFactString(mfgPartModel));

				for (ListIterator itr = mfgPartModel.getFeatureList().listIterator();itr.hasNext();) {
					MfgFeature feat = (MfgFeature) itr.next();
					
					if(feat instanceof Hole){
						buffer.append(((Hole)feat).makeFactString());
					}
					if(feat instanceof Slot){
						buffer.append(((Slot)feat).makeFactString());
					}
					if(feat instanceof Pocket){
						buffer.append(((Pocket)feat).makeFactString());
					}
				}
				System.out.println("facts created /n" + buffer.toString());
				
				try {
					DFAgentDescription[] dealers = MfgDF.searchByService(myAgent, "Process Selection Dealer", IMPlannerProperties.AgentType.Market.toString(), 1);
					//for testing only one dealer is chosen, in future we need to take care of all dealers
					if(dealers!=null){
						for(DFAgentDescription ad:dealers){
							ACLMessage message = createMessage(ACLMessage.QUERY_REF);
						try{
							AbsVariable x = new AbsVariable("x", PartModel.NAME); 
							//Create a management data of the design before passing it as term
							AbsPredicate haveVendor = new AbsPredicate(HaveVendor.NAME); //Predicate should be read from properties of service description in the future
							managementData = createManagementData();
							Long conversationID = new Long(rand.nextLong());
							managementData.setRequestID(statusRegister.status);
							haveVendor.set("managementData", (AbsTerm) ontology.fromObject(createManagementData()));
							haveVendor.set("openingBid", (AbsTerm)ontology.fromObject(new BidValue(500.00)));
							haveVendor.set("expiryDate", (AbsTerm)ontology.fromObject(Long.MAX_VALUE));
							FactFile myfactFile = new FactFile(buffer.toString());
							myfactFile.setPacketNumber(1);
							myfactFile.setLastPacket(true);
							
							System.out.println(">> Creation of fact file completed.");
							AbsTerm term = (AbsTerm) ontology.fromObject(myfactFile);
							haveVendor.set("input", term);
							haveVendor.set("result", x);

							AbsIRE iota = new AbsIRE(SLVocabulary.IOTA);
							iota.setVariable(x);
							iota.setProposition(haveVendor);
							message.addReceiver(ad.getName());
							manager.fillContent(message, iota);
							send(message);
							
						}catch (CodecException e) {
							logger.severe(e.toString()+"Query to Dealer "+ ad.getName()+" cannot be created \n"+e.getMessage());
						}catch(OntologyException e){
							logger.severe(e.toString()+"Query to Dealer "+ ad.getName()+" cannot be created \n"+e.getMessage());
						}finally{
							//log status register
							statusRegister.setStatus(IMPlannerStatus.FR);;
						}
						}
					}
				}catch (FIPAException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				}
				else{
					System.out.println("No file selected.");
				}
				
			
		}
		
	}
	
	
	
	
	public static String makePartFactString( MfgPartModel aModel)
	{
		StringBuffer buffer = new StringBuffer();
		buffer.append("(assert (part \n");
		buffer.append("\t(material "+  aModel.getPartMaterial()+ ")\n");
		buffer.append("\t(batch-size "+  aModel.getBatchSize()+ ")\n");
		buffer.append("\t(x-dim "+  20.0+ ")\n");
		buffer.append("\t(y-dim "+  10.0+ ")\n");
		buffer.append("\t(z-dim "+  10.0+ ")\n");
		buffer.append("\t(quality "+  20.0+ ")\n");
		buffer.append(")\n)\n");
		return buffer.toString();
		
	}
	
	

	public ManagementData createManagementData(){
		ManagementData m = new ManagementData();
		String partName = partFile.getName();
		partName = partName.substring(0, partName.indexOf("."));
		m.setPartID(instanceID);
		m.setPartName(partName);
		m.setVersion(1);
		return m;
	}

	private String getByteStream (FileInputStream inputStream) throws IOException{
		Vector<Integer> bytes = new Vector<Integer>();
		int c;
		while ((c = inputStream.read()) != -1) {
			bytes.add(c);
		}
		StringBuilder byteStream = new StringBuilder();
		for(int i=0; i<bytes.size(); i++){
			byteStream.append(bytes.get(i));
			byteStream.append(",");
		} 
		return byteStream.toString();
	}

	@Override
	public ServiceDescription[] getServices() {
		// TODO Auto-generated method stub
		//return null;
		ServiceDescription[] services = new ServiceDescription[]{new ServiceDescription(){{
			setName("ImplannerAgent");
			setType(AgentType.ImPlanner.toString());
			
		}
		}};
		return services;
	}

	/**
	 * Status Register class is initiated with Status FR and as
	 * progresses, decide on the next possible status
	 * @author sarkara1
	 *
	 */
	class StatusRegister{

//		public MfgPartModel mfgPartModel;
//		public String partName;
		private IMPlannerStatus status;
		public Map<ManagementData, IMPlannerStatus> statusLog = new HashMap<ManagementData, IMPlannerStatus>();

		public StatusRegister() {
			this.status = IMPlannerStatus.unloaded;
		}
		
		public IMPlannerStatus getStatus(){
			return status;
		}
		
		public boolean setStatus(IMPlannerStatus status){
			if (isPossible(status))
				this.status = status; 
			else
				return false;
			return true;
		}
		
		public boolean isPossible(IMPlannerStatus status){
			return true;
		}
		
	}
	
	/**
	 * hidden class to generate FPN
	 * @author Dusan Sormaz
	 *
	 */
	class MfgFeaturePrecedenceGenerator implements TableCellGenerator {
		public MfgFeaturePrecedenceGenerator() {
		}
		/** Method that returns TRUE or FALSE based on if two given features
		 *  precedes the other.
		 */
		public Object makeTableCell (Object o1, Object o2) {
			if (o1 instanceof MfgFeature && o2 instanceof MfgFeature) {
				if(o1.equals(o2))
					return new Boolean(false);
				return new Boolean(((MfgFeature) o1).gettNext().contains((MfgFeature) o2));
			}
			return new Boolean(false);
		}

		/**
		 * Method for updating object relation based on value entered into the
		 * cell via gui
		 */
		public void updateRelation (Object o1, Object o2 , Object dataValue) {
		}
	}
}
