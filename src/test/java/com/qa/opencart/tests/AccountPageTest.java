package com.qa.opencart.tests;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.qa.opencart.base.BaseTest;

public class AccountPageTest extends BaseTest {
	
	
	@BeforeClass
	public void loginToApp() {
		accPage = loginPage.doLogin(prop.getProperty("username"), prop.getProperty("password"));
	}
	
	@Test
	public void accountPageTitleTest() {
		Assert.assertEquals(accPage.getAccountPageTitle(), "My Account");	
	}
	
	@Test
	public void accountPageUrlTest() {
		Assert.assertTrue(accPage.getAccountPageURL().contains("account/account"));
	}
	
	@Test
	public void logoutLinkTest() {
		Assert.assertTrue(accPage.checkLogoutLink());
	}
	
	@Test
	public void accountPageHeadersTest() {
		Assert.assertEquals(accPage.getHeaders().size(), 4);
	}

}
