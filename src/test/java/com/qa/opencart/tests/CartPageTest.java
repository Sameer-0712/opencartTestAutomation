package com.qa.opencart.tests;

import com.qa.opencart.base.BaseTest;
import com.qa.opencart.constants.AppConstants;
import com.qa.opencart.pages.LoginPage;
import com.qa.opencart.pages.ProductPage;
import com.qa.opencart.productassertions.ProductCalculationAssertionsOnCartPage;
import com.qa.opencart.utils.AppUtils;
import com.qa.opencart.utils.CSVUtils;
import com.qa.opencart.utils.CostCalculation;
import com.qa.opencart.utils.StringUtil;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

import java.util.*;

import static com.qa.opencart.constants.AppConstants.PRODUCTS_TEST_DATA;

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
      productNamesQuantity = new HashMap<String,String>();
      List<String[]> productsData =  CSVUtils.csvData(PRODUCTS_TEST_DATA);

      int totalQuantity = 0;
      for(String[] e : productsData){
          productNamesQuantity.put(e[1],e[2]);
          appUtil.addProductToCart(e[0],e[1],e[2]);
          totalQuantity = totalQuantity + Integer.parseInt(e[2]);
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
        while (productNamesQuantity.size() > 1){
            cartPage.removeProductFromCart(getEntrySetIterator().next().getKey());
            totalQuantity = totalQuantity - Integer.parseInt(getEntrySetIterator().next().getValue());
            successMsg = cartPage.applyShippingRate(countryRegionPinData[0],countryRegionPinData[1],countryRegionPinData[2]);
            productNamesQuantity.remove(getEntrySetIterator().next().getKey());
            assertCalculations(successMsg,softAssert,deliveryCountry,totalQuantity);
        }

    }

    @Test()
    public void verifyTotalChangesByUpdatingProductQuantityInCart(){

        cartPage = productPage.navigateToCart();
        productCalculationAssertionsOnCartPage = new ProductCalculationAssertionsOnCartPage(softAssert,cartPage);
        Map<String,String> breakUpMap = null;
        String expectedTotalInCart = null;
        String actualTotalInCart = null;
        String expectedSubTotal = null;
        String actualSubTotal = null;
        String expectedTotal = null;
        String actualTotal = null;

        Iterator<Map.Entry<String, String>> it =  getEntrySetIterator();

        Random rn = new Random();
        int randomQuantity = 0;
        String key = null;
        while ((it.hasNext())){
            randomQuantity = rn.nextInt(10) + 1;
            key = it.next().getKey();
            productNamesQuantity.put(key,String.valueOf(randomQuantity));
            cartPage.updateProductFromCart(key, randomQuantity);
            expectedTotalInCart = CostCalculation.calculateTotalPriceWithoutTaxes(AppConstants.getProductPrice(key),randomQuantity);
            actualTotalInCart = cartPage.getProductTotalFromCartTable(key);

            expectedSubTotal = CostCalculation.calculateSubTotalUsingProductNameQuantity(productNamesQuantity);
            actualSubTotal = cartPage.getCostBreakUp().get("Sub-Total");

            expectedTotal = CostCalculation.calculateSubTotalUsingProductNameQuantity(productNamesQuantity);
            actualTotal = cartPage.getCostBreakUp().get("Total");

            softAssert.assertEquals(StringUtil.removeSpecialCharacters(actualTotalInCart),Double.parseDouble(expectedTotalInCart));
            softAssert.assertEquals(StringUtil.removeSpecialCharacters(actualSubTotal),Double.parseDouble(expectedSubTotal));
            softAssert.assertEquals(StringUtil.removeSpecialCharacters(actualTotal),Double.parseDouble(expectedTotal));
            softAssert.assertAll();
        }


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

    private Iterator<Map.Entry<String, String>> getEntrySetIterator(){

        Set<Map.Entry<String,String>> entrySet =  productNamesQuantity.entrySet();
        return entrySet.iterator();

    }

}




