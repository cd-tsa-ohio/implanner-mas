package edu.ohiou.mfgresearch.impmas.semantics;

import edu.ohiou.mfgresearch.impmas.agents.IMPlannerProperties.IMPlannerStatus;
import jade.content.Concept;

public class ManagementData implements Concept {

	private static final long serialVersionUID = -6235595303303424392L;
	
	private String partName;
	private long partID;
	private IMPlannerStatus requestID;
	private int version;
	
	public ManagementData() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public ManagementData(String partName, IMPlannerStatus requestID, long partID, int version) {
		super();
		this.partName = partName;
		this.requestID = requestID;
		this.partID = partID;
		this.version = version;
	}

	public String getPartName() {
		return partName;
	}

	public void setPartName(String partName) {
		this.partName = partName;
	}

	public IMPlannerStatus getRequestID() {
		return requestID;
	}

	public void setRequestID(IMPlannerStatus fr) {
		this.requestID = fr;
	}

	public long getPartID() {
		return partID;
	}

	public void setPartID(long partID) {
		this.partID = partID;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		if(obj instanceof ManagementData){
			ManagementData md = (ManagementData) obj;
			if(md.partID==this.partID
					&& md.requestID==this.requestID
					&& md.version == this.version)
				return true;
		}
		return false;
	}

	@Override
	public String toString() {
		return "ManagementData [partName=" + partName + ", requestID="
				+ requestID + ", partID=" + partID + ", version=" + version
				+ "]";
	}
}
