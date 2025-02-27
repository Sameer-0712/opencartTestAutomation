package com.qa.opencart.productassertions;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.qa.opencart.calculationdecorator.CalculateTotal;
import com.qa.opencart.calculationdecorator.TaxCalculation;
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

	@Step("Verify the total in the product details table for the delivery country {0}, product name {1} with quantity {2}")
	public void validateTotalInProductDetailsTable(String deliveryCountry, String productName, int quantity) {
		double expectedTotalInProductDetailsTable = 0.0;
		double actualTotalInProductDetailsTable = 0;

		if(deliveryCountry.equals(AppConstants.COUNTRY_WITH_TAXES)){
			expectedTotalInProductDetailsTable = CostCalculation.calculateUnitPrice(productName,quantity);
		}else{
			expectedTotalInProductDetailsTable = CostCalculation.calculateSubTotalForSingleProduct(productName, quantity);
		}

		switch (productName.trim()) {
		case "Sony VAIO":
			actualTotalInProductDetailsTable = StringUtil.removeSpecialCharacters(getTotalWithoutTaxesFromList("Sony VAIO"));
			softAssert.assertEquals(actualTotalInProductDetailsTable, expectedTotalInProductDetailsTable,
					AppErrors.TOTAL_BEFORE_TAXES_ERROR);
			break;
			
		case "iPod Classic":
			actualTotalInProductDetailsTable = StringUtil.removeSpecialCharacters(getTotalWithoutTaxesFromList("iPod Classic"));
			softAssert.assertEquals(actualTotalInProductDetailsTable, expectedTotalInProductDetailsTable,
					AppErrors.TOTAL_BEFORE_TAXES_ERROR);
			break;
			
		case "HP LP3065":
			actualTotalInProductDetailsTable = StringUtil.removeSpecialCharacters(getTotalWithoutTaxesFromList("HP LP3065"));
			softAssert.assertEquals(actualTotalInProductDetailsTable, expectedTotalInProductDetailsTable,
					AppErrors.TOTAL_BEFORE_TAXES_ERROR);
			break;
			
		case "MacBook Pro":
			actualTotalInProductDetailsTable = StringUtil.removeSpecialCharacters(getTotalWithoutTaxesFromList("MacBook Pro"));
			softAssert.assertEquals(actualTotalInProductDetailsTable, expectedTotalInProductDetailsTable,
					AppErrors.TOTAL_BEFORE_TAXES_ERROR);
			break;
			
		default:
			break;
		}
	}

	@Step("Verify the subtotal in the Cost Break Up table for the delivery country {0}, product {1} and quantity {2}")
	public void validateSubTotalInCostBreakUpTable(String deliveryCountry, String productName, int quantity) {
		double expectedSubTotal = CostCalculation.calculateSubTotalForSingleProduct(productName, quantity);
		double actualSubTotal = 0;

		actualSubTotal = StringUtil.removeSpecialCharacters(checkoutPage.getBreakUpDetails(deliveryCountry).get("SubTotal"));
		softAssert.assertEquals(actualSubTotal, expectedSubTotal, AppErrors.SUB_TOTAL_ERROR);

	}

	@Step("Verify the VAT for the delivery country {0}, product {1} with quantity {2}")
	public void validateVATInCostBreakUpTable(String deliveryCountry, String productName, int quantity) {
		double expectedVAT = TaxCalculation.calculateVAT(CostCalculation.calculateSubTotalForSingleProduct(productName,quantity));
		double actualVAT = 0.0;

		if (deliveryCountry.equals(AppConstants.COUNTRY_WITH_TAXES)) {
			actualVAT = StringUtil.removeSpecialCharacters(checkoutPage.getBreakUpDetails(deliveryCountry).get("VAT"));
			softAssert.assertEquals(actualVAT, expectedVAT, AppErrors.VAT_ERROR);
		} else {
			softAssert.assertEquals(actualVAT,0.0, AppErrors.VAT_ERROR);
		}

	}

	@Step("Verify the total for delivery country {0}, product {1} with quantity {2}")
	public void validateTotalInCostBreakUpTable(String deliveryCountry, String productName, int quantity) {
		double expectedTotal = CostCalculation.calculateTotalForSingleProduct(deliveryCountry,productName,quantity);
		double actualTotal = 0;
		actualTotal = StringUtil.removeSpecialCharacters(checkoutPage.getBreakUpDetails(deliveryCountry).get("Total"));
		softAssert.assertEquals(actualTotal, expectedTotal, AppErrors.TOTAL_ERROR);

	}
	
	//************************************Validation for multiple items************************************
	
		@Step("Verify the sub total for each product for country {0}")
		public void validateTotalForEachProductInProductDetailsTable(String deliveryCountry) {
			
			Map<String, String[]> actualProductDetails = checkoutPage.getProductDetailsInMap();
			Map<String,Integer> productQuantityMap = ExcelUtil.getProductQuantityMap();
			
			//verifying unit prices...
			Map<String,Double> actualProductTotalWithoutTaxesMap = new HashMap<String,Double>();
			Map<String,Double> expectedProductTotalWithoutTaxesMap = new HashMap<String,Double>();
			for (Entry<String, String[]> entry : actualProductDetails.entrySet()) {
				actualProductTotalWithoutTaxesMap.put(entry.getKey(), StringUtil.removeSpecialCharacters(entry.getValue()[3]));
			}
			double costWithoutTaxes =  0.0;
			for(Entry<String, Integer> entry : productQuantityMap.entrySet()) {
				if(deliveryCountry.equals(AppConstants.COUNTRY_WITH_TAXES)){
					costWithoutTaxes = CostCalculation.calculateUnitPrice(entry.getKey(), entry.getValue());
				}else{
					costWithoutTaxes =  CostCalculation.calculateSubTotalForSingleProduct(entry.getKey(), entry.getValue());
				}
				expectedProductTotalWithoutTaxesMap.put(entry.getKey(), costWithoutTaxes);
			}		
			
			softAssert.assertEquals(actualProductTotalWithoutTaxesMap, expectedProductTotalWithoutTaxesMap, AppErrors.SUB_TOTAL_EACH_PRODUCT_ERROR);
					
		}
		
		@Step("Verify the subtotal for delivery country {0}")
		public void validateSubTotalInCostBreakUpTable(String deliveryCountry) {
			String subTotalString = checkoutPage.getBreakUpDetails(deliveryCountry).get("SubTotal");
			double actualSubTotal = StringUtil.removeSpecialCharacters(subTotalString);
			double expectedSubTotal = CostCalculation.calculateSubTotalForMultipleProducts(ExcelUtil.getProductQuantityMap());
			softAssert.assertEquals(actualSubTotal, expectedSubTotal, AppErrors.SUB_TOTAL_MULTIPLE_PRODUCTS_ERROR);
		}
		
		@Step("Verify the total VAT for delivery country {0}")
		public void validateTotalVATInCostBreakUpTable(String deliveryCountry) {
	
			double actualTotalVAT = 0.0;
			double expectedTotalVAT = 0.0;
			if(deliveryCountry.equals(AppConstants.COUNTRY_WITH_TAXES)) {
				String actualVATString = checkoutPage.getBreakUpDetails(deliveryCountry).get("VAT");
				actualTotalVAT = StringUtil.removeSpecialCharacters(actualVATString);
				
				expectedTotalVAT = CostCalculation.calculateVATForMultipleProducts(ExcelUtil.getProductQuantityMap());
			}
			
			softAssert.assertEquals(actualTotalVAT, expectedTotalVAT, AppErrors.TOTAL_VAT_MULTIPLE_PRODUCTS_ERROR);
			
		}
		
		@Step("Verify the total for all the products for delivery country {0}")
		public void validateTotalForMultipleProducts(String deliveryCountry) {
			
			double expectedTotal = CostCalculation.calculateTotalForMultipleProducts(deliveryCountry, ExcelUtil.getProductQuantityMap());
			double actualTotal = StringUtil.removeSpecialCharacters(checkoutPage.getBreakUpDetails(deliveryCountry).get("Total"));
			softAssert.assertEquals(actualTotal, expectedTotal, AppErrors.TOTAL_ERROR);
		}
}
