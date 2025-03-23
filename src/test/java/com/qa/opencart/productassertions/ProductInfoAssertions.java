package com.qa.opencart.productassertions;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.qa.opencart.utils.StringUtil;
import org.testng.asserts.SoftAssert;

import com.qa.opencart.constants.AppConstants;
import com.qa.opencart.errors.AppErrors;
import com.qa.opencart.pages.CheckoutPage;
import com.qa.opencart.utils.ExcelUtil;

import io.qameta.allure.Step;

public class ProductInfoAssertions {

	private SoftAssert softAssert;
	private CheckoutPage checkoutPage;

	public ProductInfoAssertions(SoftAssert softAssert, CheckoutPage checkoutPage) {
		this.softAssert = softAssert;
		this.checkoutPage = checkoutPage;
	}

	@Step("Fetch the model for the product: {0}")
	private String getModelFromList(String productName) {
		return checkoutPage.getProductDetailsInMap().get(productName)[0];
	}
	
	@Step("Fetch the quantity for the product: {0}")
	private String getQuantityFromList(String productName) {
		return checkoutPage.getProductDetailsInMap().get(productName)[1];
	}

	@Step("Fetch the unit price for the product: {0}")
	private String getUnitPriceFromList(String productName) {
		return checkoutPage.getProductDetailsInMap().get(productName)[2];
	}

	@Step("Verify the details for the product: {1}")
	public void validateProductDetailsForAllProducts(String deliveryCountry, String productName, int quantity) {

		switch (productName.trim()) {
		case "Sony VAIO":
			softAssert.assertTrue(checkoutPage.getProductDetailsInMap().containsKey("Sony VAIO"));
			softAssert.assertEquals(getModelFromList("Sony VAIO"), AppConstants.PRODUCT_CODE_SONY_VAIO);
			softAssert.assertEquals(getQuantityFromList("Sony VAIO"), String.valueOf(quantity));
			if (deliveryCountry.equals(AppConstants.COUNTRY_WITH_TAXES)) {
				softAssert.assertEquals(StringUtil.removeSpecialCharacters(getUnitPriceFromList("Sony VAIO")), AppConstants.UNIT_PRICE_SONY_VAIO);
			} else {
				softAssert.assertEquals(StringUtil.removeSpecialCharacters(getUnitPriceFromList("Sony VAIO")), AppConstants.PRICE_EXTAX_SONY_VAIO);
			}
			break;

		case "iPod Classic":
			softAssert.assertTrue(checkoutPage.getProductDetailsInMap().containsKey("iPod Classic"));
			softAssert.assertEquals(getModelFromList("iPod Classic"), AppConstants.PRODUCT_CODE_IPOD_CLASSIC);
			softAssert.assertEquals(getQuantityFromList("iPod Classic"), String.valueOf(quantity));
			if (deliveryCountry.equals(AppConstants.COUNTRY_WITH_TAXES)) {
				softAssert.assertEquals(getUnitPriceFromList("iPod Classic"), AppConstants.UNIT_PRICE_IPOD_CLASSIC);
			} else {
				softAssert.assertEquals(getUnitPriceFromList("iPod Classic"), AppConstants.PRICE_EXTAX_IPOD_CLASSIC);
			}
			break;

		case "HP LP3065":
			softAssert.assertTrue(checkoutPage.getProductDetailsInMap().containsKey("HP LP3065"));
			softAssert.assertEquals(getModelFromList("HP LP3065"), AppConstants.PRODUCT_CODE_HP_LP);
			softAssert.assertEquals(getQuantityFromList("HP LP3065"), String.valueOf(quantity));
			if (deliveryCountry.equals(AppConstants.COUNTRY_WITH_TAXES)) {
				softAssert.assertEquals(StringUtil.removeSpecialCharacters(getUnitPriceFromList("HP LP3065")), AppConstants.UNIT_PRICE_HP_LP);
			} else {
				softAssert.assertEquals(StringUtil.removeSpecialCharacters(getUnitPriceFromList("HP LP3065")), AppConstants.PRICE_EXTAX_HP_LP);
			}
			break;

		case "MacBook Pro":
			softAssert.assertTrue(checkoutPage.getProductDetailsInMap().containsKey("MacBook Pro"));
			softAssert.assertEquals(getModelFromList("MacBook Pro"), AppConstants.PRODUCT_CODE_MACBOOK_PRO);
			softAssert.assertEquals(getQuantityFromList("MacBook Pro"), String.valueOf(quantity));
			if (deliveryCountry.equals(AppConstants.COUNTRY_WITH_TAXES)) {
				softAssert.assertEquals(getUnitPriceFromList("MacBook Pro"), AppConstants.UNIT_PRICE_MACBOOK_PRO);
			} else {
				softAssert.assertEquals(getUnitPriceFromList("MacBook Pro"), AppConstants.PRICE_EXTAX_MACBOOK_PRO);
			}
			break;
		default:
			break;
		}

	}

	// **********************************Assertions for multiple products**********************************

	@Step("Verify the details of all the products for {0}")
	public void validateProductDetailsForAllProducts(String deliveryCountry) {

		Map<String, String[]> actualProductDetails = checkoutPage.getProductDetailsInMap();


		//verifying quantities...
		Map<String,Integer> actualProductQuantitiesMap = new HashMap<String,Integer>();
		Map<String,Integer> expectedProductQuantitiesMap = ExcelUtil.getProductQuantityMap();
		for (Entry<String, String[]> entry : actualProductDetails.entrySet()) {
			actualProductQuantitiesMap.put(entry.getKey(), Integer.parseInt(entry.getValue()[1]));
		}
		softAssert.assertEquals(actualProductQuantitiesMap, expectedProductQuantitiesMap, AppErrors.QUANTITIES_MISMATCH_ERROR);
		
		//verifying models...
		Map<String,String> actualProductModelMap = new HashMap<String,String>();
		String[] productList = ExcelUtil.getColumnData(AppConstants.PLACE_MULTIPLE_ITEMS_ORDER_SHEET_NAME, "Product Name");
		Map<String,String> expectedProductModelMap = new HashMap<String,String>();
		Map<String,Object[]> productDetailsMap = new HashMap<String,Object[]>();

		for(String product:productList) {
			productDetailsMap.put(product, AppConstants.getProductDetails(product));
		}
		
		for (Entry<String, String[]> entry : actualProductDetails.entrySet()) {		
			actualProductModelMap.put(entry.getKey(), entry.getValue()[0]);
		}
		for(Entry<String, Object[]> entry : productDetailsMap.entrySet()) {
			expectedProductModelMap.put(entry.getKey(), entry.getValue()[1].toString());
		}
		softAssert.assertEquals(actualProductModelMap, expectedProductModelMap,AppErrors.MODELS_MISMATCH_ERROR);
		
		//verifying unit prices...
		Map<String,Double> actualProductPriceMap = new HashMap<String,Double>();
		Map<String,Double> expectedProductPriceMap = new HashMap<String,Double>();
		for (Entry<String, String[]> entry : actualProductDetails.entrySet()) {
			actualProductPriceMap.put(entry.getKey(),StringUtil.removeSpecialCharacters(entry.getValue()[2]));
		}
		for(Entry<String, Object[]> entry : productDetailsMap.entrySet()) {
			if(!deliveryCountry.equals(AppConstants.COUNTRY_WITH_TAXES)) {
				expectedProductPriceMap.put(entry.getKey(), Double.parseDouble(entry.getValue()[2].toString()));
			}else {
				expectedProductPriceMap.put(entry.getKey(), Double.parseDouble(entry.getValue()[3].toString()));
			}
		}
		softAssert.assertEquals(actualProductPriceMap, expectedProductPriceMap, AppErrors.TOTAL_BEFORE_TAXES_ERROR);
		
	}

}
