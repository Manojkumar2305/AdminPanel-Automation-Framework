package com.adminpanel.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * FramePage – iFrame interactions on eviltester.
 * Page: /styled/iframes/iframes-test.html
 *
 * eviltester iframe page has:
 *   An iframe containing a simple page with text and inputs
 *   After switching to frame: text elements and buttons are accessible
 */
public class FramePage extends BasePage {

    private final By iframeLocator    = By.cssSelector("iframe, frame");
    private final By frameContent     = By.cssSelector("p, h1, h2, body");
    private final By frameInput       = By.cssSelector("input[type='text'], input:not([type='submit'])");
    private final By frameSubmit      = By.cssSelector("input[type='submit'], button");
    private final By mainPageContent  = By.cssSelector("h1, h2, .main-content, body");

    /** Switch into the first iframe on the page */
    public void switchToFirstFrame() {
        wait.until(org.openqa.selenium.support.ui.ExpectedConditions
                .frameToBeAvailableAndSwitchToIt(0));
    }

    /** Switch into iframe by locator */
    public void switchToFrameByLocator() {
        switchToFrame(iframeLocator);
    }

    /** Get text content from inside the frame */
    public String getFrameText() {
        try { return waitForElement(frameContent).getText().trim(); }
        catch (Exception e) { return driver.findElement(By.tagName("body")).getText().trim(); }
    }

    /** Check frame content is not empty */
    public boolean isFrameContentAccessible() {
        try { return !driver.findElement(By.tagName("body")).getText().trim().isEmpty(); }
        catch (Exception e) { return false; }
    }

    /** Type into input inside the frame */
    public void typeInFrame(String text) {
        try { waitAndType(frameInput, text); }
        catch (Exception ignored) {}
    }

    /** Switch back to main document */
    public void returnToMainContent() {
        switchToDefaultContent();
    }

    /** Verify main page element is accessible after returning from frame */
    public boolean isMainPageAccessible() {
        try { return waitForElement(mainPageContent).isDisplayed(); }
        catch (Exception e) { return !driver.getTitle().isEmpty(); }
    }

    /** Check if page has at least one iframe */
    public boolean hasFrames() {
        return !driver.findElements(iframeLocator).isEmpty();
    }

    public int getFrameCount() {
        return driver.findElements(iframeLocator).size();
    }
}
