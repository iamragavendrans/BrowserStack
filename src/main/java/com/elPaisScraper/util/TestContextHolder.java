package com.elPaisScraper.util;

import org.testng.ITestContext;
import org.testng.ITestListener;

public class TestContextHolder implements ITestListener {

    private static final ThreadLocal<ITestContext> contextHolder = new ThreadLocal<>();

    public static ITestContext getContext() {
        return contextHolder.get();
    }

    @Override
    public void onStart(ITestContext context) {
        contextHolder.set(context);
    }

    @Override
    public void onFinish(ITestContext context) {
        contextHolder.remove();
    }
}
