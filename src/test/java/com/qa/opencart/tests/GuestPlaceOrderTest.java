package com.qa.opencart.tests;

import com.qa.opencart.base.BaseTest;
import com.qa.opencart.constants.AppConstants;
import com.qa.opencart.errors.AppErrors;
import com.qa.opencart.productassertions.ProductCalculationAssertions;
import com.qa.opencart.productassertions.ProductInfoAssertions;
import com.qa.opencart.productassertions.ShippingRateRadioButtonAssertions;
import com.qa.opencart.productassertions.StandardRateAssertions;
import com.qa.opencart.utils.AppUtils;
import com.qa.opencart.utils.ExcelUtil;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class GuestPlaceOrderTest extends BaseTest {

    @Test(dataProvider = "getPlaceOrderData")
    public void validatePlaceSingleItemOrderWithGuest(String searchKey, String productName,
                                                      String quantity, String isBillingAndDeliveryAddressSame){
        //Arrange
        AppUtils appUtil = new AppUtils(page, searchResultsPage, productPage, cartPage, checkoutPage);
        int qty = Integer.parseInt(quantity);
        softAssert = new SoftAssert();
        shippingRateRadioButtonAssertions = new ShippingRateRadioButtonAssertions(driver,softAssert);
        String expectedMsg = AppConstants.ORDER_PLACED_SUCCESS_MESSAGE;

        //Act
        appUtil.addProductToCart(searchKey, productName, quantity);
        checkoutPage = appUtil.navigateToCheckOutPageAndSelectGuestCheckout();
        checkoutPage.fillBillingAndDeliveryDetails(isBillingAndDeliveryAddressSame);
        String deliveryCountry = checkoutPage.getDeliveryCountry(isBillingAndDeliveryAddressSame);

        //Assert
        shippingRateRadioButtonAssertions.assertShippingRadioButtonIsSelected();
        shippingRateRadioButtonAssertions.assertShippingRadioButtonText(deliveryCountry);

        //Act
        checkoutPage.selectDeliverAndPaymentMethod();

        //Assert
        productInfoAssertions = new ProductInfoAssertions(softAssert, checkoutPage);
        productInfoAssertions.validateProductAssertions(deliveryCountry, productName, qty);
        productCalculationAssertions = new ProductCalculationAssertions(softAssert, checkoutPage);
        productCalculationAssertions.validateTotalWithoutTaxes(deliveryCountry, productName, qty);
        productCalculationAssertions.validateSubTotal(deliveryCountry, productName, qty);
        stdRateAssertion = new StandardRateAssertions(softAssert,checkoutPage);
        stdRateAssertion.validateFlatShippingRate(deliveryCountry);
        stdRateAssertion.validateEcoTax(deliveryCountry, qty);
        productCalculationAssertions.validateVAT(deliveryCountry, productName, qty);
        productCalculationAssertions.validateTotal(deliveryCountry, productName, qty);

        //Act
        String actualMsg = checkoutPage.confirmOrder();

        //Assert
        softAssert.assertEquals(actualMsg, expectedMsg, AppErrors.ORDER_PLACED_SUCCESS_MESSAGE_ERROR);
        softAssert.assertAll();

    }

    @DataProvider
    public Object[][] getPlaceOrderData() {
        return ExcelUtil.getTestData(AppConstants.PLACE_ORDER_GUEST_SHEET_NAME);
    }

    //************************************************************************************************

    @Test(dataProvider = "getFlag")
    public void validatePlaceMultipleItemOrderWithGuest(String isBillingAndDeliveryAddressSame){
        //Arrange
        AppUtils appUtil = new AppUtils(page, searchResultsPage, productPage, cartPage, checkoutPage);
        softAssert = new SoftAssert();
        shippingRateRadioButtonAssertions = new ShippingRateRadioButtonAssertions(driver,softAssert);
        String expectedMsg = AppConstants.ORDER_PLACED_SUCCESS_MESSAGE;

        //Act
        appUtil.addProductsToCart();
        checkoutPage = appUtil.navigateToCheckOutPageAndSelectGuestCheckout();
        checkoutPage.fillBillingAndDeliveryDetails(isBillingAndDeliveryAddressSame);
        String deliveryCountry = checkoutPage.getDeliveryCountry(isBillingAndDeliveryAddressSame);

        //Assert
        shippingRateRadioButtonAssertions.assertShippingRadioButtonIsSelected();
        shippingRateRadioButtonAssertions.assertShippingRadioButtonText(deliveryCountry);

        //Act
        checkoutPage.selectDeliverAndPaymentMethod();

        //Assert
        productInfoAssertions = new ProductInfoAssertions(softAssert, checkoutPage);
        productInfoAssertions.validateProductAssertions(deliveryCountry);
        productCalculationAssertions = new ProductCalculationAssertions(softAssert, checkoutPage);
        productCalculationAssertions.validateSubTotalForEachProduct(deliveryCountry);
        productCalculationAssertions.validateSubTotal(deliveryCountry);
        stdRateAssertion = new StandardRateAssertions(softAssert,checkoutPage);
        stdRateAssertion.validateFlatShippingRate(deliveryCountry);
        stdRateAssertion.validateTotalEcoTax(deliveryCountry);
        productCalculationAssertions.validateTotalVAT(deliveryCountry);
        productCalculationAssertions.validateTotalForMultipleProducts(deliveryCountry);

        //Act
        String actualMsg = checkoutPage.confirmOrder();

        //Assert
        softAssert.assertEquals(actualMsg, expectedMsg, AppErrors.ORDER_PLACED_SUCCESS_MESSAGE_ERROR);
        softAssert.assertAll();

    }

    /*
    returns the boolean flag if the billing and delivery countries are same or not
     */
    @DataProvider
    public Object[][] getFlag() {
        return new Object[][] { { "true" }, { "false" } };
    }

}
