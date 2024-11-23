package com.qa.opencart.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.qa.opencart.utils.TimeUtil;

import java.util.List;

public class SearchResultsPage extends Page {

	public SearchResultsPage(WebDriver driver) {
        super(driver);
	}
	
	private By searchProducts = By.cssSelector(".product-thumb h4 a");

	private By noResults = By.xpath("//div[@id='content']/p[2]");

	public int getSearchProductsCount() {
		return elUtil.getElements(searchProducts).size();
	}

	public List<String> getSearchProductsList(){
		return elUtil.getElementsText(searchProducts);
	}

	public String getNoResultsText(){
		return elUtil.getElementText(noResults);
	}
	
	public ProductPage navigateToProductPage(String productName) {
		elUtil.clickWhenReady(TimeUtil.DEFAULT_MEDIUM_TIME, By.linkText(productName));
		return new ProductPage(driver);
	}
}
