package com.qa.opencart.productassertions;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.testng.asserts.SoftAssert;

import com.qa.opencart.constants.AppConstants;
import com.qa.opencart.errors.AppErrors;
import com.qa.opencart.pages.CheckoutPage;
import com.qa.opencart.utils.CostCalculation;
import com.qa.opencart.utils.ExcelUtil;
import com.qa.opencart.utils.StringUtil;

import io.qameta.allure.Step;

public class ProductCalculationAssertions {

	private SoftAssert softAssert;
	private CheckoutPage checkoutPage; 
	
	public ProductCalculationAssertions(SoftAssert softAssert, CheckoutPage checkoutPage) {
		this.softAssert = softAssert;
		this.checkoutPage = checkoutPage;
	}
	
	@Step("Fetching the actual toatl before taxes")
	private String getTotalWithoutTaxesFromList(String productName) {
		return checkoutPage.getProductDetailsInMap().get(productName)[3];
	}

	@Step("Verify the total before taxes for the delivery country {0}, product name {1} with quantity {2}")
	public void validateTotalWithoutTaxes(String deliveryCountry, String productName, int quantity) {
		String expectedTotalWithoutTaxes = null;
		String actualTotalWithoutTaxes = null;

		switch (productName.trim()) {
		case "Sony VAIO":
			if (deliveryCountry.equals(AppConstants.COUNTRY_WITH_TAXES)) {
				expectedTotalWithoutTaxes = CostCalculation
						.calculateTotalPriceWithoutTaxes(AppConstants.UNIT_PRICE_SONY_VAIO, quantity);
			} else {
				expectedTotalWithoutTaxes = CostCalculation
						.calculateTotalPriceWithoutTaxes(AppConstants.PRICE_EXTAX_SONY_VAIO, quantity);
			}
			actualTotalWithoutTaxes = String.valueOf(StringUtil.removeSpecialCharacters(getTotalWithoutTaxesFromList("Sony VAIO")));
			softAssert.assertEquals(actualTotalWithoutTaxes, expectedTotalWithoutTaxes,
					AppErrors.TOTAL_BEFORE_TAXES_ERROR);
			break;
			
		case "iPod Classic":
			if (deliveryCountry.equals(AppConstants.COUNTRY_WITH_TAXES)) {
				expectedTotalWithoutTaxes = CostCalculation
						.calculateTotalPriceWithoutTaxes(AppConstants.UNIT_PRICE_IPOD_CLASSIC, quantity);
			} else {
				expectedTotalWithoutTaxes = CostCalculation
						.calculateTotalPriceWithoutTaxes(AppConstants.PRICE_EXTAX_IPOD_CLASSIC, quantity);
			}
			actualTotalWithoutTaxes = String.valueOf(StringUtil.removeSpecialCharacters(getTotalWithoutTaxesFromList("iPod Classic")));
			softAssert.assertEquals(actualTotalWithoutTaxes, expectedTotalWithoutTaxes,
					AppErrors.TOTAL_BEFORE_TAXES_ERROR);
			break;
			
		case "HP LP3065":
			if (deliveryCountry.equals(AppConstants.COUNTRY_WITH_TAXES)) {
				expectedTotalWithoutTaxes = CostCalculation
						.calculateTotalPriceWithoutTaxes(AppConstants.UNIT_PRICE_HP_LP, quantity);
			} else {
				expectedTotalWithoutTaxes = CostCalculation
						.calculateTotalPriceWithoutTaxes(AppConstants.PRICE_EXTAX_HP_LP, quantity);
			}
			actualTotalWithoutTaxes = String.valueOf(StringUtil.removeSpecialCharacters(getTotalWithoutTaxesFromList("HP LP3065")));
			softAssert.assertEquals(actualTotalWithoutTaxes, expectedTotalWithoutTaxes,
					AppErrors.TOTAL_BEFORE_TAXES_ERROR);
			break;
			
		case "MacBook Pro":
			if (deliveryCountry.equals(AppConstants.COUNTRY_WITH_TAXES)) {
				expectedTotalWithoutTaxes = CostCalculation
						.calculateTotalPriceWithoutTaxes(AppConstants.UNIT_PRICE_MACBOOK_PRO, quantity);
			} else {
				expectedTotalWithoutTaxes = CostCalculation
						.calculateTotalPriceWithoutTaxes(AppConstants.PRICE_EXTAX_MACBOOK_PRO, quantity);
			}
			actualTotalWithoutTaxes = String.valueOf(StringUtil.removeSpecialCharacters(getTotalWithoutTaxesFromList("MacBook Pro")));
			softAssert.assertEquals(actualTotalWithoutTaxes, expectedTotalWithoutTaxes,
					AppErrors.TOTAL_BEFORE_TAXES_ERROR);
			break;
			
		default:
			break;
		}
	}

	@Step("Verify the subtotal for the delivery country {0}, product {1} with quantity {2}")
	public void validateSubTotal(String deliveryCountry, String productName, int quantity) {
		String expectedSubTotal = null;
		String actualSubTotal = null;

		switch (productName.trim()) {
		case "Sony VAIO":
			expectedSubTotal = CostCalculation.calculateTotalPriceWithoutTaxes(AppConstants.PRICE_EXTAX_SONY_VAIO,
					quantity);
			actualSubTotal = String.valueOf(
					StringUtil.removeSpecialCharacters(checkoutPage.getBreakUpDetails(deliveryCountry).get("SubTotal")));
			softAssert.assertEquals(actualSubTotal, expectedSubTotal, AppErrors.SUB_TOTAL_ERROR);
			break;
		case "iPod Classic":
			expectedSubTotal = CostCalculation.calculateTotalPriceWithoutTaxes(AppConstants.PRICE_EXTAX_IPOD_CLASSIC,
					quantity);
			actualSubTotal = String.valueOf(
					StringUtil.removeSpecialCharacters(checkoutPage.getBreakUpDetails(deliveryCountry).get("SubTotal")));
			softAssert.assertEquals(actualSubTotal, expectedSubTotal, AppErrors.SUB_TOTAL_ERROR);
			break;
		case "HP LP3065":
			expectedSubTotal = CostCalculation.calculateTotalPriceWithoutTaxes(AppConstants.PRICE_EXTAX_HP_LP,
					quantity);
			actualSubTotal = String.valueOf(
					StringUtil.removeSpecialCharacters(checkoutPage.getBreakUpDetails(deliveryCountry).get("SubTotal")));
			softAssert.assertEquals(actualSubTotal, expectedSubTotal, AppErrors.SUB_TOTAL_ERROR);
			break;
		case "MacBook Pro":
			expectedSubTotal = CostCalculation.calculateTotalPriceWithoutTaxes(AppConstants.PRICE_EXTAX_MACBOOK_PRO,
					quantity);
			actualSubTotal = String.valueOf(
					StringUtil.removeSpecialCharacters(checkoutPage.getBreakUpDetails(deliveryCountry).get("SubTotal")));
			softAssert.assertEquals(actualSubTotal, expectedSubTotal, AppErrors.SUB_TOTAL_ERROR);
			break;
		default:
			break;
		}
	}

	@Step("Verify the VAT for the delivery country {0}, product {1} with quantity {2}")
	public void validateVAT(String deliveryCountry, String productName, int quantity) {
		String expectedVAT = null;
		String actualVAT = null;

		switch (productName.trim()) {
		case "Sony VAIO":
			if (deliveryCountry.equals(AppConstants.COUNTRY_WITH_TAXES)) {
				expectedVAT = CostCalculation.calculateVAT(AppConstants.PRICE_EXTAX_SONY_VAIO, quantity);
				actualVAT = String.valueOf(
						StringUtil.removeSpecialCharacters(checkoutPage.getBreakUpDetails(deliveryCountry).get("VAT")));
				softAssert.assertEquals(actualVAT, expectedVAT, AppErrors.VAT_ERROR);
			} else {
				softAssert.assertNull(actualVAT);
			}

			break;
		case "iPod Classic":
			if (deliveryCountry.equals(AppConstants.COUNTRY_WITH_TAXES)) {
				expectedVAT = CostCalculation.calculateVAT(AppConstants.PRICE_EXTAX_IPOD_CLASSIC, quantity);
				actualVAT = String.valueOf(
						StringUtil.removeSpecialCharacters(checkoutPage.getBreakUpDetails(deliveryCountry).get("VAT")));
				softAssert.assertEquals(actualVAT, expectedVAT, AppErrors.VAT_ERROR);
			} else {
				softAssert.assertNull(actualVAT);
			}

			break;
		case "HP LP3065":
			if (deliveryCountry.equals(AppConstants.COUNTRY_WITH_TAXES)) {
				expectedVAT = CostCalculation.calculateVAT(AppConstants.PRICE_EXTAX_HP_LP, quantity);
				actualVAT = String.valueOf(
						StringUtil.removeSpecialCharacters(checkoutPage.getBreakUpDetails(deliveryCountry).get("VAT")));
				softAssert.assertEquals(actualVAT, expectedVAT, AppErrors.VAT_ERROR);
			} else {
				softAssert.assertNull(actualVAT);
			}
			break;
		case "MacBook Pro":
			if (deliveryCountry.equals(AppConstants.COUNTRY_WITH_TAXES)) {
				expectedVAT = CostCalculation.calculateVAT(AppConstants.PRICE_EXTAX_MACBOOK_PRO, quantity);
				actualVAT = String.valueOf(
						StringUtil.removeSpecialCharacters(checkoutPage.getBreakUpDetails(deliveryCountry).get("VAT")));
				softAssert.assertEquals(actualVAT, expectedVAT, AppErrors.VAT_ERROR);
			} else {
				softAssert.assertNull(actualVAT);
			}
			break;
		default:
			break;
		}
	}

	@Step("Verify the total for delivery country {0}, product {1} with quantity {2}")
	public void validateTotal(String deliveryCountry, String productName, int quantity) {
		String expectedTotal = null;
		String actualTotal = null;

		switch (productName.trim()) {
		case "Sony VAIO":
			expectedTotal = CostCalculation.calculateTotalPrice(deliveryCountry, AppConstants.PRICE_EXTAX_SONY_VAIO,
					quantity);
			actualTotal = String.valueOf(
					StringUtil.removeSpecialCharacters(checkoutPage.getBreakUpDetails(deliveryCountry).get("Total")));
			softAssert.assertEquals(actualTotal, expectedTotal, AppErrors.TOTAL_ERROR);
			break;
		case "iPod Classic":
			expectedTotal = CostCalculation.calculateTotalPrice(deliveryCountry, AppConstants.PRICE_EXTAX_IPOD_CLASSIC,
					quantity);
			actualTotal = String.valueOf(
					StringUtil.removeSpecialCharacters(checkoutPage.getBreakUpDetails(deliveryCountry).get("Total")));
			softAssert.assertEquals(actualTotal, expectedTotal, AppErrors.TOTAL_ERROR);
			break;
		case "HP LP3065":
			expectedTotal = CostCalculation.calculateTotalPrice(deliveryCountry, AppConstants.PRICE_EXTAX_HP_LP,
					quantity);
			actualTotal = String.valueOf(
					StringUtil.removeSpecialCharacters(checkoutPage.getBreakUpDetails(deliveryCountry).get("Total")));
			softAssert.assertEquals(actualTotal, expectedTotal, AppErrors.TOTAL_ERROR);
			break;
		case "MacBook Pro":
			expectedTotal = CostCalculation.calculateTotalPrice(deliveryCountry, AppConstants.PRICE_EXTAX_MACBOOK_PRO,
					quantity);
			actualTotal = String.valueOf(
					StringUtil.removeSpecialCharacters(checkoutPage.getBreakUpDetails(deliveryCountry).get("Total")));
			softAssert.assertEquals(actualTotal, expectedTotal, AppErrors.TOTAL_ERROR);
			break;
		default:
			break;
		}
	}
	
	//************************************Validation for multiple items************************************
	
		public void validateSubTotalForEachProduct(String deliveryCountry) {
			
			Map<String, String[]> actualProductDetails = checkoutPage.getProductDetailsInMap();
			Map<String,String> productQuantityMap = ExcelUtil.getProductQuantityMap();
			
			//verifying unit prices...
			Map<String,Double> actualProductTotalWithoutTaxesMap = new HashMap<String,Double>();
			Map<String,Double> expectedProductTotalWithoutTaxesMap = new HashMap<String,Double>();
			for (Entry<String, String[]> entry : actualProductDetails.entrySet()) {
				actualProductTotalWithoutTaxesMap.put(entry.getKey(), StringUtil.removeSpecialCharacters(entry.getValue()[3]));
			}
			for(Entry<String, String> entry : productQuantityMap.entrySet()) {
				double costWithoutTaxes =  CostCalculation.calculateTotalPriceWithoutTaxesBasedOnProductNameQuantity(entry.getKey(), Integer.parseInt(entry.getValue()));
				expectedProductTotalWithoutTaxesMap.put(entry.getKey(), costWithoutTaxes);
			}		
			
			softAssert.assertEquals(actualProductTotalWithoutTaxesMap, expectedProductTotalWithoutTaxesMap, AppErrors.SUB_TOTAL_EACH_PRODUCT_ERROR);
					
		}
		
		public void validateSubTotal(String deliveryCountry) {		
			String subTotalString = checkoutPage.getBreakUpDetails(deliveryCountry).get("SubTotal");
			double actualSubTotal = StringUtil.removeSpecialCharacters(subTotalString);
			
			String expectedSubTotalString = CostCalculation.calculateSubTotalUsingProductNameQuantity(ExcelUtil.getProductQuantityMap());
			double expectedSubTotal = StringUtil.removeSpecialCharacters(expectedSubTotalString);
			
			softAssert.assertEquals(actualSubTotal, expectedSubTotal, AppErrors.SUB_TOTAL_MULTIPLE_PRODUCTS_ERROR);
		}
		
		public void validateTotalVAT(String deliveryCountry) {
	
			double actualTotalVAT = 0.0;
			double expectedTotalVAT = 0.0;
			if(deliveryCountry.equals(AppConstants.COUNTRY_WITH_TAXES)) {
				String actualVATString = checkoutPage.getBreakUpDetails(deliveryCountry).get("VAT");
				actualTotalVAT = StringUtil.removeSpecialCharacters(actualVATString);
				
				String expectedVATString = CostCalculation.calculateTotalVAT(ExcelUtil.getProductQuantityMap());
				expectedTotalVAT = StringUtil.removeSpecialCharacters(expectedVATString);
			}	
			
			softAssert.assertEquals(actualTotalVAT, expectedTotalVAT, AppErrors.TOTAL_VAT_MULTIPLE_PRODUCTS_ERROR);
			
		}
		
		public void validateTotalForMultipleProducts(String deliveryCountry) {
			
			String expectedTotal = CostCalculation.calculateTotalForMultipleProducts(ExcelUtil.getProductQuantityMap(), deliveryCountry);
			String actualTotal = String.valueOf(StringUtil.removeSpecialCharacters(checkoutPage.getBreakUpDetails(deliveryCountry).get("Total")));
			softAssert.assertEquals(actualTotal, expectedTotal, AppErrors.TOTAL_ERROR);
		}
}
