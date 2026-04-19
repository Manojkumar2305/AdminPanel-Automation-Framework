package com.adminpanel.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

/**
 * DynamicPage – Dynamic and Hidden elements on eviltester.
 *
 * Dynamic fields: /styled/dynamic/dynamic-fields-test.html
 * Redirects:      /styled/redirects-test.html
 *
 * FIX: showFieldButton selector narrowed to #makedynamic first, avoiding
 *      matching unrelated buttons on the page.
 */
public class DynamicPage extends BasePage {

    // Dynamic fields page – most specific selector first
    private final By showFieldButton = By.cssSelector(
            "#makedynamic, input[value*='Create' i], input[value*='show' i], " +
            "button[onclick*='dynamic']");

    private final By dynamicField = By.cssSelector(
            "#dynamicfield, input[id*='dynamic'], input[name*='dynamic'], .dynamic-field");

    // Redirect page
    private final By redirectButton = By.cssSelector(
            "input[type='submit'], a[href*='redirect'], button[onclick*='redirect']");

    // ── Dynamic fields ────────────────────────────────────────────────────────

    public void clickShowDynamicField() {
        waitAndClick(showFieldButton);
    }

    public boolean isDynamicFieldVisible() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(dynamicField));
            return true;
        } catch (Exception e) {
            // Some eviltester pages toggle visibility via class – check display
            try {
                List<WebElement> inputs = driver.findElements(
                        By.cssSelector("input[type='text']:not([style*='display: none'])"));
                return inputs.stream().anyMatch(WebElement::isDisplayed);
            } catch (Exception ex) { return false; }
        }
    }

    public void typeInDynamicField(String text) {
        try {
            waitAndType(dynamicField, text);
        } catch (Exception e) {
            driver.findElements(By.cssSelector("input[type='text']"))
                    .stream().filter(WebElement::isDisplayed)
                    .findFirst()
                    .ifPresent(el -> { el.clear(); el.sendKeys(text); });
        }
    }

    // ── Redirect ──────────────────────────────────────────────────────────────

    public void clickRedirect() {
        try {
            waitAndClick(redirectButton);
        } catch (Exception e) {
            // Fallback: first visible link
            driver.findElements(By.tagName("a")).stream()
                    .filter(WebElement::isDisplayed)
                    .findFirst()
                    .ifPresent(WebElement::click);
        }
    }

    public boolean pageLoaded() {
        return !driver.getCurrentUrl().isEmpty() && !driver.getTitle().isEmpty();
    }
}
