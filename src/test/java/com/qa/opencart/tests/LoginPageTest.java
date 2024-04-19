package com.qa.opencart.tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.qa.opencart.base.BaseTest;

public class LoginPageTest extends BaseTest{

	
	@Test
	public void loginPageTitleTest() {
		Assert.assertEquals(loginPage.getLoginPageTitle(), "Account Login");	
	}
	
	@Test
	public void loginPageUrlTest() {
		Assert.assertTrue(loginPage.getLoginPageURL().contains("account/login"));	
	}
	
	@Test
	public void loginTest() {
		accPage = loginPage.doLogin(prop.getProperty("username"), prop.getProperty("password"));
		Assert.assertEquals(accPage.getAccountPageTitle(), "My Account");
	}
		
}
