package com.qa.opencart.tests;

import com.qa.opencart.base.BaseTest;
import com.qa.opencart.constants.AppConstants;
import com.qa.opencart.pages.LoginPage;
import com.qa.opencart.pages.ProductPage;
import com.qa.opencart.productassertions.ProductCalculationAssertionsOnCartPage;
import com.qa.opencart.utils.AppUtils;
import com.qa.opencart.utils.CSVUtils;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

public class CartPageTest extends BaseTest {

    private String productName;
    private String quantity;

    @BeforeClass
    public void loginToApp() {
        loginPage = new LoginPage(driver);
        accPage = loginPage.doLogin(prop.getProperty("username"), prop.getProperty("password"));

        appUtil = new AppUtils(page, searchResultsPage, productPage, cartPage, checkoutPage);
        softAssert = new SoftAssert();
        productPage = new ProductPage(driver);

    }

    @BeforeMethod()
    public void addToCart(){
      Object[][] data = new Object[][] {{"mac","MacBook Pro","1"}};
      for(Object[] d:data){
          String searchKey = (String) d[0];
          productName = (String) d[1];
          quantity = (String) d[2];
          appUtil.addProductToCart(searchKey,productName,quantity);
      }
    }

    @Test(dataProvider = "getData")
    public void verifyFlatShippingRateIsAppliedOnTheBasisOfDeliveryCountry(String deliveryCountry){

        int qty = Integer.parseInt(quantity);

        cartPage = productPage.navigateToCart();
        productCalculationAssertionsOnCartPage = new ProductCalculationAssertionsOnCartPage(softAssert,cartPage);
        String[] countryRegionPinData = CSVUtils.getCountryRegionPinData(deliveryCountry);
        String successMsg = cartPage.applyShippingRate(countryRegionPinData[0],countryRegionPinData[1],countryRegionPinData[2]);
        softAssert.assertEquals(successMsg,AppConstants.SHIPPING_RATE_APPLIED_SUCCESS);

        productCalculationAssertionsOnCartPage.validateSubTotal(deliveryCountry,productName,qty);
        productCalculationAssertionsOnCartPage.validateFlatShippingRate(deliveryCountry);
        productCalculationAssertionsOnCartPage.validateEcoTax(deliveryCountry,qty);
        productCalculationAssertionsOnCartPage.validateVAT(deliveryCountry,productName,qty);
        productCalculationAssertionsOnCartPage.validateTotal(deliveryCountry,productName,qty);

        softAssert.assertAll();
    }

    @AfterMethod
    public void emptyCart(){
        page.doEmptyCart();
    }

    @DataProvider
    public Object[][] getData(){
        return new Object[][] {{"United Kingdom"},{"India"}};
    }

}
