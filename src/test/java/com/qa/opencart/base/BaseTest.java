package com.qa.opencart.base;

import java.util.Properties;

import com.qa.opencart.pages.*;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.asserts.SoftAssert;

import com.qa.opencart.factory.DriverFactory;
import com.qa.opencart.logger.Log;
import com.qa.opencart.productassertions.ProductCalculationAssertions;
import com.qa.opencart.productassertions.ProductInfoAssertions;
import com.qa.opencart.productassertions.StandardRateAssertions;

public class BaseTest {
	
	DriverFactory df;
	protected Properties prop;
	WebDriver driver;
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
		loginPage = new LoginPage(driver);
		page = new Page(driver);
	}
	
	@AfterTest
	public void tearDown() {
		driver.quit();
	}

	/*
	 * A method to verify that the shipping radio button is selected on the
	 * checkout page. It will also verify that the text against the radio button is
	 * based on the delivery country selected.
	 */
	protected void assertShippingRateRadioButton(String deliveryCountry) {
		softAssert = new SoftAssert();
		stdRateAssertion = new StandardRateAssertions(softAssert, checkoutPage);
		softAssert.assertTrue(checkoutPage.isFlatShippingRateRadioBtnSelected());
		stdRateAssertion.validateShippingRateInDeliveryMethodStep(deliveryCountry);
		checkoutPage.selectDeliverAndPaymentMethod();
	}

}
