package com.qa.opencart.calculationdecorator;

public class EcoTaxDecorator extends TaxDecorator{


    public EcoTaxDecorator(CalculateTotal total) {
        super(total);
//        System.out.println("Inside EcoTaxDecorator constructor...");
    }

    public double calculateTotal(String productName, int quantity) {
//        System.out.println("Inside EcoTaxDecorator calculateTotal...");
        return total.calculateTotal(productName,quantity) + calculateEcoTax(quantity);
    }

    private static double calculateEcoTax(int quantity){
        return TaxCalculation.calculateEcoTax(quantity);
    }
}
