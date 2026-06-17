package com.enterprise.mobile.reporting;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.enterprise.mobile.config.ConfigManager;
import com.enterprise.mobile.config.FrameworkConstants;

/**
 * Extent Report manager. Singleton that creates and manages the report
 * instance.
 */
public final class ExtentReportManager {

    private static ExtentReports extent;
    private static final String REPORT_DIR = FrameworkConstants.REPORT_PATH;

    private ExtentReportManager() {
    }

    public static ExtentReports getInstance() {
        if (extent == null) {
            synchronized (ExtentReportManager.class) {
                if (extent == null) {
                    extent = createInstance();
                }
            }
        }
        return extent;
    }

    private static ExtentReports createInstance() {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
        String reportPath = REPORT_DIR + "TestReport_" + timestamp + ".html";

        new File(REPORT_DIR).mkdirs();

        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
        sparkReporter.config().setDocumentTitle(FrameworkConstants.EXTENT_REPORT_TITLE);
        sparkReporter.config().setReportName(FrameworkConstants.EXTENT_REPORT_NAME);
        sparkReporter.config().setTheme(Theme.DARK);
        sparkReporter.config().setEncoding("UTF-8");
        sparkReporter.config().setTimeStampFormat("yyyy-MM-dd HH:mm:ss");

        ExtentReports extentReports = new ExtentReports();
        extentReports.attachReporter(sparkReporter);

        // System info
        ConfigManager config = ConfigManager.getInstance();
        extentReports.setSystemInfo("Platform", config.getPlatform().name());
        extentReports.setSystemInfo("Environment", config.getEnvironment().name());
        extentReports.setSystemInfo("Execution Mode", config.getExecutionMode().name());
        extentReports.setSystemInfo("Java Version", System.getProperty("java.version"));
        extentReports.setSystemInfo("OS", System.getProperty("os.name"));

        return extentReports;
    }

    public static void flush() {
        if (extent != null) {
            extent.flush();
        }
    }

    public static void reset() {
        extent = null;
    }
}
