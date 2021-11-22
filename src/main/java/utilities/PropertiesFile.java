package utilities;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import jxl.Sheet;

public class PropertiesFile  extends DataDriver{
	private static File nf;
	public static final ArrayList<String> testCases = new ArrayList<String>();
	private static String Keyword;
	private static String description;
	public static List<String> machineNames = new ArrayList<String>();
	public static Set<String> tcNames = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
	private static String level ="";
	public static HashMap<String, String> mediumTc= new HashMap<String, String>();
	public static HashMap<String, String> hardTc= new HashMap<String, String>();
	public static HashMap<String, String> lowTc= new HashMap<String, String>();
	public static HashMap<String, String> tcDescription = new HashMap<String,String>();
	public static void properties() {
		Properties prop = new Properties();
		int noBrowser=0;
		List<String> machineName=new ArrayList<String>(noBrowser);
		List<String> host=new ArrayList<String>(noBrowser);
		List<String> port=new ArrayList<String>(noBrowser);
		List<String> browser=new ArrayList<String>(noBrowser);
		List<String> os=new ArrayList<String>(noBrowser);
		List<String> browserVersion=new ArrayList<String>(noBrowser);
		List<String> osVersion=new ArrayList<String>(noBrowser);
		List<String> sheetNumber=new ArrayList<String>(noBrowser);
		List<String> environment = new ArrayList<String>(noBrowser);
		
		
		try {
		DataDriver dd = new DataDriver();
			dd.useExcelSheet("./src/main/resources/data/TestConfiguration.xls", 1);
			Sheet readsheet = dd.w.getSheet(0);
			
			for (int i = 1; i < readsheet.getRows(); i++) {
				String Keyword = readsheet.getCell(1, i).getContents();
				String value = readsheet.getCell(2, i).getContents();
				prop.setProperty(Keyword, value);

			}

			prop.store(new FileOutputStream(
					"./src/main/resources/config/TestConfiguration.properties"), null);
			dd.w.close();
			if(Common.getConfigProperty("GridExecution").toLowerCase().trim().contains("yes"))
			{
				dd.useExcelSheet("./src/main/resources/data/TestConfiguration.xls", 2);
				Sheet sheet1 = dd.w.getSheet(1);
				
				for(int index=1;index<sheet1.getRows();index++)
				{
					if(sheet1.getCell(9, index).getContents().equalsIgnoreCase("yes"))
					{
						machineName.add(sheet1.getCell(1, index).getContents());
						machineNames.add(sheet1.getCell(1, index).getContents());
						host.add(sheet1.getCell(2, index).getContents());
						port.add(sheet1.getCell(3, index).getContents());
						os.add(sheet1.getCell(4, index).getContents());
						browser.add(sheet1.getCell(6, index).getContents());
						browserVersion.add(sheet1.getCell(7, index).getContents());
						osVersion.add(sheet1.getCell(5, index).getContents());
						sheetNumber.add(sheet1.getCell(8, index).getContents());
						//environment.add(sheet1.getCell(10, index).getContents());
						
					}
					
				}
				dd.w.close();
			}
			else
				
			{
				machineName.add(Common.getConfigProperty("MachineName"));
				machineNames.add(Common.getConfigProperty("MachineName"));
				host.add("");
				port.add("");
				os.add(Common.getConfigProperty("OsName"));
				browser.add(Common.getConfigProperty("BrowserName"));
				browserVersion.add(Common.getConfigProperty("BrowserVersion"));
				osVersion.add(Common.getConfigProperty("OsVersion"));
				sheetNumber.add(Common.getConfigProperty("TestDataSheetNo"));
			}
			noBrowser=machineName.size();
			
			
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = dbf.newDocumentBuilder();
			Document doc = docBuilder.newDocument();
			
			Element rootElement = doc.createElement("suite");
			doc.appendChild(rootElement);
			rootElement.setAttribute("name", "Suite");
			rootElement.setAttribute("parallel", "tests");
			rootElement.setAttribute("thread-count", "20");
			int id=1;			
			for(int brow=1;brow<=noBrowser;brow++)
			{								
				String idS=""+id++;
				Element test = doc.createElement("test");
				test.setAttribute("name", machineName.get(brow-1).toString());			
				test.setAttribute("id", idS);
				
				Element classes = doc.createElement("classes");
				classes.setAttribute("name", idS);
				
				Element classs = doc.createElement("class");
				classs.setAttribute("name", "TestCases.TestCases");	
				classs.setAttribute("id", idS);	
				
				Element methods = doc.createElement("methods");
				methods.setAttribute("id", idS);

				Element parameter0 = doc.createElement("parameter");
				parameter0.setAttribute("name", "selenium.machinename");
				parameter0.setAttribute("value", machineName.get(brow-1).toString());
				
				Element parameter1 = doc.createElement("parameter");
				parameter1.setAttribute("name", "selenium.host");
				parameter1.setAttribute("value", host.get(brow-1).toString());
				
				
				Element parameter2 = doc.createElement("parameter");
				parameter2.setAttribute("name", "selenium.port");
				parameter2.setAttribute("value", port.get(brow-1).toString());
				Element parameter3 = doc.createElement("parameter");
				parameter3.setAttribute("name", "selenium.browser");
				parameter3.setAttribute("value",browser.get(brow-1).toString());
				Element parameter4 = doc.createElement("parameter");
				parameter4.setAttribute("name", "selenium.url");
				parameter4.setAttribute("value", getConfigProperty("AppURL"));
				parameter4.setAttribute("id", idS);
				
				Element parameter5 = doc.createElement("parameter");
				parameter5.setAttribute("name", "selenium.os");
				parameter5.setAttribute("value", os.get(brow-1).toString());
				parameter5.setAttribute("id", idS);
				
				Element parameter6 = doc.createElement("parameter");
				parameter6.setAttribute("name", "selenium.browserVersion");
				parameter6.setAttribute("value", browserVersion.get(brow-1).toString());
				parameter6.setAttribute("id", idS);
				
				Element parameter7 = doc.createElement("parameter");
				parameter7.setAttribute("name", "selenium.osVersion");
				parameter7.setAttribute("value", osVersion.get(brow-1).toString());
				parameter7.setAttribute("id", idS);
				
				Element parameter8 = doc.createElement("parameter");
				parameter8.setAttribute("name", "TestData.SheetNumber");
				parameter8.setAttribute("value", sheetNumber.get(brow-1).toString());
				parameter8.setAttribute("id", idS);
				
				//Element parameter9 = doc.createElement("parameter");
				//parameter9.setAttribute("name", "TestData.Environment");
				//parameter9.setAttribute("value", environment.get(brow-1).toString());
				//parameter9.setAttribute("id", idS);
				
				rootElement.appendChild(test);
				test.appendChild(parameter0);
				test.appendChild(parameter1);
				test.appendChild(parameter2);
				test.appendChild(parameter3);
				test.appendChild(parameter4);		
				test.appendChild(parameter5);	
				test.appendChild(parameter6);
				test.appendChild(parameter7);
				test.appendChild(parameter8);
				//test.appendChild(parameter9);
				test.appendChild(classes);
				classes.appendChild(classs);
				classes.appendChild(methods);
				
				dd.useExcelSheet(Common.getConfigProperty("Tc_Settings_Excelpath"), 1);
				String [] sheetNames = dd.w.getSheetNames();
			    int sheetNo=0;
			    for (int i = 0 ; i < sheetNames.length ; i ++ ) {
			      
			      if(sheetNames[i].trim().toLowerCase().equalsIgnoreCase(Common.getConfigProperty("Test Suite")))
			      {
			    	  sheetNo=i;
			      }
			    }
				Sheet readsheet1 = dd.w.getSheet(sheetNo);
				//int ids=1;
				for (int i = 1; i < readsheet1.getRows(); i++) 
				{
					//String idss=""+ids++;
					String value = readsheet1.getCell(5, i).getContents();
					Keyword = readsheet1.getCell(3, i).getContents();
					description=readsheet1.getCell(4, i).getContents();
					level=readsheet1.getCell(2, i).getContents();
					if (value.trim().equalsIgnoreCase("Yes")) 
					{

						Element include = doc.createElement("include");
						methods.appendChild(include);
						
						//testCases.add(Keyword);
						include.setAttribute("name", Keyword);
						tcNames.add(Keyword);
						if (level.trim().equalsIgnoreCase("low")) 
						{
							lowTc.put(Keyword, level);
						}
						else if(level.trim().equalsIgnoreCase("medium"))
						{
							mediumTc.put(Keyword, level);
						}
						else if(level.trim().equalsIgnoreCase("hard"))
						{
							hardTc.put(Keyword, level);
						}
						else
						{
							System.out.println("Invalid Testcase priority level-"+level);
						}
						tcDescription.put(Keyword,description);
						
					} 
			   
					else if (value.trim().equalsIgnoreCase("No"))
					{
						Element exclude = doc.createElement("exclude");
						methods.appendChild(exclude);
						//String Keyword = readsheet1.getCell(0, i).getContents();
						exclude.setAttribute("name", Keyword);
						
					} 
					else 
					{
						if(!value.trim().equalsIgnoreCase(""))
						{
							System.out.println("Warnin!!Invalid/Empty Execution flag");
						}
						
					}
				}
			}
				TransformerFactory tff = TransformerFactory.newInstance();
			Transformer transformer = tff.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(
					"{http://xml.apache.org/xslt}indent-amount", "2");
			DOMSource xmlSource = new DOMSource(doc);
			StreamResult outputTarget = new StreamResult(
					"./src/main/resources/Config/testng.xml");
			transformer.transform(xmlSource, outputTarget);
			dd.w.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}