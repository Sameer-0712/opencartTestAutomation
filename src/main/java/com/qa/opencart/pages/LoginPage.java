package com.qa.opencart.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.qa.opencart.constants.AppConstants;
import com.qa.opencart.utils.ElementUtil;
import com.qa.opencart.utils.TimeUtil;

public class LoginPage {
	
	private WebDriver driver;
	private ElementUtil elUtil;
	
	public LoginPage(WebDriver driver) {
		this.driver = driver;
		elUtil = new ElementUtil(driver);
	}
	
	private By emailId = By.id("input-email");
	private By password = By.id("input-password");
	private By forgotPwdLink = By.linkText("Forgotten Password");
	private By loginButton = By.xpath("//input[@value='Login']");
	
	public AccountPage doLogin(String username, String pwd) {
		elUtil.sendKeysToElement(emailId, username);
		elUtil.sendKeysToElement(password, pwd);
		elUtil.clickElement(loginButton);
		return new AccountPage(driver);
	}
	
	public boolean checkForgottenPasswordLinkPresence() {
		return elUtil.isElementExist(forgotPwdLink);
	}
	
	public String getLoginPageTitle() {
		return elUtil.waitForPageTitleIs(TimeUtil.DEFAULT_MEDIUM_TIME, AppConstants.LOGIN_PAGE_TITLE);
	}
	
	public String getLoginPageURL() {
		return elUtil.waitForPageURLContains(TimeUtil.DEFAULT_MEDIUM_TIME, AppConstants.LOGIN_PAGE_URL_FRACTION);
	}
	
}
