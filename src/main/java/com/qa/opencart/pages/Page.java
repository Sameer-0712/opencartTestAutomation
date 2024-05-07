package com.qa.opencart.pages;

import com.qa.opencart.utils.ElementUtil;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class Page {

    private WebDriver driver;
    private ElementUtil elUtil;

    public Page(WebDriver driver){
        this.driver = driver;
        elUtil = new ElementUtil(driver);
    }

    private By searchField = By.name("search");
    private By searchIcon = By.cssSelector(".input-group-btn");

    @Step("Search with {0}")
    public SearchResultsPage doSearch(String searchKey) {
        elUtil.sendKeysToElement(searchField, searchKey);
        elUtil.clickElement(searchIcon);
        return new SearchResultsPage(driver);
    }

}
