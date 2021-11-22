package pages.android;

import iSAFE.ApplicationKeywords;
import baseClass.BaseClass;

public class AndroidHomePage extends ApplicationKeywords {

	private static final String btn_hamburger = "Hamburger Icon#xpath=//android.view.View/android.widget.Button[@content-desc='Open navigation menu']";
	private static final String btn_signOut = "Sign Out#xpath=//android.widget.Button[@content-desc='Sign Out']";
	private static final String btn_login = "Login#xpath=//android.view.View/android.widget.Button[@content-desc='Login']";

	private static final String lbl_messaging = "Messaging#xpath=//android.view.View[@content-desc='Messaging']";
	private static final String btn_skip = "Skip#xpath=//android.view.View/android.widget.Button[@content-desc='SKIP']";
	private static final String lbl_noPropAssign = "No property assigned#xpath=//android.view.View[@content-desc='No Property Assigned']";

	private static final String lbl_welcome = "Welcome Heading#xpath=//android.view.View[contains(@content-desc,'Welcome tester auto')]";
	private static final String txt_groupID = "Group ID#xpath=//android.view.View/android.widget.EditText[@text='Group Id']";
	private static final String btn_request = "Request#xpath=//android.widget.ScrollView/android.widget.Button[@content-desc='Request']";
	private static final String lbl_requestSuccess = "Request success#xpath=//android.view.View[contains(@content-desc,'You have')]";
	private static final String btn_requestCancel = "Request cancel#xpath=//android.view.View/android.widget.Button[@content-desc='Cancel Request']";

	public AndroidHomePage(BaseClass obj) {
		super(obj);
	}

	public void tenantSignOut() {
		String loginBtn = "//android.view.View/android.widget.Button[@content-desc='Login']";
		String skipButton = "//android.view.View/android.widget.Button[@content-desc='SKIP']";
		String welcomeLbl = " //android.view.View[contains(@content-desc,'Welcome')]";

		try {
			if (isElementPresent(welcomeLbl, 15)) {
				clickOn(btn_signOut);
			} else if(isElementPresent(skipButton, 15)){
				clickOn(btn_skip);
				clickOn(btn_hamburger);
				clickOn(btn_signOut);	
			}else {
				clickOn(btn_hamburger);
				clickOn(btn_signOut);	
			}
			if (isElementPresent(loginBtn, 10)) {
				testStepPassed("Successfully signed out of the tenant android application");
			} else {
				testStepFailed("Sign out Failed");
			}
		} catch (Exception e) {
			writeToLogFile("Error", e.toString());
			testStepFailed("Sign out Failed");
		}
	}

	public void sendGroupRequest(String groupID) {
		String requestSuccess = "//android.view.View[contains(@content-desc,'You have')]";
		try {
			typeIn(txt_groupID, groupID);
			clickOn(btn_request);
			if (isElementPresent(requestSuccess, 10)) {
				testStepPassed("Group request sent successfully");
			} else {
				testStepFailed("Group request sending failed");
			}
		} catch (Exception e) {
			writeToLogFile("Error", e.toString());
			testStepFailed("Group request sending failed");
		}
	}

	public void cancelGroupRequest() {
		clickOn(btn_requestCancel);
		if (isElementDisplayed(btn_request)) {
			testStepPassed("Group request cancelled successfully");
		} else {
			testStepFailed("Failed to cancel group request");
		}
	}

	public void validateGroupRequest(String group) {
		String lbl_groupID = "Group ID#xpath=//android.widget.ImageView[@content-desc='" + group + "']";
		clickOn(btn_hamburger);
		if (isElementDisplayed(lbl_groupID)) {
			testStepPassed("Tenant added to the requested group");
			swipeRighttoLeft();
		} else {
			testStepFailed("Tenant is not added to the group");
			swipeRighttoLeft();
		}
	}

	public void validateGroupRemoval() {
		if (isElementDisplayed(btn_request)) {
			testStepPassed("Tenant removed from the group successfully");
		} else {
			testStepFailed("Failed to remove the tenant from the group");
		}
	}

	public void skipWalkthrough() {
		if (isElementDisplayed(lbl_messaging)) {
			clickOn(btn_skip);
			testStepPassed("Walkthough popup skipped");
		} else {
			testStepInfo("Walkthrough popup not displayed");
		}
	}

}
