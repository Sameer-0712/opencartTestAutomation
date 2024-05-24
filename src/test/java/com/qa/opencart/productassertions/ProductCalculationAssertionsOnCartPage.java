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
    private Map <String, String> getTableDetailsInMap(String deliveryCountry, String productName) {
        Map <String, String> map = null;
        if(deliveryCountry.equals(AppConstants.COUNTRY_WITH_TAXES)){
            map = cartPage.getCostBreakUpForUK();
        }else{
            map = cartPage.getCostBreakUp();
        }
        return map;
    }

    @Step("Verify the total before taxes for the delivery country {0}, product name {1} with quantity {2}")
    public void validateSubTotal(String deliveryCountry, String productName, int quantity) {
        String expectedTotalWithoutTaxes = null;
        String actualTotalWithoutTaxes = null;

        switch (productName.trim()) {
            case "Sony VAIO":
                expectedTotalWithoutTaxes = CostCalculation.calculateTotalPriceWithoutTaxes(AppConstants.PRICE_EXTAX_SONY_VAIO, quantity);
                actualTotalWithoutTaxes = String.valueOf(StringUtil.removeSpecialCharacters(getTableDetailsInMap(deliveryCountry,"Sony VAIO").get("Sub-Total")));
                softAssert.assertEquals(actualTotalWithoutTaxes, expectedTotalWithoutTaxes,
                        AppErrors.TOTAL_BEFORE_TAXES_ERROR);
                break;

            case "iPod Classic":
                expectedTotalWithoutTaxes = CostCalculation.calculateTotalPriceWithoutTaxes(AppConstants.PRICE_EXTAX_IPOD_CLASSIC, quantity);
                actualTotalWithoutTaxes = String.valueOf(StringUtil.removeSpecialCharacters(getTableDetailsInMap(deliveryCountry,"iPod Classic").get("Sub-Total")));
                softAssert.assertEquals(actualTotalWithoutTaxes, expectedTotalWithoutTaxes,
                        AppErrors.TOTAL_BEFORE_TAXES_ERROR);
                break;

            case "HP LP3065":
                expectedTotalWithoutTaxes = CostCalculation.calculateTotalPriceWithoutTaxes(AppConstants.PRICE_EXTAX_HP_LP, quantity);
                actualTotalWithoutTaxes = String.valueOf(StringUtil.removeSpecialCharacters(getTableDetailsInMap(deliveryCountry,"HP LP3065").get("Sub-Total")));
                softAssert.assertEquals(actualTotalWithoutTaxes, expectedTotalWithoutTaxes,
                        AppErrors.TOTAL_BEFORE_TAXES_ERROR);
                break;

            case "MacBook Pro":
                expectedTotalWithoutTaxes = CostCalculation.calculateTotalPriceWithoutTaxes(AppConstants.PRICE_EXTAX_MACBOOK_PRO, quantity);
                actualTotalWithoutTaxes = String.valueOf(StringUtil.removeSpecialCharacters(getTableDetailsInMap(deliveryCountry,"MacBook Pro").get("Sub-Total")));
                softAssert.assertEquals(actualTotalWithoutTaxes, expectedTotalWithoutTaxes,
                        AppErrors.TOTAL_BEFORE_TAXES_ERROR);
                break;

            default:
                break;
        }
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

    @Step("Verify the VAT for the delivery country {0}, product {1} with quantity {2}")
    public void validateVAT(String deliveryCountry, String productName, int quantity) {
        String expectedVAT = null;
        String actualVAT = null;

        switch (productName.trim()) {
            case "Sony VAIO":
                if (deliveryCountry.equals(AppConstants.COUNTRY_WITH_TAXES)) {
                    expectedVAT = CostCalculation.calculateVAT(AppConstants.PRICE_EXTAX_SONY_VAIO, quantity);
                    actualVAT = String.valueOf(
                            StringUtil.removeSpecialCharacters(cartPage.getCostBreakUpForUK().get("VAT")));
                    softAssert.assertEquals(actualVAT, expectedVAT, AppErrors.VAT_ERROR);
                } else {
                    softAssert.assertNull(actualVAT);
                }

                break;
            case "iPod Classic":
                if (deliveryCountry.equals(AppConstants.COUNTRY_WITH_TAXES)) {
                    expectedVAT = CostCalculation.calculateVAT(AppConstants.PRICE_EXTAX_IPOD_CLASSIC, quantity);
                    actualVAT = String.valueOf(
                            StringUtil.removeSpecialCharacters(cartPage.getCostBreakUpForUK().get("VAT")));
                    softAssert.assertEquals(actualVAT, expectedVAT, AppErrors.VAT_ERROR);
                } else {
                    softAssert.assertNull(actualVAT);
                }

                break;
            case "HP LP3065":
                if (deliveryCountry.equals(AppConstants.COUNTRY_WITH_TAXES)) {
                    expectedVAT = CostCalculation.calculateVAT(AppConstants.PRICE_EXTAX_HP_LP, quantity);
                    actualVAT = String.valueOf(
                            StringUtil.removeSpecialCharacters(cartPage.getCostBreakUpForUK().get("VAT")));
                    softAssert.assertEquals(actualVAT, expectedVAT, AppErrors.VAT_ERROR);
                } else {
                    softAssert.assertNull(actualVAT);
                }
                break;
            case "MacBook Pro":
                if (deliveryCountry.equals(AppConstants.COUNTRY_WITH_TAXES)) {
                    expectedVAT = CostCalculation.calculateVAT(AppConstants.PRICE_EXTAX_MACBOOK_PRO, quantity);
                    actualVAT = String.valueOf(
                            StringUtil.removeSpecialCharacters(cartPage.getCostBreakUpForUK().get("VAT")));
                    softAssert.assertEquals(actualVAT, expectedVAT, AppErrors.VAT_ERROR);
                } else {
                    softAssert.assertNull(actualVAT);
                }
                break;
            default:
                break;
        }
    }

    @Step("Verify the total for delivery country {0}, product {1} with quantity {2}")
    public void validateTotal(String deliveryCountry, String productName, int quantity) {
        String expectedTotal = null;
        String actualTotal = null;

        switch (productName.trim()) {
            case "Sony VAIO":
                expectedTotal = CostCalculation.calculateTotalPrice(deliveryCountry, AppConstants.PRICE_EXTAX_SONY_VAIO,
                        quantity);
                if(deliveryCountry.equals(AppConstants.COUNTRY_WITH_TAXES)){
                    actualTotal = String.valueOf(
                            StringUtil.removeSpecialCharacters(cartPage.getCostBreakUpForUK().get("Total")));
                }else{
                    actualTotal = String.valueOf(
                            StringUtil.removeSpecialCharacters(cartPage.getCostBreakUp().get("Total")));
                }
                softAssert.assertEquals(actualTotal, expectedTotal, AppErrors.TOTAL_ERROR);
                break;
            case "iPod Classic":
                expectedTotal = CostCalculation.calculateTotalPrice(deliveryCountry, AppConstants.PRICE_EXTAX_IPOD_CLASSIC,
                        quantity);
                if(deliveryCountry.equals(AppConstants.COUNTRY_WITH_TAXES)){
                    actualTotal = String.valueOf(
                            StringUtil.removeSpecialCharacters(cartPage.getCostBreakUpForUK().get("Total")));
                }else{
                    actualTotal = String.valueOf(
                            StringUtil.removeSpecialCharacters(cartPage.getCostBreakUp().get("Total")));
                }
                softAssert.assertEquals(actualTotal, expectedTotal, AppErrors.TOTAL_ERROR);
                break;
            case "HP LP3065":
                expectedTotal = CostCalculation.calculateTotalPrice(deliveryCountry, AppConstants.PRICE_EXTAX_HP_LP,
                        quantity);
                if(deliveryCountry.equals(AppConstants.COUNTRY_WITH_TAXES)){
                    actualTotal = String.valueOf(
                            StringUtil.removeSpecialCharacters(cartPage.getCostBreakUpForUK().get("Total")));
                }else{
                    actualTotal = String.valueOf(
                            StringUtil.removeSpecialCharacters(cartPage.getCostBreakUp().get("Total")));
                }
                softAssert.assertEquals(actualTotal, expectedTotal, AppErrors.TOTAL_ERROR);
                break;
            case "MacBook Pro":
                expectedTotal = CostCalculation.calculateTotalPrice(deliveryCountry, AppConstants.PRICE_EXTAX_MACBOOK_PRO,
                        quantity);
                if(deliveryCountry.equals(AppConstants.COUNTRY_WITH_TAXES)){
                    actualTotal = String.valueOf(
                            StringUtil.removeSpecialCharacters(cartPage.getCostBreakUpForUK().get("Total")));
                }else{
                    actualTotal = String.valueOf(
                            StringUtil.removeSpecialCharacters(cartPage.getCostBreakUp().get("Total")));
                }
                softAssert.assertEquals(actualTotal, expectedTotal, AppErrors.TOTAL_ERROR);
                break;
            default:
                break;
        }
    }

}
