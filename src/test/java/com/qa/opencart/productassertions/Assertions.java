package com.qa.opencart.productassertions;

import com.qa.opencart.constants.AppConstants;
import com.qa.opencart.pages.CartPage;
import com.qa.opencart.pages.CheckoutPage;
import org.testng.asserts.SoftAssert;

import java.util.Map;

public class Assertions {

    private final SoftAssert softAssert;
    private CheckoutPage checkoutPage;
    private CartPage cartPage;
    private final String deliveryCountry;
    private String productName;
    private int qty;

    public Assertions(SoftAssert softAssert, CheckoutPage checkoutPage, String deliveryCountry, String productName, int qty) {
        this.softAssert = softAssert;
        this.checkoutPage = checkoutPage;
        this.deliveryCountry = deliveryCountry;
        this.productName = productName;
        this.qty = qty;
    }


    public Assertions(SoftAssert softAssert, CheckoutPage checkoutPage, String deliveryCountry) {
        this.softAssert = softAssert;
        this.checkoutPage = checkoutPage;
        this.deliveryCountry = deliveryCountry;
    }

    public Assertions(SoftAssert softAssert, CartPage cartPage, String deliveryCountry, int qty) {
        this.softAssert = softAssert;
        this.cartPage = cartPage;
        this.deliveryCountry = deliveryCountry;
        this.qty = qty;
    }

    public void validateProductCalculationsOnCheckoutPage(boolean isSingleItemOrder){

        ProductInfoAssertions productInfoAssertions;
        ProductCalculationAssertions productCalculationAssertions;
        StandardRateAssertions stdRateAssertion;
        if(isSingleItemOrder){
            productInfoAssertions = new ProductInfoAssertions(softAssert, checkoutPage);
            productInfoAssertions.validateProductDetailsForAllProducts(deliveryCountry, productName, qty);
            productCalculationAssertions = new ProductCalculationAssertions(softAssert, checkoutPage);
            productCalculationAssertions.validateTotalInProductDetailsTable(deliveryCountry, productName, qty);
            productCalculationAssertions.validateSubTotalInCostBreakUpTable(deliveryCountry, productName, qty);
            stdRateAssertion = new StandardRateAssertions(softAssert,checkoutPage);
            stdRateAssertion.validateFlatShippingRateInCostBreakUpTable(deliveryCountry);
            stdRateAssertion.validateEcoTaxInCostBreakUpTable(deliveryCountry, qty);
            productCalculationAssertions.validateVATInCostBreakUpTable(deliveryCountry, productName, qty);
            productCalculationAssertions.validateTotalInCostBreakUpTable(deliveryCountry, productName, qty);
        }else{
            productInfoAssertions = new ProductInfoAssertions(softAssert, checkoutPage);
            productInfoAssertions.validateProductDetailsForAllProducts(deliveryCountry);
            productCalculationAssertions = new ProductCalculationAssertions(softAssert, checkoutPage);
            productCalculationAssertions.validateTotalForEachProductInProductDetailsTable(deliveryCountry);
            productCalculationAssertions.validateSubTotalInCostBreakUpTable(deliveryCountry);
            stdRateAssertion = new StandardRateAssertions(softAssert, checkoutPage);
            stdRateAssertion.validateFlatShippingRateInCostBreakUpTable(deliveryCountry);
            stdRateAssertion.validateTotalEcoTaxInCostBreakUpTable(deliveryCountry);
            productCalculationAssertions.validateTotalVATInCostBreakUpTable(deliveryCountry);
            productCalculationAssertions.validateTotalForMultipleProducts(deliveryCountry);
        }

    }

    public void validateProductCalculationsOnCartPage(String successMsg,  Map<String, Integer> productNamesQuantity){
        softAssert.assertEquals(successMsg, AppConstants.SHIPPING_RATE_APPLIED_SUCCESS);
        ProductCalculationAssertionsOnCartPage productCalculationAssertionsOnCartPage = new ProductCalculationAssertionsOnCartPage(softAssert,cartPage);
        productCalculationAssertionsOnCartPage.validateSubTotal(deliveryCountry,productNamesQuantity);
        productCalculationAssertionsOnCartPage.validateFlatShippingRate(deliveryCountry);
        productCalculationAssertionsOnCartPage.validateEcoTax(deliveryCountry,qty);
        productCalculationAssertionsOnCartPage.validateVAT(deliveryCountry,productNamesQuantity);
        productCalculationAssertionsOnCartPage.validateTotal(deliveryCountry,productNamesQuantity);
        softAssert.assertAll();
    }

}
