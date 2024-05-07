package com.qa.opencart.pages;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.qa.opencart.utils.CSVUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.qa.opencart.constants.AppConstants;
import com.qa.opencart.logger.Log;
import com.qa.opencart.utils.ElementUtil;
import com.qa.opencart.utils.TimeUtil;

import io.qameta.allure.Step;

public class CheckoutPage {
	
	private ElementUtil elUtil;
	private Map<String,String> breakUpDetails = new LinkedHashMap<String, String>();
	String[] billingAddress = CSVUtils.getBillingAddress();
	String[] deliveryAddress = CSVUtils.getDeliveryAddress();

	public CheckoutPage(WebDriver driver) {
		elUtil = new ElementUtil(driver);
	}
	
	private By billingDetailsContinueBtn = By.cssSelector("input#button-payment-address");
	private By billingDetailsContinueBtnForGuest = By.id("button-guest");
	private By deliveryDetailsContinueBtnForGuest = By.id("button-guest-shipping");
	private By shippingDetailsContinueBtn = By.cssSelector("input#button-shipping-address");
	private By shippingMethodContinueBtn = By.cssSelector("input#button-shipping-method");
	private By paymentMethodContinueBtn = By.cssSelector("input#button-payment-method");
	private By termsAndConditionsCheckbox = By.name("agree");
	private String productDetailsXpath = "//table[@class='table table-bordered table-hover']/tbody/tr/td";
	private String productRow = "//table[@class='table table-bordered table-hover']/tbody/tr";
	private By breakUpDetailsXpath = By.xpath("//tfoot//td[2]");
	private By billingAddressDropdown = By.name("address_id");
	private By deliveryAddressDropdown = By.cssSelector("#collapse-shipping-address .form-control");
	private By orderPlacedMessage = By.xpath("//h1");
	private By shippingRateRadioBtnText = By.xpath("//label[contains(normalize-space(),'Flat Shipping Rate')]");
	private By shippingRateRadioBtn = By.cssSelector("#collapse-shipping-method input[name='shipping_method']");
	private By confirmOrder = By.xpath("//input[@value='Confirm Order']");
	private By detailsSectionExpanded = By.xpath("//div[@aria-expanded='true']");
	private By shippingAddressCollapsed = By.id("collapse-shipping-address");
	private By shippingMethodCollapsed = By.id("collapse-shipping-method");
	private By paymentMethodCollapsed = By.id("collapse-payment-method");
	private By confirmOrderCollapsed = By.id("collapse-checkout-confirm");
	private By continueButton = By.xpath("//a[normalize-space()='Continue']");
	private By guestRadioBtn = By.xpath("//input[@value='guest']");
	private By checkoutOptionsContinueBtn = By.cssSelector("input#button-account");
	private By checkoutOptionsCollapsed = By.id("collapse-checkout-option");

	private By billingDetailsCollapsed = By.id("collapse-payment-address");


	private By billingDetailsFirstName = By.id("input-payment-firstname");
	private By billingDetailsLastName = By.id("input-payment-lastname");
	private By billingDetailsEmail = By.id("input-payment-email");
	private By billingDetailstelephone = By.id("input-payment-telephone");
	private By billingDetailsCompany = By.id("input-payment-company");
	private By billingDetailsAddress1 = By.id("input-payment-address-1");
	private By billingDetailsAddress2 = By.id("input-payment-address-2");
	private By billingDetailsCity = By.id("input-payment-city");
	private By billingDetailsPostCode = By.id("input-payment-postcode");
	private By billingDetailsCountryDropdown = By.id("input-payment-country");
	private By billingDetailsRegionDropdown = By.id("input-payment-zone");
	private By deliveryAndBillingAddressSameCheckBox = By.name("shipping_address");

	private By deliveryDetailsFirstName = By.id("input-shipping-firstname");
	private By deliveryDetailsLastName = By.id("input-shipping-lastname");
	private By deliveryDetailsCompany = By.id("input-shipping-company");
	private By deliveryDetailsAddress1 = By.id("input-shipping-address-1");
	private By deliveryDetailsAddress2 = By.id("input-shipping-address-2");
	private By deliveryDetailsCity = By.id("input-shipping-city");
	private By deliveryDetailsPostCode = By.id("input-shipping-postcode");
	private By deliveryDetailsCountry = By.id("input-shipping-country");
	private By deliveryDetailsRegion = By.id("input-shipping-zone");


	public void selectBillingAndDeliveryDetails(String billingCountry, String deliveryCountry) {
		
		elUtil.waitForElementVisibility(TimeUtil.DEFAULT_MEDIUM_TIME, detailsSectionExpanded);
		Log.info("Selecting address from Billing Country: "+billingCountry);
		elUtil.doSelectByPartialText(billingAddressDropdown, billingCountry);
		elUtil.waitForElementVisibility(TimeUtil.DEFAULT_MEDIUM_TIME, billingDetailsContinueBtn).click();	
		
		elUtil.waitForElementAttributeToContain(TimeUtil.DEFAULT_MEDIUM_TIME, shippingAddressCollapsed, "class", "collapse in");
		Log.info("Selecting address from Delivery Country: "+deliveryCountry);
		elUtil.doSelectByPartialText(deliveryAddressDropdown, deliveryCountry);
		elUtil.waitForElementVisibility(TimeUtil.DEFAULT_MEDIUM_TIME, shippingDetailsContinueBtn).click();
	
	}
	
	@Step("Select the Delivery and Payment Methods")
	public void selectDeliverAndPaymentMethod() {
		elUtil.waitForElementVisibility(TimeUtil.DEFAULT_MEDIUM_TIME, shippingMethodContinueBtn).click();
		elUtil.waitForElementVisibility(TimeUtil.DEFAULT_MEDIUM_TIME, termsAndConditionsCheckbox).click();
		
		elUtil.waitForElementAttributeToContain(TimeUtil.DEFAULT_MEDIUM_TIME, paymentMethodCollapsed, "class", "collapse in");
		elUtil.waitForElementVisibility(TimeUtil.DEFAULT_MEDIUM_TIME, paymentMethodContinueBtn).click();
	}
	
	@Step("Confirm order")
	public String confirmOrder() {
		elUtil.waitForElementAttributeToContain(TimeUtil.DEFAULT_MEDIUM_TIME, confirmOrderCollapsed, "class", "collapse in");
		elUtil.clickElement(confirmOrder);
		elUtil.waitForPageTitleIs(TimeUtil.DEFAULT_MEDIUM_TIME, "Your order has been placed!");
		String orderPlacedMessageString = elUtil.getElementText(orderPlacedMessage);
		elUtil.clickElement(continueButton);
		return orderPlacedMessageString;
	}
	
	@Step("Fetching the breakup details for Non-UK country")
	private void getBreakupDetailsForNonUK() {
		List<String> details = elUtil.getElementsText(breakUpDetailsXpath);
		breakUpDetails.put("SubTotal", details.get(0));
		breakUpDetails.put("FlatShippingRate", details.get(1));
		breakUpDetails.put("Total", details.get(2));
	}
	
	@Step("Fetching the breakup details for UK")
	private void getBreakupDetailsForUK() {
		List<String> details = elUtil.getElementsText(breakUpDetailsXpath);
		breakUpDetails.put("SubTotal", details.get(0));
		breakUpDetails.put("FlatShippingRate", details.get(1));
		breakUpDetails.put("EcoTax", details.get(2));
		breakUpDetails.put("VAT", details.get(3));
		breakUpDetails.put("Total", details.get(4));
	}
	
	@Step("Fetch the breakup details for the delivery country {0}")
	public Map<String, String> getBreakUpDetails(String deliveryCountry) {
//		getProductDetails();
		if(deliveryCountry.equals(AppConstants.COUNTRY_WITH_TAXES)) {
			getBreakupDetailsForUK();
		}else {
			getBreakupDetailsForNonUK();
		}	
		return breakUpDetails;
	}
	
	
	public String getOrderPlacedMessage() {
		return elUtil.getElementText(orderPlacedMessage);
	}
	
	@Step("Verify if the Flat Shiping Rate radio button is selected")
	public boolean isFlatShippingRateRadioBtnSelected() {
		elUtil.waitForElementAttributeToContain(TimeUtil.DEFAULT_MEDIUM_TIME, shippingMethodCollapsed, "class", "collapse in");
		return elUtil.isElementSelected(shippingRateRadioBtn);
	}
	
	@Step("Fetch the against the shipping rate radio button")
	public String getFlatShippingRateRadioBtnText() {
		return elUtil.getElementText(shippingRateRadioBtnText);
	}
	
	@Step("Fetch the product details from the page and store in map")
	public Map<String, String[]> getProductDetailsInMap() {
		
		By locator = elUtil.getBy("xpath", productDetailsXpath);
		elUtil.waitForElementsVisibilityWithFluentWait(TimeUtil.DEFAULT_MEDIUM_TIME, 1, locator);
		
		Map<String,String[]> infoMap = new LinkedHashMap<String,String[]>();
		int rows = elUtil.getElements(elUtil.getBy("xpath", productRow)).size();	
		for(int i=1;i<=rows;i++) {
			infoMap.put(getProductFromRow(i), getProductInfo(getProductFromRow(i)));
		}
		return infoMap;
	}
	
	private String getProductFromRow(int rowNum) {
		String xpath = productRow+"["+rowNum+"]//a";
		return elUtil.getElementText(elUtil.getBy("xpath", xpath));
	}
	
	private String[] getProductInfo(String productName) {
		String xpath = "//table[@class='table table-bordered table-hover']//a[normalize-space()='"+productName+"']/parent::td/following-sibling::td";
		return elUtil.getElementsText(elUtil.getBy("xpath", xpath)).toArray(new String[0]);
	}
	
	public Map<String, String> getProductQuantityMap() {
		Map<String,String> prodQuantity = new LinkedHashMap<String,String>();
		for(Entry<String,String[]> entry:getProductDetailsInMap().entrySet()) {
			prodQuantity.put(entry.getKey(),entry.getValue()[1]);
		}
		return prodQuantity;
	}
	
	public List<String> getProducts() {
		String xpath = productRow+"//a";
		return elUtil.getElementsText(elUtil.getBy("xpath", xpath));
	}

	public void selectGuestCheckoutRadioButtonAndContinue(){

		elUtil.waitForElementAttributeToContain(TimeUtil.DEFAULT_MEDIUM_TIME,checkoutOptionsCollapsed,"class","collapse in");
		elUtil.clickElement(guestRadioBtn);
		elUtil.clickElement(checkoutOptionsContinueBtn);

	}

	public void fillBillingAndDeliveryDetails(String isBillingAndDeliveryAddressSame){

		elUtil.waitForElementAttributeToContain(TimeUtil.DEFAULT_MEDIUM_TIME,billingDetailsCollapsed,"class","collapse in");
		elUtil.sendKeysToElement(billingDetailsFirstName,billingAddress[0]);
		elUtil.sendKeysToElement(billingDetailsLastName,billingAddress[1]);
		elUtil.sendKeysToElement(billingDetailsEmail,billingAddress[2]);
		elUtil.sendKeysToElement(billingDetailstelephone,billingAddress[3]);
//		elUtil.sendKeysToElement(billingDetailsCompany,"");
		elUtil.sendKeysToElement(billingDetailsAddress1,billingAddress[4]);
//		elUtil.sendKeysToElement(billingDetailsAddress2,"");
		elUtil.sendKeysToElement(billingDetailsCity,billingAddress[5]);
		elUtil.sendKeysToElement(billingDetailsPostCode,billingAddress[6]);

		elUtil.doSelectByVisibleText(billingDetailsCountryDropdown,billingAddress[8]);
		TimeUtil.shortTime();
		elUtil.doSelectByVisibleText(billingDetailsRegionDropdown,billingAddress[7]);

		if(Boolean.parseBoolean(isBillingAndDeliveryAddressSame)){
			if(!elUtil.isElementSelected(deliveryAndBillingAddressSameCheckBox)){
				elUtil.clickElement(deliveryAndBillingAddressSameCheckBox);
			}
			elUtil.clickElement(billingDetailsContinueBtnForGuest);
		}else{
			if(elUtil.isElementSelected(deliveryAndBillingAddressSameCheckBox)){
				elUtil.clickElement(deliveryAndBillingAddressSameCheckBox);
			}
			elUtil.clickElement(billingDetailsContinueBtnForGuest);
			fillDeliveryDetails();
		}

	}

	private void fillDeliveryDetails(){

		elUtil.waitForElementAttributeToContain(TimeUtil.DEFAULT_MEDIUM_TIME, shippingAddressCollapsed, "class", "collapse in");
		elUtil.sendKeysToElement(deliveryDetailsFirstName,deliveryAddress[0]);
		elUtil.sendKeysToElement(deliveryDetailsLastName,deliveryAddress[1]);
//		elUtil.sendKeysToElement(deliveryDetailsCompany,"");
		elUtil.sendKeysToElement(deliveryDetailsAddress1,deliveryAddress[2]);
//		elUtil.sendKeysToElement(deliveryDetailsAddress2,"");
		elUtil.sendKeysToElement(deliveryDetailsCity,deliveryAddress[3]);
		elUtil.sendKeysToElement(deliveryDetailsPostCode,deliveryAddress[4]);
		elUtil.doSelectByVisibleText(deliveryDetailsCountry,deliveryAddress[5]);
		elUtil.doSelectByVisibleText(deliveryDetailsRegion,deliveryAddress[6]);
		elUtil.clickElement(deliveryDetailsContinueBtnForGuest);

	}

	public String getDeliveryCountry(String isBillingAndDeliveryAddressSame){

		String deliveryCountry;
		if(Boolean.parseBoolean(isBillingAndDeliveryAddressSame)){
			deliveryCountry = billingAddress[8];
		}else{
			deliveryCountry = deliveryAddress[5];
		}
		return deliveryCountry;
	}

}
