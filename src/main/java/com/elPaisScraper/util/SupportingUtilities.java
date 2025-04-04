package com.elPaisScraper.util;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

public class SupportingUtilities {

    private static final Logger logger = LoggerFactory.getLogger(SupportingUtilities.class);

    public static void clickWithRetry(WebDriver driver, By locator) {
        int attempts = 0;
        while (attempts < 2) {
            try {
                driver.findElement(locator).click();
                break;
            } catch (StaleElementReferenceException e) {
                attempts++;
            }
        }
    }

    public static void acceptCookiesIfPresent(WebDriver driver) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            WebElement acceptButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.id("didomi-notice-agree-button")));
            acceptButton.click();
            logger.info("Cookie consent accepted.");
        } catch (TimeoutException e) {
            logger.info("Cookie consent not shown.");
        }
    }
}
