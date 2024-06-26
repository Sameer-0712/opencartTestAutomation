package com.qa.opencart.productassertions;

import com.qa.opencart.constants.AppConstants;
import com.qa.opencart.errors.AppErrors;
import com.qa.opencart.pages.CartPage;
import com.qa.opencart.utils.CostCalculation;
import com.qa.opencart.utils.StringUtil;
import io.qameta.allure.Step;
import org.testng.asserts.SoftAssert;

import java.util.Map;

public class ProductCalculationAssertionsOnCartPage {

    private SoftAssert softAssert;
    private CartPage cartPage;

    public ProductCalculationAssertionsOnCartPage(SoftAssert softAssert, CartPage cartPage) {
        this.softAssert = softAssert;
        this.cartPage = cartPage;
    }

    @Step("Fetching the actual sub total before taxes")
    private Map <String, String> getTableDetailsInMap(String deliveryCountry) {
        Map <String, String> map = null;
        if(deliveryCountry.equals(AppConstants.COUNTRY_WITH_TAXES)){
            map = cartPage.getCostBreakUpForUK();
        }else{
            map = cartPage.getCostBreakUp();
        }
        return map;
    }


    @Step("Verify the shipping rate for the delivery country {0}")
    public void validateFlatShippingRate(String deliveryCountry) {
        softAssert.assertEquals(cartPage.getFlatShippingRate(deliveryCountry),
                    AppConstants.getFlatShippingRateInString(), AppErrors.FLAT_SHIPPING_RATE_ERROR);
    }

    @Step("Verify the eco tax for the quantity {1}")
    public void validateEcoTax(String deliveryCountry, int quantity) {
        String actualEcoTax = null;
        if(deliveryCountry.equals(AppConstants.COUNTRY_WITH_TAXES)) {
            String expectedEcoTax = CostCalculation.calculateEcoTax(quantity);
            actualEcoTax = String
                    .valueOf(StringUtil.removeSpecialCharacters(cartPage.getCostBreakUpForUK().get("Eco Tax")));
            softAssert.assertEquals(actualEcoTax, expectedEcoTax, AppErrors.ECO_TAX_ERROR);
        }else {
            softAssert.assertNull(actualEcoTax);
        }
    }

    //Assertions for multiple products

    @Step("Verify the subtotal value for {0} delivery")
    public void validateSubTotal(String deliveryCountry, Map<String,String> productQuantityMap){
        String expectedTotalWithoutTaxes = null;
        String actualTotalWithoutTaxes = null;
        expectedTotalWithoutTaxes = CostCalculation.calculateSubTotalUsingProductNameQuantity(productQuantityMap);
        actualTotalWithoutTaxes = String.valueOf(StringUtil.removeSpecialCharacters(getTableDetailsInMap(deliveryCountry).get("Sub-Total")));
        softAssert.assertEquals(actualTotalWithoutTaxes, expectedTotalWithoutTaxes,
                AppErrors.TOTAL_BEFORE_TAXES_ERROR);
    }

    @Step("Verify the total VAT for {0} delivery")
    public void validateVAT(String deliveryCountry, Map<String,String> productQuantityMap){
        String expectedVAT = null;
        String actualVAT = null;
        if (deliveryCountry.equals(AppConstants.COUNTRY_WITH_TAXES)) {
            expectedVAT = CostCalculation.calculateTotalVAT(productQuantityMap);
            actualVAT = String.valueOf(
                    StringUtil.removeSpecialCharacters(cartPage.getCostBreakUpForUK().get("VAT")));
            softAssert.assertEquals(actualVAT, expectedVAT, AppErrors.VAT_ERROR);
        } else {
            softAssert.assertNull(actualVAT);
        }
    }

    @Step("Verify the total for {0} delivery")
    public void validateTotal(String deliveryCountry, Map<String,String> productQuantityMap){
        String expectedTotal = null;
        String actualTotal = null;
        expectedTotal = CostCalculation.calculateTotalForMultipleProducts(productQuantityMap,deliveryCountry);
        if(deliveryCountry.equals(AppConstants.COUNTRY_WITH_TAXES)){
            actualTotal = String.valueOf(
                    StringUtil.removeSpecialCharacters(cartPage.getCostBreakUpForUK().get("Total")));
        }else{
            actualTotal = String.valueOf(
                    StringUtil.removeSpecialCharacters(cartPage.getCostBreakUp().get("Total")));
        }
        softAssert.assertEquals(actualTotal, expectedTotal, AppErrors.TOTAL_ERROR);
    }

}
