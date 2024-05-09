package com.qa.opencart.productassertions;

import com.qa.opencart.pages.CheckoutPage;
import org.openqa.selenium.WebDriver;
import org.testng.asserts.SoftAssert;

public class ShippingRateRadioButtonAssertions {


    private CheckoutPage checkoutPage;
    private SoftAssert softAssert;
    private StandardRateAssertions stdRateAssertion;

    public ShippingRateRadioButtonAssertions(WebDriver driver, SoftAssert softAssert) {
        this.softAssert = softAssert;
        checkoutPage = new CheckoutPage(driver);
    }

    public void assertShippingRadioButtonIsSelected(){
        softAssert.assertTrue(checkoutPage.isFlatShippingRateRadioBtnSelected());
    }

    public void assertShippingRadioButtonText(String deliveryCountry){
        stdRateAssertion = new StandardRateAssertions(softAssert, checkoutPage);
        stdRateAssertion.validateShippingRateInDeliveryMethodStep(deliveryCountry);
    }
}
