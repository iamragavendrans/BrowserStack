package elPaisScraper.base;

import com.elPaisScraper.driver.DriverFactory;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.testng.ITestContext;
import org.testng.xml.XmlTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Hooks {
    private static final Logger logger = LoggerFactory.getLogger(Hooks.class);

    @Before
    public void setUp(Scenario scenario) {
        XmlTest xmlTest = DriverFactory.getXmlTest(); // See below how we store this
        String runEnv = xmlTest.getParameter("runEnv");
        String browser = xmlTest.getParameter("browser");
        String os = xmlTest.getParameter("os");
        String osVersion = xmlTest.getParameter("osVersion");
        String browserVersion = xmlTest.getParameter("browserVersion");

        logger.info("Initializing driver for scenario '{}' on '{}' '{}' with '{}' '{}'",
                scenario.getName(), os, osVersion, browser, browserVersion);

        DriverFactory.initializeDriver(runEnv, browser, os, osVersion, browserVersion);
    }

    @After
    public void tearDown(Scenario scenario) {
        logger.info("Cleaning up driver for scenario: {}", scenario.getName());
        DriverFactory.quitDriver();
        DriverFactory.removeXmlTest();
    }

}
