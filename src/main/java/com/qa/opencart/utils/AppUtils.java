package com.qa.opencart.utils;

import com.qa.opencart.constants.AppConstants;
import com.qa.opencart.logger.Log;
import com.qa.opencart.pages.*;

import io.qameta.allure.Step;

public class AppUtils {
	
	private Page page;
	private SearchResultsPage searchResultsPage;
	private ProductPage productPage;
	private CartPage cartPage;
	private CheckoutPage checkoutPage;
	

	public AppUtils(Page page, SearchResultsPage searchResultsPage, ProductPage productPage, CartPage cartPage, CheckoutPage checkoutPage) {
		this.page = page;
		this.searchResultsPage = searchResultsPage;
		this.productPage = productPage;
		this.cartPage = cartPage;
		this.checkoutPage = checkoutPage;
	}
	
	@Step("Navigate to the checkout page and fill the details")
	public CheckoutPage navigateToCheckOutPageAndFillDetails(String billingCountry, String deliveryCountry) {
		cartPage = productPage.navigateToCart();
		checkoutPage = cartPage.doCheckOut();
		Log.info("Filling the details on checkout page...");
		checkoutPage.selectBillingAndDeliveryDetails(billingCountry, deliveryCountry);
		return checkoutPage;
	}

	@Step("Navigate to the checkout page and fill the details")
	public CheckoutPage navigateToCheckOutPageAndSelectGuestCheckout() {
		cartPage = productPage.navigateToCart();
		checkoutPage = cartPage.doCheckOut();
		Log.info("Filling the details on checkout page...");
		checkoutPage.selectGuestCheckoutRadioButtonAndContinue();
		return checkoutPage;
	}

	@Step("Search product with {0} and add product {1} with quantity {2}")
	public void addProductToCart(String searchKey, String productName, String quantity) {

		int qty = Integer.parseInt(quantity);
		searchResultsPage = page.doSearch(searchKey);
		productPage = searchResultsPage.navigateToProductPage(productName);
		Log.info("Adding product: "+productName);
		Log.info("Quantity: "+quantity);
		productPage.addToCart(qty);
	}

	public void addProductsToCart() {
		Object[][] productsData = ExcelUtil.getTestData(AppConstants.PLACE_MULTIPLE_ITEMS_ORDER_SHEET_NAME);

		int countOfProducts = productsData.length;
		String searchKey = null;
		String productName = null;
		String quantity = null;

		for (int i = 0; i < countOfProducts; i++) {
			searchKey = (String) productsData[i][0];
			productName = (String) productsData[i][1];
			quantity = (String) productsData[i][2];
			addProductToCart(searchKey, productName, quantity);
		}

	}

}
