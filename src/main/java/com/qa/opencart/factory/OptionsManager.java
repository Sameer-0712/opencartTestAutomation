package com.qa.opencart.factory;

import java.util.Properties;

import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;

import com.qa.opencart.logger.Log;

public class OptionsManager {
	
	private Properties prop;
	private ChromeOptions co;
	private EdgeOptions eo;
	
	public OptionsManager(Properties prop) {
		this.prop = prop;
	}
	
	public ChromeOptions getChromeOptions() {
		co = new ChromeOptions();
		if(Boolean.parseBoolean(prop.getProperty("headless"))){
			Log.info("Running tests on chrome in headless mode");
			co.addArguments("--headless");
		}
		return co;
	}
	
	public EdgeOptions getEdgeOptions() {
		eo = new EdgeOptions();
		if(Boolean.parseBoolean(prop.getProperty("headless"))){
			Log.info("Running tests on edge in headless mode");
			eo.addArguments("--headless");
		}
		return eo;
	}
}
