package elPaisScraper.base;

import com.elPaisScraper.driver.DriverManager;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Hooks {

    private static final Logger logger = LoggerFactory.getLogger(Hooks.class);

    @Before
    public void setUp() {
        logger.info("Initializing driver...");
        DriverManager.initializeDriver();
    }

    @After
    public void tearDown(Scenario scenario) {
        logger.info("Cleaning up driver for scenario: {}", scenario.getName());
        DriverManager.quitDriver();
    }
}
