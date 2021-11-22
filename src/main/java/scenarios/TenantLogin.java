package scenarios;

import pages.android.AndroidHomePage;
import pages.android.AndroidLoginPage;
import iSAFE.ApplicationKeywords;
import baseClass.BaseClass;

public class TenantLogin extends ApplicationKeywords {

	BaseClass obj;
	AndroidLoginPage androidLoginPage;
	AndroidHomePage androidHomePage;

	private Boolean status = false;

	public TenantLogin(BaseClass obj) {

		super(obj);
		this.obj = obj;
	}

	public void signUp() {
		try {
			String negScenario = retrieve("Negative Validation");
			String tenantEmailAddress = retrieve("Tenant Email Address");
			String tenantPassword = retrieve("Tenant Password");

			switchApplication("Tenant");
			obj.driver = driver;
			androidLoginPage = new AndroidLoginPage(obj);
			androidHomePage = new AndroidHomePage(obj);
			androidLoginPage.entertenLoginCreds(tenantEmailAddress, tenantPassword);
			androidLoginPage.loginValidation(negScenario);
			if (negScenario.equalsIgnoreCase("Positive")) {
				androidHomePage.tenantSignOut();
			}
			if (androidLoginPage.testFailure || androidHomePage.testFailure) {
				this.testFailure = true;
			}
		} catch (Exception e) {
			System.err.println(e.toString());
		} finally {
			this.testCaseExecutionStatus = status;
		}
	}
}
