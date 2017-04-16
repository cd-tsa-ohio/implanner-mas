package edu.ohiou.mfgresearch.impmas.mfgOntology;

import javax.vecmath.Vector3d;

import edu.ohiou.mfgresearch.implanner.activity.MfgActivity;
import edu.ohiou.mfgresearch.implanner.features.*;
import edu.ohiou.mfgresearch.implanner.geometry.CADObject;
import edu.ohiou.mfgresearch.implanner.geometry.Cylinder;
import edu.ohiou.mfgresearch.implanner.geometry.ExtrudeSolid;
import edu.ohiou.mfgresearch.implanner.geometry.PartModel;
import edu.ohiou.mfgresearch.implanner.geometry.Solid;
import edu.ohiou.mfgresearch.implanner.geometry.Stock;
import edu.ohiou.mfgresearch.implanner.parts.MfgPartModel;
import edu.ohiou.mfgresearch.implanner.processes.Boring;
import edu.ohiou.mfgresearch.implanner.processes.CenterDrilling;
import edu.ohiou.mfgresearch.implanner.processes.CoreMaking;
import edu.ohiou.mfgresearch.implanner.processes.CounterBoring;
import edu.ohiou.mfgresearch.implanner.processes.CuttingParameter;
import edu.ohiou.mfgresearch.implanner.processes.EndDrilling;
import edu.ohiou.mfgresearch.implanner.processes.EndMilling;
import edu.ohiou.mfgresearch.implanner.processes.EndMillingPeripheral;
import edu.ohiou.mfgresearch.implanner.processes.EndMillingSlotting;
import edu.ohiou.mfgresearch.implanner.processes.FaceMilling;
import edu.ohiou.mfgresearch.implanner.processes.FinishMilling;
import edu.ohiou.mfgresearch.implanner.processes.GunDrilling;
import edu.ohiou.mfgresearch.implanner.processes.HoleGrinding;
import edu.ohiou.mfgresearch.implanner.processes.HoleImproving;
import edu.ohiou.mfgresearch.implanner.processes.HoleStarting;
import edu.ohiou.mfgresearch.implanner.processes.Holemaking;
import edu.ohiou.mfgresearch.implanner.processes.Honing;
import edu.ohiou.mfgresearch.implanner.processes.MfgProcess;
import edu.ohiou.mfgresearch.implanner.processes.Milling;
import edu.ohiou.mfgresearch.implanner.processes.PrecisionBoring;
import edu.ohiou.mfgresearch.implanner.processes.Reaming;
import edu.ohiou.mfgresearch.implanner.processes.RoughMilling;
import edu.ohiou.mfgresearch.implanner.processes.SemiFinishMilling;
import edu.ohiou.mfgresearch.implanner.processes.SideMilling;
import edu.ohiou.mfgresearch.implanner.processes.SlabMilling;
import edu.ohiou.mfgresearch.implanner.processes.SpadeDrilling;
import edu.ohiou.mfgresearch.implanner.processes.SpotDrilling;
import edu.ohiou.mfgresearch.implanner.processes.Tool;
import edu.ohiou.mfgresearch.implanner.processes.TwistDrilling;
import edu.ohiou.mfgresearch.implanner.resources.Machine;
import edu.ohiou.mfgresearch.implanner.resources.MfgSystem;
import edu.ohiou.mfgresearch.impmas.semantics.*;
import edu.ohiou.mfgresearch.labimp.gtk3d.Arc;
import edu.ohiou.mfgresearch.labimp.gtk3d.LineSegment;
import edu.ohiou.mfgresearch.labimp.gtk3d.Plane;
import edu.ohiou.mfgresearch.labimp.gtk3d.Profile3d;
import jade.content.onto.BasicOntology;
import jade.content.onto.BeanOntology;
import jade.content.onto.BeanOntologyException;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.OntologyUtils;
import jade.content.onto.basic.Done;
import jade.content.schema.ConceptSchema;

public class IMPlannerOntology extends BeanOntology implements IMPlannerVocabulary {

	private static final long serialVersionUID = 6616771620181713072L;

	public static final String ONTOLOGY_NAME = "IMPlanner-ontology";

	// The singleton instance of this ontology
	private static Ontology theInstance = null;

	public synchronized final static Ontology getInstance() throws BeanOntologyException {
		if(theInstance == null)
			theInstance = new IMPlannerOntology();
		return theInstance;
	}

	/**
	 * Constructor
	 */
	private IMPlannerOntology() {
		super(ONTOLOGY_NAME);

		try {
			//Add Concepts
			add(PartFile.class);
			add(ManagementData.class);
			add(BidValue.class);
			add(FactFile.class);
			//add(CADObject.class);              //pass
			add(PartModel.class);              //pass
			add(MfgPartModel.class);           //pass
			add(Stock.class);
			add(CuttingParameter.class);       //pass 
			add(MfgFeature.class);             //pass
			//add(Slot.class);             		//pass
			//add(Hole.class);

			add(MfgProcess.class);
			add(MfgActivity.class);            //pass
			add(MfgSystem.class);              //pass
			add(Machine.class);                //pass
			add(Tool.class); 
			add(Plane.class);
			add(Arc.class);
			add(LineSegment.class);
			add(ExtrudeSolid.class);
			add(Profile3d.class);
			//Add features
			
			add(Slot.class);
			add(Hole.class);
			add(Pocket.class);
			//add(Vector3d.class);
			//add(CircularFeatureSet.class);
			add(DovetailSlot.class);
			//add(FeatureSet.class);
			//add(Hole.class);
			//add(InstanceFeature.class);
			/*add(LinearFeatureSet.class);
			add(MfgFeatureSubclassHandler.class);
			add(MfgPartModeFpnPanel.class);
			add(Pocket.class);
			add(PocketPanel.class);
			add(TestPocket.class);
			add(TSlot.class);*/
			
			add(ProcessFile.class);
			
			//Add Processes
			add(Milling.class);
			add(EndMilling.class);
			add(EndMillingSlotting.class);
			add(EndMillingPeripheral.class);
			add(FaceMilling.class);
			add(FinishMilling.class);
			add(RoughMilling.class);
			add(SemiFinishMilling.class);
			add(SideMilling.class);
			add(SlabMilling.class);
			add(HoleStarting.class);
			add(Holemaking.class);
			add(HoleImproving.class);
			add(HoleGrinding.class);
			add(Honing.class);
			add(Boring.class);
			add(PrecisionBoring.class);
			add(Reaming.class);
			
			add(CenterDrilling.class);
			add(CounterBoring.class);
			add(CoreMaking.class);
			add(EndDrilling.class);
			add(GunDrilling.class);
			add(SpadeDrilling.class);
			add(SpotDrilling.class);
			add(TwistDrilling.class);
			
			add(SequenceFile.class);
			add(SimulationFile.class);
			//Add Predicates
			add(Send_Quotes.class);
			add(Done.class);
			
			//Add Actions
			add(HaveVendor.class);
			add(RegisterIMPlanner.class);

			add(ServiceAction.class);

//			add(Service.class);
			add(ResultIs.class);
			
//			add geometry
			add(Solid.class);
			add(Cylinder.class);


		} catch (OntologyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	


	public static void main(String[] args) throws Exception {
		Ontology onto = IMPlannerOntology.getInstance();
		OntologyUtils.exploreOntology(onto);
	}	
}

