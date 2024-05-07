package com.qa.opencart.tests;

import com.qa.opencart.base.BaseTest;
import com.qa.opencart.constants.AppConstants;
import com.qa.opencart.errors.AppErrors;
import com.qa.opencart.productassertions.ProductCalculationAssertions;
import com.qa.opencart.productassertions.ProductInfoAssertions;
import com.qa.opencart.utils.AppUtils;
import com.qa.opencart.utils.ExcelUtil;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class GuestPlaceOrderTest extends BaseTest {

    @Test(dataProvider = "getPlaceOrderData")
    public void validatePlaceSingleItemOrderWithGuest(String searchKey, String productName,
                                                      String quantity, String isBillingAndDeliveryAddressSame){
        AppUtils appUtil = new AppUtils(page, searchResultsPage, productPage, cartPage, checkoutPage);
        int qty = Integer.parseInt(quantity);

        appUtil.addProductToCart(searchKey, productName, quantity);
        checkoutPage = appUtil.navigateToCheckOutPageAndSelectGuestCheckout();

        checkoutPage.fillBillingAndDeliveryDetails(isBillingAndDeliveryAddressSame);

        assertShippingRateRadioButton(checkoutPage.getDeliveryCountry(isBillingAndDeliveryAddressSame));

        productInfoAssertions = new ProductInfoAssertions(softAssert, checkoutPage);
        productInfoAssertions.validateProductAssertions(checkoutPage.getDeliveryCountry(isBillingAndDeliveryAddressSame), productName, qty);
        productCalculationAssertions = new ProductCalculationAssertions(softAssert, checkoutPage);
        productCalculationAssertions.validateTotalWithoutTaxes(checkoutPage.getDeliveryCountry(isBillingAndDeliveryAddressSame), productName, qty);
        productCalculationAssertions.validateSubTotal(checkoutPage.getDeliveryCountry(isBillingAndDeliveryAddressSame), productName, qty);
        stdRateAssertion.validateFlatShippingRate(checkoutPage.getDeliveryCountry(isBillingAndDeliveryAddressSame));
        stdRateAssertion.validateEcoTax(checkoutPage.getDeliveryCountry(isBillingAndDeliveryAddressSame), qty);
        productCalculationAssertions.validateVAT(checkoutPage.getDeliveryCountry(isBillingAndDeliveryAddressSame), productName, qty);
        productCalculationAssertions.validateTotal(checkoutPage.getDeliveryCountry(isBillingAndDeliveryAddressSame), productName, qty);

        String actualMsg = checkoutPage.confirmOrder();

        String expectedMsg = AppConstants.ORDER_PLACED_SUCCESS_MESSAGE;

        softAssert.assertEquals(actualMsg, expectedMsg, AppErrors.ORDER_PLACED_SUCCESS_MESSAGE_ERROR);
        softAssert.assertAll();

    }

    @DataProvider
    public Object[][] getPlaceOrderData() {
        return ExcelUtil.getTestData(AppConstants.PLACE_ORDER_GUEST_SHEET_NAME);
    }

}
