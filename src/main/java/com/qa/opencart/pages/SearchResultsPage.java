package com.qa.opencart.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.qa.opencart.utils.ElementUtil;
import com.qa.opencart.utils.TimeUtil;

public class SearchResultsPage {
	
	private WebDriver driver;
	private ElementUtil elUtil;

	public SearchResultsPage(WebDriver driver) {
		this.driver = driver;
		elUtil = new ElementUtil(driver);
	}
	
	private By searchProducts = By.cssSelector(".product-thumb");
	
	
	public int getSearchProductsCount() {
		return elUtil.getElements(searchProducts).size();
	}
	
	public ProductPage navigateToProductPage(String productName) {
		elUtil.clickWhenReady(TimeUtil.DEFAULT_MEDIUM_TIME, By.linkText(productName));
		return new ProductPage(driver);
	}
}
