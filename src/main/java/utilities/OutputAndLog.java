package utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import automationFramework.GenericKeywords;

public class OutputAndLog
{
	public static String am_pm,min,hr,sec,yr,mon,day;
	public static void createOutputDirectory()
	{
	//outputDirectory="";
	File curdir = new File(".");
  
  	Calendar calendar = new GregorianCalendar();
  	//String am_pm,min,hr,sec,yr,mon,day;
  	
  	hr=("0"+calendar.get(Calendar.HOUR));
  	hr=hr.substring(hr.length()-2);

  	min=("0"+calendar.get(Calendar.MINUTE));
  	min=min.substring(min.length()-2);
  

  	sec=("0"+calendar.get(Calendar.SECOND));
  	sec=sec.substring(sec.length()-2);

  	yr=""+calendar.get(Calendar.YEAR);
  	//yr=yr.substring(yr.length()-2);

  	mon=("0"+(calendar.get(Calendar.MONTH)+1));
  	mon=mon.substring(mon.length()-2);

  	day=("0"+calendar.get(Calendar.DAY_OF_MONTH));
  	day=day.substring(day.length()-2);

  	if(calendar.get(Calendar.AM_PM) == 0)
	  	am_pm = "AM";
  	else
	  	am_pm = "PM";

  	GenericKeywords.timeStamp=yr +"_"+ mon+"_" +day+"_"+hr+"_"+min+"_"+sec+"_"+am_pm;
  	try 
  	{
  		GenericKeywords.outputDirectory = curdir.getCanonicalPath()+ "/TestResults/" + yr +"_"+ mon+"_" +day+"_"+hr+"_"+min+"_"+sec+"_"+am_pm;
  	} 
  	catch (IOException e) 
  	{
  		// TODO Auto-generated catch block
	  	System.out.println("IO Error while creating Output Directory : "+GenericKeywords.outputDirectory);
  	}

  		createLogFile();
}


public static void createLogFile() 
{
    Properties props = new Properties();
    String propsFileName = "./src/main/resources/Config/log4j.properties";

    try 
    {
    	FileInputStream configStream = new FileInputStream(propsFileName);
    	props.load(configStream);
    	
      
    	String myStr;
    	myStr=GenericKeywords.outputDirectory.substring(GenericKeywords.outputDirectory.length()-22);
      
    	myStr="./TestResults/"+myStr+"/LogFile.log";
    	//System.out.println(myStr);
		
    	props.setProperty("log4j.appender.FA.File",myStr);
    	FileOutputStream output = new FileOutputStream(propsFileName);
    	props.store(output, "Output Directory updated : "+GenericKeywords.outputDirectory);
    	//System.out.println(props.getProperty("log4j.appender.FA.File"));
		
    	output.close();
    	configStream.close();
   
    	//props.clear();
    	PropertyConfigurator.configure(propsFileName);
		//System.out.println(props.getProperty("log4j.appender.FA.File"));
    	GenericKeywords.logger=Logger.getLogger(myStr);
    	GenericKeywords.writeToLogFile("INFO","Startup activites...");
		
    	GenericKeywords.writeToLogFile("INFO", "Test Output Directory creation successful :"+GenericKeywords.outputDirectory);
    	GenericKeywords.writeToLogFile("INFO", "Log File creation successful : LogFile.log");
    } 
    catch (IOException ex) 
    {
    	System.out.println("There was an error creating the log file");
    }
}

}
