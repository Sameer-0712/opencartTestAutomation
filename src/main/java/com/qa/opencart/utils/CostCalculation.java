package com.qa.opencart.utils;

import com.qa.opencart.calculationdecorator.*;
import com.qa.opencart.constants.AppConstants;

import java.util.Iterator;
import java.util.Map;

public class CostCalculation {

	static CalculateTotal total = new CalculateTotalImpl();

	public static double calculateSubTotalForSingleProduct(String productName, int quantity){

		return total.calculateTotal(productName,quantity);

	}

	public static double calculateTotalForSingleProduct(String deliveryCountry, String productName, int quantity){

		double total = 0;

		if(deliveryCountry.equals(AppConstants.COUNTRY_WITH_TAXES)){
			total = new EcoTaxDecorator(new VATDecorator(CostCalculation.total)).calculateTotal(productName,quantity) + AppConstants.FLAT_SHIPPING_RATE;
		}else{
			total = calculateSubTotalForSingleProduct(productName,quantity) + AppConstants.FLAT_SHIPPING_RATE;
		}

		return total;

	}

	public static double calculateSubTotalForMultipleProducts(Map<String,Integer> prodQuantity){

		return calculateSubTotal(prodQuantity);
	}

	public static double calculateTotalForMultipleProducts(String deliveryCountry, Map<String,Integer> prodQuantity){

		double total = 0;
		if(deliveryCountry.equals(AppConstants.COUNTRY_WITH_TAXES)){
			total = calculateSubTotalForMultipleProducts(prodQuantity) +
					TaxCalculation.calculateEcoTax(calculateQuantity(prodQuantity)) +
					TaxCalculation.calculateVAT(calculateSubTotalForMultipleProducts(prodQuantity)) +
					AppConstants.FLAT_SHIPPING_RATE;
		}
		else{
			total = calculateSubTotalForMultipleProducts(prodQuantity) + AppConstants.FLAT_SHIPPING_RATE;
		}
		return total;

	}

	public static double calculateEcoTaxForMultipleProducts(Map<String,Integer> prodQuantity){

		return TaxCalculation.calculateEcoTax(calculateQuantity(prodQuantity));

	}

	public static double calculateVATForMultipleProducts(Map<String,Integer> prodQuantity){

		return TaxCalculation.calculateVAT(calculateSubTotal(prodQuantity));

	}

	public static double calculateUnitPrice(String productName, int quantity){

		return calculateSubTotalForSingleProduct(productName,quantity) + calculateSubTotalForSingleProduct(productName,quantity) * AppConstants.VAT + 2 * quantity;
	}

	//********************************Private Methods*******************************//

	private static double calculateSubTotal(Map<String,Integer> prodQuantity){
		double subTotal = 0;

		for (Map.Entry<String, Integer> entry : prodQuantity.entrySet()){
			subTotal = calculateSubTotalForSingleProduct(entry.getKey(), entry.getValue()) + subTotal;
		}

		return subTotal;
	}

	private static int calculateQuantity(Map<String,Integer> prodQuantity){
		int totalQuantity = 0;
		Iterator<Map.Entry<String, Integer>> it = prodQuantity.entrySet().iterator();
		while (it.hasNext()){
			totalQuantity = it.next().getValue() + totalQuantity;
		}
		return totalQuantity;
	}


}
