package elPaisScraper.runner;

import com.elPaisScraper.driver.DriverFactory;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.ITestContext;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;

@CucumberOptions(features = "src/test/resources/features",
        glue = {"elPaisScraper.steps", "elPaisScraper.base"},
        tags = "@ElPaisScraper",
        plugin = {"pretty"})

public class TestRunner extends AbstractTestNGCucumberTests {

    @BeforeMethod(alwaysRun = true)
    @Parameters({"runEnv", "browser", "os", "osVersion", "browserVersion"})
    public void beforeTest(String runEnv, String browser, String os, String osVersion, String browserVersion, ITestContext context) {
        DriverFactory.setXmlTest(context.getCurrentXmlTest());
        System.setProperty("runEnv", runEnv);
        System.setProperty("browser", browser);
        System.setProperty("os", os);
        System.setProperty("osVersion", osVersion);
        System.setProperty("browserVersion", browserVersion);
    }
}
