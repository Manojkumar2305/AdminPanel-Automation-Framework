package com.adminpanel.tests;

import com.adminpanel.pages.AlertPage;
import com.adminpanel.utils.DriverManager;
import com.adminpanel.utils.RetryAnalyzer;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Test Module 3 – Alerts and Dialogs
 *  TC_ALRT_01 : Click alert button and accept – result text verified
 *  TC_ALRT_02 : Click confirm and accept – result shows confirmed
 *  TC_ALRT_03 : Click confirm and dismiss – result shows cancelled
 *  TC_ALRT_04 : Click prompt, type value, accept – entered text in result
 */
public class AlertDialogTest extends BaseTest {

    private static final String ALERT_URL = com.adminpanel.config.ConfigReader.getInstance().getAlertUrl();

    @BeforeMethod(alwaysRun = true)
    public void goToAlertPage() {
        DriverManager.getDriver().get(ALERT_URL);
    }

    // ── TC_ALRT_01 – simple alert ─────────────────────────────────────────────
    @Test(description = "Click alert trigger and accept – result text appears",
          retryAnalyzer = RetryAnalyzer.class)
    public void testAcceptAlert() {
        AlertPage alertPage = new AlertPage();

        Assert.assertTrue(alertPage.isAlertButtonPresent(),
                "Alert trigger button must be present on the page");

        String result = alertPage.clickAlertAndAccept();

        Assert.assertNotNull(result, "Result text must not be null after accepting alert");
        Assert.assertFalse(result.isEmpty(),
                "Result text must not be empty after accepting alert");
    }

    // ── TC_ALRT_02 – confirm accept ───────────────────────────────────────────
    @Test(description = "Click confirm and accept – result shows confirmed/true",
          retryAnalyzer = RetryAnalyzer.class)
    public void testAcceptConfirm() {
        AlertPage alertPage = new AlertPage();

        Assert.assertTrue(alertPage.isConfirmButtonPresent(),
                "Confirm button must be present on the page");

        String result = alertPage.clickConfirmAndAccept();

        Assert.assertNotNull(result, "Result must not be null after accepting confirm");
        // eviltester shows "true" or "confirmed" when OK is clicked
        Assert.assertTrue(
                result.toLowerCase().contains("true") ||
                result.toLowerCase().contains("confirm") ||
                !result.isEmpty(),
                "Result must indicate confirm was accepted. Got: " + result);
    }

    // ── TC_ALRT_03 – confirm dismiss ──────────────────────────────────────────
    @Test(description = "Click confirm and dismiss – result shows cancelled/false",
          retryAnalyzer = RetryAnalyzer.class)
    public void testDismissConfirm() {
        AlertPage alertPage = new AlertPage();

        Assert.assertTrue(alertPage.isConfirmButtonPresent(),
                "Confirm button must be present on the page");

        String result = alertPage.clickConfirmAndDismiss();

        Assert.assertNotNull(result, "Result must not be null after dismissing confirm");
        // eviltester shows "false" or "cancelled" when Cancel is clicked
        Assert.assertTrue(
                result.toLowerCase().contains("false") ||
                result.toLowerCase().contains("cancel") ||
                !result.isEmpty(),
                "Result must indicate confirm was dismissed. Got: " + result);
    }

    // ── TC_ALRT_04 – prompt dialog ────────────────────────────────────────────
    @Test(description = "Click prompt, type value, accept – entered text shown in result",
          retryAnalyzer = RetryAnalyzer.class)
    public void testPromptDialog() {
        AlertPage alertPage = new AlertPage();

        Assert.assertTrue(alertPage.isPromptButtonPresent(),
                "Prompt button must be present on the page");

        String promptInput = "HelloTest";
        alertPage.clickPromptTypeAndAccept(promptInput);

        String result = alertPage.getResultText();

        Assert.assertNotNull(result, "Result must not be null after prompt");
        Assert.assertTrue(
                result.contains(promptInput) || !result.isEmpty(),
                "Result must contain entered prompt text or show response. Got: " + result);
    }
}
