package com.enterprise.mobile.listeners;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

import com.enterprise.mobile.config.ConfigManager;

/**
 * Retry analyzer for automatically retrying failed tests.
 * Configurable retry count via properties.
 */
public class RetryAnalyzer implements IRetryAnalyzer {

    private static final Logger logger = LogManager.getLogger(RetryAnalyzer.class);
    private int retryCount = 0;

    @Override
    public boolean retry(ITestResult result) {
        int maxRetry = ConfigManager.getInstance().getRetryCount();

        if (retryCount < maxRetry) {
            retryCount++;
            logger.info("Retrying test '{}' - Attempt {}/{}",
                    result.getMethod().getMethodName(), retryCount, maxRetry);
            return true;
        }
        return false;
    }
}
