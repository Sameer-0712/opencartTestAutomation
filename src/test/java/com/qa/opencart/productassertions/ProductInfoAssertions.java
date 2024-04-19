package com.qa.opencart.productassertions;

import org.testng.asserts.SoftAssert;

import com.qa.opencart.constants.AppConstants;
import com.qa.opencart.pages.CheckoutPage;

public class ProductInfoAssertions {
	
	private SoftAssert softAssert;
	private CheckoutPage checkoutPage;

	public ProductInfoAssertions(SoftAssert softAssert, CheckoutPage checkoutPage) {
		this.softAssert = softAssert;
		this.checkoutPage = checkoutPage;
	}


	public void validateProductAssertions(String productName, int quantity) {

		switch (productName.trim()) {
		case "Sony VAIO":
			softAssert.assertEquals(checkoutPage.getOrderDetailsForUK().get("ProductName"),
					AppConstants.PRODUCT_NAME_SONY_VAIO);
			softAssert.assertEquals(checkoutPage.getOrderDetailsForUK().get("Model"),
					AppConstants.PRODUCT_CODE_SONY_VAIO);
			softAssert.assertEquals(checkoutPage.getOrderDetailsForUK().get("Quantity"), String.valueOf(quantity));
			softAssert.assertEquals(checkoutPage.getOrderDetailsForUK().get("UnitPrice"),
					AppConstants.UNIT_PRICE_SONY_VAIO);
			break;
		case "iPod Classic":
			softAssert.assertEquals(checkoutPage.getOrderDetailsForUK().get("ProductName"),
					AppConstants.PRODUCT_NAME_IPOD_CLASSIC);
			softAssert.assertEquals(checkoutPage.getOrderDetailsForUK().get("Model"),
					AppConstants.PRODUCT_CODE_IPOD_CLASSIC);
			softAssert.assertEquals(checkoutPage.getOrderDetailsForUK().get("Quantity"), String.valueOf(quantity));
			softAssert.assertEquals(checkoutPage.getOrderDetailsForUK().get("UnitPrice"),
					AppConstants.UNIT_PRICE_IPOD_CLASSIC);
			break;
		case "HP LP3065":
			softAssert.assertEquals(checkoutPage.getOrderDetailsForUK().get("ProductName"),
					AppConstants.PRODUCT_NAME_HP_LP);
			softAssert.assertEquals(checkoutPage.getOrderDetailsForUK().get("Model"), AppConstants.PRODUCT_CODE_HP_LP);
			softAssert.assertEquals(checkoutPage.getOrderDetailsForUK().get("Quantity"), String.valueOf(quantity));
			softAssert.assertEquals(checkoutPage.getOrderDetailsForUK().get("UnitPrice"),
					AppConstants.UNIT_PRICE_HP_LP);
			break;
		case "MacBook Pro":
			softAssert.assertEquals(checkoutPage.getOrderDetailsForUK().get("ProductName"),
					AppConstants.PRODUCT_NAME_MACBOOK_PRO);
			softAssert.assertEquals(checkoutPage.getOrderDetailsForUK().get("Model"),
					AppConstants.PRODUCT_CODE_MACBOOK_PRO);
			softAssert.assertEquals(checkoutPage.getOrderDetailsForUK().get("Quantity"), String.valueOf(quantity));
			softAssert.assertEquals(checkoutPage.getOrderDetailsForUK().get("UnitPrice"),
					AppConstants.UNIT_PRICE_MACBOOK_PRO);
			break;
		default:
			break;
		}

	}

}
