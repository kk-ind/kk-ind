package utilities;





import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.simple.JSONValue;
import org.testng.IExecutionListener;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

import automationFramework.GenericKeywords;




public class HtmlReport extends TestListenerAdapter implements IExecutionListener  {

	public static int couchDBDocRows=1;
	public static String backgroudColour="";
	public int totalTCSuccess=0;
	public int totalTCFail=0;
	public int totalTCSkip=0;
	String projectName="";
	String clientLogoPath="/src/main/resources/Logos/client_logo.jpg";
	String indiumSoftwareLogo="/src/main/resources/Logos/Indium-Software-Logo.jpg";
	long totalDurationInMillis=0;
	String totalDuration="";
	String buildNo="";
	String machine="";
	String os="";
	String osVersion="";
	String browser="";
	String browserVersion="";
	int totalTC=PropertiesFile.tcNames.size();
	int totalPassPercentage=0;
	int totalFailPercentage=0;
	int totalSkipPercentage=0;
	static long totalGridDuration=0;
	public HashMap<String, String> tcStatus = new HashMap<String, String>();
	public HashMap<String, String> tcDuration = new HashMap<String, String>();
	public static HashMap<String, String> operatingSytsem = new HashMap<String, String>();
	public static HashMap<String, String> operatingSytsemVersion = new HashMap<String, String>();
	public static HashMap<String, String> browserName = new HashMap<String, String>();
	public static HashMap<String, String> browserVersionName = new HashMap<String, String>();
	public static HashMap<String, String> totExecutedInOs = new HashMap<String, String>();
	public static HashMap<String, String> totPassedInOS = new HashMap<String, String>();
	public static HashMap<String, String> totFailedInOS = new HashMap<String, String>();
	public static HashMap<String, String> totSkippedInOS = new HashMap<String, String>();
	public static HashMap<String, String> totDurationInOS = new HashMap<String, String>();
	int i=1;
	int lowTcFailure=0;
	int mediumTcFailure=0;
	int hardTcFailure=0;
	
	public static String dasboard = null;
	
	public String getCurrentDate()
	{
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		   //get current date time with Date()
		   Date date = new Date();
		  return dateFormat.format(date);
	}
	public String addTblRowInSummaryPage()
	{
		
		String temp="";
		 String a="";
		 int sNo=1;
		 try {
			
			 for(String tcName : PropertiesFile.tcNames)
			 {
				 if(tcDuration.containsKey(tcName))
				 {
					 String tdStatus="";
					 if(tcStatus.get(tcName).equalsIgnoreCase("fail"))
					 {
						 tdStatus= "<TD style='color:red'>"+tcStatus.get(tcName).replace("null", "pending")+"</TD>";
					 }
					 if(tcStatus.get(tcName).equalsIgnoreCase("pass"))
					 {
						 tdStatus= "<TD style='color:green'>"+tcStatus.get(tcName).replace("null", "pending")+"</TD>";
					 }
					 if(tcStatus.get(tcName).equalsIgnoreCase("skip"))
					 {
						 tdStatus= "<TD style='color:blue'>"+tcStatus.get(tcName).replace("null", "pending")+"</TD>";
					 }
				 temp="<TR><TD>"+sNo+"</TD><TD><a href='./Resultfiles/"+tcName+".html' target=''>"+tcName+"</a></TD><TD>      "+PropertiesFile.tcDescription.get(tcName)+"    </TD><td>"+tcDuration.get(tcName).replace("null", "pending")+"</td>"+tdStatus+"</TR>";
				 }
				 else
				 {
					 temp="<TR><TD>"+sNo+"</TD><TD><a href='./Resultfiles/"+tcName+".html' target=''>"+tcName+"</a></TD><TD>      "+PropertiesFile.tcDescription.get(tcName)+"    </TD><td>pending</td><TD>pending</TD></TR>";
						
				 }
				 a=a+temp;
				 dasboard = dasboard+temp;
				 sNo++;
			 }
	        } catch (Exception e) {
	            // ...
	        } 
		return a;
		
	}
	
	public String addTblRowInReportPage()
	{
		
		String temp="";
		 String a="";
		 int sNo = 1;
		 try {
			 
			 for(String machineName : PropertiesFile.machineNames)
			 {
				 if(operatingSytsem.containsKey(machineName))
				 {
					 temp="<TR><TD><a href='"+getTestSuitePath(machineName.replace(" ", ""))+"'>"+machineName+"</a></TD><TD>"+operatingSytsem.get(machineName)+"</TD><TD>"+operatingSytsemVersion.get(machineName)+"</TD><TD>"+browserName.get(machineName)+"</TD><TD>"+browserVersionName.get(machineName)+"</TD><td>"+totExecutedInOs.get(machineName)+"</td><TD>"+totPassedInOS.get(machineName)+"</TD><TD>"+totFailedInOS.get(machineName)+"</TD><TD>"+totSkippedInOS.get(machineName)+"</TD><TD>"+totDurationInOS.get(machineName)+"</TD></TR>";
					 
				 }
				 else
				 {
					 temp="<TR><TD><a href='"+getTestSuitePath(machineName.replace(" ", ""))+"'>"+machineName+"</a></TD><TD>pending</TD><TD>pending</TD><TD>pending</TD><td>pending</td><td>pending</td><TD>pending</TD><TD>pending</TD><TD>pending</TD><TD>pending</TD></TR>";
						 
				 }
				 a=a+temp;
				 sNo++;
			 }
	        } catch (Exception e) {
	            // ...
	        } 
		return a;
		
	}
	
	public String addPieChartJsScriptInHtml()
	{
		String script="<script type='text/javascript'>window.onload = function () {	var chart = new CanvasJS.Chart('chartContainer',	{	backgroundColor: '"+backgroudColour+"',		title:{			text: ''"
				+ "		},                animationEnabled: true,		legend:{			verticalAlign: 'center',			horizontalAlign: 'Bottom',			fontSize: 20,			fontFamily: 'Helvetica'"
				+ "		},		theme: 'theme2',		data: [		{			type: 'pie',			indexLabelFontFamily: 'Garamond',			indexLabelFontSize: 20,	indexLabel: '{label} {y}%',"
				+ "			startAngle:-20,			showInLegend: true,	toolTipContent:'{legendText} {y}%',			dataPoints: [				{  y: "+totalSkipPercentage+", legendText:'Skip', label: 'Skip' },"
				+ "				{  y: "+totalFailPercentage+", legendText:'Fail', label: 'Fail' },				{  y: "+totalPassPercentage+", legendText:'Pass', label: 'Pass' },			]		}		]	});	chart.render();}</script><script type='text/javascript' src='../Extras/canvasjs.min.js'></script>";
		return script;
	}
	

	public String addBarChartInReportHtml()
	{
		
		String script="<script type='text/javascript'>	window.onload = function () {		var chart = new CanvasJS.Chart('chartContainer',		{		backgroundColor: '"+backgroudColour+"',			theme: 'theme3',                        animationEnabled: true,			title:{				text: '"+addTitleInBarChart()+"',				fontSize: 30			},			toolTip: {				shared: true			},			axisY: {				title: ''			},			axisY2: {				title: ''			},					data: [ 			{"
				+ "			type: 'column',					name: 'Pass',				legendText: 'Pass',				showInLegend: true, 				"+addBarChartDetails("pass")+"			},		{				type: 'column',					name: 'Fail',				legendText: 'Fail',				axisYType: 'secondary',				showInLegend: true,				"+addBarChartDetails("fail")+"			},			{				type: 'column',					name: 'Skip',				legendText: 'Skip',				axisYType: 'secondary',				showInLegend: true,				"+addBarChartDetails("skip")+"			}"
								+ "			],          legend:{            cursor:'pointer',            itemclick: function(e){              if (typeof(e.dataSeries.visible) === 'undefined' || e.dataSeries.visible) {              	e.dataSeries.visible = false;              }              else {                e.dataSeries.visible = true;              }            	chart.render();            }          },        });chart.render();}</script>  <script type='text/javascript' src='./Extras/canvasjs.min.js'></script></script>";		return script;
	}
	private static String addTitleInBarChart()
	{
		String titleTXt="";
		if(GenericKeywords.getConfigProperty("Last runs results in Barchart").toLowerCase().contains("yes"))
		{
			titleTXt="Last "+couchDBDocRows+" runs results";
		}
		else if(GenericKeywords.getConfigProperty("GridExecution").toLowerCase().contains("yes"))
		{
			titleTXt="Grid Execution Status";
		}
		else
		{
			titleTXt="Execution Status";
		}
		return titleTXt;
	}
	private static String addBarChartDetails(String status)
	{
		String temp="";
		 String a="";
		 try {
			 for(String machineName : PropertiesFile.machineNames)
			 {
				 if(operatingSytsem.containsKey(machineName))
				 {
					 if(status.equalsIgnoreCase("pass"))
					 {
						 temp="{label: '"+machineName+"', y: "+totPassedInOS.get(machineName)+"},";
					 }
					 else if(status.equalsIgnoreCase("fail"))
					 {
						 temp="{label: '"+machineName+"', y: "+totFailedInOS.get(machineName)+"},";
					 }
					 else if(status.equalsIgnoreCase("skip"))
					 {
						 temp="{label: '"+machineName+"', y: "+totSkippedInOS.get(machineName)+"},";
					 }
					 else
					 {
						 System.out.println("Invalid status--"+status);
					 }
					 a=a+temp;
				 }
				
			 }
	        } catch (Exception e) {
	            // ...
	        } 
		return "dataPoints:["+a+"]";
	}
	/*private static String addBarChartDetails(String status)
	{
		
		String temp="";
		 String a="";
		 
		 try {


	           DefaultHttpClient httpclient = new DefaultHttpClient();

	           HttpGet get = new HttpGet("http://localhost:5984/executionstatus/_design/couchview/_view/javalanguage");

	           HttpResponse response = httpclient.execute(get);

	           HttpEntity entity=response.getEntity();

	           InputStream instream = entity.getContent();

	           BufferedReader reader = new BufferedReader(new InputStreamReader(instream));

	           String strdata = null;

	           String jsonString = "" ;

	           while( (strdata =reader.readLine())!=null)

	           {

//	                   System.out.println(strdata);

	                  jsonString += strdata;

	           }

	           System.out.println("Json String: " + jsonString);

	           Map<String, Object> jsonMap = getMapFromJsonString(jsonString);

	           if(jsonMap!=null)

	           {

	               System.out.println("total_rows: " + jsonMap.get("total_rows"));

	               System.out.println("offset: " + jsonMap.get("offset"));

	               List<Map> rowsList = (List<Map>) jsonMap.get("rows");

	               if(rowsList!=null)

	               {
	            	   couchDBDocRows=rowsList.size();
	            	   for(Map row: rowsList)
	      			 {
	            		   Map values=(Map) ((Map)row.get("value")).get("Status");
	                       String[] value=values.values().toString().replace("[", "").replace("]", "").split(",");
	      					 if(status.equalsIgnoreCase("pass"))
	      					 {
	      						 temp="{label: '"+row.get("id")+"', y: "+value[1].replace("Passed", "")+"},";
	      					 }
	      					 else if(status.equalsIgnoreCase("fail"))
	      					 {
	      						 temp="{label: '"+row.get("id")+"', y: "+value[2].replace("Failed", "")+"},";
	      					 }
	      					 else if(status.equalsIgnoreCase("skip"))
	      					 {
	      						 temp="{label: '"+row.get("id")+"', y: "+value[3].replace("Skipped", "")+"},";
	      					 }
	      					 else
	      					 {
	      						 System.out.println("Invalid status--"+status);
	      					 }
	      					 a=a+temp;
	      				
	      				
	      			 }

	                   

	               }

	           }

	       
			
	        } catch (Exception e) {
	            // ...
	        } 
		return "dataPoints:["+a+"]";
	}*/
	public  void createCoverPageHtmlReport(String path)
	{
		
		try{
			PrintWriter writer = new PrintWriter(path);
			writer.print("");
			writer.close();
			//Writer output;
			BufferedWriter output;
			output = new BufferedWriter(new FileWriter(path,true));  //clears file every time
			output.write("<html>"+addPieChartJsScriptInHtml() +addCssLink()+"<title>HTML REPORTING</title>" +"<body><div class='container'>"+
			"<div id='header'><img src='../Logos/client_logo.jpg' class='clientLogo' width='100' height='100' /><img src='../Logos/Indium-Software-Logo.jpg' class='indiumLogo' width='180' height='110'/><h1>"+projectName+"</h1>"+			
			"</div><div id='ddb'><p><strong>Date: </strong>"+" "+getCurrentDate()+"<span><strong>Duration: </strong>"+" "+totalDuration+"</span><strong>Build no: </strong>"+" "+buildNo+"</p></div><div id='mob'>"+
			"<p><strong>Machine Name: </strong>"+" "+machine+"<span><strong>Operating System: </strong> "+" "+os+"</span><strong>Browser/App-Name: </strong>"+" "+browser+"</p></div><div id='chartContainer' style='height: 300px; width: 100%;'></div><div id='execstatuscover'>"	+			
			"<table class='column-options'>"+
			"<tr><th colspan='8' style='width:100%; text-align:center'>Test Case Execution Status</th></tr><tr><th>Executed</th><th>Passed</th><th>Failed</th><th>Skipped</th></tr>"+
			"<tr><td>"+totalTC+"</td><td>"+totalTCSuccess+"</td><td>"+totalTCFail+"</td><td>"+totalTCSkip+"</td></tr><tr class='odd'><td>Percentage</td><td>"+totalPassPercentage+"%"+"</td><td>"+totalFailPercentage+"%"+"</td><td>"+totalSkipPercentage+"%"+"</td></tr></table>"+
			"<table class='column-options'><tr><th colspan='8' style='width:100%; text-align:center'>Severity Wise Failure Status</th></tr><tr><th>Low TC Failure</th><th>Medium TC Failure</th><th>High TC Failure</th></tr><tr><td>"+lowTcFailure+"</td><td>"+mediumTcFailure+"</td><td>"+hardTcFailure+"</td></tr></table>"+
			"</div><div id='buttonsar'><a class='btn' href='SummaryPage.html'>Click here to see the summary report</a>&nbsp<a class='btn' href='../Report.html'>Click here to see the dashboard report</a></div></div></body></html>");
			output.close();
		}
		catch(Exception e)
		{
			System.out.println(e.toString());
		}
	}
	public String addReportLink()
	{
		String txt="";
		/*if(PropertiesFile.getConfigProperty("GridExecution").toLowerCase().contains("yes"))
		{*/
			txt="<br><a href='./../Report.html'>Click here to see the report page</a>";
		//}
		return txt;
	}
	public static String addCssLink()
	{
		String txt="";
		if(!(PropertiesFile.getConfigProperty("CssStyle").toLowerCase().contains("nostyle")))
		{
			txt="<link rel='stylesheet' type='text/css' href='../Extras/style.css'></br>";
		}
		return txt;
	}
	
	public  void createSummaryPageHtmlReport(String path)
	{
		try{
			PrintWriter writer = new PrintWriter(path);
			writer.print("");
			writer.close();
			//Writer output;
			BufferedWriter output;
			output = new BufferedWriter(new FileWriter(path,true));  //clears file every time
			output.write("<html>"+addPieChartJsScriptInHtml() +addCssLink()+"<title>HTML REPORTING</title>" +"<body><div class='container'>"+
					"<div id='header'><img src='../Logos/client_logo.jpg' class='clientLogo' width='100' height='100' /><img src='../Logos/Indium-Software-Logo.jpg' class='indiumLogo' width='180' height='110'/><h1>"+projectName+"</h1>"+			
					"</div><div id='ddb'><p><strong>Date: </strong>"+" "+getCurrentDate()+"<span><strong>Duration: </strong>"+" "+totalDuration+"</span><strong>Build no: </strong>"+" "+buildNo+"</p></div><div id='mob'>"+
					"<p><strong>Machine Name: </strong>"+" "+machine+"<span><strong>Operating System: </strong> "+" "+os+"</span><strong>Browser/App-Name: </strong>"+" "+browser+"</p></div><div id='chartContainer' style='height: 300px; width: 100%;'></div>"
							+ "<div id='execstatuscover'>"	+			
					"<table class='column-options'>"+
					"<tr><th colspan='8' text-align:center'>Test Case Execution Status</th></tr><tr><th>Executed</th><th>Passed</th><th>Failed</th><th>Skipped</th></tr>"+
					"<tr><td>"+totalTC+"</td><td>"+totalTCSuccess+"</td><td>"+totalTCFail+"</td><td>"+totalTCSkip+"</td></tr><tr class='odd'><td>Percentage</td><td>"+totalPassPercentage+"%"+"</td><td>"+totalFailPercentage+"%"+"</td><td>"+totalSkipPercentage+"%"+"</td></tr>"
							+ "</table>"+
					"<table class='column-options' align='left'>"
					+ "<tr><th colspan='8' text-align:center'>Severity Wise Failure Status</th></tr>"
					+ "<tr><th>Low TC Failure</th><th>Medium TC Failure</th><th>High TC Failure</th></tr>"
					+ "<tr><td>"+lowTcFailure+"</td><td>"+mediumTcFailure+"</td><td>"+hardTcFailure+"</td></tr>"
							+ "</table>"+
					"</div><div id='exectable' style='clear:both;'>"
					+ "<table class='column-options'>"
					+ "<tr><th>Test case no</th><th>Test case name</th><th>Test case description</th><th>Duration</th><th>Status</th></tr>"
					+ ""+addTblRowInSummaryPage()+
					"</table>"
					+ "</div>"
					/*+ "<div id='execstatuscover'>"			
					+ "<table class='column-options'>"
					+ "<tr><th colspan='3'>Failure Summary</th></tr><tr><td>No.Of valid failures</td><td>"+GenericKeywords.totValidFailures+"</td><td><a href='./FailurePage.html'>click here</a></td> </tr><tr><td>No.of exeception failures</td><td>"+GenericKeywords.totExceptionFailures+"</td><td><a href='./FailurePage.html'>click here</a></td> </tr>"
							+ "</table></div>"*/
					
							+ "<div id='buttonsar'>"
							+ "<a class='btn' href='CoverPage.html'>Click here to see the Cover page</a>&nbsp<a class='btn' href='../Report.html'>Click here to see the dashboard report</a>"
							+ "</div>"
							+ "</div>"
							+ "</body>"
							+ "</html>");
					
			output.close();
		}
		catch(Exception e)
		{
			System.out.println(e.toString());
		}
	}

	public  void createReportPageHtmlReport(String path)
	{
		try{
			PrintWriter writer = new PrintWriter(path);
			writer.print("");
			writer.close();
			//Writer output;
			BufferedWriter output;
			output = new BufferedWriter(new FileWriter(path,true));  //clears file every time
			output.write("<html>"+ addBarChartInReportHtml() +addCssLink().replace("../","./")+"<title>HTML REPORTING</title><body><div class='container'><div id='header'><img src='Logos/client_logo.jpg' class='clientLogo' width='100' height='100' /><img src='Logos/Indium-Software-Logo.jpg' class='indiumLogo'  width='180' height='110' /><h1>"+projectName+"</h1>"
					+ "</div><div id='ddb'><p><strong>Date:</strong> "+getCurrentDate()+"<span><strong>Duration:</strong> "+getDuration(totalGridDuration).replace("00hrs:", "").replace("00mins:", "")+"</span><strong>Build no:</strong> "+buildNo+"</p>"
							+ "</div><div id='chartContainer' style='height: 300px; width: 100%;'></div><div id='exectable'>"
							+ "<table class='column-options'><tr><th colspan='10' style='width:100%;'>Execution Status</th></tr><tr><th>Machine name</th><th>OS</th><th>Version</th><th>Browser/App-Name</th><th>Version</th><th>Executed</th><th>Passed</th><th>Failed</th><th>Skipped</th><th>Duration</th></tr>"+addTblRowInReportPage()+"</TABLE></body></html>");
		output.close();
		}
		catch(Exception e)
		{
			System.out.println(e.toString());
		}
		
	}

	public void failureReport(String path)
	{
		
			
			try{
				PrintWriter writer = new PrintWriter(path);
				writer.print("");
				writer.close();
				//Writer output;
				BufferedWriter output;
				output = new BufferedWriter(new FileWriter(path,true));  //clears file every time
				output.write(failureTcs());
				output.close();
			}
			catch(Exception e)
			{
				System.out.println(e.toString());
			}
	}
	public String failureTcs()
	{
		String txt=addCssLink()+"<table>";
		for(String tc :GenericKeywords.failureTcs)
		{

			txt=txt+"<tr><td><a style='color:red' href='./DefectAnalysis/"+tc+".html'>"+tc+"</td><td>"+GenericKeywords.failureTcsDescription.get(tc.trim())+"</td></tr>";
		}
		return txt+"</table>";
		
	}
	public void updateReport(ITestResult result)
	{
	
		for(int i=0;i<GenericKeywords.coverPageResultsDir.size();i++)
		   {
			   if(GenericKeywords.coverPageResultsDir.get(i).toLowerCase().contains(result.getMethod().getXmlTest().getParameter("selenium.machinename").toLowerCase().replace(" ","")))
			   {
				   createCoverPageHtmlReport(GenericKeywords.coverPageResultsDir.get(i));
			   		createSummaryPageHtmlReport(GenericKeywords.summaryPageResultsDir.get(i));
			   		createReportPageHtmlReport(GenericKeywords.outputDirectory+"/Report.html");
			   		//failureReport(GenericKeywords.failurePageResultsDir.get(i));
			   		
			   }
		   }
		/* if(PropertiesFile.getConfigProperty("GridExecution").toLowerCase().contains("yes"))
		 {

			 createReportPageHtmlReport(GenericKeywords.outputDirectory+"/Report.html");
		 }*/
		 
	}
	
	
	public String getTestSuitePath(String machineName)
	{
		String path="";
		 for(int i=0;i<GenericKeywords.coverPageResultsDir.size();i++)
		   {
			   if(GenericKeywords.summaryPageResultsDir.get(i).toLowerCase().contains(machineName.toLowerCase()))
			   {
				   path=	GenericKeywords.summaryPageResultsDir.get(i).replace(GenericKeywords.outputDirectory, ".");
				 
			   		
			   }
		   }
		return path;
		 
	}
   @Override
   public void onTestFailure(ITestResult result) 
   {
	   
	   totalTCFail++;
	   if(PropertiesFile.lowTc.containsKey(result.getMethod().getMethodName()))
	   {
		   lowTcFailure++;
	   }
	   else if(PropertiesFile.mediumTc.containsKey(result.getMethod().getMethodName()))
	   {
		   mediumTcFailure++;
	   }
	   else if(PropertiesFile.hardTc.containsKey(result.getMethod().getMethodName()))
	   {
		 hardTcFailure++;   
	   }
	   else
	   {
		   System.out.println(PropertiesFile.lowTc+" doesn't contain key name -"+result.getMethod().getMethodName());
	   }
	   updateReportVariables(result);
	   int percentage=(int) (((double)totalTCFail/totalTC)*100);
	   totalFailPercentage=percentage;
	   tcStatus.put(result.getMethod().getMethodName(), "Fail");
	   updateReport(result);

   }
   
   @Override
   public void onTestSuccess(ITestResult result) {
	  
	   totalTCSuccess++;
	   updateReportVariables(result);
	   int percentage=(int) (((double)totalTCSuccess/totalTC)*100);
	   totalPassPercentage=percentage;
	   tcStatus.put(result.getMethod().getMethodName(), "Pass");
	   updateReport(result);

	   
	   
   }
   
   @Override
   public void onTestSkipped(ITestResult result) {
	   totalTCSkip++;
	   updateReportVariables(result);
	    int percentage=(int) (((double)totalTCSkip/totalTC)*100);
	   totalSkipPercentage=percentage;
	   tcStatus.put(result.getMethod().getMethodName(),"Skip");
	   updateReport(result);

   }
   
   public void updateReportVariables(ITestResult result)
   {
	   long time = result.getEndMillis() - result.getStartMillis();
	   tcDuration.put(result.getMethod().getMethodName(), getDuration(time).replace("00hrs:", "").replace("00mins:", ""));
	   totalDurationInMillis=totalDurationInMillis+time;
	   if(totalDurationInMillis>totalGridDuration)
	   {
		   totalGridDuration=totalDurationInMillis;
	   }
	   totalDuration=getDuration(totalDurationInMillis).replace("00hrs:", "").replace("00mins:", "");
	   machine=result.getTestClass().getXmlTest().getParameter("selenium.machinename");
	   os=result.getTestClass().getXmlTest().getParameter("selenium.os");
	   osVersion=result.getTestClass().getXmlTest().getParameter("selenium.osVersion");
	   browserVersion=result.getTestClass().getXmlTest().getParameter("selenium.browserVersion");
	   browser=result.getTestClass().getXmlTest().getParameter("selenium.browser");
	   operatingSytsem.put(machine, os);
	   operatingSytsemVersion.put(machine, osVersion);
	   browserName.put(machine, browser);
	   browserVersionName.put(machine, browserVersion);
	   
	   totExecutedInOs.put(machine, ""+PropertiesFile.tcNames.size());
	   totPassedInOS.put(machine, ""+totalTCSuccess);
	   totFailedInOS.put(machine,""+totalTCFail);
	   totSkippedInOS.put(machine,""+totalTCSkip);
	   totDurationInOS.put(machine,totalDuration);
	   buildNo=PropertiesFile.getConfigProperty("Version_Name");
	   projectName=PropertiesFile.getConfigProperty("Project_Name");
	
   }
  
   public String getDuration(long millis){
	   String hms ="";
	   try{
	   hms = String.format("%02dhrs:%02dmins:%02dsecs", TimeUnit.MILLISECONDS.toHours(millis),
	            TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
	            TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
	    
	   }
	   catch(Exception e)
	   {
		   System.out.println(e.toString());
	   }
	return hms;
	   
	}
   
   public  static void createCssFile(String path)
   {
	   String css="";
	   try
	   {
		   String style=PropertiesFile.getConfigProperty("CssStyle").toLowerCase();
		   if(style.toLowerCase().contains("style1"))
		   {
			   backgroudColour="lightblue";
			   css="body {    background-color: lightblue;}h1 {    color: navy;    margin-left: 20px;}";
		   }
		   else if(style.toLowerCase().contains("style2"))
		   {
			   backgroudColour="linen";
			   css="body {    background-color: linen;}h1 {    color: maroon;    margin-left: 40px;}";
		   }
		   else if(style.toLowerCase().contains("style3"))
		   {
			   backgroudColour="#e5e5e5";
			   css="body {background-color:#e5e5e5; }h1 {    color: maroon;    margin-left: 40px;}th {    background-color: #4CAF50;    color: white;}a:link {    color: black;}a:visited {    color: blue;}a:hover {    color: green;}a:active {    color: blue;}tr:hover{background-color:gray}th {    background-color: #8A4117;    color: white;}tr{background-color: #E2A76F;}";
		   }
		   else
		   {
			   System.out.println(style+" Invalid style selected in tset configuration sheet");
		   }
		 
	   }
	   catch(Exception e)
	   {
		   System.out.println(e.toString());
	   }
   }
   
   public static Map<String, Object> getMapFromJsonString(String jsonString){

       Map<String, Object> jsonMap = (Map<String, Object>) JSONValue.parse(jsonString);

       System.out.println("Json Map: " + jsonMap);

       return jsonMap;

   }
   
   @Override
   public void onExecutionFinish() {
	   for(String machineName : PropertiesFile.machineNames)
		 {
			 if(HtmlReport.operatingSytsem.containsKey(machineName))
			 {
				 if(GenericKeywords.getConfigProperty("StoreResultsInCouchDB").toLowerCase().contains("yes"))
				 {
				 String dbName = "executionstatus";
				 CouchDBKeywords.createDatabase(dbName);
				
				 CouchDBKeywords.saveDocument(CouchDBKeywords.getDocument("{'"+machineName.replace(" ", "")+"':'Total"+HtmlReport.totExecutedInOs.get(machineName)+",Passed"+HtmlReport.totPassedInOS.get(machineName)+",Failed"+HtmlReport.totFailedInOS.get(machineName)+",Skipped"+HtmlReport.totSkippedInOS.get(machineName)+"'}"));
				 couchDBDocRows=couchDBDocRows+1;
				 }
				 createReportPageHtmlReport(GenericKeywords.outputDirectory+"/Report.html");
			 }
		 }
       
   }
@Override
public void onExecutionStart() {
	// TODO Auto-generated method stub
	
}

}