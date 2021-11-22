package scenarios;

import pages.android.AndroidHomePage;
import pages.android.AndroidLoginPage;
import pages.android.AndroidTenantRegisterationPage;
import pages.web.WebLoginPage;
import iSAFE.ApplicationKeywords;
import baseClass.BaseClass;

public class TenantRegistration extends ApplicationKeywords {

	BaseClass obj;

	private Boolean status = false;

	public TenantRegistration(BaseClass obj) {
		super(obj);
		this.obj = obj;
	}

	AndroidLoginPage androidLoginPage;
	AndroidTenantRegisterationPage androidTenRegistrationPage;
	WebLoginPage webLoginPage;
	AndroidHomePage androidHomePage;

	public void tenantRegisteration() {
		try {

			String tenantFirstName = retrieve("Tenant First Name");
			String tenantLastName = retrieve("Tenant Last Name");
			String tenantEmailAddress = retrieve("Tenant Email Address");
			String tenantPassword = retrieve("Tenant Password");
            boolean flag = false;
			
			
			switchApplication("Tenant");
			obj.driver = driver;
			androidTenRegistrationPage = new AndroidTenantRegisterationPage(obj);
			androidTenRegistrationPage.tenantRegisteration(tenantEmailAddress, tenantFirstName, tenantLastName);
			flag = androidTenRegistrationPage.validateRegisteration();
			
			
			if (flag) {
				switchApplication("Admin");
				obj.driver = driver;
				webLoginPage = new WebLoginPage(obj);
				String registrationURL = getMailContent(tenantEmailAddress, tenantPassword);
				navigateTo(registrationURL);
				webLoginPage.tenantSetPassword(tenantPassword);
			}
			if (flag) {
				switchApplication("Tenant");
				obj.driver = driver;
				androidLoginPage = new AndroidLoginPage(obj);
				androidHomePage = new AndroidHomePage(obj);
				androidLoginPage.entertenLoginCreds(tenantEmailAddress, tenantPassword);
				androidLoginPage.loginValidation("Positive");
				androidHomePage.tenantSignOut();
			}
			
			if (androidLoginPage.testFailure || androidTenRegistrationPage.testFailure || webLoginPage.testFailure
					|| androidHomePage.testFailure) {
				this.testFailure = true;
			}

		} catch (Exception e) {
			System.err.println(e.toString());
			this.testFailure = true;
		} 
		finally {
			this.testCaseExecutionStatus = status;
		}
	}
}
