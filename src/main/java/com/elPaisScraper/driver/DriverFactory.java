package com.elPaisScraper.driver;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.github.cdimascio.dotenv.Dotenv;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.xml.XmlTest;

public class DriverFactory {

    private static final Logger logger = LoggerFactory.getLogger(DriverFactory.class);
    private static final Dotenv dotenv = Dotenv.load();
    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    private static final ThreadLocal<XmlTest> xmlTestThreadLocal = new ThreadLocal<>();

    public static XmlTest getXmlTest() {
        return xmlTestThreadLocal.get();
    }

    public static void setXmlTest(XmlTest xmlTest) {
        xmlTestThreadLocal.set(xmlTest);
    }

    public static void removeXmlTest() {
        xmlTestThreadLocal.remove();
    }

    public static WebDriver getDriver() {
        return driver.get();
    }

    public static void initializeDriver(String runEnv, String browserName, String os, String osVersion, String browserVersion) {
        try {
            BrowserType browser = BrowserType.valueOf(browserName.toUpperCase());

            if (runEnv.equalsIgnoreCase("local")) {
                driver.set(createLocalDriver(browser));
                logger.info("Initialized Local WebDriver: {}", browserName);
            } else if (runEnv.equalsIgnoreCase("browserstack")) {
                driver.set(createRemoteDriver(browser, os, osVersion, browserVersion));
                logger.info("Initialized BrowserStack WebDriver: {} on {} {}", browserName, os, osVersion);
            } else {
                throw new IllegalArgumentException("Unsupported run environment: " + runEnv);
            }

        } catch (Exception e) {
            logger.error("Failed to initialize driver: {}", e.getMessage(), e);
            throw new RuntimeException("Driver initialization failed", e);
        }
    }

    public static void quitDriver() {
        if (driver.get() != null) {
            logger.info("Quitting WebDriver session");
            driver.get().quit();
            driver.remove();
        }
    }

    private static WebDriver createLocalDriver(BrowserType browser) {
        switch (browser) {
            case CHROME:
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("--start-maximized", "--disable-extensions");
                return new ChromeDriver(chromeOptions);

            case EDGE:
                WebDriverManager.edgedriver().setup();
                EdgeOptions edgeOptions = new EdgeOptions();
                edgeOptions.addArguments("--start-maximized", "--disable-extensions");
                return new EdgeDriver(edgeOptions);

            default:
                throw new IllegalArgumentException("Unsupported local browser: " + browser);
        }
    }

    private static WebDriver createRemoteDriver(BrowserType browser, String os, String osVersion, String browserVersion) {
        // Uses optimized BrowserStackDriverManager with expanded parameters for cross-browser/platform support
        return BrowserStackDriverManager.createDriver(os, osVersion, browser.name().toLowerCase(), browserVersion);
    }
}
