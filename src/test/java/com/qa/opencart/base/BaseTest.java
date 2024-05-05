package com.qa.opencart.base;

import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.asserts.SoftAssert;

import com.qa.opencart.factory.DriverFactory;
import com.qa.opencart.logger.Log;
import com.qa.opencart.pages.AccountPage;
import com.qa.opencart.pages.CartPage;
import com.qa.opencart.pages.CheckoutPage;
import com.qa.opencart.pages.LoginPage;
import com.qa.opencart.pages.ProductPage;
import com.qa.opencart.pages.SearchResultsPage;
import com.qa.opencart.productassertions.ProductCalculationAssertions;
import com.qa.opencart.productassertions.ProductInfoAssertions;
import com.qa.opencart.productassertions.StandardRateAssertions;

public class BaseTest {

	DriverFactory df;
	protected Properties prop;
	WebDriver driver;
	protected LoginPage loginPage;
	protected AccountPage accPage;
	protected SearchResultsPage searchResultsPage;
	protected ProductPage productPage;
	protected SoftAssert softAssert;
	protected CartPage cartPage;
	protected CheckoutPage checkoutPage;
	protected ProductInfoAssertions productInfoAssertions;
	protected ProductCalculationAssertions productCalculationAssertions;
	protected StandardRateAssertions stdRateAssertion;

	@Parameters({ "browser", "browserversion" })
	@BeforeTest
	public void setup(String browserName, String browserVersion) {
		df = new DriverFactory();
		prop = df.initProp();
		if (!(browserName == null)) {
			Log.info("Tests running on browser: " + browserName);
			prop.setProperty("browser", browserName);
			prop.setProperty("browserversion", browserVersion);
		}
		driver = df.initDriver(prop);
		loginPage = new LoginPage(driver);
	}

	@AfterTest
	public void tearDown() {
		driver.quit();
	}

}
