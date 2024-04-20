package com.qa.opencart.utils;

import com.qa.opencart.constants.AppConstants;

public class CostCalculation {

	public static String calculateTotalPrice(String deliveryCountry, String productPrice, int quantity) {
		double price = Double.parseDouble(calculateTotalPriceWithoutTaxes(productPrice, quantity));
		double ecoTax = 0.0;
		double vat = 0.0;
		if(deliveryCountry.equals("United Kingdom")) {
			ecoTax = Double.parseDouble(calculateEcoTax(quantity));
			vat = Double.parseDouble(calculateVAT(productPrice, quantity));
		}
		double flatShippingCharge = AppConstants.FLAT_SHIPPING_RATE;
		double totalPrice = price + ecoTax + vat + flatShippingCharge;
		return String.valueOf(totalPrice);
	}

	public static String calculateTotalPriceWithoutTaxes(String productPrice, int quantity) {
		double price = StringUtil.removeSpecialCharacters(productPrice);
		return String.valueOf(price * quantity);
	}

	public static String calculateEcoTax(int quantity) {
			double totalEcoTax = AppConstants.ECO_TAX * quantity + 2.00;
			return String.valueOf(totalEcoTax);	
	}

	public static String calculateVAT(String productPrice, int quantity) {
			double price = StringUtil.removeSpecialCharacters(productPrice);
			double totalVAT = AppConstants.VAT * price * quantity + 1.00;
			return String.valueOf(totalVAT);
	}

}
