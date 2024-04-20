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
	public void validatePlaceSingleItemOrders(String searchKey, String productName, String quantity, String billingCountry, String deliveryCountry) {
		
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
		
		stdRateAssertion.validateShippingRateInDeliveryMethodStep(deliveryCountry);
		
		checkoutPage.selectDeliverAndPaymentMethod();
		
		productInfoAssertions = new ProductInfoAssertions(softAssert, checkoutPage);
		productInfoAssertions.validateProductAssertions(deliveryCountry,productName, qty);
		productCalculationAssertions = new ProductCalculationAssertions(softAssert, checkoutPage);		
		productCalculationAssertions.validateTotalWithoutTaxes(deliveryCountry,productName, qty);
		productCalculationAssertions.validateSubTotal(deliveryCountry,productName, qty);
		stdRateAssertion.validateFlatShippingRate(deliveryCountry);
		stdRateAssertion.validateEcoTax(deliveryCountry,qty);
		productCalculationAssertions.validateVAT(deliveryCountry,productName, qty);
		productCalculationAssertions.validateTotal(deliveryCountry,productName, qty);
		
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
