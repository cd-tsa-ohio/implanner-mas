package edu.ohiou.mgfresearch.implanner.simulation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import jess.Rete;
import edu.ohiou.mfgresearch.implanner.activity.PartActivity;
import edu.ohiou.mfgresearch.implanner.features.MfgFeature;
import edu.ohiou.mfgresearch.implanner.network.MachineSequenceObject;
import edu.ohiou.mfgresearch.implanner.network.NetObject;
import edu.ohiou.mfgresearch.implanner.network.Network;
import edu.ohiou.mfgresearch.implanner.network.TestExamples;
import edu.ohiou.mfgresearch.implanner.parts.IntegrationPanel;
import edu.ohiou.mfgresearch.implanner.parts.MfgPartModel;
import edu.ohiou.mfgresearch.implanner.processes.MfgProcess;
import edu.ohiou.mfgresearch.implanner.resources.Machine;

public class SimulationPanel extends IntegrationPanel {
	
	public static int SHOW_SIMULATION = 2048;
	public static int SHOW_PPN = 1024;
	
    protected JButton showSimulationButton = null;
	protected JButton showPPNButton = null;
	public static LinkedList<MachineSequenceObject> machineSequence;
	
	{

		addButtonOptions(SHOW_PPN | SHOW_SIMULATION);
//		removeButtonOptions(OPEN_XML | SAVE_XML);
	}

	public SimulationPanel() {
		// TODO Auto-generated constructor stub
	}

	public SimulationPanel(MfgPartModel model) {
		super(model);
		// TODO Auto-generated constructor stub
	}

	public SimulationPanel(Rete engine) {
		super(engine);
		// TODO Auto-generated constructor stub
	}
	
protected void configureButtons() {
	super.configureButtons();
	this.buttonToolBar.addSeparator();
   	if (isOptionActive(SHOW_PPN)) buttonToolBar.add(getShowPPNButton());
   	if (isOptionActive(SHOW_SIMULATION)) buttonToolBar.add(getShowSimulationButton());
}
	
	/**
	 * This method initializes showSimulationButton
	 *
	 * @return javax.swing.JButton
	 */
	private JButton getShowSimulationButton() {
		if (showSimulationButton == null) {
			showSimulationButton = new JButton();
			showSimulationButton.setText("show Simulation");
			showSimulationButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					showSimulationButton_actionPerformed(e);
				}
			});
		}
		return showSimulationButton;
	}

	void showSimulationButton_actionPerformed(java.awt.event.ActionEvent e) {
		LinkedList<MachineSequenceObject> machineList = machineSequence;
		Map<String, LinkedList<MachineSequenceObject>> partMap = new HashMap<String, LinkedList<MachineSequenceObject>>();
		partMap.put(this.partModel.getPartName(), machineList);	
		ImplannerSimulator simulator = new ImplannerSimulator(partMap);
		simulator.display("My Simulation");


	}


	
	/**
	 * This method initializes showPPNButton
	 *
	 * @return javax.swing.JButton
	 */
	private JButton getShowPPNButton() {
		if (showPPNButton == null) {
			showPPNButton = new JButton();
			showPPNButton.setText("show PPN");
			showPPNButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					showPPNButton_actionPerformed(e);
				}
			});
		}
		return showPPNButton;
	}
	
	void showPPNButton_actionPerformed(java.awt.event.ActionEvent e) {
		TestExamples example = new TestExamples();
		example.setPartName(this.partModel.getPartName());
		PartActivity pAct = new PartActivity("partAct", example, 3, 3.0);
		pAct.setActivityForPart(example);
		example.addAltPlan(pAct);
		// example.display("Test");
		//example.setData(7);
		this.populatePPNData(example);
		//ImpObject.doNothing(example, "Running " + exampleNames[exampleNumber] );
		example.net = new Network(example, example.useGhosal);
		example.net.display();
		LinkedList netObj = example.net.netObjects();
		System.out.println("======> Net objects from canvas");
		for (Object o : netObj) {
			NetObject n = (NetObject) o;		
//		System.out.println ("net object is " + n.toString());
		System.out.println( n.toGraphString());
		}
		System.out.println("======> Net objects from process canvas");
		try {
			example.net.writeNetObjectsProcess(example.getPartName() + ".graph");
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
//		try {
//			example.writeXMLFile(new File ("example.xml"));
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
		this.machineSequence = example.net.getMachineSequence();

	}
	boolean useGhosal = true;
	void showPPNButton_actionPerformedModel(java.awt.event.ActionEvent e) {
		TestExamples example = new TestExamples();
		example.setPartName(this.partModel.getPartName());
		PartActivity pAct = new PartActivity();
		pAct.setActivityForPart(partModel);
		partModel.addAltPlan(pAct);
		// example.display("Test");
		//example.setData(7);
		this.populatePPNDataModel(partModel);
		//ImpObject.doNothing(example, "Running " + exampleNames[exampleNumber] );
		Network net = new Network(partModel, useGhosal);
		net.display();
		this.machineSequence = net.getMachineSequence();

	}
	
	/**
	 * This method populates data for PPN representation
	 *
	 * @return javax.swing.JButton
	 */
	private void populatePPNData( TestExamples testExample){
		testExample.setFeatureList(this.partModel.getFeatureList());

		LinkedList <MfgFeature> featureList = testExample.getFeatureList();
		LinkedList <Machine> machineList = new LinkedList<Machine>();
		double cost =2.0;
		for(int i=0; i< featureList.size(); i++){
			MfgFeature currentFeature = featureList.get(i);
			LinkedList <MfgProcess> processList = currentFeature.getProcesses();
			for (int j=0;j< processList.size(); j++){
				MfgProcess currentProcess = ((MfgProcess)processList.get(j));
				Machine currentMachine = currentProcess.getMachine();
				if(!machineList.contains(currentMachine)){
					machineList.add(currentMachine); 
				}
				currentMachine.addMachiningCost(currentFeature, currentProcess.getProcessTime());
				//cost = cost+.75;

			}
			//cost = cost -1.0 ;
		}
		testExample.useGhosal =true;

		System.out.println("done PPN");

	}
	
	/**
	 * This method populates data for PPN representation
	 *
	 * @return javax.swing.JButton
	 */
	private void populatePPNDataModel( MfgPartModel testExample){
		testExample = partModel;

		LinkedList <MfgFeature> featureList = testExample.getFeatureList();
		LinkedList <Machine> machineList = new LinkedList<Machine>();
		double cost =2.0;
		for(int i=0; i< featureList.size(); i++){
			MfgFeature currentFeature = featureList.get(i);
			LinkedList <MfgProcess> processList = currentFeature.getProcesses();
			for (int j=0;j< processList.size(); j++){
				MfgProcess currentProcess = ((MfgProcess)processList.get(j));
				Machine currentMachine = currentProcess.getMachine();
				if(!machineList.contains(currentMachine)){
					machineList.add(currentMachine); 
				}
				currentMachine.addMachiningCost(currentFeature, currentProcess.getProcessTime());
				//cost = cost+.75;

			}
			//cost = cost -1.0 ;
		}
		useGhosal =true;

		System.out.println("done PPN");

	}


	public static void main(String[] args) {
		SimulationPanel sp = new SimulationPanel();
		sp.display("Simulation Panel");

	}

}
