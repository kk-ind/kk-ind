package utilities;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import automationFramework.GenericKeywords;
import baseClass.BaseClass;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class DataDriver extends Common {
	public int dataRowNo=0;
	public String currentTestCaseName="";
	public  int rowCount = 0, colCount = 0, maxRows = 100,
			currentExcelSheetNo;
	public  String wkbook = "";
	public  File nf;
	public  Workbook w;
	public int tempNo=1;
	public  Sheet s;
	public  String currentExcelBook, currentExcelSheet;
	public List<Integer> iterationCount = new ArrayList<Integer>();
	public DataDriver(BaseClass obj) {
		// TODO Auto-generated constructor stub
		super(obj);
		this.dataRowNo=obj.dataRowNo;
		this.currentTestCaseName=obj.currentTestCaseName;
		this.rowCount = obj.rowCount; 
		this.colCount = obj.colCount;
		this.maxRows = obj.maxRows;
		this.currentExcelSheetNo=obj.currentExcelSheetNo;
			this.wkbook = obj.wkbook;
				this.nf=obj.nf;
				this.w=obj.w;
				this.tempNo=obj.tempNo;
				this.s=obj.s;
				this.currentExcelBook=obj.currentExcelBook;
				this.currentExcelSheet=obj.currentExcelSheet;
				this.iterationCount=obj.iterationCount;
				this.testCaseNames=obj.testCaseNames;
	}
	public DataDriver()
	{
		
	}

	public void useExcelSheet(String wkbook, int dsheet) {
		currentExcelBook = wkbook;
		currentExcelSheetNo = dsheet;
		nf = new File(currentExcelBook);
		if (nf.exists()) {
			try {
				w = Workbook.getWorkbook(nf);
			} catch (BiffException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (w.getNumberOfSheets() >= dsheet) {
				s = w.getSheet(dsheet - 1);
				updateRowCount();
				updateColCount();
			}
		}
	}
	
	public String getData(int row, int col) {
		Cell c;
		c = s.getCell(col - 1, row - 1);
		return c.getContents();
	}

	public void loadTestCaseData()
	{
		useExcelSheet(getConfigProperty("TestDataFile"),testDataSheetNo);
		
		Sheet readsheet = w.getSheet((testDataSheetNo-1));
		for (int i = 0; i < readsheet.getRows(); i++) {
			String testCaseName = readsheet.getCell(0, i).getContents();
			testCaseNames.add(testCaseName);
			
		}
	}
	/*public ArrayList<String> getTotalNoTCExecution()
	{
		ArrayList<String> testcasesExecutionNames = new ArrayList<String>();
		useExcelSheet(Common.getConfigProperty("Tc_Settings_Excelpath"), 1);
		
		Sheet readsheet = w.getSheet(0);
		for (int i = 1; i < readsheet.getRows(); i++) 
		{
			String executionFlag = readsheet.getCell(4, i).getContents();
			
			if(executionFlag.trim().equalsIgnoreCase("yes"))
			{
				String testCaseName = readsheet.getCell(2, i).getContents();
				testcasesExecutionNames.add(testCaseName);
			}
			
			
		}
		return testcasesExecutionNames;
	}*/
	public int getTestCaseDataRowNo(String testCaseName)
	{
		int rowNo=0;
		loadTestCaseData();
		for(String a: testCaseNames)
        {
			
			if(a.trim().equalsIgnoreCase(testCaseName.trim()))
			{
				
				rowNo=rowNo+1;
				break;
			}
			rowNo++;
        }
		return rowNo;
		
	}
	public  String retrieve(String Label)
	{
		return retrieve(dataRowNo, Label);
	}

public  String retrieve(int datasetNo, String colLabel) {  
		
            return getData(datasetNo, returnColNo(getTestCaseDataRowNo(currentTestCaseName),colLabel));
            
      }
      public  int returnColNo(int datasetNo,String colLabel) {
            boolean flag = true;
            int temp = 0;
            while (flag) 
            {
                  
                  try 
                  {
                	  	temp++;
                        if (getData(datasetNo, temp).trim().equalsIgnoreCase(colLabel.trim())) 
                        {
                              flag = false;
                              return temp;
                        }
                        
                  } catch (Exception e)
                  {
                     Common.testFailed();
                	  break;
                  }
            }
            return 0;
      }

	public void updateRowCount() {
		boolean flag = true;
		int temp = 0;
		while (flag) {
			temp++;
			try {
				if (getData(temp, 1).trim().length() == 0) {
					flag = false;
				}
			} catch (Exception e) {
				GenericKeywords.rowCount = temp - 2;
				break;
			}
		}

	}

	public void updateColCount() {
		boolean flag = true;
		int temp = 0;
		while (flag) {
			temp++;
			try {
				if (getData(1, temp).trim().length() == 0) {
					flag = false;
				}
			} catch (Exception e) {
				GenericKeywords.colCount = temp - 1;
				break;
			}
		}
	}

	public static void putData(int row, int col, String data) {
		String wkbook;
		int dsheet;
		if (GenericKeywords.currentExcelBook.length() != 0) {
			wkbook = GenericKeywords.currentExcelBook;
		} else {
			wkbook = "";
			GenericKeywords.writeToLogFile("ERROR",
					"Excel Book - Not initialized/defined earlier");
		}
		if (GenericKeywords.currentExcel != 0)
			dsheet = GenericKeywords.currentExcel;
		else
			dsheet = 0;
		GenericKeywords.writeToLogFile("ERROR",
				"Excel Sheet - Not initialized/defined earlier");

		File nf = new File(wkbook);
		WritableWorkbook w = null;
		WritableSheet s = null;
		WritableCell c = null;
		try {
			if (nf.exists()) {
				Workbook wb = Workbook.getWorkbook(nf);
				w = Workbook.createWorkbook(new File(wkbook), wb);

				if (w.getNumberOfSheets() >= dsheet) {
					s = w.getSheet(dsheet - 1);

					try {
						c = s.getWritableCell(col - 1, row - 1);
						GenericKeywords.writeToLogFile(
								"INFO",
								"Value of cell before modification : "
										+ c.getContents());
						Label l = new Label(col - 1, row - 1, data);
						s.addCell(l);
						w.write();
						Cell c1 = s.getCell(col - 1, row - 1);
						GenericKeywords.writeToLogFile(
								"INFO",
								"Value of cell after modification : "
										+ c1.getContents());

					} catch (Exception e) {
						GenericKeywords.writeToLogFile("ERROR",
								"Data cannot be modified in the cell | Row:"
										+ row + ", Col:" + col);
					}
				} else {
					GenericKeywords.writeToLogFile("ERROR",
							"Invalid sheet number :" + dsheet);
				}

				w.close();
			} else {
				GenericKeywords.writeToLogFile("ERROR",
						"Specified File/Path does not exist :" + wkbook
								+ ". Please check.");
			}
		} catch (Exception e) {
			GenericKeywords.writeToLogFile("ERROR",
					"Data cannot be modified in the cell | Row:" + row
							+ ", Col:" + col);
		}
	}

	public void closeExcelSheet() {
		currentExcelBook = "";
		currentExcelSheetNo = 0;
		w.close();
		nf = null;
	}
	
	public void iterationCountInTextData()
	  {
	    try
	    {
	    	testCaseDataRow=getTestCaseDataRowNo(currentTestCaseName);
	      for (int i = 1;; i++) {
	        if (getData(testCaseDataRow + i, 2).equalsIgnoreCase("yes")) {
	          iterationCount.add(Integer.valueOf(testCaseDataRow + i));
	        } else {
	          if (getData(testCaseDataRow + i, 2).equalsIgnoreCase("")) {
	            break;
	          }
	        }
	      }
	    }
	    catch (Exception e)
	    {
	      System.out.println(e.toString());
	    }
	  }
	public void testDataRowNo(int no)
	{
		testCaseDataRow=no;
	}
}
