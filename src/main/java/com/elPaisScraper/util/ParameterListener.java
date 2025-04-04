package com.elPaisScraper.util;

import org.testng.ITestContext;
import org.testng.ITestListener;

public class ParameterListener implements ITestListener {

    @Override
    public void onStart(ITestContext context) {
        // Suite level parameters
        String runEnv = context.getSuite().getParameter("runEnv");
        if(runEnv != null) System.setProperty("runEnv", runEnv);

        // Test level parameters
        String browser = context.getCurrentXmlTest().getParameter("browser");
        String os = context.getCurrentXmlTest().getParameter("os");
        String osVersion = context.getCurrentXmlTest().getParameter("osVersion");
        String browserVersion = context.getCurrentXmlTest().getParameter("browserVersion");

        if(browser != null) System.setProperty("browser", browser);
        if(os != null) System.setProperty("os", os);
        if(osVersion != null) System.setProperty("osVersion", osVersion);
        if(browserVersion != null) System.setProperty("browserVersion", browserVersion);
    }
}
