package com.qa.opencart.tests;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.qa.opencart.base.BaseTest;
import com.qa.opencart.constants.AppConstants;
import com.qa.opencart.errors.AppErrors;
import com.qa.opencart.logger.Log;
import com.qa.opencart.productassertions.ProductCalculationAssertions;
import com.qa.opencart.productassertions.ProductInfoAssertions;
import com.qa.opencart.productassertions.StandardRateAssertions;
import com.qa.opencart.utils.AppUtils;
import com.qa.opencart.utils.ExcelUtil;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Step;

public class PlaceOrderTest extends BaseTest {

	@Step("Login to the application")
	@BeforeClass
	public void loginToTheApp() {
		Log.info("Login to the application");
		accPage = loginPage.doLogin(prop.getProperty("username"), prop.getProperty("password"));
		Log.info("User logged in");
	}

	@Severity(SeverityLevel.CRITICAL)
	@Description("End to end test to verify the order for single items")
	@Step("Starting the execution. Search Key: {0), Product: {1}, Quantity: {2}, Billing Country: {3}, Delivery Country: {4}")
	@Test(dataProvider = "getPlaceOrderData")
	public void validatePlaceSingleItemOrders(String searchKey, String productName, String quantity,
			String billingCountry, String deliveryCountry) {
		AppUtils appUtil = new AppUtils(accPage, searchResultsPage, productPage, cartPage, checkoutPage);
		int qty = Integer.parseInt(quantity);

		appUtil.addProductToCart(searchKey, productName, quantity);
		checkoutPage = appUtil.navigateToCheckOutPageAndFillDetails(billingCountry, deliveryCountry);

		assertShippingRateRadioButton(deliveryCountry);

		productInfoAssertions = new ProductInfoAssertions(softAssert, checkoutPage);
		productInfoAssertions.validateProductAssertions(deliveryCountry, productName, qty);
		productCalculationAssertions = new ProductCalculationAssertions(softAssert, checkoutPage);
		productCalculationAssertions.validateTotalWithoutTaxes(deliveryCountry, productName, qty);
		productCalculationAssertions.validateSubTotal(deliveryCountry, productName, qty);
		stdRateAssertion.validateFlatShippingRate(deliveryCountry);
		stdRateAssertion.validateEcoTax(deliveryCountry, qty);
		productCalculationAssertions.validateVAT(deliveryCountry, productName, qty);
		productCalculationAssertions.validateTotal(deliveryCountry, productName, qty);

		String actualMsg = checkoutPage.confirmOrder();

		String expectedMsg = AppConstants.ORDER_PLACED_SUCCESS_MESSAGE;

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

		AppUtils appUtil = new AppUtils(accPage, searchResultsPage, productPage, cartPage, checkoutPage);
		appUtil.addProductsToCart();

		checkoutPage = appUtil.navigateToCheckOutPageAndFillDetails(billingCountry, deliveryCountry);

		assertShippingRateRadioButton(deliveryCountry);

		productInfoAssertions = new ProductInfoAssertions(softAssert, checkoutPage);
		productInfoAssertions.validateProductAssertions(deliveryCountry);
		productCalculationAssertions = new ProductCalculationAssertions(softAssert, checkoutPage);
		productCalculationAssertions.validateSubTotalForEachProduct(deliveryCountry);
		productCalculationAssertions.validateSubTotal(deliveryCountry);
		stdRateAssertion.validateFlatShippingRate(deliveryCountry);
		stdRateAssertion.validateTotalEcoTax(deliveryCountry);
		productCalculationAssertions.validateTotalVAT(deliveryCountry);
		productCalculationAssertions.validateTotalForMultipleProducts(deliveryCountry);
		String actualMsg = checkoutPage.confirmOrder();

		String expectedMsg = AppConstants.ORDER_PLACED_SUCCESS_MESSAGE;

		softAssert.assertEquals(actualMsg, expectedMsg, AppErrors.ORDER_PLACED_SUCCESS_MESSAGE_ERROR);
		softAssert.assertAll();
	}

	@DataProvider
	public Object[][] getBillingAndDeliveryCountries() {

		return new Object[][] { { "United Kingdom", "Japan" }, { "Japan", "United Kingdom" } };
	}

	// ***************************************************************************************************************************

	/*
	 * A private method to verify that the shipping radio button is selected on the
	 * checkout page and to verify the text against the radio button which will be
	 * based on the delivery country
	 */

	private void assertShippingRateRadioButton(String deliveryCountry) {
		softAssert = new SoftAssert();
		stdRateAssertion = new StandardRateAssertions(softAssert, checkoutPage);

		softAssert.assertTrue(checkoutPage.isFlatShippingRateRadioBtnSelected());

		stdRateAssertion.validateShippingRateInDeliveryMethodStep(deliveryCountry);

		checkoutPage.selectDeliverAndPaymentMethod();
	}

}
