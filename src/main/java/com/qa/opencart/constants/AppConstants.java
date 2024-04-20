package com.qa.opencart.constants;

public class AppConstants {
	
	public static final String LOGIN_PAGE_TITLE = "Account Login";

	public static final String ACCOUNTS_PAGE_TITLE = "My Account";

	public static final String LOGIN_PAGE_URL_FRACTION = "route=account/login";
	public static final String ACC_PAGE_URL_FRACTION = "route=account/account";
	
	public static final double ECO_TAX = 2.00;
	public static final double VAT = 0.2;
	public static final double FLAT_SHIPPING_RATE = 5.00;
	public static final String FLAT_SHIPPING_RATE_UK_DELIVERY = "$8.00";
	public static final String FLAT_SHIPPING_RATE_NON_UK_DELIVERY = "$5.00";
	
	//*****************************Test Data*****************************
	
	public static final String PLACE_ORDER_SHEET_NAME = "placeOrder";
	
	//*****************************Application Success Messages*****************************

	public static final String ORDER_PLACED_SUCCESS_MESSAGE = "Your order has been placed!";

	//*****************************Product Info*****************************
	
	//Sony VAIO
	
	public static final String PRODUCT_NAME_SONY_VAIO = "Sony VAIO";
	public static final String BRAND_NAME_SONY_VAIO = "Sony";
	public static final String PRODUCT_CODE_SONY_VAIO = "Product 19";
	public static final String PRICE_EXTAX_SONY_VAIO = "$1,000.00";
	public static final String UNIT_PRICE_SONY_VAIO = "$1,202.00";
	
	//iPod Classic
	
	public static final String PRODUCT_NAME_IPOD_CLASSIC = "iPod Classic";
	public static final String BRAND_NAME_IPOD_CLASSIC = "Apple";
	public static final String PRODUCT_CODE_IPOD_CLASSIC = "product 20";
	public static final String PRICE_EXTAX_IPOD_CLASSIC = "$100.00";
	public static final String UNIT_PRICE_IPOD_CLASSIC = "$122.00";
	
	//HP LP3065
	
	public static final String PRODUCT_NAME_HP_LP = "HP LP3065";
	public static final String BRAND_NAME_HP_LP = "Hewlett-Packard";
	public static final String PRODUCT_CODE_HP_LP = "Product 21";
	public static final String PRICE_EXTAX_HP_LP = "$100.00";
	public static final String UNIT_PRICE_HP_LP = "$122.00";
	
	//MacBook Pro
	
	public static final String PRODUCT_NAME_MACBOOK_PRO = "MacBook Pro";
	public static final String BRAND_NAME_MACBOOK_PRO = "Apple";
	public static final String PRODUCT_CODE_MACBOOK_PRO = "Product 18";
	public static final String PRICE_EXTAX_MACBOOK_PRO = "$2,000.00";
	public static final String UNIT_PRICE_MACBOOK_PRO = "$2,000.00";//Exact Value can be known from DB or from Product Owner
	
	public static String getFlatShippingRateInString() {
		return "$5.00";
	}

}
