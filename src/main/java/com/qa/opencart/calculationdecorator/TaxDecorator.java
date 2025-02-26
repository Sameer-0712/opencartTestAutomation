package com.qa.opencart.calculationdecorator;

public class TaxDecorator implements CalculateTotal{


    protected CalculateTotal total;

    public TaxDecorator(CalculateTotal total) {
        this.total = total;
    }

    @Override
    public double calculateTotal(String productName, int quantity) {
//        System.out.println("Inside TaxDecorator calculateTotal...");
        return total.calculateTotal(productName,quantity);
    }


}
