package com.adminpanel.pages;

import org.openqa.selenium.By;

/**
 * AlertPage – /styled/alerts/alert-test.html
 *
 * Verified eviltester alert page:
 *   Alert button  → id="alertexamples"  (triggers alert)
 *   Confirm button→ id="confirmexample" (triggers confirm)
 *   Prompt button → id="promptexample"  (triggers prompt)
 *   Result text   → id="alertexplanation" or p.explanation
 */
public class AlertPage extends BasePage {

    private final By alertButton   = By.id("alertexamples");
    private final By confirmButton = By.id("confirmexample");
    private final By promptButton  = By.id("promptexample");
    private final By resultText    = By.cssSelector(
            "#alertexplanation, p.explanation, .explanation, #result, p");

    // ── Alert ─────────────────────────────────────────────────────────────────

    /** Click alert button, accept the alert, return result text on page */
    public String clickAlertAndAccept() {
        waitAndClick(alertButton);
        acceptAlert();
        return getResultText();
    }

    // ── Confirm ───────────────────────────────────────────────────────────────

    /** Click confirm, accept it, return result text */
    public String clickConfirmAndAccept() {
        waitAndClick(confirmButton);
        acceptAlert();
        return getResultText();
    }

    /** Click confirm, dismiss it, return result text */
    public String clickConfirmAndDismiss() {
        waitAndClick(confirmButton);
        dismissAlert();
        return getResultText();
    }

    // ── Prompt ────────────────────────────────────────────────────────────────

    /** Click prompt, type input, accept, return result text */
    public String clickPromptTypeAndAccept(String input) {
        waitAndClick(promptButton);
        typeAndAcceptPrompt(input);
        return getResultText();
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    public String getResultText() {
        try { return fluentWait(resultText).getText().trim(); }
        catch (Exception e) { return driver.getPageSource().substring(0, 200); }
    }

    public boolean isAlertButtonPresent()   { return isElementPresent(alertButton); }
    public boolean isConfirmButtonPresent() { return isElementPresent(confirmButton); }
    public boolean isPromptButtonPresent()  { return isElementPresent(promptButton); }
}
