package edu.ohiou.mfgresearch.impmas.semantics;

import java.io.File;
import java.io.FileOutputStream;
import java.util.StringTokenizer;

import edu.ohiou.mfgresearch.implanner.MfgConcept;

public class PartFile extends MfgConcept {

	private static final long serialVersionUID = -7451020825784194564L;

	public static final String NAME = "PartFile";
	
	private String partFile  = null;
	
	private String name = "";
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	private File tempFile = null;
	
	public PartFile () {
		
	}

	public PartFile(String partFile) {
		super();
		this.partFile = partFile;
	}

	public String getPartFile() {
		return partFile;
	}

	public void setPartFile(String partFile) {
		this.partFile = partFile;
	}
	
// @author Suvo
	public File getTempFile(){
	
		StringTokenizer st=new StringTokenizer(partFile,",");
		tempFile= new File(System.getProperty("java.io.tmpdir")+this.name);
		
		byte [] b2=new byte [partFile.length()];
		int i=0;
		try {
		FileOutputStream file=new FileOutputStream(tempFile);
		
			while(st.hasMoreElements()){
				
				b2[i] = (byte)Integer.parseInt ((String) st.nextElement()) ;

				file.write(b2[i]);
				i++;
				
					
					
						
					
					
				
				
			}
			file.close();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tempFile;

	}
	

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		if(partFile!=null){
			return "File of length"+partFile.length();
		}
		else{
			return "File of length"+0;
		}
	}

}
