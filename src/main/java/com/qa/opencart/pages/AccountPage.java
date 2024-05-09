package com.qa.opencart.pages;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.qa.opencart.constants.AppConstants;
import com.qa.opencart.utils.TimeUtil;

public class AccountPage extends Page {

	public AccountPage(WebDriver driver) {
		super(driver);
	}

	private By logoutLink = By.linkText("Logout");
	private By headers = By.cssSelector("div#content h2");


	public List<String> getHeaders() {
		List<WebElement> headersElements = elUtil.getElements(headers);
		List<String> headersText = new ArrayList<String>();
		for (WebElement e : headersElements) {
			headersText.add(e.getText());
		}
		return headersText;
	}

	public boolean checkLogoutLink() {
		return elUtil.isElementExist(logoutLink);
	}

	public String getAccountPageTitle() {
		return elUtil.waitForPageTitleIs(TimeUtil.DEFAULT_MEDIUM_TIME, AppConstants.ACCOUNTS_PAGE_TITLE);
	}

	public String getAccountPageURL() {
		return elUtil.waitForPageURLContains(TimeUtil.DEFAULT_MEDIUM_TIME, AppConstants.ACC_PAGE_URL_FRACTION);
	}

}
