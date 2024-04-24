package com.qa.opencart.utils;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.qa.opencart.exceptions.ElementExceptions;
import com.qa.opencart.logger.Log;

public class ElementUtil {

	private WebDriver driver;
	private Actions action;
	private final String DEFAULT_ELEMENT_TIMEOUT_MESSAGE = "Timed Out.. Element is not found";
	private final String DEFAULT_ALERT_TIMEOUT_MESSAGE = "Timed Out.. Alert is not found";

	public ElementUtil(WebDriver driver) {
		this.driver = driver;
	}

	private void nullBlankCheck(String s) {
		if (s == null || s.length() == 0) {
			throw new ElementExceptions("String cannot be null or blank");
		}
	}

	public By getBy(String locatorType, String locatorTypeValue) {

		By locator = null;

		switch (locatorType.trim()) {
		case "id":
			locator = By.id(locatorTypeValue);
			break;
		case "name":
			locator = By.name(locatorTypeValue);
			break;
		case "linkText":
			locator = By.linkText(locatorTypeValue);
			break;
		case "partialLinkText":
			locator = By.partialLinkText(locatorTypeValue);
			break;
		case "tagName":
			locator = By.tagName(locatorTypeValue);
			break;
		case "className":
			locator = By.className(locatorTypeValue);
			break;
		case "xpath":
			locator = By.xpath(locatorTypeValue);
			break;
		case "cssSelector":
			locator = By.cssSelector(locatorTypeValue);
			break;

		default:
			break;
		}

		return locator;
	}

	public WebElement getElement(By locator) {

		WebElement element = null;

		try {
			element = driver.findElement(locator);
		} catch (NoSuchElementException ex) {
			throw new ElementExceptions("Element is not present");
		}
		return element;
	}

	public WebElement getElement(String locatorType, String locatorTypeValue) {
		return getElement(getBy(locatorType, locatorTypeValue));
	}

	public void clickElement(By locator) {
		getElement(locator).click();
	}

	public void sendKeysToElement(By locator, String text) {
		nullBlankCheck(text);
		getElement(locator).clear();
		getElement(locator).sendKeys(text);
	}

	public String getElementAttribute(By locator, String attribute) {
		return getElement(locator).getAttribute(attribute);
	}

	public List<WebElement> getElements(By locator) {
		return driver.findElements(locator);
	}

	public int getElementsCount(By locator) {
		return getElements(locator).size();
	}

	public String getElementText(By locator) {
		return getElement(locator).getText();
	}

	public String getElementText(WebElement element) {
		return element.getText();
	}

	public List<String> getElementsText(By locator) {
		List<WebElement> eleList = getElements(locator);
		List<String> eleTexts = new ArrayList<String>();
		for (WebElement el : eleList) {
			eleTexts.add(el.getText());
		}
		return eleTexts;
	}

	public List<String> getElementsAttribute(By locator, String attribute) {
		List<WebElement> eleList = getElements(locator);
		List<String> eleAttributes = new ArrayList<String>();
		for (WebElement el : eleList) {
			eleAttributes.add(el.getAttribute(attribute));
		}
		return eleAttributes;
	}

	public boolean isElementExist(By locator) {
		if (getElements(locator).size() == 1) {
			return true;
		}
		return false;
	}

	public boolean multipleElementsExist(By locator) {
		if (getElements(locator).size() > 0) {
			return true;
		}
		return false;
	}

	public boolean isElementSelected(By locator) {
		return getElement(locator).isSelected();
	}

	// ********************************Select based Dropdown
	// Utilities********************************//

	public void doSelectByIndex(By locator, int index) {
		Select select = new Select(getElement(locator));
		select.selectByIndex(index);
	}

	public void doSelectByValue(By locator, String value) {
		nullBlankCheck(value);
		Select select = new Select(getElement(locator));
		select.selectByValue(value);
	}

	public void doSelectByVisibleText(By locator, String text) {
		nullBlankCheck(text);
		Select select = new Select(getElement(locator));
		select.selectByVisibleText(text);
	}

	public void doSelectByPartialText(By locator, String text) {
		nullBlankCheck(text);
		List<WebElement> dropdownOptions = getDropdownOptions(locator);
		for (WebElement e : dropdownOptions) {
			if (e.getText().contains(text)) {
				e.click();
				break;
			}
		}
	}

	public List<WebElement> getDropdownOptions(By locator) {
		Select select = new Select(getElement(locator));
		return select.getOptions();
	}

	public List<String> getDropdownOptionsText(By locator) {
		List<WebElement> options = getDropdownOptions(locator);
		List<String> optionsText = new ArrayList<String>();
		for (WebElement e : options) {
			optionsText.add(e.getText());
		}
		return optionsText;
	}

	/**
	 * Selects an option from a dynamic dropdown
	 * 
	 * @param locator
	 * @param optionsLocator
	 * @param searchKey
	 * @param searchValue
	 * @throws InterruptedException
	 */
	public void doSearch(By locator, By optionsLocator, String searchKey, String searchValue)
			throws InterruptedException {
		getElement(locator).sendKeys(searchKey);
		Thread.sleep(3000);

		List<WebElement> options = getElements(optionsLocator);

		for (WebElement option : options) {
			if (option.getText().contains(searchValue)) {
				option.click();
				break;
			}
		}
	}

	/**
	 * Selects the multiple options from a multi-select dropdown
	 * 
	 * @param dropdownLocator
	 * @param optionsLocator
	 * @param choices
	 * @throws InterruptedException
	 */

	public void selectChoices(By dropdownLocator, By optionsLocator, String... choices) throws InterruptedException {

		getElement(dropdownLocator).click();
		Thread.sleep(2000);
		List<WebElement> options = getElements(optionsLocator);
		if (choices[0].equals("all")) {
			for (WebElement option : options) {
				try {
					option.click();
				} catch (ElementNotInteractableException ex) {
					break;
				}
			}
		} else {
			for (WebElement option : options) {
				for (String choice : choices) {
					if (option.getText().equals(choice)) {
						option.click();
					}
				}
			}
		}
	}

	// ********************************Actions
	// Utilities********************************//

	public void hoverElement(By locator) {
		action = new Actions(driver);
		action.moveToElement(getElement(locator)).perform();
		;
	}

	public void doActionsClick(By locator) {
		action = new Actions(driver);
		action.click(getElement(locator)).perform();
		;
	}

	public void doActionsSendKeys(By locator, String text) {
		action = new Actions(driver);
		action.sendKeys(getElement(locator), text).perform();
		;
	}

	public void handleMenuSubMenuLevel2(By parentMenu, By childMenu) throws InterruptedException {
		hoverElement(parentMenu);
		Thread.sleep(2000);
		clickElement(childMenu);
	}

	public void handleMenuSubMenuLevel4(By level1Menu, By level2Menu, By level3Menu, By level4Menu)
			throws InterruptedException {
		clickElement(level1Menu);
		Thread.sleep(2000);
		hoverElement(level2Menu);
		Thread.sleep(2000);
		hoverElement(level3Menu);
		Thread.sleep(2000);
		clickElement(level4Menu);
	}

	// ********************************Wait
	// Utilities********************************//

	public Alert waitForJSAlert(int timeOut) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
		return wait.until(ExpectedConditions.alertIsPresent());
	}

	public String waitForPageTitleIs(int timeOut, String title) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));

		try {
			if (wait.until(ExpectedConditions.titleIs(title))) {
				return driver.getTitle();
			}
		} catch (Exception e) {
			Log.error("Title is not found within " + timeOut + " seconds",e);
		}

		return null;
	}

	public String waitForPageTitleContains(int timeOut, String partialText) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
		try {
			if (wait.until(ExpectedConditions.titleContains(partialText))) {
				return driver.getTitle();
			}
		} catch (Exception e) {
			Log.error("Title is not found within " + timeOut + " seconds",e);
		}
		return null;
	}

	public String waitForPageURLIs(int timeOut, String url) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
		try {
			if (wait.until(ExpectedConditions.urlToBe(url))) {
				return driver.getCurrentUrl();
			}
		} catch (Exception e) {
			Log.error("URL is not found within " + timeOut + " seconds",e);
		}
		return null;
	}

	public String waitForPageURLContains(int timeOut, String partialUrl) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
		try {
			if (wait.until(ExpectedConditions.urlContains(partialUrl))) {
				return driver.getCurrentUrl();
			}
		} catch (Exception e) {
			Log.error("URL fraction is not found within " + timeOut + " seconds",e);
		}
		return null;
	}

	public boolean waitForWindows(int timeOut, int numberOfWindows) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
		return wait.until(ExpectedConditions.numberOfWindowsToBe(numberOfWindows));
	}

	public WebElement waitForElementPresence(int timeOut, By locator) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
		return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
	}

	public WebElement waitForElementVisibility(int timeOut, By locator) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
		return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
	}

	public Boolean waitForElementAttributeToContain(int timeOut, By locator, String attribute, String value) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
		return wait.until(ExpectedConditions.attributeContains(locator, attribute, value));
	}

	public void waitForFrame(int timeOut, By frameLocator) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
		wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameLocator));
	}

	public void clickWhenReady(int timeOut, By locator) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOut));
		wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
	}

	public List<WebElement> waitForElementsPresenceWithFluentWait(int timeOut, int pollingTime, By locator) {
		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(Duration.ofSeconds(timeOut))
				.pollingEvery(Duration.ofMillis(pollingTime)).ignoring(NoSuchElementException.class)
				.withMessage(DEFAULT_ELEMENT_TIMEOUT_MESSAGE);

		return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
	}
	
	public List<WebElement> waitForElementsVisibilityWithFluentWait(int timeOut, int pollingTime, By locator) {
		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(Duration.ofSeconds(timeOut))
				.pollingEvery(Duration.ofMillis(pollingTime)).ignoring(NoSuchElementException.class)
				.withMessage(DEFAULT_ELEMENT_TIMEOUT_MESSAGE);

		return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
	}

	public Alert waitForJSAlertWithFluentWait(int timeOut, int pollingTime) {
		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(Duration.ofSeconds(timeOut))
				.pollingEvery(Duration.ofMillis(pollingTime)).ignoring(NoAlertPresentException.class)
				.withMessage(DEFAULT_ALERT_TIMEOUT_MESSAGE);

		return wait.until(ExpectedConditions.alertIsPresent());
	}

	// ********************************Alert
	// Utilities********************************//

	public String getAlertText(int timeOut) {
		return waitForJSAlert(timeOut).getText();
	}

	public void acceptAlert(int timeOut) {
		waitForJSAlert(timeOut).accept();
	}

	public void dismissAlert(int timeOut) {
		waitForJSAlert(timeOut).dismiss();
	}

	public void sendKeysToAlert(int timeOut, String text) {
		waitForJSAlert(timeOut).sendKeys(text);
	}

	// ********************************Custom Wait
	// Utilities********************************//

	public WebElement retryElementWithAttempts(By locator, int maxAttempts) {

		WebElement element = null;
		int attempt = 1;
		while (attempt <= maxAttempts) {
			try {
				element = getElement(locator);
				Log.error("Element found in attempt: " + attempt);
				break;
			} catch (Exception e) {
				Log.error("Element not found: Attempt: " + attempt,e);
				try {
					Thread.sleep(500);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			attempt++;
		}

		if (element == null) {
			Log.error("Element does not exist...");
			throw new ElementExceptions("Element does not exist");
		}
		return element;
	}

	public WebElement retryElementWithAttempts(By locator, int maxAttempts, int timeOut) {

		WebElement element = null;
		int attempt = 1;
		while (attempt <= maxAttempts) {
			try {
				element = getElement(locator);
				Log.error("Element found in attempt: " + attempt);
				break;
			} catch (Exception e) {
				Log.error("Element not found: Attempt: " + attempt,e);
				try {
					Thread.sleep(timeOut);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			attempt++;
		}

		if (element == null) {
			Log.error("Element does not exist...");
			throw new ElementExceptions("Element does not exist");
		}
		return element;
	}

	public WebElement retryElementWithTimeOut(By locator, int maxTimeOut) {

		WebElement element = null;
		int timeOut = 500;
		int counter = 1;
		while (maxTimeOut != 0) {
			try {
				element = getElement(locator);
				Log.error("Element found in " + timeOut * counter + " ms");
			} catch (NoSuchElementException e) {
				Log.error("Element not found in " + timeOut * counter + " ms",e);
				try {
					Thread.sleep(timeOut);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			counter++;
			maxTimeOut = maxTimeOut - timeOut;
		}
		if (element == null) {
			Log.error("Element does not exist...");
			throw new ElementExceptions("Element does not exist");
		}
		return element;
	}
}
