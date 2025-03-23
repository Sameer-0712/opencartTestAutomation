package com.qa.opencart.calculationdecorator;

import com.qa.opencart.constants.AppConstants;
import com.qa.opencart.exceptions.FrameworkExceptions;

public class VATDecorator extends TaxDecorator{


    public VATDecorator(CalculateTotal total) {
        super(total);
    }

    @Override
    public double calculateTotal(String productName, int quantity) {
        return total.calculateTotal(productName,quantity) + calculateVAT(productName,quantity);
    }

    private static double calculateVAT(String productName, int quantity){

        double vat = 0;

        switch (productName){
            case AppConstants.PRODUCT_NAME_SONY_VAIO:
                vat = TaxCalculation.calculateVAT(AppConstants.PRICE_EXTAX_SONY_VAIO * quantity);
                break;
            case AppConstants.PRODUCT_NAME_IPOD_CLASSIC:
                vat = TaxCalculation.calculateVAT(AppConstants.PRICE_EXTAX_IPOD_CLASSIC * quantity);
                break;
            case AppConstants.PRODUCT_NAME_HP_LP:
                vat = TaxCalculation.calculateVAT(AppConstants.PRICE_EXTAX_HP_LP * quantity);
                break;
            case AppConstants.PRODUCT_NAME_MACBOOK_PRO:
                vat = TaxCalculation.calculateVAT(AppConstants.PRICE_EXTAX_MACBOOK_PRO * quantity);
                break;
            default:
                throw new FrameworkExceptions("Product Not Found");
        }

        return vat;

    }

}
