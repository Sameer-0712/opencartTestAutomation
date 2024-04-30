package com.qa.opencart.productassertions;

import org.testng.asserts.SoftAssert;

import com.qa.opencart.constants.AppConstants;
import com.qa.opencart.errors.AppErrors;
import com.qa.opencart.pages.CheckoutPage;
import com.qa.opencart.utils.CostCalculation;
import com.qa.opencart.utils.ExcelUtil;
import com.qa.opencart.utils.StringUtil;

import io.qameta.allure.Step;

public class StandardRateAssertions {

	private SoftAssert softAssert;
	private CheckoutPage checkoutPage;

	public StandardRateAssertions(SoftAssert softAssert, CheckoutPage checkoutPage) {
		this.softAssert = softAssert;
		this.checkoutPage = checkoutPage;
	}

	@Step("Verify the shipping rate for the delivery country {0}")
	public void validateFlatShippingRate(String deliveryCountry) {
		softAssert.assertEquals(checkoutPage.getBreakUpDetails(deliveryCountry).get("FlatShippingRate"),
				AppConstants.getFlatShippingRateInString(), AppErrors.FLAT_SHIPPING_RATE_ERROR);
	}

	@Step("Verify the eco tax for the quantity {1}")
	public void validateEcoTax(String deliveryCountry, int quantity) {
		String actualEcoTax = null;
		if(deliveryCountry.equals(AppConstants.COUNTRY_WITH_TAXES)) {
			String expectedEcoTax = CostCalculation.calculateEcoTax(quantity);
			actualEcoTax = String
					.valueOf(StringUtil.removeSpecialCharacters(checkoutPage.getBreakUpDetails(deliveryCountry).get("EcoTax")));
			softAssert.assertEquals(actualEcoTax, expectedEcoTax, AppErrors.ECO_TAX_ERROR);
		}else {
			softAssert.assertNull(actualEcoTax);
		}	
	}

	@Step("Validate the shipping rate for {0} in the delivery method")
	public void validateShippingRateInDeliveryMethodStep(String deliveryCountry) {
		if (deliveryCountry.equals(AppConstants.COUNTRY_WITH_TAXES)) {
			softAssert.assertTrue(checkoutPage.getFlatShippingRateRadioBtnText()
					.contains(AppConstants.FLAT_SHIPPING_RATE_UK_DELIVERY));
		} else {
			softAssert.assertTrue(checkoutPage.getFlatShippingRateRadioBtnText()
					.contains(AppConstants.FLAT_SHIPPING_RATE_NON_UK_DELIVERY));
		}
	}
	
	//***************************Assertions for Multiple Products**********************************
	
	public void validateTotalEcoTax(String deliveryCountry) {
		String actualEcoTax = null;
		if(deliveryCountry.equals(AppConstants.COUNTRY_WITH_TAXES)) {
			String[] quantitiesStringArray = ExcelUtil.getColumnData(AppConstants.PLACE_MULTIPLE_ITEMS_ORDER_SHEET_NAME, "Quantity");
			int totalQuantity = StringUtil.getSumFromStringArray(quantitiesStringArray);
			String expectedEcoTax = CostCalculation.calculateEcoTax(totalQuantity);
			actualEcoTax = String
					.valueOf(StringUtil.removeSpecialCharacters(checkoutPage.getBreakUpDetails(deliveryCountry).get("EcoTax")));
			softAssert.assertEquals(actualEcoTax, expectedEcoTax, AppErrors.ECO_TAX_ERROR);
		}else {
			softAssert.assertNull(actualEcoTax);
		}	
	}

}
