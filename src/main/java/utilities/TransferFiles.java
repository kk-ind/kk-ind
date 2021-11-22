package utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import automationFramework.GenericKeywords;

public class TransferFiles 
{
public static void transferLogo()
{
	
	try 
	{
		for(int i=0;i<GenericKeywords.hostName.size();i++)
		{
		(new File(GenericKeywords.outputDirectory+"/Logos/")).mkdir();
		
		
		File sourcecompanyLogo = new File("./src/main/resources/Logos/"+Common.getConfigProperty("indium_logo_path"));
		File sourceclientLogo = new File("./src/main/resources/Logos/"+Common.getConfigProperty("client_logo_path"));
		File designationcompanyLogo = new File(GenericKeywords.outputDirectory+"/Logos/"+Common.getConfigProperty("indium_logo_path"));
		File designationclientLogo = new File(GenericKeywords.outputDirectory+"/Logos/"+Common.getConfigProperty("client_logo_path"));
		InputStream in = new FileInputStream(sourcecompanyLogo);
		OutputStream out = new FileOutputStream(designationcompanyLogo);
		byte[] buf = new byte[1024];
		int len;
		while ((len = in.read(buf)) > 0) {
			out.write(buf, 0, len);
		}
		in.close();
		out.close();
		InputStream in1 = new FileInputStream(sourceclientLogo);
		OutputStream out1 = new FileOutputStream(designationclientLogo);
		byte[] buf1 = new byte[1024];
		int len1;
		while ((len1 = in1.read(buf1)) > 0) {
			out1.write(buf1, 0, len1);
		}
		in1.close();
		out1.close();
		}
	}

	catch (FileNotFoundException ex) 
	{

		System.out.println(ex.getMessage()+ " in  the specified directory.");
		System.exit(0);

	}

	catch (IOException e)
	{

		System.out.println(e.getMessage());

	} 

}

public static void transferJS()
{
	
	try 
	{
		for(int i=0;i<GenericKeywords.hostName.size();i++)
		{
			(new File(GenericKeywords.outputDirectory+"/Extras/")).mkdir();
		File source= new File("./src/main/resources/Extras/canvasjs.min.js");
		File designation = new File(GenericKeywords.outputDirectory+"/Extras/canvasjs.min.js");
		InputStream in = new FileInputStream(source);
		OutputStream out = new FileOutputStream(designation);
		byte[] buf = new byte[1024];
		int len;
		while ((len = in.read(buf)) > 0) {
			out.write(buf, 0, len);
		}
		in.close();
		out.close();
		}
	}

	catch (FileNotFoundException ex) 
	{

		System.out.println(ex.getMessage()+ " in  the specified directory.");
		System.exit(0);

	}

	catch (IOException e)
	{

		System.out.println(e.getMessage());

	} 

}

public static void transferCss()
{
	
	try 
	{
		for(int i=0;i<GenericKeywords.hostName.size();i++)
		{
		(new File(GenericKeywords.outputDirectory+"/Extras/")).mkdir();
		
		File source= new File("./src/main/resources/Extras/style.css");
		File designation = new File(GenericKeywords.outputDirectory+"/Extras/style.css");
		InputStream in = new FileInputStream(source);
		OutputStream out = new FileOutputStream(designation);
		byte[] buf = new byte[1024];
		int len;
		while ((len = in.read(buf)) > 0) {
			out.write(buf, 0, len);
		}
		in.close();
		out.close();
		}
	}

	catch (FileNotFoundException ex) 
	{

		System.out.println(ex.getMessage()+ " in  the specified directory.");
		System.exit(0);

	}

	catch (IOException e)
	{

		System.out.println(e.getMessage());

	} 

}

}
