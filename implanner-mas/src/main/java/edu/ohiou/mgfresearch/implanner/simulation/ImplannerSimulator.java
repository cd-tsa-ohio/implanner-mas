package edu.ohiou.mgfresearch.implanner.simulation;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JApplet;
import javax.swing.JPanel;

import edu.ohiou.mfgresearch.implanner.network.MachineSequenceObject;
import edu.ohiou.mfgresearch.labimp.basis.ViewObject;
import sequece_test.Simulation;

public class ImplannerSimulator extends ViewObject{
	
	Simulation s;
	Map <String, LinkedList<MachineSequenceObject>> partSequenceMap;
	
	public ImplannerSimulator(Map <String, LinkedList<MachineSequenceObject>> partSequenceMap){
		this.partSequenceMap = partSequenceMap;
	}
	 
	 public void init(){
//		 panel = new JPanel();
	 try {
		 this.s = new Simulation();
		 s.machineList = new LinkedList<String[]>();
		 s.processTimeList = new LinkedList<Double[]>();
		//LinkedList<String[]>mymachineList = new LinkedList<String[]>();
		//LinkedList<Double[]>myprocessTimeList = new LinkedList<Double[]>();
		 for(Entry<String, LinkedList<MachineSequenceObject>> entry : partSequenceMap.entrySet()){
			
			
				String[] machineList = new String[entry.getValue().size()];
				Double[] processTimeList = new Double[entry.getValue().size()];
				
				for (int i =0; i< entry.getValue().size();i++){
					machineList[i]=(((MachineSequenceObject)entry.getValue().get(i)).getMachineName());
					processTimeList[i]=(((MachineSequenceObject)entry.getValue().get(i)).getMachineTime());
				}
				
				if(machineList.length!= processTimeList.length){
						throw new Exception("lists do not match");
				}
				
				s.machineList.add(machineList);
				s.processTimeList.add(processTimeList);
		 }
		 s.Dispatch = "LIPT";
		 System.out.println("end");
		
	 } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 	
		s.setup(this.gettApplet());
		
	 }
	 
	 public static void main(String[] args) {
		 
		 LinkedList<MachineSequenceObject> machineList = new LinkedList<MachineSequenceObject>();
		 MachineSequenceObject obj = new MachineSequenceObject();
		 obj.setMachineName("CncHMillFast");
		 obj.setMachineTime(1.2);
		 MachineSequenceObject obj1 = new MachineSequenceObject();
		 obj1.setMachineName("CncDrillFast");
		 obj1.setMachineTime(1.3);
		 MachineSequenceObject obj2 = new MachineSequenceObject();
		 obj2.setMachineName("CncVMillSlow");
		 obj2.setMachineTime(1.4);
		 MachineSequenceObject obj3 = new MachineSequenceObject();
		 obj3.setMachineName("CncDrillFast");
		 obj3.setMachineTime(1.9);
		 MachineSequenceObject obj4 = new MachineSequenceObject();
		 obj4.setMachineName("CncHMillFast");
		 obj4.setMachineTime(1.3);
		 
		 machineList.add(obj);
	     machineList.add(obj1);
	     machineList.add(obj2);
	     machineList.add(obj3);
	     machineList.add(obj4);
			
	   //  ImplannerSimulator myclass = new ImplannerSimulator(machineList);
		// myclass.display("My Simulation");
	}
	

}
