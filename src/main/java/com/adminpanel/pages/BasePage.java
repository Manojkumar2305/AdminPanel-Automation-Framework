package com.adminpanel.pages;

import com.adminpanel.config.ConfigReader;
import com.adminpanel.utils.DriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * BasePage – shared utilities for all Page classes.
 * Includes switchToAlert() as required by the hackathon spec.
 * Zero Thread.sleep() – only WebDriverWait and FluentWait.
 */
public abstract class BasePage {

    protected WebDriver     driver;
    protected WebDriverWait wait;

    public BasePage() {
        this.driver = DriverManager.getDriver();
        this.wait   = new WebDriverWait(driver,
                Duration.ofSeconds(ConfigReader.getInstance().getTimeout()));
    }

    protected WebElement waitForElement(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected void waitAndClick(By locator) {
        WebElement el = wait.until(ExpectedConditions.elementToBeClickable(locator));
        // Scroll element into view to avoid interception by sticky headers/banners
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block:'center'});", el);
        el.click();
    }

    protected void waitAndType(By locator, String text) {
        WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        el.clear();
        el.sendKeys(text);
    }

    protected void selectByText(By locator, String text) {
        new Select(waitForElement(locator)).selectByVisibleText(text);
    }

    protected void selectByValue(By locator, String value) {
        new Select(waitForElement(locator)).selectByValue(value);
    }

    /** FluentWait – polls every 500ms for slow-loading elements */
    protected WebElement fluentWait(By locator) {
        return new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(ConfigReader.getInstance().getTimeout()))
                .pollingEvery(Duration.ofMillis(500))
                .ignoring(NoSuchElementException.class)
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /** Switch to browser alert and return it */
    protected Alert switchToAlert() {
        wait.until(ExpectedConditions.alertIsPresent());
        return driver.switchTo().alert();
    }

    /** Accept alert and return its text */
    protected String acceptAlert() {
        Alert alert = switchToAlert();
        String text = alert.getText();
        alert.accept();
        return text;
    }

    /** Dismiss alert and return its text */
    protected String dismissAlert() {
        Alert alert = switchToAlert();
        String text = alert.getText();
        alert.dismiss();
        return text;
    }

    /** Type into prompt alert then accept */
    protected String typeAndAcceptPrompt(String input) {
        Alert alert = switchToAlert();
        String text = alert.getText();
        alert.sendKeys(input);
        alert.accept();
        return text;
    }

    /** Switch to iframe by index */
    protected void switchToFrame(int index) {
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(index));
    }

    /** Switch to iframe by locator */
    protected void switchToFrame(By locator) {
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(locator));
    }

    /** Switch back to main document */
    protected void switchToDefaultContent() {
        driver.switchTo().defaultContent();
    }

    protected void jsClick(By locator) {
        WebElement el = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
    }

    protected boolean isElementPresent(By locator) {
        try { return !driver.findElements(locator).isEmpty(); }
        catch (Exception e) { return false; }
    }

    public String getPageTitle()  { return driver.getTitle(); }
    public String getCurrentUrl() { return driver.getCurrentUrl(); }
}
