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
import com.qa.opencart.utils.ExcelUtil;

public class PlaceOrderTest extends BaseTest{
	
	
	@BeforeClass
	public void loginToTheApp() {
		accPage = loginPage.doLogin(prop.getProperty("username"), prop.getProperty("password"));
	}
	
	@Test(dataProvider = "getPlaceOrderData")
	public void placeOrderDeliverInUK(String searchKey, String productName, String quantity, String billingCountry, String deliveryCountry) {
		
		int qty = Integer.parseInt(quantity);
		searchResultsPage = accPage.doSearch(searchKey);
		productPage = searchResultsPage.navigateToProductPage(productName); 
		productPage.addToCart(qty);
		cartPage = productPage.navigateToCart();
		checkoutPage = cartPage.doCheckOut();
		checkoutPage.selectBillingAndDeliveryDetails(billingCountry, deliveryCountry);
		softAssert = new SoftAssert();
		stdRateAssertion = new StandardRateAssertions(softAssert, checkoutPage);
		softAssert.assertTrue(checkoutPage.isFlatShippingRateRadioBtnSelected());
		stdRateAssertion.validateShippingRate(deliveryCountry);
		checkoutPage.selectDeliverAndPaymentMethod();
		
		productInfoAssertions = new ProductInfoAssertions(softAssert, checkoutPage);
		productInfoAssertions.validateProductAssertions(productName, qty);
		
		productCalculationAssertions = new ProductCalculationAssertions(softAssert, checkoutPage);
		
		productCalculationAssertions.validateTotalWithoutTaxes(productName, qty);
		productCalculationAssertions.validateSubTotal(productName, qty);
		stdRateAssertion.validateFlatShippingRate();
		stdRateAssertion.validateEcoTax(qty);
		productCalculationAssertions.validateVAT(productName, qty);
		productCalculationAssertions.validateTotal(productName, qty);
		String actualMsg = checkoutPage.confirmOrder();
		String expectedMsg = AppConstants.ORDER_PLACED_SUCCESS_MESSAGE;
		
		softAssert.assertEquals(actualMsg, expectedMsg, AppErrors.ORDER_PLACED_SUCCESS_MESSAGE_ERROR);
		softAssert.assertAll();
		
	}
	
	@DataProvider
	public Object[][] getPlaceOrderData() {
		return ExcelUtil.getTestData(AppConstants.PLACE_ORDER_SHEET_NAME);
	}
		
}
