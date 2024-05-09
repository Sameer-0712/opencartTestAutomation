package com.qa.opencart.tests;

import com.qa.opencart.pages.LoginPage;
import com.qa.opencart.productassertions.ShippingRateRadioButtonAssertions;
import com.qa.opencart.productassertions.StandardRateAssertions;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.qa.opencart.base.BaseTest;
import com.qa.opencart.constants.AppConstants;
import com.qa.opencart.errors.AppErrors;
import com.qa.opencart.logger.Log;
import com.qa.opencart.productassertions.ProductCalculationAssertions;
import com.qa.opencart.productassertions.ProductInfoAssertions;
import com.qa.opencart.utils.AppUtils;
import com.qa.opencart.utils.ExcelUtil;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Step;
import org.testng.asserts.SoftAssert;

public class PlaceOrderTest extends BaseTest {

	@Step("Login to the application")
	@BeforeClass
	public void loginToTheApp() {
		Log.info("Login to the application");
		loginPage = new LoginPage(driver);
		accPage = loginPage.doLogin(prop.getProperty("username"), prop.getProperty("password"));
		Log.info("User logged in");
	}

	@Severity(SeverityLevel.CRITICAL)
	@Description("End to end test to verify the order for single items")
	@Step("Starting the execution. Search Key: {0), Product: {1}, Quantity: {2}, Billing Country: {3}, Delivery Country: {4}")
	@Test(dataProvider = "getPlaceOrderData")
	public void validatePlaceSingleItemOrders(String searchKey, String productName, String quantity,
			String billingCountry, String deliveryCountry) {

		//Arrange
		AppUtils appUtil = new AppUtils(page, searchResultsPage, productPage, cartPage, checkoutPage);
		int qty = Integer.parseInt(quantity);
		softAssert = new SoftAssert();
		shippingRateRadioButtonAssertions = new ShippingRateRadioButtonAssertions(driver,softAssert);
		String expectedMsg = AppConstants.ORDER_PLACED_SUCCESS_MESSAGE;

		//Act
		appUtil.addProductToCart(searchKey, productName, quantity);
		checkoutPage = appUtil.navigateToCheckOutPageAndFillDetails(billingCountry, deliveryCountry);

		//Assert
		shippingRateRadioButtonAssertions.assertShippingRadioButtonIsSelected();
		shippingRateRadioButtonAssertions.assertShippingRadioButtonText(deliveryCountry);

		//Act
		checkoutPage.selectDeliverAndPaymentMethod();

		//Assert
		productInfoAssertions = new ProductInfoAssertions(softAssert, checkoutPage);
		productInfoAssertions.validateProductAssertions(deliveryCountry, productName, qty);
		productCalculationAssertions = new ProductCalculationAssertions(softAssert, checkoutPage);
		productCalculationAssertions.validateTotalWithoutTaxes(deliveryCountry, productName, qty);
		productCalculationAssertions.validateSubTotal(deliveryCountry, productName, qty);
		stdRateAssertion = new StandardRateAssertions(softAssert,checkoutPage);
		stdRateAssertion.validateFlatShippingRate(deliveryCountry);
		stdRateAssertion.validateEcoTax(deliveryCountry, qty);
		productCalculationAssertions.validateVAT(deliveryCountry, productName, qty);
		productCalculationAssertions.validateTotal(deliveryCountry, productName, qty);

		//Act
		String actualMsg = checkoutPage.confirmOrder();

		//Assert
		softAssert.assertEquals(actualMsg, expectedMsg, AppErrors.ORDER_PLACED_SUCCESS_MESSAGE_ERROR);
		softAssert.assertAll();

	}

	@DataProvider
	public Object[][] getPlaceOrderData() {
		return ExcelUtil.getTestData(AppConstants.PLACE_ORDER_SHEET_NAME);
	}

	// ***************************************************************************************************************************

	@Severity(SeverityLevel.CRITICAL)
	@Description("End to end test to verify the order for multiple items")
	@Step("Starting the execution for billing country {0} and delivery country {1}")
	@Test(dataProvider = "getBillingAndDeliveryCountries")
	public void validatePlaceMultipleItemOrders(String billingCountry, String deliveryCountry) {

		//Arrange
		AppUtils appUtil = new AppUtils(page, searchResultsPage, productPage, cartPage, checkoutPage);
		softAssert = new SoftAssert();
		shippingRateRadioButtonAssertions = new ShippingRateRadioButtonAssertions(driver,softAssert);
		String expectedMsg = AppConstants.ORDER_PLACED_SUCCESS_MESSAGE;

		//Act
		appUtil.addProductsToCart();
		checkoutPage = appUtil.navigateToCheckOutPageAndFillDetails(billingCountry, deliveryCountry);

		//Assert
		shippingRateRadioButtonAssertions.assertShippingRadioButtonIsSelected();
		shippingRateRadioButtonAssertions.assertShippingRadioButtonText(deliveryCountry);

		//Act
		checkoutPage.selectDeliverAndPaymentMethod();

		//Assert
		productInfoAssertions = new ProductInfoAssertions(softAssert, checkoutPage);
		productInfoAssertions.validateProductAssertions(deliveryCountry);
		productCalculationAssertions = new ProductCalculationAssertions(softAssert, checkoutPage);
		productCalculationAssertions.validateSubTotalForEachProduct(deliveryCountry);
		productCalculationAssertions.validateSubTotal(deliveryCountry);
		stdRateAssertion = new StandardRateAssertions(softAssert, checkoutPage);
		stdRateAssertion.validateFlatShippingRate(deliveryCountry);
		stdRateAssertion.validateTotalEcoTax(deliveryCountry);
		productCalculationAssertions.validateTotalVAT(deliveryCountry);
		productCalculationAssertions.validateTotalForMultipleProducts(deliveryCountry);

		//Act
		String actualMsg = checkoutPage.confirmOrder();

		//Assert
		softAssert.assertEquals(actualMsg, expectedMsg, AppErrors.ORDER_PLACED_SUCCESS_MESSAGE_ERROR);
		softAssert.assertAll();
	}

	@DataProvider
	public Object[][] getBillingAndDeliveryCountries() {
		return new Object[][] { { "United Kingdom", "Japan" }, { "Japan", "United Kingdom" } };
	}


}
