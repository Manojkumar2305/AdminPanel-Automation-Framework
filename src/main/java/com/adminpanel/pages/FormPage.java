package com.adminpanel.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * FormPage – Basic HTML Form at /styled/basic-html-form-test.html
 *
 * Verified locators from testpages.eviltester.com:
 *   username   → input[name='username']
 *   password   → input[name='password']
 *   comments   → textarea[name='comments']
 *   dropdown   → select[name='dropdown']
 *   radio btns → input[name='radioval']
 *   checkboxes → input[name='checkboxes[]']
 *   submit     → input[type='submit']
 *   result     → #_valuesdisplayed, .resultblock, table.results
 */
public class FormPage extends BasePage {

    // Form field locators
    private final By usernameField  = By.name("username");
    private final By passwordField  = By.name("password");
    private final By commentsField  = By.name("comments");
    private final By dropdown       = By.name("dropdown");
    private final By radioButtons   = By.name("radioval");
    private final By checkboxes     = By.cssSelector("input[name='checkboxes[]'], input[type='checkbox']");
    private final By submitButton   = By.cssSelector("input[type='submit']");

    // Result display after form submission
    private final By resultSection  = By.cssSelector("#_valuesdisplayed, .resultblock, table, .results");
    private final By resultRows     = By.cssSelector("tr, .result-row");

    public void enterUsername(String username) { waitAndType(usernameField, username); }
    public void enterPassword(String password) { waitAndType(passwordField, password); }
    public void enterComments(String comments) { waitAndType(commentsField, comments); }

    public void selectDropdownOption(String option) {
        selectByText(dropdown, option);
    }

    public void selectDropdownByValue(String value) {
        selectByValue(dropdown, value);
    }

    public void selectRadioButton(String value) {
        for (WebElement radio : driver.findElements(radioButtons)) {
            if (radio.getAttribute("value").equalsIgnoreCase(value)) {
                if (!radio.isSelected()) radio.click();
                return;
            }
        }
    }

    public void selectFirstRadioButton() {
        java.util.List<WebElement> radios = driver.findElements(radioButtons);
        if (!radios.isEmpty() && !radios.get(0).isSelected()) radios.get(0).click();
    }

    public void checkFirstCheckbox() {
        java.util.List<WebElement> boxes = driver.findElements(checkboxes);
        if (!boxes.isEmpty() && !boxes.get(0).isSelected()) boxes.get(0).click();
    }

    /**
     * Click the submit button.
     * Falls back to JavaScript click if the button is intercepted by an
     * overlapping element (e.g. sticky banners), which caused
     * ElementClickInterceptedException in the original implementation.
     */
    public void clickSubmit() {
        try {
            waitAndClick(submitButton);
        } catch (org.openqa.selenium.ElementClickInterceptedException e) {
            jsClick(submitButton);
        }
    }

    public void fillAndSubmitForm(String username, String password, String comments) {
        enterUsername(username);
        enterPassword(password);
        enterComments(comments);
        clickSubmit();
    }

    public boolean isResultDisplayed() {
        try { return fluentWait(resultSection).isDisplayed(); }
        catch (Exception e) { return false; }
    }

    public String getResultText() {
        try { return waitForElement(resultSection).getText().trim(); }
        catch (Exception e) { return ""; }
    }

    public boolean resultContains(String text) {
        return getResultText().toLowerCase().contains(text.toLowerCase());
    }
}
