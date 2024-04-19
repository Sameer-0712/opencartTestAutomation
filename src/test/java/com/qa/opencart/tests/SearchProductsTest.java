package com.qa.opencart.tests;

import java.util.Map;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.qa.opencart.base.BaseTest;

public class SearchProductsTest extends BaseTest {
	
	
	@BeforeClass
	public void loginToApp() {
		accPage = loginPage.doLogin(prop.getProperty("username"), prop.getProperty("password"));
	}
	
	@Test
	public void productInfoTest() {
		searchResultsPage =  accPage.doSearch("mac");
		productPage = searchResultsPage.navigateToProductPage("MacBook");
		softAssert = new SoftAssert();
		Map<String,String> productDetails = productPage.getProductInfo();
		softAssert.assertEquals(productDetails.get("Brand"), "Apple");
		softAssert.assertEquals(productDetails.get("Product Code"), "Product 16");
		softAssert.assertEquals(productDetails.get("Reward Points"), "600");
		softAssert.assertEquals(productDetails.get("Availability"), "In Stock");
		softAssert.assertEquals(productDetails.get("Price"), "$602.00");
		softAssert.assertEquals(productDetails.get("Ex Tax"), "$500.00");
		softAssert.assertAll();
	}
	
	

}
