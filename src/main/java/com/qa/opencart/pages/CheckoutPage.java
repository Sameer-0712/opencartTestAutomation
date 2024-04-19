package com.qa.opencart.pages;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.qa.opencart.utils.ElementUtil;
import com.qa.opencart.utils.TimeUtil;

public class CheckoutPage {
	
	private WebDriver driver;
	private ElementUtil elUtil;
	private Map<String,String> orderDetails = new HashMap<String, String>();

	public CheckoutPage(WebDriver driver) {
		this.driver = driver;
		elUtil = new ElementUtil(driver);
	}
	
	private By billingDetailsContinueBtn = By.cssSelector("input#button-payment-address");
	private By shippingDetailsContinueBtn = By.cssSelector("input#button-shipping-address");
	private By shippingMethodContinueBtn = By.cssSelector("input#button-shipping-method");
	private By paymentMethodContinueBtn = By.cssSelector("input#button-payment-method");
	private By termsAndConditionsCheckbox = By.name("agree");
	private By productDetails = By.xpath("//table[@class='table table-bordered table-hover']/tbody//td");
	private By productNameInProductDetails = By.xpath("//table[@class='table table-bordered table-hover']/tbody//td/a");
	private By breakUpDetails = By.xpath("//tfoot//td[2]");
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

	public void selectBillingAndDeliveryDetails(String billingCountry, String deliveryCountry) {
		
		elUtil.waitForElementVisibility(TimeUtil.DEFAULT_MEDIUM_TIME, detailsSectionExpanded);
		elUtil.doSelectByPartialText(billingAddressDropdown, billingCountry);
		elUtil.waitForElementVisibility(TimeUtil.DEFAULT_MEDIUM_TIME, billingDetailsContinueBtn).click();	
		
		elUtil.waitForElementAttributeToContain(TimeUtil.DEFAULT_MEDIUM_TIME, shippingAddressCollapsed, "class", "collapse in");
		elUtil.doSelectByPartialText(deliveryAddressDropdown, deliveryCountry);
		elUtil.waitForElementVisibility(TimeUtil.DEFAULT_MEDIUM_TIME, shippingDetailsContinueBtn).click();
	
	}
	
	public void selectDeliverAndPaymentMethod() {
		elUtil.waitForElementVisibility(TimeUtil.DEFAULT_MEDIUM_TIME, shippingMethodContinueBtn).click();
		elUtil.waitForElementVisibility(TimeUtil.DEFAULT_MEDIUM_TIME, termsAndConditionsCheckbox).click();
		
		elUtil.waitForElementAttributeToContain(TimeUtil.DEFAULT_MEDIUM_TIME, paymentMethodCollapsed, "class", "collapse in");
		elUtil.waitForElementVisibility(TimeUtil.DEFAULT_MEDIUM_TIME, paymentMethodContinueBtn).click();
	}
	
	public String confirmOrder() {
		elUtil.waitForElementAttributeToContain(TimeUtil.DEFAULT_MEDIUM_TIME, confirmOrderCollapsed, "class", "collapse in");
		elUtil.clickElement(confirmOrder);
		elUtil.waitForPageTitleIs(TimeUtil.DEFAULT_MEDIUM_TIME, "Your order has been placed!");
		String orderPlacedMessageString = elUtil.getElementText(orderPlacedMessage);
		elUtil.clickElement(continueButton);
		return orderPlacedMessageString;
	}
	
	private void getProductDetails() {	
		elUtil.waitForElementsPresenceWithFluentWait(TimeUtil.DEFAULT_MEDIUM_TIME, 1, productDetails);
		List<String> details = elUtil.getElementsText(productDetails);
		orderDetails.put("ProductName", elUtil.getElementText(productNameInProductDetails));
		orderDetails.put("Model", details.get(1));
		orderDetails.put("Quantity", details.get(2));
		orderDetails.put("UnitPrice", details.get(3));
		orderDetails.put("TotalBeforeTaxes", details.get(4));
	}
	
	private void getBreakupDetails() {
		List<String> details = elUtil.getElementsText(breakUpDetails);
		orderDetails.put("SubTotal", details.get(0));
		orderDetails.put("FlatShippingRate", details.get(1));
		orderDetails.put("Total", details.get(4));
	}
	
	private void getBreakupDetailsForUK() {
		List<String> details = elUtil.getElementsText(breakUpDetails);
		orderDetails.put("SubTotal", details.get(0));
		orderDetails.put("FlatShippingRate", details.get(1));
		orderDetails.put("EcoTax", details.get(2));
		orderDetails.put("VAT", details.get(3));
		orderDetails.put("Total", details.get(4));
	}
	
	public Map<String, String> getOrderDetails() {
		getProductDetails();
		getBreakupDetails();
		return orderDetails;
	}
	
	public Map<String, String> getOrderDetailsForUK() {
		getProductDetails();
		getBreakupDetailsForUK();
		return orderDetails;
	}
	
	public String getOrderPlacedMessage() {
		return elUtil.getElementText(orderPlacedMessage);
	}
	
	public boolean isFlatShippingRateRadioBtnSelected() {
		elUtil.waitForElementAttributeToContain(TimeUtil.DEFAULT_MEDIUM_TIME, shippingMethodCollapsed, "class", "collapse in");
		return elUtil.isElementSelected(shippingRateRadioBtn);
	}
	
	public String getFlatShippingRateRadioBtnText() {
		return elUtil.getElementText(shippingRateRadioBtnText);
	}

}
