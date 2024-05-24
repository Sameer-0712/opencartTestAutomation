package com.qa.opencart.base;

import java.util.Properties;

import com.qa.opencart.pages.*;
import com.qa.opencart.productassertions.*;
import com.qa.opencart.utils.AppUtils;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.asserts.SoftAssert;

import com.qa.opencart.factory.DriverFactory;
import com.qa.opencart.logger.Log;

public class BaseTest {
	
	DriverFactory df;
	protected Properties prop;
	protected WebDriver driver;
	protected LoginPage loginPage;
	protected Page page;
	protected AccountPage accPage;
	protected SearchResultsPage searchResultsPage;
	protected ProductPage productPage;
	protected SoftAssert softAssert;
	protected CartPage cartPage;
	protected CheckoutPage checkoutPage;
	protected ProductInfoAssertions productInfoAssertions;
	protected ProductCalculationAssertions productCalculationAssertions;
	protected StandardRateAssertions stdRateAssertion;
	protected ShippingRateRadioButtonAssertions shippingRateRadioButtonAssertions;
	protected ProductCalculationAssertionsOnCartPage productCalculationAssertionsOnCartPage;
	protected AppUtils appUtil;
	
	@Parameters("browser")
	@BeforeTest
	public void setup(String browserName) {
		df = new DriverFactory();
		prop = df.initProp();
		if(!(browserName == null)) {
			Log.info("Tests running on browser: "+browserName);
			prop.setProperty("browser", browserName);
		}
		driver = df.initDriver(prop);
		page = new Page(driver);
	}
	
	@AfterTest
	public void tearDown() {
		driver.quit();
	}

}
