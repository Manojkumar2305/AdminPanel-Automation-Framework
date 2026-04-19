package com.adminpanel.listeners;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.adminpanel.utils.ExtentReportManager;
import com.adminpanel.utils.ScreenshotUtil;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener implements ITestListener {

    @Override
    public void onStart(ITestContext ctx) { ExtentReportManager.getExtentReports(); }

    @Override
    public void onTestStart(ITestResult result) {
        ExtentTest test = ExtentReportManager.getExtentReports()
                .createTest(result.getMethod().getMethodName(),
                            result.getMethod().getDescription());
        ExtentReportManager.setTest(test);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ExtentReportManager.getTest().log(Status.PASS, "PASSED");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String path = ScreenshotUtil.capture(result.getMethod().getMethodName());
        ExtentReportManager.getTest().log(Status.FAIL, result.getThrowable().getMessage());
        ExtentReportManager.getTest().fail(
                MediaEntityBuilder.createScreenCaptureFromPath(path).build());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ExtentReportManager.getTest().log(Status.SKIP, "SKIPPED");
    }

    @Override
    public void onFinish(ITestContext ctx) { ExtentReportManager.flush(); }
}
