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

import java.util.HashMap;
import java.util.Map;

public class CartPageTest extends BaseTest {

    private Map<String,String> productNamesQuantity;
    private int quantity;

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
      Object[][] data = new Object[][] {{"ipod","iPod Classic","3"},{"sony","Sony VAIO","2"},{"hp","HP LP3065","7"}};
      productNamesQuantity = new HashMap<String,String>();
      int totalQuantity = 0;
      for(Object[] d:data){
          String searchKey = (String) d[0];
          productNamesQuantity.put((String) d[1],(String) d[2]);
          appUtil.addProductToCart(searchKey,(String) d[1],String.valueOf(d[2]));
          totalQuantity = totalQuantity + Integer.parseInt(String.valueOf(d[2]));
      }
      quantity = totalQuantity;
    }

    @Test(dataProvider = "getData")
    public void verifyFlatShippingRateIsAppliedOnTheBasisOfDeliveryCountry(String deliveryCountry){

        cartPage = productPage.navigateToCart();
        productCalculationAssertionsOnCartPage = new ProductCalculationAssertionsOnCartPage(softAssert,cartPage);
        String[] countryRegionPinData = CSVUtils.getCountryRegionPinData(deliveryCountry);
        String successMsg = cartPage.applyShippingRate(countryRegionPinData[0],countryRegionPinData[1],countryRegionPinData[2]);
        softAssert.assertEquals(successMsg,AppConstants.SHIPPING_RATE_APPLIED_SUCCESS);

        productCalculationAssertionsOnCartPage.validateSubTotal(deliveryCountry,productNamesQuantity);
        productCalculationAssertionsOnCartPage.validateFlatShippingRate(deliveryCountry);
        productCalculationAssertionsOnCartPage.validateEcoTax(deliveryCountry,quantity);
        productCalculationAssertionsOnCartPage.validateVAT(deliveryCountry,productNamesQuantity);
        productCalculationAssertionsOnCartPage.validateTotal(deliveryCountry,productNamesQuantity);

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
