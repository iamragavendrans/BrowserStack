package com.elPaisScraper.driver;

import com.elPaisScraper.driver.BrowserType;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.github.cdimascio.dotenv.Dotenv;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class DriverFactory {

    private static final Logger logger = LoggerFactory.getLogger(DriverFactory.class);
    private static final Dotenv dotenv = Dotenv.load();
    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    public static WebDriver getDriver() {
        return driver.get();
    }

    public static void initializeDriver(String runEnv, String browserName) {
        try {
            BrowserType browser = BrowserType.valueOf(browserName.toUpperCase());

            if (runEnv.equalsIgnoreCase("local")) {
                driver.set(createLocalDriver(browser));
            } else if (runEnv.equalsIgnoreCase("browserstack")) {
                driver.set(createRemoteDriver(browser));
            } else {
                throw new IllegalArgumentException("Unsupported runEnv: " + runEnv);
            }

        } catch (Exception e) {
            logger.error("Failed to initialize driver: {}", e.getMessage(), e);
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
                chromeOptions.addArguments("--start-maximized");
                return new ChromeDriver(chromeOptions);

            case EDGE:
                WebDriverManager.edgedriver().setup();
                EdgeOptions edgeOptions = new EdgeOptions();
                edgeOptions.addArguments("--start-maximized");
                return new EdgeDriver(edgeOptions);

            default:
                throw new IllegalArgumentException("Unsupported browser for local: " + browser);
        }
    }

    private static WebDriver createRemoteDriver(BrowserType browser) {
        String username = dotenv.get("BROWSERSTACK_USERNAME");
        String accessKey = dotenv.get("BROWSERSTACK_ACCESS_KEY");

        String remoteUrl = String.format("https://%s:%s@hub-cloud.browserstack.com/wd/hub", username, accessKey);

        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("browserName", browser.name().toLowerCase());
        caps.setCapability("browserVersion", "latest");

        Map<String, Object> options = new HashMap<>();
        options.put("os", "Windows");
        options.put("osVersion", "11");
        options.put("projectName", "ElPais Scraper");
        options.put("buildName", "CrossBrowser Test");
        options.put("sessionName", browser.name() + " test");
        options.put("seleniumVersion", "4.15.0");

        caps.setCapability("bstack:options", options);

        try {
            return new RemoteWebDriver(URI.create(remoteUrl).toURL(), caps);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create remote driver: " + e.getMessage(), e);
        }
    }
}
