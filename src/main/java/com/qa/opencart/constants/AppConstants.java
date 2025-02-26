package com.qa.opencart.constants;

import java.util.HashMap;
import java.util.Map;

public class AppConstants {

	public static final String LOGIN_PAGE_TITLE = "Account Login";

	public static final String ACCOUNTS_PAGE_TITLE = "My Account";

	public static final String LOGIN_PAGE_URL_FRACTION = "route=account/login";
	public static final String ACC_PAGE_URL_FRACTION = "route=account/account";

	public static final double ECO_TAX = 2.00;
	public static final double VAT = 0.2;
	public static final double FLAT_SHIPPING_RATE = 5.00;
	public static final double FLAT_SHIPPING_RATE_UK_DELIVERY = 8.00;
	public static final double FLAT_SHIPPING_RATE_NON_UK_DELIVERY = 5.00;

	// *****************************Test Data*****************************

	public static final String PLACE_ORDER_SHEET_NAME = "placeOrder";
	public static final String PLACE_MULTIPLE_ITEMS_ORDER_SHEET_NAME = "placeMultipleItems";
	public static final String PLACE_ORDER_GUEST_SHEET_NAME = "placeOrderGuest";
	public static final String PRODUCTS_TEST_DATA = "Products";

	// *****************************Application Success Messages*****************************

	public static final String ORDER_PLACED_SUCCESS_MESSAGE = "Your order has been placed!";
	public static final String SHIPPING_RATE_APPLIED_SUCCESS = "Success: Your shipping estimate has been applied!";
	public static final String CART_PAGE_MODIFY_SUCCESS = "Success: You have modified your shopping cart!";
	
	// *****************************Special Country*****************************

		public static final String COUNTRY_WITH_TAXES = "United Kingdom";

	// *****************************Product Info*****************************

	// Sony VAIO

	public static final String PRODUCT_NAME_SONY_VAIO = "Sony VAIO";
	public static final String BRAND_NAME_SONY_VAIO = "Sony";
	public static final String PRODUCT_CODE_SONY_VAIO = "Product 19";
	public static final double PRICE_EXTAX_SONY_VAIO = 1000.00;
	public static final double UNIT_PRICE_SONY_VAIO = 1202.00;

	// iPod Classic

	public static final String PRODUCT_NAME_IPOD_CLASSIC = "iPod Classic";
	public static final String BRAND_NAME_IPOD_CLASSIC = "Apple";
	public static final String PRODUCT_CODE_IPOD_CLASSIC = "product 20";
	public static final double PRICE_EXTAX_IPOD_CLASSIC = 100.00;
	public static final double UNIT_PRICE_IPOD_CLASSIC = 122.00;

	// HP LP3065

	public static final String PRODUCT_NAME_HP_LP = "HP LP3065";
	public static final String BRAND_NAME_HP_LP = "Hewlett-Packard";
	public static final String PRODUCT_CODE_HP_LP = "Product 21";
	public static final double PRICE_EXTAX_HP_LP = 100.00;
	public static final double UNIT_PRICE_HP_LP = 122.00;

	// MacBook Pro

	public static final String PRODUCT_NAME_MACBOOK_PRO = "MacBook Pro";
	public static final String BRAND_NAME_MACBOOK_PRO = "Apple";
	public static final String PRODUCT_CODE_MACBOOK_PRO = "Product 18";
	public static final double PRICE_EXTAX_MACBOOK_PRO = 2000.00;
	public static final double UNIT_PRICE_MACBOOK_PRO = 2000.00;// Exact Value can be known from DB or from Product
																	// Owner

	private static Map<String, Object[]> namePriceMap = new HashMap<String, Object[]>();

	private static void createProductDetailsMap() {

		namePriceMap.put(PRODUCT_NAME_SONY_VAIO, new Object[] { BRAND_NAME_SONY_VAIO, PRODUCT_CODE_SONY_VAIO,
				PRICE_EXTAX_SONY_VAIO, UNIT_PRICE_SONY_VAIO});
		namePriceMap.put(PRODUCT_NAME_IPOD_CLASSIC, new Object[] { BRAND_NAME_IPOD_CLASSIC, PRODUCT_CODE_IPOD_CLASSIC,
				PRICE_EXTAX_IPOD_CLASSIC, UNIT_PRICE_IPOD_CLASSIC});
		namePriceMap.put(PRODUCT_NAME_HP_LP,
				new Object[] { BRAND_NAME_HP_LP, PRODUCT_CODE_HP_LP, PRICE_EXTAX_HP_LP, UNIT_PRICE_HP_LP});
		namePriceMap.put(PRODUCT_NAME_MACBOOK_PRO, new Object[] { BRAND_NAME_MACBOOK_PRO, PRODUCT_CODE_MACBOOK_PRO,
				PRICE_EXTAX_MACBOOK_PRO, UNIT_PRICE_MACBOOK_PRO});
		
	}
	
	private static Map<String, Object[]> getProductDetailsMap() {
		createProductDetailsMap();	
		return namePriceMap;
	}
	
	public static Object[] getProductDetails(String product) {
		return getProductDetailsMap().get(product);
	}

	public static String getFlatShippingRateInString() {
		return "$5.00";
	}

	public static double getProductPrice(String productName){
		return Double.parseDouble(getProductDetailsMap().get(productName)[2].toString());
	}

	public static final String NO_PRODUCTS_SEARCH_RESULTS = "There is no product that matches the search criteria.";

}
