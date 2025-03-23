package com.qa.opencart.tests;

import com.qa.opencart.base.BaseTest;
import com.qa.opencart.constants.AppConstants;
import com.qa.opencart.pages.LoginPage;
import com.qa.opencart.pages.ProductPage;
import com.qa.opencart.productassertions.Assertions;
import com.qa.opencart.utils.AppUtils;
import com.qa.opencart.utils.CSVUtils;
import com.qa.opencart.utils.CostCalculation;
import com.qa.opencart.utils.StringUtil;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

import java.util.*;

import static com.qa.opencart.constants.AppConstants.PRODUCTS_TEST_DATA;

public class CartPageTest extends BaseTest {

    private Map<String,Integer> productNamesQuantity;
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
      productNamesQuantity = new HashMap<String,Integer>();
      List<String[]> productsData =  CSVUtils.csvData(PRODUCTS_TEST_DATA);

      int totalQuantity = 0;
      for(String[] e : productsData){
          productNamesQuantity.put(e[1],Integer.parseInt(e[2]));
          appUtil.addProductToCart(e[0],e[1],e[2]);
          totalQuantity = totalQuantity + Integer.parseInt(e[2]);
      }
        quantity = totalQuantity;
    }

    @Test(dataProvider = "getData", enabled = false)
    public void verifyFlatShippingRateAndTaxesAreAppliedOnTheBasisOfDeliveryCountry(String deliveryCountry){

        cartPage = productPage.navigateToCart();
        String[] countryRegionPinData = CSVUtils.getCountryRegionPinData(deliveryCountry);
        String successMsg = cartPage.applyShippingRate(countryRegionPinData[0],countryRegionPinData[1],countryRegionPinData[2]);
        Assertions assertions = new Assertions(softAssert,cartPage,deliveryCountry,quantity);
        assertions.validateProductCalculationsOnCartPage(successMsg,productNamesQuantity);
    }

    @Test(dataProvider = "getData", enabled = false)
    public void verifyTaxesAndTotalChangesByRemovingProductFromCart(String deliveryCountry){

        cartPage = productPage.navigateToCart();
        String[] countryRegionPinData = CSVUtils.getCountryRegionPinData(deliveryCountry);
        String successMsg = null;
        int totalQuantity = quantity;
        while (productNamesQuantity.size() > 1){
            cartPage.removeProductFromCart(getEntrySetIterator().next().getKey());
            totalQuantity = totalQuantity - getEntrySetIterator().next().getValue();
            successMsg = cartPage.applyShippingRate(countryRegionPinData[0],countryRegionPinData[1],countryRegionPinData[2]);
            productNamesQuantity.remove(getEntrySetIterator().next().getKey());
            Assertions assertions = new Assertions(softAssert,cartPage,deliveryCountry,totalQuantity);
            assertions.validateProductCalculationsOnCartPage(successMsg,productNamesQuantity);
        }

    }

    @Test(dataProvider = "getData")
    public void verifyTotalChangesByUpdatingProductQuantityInCart(String deliveryCountry){

        cartPage = productPage.navigateToCart();
        double expectedTotalInCartTable;
        double actualTotalInCartTable;
        double expectedSubTotalInBreakUpTable;
        double actualSubTotalInBreakUpTable;
        double expectedTotalInBreakUpTable;
        double actualTotalInBreakUpTable;
        String[] countryRegionPinData = CSVUtils.getCountryRegionPinData(deliveryCountry);
        String successMsg = null;

        Iterator<Map.Entry<String, Integer>> it =  getEntrySetIterator();

        Random rn = new Random();
        int randomQuantity = 0;
        String key = null;
        while ((it.hasNext())){
            randomQuantity = rn.nextInt(10) + 1;
            key = it.next().getKey();
            productNamesQuantity.put(key,randomQuantity);
            cartPage.updateProductFromCart(key, randomQuantity);
            if(deliveryCountry.equals(AppConstants.COUNTRY_WITH_TAXES)){
                expectedTotalInCartTable = CostCalculation.calculateUnitPrice(key,randomQuantity);
            }else{
                expectedTotalInCartTable = CostCalculation.calculateSubTotalForSingleProduct(key,randomQuantity);
            }
            successMsg = cartPage.applyShippingRate(countryRegionPinData[0],countryRegionPinData[1],countryRegionPinData[2]);
            actualTotalInCartTable = StringUtil.removeSpecialCharacters(cartPage.getProductTotalFromCartTable(key));
            softAssert.assertEquals(actualTotalInCartTable, expectedTotalInCartTable, String.format("Actual Total for %s in Cart table is not equal to the expected total in Cart table",key));
        }
            expectedSubTotalInBreakUpTable = CostCalculation.calculateSubTotalForMultipleProducts(productNamesQuantity);
            actualSubTotalInBreakUpTable = StringUtil.removeSpecialCharacters(cartPage.getCostBreakUp().get("Sub-Total"));

            expectedTotalInBreakUpTable = CostCalculation.calculateTotalForMultipleProducts(deliveryCountry,productNamesQuantity);
            actualTotalInBreakUpTable = StringUtil.removeSpecialCharacters(cartPage.getCostBreakUp().get("Total"));

        softAssert.assertEquals(successMsg,AppConstants.SHIPPING_RATE_APPLIED_SUCCESS);
        softAssert.assertEquals(actualSubTotalInBreakUpTable, expectedSubTotalInBreakUpTable, "Actual SubTotal in Breakup table is not equal to the expected SubTotal in Breakup table");
        softAssert.assertEquals(actualTotalInBreakUpTable,expectedTotalInBreakUpTable,"Actual Total in Breakup table is not equal to the expected total in Breakup table");
        softAssert.assertAll();

    }

    @AfterMethod
    public void emptyCart(){
        page.doEmptyCart();
    }

    @DataProvider
    public Object[][] getData(){
        return new Object[][] {{"United Kingdom"},{"Japan"}};
    }

    private Iterator<Map.Entry<String, Integer>> getEntrySetIterator(){

        Set<Map.Entry<String,Integer>> entrySet =  productNamesQuantity.entrySet();
        return entrySet.iterator();

    }

}




