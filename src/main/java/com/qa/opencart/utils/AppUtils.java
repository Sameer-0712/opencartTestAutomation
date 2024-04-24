package com.qa.opencart.utils;

import com.qa.opencart.constants.AppConstants;
import com.qa.opencart.logger.Log;
import com.qa.opencart.pages.AccountPage;
import com.qa.opencart.pages.CartPage;
import com.qa.opencart.pages.CheckoutPage;
import com.qa.opencart.pages.ProductPage;
import com.qa.opencart.pages.SearchResultsPage;

public class AppUtils {
	
	private AccountPage accPage;
	private SearchResultsPage searchResultsPage;
	private ProductPage productPage;
	private CartPage cartPage;
	private CheckoutPage checkoutPage;
	

	public AppUtils(AccountPage accPage, SearchResultsPage searchResultsPage, ProductPage productPage, CartPage cartPage, CheckoutPage checkoutPage) {
		this.accPage = accPage;
		this.searchResultsPage = searchResultsPage;
		this.productPage = productPage;
		this.cartPage = cartPage;
		this.checkoutPage = checkoutPage;
	}
	
	public CheckoutPage navigateToCheckOutPageAndFillDetails(String billingCountry, String deliveryCountry) {
		cartPage = productPage.navigateToCart();
		checkoutPage = cartPage.doCheckOut();
		Log.info("Filling the details on checkout page...");
		checkoutPage.selectBillingAndDeliveryDetails(billingCountry, deliveryCountry);
		return checkoutPage;
	}

	public void addProductToCart(String searchKey, String productName, String quantity) {

		int qty = Integer.parseInt(quantity);
		searchResultsPage = accPage.doSearch(searchKey);
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
