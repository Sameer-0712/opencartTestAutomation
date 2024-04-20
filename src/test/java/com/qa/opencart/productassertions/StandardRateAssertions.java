package com.qa.opencart.productassertions;

import org.testng.asserts.SoftAssert;

import com.qa.opencart.constants.AppConstants;
import com.qa.opencart.errors.AppErrors;
import com.qa.opencart.pages.CheckoutPage;
import com.qa.opencart.utils.CostCalculation;
import com.qa.opencart.utils.StringUtil;

public class StandardRateAssertions {

	private SoftAssert softAssert;
	private CheckoutPage checkoutPage;

	public StandardRateAssertions(SoftAssert softAssert, CheckoutPage checkoutPage) {
		this.softAssert = softAssert;
		this.checkoutPage = checkoutPage;
	}

	public void validateFlatShippingRate(String deliveryCountry) {
		softAssert.assertEquals(checkoutPage.getOrderDetails(deliveryCountry).get("FlatShippingRate"),
				AppConstants.getFlatShippingRateInString(), AppErrors.FLAT_SHIPPING_RATE_ERROR);
	}

	public void validateEcoTax(String deliveryCountry, int quantity) {
		String actualEcoTax = null;
		if(deliveryCountry.equals("United Kingdom")) {
			String expectedEcoTax = CostCalculation.calculateEcoTax(quantity);
			actualEcoTax = String
					.valueOf(StringUtil.removeSpecialCharacters(checkoutPage.getOrderDetails(deliveryCountry).get("EcoTax")));
			softAssert.assertEquals(actualEcoTax, expectedEcoTax, AppErrors.ECO_TAX_ERROR);
		}else {
			softAssert.assertNull(actualEcoTax);
		}	
	}

	public void validateShippingRateInDeliveryMethodStep(String deliveryCountry) {
		if (deliveryCountry.equals("United Kingdom")) {
			softAssert.assertTrue(checkoutPage.getFlatShippingRateRadioBtnText()
					.contains(AppConstants.FLAT_SHIPPING_RATE_UK_DELIVERY));
		} else {
			softAssert.assertTrue(checkoutPage.getFlatShippingRateRadioBtnText()
					.contains(AppConstants.FLAT_SHIPPING_RATE_NON_UK_DELIVERY));
		}
	}

}
