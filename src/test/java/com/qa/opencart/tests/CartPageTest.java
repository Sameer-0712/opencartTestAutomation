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
    private Object[][] data = new Object[][] {{"ipod","iPod Classic","3"},{"sony","Sony VAIO","2"},{"hp","HP LP3065","7"}};

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

    @Test(dataProvider = "getData",enabled = false)
    public void verifyFlatShippingRateAndTaxesAreAppliedOnTheBasisOfDeliveryCountry(String deliveryCountry){

        cartPage = productPage.navigateToCart();
        productCalculationAssertionsOnCartPage = new ProductCalculationAssertionsOnCartPage(softAssert,cartPage);
        String[] countryRegionPinData = CSVUtils.getCountryRegionPinData(deliveryCountry);
        String successMsg = cartPage.applyShippingRate(countryRegionPinData[0],countryRegionPinData[1],countryRegionPinData[2]);
        assertCalculations(successMsg,softAssert,deliveryCountry,quantity);
    }

    @Test(dataProvider = "getData",enabled = false)
    public void verifyTaxesAndTotalChangesByRemovingProductFromCart(String deliveryCountry){

        cartPage = productPage.navigateToCart();
        productCalculationAssertionsOnCartPage = new ProductCalculationAssertionsOnCartPage(softAssert,cartPage);
        String[] countryRegionPinData = CSVUtils.getCountryRegionPinData(deliveryCountry);
        String successMsg = null;
        int totalQuantity = quantity;
        int i = 0;
        while (productNamesQuantity.size() > 1){
            cartPage.removeProductFromCart((String) data[i][1]);
            totalQuantity = totalQuantity - Integer.parseInt((String) data[i][2]);
            successMsg = cartPage.applyShippingRate(countryRegionPinData[0],countryRegionPinData[1],countryRegionPinData[2]);
            productNamesQuantity.remove((String) data[i][1]);
            assertCalculations(successMsg,softAssert,deliveryCountry,totalQuantity);
            i++;
        }

    }

    //WIP
    @Test(dataProvider = "getData")
    public void verifyTaxesAndTotalChangesByUpdatingProductQuantityInCart(String deliveryCountry){

        cartPage = productPage.navigateToCart();
        productCalculationAssertionsOnCartPage = new ProductCalculationAssertionsOnCartPage(softAssert,cartPage);
        String[] countryRegionPinData = CSVUtils.getCountryRegionPinData(deliveryCountry);

        cartPage.updateProductFromCart((String) data[1][1],6);
        updateProductQuantities((String) data[1][1],6);
        String successMsg = cartPage.applyShippingRate(countryRegionPinData[0],countryRegionPinData[1],countryRegionPinData[2]);
        assertCalculations(successMsg,softAssert,deliveryCountry,getSumOfQuantities());

        cartPage.updateProductFromCart((String) data[1][2],9);
        updateProductQuantities((String) data[1][2],9);
        successMsg = cartPage.applyShippingRate(countryRegionPinData[0],countryRegionPinData[1],countryRegionPinData[2]);
        assertCalculations(successMsg,softAssert,deliveryCountry,getSumOfQuantities());

        cartPage.updateProductFromCart((String) data[1][3],4);
        updateProductQuantities((String) data[1][3],4);
        successMsg = cartPage.applyShippingRate(countryRegionPinData[0],countryRegionPinData[1],countryRegionPinData[2]);
        assertCalculations(successMsg,softAssert,deliveryCountry,getSumOfQuantities());


    }

    @AfterMethod
    public void emptyCart(){
        page.doEmptyCart();
    }

    @DataProvider
    public Object[][] getData(){
        return new Object[][] {{"United Kingdom"}};
    }

    private void assertCalculations(String successMsg, SoftAssert softAssert, String deliveryCountry, int quantity){
        softAssert.assertEquals(successMsg,AppConstants.SHIPPING_RATE_APPLIED_SUCCESS);
        productCalculationAssertionsOnCartPage.validateSubTotal(deliveryCountry,productNamesQuantity);
        productCalculationAssertionsOnCartPage.validateFlatShippingRate(deliveryCountry);
        productCalculationAssertionsOnCartPage.validateEcoTax(deliveryCountry,quantity);
        productCalculationAssertionsOnCartPage.validateVAT(deliveryCountry,productNamesQuantity);
        productCalculationAssertionsOnCartPage.validateTotal(deliveryCountry,productNamesQuantity);
        softAssert.assertAll();
    }

    private void updateProductQuantities(String product, int quantity){

        String d_string = null;
        for(int i=0;i<data.length;i++){
            d_string = (String) data[i][1];
            if(d_string.equals(product)){
                data[i][2] = quantity;
            }
        }

    }

    private int getSumOfQuantities(){

        int sum = 0;

        for(Object[] d: data){
            sum = sum + Integer.parseInt(String.valueOf(d[2]));
        }
        return sum;

    }

}
