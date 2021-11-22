package utilities;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import automationFramework.GenericKeywords;

public class XMLReport {

	
	public DocumentBuilderFactory dbFactory;
	public DocumentBuilder dBuilder;
	public Document doc;

	//public Element element;
	
	public static void createXmlReport(String fileName,String machineName) {
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder;

			dBuilder = dbFactory.newDocumentBuilder();
			
			Document doc = dBuilder.newDocument();
			Element element = doc.createElement("Scenario");
			element.setAttribute("MachineName", machineName);
			doc.appendChild(element);
			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(fileName));
			transformer.transform(source, result);
			// Output to console for testing
			//StreamResult consoleResult = new StreamResult(System.out);
			//transformer.transform(source, consoleResult);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void openXmlReport(String fileName)
	{
		try
		{
			this.dbFactory = DocumentBuilderFactory.newInstance();
			this.dBuilder = this.dbFactory.newDocumentBuilder();
			this.doc = this.dBuilder.parse(new File(fileName));
			this.doc.getDocumentElement().normalize();
			GenericKeywords.writeToLogFile("INFO", "XML file is opened");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void saveXmlReport(String fileName)
	{
		try
		{
			TransformerFactory transformerFactory;
			Transformer transformer;
			transformerFactory = TransformerFactory.newInstance();
			transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			DOMSource source = new DOMSource(this.doc);
			StreamResult result = new StreamResult(new File(fileName));
			transformer.transform(source, result);
			GenericKeywords.writeToLogFile("INFO", "XML file is saved and closed");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void createTestCaseNode(String fileName,String testCaseName,String description)
	{
		Boolean flag = false;
		openXmlReport(fileName);
		try
		{
			Node node = this.doc.getElementsByTagName("Scenario").item(0);
			Element element = (Element) node;
			NodeList nodeList = element.getChildNodes();
			
			if(nodeList.getLength() == 0)
			{
				flag = false;
			}
			else
			{
				for (int i=0;i<nodeList.getLength();i++)
				{
					if(nodeList.item(i).getNodeName().equals("TestCase"))
					{
						Element ele = (Element) nodeList.item(i);
						if(ele.getAttribute("id").equals(testCaseName))
						{
							flag = true;
						}
					}
				}
			}
			if (!flag)
			{
				Element testCase = this.doc.createElement("TestCase");
				testCase.setAttribute("id", testCaseName);
				testCase.setAttribute("description", description);
				element.appendChild(testCase);
				//this.doc.appendChild(element);
				GenericKeywords.writeToLogFile("INFO", "Test Case tag is created");
			}	
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		saveXmlReport(fileName);
	}
	
	public void createTestStep(String fileName,String testCaseName,String status,String innerText,String screenShotName, String link)
	{
		openXmlReport(fileName);
		try
		{
			Node node = this.doc.getElementsByTagName("Scenario").item(0);			
			NodeList nodeList = node.getChildNodes();
			if(nodeList.getLength() == 0)
			{
				saveXmlReport(fileName);
				return;
			}
			else
			{
				for (int i=0;i<nodeList.getLength();i++)
				{
					
					if(nodeList.item(i).getNodeName().equals("TestCase"))
					{	
						Element element = (Element) nodeList.item(i);
						if (element.getAttribute("id").equals(testCaseName))
						{
							Element testStep = this.doc.createElement("TestStep");
							testStep.setAttribute("Status", status);
							testStep.setAttribute("Message",innerText);
							testStep.setAttribute("ScreenShotName", screenShotName);
							testStep.setAttribute("Link", link);
							element.appendChild(testStep);
							GenericKeywords.writeToLogFile("INFO", "Test step is created");
							break;
						}
					}
				}
			}
			
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		saveXmlReport(fileName);
	}
	
	public void transformXsltToHtmlPage(String location,String fileName,String testCaseName) {
		try {

			//GenericKeywords.writeToLogFile("INFO", "Input File"+ location+"/"+fileName+".xml");
			//GenericKeywords.writeToLogFile("INFO", "Output File"+ location+"/"+testCaseName+".html");
			PrintWriter pw = new PrintWriter(new FileWriter(location+"/"+testCaseName+".html"));
			Source input = new StreamSource(location+"/"+fileName+".xml");
			Source xsl = new StreamSource(GenericKeywords.XSLT_FILE_ReportPage);
			Result output = new StreamResult(pw);
			TransformerFactory factory = TransformerFactory.newInstance();
			Transformer transformer = factory.newTransformer(xsl);
			transformer.setParameter("testCaseID", testCaseName);
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.transform(input, output);
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
