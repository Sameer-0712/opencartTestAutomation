package com.qa.opencart.productassertions;

import org.testng.asserts.SoftAssert;

import com.qa.opencart.constants.AppConstants;
import com.qa.opencart.errors.AppErrors;
import com.qa.opencart.pages.CheckoutPage;
import com.qa.opencart.utils.CostCalculation;
import com.qa.opencart.utils.StringUtil;

public class ProductCalculationAssertions {

	private SoftAssert softAssert;
	private CheckoutPage checkoutPage;

	public ProductCalculationAssertions(SoftAssert softAssert, CheckoutPage checkoutPage) {
		this.softAssert = softAssert;
		this.checkoutPage = checkoutPage;
	}

	public void validateTotalWithoutTaxes(String productName, int quantity) {
		String expectedTotalWithoutTaxes = null;
		String actualTotalWithoutTaxes = null;

		switch (productName.trim()) {
		case "Sony VAIO":
			expectedTotalWithoutTaxes = CostCalculation
					.calculateTotalPriceWithoutTaxes(AppConstants.UNIT_PRICE_SONY_VAIO, quantity);
			actualTotalWithoutTaxes = String.valueOf(
					StringUtil.removeSpecialCharacters(checkoutPage.getOrderDetailsForUK().get("TotalBeforeTaxes")));
			softAssert.assertEquals(actualTotalWithoutTaxes, expectedTotalWithoutTaxes,
					AppErrors.TOTAL_BEFORE_TAXES_ERROR);
			break;
		case "iPod Classic":
			expectedTotalWithoutTaxes = CostCalculation
					.calculateTotalPriceWithoutTaxes(AppConstants.UNIT_PRICE_IPOD_CLASSIC, quantity);
			actualTotalWithoutTaxes = String.valueOf(
					StringUtil.removeSpecialCharacters(checkoutPage.getOrderDetailsForUK().get("TotalBeforeTaxes")));
			softAssert.assertEquals(actualTotalWithoutTaxes, expectedTotalWithoutTaxes,
					AppErrors.TOTAL_BEFORE_TAXES_ERROR);
			break;
		case "HP LP3065":
			expectedTotalWithoutTaxes = CostCalculation.calculateTotalPriceWithoutTaxes(AppConstants.UNIT_PRICE_HP_LP,
					quantity);
			actualTotalWithoutTaxes = String.valueOf(
					StringUtil.removeSpecialCharacters(checkoutPage.getOrderDetailsForUK().get("TotalBeforeTaxes")));
			softAssert.assertEquals(actualTotalWithoutTaxes, expectedTotalWithoutTaxes,
					AppErrors.TOTAL_BEFORE_TAXES_ERROR);
			break;
		case "MacBook Pro":
			expectedTotalWithoutTaxes = CostCalculation
					.calculateTotalPriceWithoutTaxes(AppConstants.UNIT_PRICE_MACBOOK_PRO, quantity);
			actualTotalWithoutTaxes = String.valueOf(
					StringUtil.removeSpecialCharacters(checkoutPage.getOrderDetailsForUK().get("TotalBeforeTaxes")));
			softAssert.assertEquals(actualTotalWithoutTaxes, expectedTotalWithoutTaxes,
					AppErrors.TOTAL_BEFORE_TAXES_ERROR);
			break;
		default:
			break;
		}
	}

	public void validateSubTotal(String productName, int quantity) {
		String expectedSubTotal = null;
		String actualSubTotal = null;

		switch (productName.trim()) {
		case "Sony VAIO":
			expectedSubTotal = CostCalculation.calculateTotalPriceWithoutTaxes(AppConstants.PRICE_EXTAX_SONY_VAIO,
					quantity);
			actualSubTotal = String
					.valueOf(StringUtil.removeSpecialCharacters(checkoutPage.getOrderDetailsForUK().get("SubTotal")));
			softAssert.assertEquals(actualSubTotal, expectedSubTotal, AppErrors.SUB_TOTAL_ERROR);
			break;
		case "iPod Classic":
			expectedSubTotal = CostCalculation.calculateTotalPriceWithoutTaxes(AppConstants.PRICE_EXTAX_IPOD_CLASSIC,
					quantity);
			actualSubTotal = String
					.valueOf(StringUtil.removeSpecialCharacters(checkoutPage.getOrderDetailsForUK().get("SubTotal")));
			softAssert.assertEquals(actualSubTotal, expectedSubTotal, AppErrors.SUB_TOTAL_ERROR);
			break;
		case "HP LP3065":
			expectedSubTotal = CostCalculation.calculateTotalPriceWithoutTaxes(AppConstants.PRICE_EXTAX_HP_LP,
					quantity);
			actualSubTotal = String
					.valueOf(StringUtil.removeSpecialCharacters(checkoutPage.getOrderDetailsForUK().get("SubTotal")));
			softAssert.assertEquals(actualSubTotal, expectedSubTotal, AppErrors.SUB_TOTAL_ERROR);
			break;
		case "MacBook Pro":
			expectedSubTotal = CostCalculation.calculateTotalPriceWithoutTaxes(AppConstants.PRICE_EXTAX_MACBOOK_PRO,
					quantity);
			actualSubTotal = String
					.valueOf(StringUtil.removeSpecialCharacters(checkoutPage.getOrderDetailsForUK().get("SubTotal")));
			softAssert.assertEquals(actualSubTotal, expectedSubTotal, AppErrors.SUB_TOTAL_ERROR);
			break;
		default:
			break;
		}
	}

	public void validateVAT(String productName, int quantity) {
		String expectedVAT = null;
		String actualVAT = null;

		switch (productName.trim()) {
		case "Sony VAIO":
			expectedVAT = CostCalculation.calculateVATForUK(AppConstants.PRICE_EXTAX_SONY_VAIO, quantity);
			actualVAT = String
					.valueOf(StringUtil.removeSpecialCharacters(checkoutPage.getOrderDetailsForUK().get("VAT")));
			softAssert.assertEquals(actualVAT, expectedVAT, AppErrors.VAT_ERROR);
			break;
		case "iPod Classic":
			expectedVAT = CostCalculation.calculateVATForUK(AppConstants.PRICE_EXTAX_IPOD_CLASSIC, quantity);
			actualVAT = String
					.valueOf(StringUtil.removeSpecialCharacters(checkoutPage.getOrderDetailsForUK().get("VAT")));
			softAssert.assertEquals(actualVAT, expectedVAT, AppErrors.VAT_ERROR);
			break;
		case "HP LP3065":
			expectedVAT = CostCalculation.calculateVATForUK(AppConstants.PRICE_EXTAX_HP_LP, quantity);
			actualVAT = String
					.valueOf(StringUtil.removeSpecialCharacters(checkoutPage.getOrderDetailsForUK().get("VAT")));
			softAssert.assertEquals(actualVAT, expectedVAT, AppErrors.VAT_ERROR);
			break;
		case "MacBook Pro":
			expectedVAT = CostCalculation.calculateVATForUK(AppConstants.PRICE_EXTAX_MACBOOK_PRO, quantity);
			actualVAT = String
					.valueOf(StringUtil.removeSpecialCharacters(checkoutPage.getOrderDetailsForUK().get("VAT")));
			softAssert.assertEquals(actualVAT, expectedVAT, AppErrors.VAT_ERROR);
			break;
		default:
			break;
		}
	}

	public void validateTotal(String productName, int quantity) {
		String expectedTotal = null;
		String actualTotal = null;

		switch (productName.trim()) {
		case "Sony VAIO":
			expectedTotal = CostCalculation.calculateTotalPriceForUK(AppConstants.PRICE_EXTAX_SONY_VAIO, quantity);
			actualTotal = String
					.valueOf(StringUtil.removeSpecialCharacters(checkoutPage.getOrderDetailsForUK().get("Total")));
			softAssert.assertEquals(actualTotal, expectedTotal, AppErrors.TOTAL_ERROR);
			break;
		case "iPod Classic":
			expectedTotal = CostCalculation.calculateTotalPriceForUK(AppConstants.PRICE_EXTAX_IPOD_CLASSIC, quantity);
			actualTotal = String
					.valueOf(StringUtil.removeSpecialCharacters(checkoutPage.getOrderDetailsForUK().get("Total")));
			softAssert.assertEquals(actualTotal, expectedTotal, AppErrors.TOTAL_ERROR);
			break;
		case "HP LP3065":
			expectedTotal = CostCalculation.calculateTotalPriceForUK(AppConstants.PRICE_EXTAX_HP_LP, quantity);
			actualTotal = String
					.valueOf(StringUtil.removeSpecialCharacters(checkoutPage.getOrderDetailsForUK().get("Total")));
			softAssert.assertEquals(actualTotal, expectedTotal, AppErrors.TOTAL_ERROR);
			break;
		case "MacBook Pro":
			expectedTotal = CostCalculation.calculateTotalPriceForUK(AppConstants.PRICE_EXTAX_MACBOOK_PRO, quantity);
			actualTotal = String
					.valueOf(StringUtil.removeSpecialCharacters(checkoutPage.getOrderDetailsForUK().get("Total")));
			softAssert.assertEquals(actualTotal, expectedTotal, AppErrors.TOTAL_ERROR);
			break;
		default:
			break;
		}
	}
}
