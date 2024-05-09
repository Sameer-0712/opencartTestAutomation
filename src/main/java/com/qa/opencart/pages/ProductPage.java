package com.qa.opencart.pages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.qa.opencart.utils.TimeUtil;

import io.qameta.allure.Step;

public class ProductPage extends Page{

	private List<WebElement> productInfoElements;
	private List<String> productInfo = new ArrayList<String>();;
	private Map<String,String> productInfoMap = new HashMap<String, String>();

	public ProductPage(WebDriver driver) {
        super(driver);
	}
	
	private By metaData = By.cssSelector("#content ul.list-unstyled li");
	private By productHeader = By.cssSelector("#content h1");
	private By addToCart = By.xpath("//button[text()='Add to Cart']");
	private By addToCartSuccessMsg = By.cssSelector(".alert-success");
	private By shoppingCart = By.cssSelector(".alert-success a:nth-child(2)");
	private By quantityField = By.id("input-quantity");
	
	private List<String> getProductInfoList() {
		productInfoElements = elUtil.getElements(metaData);
		for(WebElement productInfoElement:productInfoElements) {
			productInfo.add(elUtil.getElementText(productInfoElement));
		}
		return productInfo;
	}
	
	public Map<String, String> getProductInfo() {
		for(String info:getProductInfoList()) {
			if(info.contains(":")) {
				String key = info.split(":")[0].trim();
				String value = info.split(":")[1].trim();
				productInfoMap.put(key, value);
			}	
			else {
				productInfoMap.put("Price", info);
			}
		}
		System.out.println("Product Details:-");
		System.out.println(productInfoMap);
		return productInfoMap;
	}
	
	public String getProductHeader() {
		return elUtil.getElementText(productHeader);
	}
	
	public String addToCart(int quantity) {
		if(quantity>1) {
			elUtil.sendKeysToElement(quantityField, String.valueOf(quantity));
		}
		elUtil.clickElement(addToCart);	
		return elUtil.getElementText(elUtil.waitForElementVisibility(TimeUtil.DEFAULT_MEDIUM_TIME, addToCartSuccessMsg));
	}
	
	@Step("Navigate to the cart page")
	public CartPage navigateToCart() {
		elUtil.clickElement(shoppingCart);
		return new CartPage(driver);
	}

}
