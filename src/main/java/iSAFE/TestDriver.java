package iSAFE;

import java.util.ArrayList;
import java.util.List;
import org.testng.TestNG;

import baseClass.BaseClass;
import utilities.Common;

public class TestDriver extends ApplicationKeywords {

	public TestDriver(BaseClass obj) {
		super(obj);
	}

	public static void main(String[] args) {
		try {
			Common common = new Common();
			common.startup();
			TestNG testng = new TestNG();
			List<String> suites = new ArrayList<String>();
			suites.add("./src/main/resources/config/testng.xml");
			testng.setOutputDirectory(outputDirectory + "/testng");
			testng.setTestSuites(suites);
			testng.run();
		} catch (Exception e) {
			writeToLogFile("error", e.toString());
		} finally {
			try {
				cleanup();
			} catch (Exception e) {
				writeToLogFile("error", e.toString());
			} finally {
				writeToLogFile("INFO", "###################################");
				writeToLogFile("INFO", "Script Execution Complete");
				writeToLogFile("INFO", "###################################");
			}
		}
	}
}