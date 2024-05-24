package com.qa.opencart.utils;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class JavaScriptUtils {

    private WebDriver driver;

    public JavaScriptUtils(WebDriver driver) {
        this.driver = driver;
    }

    public String getTextFromTextNode(WebElement element){
        JavascriptExecutor js = (JavascriptExecutor) driver;
        return (String) js.executeScript("return arguments[0].nextSibling.nodeValue;", element);
    }
}
