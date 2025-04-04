package com.elPaisScraper.scraper;

import com.elPaisScraper.model.Article;
import com.elPaisScraper.util.ImageDownloader;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static com.elPaisScraper.util.SupportingUtilities.clickWithRetry;

public class ArticleFetcher {

    private static final Logger logger = LoggerFactory.getLogger(ArticleFetcher.class);
    private final WebDriver driver;

    public ArticleFetcher(WebDriver driver) {
        this.driver = driver;
    }

    public List<Article> fetchFirstFiveOpinionArticles() {
        List<Article> articles = new ArrayList<>();
        navigateToOpinionSection();

        List<WebElement> articleElements = getArticleElements();
        int count = 0;

        for (WebElement element : articleElements) {
            Article article = extractArticle(element);
            if (article != null) {
                articles.add(article);
                count++;
            }
            if (count >= 5) break;
        }

        return articles;
    }

    private void navigateToOpinionSection() {
        clickWithRetry(driver, By.linkText("Opinión"));
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.warn("Thread interrupted while waiting for Opinion page to load.");
        }
    }

    private List<WebElement> getArticleElements() {
        return driver.findElements(By.cssSelector("article"));
    }

    private Article extractArticle(WebElement articleElement) {
        try {
            String title = articleElement.findElement(By.cssSelector("h2 a")).getText();
            String content = articleElement.findElement(By.cssSelector("p")).getText();
            String imageUrl = downloadImageIfAvailable(articleElement, title);

            return new Article(title, content, imageUrl);

        } catch (NoSuchElementException e) {
            logger.warn("Skipping article due to missing title or content.");
            return null;
        }
    }

    private String downloadImageIfAvailable(WebElement articleElement, String title) {
        try {
            WebElement img = articleElement.findElement(By.cssSelector("img"));
            String imageUrl = img.getDomAttribute("src");

            if (imageUrl != null && !imageUrl.isEmpty()) {
                String safeTitle = title.replaceAll("[^a-zA-Z0-9\\-_]", "_");
                ImageDownloader.download(imageUrl, safeTitle);
                logger.info("Image downloaded for article: {}", title);
                return imageUrl;
            } else {
                logger.warn("Image URL empty for article: {}", title);
            }

        } catch (NoSuchElementException e) {
            logger.warn("No <img> tag found for article: {}", title);
        } catch (Exception e) {
            logger.error("Failed to download image for article: {}", title, e);
        }

        return null;
    }
}
