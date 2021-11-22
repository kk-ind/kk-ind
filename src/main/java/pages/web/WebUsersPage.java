package pages.web;

import iSAFE.ApplicationKeywords;
import baseClass.BaseClass;

public class WebUsersPage extends ApplicationKeywords {

	public WebUsersPage(BaseClass obj) {
		super(obj);
	}

	public void acceptCancelGroupRequest(String decision, String user) {
		String lbl_email = "Tenant email#xpath=//div[contains(text(),'" + user + "')]";
		String btn_accept = "Accept#xpath=//span[contains(text(),'Accept')]";
		String btn_reject = "Reject#xpath=//span[contains(text(),'Reject')]";
	}

	public void deleteTenantFromGroup(String tenant) {
		
		
		/*
		 * delete: //button[@title='Delete']
		 * 
		 * confirmtion label box header: //span[contains(text(),'Confirm')] OK:
		 * //span[contains(text(),'Ok')] Cancel: //span[contains(text(),'Cancel')]
		 */ String btn_menu = tenant + " checkbox#xpath=//td[contains(text(),'" + tenant + "')]/preceding::td//input";
	}

}
