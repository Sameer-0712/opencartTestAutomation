package com.qa.opencart.calculationdecorator;

import com.qa.opencart.constants.AppConstants;

public class TaxCalculation {

    private TaxCalculation() {}

    public static double calculateEcoTax(int quantity){
        return quantity * 2.0 + 2.0;
    }

    public static double calculateVAT(double subTotal){
        return subTotal * AppConstants.VAT + 1.0;
    }

}
