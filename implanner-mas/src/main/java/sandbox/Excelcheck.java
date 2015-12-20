package sandbox;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Excelcheck {
	
	public static void main(String[] args) {

		// TODO Auto-generated method stub

		FileInputStream fileInputStream;
		try {
			fileInputStream = new FileInputStream("C:/Users/Subhabrata/Documents/thesis/Results/pp_PPNbased_Incr/PP_0.2_Incr.xlsx");
		
		XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
		XSSFSheet worksheet = workbook.getSheet("Sample sheet");
		
		double CncHMillFastDelay = 0;
		double CncVMillFastDelay = 0;
		double CncHMillSlowDelay = 0;
		double CncVMillSlowDelay = 0;
		double CncDrillFastDelay = 0;
		double CncDrillSlowDelay = 0;
		double CncVMillCombDelay = 0;
		double CncHMillCombDelay = 0;
		
		
		
		
		
		for (int i =1; i< 15; i++){
			XSSFRow row1 = worksheet.getRow(i);
			if(row1 != null){
				for(int j=0; j<20; j=j+2){
					XSSFCell cellA1 = row1.getCell(j);
					if(cellA1 != null){
					String a1Val = cellA1.getStringCellValue();
					
						switch(a1Val){
						case "CncDrillSlow": 
							CncDrillSlowDelay = CncDrillSlowDelay + row1.getCell(j +1).getNumericCellValue();
							break;
						case "CncDrillFast": 
							CncDrillFastDelay = CncDrillFastDelay + row1.getCell(j +1).getNumericCellValue();
							break;
						case "CncHMillFast": 
							CncHMillFastDelay = CncHMillFastDelay + row1.getCell(j +1).getNumericCellValue();
							break;
						case "CncVMillFast": 
							CncVMillFastDelay = CncVMillFastDelay + row1.getCell(j +1).getNumericCellValue();
							break;
						case "CncHMillSlow": 
							CncHMillSlowDelay = CncHMillSlowDelay + row1.getCell(j +1).getNumericCellValue();
							break;
						case "CncVMillSlow": 
							CncVMillSlowDelay = CncVMillSlowDelay + row1.getCell(j +1).getNumericCellValue();
							break;
						case "CncVMillComb": 
							CncVMillCombDelay = CncVMillCombDelay + row1.getCell(j +1).getNumericCellValue();
							break;
						case "CncHMillComb": 
							CncHMillCombDelay = CncHMillCombDelay + row1.getCell(j +1).getNumericCellValue();
							break;
					  default: 
							System.out.println("error");
							break;
					}
					
					
					
					}
				}
			}
		
		}
		
		System.out.println(CncHMillFastDelay);
		System.out.println(CncVMillFastDelay);
		System.out.println(CncHMillSlowDelay);
		System.out.println(CncVMillSlowDelay);
		System.out.println(CncDrillSlowDelay);
		System.out.println(CncDrillFastDelay);
		System.out.println(CncVMillCombDelay);
		System.out.println(CncHMillCombDelay);
		
		
		
		
		
		
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
       
        
		
	
	}

}
