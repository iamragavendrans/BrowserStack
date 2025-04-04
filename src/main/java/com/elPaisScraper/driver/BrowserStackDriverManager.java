package com.elPaisScraper.driver;

import io.github.cdimascio.dotenv.Dotenv;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

public class BrowserStackDriverManager {

    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    private static final Logger logger = LoggerFactory.getLogger(BrowserStackDriverManager.class);
    private static final Dotenv dotenv = Dotenv.configure().load();

    public static WebDriver createDriver(String os, String osVersion, String browser, String browserVersion) {
        try {
            Dotenv dotenv = Dotenv.load();
            String user = dotenv.get("BROWSERSTACK_USERNAME");
            String key = dotenv.get("BROWSERSTACK_ACCESS_KEY");
            String URL = "https://" + user + ":" + key + "@hub-cloud.browserstack.com/wd/hub";

            MutableCapabilities capabilities = new MutableCapabilities();
            capabilities.setCapability("browserName", browser);
            capabilities.setCapability("browserVersion", browserVersion);

            // Correct W3C syntax for BrowserStack options
            Map<String, Object> bstackOptions = new HashMap<>();
            bstackOptions.put("os", os);
            bstackOptions.put("osVersion", osVersion);
            bstackOptions.put("projectName", "ElPais Scraper");
            bstackOptions.put("buildName", "CrossBrowserTest");
            bstackOptions.put("sessionName", browser + " on " + os);
            bstackOptions.put("seleniumVersion", "4.15.0");
            bstackOptions.put("debug", true);
            bstackOptions.put("local", false);

            capabilities.setCapability("bstack:options", bstackOptions);

            return new RemoteWebDriver(new URL(URL), capabilities);
        } catch (Exception e) {
            logger.error("Error initializing BrowserStack WebDriver: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to initialize BrowserStack driver", e);
        }
    }


    public static WebDriver getDriver() {
        return driver.get();
    }

    public static void quitDriver() {
        if (driver.get() != null) {
            logger.info("Quitting BrowserStack WebDriver session.");
            driver.get().quit();
            driver.remove();
        }
    }
}
