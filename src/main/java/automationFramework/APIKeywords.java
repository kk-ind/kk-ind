package automationFramework;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import baseClass.BaseClass;

import java.util.Properties;


public class APIKeywords extends DBKeywords{
	public String SOAPUrl = "";
	public String xmlFile2Send = "";
	public String fileName = "";
	public String response;
	public String timeStamsIridium;
	public String signatureIridium;
	public String methodNameIridium;
	Properties property = new Properties();
	public String requestResponseFile = "";
	public BufferedWriter requestWrite = null;
	public HttpURLConnection httpConnection = null;
	public byte[] xmlRequestBuffer = null;
	public OutputStream xmlResponseBuffer = null;
	public long startTime = 0;
	public long endTime = 0;

	public URLConnection urlConnection = null;

	public APIKeywords(BaseClass obj) {
		// TODO Auto-generated constructor stub
		super(obj);
	}
	public APIKeywords()
	{

	}

	public void apiGETRequest(String url) {

		validateResponse(url);
		// for JSON
		if (response == "json") {
			fileName=fileName.concat("api.json");
			String line;
			StringBuffer jsonString = new StringBuffer();
			try {
				String payload = "";
				URL getUrl = new URL(url);
				testStepPassed("Send get request url:"+url);
				HttpURLConnection connection = (HttpURLConnection) getUrl.openConnection();

				connection.setDoInput(true);
				connection.setDoOutput(true);
				connection.setRequestMethod("GET");
				connection.setRequestProperty("Accept", "application/json");
				connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

				OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
				writer.write(payload);
				writer.close();
				BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				while ((line = br.readLine()) != null) {
					jsonString.append(line);
				}
				br.close();
				String responseContent = connection.getContentType();
				int responseCode = connection.getResponseCode();
				testStepPassed("GET Response Code :: " + responseCode);
				testStepPassed("GET Response Type :: " + responseContent);
				connection.disconnect();
				if (responseCode == 200) {

					testStepPassed("apiGETRequest :: Success");
					testStepPassed("Response::"+jsonString);
					store(jsonString);
				} else if (responseCode >= 400 && responseCode <= 499) {
					//testStepFailed("Client Request Errors,","" , false);
					if (responseCode == 404) {
						//testStepFailed("apiGETRequest :: Page not found","" , false);
					}
				} else if (responseCode >= 500 && responseCode <= 599) {
					if (responseCode == 503) {
						//testStepFailed("apiGETRequest :: Service Unavailable","",false);
					}
					//testStepFailed("apiGETRequest :: Server Errors","",false);
				} else {
					//testStepFailed("apiGETRequest :: undefined response","",false);
				}
				connection.disconnect();
			} catch (Exception e) {
				//testStepFailed("",e.getMessage(),false);
			}
			writeToLogFile("INFO", jsonString.toString());
		}

		// for XML
		else if (response == "xml") {
			fileName=fileName.concat("api.xml");
			String line;
			StringBuffer xmlString = new StringBuffer();
			try {
				String payload = "";
				URL getUrl = new URL(url);
				HttpURLConnection connection = (HttpURLConnection) getUrl.openConnection();

				connection.setDoInput(true);
				connection.setDoOutput(true);
				connection.setRequestMethod("GET");
				connection.setRequestProperty("Accept", "application/xml");
				connection.setRequestProperty("Content-Type", "application/xml; charset=UTF-8");
				testStepPassed("Send request url to server "+url);
				OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
				writer.write(payload);
				writer.close();
				BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				while ((line = br.readLine()) != null) {
					xmlString.append(line);
				}
				br.close();
				String responseContent = connection.getContentType();
				int responseCode = connection.getResponseCode();
				testStepPassed("GET Response Code :: " + responseCode);
				testStepPassed("GET Response Type :: " + responseContent);
				connection.disconnect();
				if (responseCode == 200) {
					testStepPassed("apiGETRequest :: Success");
					testStepPassed("Response stored in data/api.xml file");
					store(xmlString);
				} else if (responseCode >= 400 && responseCode <= 499) {
					//testStepFailed("Client Request Errors,","",false);
					if (responseCode == 404) {
						//testStepFailed("apiGETRequest :: Page not found","",false);
					}
				} else if (responseCode >= 500 && responseCode <= 599) {
					if (responseCode == 503) {
						//testStepFailed("apiGETRequest :: Service Unavailable","",false);
					}
					//testStepFailed("apiGETRequest :: Server Errors","",false);
				} else {
					//testStepFailed("apiGETRequest :: undefined response","",false);
				}
				connection.disconnect();
			} catch (Exception e) {
				//testStepFailed("",e.getMessage(),false);
			}
			writeToLogFile("INFO", xmlString.toString());

		}
	}

	public void apiPostRequest(String postUrl) {
		try {
			URL url = new URL(postUrl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			testStepPassed("Send post request url to server:"+url);
			String responseContent = connection.getContentType();
			int responseCode = connection.getResponseCode();
			testStepPassed("GET Response Code :: " + responseCode);
			testStepPassed("GET Response Type :: " + responseContent);
			connection.disconnect();
			if (responseCode == 200) {
				testStepPassed("apiPostRequest :: Success");
			} else if (responseCode >= 400 && responseCode <= 499) {
				//testStepFailed("apiPostRequest :: Client Request Errors","",false);
				if (responseCode == 404) {
					//testStepFailed("apiPostRequest :: Page not found","",false);
				}
			} else if (responseCode >= 500 && responseCode <= 599) {
				writeToLogFile("INFO", "apiPostRequest :: Server Errors");
				if (responseCode == 503) {
					//testStepFailed("apiPostRequest :: Service Unavailable","",false);
				}
			} else {
				//testStepFailed("apiPostRequest :: undefined response","",false);
			}
		} catch (Exception e) {
			//testStepFailed("",e.getMessage(),false);
		}

	}

	public  void apiRetrieveKeyValue(String tagName, String attribute) throws ParserConfigurationException,
	SAXException, IOException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document document = db.parse(new File(fileName));
		NodeList nodeList = document.getElementsByTagName(tagName);
		ArrayList<String> hi = new ArrayList<String>();
		for (int x = 0, size = nodeList.getLength(); x < size; x++) {
			try {

				hi.add(nodeList.item(x).getAttributes().getNamedItem(attribute).getNodeValue());


			} catch (Exception e) {
				writeToLogFile("INFO", "No such attribute in " + x + " index");
			}
		}
		System.out.println("");
		for (String abc : hi) {
			writeToLogFile("INFO", abc);
			testStepPassed("Attribute["+attribute+"] for Tag["+tagName+"] is "+abc);
		}

	}

	public  void apiRetrieveKeyValue(String tagName) throws SAXException, IOException,
	ParserConfigurationException {

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		// Document document = builder.parse(new InputSource(new
		// StringReader(xml)));
		Document document = builder.parse(new File(fileName));
		Element rootElement = document.getDocumentElement();
		ArrayList<String> str = new ArrayList<String>();
		NodeList nodeList = rootElement.getElementsByTagName(tagName);
		String temp = "";
		for (int i = 0; i < nodeList.getLength(); i++) {
			str.add(getTextValue(nodeList.item(i)));
		}

		for (String abc : str) {
			writeToLogFile("INFO", abc);
			temp = temp+" "+abc;
		}
		testStepPassed("List of values in tag["+tagName+"] are "+temp);

		// writeToLogFile("Info", "TagName :: "+tagName+ " 	Value :: "+
		// getTextValuesByTagName(rootElement, tagName));

	}

	public  boolean apiValidateKeyValue(String tagName, String attribute, String value)
			throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document document = db.parse(new File(fileName));
		NodeList nodeList = document.getElementsByTagName(tagName);
		ArrayList<String> hi = new ArrayList<String>();
		for (int x = 0, size = nodeList.getLength(); x < size; x++) {
			try {

				hi.add(nodeList.item(x).getAttributes().getNamedItem(attribute).getNodeValue());
			} catch (Exception e) {
				writeToLogFile("INFO", "No such attribute in " + x + " index");
			}
		}

		if (hi.contains(value)) {
			return true;
		} else {
			return false;
		}
	}

	public  boolean apiValidateKeyValue(String tagName, String value) throws SAXException, IOException,
	ParserConfigurationException {

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		// Document document = builder.parse(new InputSource(new
		// StringReader(xml)));
		Document document = builder.parse(new File(fileName));
		Element rootElement = document.getDocumentElement();
		ArrayList<String> str = new ArrayList<String>();
		NodeList nodeList = rootElement.getElementsByTagName(tagName);
		for (int i = 0; i < nodeList.getLength(); i++) {
			str.add(getTextValue(nodeList.item(i)));
		}

		if (str.contains(value)) {
			return true;
		} else {
			return false;
		}
	}

	public  void getAllAttributes(String tagName) throws ParserConfigurationException, FileNotFoundException,
	SAXException, IOException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setValidating(false);
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(new FileInputStream(new File(fileName)));
		NodeList entries = doc.getElementsByTagName(tagName);
		int num = entries.getLength();

		for (int i = 0; i < num; i++) {
			Element node = (Element) entries.item(i);
			listAllAttributes(node);
		}
	}

	public  void getElements(String tagName) {
		try {
			File file = new File(fileName);
			if (file.exists()) {
				// Create a factory
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				// Use the factory to create a builder
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document doc = builder.parse(fileName);
				// Get a list of all elements in the document
				NodeList list = doc.getElementsByTagName(tagName);
				writeToLogFile("INFO", "XML Elements: ");
				for (int i = 0; i < list.getLength(); i++) {
					// Get element
					Element element = (Element) list.item(i);
					writeToLogFile("INFO", element.getNodeName());
				}
				testStepPassed("TagName is retrieved");
			} else {
				//testStepFailed("File not found!","",false);
			}
		} catch (Exception e) {
			System.exit(1);
		}
	}

	public  void apiValidateResponseCount(int expOut, String tagName) {
		try {
			File file = new File(fileName);
			if (file.exists()) {
				// Create a factory
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				// Use the factory to create a builder
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document doc = builder.parse(fileName);
				// Get a list of all elements in the document
				NodeList list = doc.getElementsByTagName(tagName);
				writeToLogFile("INFO", "XML Elements Count: " + list.getLength());
				if (expOut == list.getLength())
					testStepPassed("Count["+expOut+"] Equal for tag["+tagName+"]");
				else if (expOut < list.getLength())
					testStepPassed("Count["+list.getLength()+"] Less Than given["+expOut+"] number for tag["+tagName+"]");
				else if (expOut > list.getLength())
					testStepPassed("Count["+list.getLength()+"] Exceeds given["+expOut+"] number for tag["+tagName+"]");
				//else
				//testStepFailed("Tag["+tagName+"] does not Exist in File.");
			} else {
				//testStepFailed("File not found!","",false);
			}
		} catch (Exception e) {
			System.exit(1);
		}
	}

	public  void apiResposeCount(String target) {
		try {
			// BufferedReader bf = new BufferedReader(new
			// InputStreamReader(System.in));
			// System.out.print("Enter XML File name: ");
			// String xmlFile = bf.readLine();
			File file = new File(fileName);
			if (file.exists()) {
				// Create a factory
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				// Use the factory to create a builder
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document doc = builder.parse(fileName);
				// Get a list of all elements in the document
				NodeList list = doc.getElementsByTagName(target);
				writeToLogFile("INFO", "XML Elements: ");
				for (int i = 0; i < list.getLength(); i++) {
					// Get element
					Element element = (Element) list.item(i);
					writeToLogFile("INFO", element.getNodeName());

				}
				writeToLogFile("INFO", "Count :: " + list.getLength());
				testStepPassed("api Response count Passed");
			} else {
				//testStepFailed("File not found!","",false);
			}
		} catch (Exception e) {
			System.exit(1);
		}
	}

	public  void apiTagCount(String tagName) {
		try {
			// BufferedReader bf = new BufferedReader(new
			// InputStreamReader(System.in));
			// System.out.print("Enter XML File name: ");
			// String xmlFile = bf.readLine();
			File file = new File(fileName);
			if (file.exists()) {
				// Create a factory
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				// Use the factory to create a builder
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document doc = builder.parse(fileName);
				// Get a list of all elements in the document
				NodeList list = doc.getElementsByTagName(tagName);
				writeToLogFile("INFO", "XML Elements: ");
				for (int i = 0; i < list.getLength(); i++) {
					// Get element
					Element element = (Element) list.item(i);
					writeToLogFile("INFO", element.getNodeName());

				}
				writeToLogFile("INFO", "Count :: " + list.getLength());
				testStepPassed("tag["+tagName+"] count: "+list.getLength());
			} else {
				//testStepFailed("File not found!","",false);
			}
		} catch (Exception e) {
			System.exit(1);
		}
	}

	// XML Supporting method
	public  String getTextValue(Node node) {
		StringBuffer textValue = new StringBuffer();
		int length = node.getChildNodes().getLength();
		for (int i = 0; i < length; i++) {
			Node c = node.getChildNodes().item(i);
			if (c.getNodeType() == Node.TEXT_NODE) {
				textValue.append(c.getNodeValue());
			}
		}
		return textValue.toString().trim();
	}

	public  void listAllAttributes(Element element) {

		writeToLogFile("INFO", "Tag: " + element.getNodeName());
		// get a map containing the attributes of this node
		NamedNodeMap attributes = element.getAttributes();
		// get the number of nodes in this map
		int numAttrs = attributes.getLength();

		for (int i = 0; i < numAttrs; i++) {
			Attr attr = (Attr) attributes.item(i);
			String attrName = attr.getNodeName();
			String attrValue = attr.getNodeValue();
			writeToLogFile("INFO", "Attribute name: " + attrName + " Attribute value: " + attrValue);

		}
		testStepPassed("Get all attributes :: Passed");
	}

	// Common Supporting methods
	public  void validateResponse(String Url) {

		try {
			URL url = new URL(Url);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			String responseContent = connection.getContentType();
			testStepPassed("Send Url to server:"+url);
			if (responseContent.equals("application/json; charset=utf-8")) {
				testStepPassed("GET Response Content :: " + responseContent);
				response = "json";
			} else if (responseContent.equals("application/xml; charset=utf-8")) {
				testStepPassed("GET Response Content :: " + responseContent);
				response = "xml";
			}  else if (responseContent.equals("text/xml; charset=utf-8")) {
				testStepPassed("GET Response Content :: " + responseContent);
				response = "xml";
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}

	}

	public  void writeToFile(String pFilename, StringBuffer pData) throws IOException {
		BufferedWriter out = new BufferedWriter(new FileWriter(pFilename));
		out.write(pData.toString());
		out.flush();
		out.close();
	}

	public  void store(StringBuffer jsonOrXMLString) {
		try {
			File file = new File(fileName);
			if (file.createNewFile()) {
				writeToLogFile("INFO", "File is created!");
			} else {
				writeToLogFile("INFO", "File already exists.");
			}
			writeToFile(fileName, jsonOrXMLString);

		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
		}

	}

	public String convert(String json, String root)
	{
		String xml = null;
		try
		{
			org.json.JSONObject jsonFileObject = new org.json.JSONObject(json);
			xml = "<"+root+">" + org.json.XML.toString(jsonFileObject) + "</"+root+">";
		}catch(Exception e)
		{
			e.printStackTrace();
			//testStepFailed("",e.toString(),false);
		}        
		return xml;
	}

	public String readFile(String filepath)
	{
		StringBuilder sb = null;
		try
		{
			sb = new StringBuilder();
			InputStream in = new FileInputStream(filepath);
			Charset encoding = Charset.defaultCharset();

			Reader reader = new InputStreamReader(in, encoding);

			int r = 0;
			while ((r = reader.read()) != -1)//Note! use read() rather than readLine()
				//Can process much larger files with read()
			{
				char ch = (char) r;
				sb.append(ch);
			}

			in.close();
			reader.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			//testStepFailed("",e.toString(),false);
		}  

		return sb.toString();
	}

	public void writeFile(String filepath, String output)
	{

		try 
		{
			FileWriter ofstream = new FileWriter(filepath);
			BufferedWriter out = new BufferedWriter(ofstream);
			out.write(output);
			out.close();

		}
		catch(Exception e)
		{
			e.printStackTrace();
			//testStepFailed("",e.toString(),false);
		} 
	}

	
}
