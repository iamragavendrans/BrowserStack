package com.elPaisScraper.driver;

import io.github.cdimascio.dotenv.Dotenv;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class BrowserStackDriverManager {

    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    private static final Logger logger = LoggerFactory.getLogger(BrowserStackDriverManager.class);

    private static final Dotenv dotenv = Dotenv.configure().load();
    private static final String USERNAME = dotenv.get("BROWSERSTACK_USERNAME");
    private static final String ACCESS_KEY = dotenv.get("BROWSERSTACK_ACCESS_KEY");

    private static final String BROWSERSTACK_URL = String.format(
            "https://%s:%s@hub-cloud.browserstack.com/wd/hub",
            USERNAME, ACCESS_KEY
    );

    public static WebDriver createDriver(String os, String osVersion, String browser, String browserVersion) {
        try {
            DesiredCapabilities caps = new DesiredCapabilities();
            caps.setCapability("os", os);
            caps.setCapability("osVersion", osVersion);
            caps.setCapability("browserName", browser);
            caps.setCapability("browserVersion", browserVersion);

            Map<String, Object> browserstackOptions = new HashMap<>();
            browserstackOptions.put("projectName", "ElPais Scraper");
            browserstackOptions.put("buildName", "CrossBrowserTest");
            browserstackOptions.put("sessionName", browser + " on " + os);
            browserstackOptions.put("local", "false");
            browserstackOptions.put("seleniumVersion", "4.15.0");

            caps.setCapability("bstack:options", browserstackOptions);

            WebDriver remoteDriver = new RemoteWebDriver(
                    URI.create(BROWSERSTACK_URL).toURL(),
                    caps
            );

            driver.set(remoteDriver);
            logger.info("BrowserStack WebDriver created successfully for {} on {}", browser, os);

        } catch (Exception e) {
            logger.error("Failed to create BrowserStack driver for {} on {}: {}", browser, os, e.getMessage(), e);
        }

        return driver.get();
    }

    public static WebDriver getDriver() {
        return driver.get();
    }

    public static void quitDriver() {
        if (driver.get() != null) {
            logger.info("Quitting BrowserStack WebDriver session");
            driver.get().quit();
            driver.remove();
        }
    }
}
