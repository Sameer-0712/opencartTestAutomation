package com.qa.opencart.pages;

import com.qa.opencart.utils.ElementUtil;
import com.qa.opencart.utils.TimeUtil;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * A generic Page class which will have common functionalities across all the pages
 */
public class Page {

    protected WebDriver driver;
    protected ElementUtil elUtil;

    public Page(WebDriver driver){
        this.driver = driver;
        elUtil = new ElementUtil(driver);
    }

    private By searchField = By.name("search");
    private By searchIcon = By.cssSelector(".input-group-btn");
    private By cartButton = By.cssSelector("#cart>button");
    private By removeFromCart = By.xpath("//button[@title='Remove']");

    @Step("Search with {0}")
    public SearchResultsPage doSearch(String searchKey) {
        elUtil.sendKeysToElement(searchField, searchKey);
        elUtil.clickElement(searchIcon);
        return new SearchResultsPage(driver);
    }

    public void doEmptyCart(){

        while(elUtil.multipleElementsExist(removeFromCart)){
            elUtil.clickWhenReady(TimeUtil.DEFAULT_MEDIUM_TIME,cartButton);
            elUtil.waitForElementAttributeToContain(TimeUtil.DEFAULT_MEDIUM_TIME, cartButton, "aria-expanded", "true");
            if(elUtil.multipleElementsExist(removeFromCart)){
                elUtil.clickElement(removeFromCart);
            }
        }
        elUtil.clickWhenReady(TimeUtil.DEFAULT_MEDIUM_TIME,cartButton);
    }

}
