package scenarios;

import pages.android.AndroidHomePage;
import pages.android.AndroidLoginPage;
import pages.web.WebHomePage;
import pages.web.WebLoginPage;
import pages.web.WebUsersPage;
import iSAFE.ApplicationKeywords;
import baseClass.BaseClass;

public class TenantGroupRequest extends ApplicationKeywords {

	BaseClass obj;
	AndroidLoginPage androidLoginPage;
	AndroidHomePage androidHomePage;
	WebLoginPage webLoginPage;
	WebUsersPage webUsersPage;

	private Boolean status = false;

	public TenantGroupRequest(BaseClass obj) {

		super(obj);
		this.obj = obj;
	}

	public TenantGroupRequest() {

	}

	public void signUp() {
		try {
			String negScenario = "Positive";
			String tenantEmailAddress = retrieve("Tenant Email Address");
			String tenantPassword = retrieve("Tenant Password");
			String groupID = retrieve("Group Name");
			String adminUrl = retrieve("Admin Url");
			String adminEmail = retrieve("Admin Email");
			String adminPassword = retrieve("Admin Password");
			String adminName = retrieve("Admin Name");
			

			switchApplication("Tenant");
			obj.driver = driver;
			androidLoginPage = new AndroidLoginPage(obj);
			androidHomePage = new AndroidHomePage(obj);
			androidLoginPage.entertenLoginCreds(tenantEmailAddress, tenantPassword);
			androidLoginPage.loginValidation(negScenario);
			androidHomePage.sendGroupRequest(groupID);
			
		
			switchApplication("Admin");
			obj.driver = driver;
			navigateTo(adminUrl);
			webLoginPage = new WebLoginPage(obj);
			webUsersPage = new WebUsersPage(obj);
			webLoginPage.adminLogin(adminEmail, adminPassword, adminName);
			navigateTo("Users");
			webUsersPage.acceptCancelGroupRequest("Accept", tenantEmailAddress);
			
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
