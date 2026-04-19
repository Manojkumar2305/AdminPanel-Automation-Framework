package com.adminpanel.tests;

import com.adminpanel.pages.DynamicPage;
import com.adminpanel.utils.DriverManager;
import com.adminpanel.utils.RetryAnalyzer;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;

/**
 * Test Module 4 – Dynamic Elements
 *  TC_DYN_01 : Hidden/dynamic field becomes visible after trigger click
 *  TC_DYN_02 : Interact with dynamically appearing element and verify result
 *  TC_DYN_03 : Redirect page lands on expected destination URL
 */
public class DynamicElementTest extends BaseTest {

    private static final String DYNAMIC_URL = com.adminpanel.config.ConfigReader.getInstance().getDynamicUrl();
    private static final String REDIRECT_URL = com.adminpanel.config.ConfigReader.getInstance().getRedirectUrl();

    // ── TC_DYN_01 – dynamic field appears after trigger ───────────────────────
    @Test(description = "Hidden/dynamic field becomes visible after clicking trigger",
          retryAnalyzer = RetryAnalyzer.class)
    public void testDynamicFieldAppearsAfterTrigger() {
        DriverManager.getDriver().get(DYNAMIC_URL);
        DynamicPage dynamicPage = new DynamicPage();

        dynamicPage.clickShowDynamicField();

        Assert.assertTrue(dynamicPage.isDynamicFieldVisible(),
                "Dynamic field must become visible after clicking the trigger button");
    }

    // ── TC_DYN_02 – interact with dynamic element ─────────────────────────────
    @Test(description = "Interact with dynamically appearing element – action completes",
          retryAnalyzer = RetryAnalyzer.class)
    public void testInteractWithDynamicElement() {
        DriverManager.getDriver().get(DYNAMIC_URL);
        DynamicPage dynamicPage = new DynamicPage();

        dynamicPage.clickShowDynamicField();
        dynamicPage.typeInDynamicField("AutoTest");

        // Verify page is still responsive after interaction
        Assert.assertTrue(dynamicPage.pageLoaded(),
                "Page must remain loaded after interacting with dynamic element");
        Assert.assertTrue(DriverManager.getDriver().getCurrentUrl().contains("eviltester"),
                "Must still be on eviltester after dynamic interaction");
    }

    // ── TC_DYN_03 – redirect lands on correct URL ─────────────────────────────
    @Test(description = "Redirect page correctly lands on expected destination URL",
          retryAnalyzer = RetryAnalyzer.class)
    public void testRedirectPage() {
        DriverManager.getDriver().get(REDIRECT_URL);
        DynamicPage dynamicPage = new DynamicPage();

        String urlBefore = DriverManager.getDriver().getCurrentUrl();
        Assert.assertTrue(urlBefore.contains("redirect"),
                "Must start on redirect page. URL: " + urlBefore);

        dynamicPage.clickRedirect();

        // Wait for URL to change
        new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(10))
                .until(ExpectedConditions.not(
                        ExpectedConditions.urlToBe(urlBefore)));

        String urlAfter = DriverManager.getDriver().getCurrentUrl();
        Assert.assertTrue(urlAfter.contains("eviltester"),
                "After redirect must still be on eviltester site. URL: " + urlAfter);
        Assert.assertNotEquals(urlAfter, urlBefore,
                "URL must change after clicking redirect");
    }
}
