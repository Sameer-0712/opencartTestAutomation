package com.qa.opencart.utils;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.qa.opencart.constants.AppConstants;

import io.qameta.allure.Step;

public class CostCalculation {

	@Step("Calculate the total price for delivery country {0}, product {1} with quantity {2}")
	public static String calculateTotalPrice(String deliveryCountry, String productPrice, int quantity) {
		double price = Double.parseDouble(calculateTotalPriceWithoutTaxes(productPrice, quantity));
		double ecoTax = 0.0;
		double vat = 0.0;
		if(deliveryCountry.equals(AppConstants.COUNTRY_WITH_TAXES)) {
			ecoTax = Double.parseDouble(calculateEcoTax(quantity));
			vat = Double.parseDouble(calculateVAT(productPrice, quantity));
		}
		double flatShippingCharge = AppConstants.FLAT_SHIPPING_RATE;
		double totalPrice = price + ecoTax + vat + flatShippingCharge;
		return String.valueOf(totalPrice);
	}

	@Step("Calculating total price before taxes")
	public static String calculateTotalPriceWithoutTaxes(String productPrice, int quantity) {
		double price = StringUtil.removeSpecialCharacters(productPrice);
		return String.valueOf(price * quantity);
	}
	
	@Step("Calculate the eco tax for quantity {0}")
	public static String calculateEcoTax(int quantity) {
			double totalEcoTax = AppConstants.ECO_TAX * quantity + 2.00;
			return String.valueOf(totalEcoTax);	
	}

	@Step("Calculate the VAT")
	public static String calculateVAT(String productPrice, int quantity) {
			double price = StringUtil.removeSpecialCharacters(productPrice);
			double totalVAT = AppConstants.VAT * price * quantity + 1.00;
			return String.valueOf(totalVAT);
	}
	
	//****************Methods for multiple products//****************
	
	@Step("Calculate the total price before taxes for product {0} with quantity {1}")
	public static double calculateTotalPriceWithoutTaxesBasedOnProductNameQuantity(String productName, int quantity) {
		String productPrice = AppConstants.getProductDetails(productName)[2];
		double price = StringUtil.removeSpecialCharacters(productPrice);
		return price * quantity;
	}
	
	public static String calculateSubTotal(Map<String,String> hm) {
		//Provide map of quantity and unit price
		int qty = 0;
		double unitPrice = 0.0;
		double totalPrice = 0.0;
		Set<Entry<String, String>> entrySet = hm.entrySet();
		for(Entry<String, String> keyVal:entrySet) {
			qty = Integer.valueOf(keyVal.getKey());
			unitPrice = StringUtil.removeSpecialCharacters(keyVal.getValue());
			totalPrice = totalPrice + unitPrice*qty;
		}
		return String.valueOf(totalPrice);
	}
	
	@Step("Calculate the subtotal from the product-quantity map {0}")
	public static String calculateSubTotalUsingProductNameQuantity(Map<String,String> hm) {
		//Provide map of product name and quantity
		int qty = 0;
		String productName = null;
		double totalPrice = 0.0;
		Set<Entry<String, String>> entrySet = hm.entrySet();
		for(Entry<String, String> keyVal:entrySet) {
			productName = keyVal.getKey();
			qty = Integer.parseInt(keyVal.getValue());
			totalPrice = calculateTotalPriceWithoutTaxesBasedOnProductNameQuantity(productName,qty) + totalPrice;
		}
		return String.valueOf(totalPrice);
	}
	
	private static double calculateVATUsingProductNameQuantity(String productName, int quantity) {
		String productPrice = AppConstants.getProductDetails(productName)[2];
		double price = StringUtil.removeSpecialCharacters(productPrice);
		double totalVAT = AppConstants.VAT * price * quantity;
		return totalVAT;
	}
	
	@Step("Calculate the total VAT from the product-quantity map {0}")
	public static String calculateTotalVAT(Map<String,String> hm) {
		//Provide map of product name and quantity
		int qty = 0;
		String productName = null;
		double totalVAT = 0.0;
		Set<Entry<String, String>> entrySet = hm.entrySet();
		for(Entry<String, String> keyVal:entrySet) {
			productName = keyVal.getKey();
			qty = Integer.parseInt(keyVal.getValue());
			totalVAT = calculateVATUsingProductNameQuantity(productName,qty) + totalVAT;
		}
		return String.valueOf(totalVAT + 1.0);
	}
	
	@Step("Calculate the total from the product-quantity map {0}")
	public static String calculateTotalForMultipleProducts(Map<String,String> hm, String deliveryCountry) {
		
		//Provide map of product name and quantity
		double subTotal = Double.parseDouble(calculateSubTotalUsingProductNameQuantity(hm));
		int qty = hm.values().stream().map(s -> Integer.parseInt(s)).reduce(0, Integer::sum);
		double flatShippingCharge = AppConstants.FLAT_SHIPPING_RATE;
		double totalEcoTax = 0.0;
		double totalVAT = 0.0;
		if(deliveryCountry.equals(AppConstants.COUNTRY_WITH_TAXES)) {
			totalEcoTax = Double.parseDouble(calculateEcoTax(qty));
			totalVAT = Double.parseDouble(calculateTotalVAT(hm));
		}
			
		double total = subTotal + flatShippingCharge + totalEcoTax + totalVAT;
		
		return String.valueOf(total);
		
	}

}
