package com.qa.opencart.tests;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.qa.opencart.base.BaseTest;
import com.qa.opencart.constants.AppConstants;
import com.qa.opencart.errors.AppErrors;
import com.qa.opencart.productassertions.ProductCalculationAssertions;
import com.qa.opencart.productassertions.ProductInfoAssertions;
import com.qa.opencart.productassertions.StandardRateAssertions;
import com.qa.opencart.utils.AppUtils;

public class PlaceMultipleItemsOrderTest extends BaseTest {

	@BeforeClass
	public void loginToTheApp() {
		accPage = loginPage.doLogin(prop.getProperty("username"), prop.getProperty("password"));
	}

	@Test(dataProvider = "getBillingAndDeliveryCountries")
	public void validatePlaceMultipleItemOrders(String billingCountry, String deliveryCountry) {

		AppUtils appUtil = new AppUtils(accPage, searchResultsPage, productPage, cartPage, checkoutPage);
		appUtil.addProductsToCart();

		checkoutPage = appUtil.navigateToCheckOutPageAndFillDetails(billingCountry, deliveryCountry);

		softAssert = new SoftAssert();
		stdRateAssertion = new StandardRateAssertions(softAssert, checkoutPage);

		softAssert.assertTrue(checkoutPage.isFlatShippingRateRadioBtnSelected());

		stdRateAssertion.validateShippingRateInDeliveryMethodStep(deliveryCountry);

		checkoutPage.selectDeliverAndPaymentMethod();

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

}
