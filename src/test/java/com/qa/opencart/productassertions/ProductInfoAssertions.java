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

	public void validateProductAssertions(String deliveryCountry, String productName, int quantity) {

		switch (productName.trim()) {
		case "Sony VAIO":
			softAssert.assertEquals(checkoutPage.getOrderDetails(deliveryCountry).get("ProductName"),
					AppConstants.PRODUCT_NAME_SONY_VAIO);
			softAssert.assertEquals(checkoutPage.getOrderDetails(deliveryCountry).get("Model"),
					AppConstants.PRODUCT_CODE_SONY_VAIO);
			softAssert.assertEquals(checkoutPage.getOrderDetails(deliveryCountry).get("Quantity"),
					String.valueOf(quantity));
			if (deliveryCountry.equals("United Kingdom")) {
				softAssert.assertEquals(checkoutPage.getOrderDetails(deliveryCountry).get("UnitPrice"),
						AppConstants.UNIT_PRICE_SONY_VAIO);
			} else {
				softAssert.assertEquals(checkoutPage.getOrderDetails(deliveryCountry).get("UnitPrice"),
						AppConstants.PRICE_EXTAX_SONY_VAIO);
			}

			break;
		case "iPod Classic":
			softAssert.assertEquals(checkoutPage.getOrderDetails(deliveryCountry).get("ProductName"),
					AppConstants.PRODUCT_NAME_IPOD_CLASSIC);
			softAssert.assertEquals(checkoutPage.getOrderDetails(deliveryCountry).get("Model"),
					AppConstants.PRODUCT_CODE_IPOD_CLASSIC);
			softAssert.assertEquals(checkoutPage.getOrderDetails(deliveryCountry).get("Quantity"),
					String.valueOf(quantity));
			if (deliveryCountry.equals("United Kingdom")) {
				softAssert.assertEquals(checkoutPage.getOrderDetails(deliveryCountry).get("UnitPrice"),
						AppConstants.UNIT_PRICE_IPOD_CLASSIC);
			} else {
				softAssert.assertEquals(checkoutPage.getOrderDetails(deliveryCountry).get("UnitPrice"),
						AppConstants.PRICE_EXTAX_IPOD_CLASSIC);
			}
			break;
		case "HP LP3065":
			softAssert.assertEquals(checkoutPage.getOrderDetails(deliveryCountry).get("ProductName"),
					AppConstants.PRODUCT_NAME_HP_LP);
			softAssert.assertEquals(checkoutPage.getOrderDetails(deliveryCountry).get("Model"),
					AppConstants.PRODUCT_CODE_HP_LP);
			softAssert.assertEquals(checkoutPage.getOrderDetails(deliveryCountry).get("Quantity"),
					String.valueOf(quantity));
			if (deliveryCountry.equals("United Kingdom")) {
				softAssert.assertEquals(checkoutPage.getOrderDetails(deliveryCountry).get("UnitPrice"),
						AppConstants.UNIT_PRICE_HP_LP);
			} else {
				softAssert.assertEquals(checkoutPage.getOrderDetails(deliveryCountry).get("UnitPrice"),
						AppConstants.PRICE_EXTAX_HP_LP);
			}
			break;
		case "MacBook Pro":
			softAssert.assertEquals(checkoutPage.getOrderDetails(deliveryCountry).get("ProductName"),
					AppConstants.PRODUCT_NAME_MACBOOK_PRO);
			softAssert.assertEquals(checkoutPage.getOrderDetails(deliveryCountry).get("Model"),
					AppConstants.PRODUCT_CODE_MACBOOK_PRO);
			softAssert.assertEquals(checkoutPage.getOrderDetails(deliveryCountry).get("Quantity"),
					String.valueOf(quantity));
			if (deliveryCountry.equals("United Kingdom")) {
				softAssert.assertEquals(checkoutPage.getOrderDetails(deliveryCountry).get("UnitPrice"),
						AppConstants.UNIT_PRICE_MACBOOK_PRO);
			} else {
				softAssert.assertEquals(checkoutPage.getOrderDetails(deliveryCountry).get("UnitPrice"),
						AppConstants.PRICE_EXTAX_MACBOOK_PRO);
			}
			break;
		default:
			break;
		}

	}

}
