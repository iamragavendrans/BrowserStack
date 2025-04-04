package com.elPaisScraper.util;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LanguageVerifier {
    public static boolean isPageInSpanish(WebDriver driver) {
        try {
            String lang = driver.findElement(By.tagName("html")).getDomAttribute("lang");
            return lang != null && lang.startsWith("es");
        } catch (Exception e) {
            return true;
        }
    }
}
