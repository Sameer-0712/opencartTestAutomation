package com.qa.opencart.tests;

import java.util.List;
import java.util.Map;

import com.qa.opencart.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.qa.opencart.base.BaseTest;

import static com.qa.opencart.constants.AppConstants.NO_PRODUCTS_SEARCH_RESULTS;
import static com.qa.opencart.logger.Log.logger;

public class SearchProductsTest extends BaseTest {
	
	
	@BeforeClass
	public void loginToApp() {
		loginPage = new LoginPage(driver);
		accPage = loginPage.doLogin(prop.getProperty("username"), prop.getProperty("password"));
	}
	
	@Test(dataProvider = "searchKeys")
	public void productInfoTest(String searchKey) {
		searchResultsPage =  page.doSearch(searchKey);
		List<String> products = searchResultsPage.getSearchProductsList();

		if(products.size() > 0){
			for(String product: products){
				Assert.assertTrue(product.toLowerCase().contains(searchKey.toLowerCase()));
				logger.info(String.format("%s contains the text '%s'",product,searchKey));
			}
		}else {
			logger.info("No products exist for the searched text: "+searchKey);
			Assert.assertEquals(searchResultsPage.getNoResultsText(),NO_PRODUCTS_SEARCH_RESULTS);
		}

	}

	@DataProvider
	public Object[][] searchKeys(){
		return new Object[][] {
				{"mac"},{"sony"},{"ipad"}
		};
	}
	

}
