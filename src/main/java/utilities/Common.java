package utilities;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import javax.imageio.ImageIO;
import org.testng.Assert;

import automationFramework.GenericKeywords;
import baseClass.BaseClass;

public class Common 
{
	public static String testName;
	public int testCaseDataNo,testCaseexecutionNo=0;
	public ArrayList<String> testCaseNames = new ArrayList<String>();
  	public int testStepNo=0;
	public int failuretestStepNo=0;
	public int testCaseDataRow =0;
	public int testDataSheetNo=0;
	public String testStepReport="";
	public String failureStepReport="";
	public Common(BaseClass obj) {
		// TODO Auto-generated constructor stub
		this.testCaseDataNo=obj.testCaseDataNo;
		this.testCaseexecutionNo=obj.testCaseexecutionNo;
		this.testStepNo=obj.testStepNo;
		this.failuretestStepNo=obj.failuretestStepNo;
		this.testCaseDataRow =obj.testCaseDataRow;
		this.testDataSheetNo=obj.testDataSheetNo;
		this.testStepReport=obj.testStepReport;
		this.failureStepReport=obj.failureStepReport;
	}
	public Common()
	
	{
		
	}

	public static String getConfigProperty(String keyword)
	{
		Properties properties=new Properties();
		try 
		{
			properties.load(new FileInputStream("./src/main/resources/config/TestConfiguration.properties"));
		} 
		catch (FileNotFoundException e) 
		{
			writeToLogFile("ERROR","File Not Found Exception thrown while getting value of "+keyword+" from Test Configuration file");
		} catch (IOException e) 
		{
			writeToLogFile("ERROR","IO Exception thrown while getting value of "+keyword+" from Test Configuration file");
		}
		writeToLogFile("INFO","Getting value of "+keyword+" from Test Configuration file : "+properties.getProperty(keyword));
		System.out.println(properties.getProperty(keyword).trim());
		return properties.getProperty(keyword).trim();		
	}
	
	public static void writeToLogFile(String type, String message)
	{
		String t=type.toUpperCase();
		if (t.equalsIgnoreCase("DEBUG"))
		{
			GenericKeywords.logger.debug(message);
		}
		else if (t.equalsIgnoreCase("INFO"))
		{
			GenericKeywords.logger.info(message);
		}
		else if (t.equalsIgnoreCase("WARN"))
		{
			GenericKeywords.logger.warn(message);
		}
		else if (t.equalsIgnoreCase("ERROR"))
		{
			GenericKeywords.logger.error(message);
		}
		else if (t.equalsIgnoreCase("FATAL"))
		{
			GenericKeywords.logger.fatal(message);
		}
		else
			GenericKeywords.logger.error("Invalid log Type :"+type+". Unable to log the message.");
	}

	public void startup()
	{
		OutputAndLog.createOutputDirectory();
		
		
		try {
			PropertiesFile.properties();
			if(!(PropertiesFile.getConfigProperty("CssStyle").toLowerCase().contains("nostyle")))
			{
				HtmlReport.createCssFile(GenericKeywords.outputDirectory+"/style.css");
			}
		} catch (Exception e) 
		{
		writeToLogFile("INFO","Startup activities - Done...");
		}
	}

	public static void cleanup()
	{
		
		DeleteTempFiles.delete();
		writeToLogFile("INFO","Cleanup activites...");
		writeToLogFile("INFO","Cleanup activities - Done...");
			
		
		
	}


	public static void screenShot(String filename)
	{
		String scrPath=GenericKeywords.outputDirectory+"/Screenshots";
		File file = new File(scrPath);
	    file.mkdir();
	    try {
	        Robot robot = new Robot();
	        Rectangle captureSize = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
	        BufferedImage bufferedImage = robot.createScreenCapture(captureSize);
	        File outputfile = new File(scrPath+"/"+filename+".png");
	        ImageIO.write(bufferedImage, "png", outputfile);       
       		GenericKeywords.writeToLogFile("INFO", "Taken screenshot of failing screen");
	    }
	    catch(AWTException e) {
	        GenericKeywords.writeToLogFile("ERROR", "AWT Exception : While taking screenshot of the failing test case");
	    } catch (IOException e) {
	        GenericKeywords.writeToLogFile("ERROR", "IO Exception : While taking screenshot of the failing test case");
		}
	}
	
	public static void testFailed()
	{
	                        
		Assert.fail();
   
	}
	public void useExcelSheet(String pathOfExcel, int sheetNumber)
	{
		useExcelSheet(pathOfExcel, sheetNumber);
	}
	public void closeExcelSheet()
	{
		closeExcelSheet();
	}
	
	/*public void writeTCFailureStepsInHtml(String report,String currentMachineName, String currentTestCaseName)
	{
		try{
		Writer output;
		
		for(String machineNames:GenericKeywords.testCaseResultsDir)
		{
			if(!(report.contains("<a") || report.contains("<img")))
			{
				failuretestStepNo++;
			}
			if(report.contains("<B></B>"))
			{
				failureStepReport="";
				failuretestStepNo=0;
			}
			failureStepReport=failureStepReport+report.replace("stepNo", failuretestStepNo+".");
			if(machineNames.contains(currentMachineName))
			{		
				
				File dir = new File(machineNames.replace("/Resultfiles/", "/DefectAnalysis/"));
				dir.mkdir();
				
				PrintWriter writer = new PrintWriter(machineNames.replace("/Resultfiles/", "/DefectAnalysis/")+"./"+currentTestCaseName+".html");
				writer.print("");
				writer.close();
				output = new BufferedWriter(new FileWriter(machineNames.replace("/Resultfiles/", "/DefectAnalysis/")+"./"+currentTestCaseName+".html",true));  //clears file every time
				
				output.write(HtmlReport.addCssLink().replace("../", "../../")+failureStepReport);
				//output.write("<center><a href='../SummaryPage.html'>back to summary</a></center></body></html>");
				
				
				output.close();
			}
			
			
		}
		
		}
		catch(Exception e)
		{
			System.out.println(e.toString());
		}
	}
	public void writeHtmlTestStepReport(String report,String currentMachineName, String currentTestCaseName)
	{

		try{
			//Writer output;
			for(String machineNames:GenericKeywords.testCaseResultsDir)
			{
				if(machineNames.contains(currentMachineName))
				{
					if(report.contains("<B></B>") || report.toLowerCase().contains("dataset"))
					{
						testStepNo=0;
						if(report.contains("<B></B>"))
						{
							testStepReport="";
						}

					}
					//testStepReport=testStepReport.replace("<center><a href='../SummaryPage.html'>back to summary</a></center></body></html>", "")+report.replace("stepNo", testStepNo+".");
					//PrintWriter writer = new PrintWriter(machineNames+"./"+currentTestCaseName+".html");
					//writer.print("");
					////writer.close();
					//output = new BufferedWriter(new FileWriter(machineNames+"./"+currentTestCaseName+".html",true));  //clears file every time

					//output.write(HtmlReport.addCssLink().replace("../", "../../")+testStepReport);
					//	output.write(testStepReport);
					//output.write("<center><a href='../SummaryPage.html'>back to summary</a></center></body></html>");
					if(!(report.contains("TestFailure")))
					{
						testStepNo++;
					}

					//output.close();
				}

			}

		}
		catch(Exception e)
		{
			System.out.println(e.toString());
		}
	}*/
	
	public void XmlReport(String status,String description,String currentMachineName,String currentTestCaseName,String screenShotName, String link)
	{
		XMLReport xmlReport = new XMLReport();
		String location = "";
		try
		{
			for(String machineName: GenericKeywords.xmlReport)
			{

				if(machineName.contains(currentMachineName))
				{
					location = machineName;
					//writeToLogFile("INFO", "MachineName:"+machineName);
					//writeToLogFile("INFO", "CurrentMachineName:"+currentMachineName);
					//writeToLogFile("INFO", "CurrentTestCaseName:"+currentTestCaseName);
					if(status == null)
					{
						xmlReport.createTestCaseNode(machineName,currentTestCaseName,description);
					}
					else
					{
						xmlReport.createTestStep(machineName,currentTestCaseName,status,description,screenShotName,link);
						xmlReport.transformXsltToHtmlPage(location.replace(currentMachineName+".xml", ""), currentMachineName, currentTestCaseName);
					}

				}

			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void XmlReport(String description,String currentMachineName,String currentTestCaseName){
		XmlReport(null, description, currentMachineName, currentTestCaseName, null, null);
	}
	
	public void XmlReport(String status, String description,String currentMachineName,String currentTestCaseName,String link){
		XmlReport(status, description, currentMachineName, currentTestCaseName, null, link);
	}
	
	public void XmlReport(String status, String description,String currentMachineName,String currentTestCaseName){
		XmlReport(status, description, currentMachineName, currentTestCaseName, null, null);
	}
	
	
}
