package TestCases;

import java.lang.reflect.Method;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import automationFramework.GenericKeywords;
import baseClass.BaseClass;
import scenarios.TenantLogin;
import scenarios.TenantRegistration;

@Listeners({ utilities.HtmlReport.class })
public class TestCases {
	private BaseClass obj;

	////////////////////////////////////////////////////////////////////////////////
	// Function Name :
	// Purpose :
	// Parameters :
	// Return Value :
	// Created by :
	// Created on :
	// Remarks :
	/////////////////////////////////////////////////////////////////////////////////

	private void TestStart(String testCaseName) {

		obj.currentTestCaseName = testCaseName;
		obj.setEnvironmentTimeouts();
		obj.reportStart();
		obj.iterationCount.clear();
		obj.iterationCountInTextData();

	}

	@BeforeClass
	@Parameters({ "selenium.machinename", "selenium.host", "selenium.port", "selenium.browser", "selenium.os",
			"selenium.browserVersion", "selenium.osVersion", "TestData.SheetNumber" })
	public void precondition(String machineName, String host, String port, String browser, String os,
			String browserVersion, String osVersion, String sheetNo) {
		obj = new BaseClass();
		if (os.contains("Android")) {
//			obj.startServer(host, port);
			obj.waitTime(10);
		}
		obj.setup(machineName, host, port, browser, os, browserVersion, osVersion, sheetNo);
	}

	@AfterClass
	public void closeSessions() {
		obj.closeAllSessions();
	}

	@Test(alwaysRun = true)
	@Parameters({ "selenium.machinename" })
	public void TC_tenantRegistration(String machineName, Method method) throws InterruptedException {

		TestStart(method.getName());
		TenantRegistration tenantRegistrationObj = new TenantRegistration(obj);
		for (int i = 0; i < tenantRegistrationObj.iterationCount.size(); i++) {

			tenantRegistrationObj.dataRowNo = Integer.parseInt(tenantRegistrationObj.iterationCount.get(i).toString());
			tenantRegistrationObj.testStepInfo(
					"################################DataSet: " + (i + 1) + "################################");
			tenantRegistrationObj.tenantRegisteration();
		}
		obj.testFailure = tenantRegistrationObj.testFailure;
		TestEnd();
	}

	@Test(alwaysRun = true)
	@Parameters({ "selenium.machinename" })
	public void TC_tenantLogin(String machineName, Method method) throws InterruptedException {

		TestStart(method.getName());
		TenantLogin tenantLoginObj = new TenantLogin(obj);
		for (int i = 0; i < tenantLoginObj.iterationCount.size(); i++) {

			tenantLoginObj.dataRowNo = Integer.parseInt(tenantLoginObj.iterationCount.get(i).toString());
			tenantLoginObj.testStepInfo(
					"################################DataSet: " + (i + 1) + "################################");
			tenantLoginObj.signUp();
		}
		obj.testFailure = tenantLoginObj.testFailure;
		TestEnd();
	}

	private void TestEnd() {
		obj.waitTime(1);
		if (obj.testFailure) {
			GenericKeywords.testFailed();
		}
	}

	@BeforeMethod
	public void before() {
		obj.testFailure = false;
	}

}
