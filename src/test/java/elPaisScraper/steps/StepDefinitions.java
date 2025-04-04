package elPaisScraper.steps;

import com.elPaisScraper.api.Translator;
import com.elPaisScraper.driver.DriverManager;
import com.elPaisScraper.model.Article;
import com.elPaisScraper.scraper.ArticleFetcher;
import com.elPaisScraper.util.LanguageVerifier;
import com.elPaisScraper.util.TextAnalyzer;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.List;

import static com.elPaisScraper.util.PrintHelper.printArticlesAsTable;
import static com.elPaisScraper.util.PrintHelper.printTranslatedTitles;
import static com.elPaisScraper.util.SupportingUtilities.acceptCookiesIfPresent;

public class StepDefinitions {

    private static final Logger logger = LoggerFactory.getLogger(StepDefinitions.class);
    private final List<Article> articles = new ArrayList<>();
    private final List<String> translatedTitles = new ArrayList<>();

    private WebDriver driver;

    @Given("I launch the El Pais website")
    public void launchElPais() {
        driver = DriverManager.getDriver();
        driver.get("https://elpais.com/");
        logger.info("Navigated to El Pais homepage.");
        acceptCookiesIfPresent(driver);
    }

    @Given("I verify the El Pais website is in Spanish")
    public void verifyThePageInSpanish() {
        boolean isSpanish = LanguageVerifier.isPageInSpanish(driver);
        logger.info("Language Check: isPageInSpanish = {}", isSpanish);
        Assert.assertTrue(isSpanish, "The website is NOT in Spanish.");
    }

    @When("I navigate to the Opinion section and fetch 5 articles")
    public void fetchArticles() {
        ArticleFetcher fetcher = new ArticleFetcher(driver);
        articles.addAll(fetcher.fetchFirstFiveOpinionArticles());
        printArticlesAsTable(articles);
    }

    @And("I translate article titles")
    public void translateTitles() {
        for (Article article : articles) {
            String translated = Translator.translate(article.title());
            translatedTitles.add(translated);
        }
        printTranslatedTitles(articles, translatedTitles);
    }

    @Then("I analyze repeated words in translated titles")
    public void analyzeTranslatedTitles() {
        logger.info("Analyzing repeated words in translated titles...");
        TextAnalyzer.analyze(translatedTitles);
    }
}
