package com.qa.opencart.calculationdecorator;

public class TaxDecorator implements CalculateTotal{


    protected CalculateTotal total;

    public TaxDecorator(CalculateTotal total) {
        this.total = total;
    }

    @Override
    public double calculateTotal(String productName, int quantity) {
        return total.calculateTotal(productName,quantity);
    }


}
