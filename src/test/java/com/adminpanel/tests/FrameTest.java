package com.adminpanel.tests;

import com.adminpanel.pages.FramePage;
import com.adminpanel.utils.DriverManager;
import com.adminpanel.utils.RetryAnalyzer;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Test Module 5 – Frames
 *  TC_FRM_01 : Switch to iframe and interact with element inside it
 *  TC_FRM_02 : Verify text content inside frame is accessible
 *  TC_FRM_03 : Switch back to main content – main page elements accessible
 */
public class FrameTest extends BaseTest {

    private static final String FRAME_URL = com.adminpanel.config.ConfigReader.getInstance().getFrameUrl();

    // ── TC_FRM_01 – switch to frame and interact ──────────────────────────────
    @Test(description = "Switch to iframe and interact with element inside it",
          retryAnalyzer = RetryAnalyzer.class)
    public void testSwitchToFrameAndInteract() {
        DriverManager.getDriver().get(FRAME_URL);
        FramePage framePage = new FramePage();

        Assert.assertTrue(framePage.hasFrames(),
                "Page must have at least one iframe. Found: " + framePage.getFrameCount());

        framePage.switchToFirstFrame();

        Assert.assertTrue(framePage.isFrameContentAccessible(),
                "Content inside iframe must be accessible after switching");

        framePage.returnToMainContent();
    }

    // ── TC_FRM_02 – text content inside frame ────────────────────────────────
    @Test(description = "Text content inside iframe is accessible and correct",
          retryAnalyzer = RetryAnalyzer.class)
    public void testFrameTextContent() {
        DriverManager.getDriver().get(FRAME_URL);
        FramePage framePage = new FramePage();

        Assert.assertTrue(framePage.hasFrames(),
                "Page must have at least one iframe");

        framePage.switchToFirstFrame();

        String frameText = framePage.getFrameText();
        Assert.assertNotNull(frameText,
                "Frame text content must not be null");
        Assert.assertFalse(frameText.isEmpty(),
                "Frame must contain some text content");

        framePage.returnToMainContent();
    }

    // ── TC_FRM_03 – return to main content ───────────────────────────────────
    @Test(description = "Switch back to main content – main page elements accessible",
          retryAnalyzer = RetryAnalyzer.class)
    public void testReturnToMainContent() {
        DriverManager.getDriver().get(FRAME_URL);
        FramePage framePage = new FramePage();

        // Switch into frame first
        framePage.switchToFirstFrame();
        Assert.assertTrue(framePage.isFrameContentAccessible(),
                "Must be able to access frame content");

        // Switch back
        framePage.returnToMainContent();

        // Main page elements must be accessible again
        Assert.assertTrue(framePage.isMainPageAccessible(),
                "Main page elements must be accessible after switching back from iframe");
        Assert.assertFalse(DriverManager.getDriver().getTitle().isEmpty(),
                "Page title must be accessible after returning to main content");
    }
}
