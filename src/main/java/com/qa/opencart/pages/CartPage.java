package com.qa.opencart.pages;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.qa.opencart.constants.AppConstants;
import com.qa.opencart.exceptions.FrameworkExceptions;
import com.qa.opencart.utils.CSVUtils;
import com.qa.opencart.utils.JavaScriptUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.qa.opencart.utils.TimeUtil;

public class CartPage extends Page {

	private Map<String,String> prodDetails = new HashMap<String,String>();
	private Map<String,String> costBreakUp = new HashMap<String,String>();
	private JavaScriptUtils jsUtil;

	public CartPage(WebDriver driver) {
        super(driver);
		jsUtil = new JavaScriptUtils(driver);
	}
	
	private By countryDropdown = By.id("input-country");
	private By regionDropdown = By.id("input-zone");
	private By pin = By.name("postcode");
	private By getQuotes = By.id("button-quote");
	private By flatRateRadioBtn = By.name("shipping_method");
	private By applyShippingBtn = By.cssSelector("input#button-shipping");
	private By successMsg = By.xpath("//div/i");
	private By costs = By.xpath("(//table[@class='table table-bordered'])[3]//tr/td[position()=2]");
	private By checkoutBtn = By.xpath("//a[text()='Checkout']");
	private By estimateShippingAndTaxes = By.cssSelector("div.panel-default:nth-of-type(2) a");
	private By estimateShippingAndTaxesPanel = By.id("collapse-shipping");
	
	private void getProductModelAndPrice(String productName) {
		By xpath = By.xpath("(//a[normalize-space()='"+productName+"'])[2]/parent::td/following-sibling::td[not(position()=2)]");
		List<String> values = elUtil.getElementsText(xpath);
		prodDetails.put("Model", values.get(0));
		prodDetails.put("UnitPrice", values.get(1));
		prodDetails.put("Total", values.get(2));
	}
	
	private void getProductQuantityDetailsFromCart(String productName) {
		By xpath = By.xpath("(//a[normalize-space()='"+productName+"'])[2]/parent::td/following-sibling::td[2]//input");
		prodDetails.put("quantity", elUtil.getElementAttribute(xpath, "value"));
	}
	
	public Map<String,String> getProductDetailsFromCart(String productName){
		getProductModelAndPrice(productName);
		getProductQuantityDetailsFromCart(productName);
		return prodDetails;
	}

	public String applyShippingRate(String country, String region, String pin) {
		elUtil.clickElement(estimateShippingAndTaxes);
		elUtil.waitForElementAttributeToContain(TimeUtil.DEFAULT_MEDIUM_TIME,estimateShippingAndTaxesPanel,"class","collapse in");
		elUtil.doSelectByVisibleText(countryDropdown, country);
		TimeUtil.defaultTime();
		elUtil.doSelectByVisibleText(regionDropdown, region);
		elUtil.sendKeysToElement(this.pin, pin);
		elUtil.clickElement(getQuotes);
		elUtil.waitForElementVisibility(TimeUtil.DEFAULT_MEDIUM_TIME, flatRateRadioBtn).click();
		elUtil.clickElement(applyShippingBtn);
		TimeUtil.defaultTime();
		return jsUtil.getTextFromTextNode(elUtil.getElement(successMsg)).trim();
	}
	
	public Map<String, String> getCostBreakUpForUK() {
		
		List<String> costs = elUtil.getElementsText(this.costs);
		costBreakUp.put("Sub-Total", costs.get(0));
		costBreakUp.put("Flat Shipping Rate", costs.get(1));
		costBreakUp.put("Eco Tax", costs.get(2));
		costBreakUp.put("VAT", costs.get(3));
		costBreakUp.put("Total", costs.get(4));
		return costBreakUp;
	}

	public Map<String, String> getCostBreakUp() {
		List<String> costs = elUtil.getElementsText(this.costs);
		costBreakUp.put("Sub-Total", costs.get(0));
		costBreakUp.put("Flat Shipping Rate", costs.get(1));
		costBreakUp.put("Total", costs.get(2));
		return costBreakUp;
	}
	
	public CheckoutPage doCheckOut() {
		elUtil.waitForElementVisibility(TimeUtil.DEFAULT_LONG_TIME, checkoutBtn).click();
		return new CheckoutPage(driver);
	}

	public String getFlatShippingRate(String deliveryCountry){
		if(deliveryCountry!=null){
			Map<String, String> map = null;
			if(deliveryCountry.equals(AppConstants.COUNTRY_WITH_TAXES)){
				map = getCostBreakUpForUK();
			}else{
				map = getCostBreakUp();
			}
			return map.get("Flat Shipping Rate");
		}else{
			throw new FrameworkExceptions("Delivery Country is null");
		}
	}

}
