package com.qa.opencart.calculationdecorator;

import com.qa.opencart.constants.AppConstants;
import com.qa.opencart.exceptions.FrameworkExceptions;

public class CalculateTotalImpl implements CalculateTotal{


    @Override
    public double calculateTotal(String productName, int quantity) {

            return calculateSubTotal(productName, quantity);

    }

    private double calculateSubTotal(String productName, int quantity){

        double subTotal = 0;

        switch (productName){
            case AppConstants.PRODUCT_NAME_SONY_VAIO:
                subTotal = AppConstants.PRICE_EXTAX_SONY_VAIO * quantity;
                break;
            case AppConstants.PRODUCT_NAME_IPOD_CLASSIC:
                subTotal = AppConstants.PRICE_EXTAX_IPOD_CLASSIC * quantity;
                break;
            case AppConstants.PRODUCT_NAME_HP_LP:
                subTotal = AppConstants.PRICE_EXTAX_HP_LP * quantity;
                break;
            case AppConstants.PRODUCT_NAME_MACBOOK_PRO:
                subTotal = AppConstants.PRICE_EXTAX_MACBOOK_PRO * quantity;
                break;
            default:
                throw new FrameworkExceptions("Product Not Found");
        }

        return subTotal;
    }

}
