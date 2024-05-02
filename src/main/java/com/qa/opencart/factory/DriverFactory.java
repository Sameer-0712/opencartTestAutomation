package com.qa.opencart.factory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.qa.opencart.exceptions.BrowserExceptions;
import com.qa.opencart.exceptions.FrameworkExceptions;
import com.qa.opencart.logger.Log;

public class DriverFactory {

	WebDriver driver;
	Properties prop;
	OptionsManager optionsManager;
	public static ThreadLocal<WebDriver> tlDriver = new ThreadLocal<WebDriver>();

	public WebDriver initDriver(Properties prop) {

		String browserName = prop.getProperty("browser");

		optionsManager = new OptionsManager(prop);
		switch (browserName.toLowerCase()) {
		case "chrome":
			if (Boolean.parseBoolean(prop.getProperty("remote"))) {
				initRemoteDriver(browserName);
			} else {
				tlDriver.set(new ChromeDriver(optionsManager.getChromeOptions()));
			}

			break;
		case "firefox":
			if (Boolean.parseBoolean(prop.getProperty("remote"))) {
				initRemoteDriver(browserName);
			} else {
				tlDriver.set(new FirefoxDriver(optionsManager.getFirefoxOptions()));
			}
			break;
		case "edge":
			if (Boolean.parseBoolean(prop.getProperty("remote"))) {
				initRemoteDriver(browserName);
			} else {
				tlDriver.set(new EdgeDriver(optionsManager.getEdgeOptions()));
			}
			break;
		default:
			Log.error("Please pass the right browser");
			throw new BrowserExceptions("Incorrect browser name");
		}
		Log.info("Driver initialized");
		getDriver().manage().window().maximize();
		getDriver().manage().deleteAllCookies();
		getDriver().get(prop.getProperty("url"));

		return getDriver();
	}

	private void initRemoteDriver(String browserName) {

		Log.info("Running tests on the remote GRID on browser: " + browserName);
		try {
			switch (browserName.toLowerCase().trim()) {
			case "chrome":
				tlDriver.set(
						new RemoteWebDriver(new URL(prop.getProperty("hubUrl")), optionsManager.getChromeOptions()));
				break;
			case "firefox":
				tlDriver.set(
						new RemoteWebDriver(new URL(prop.getProperty("hubUrl")), optionsManager.getFirefoxOptions()));
				break;
			case "edge":
				tlDriver.set(new RemoteWebDriver(new URL(prop.getProperty("hubUrl")), optionsManager.getEdgeOptions()));
				break;

			default:
				break;
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	public static WebDriver getDriver() {
		return tlDriver.get();
	}

	public Properties initProp() {
		prop = new Properties();
		FileInputStream fis = null;
		String envName = System.getProperty("env");

		try {
			if (envName == null) {
				envName = "qa";
				fis = new FileInputStream("./src/test/resources/config/config.qa.properties");
			} else {
				switch (envName.trim().toLowerCase()) {
				case "qa":
					fis = new FileInputStream("./src/test/resources/config/config.qa.properties");
					break;
				case "stage":
					fis = new FileInputStream("./src/test/resources/config/config.stage.properties");
					break;
				case "dev":
					fis = new FileInputStream("./src/test/resources/config/config.dev.properties");
					break;
				default:
					Log.error("Please pass the right environment");
					throw new FrameworkExceptions("NO SUCH ENVIRONMENT");
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		try {
			prop.load(fis);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Log.info("Running the tests on: " + envName + " environment");
		return prop;
	}
}
