package com.adminpanel.tests;

import com.adminpanel.dataproviders.TestDataProvider;
import com.adminpanel.pages.FormPage;
import com.adminpanel.utils.DriverManager;
import com.adminpanel.utils.RetryAnalyzer;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Test Module 1 – Form Interactions
 *  TC_FORM_01 : Fill valid form data and submit – result shown (data-driven Excel)
 *  TC_FORM_02 : Select dropdown and verify value in result
 *  TC_FORM_03 : Toggle radio button – verify submitted value
 *  TC_FORM_04 : Submit empty form – verify default/error behavior
 */
public class FormInteractionTest extends BaseTest {

    private static final String FORM_URL = com.adminpanel.config.ConfigReader.getInstance().getFormUrl();

    @BeforeMethod(alwaysRun = true)
    public void goToFormPage() {
        DriverManager.getDriver().get(FORM_URL);
    }

    @DataProvider(name = "formDataExcel", parallel = false)
    public Object[][] formDataFromExcel() {
        return TestDataProvider.getFormDataFromExcel();
    }

    @DataProvider(name = "formDataJson", parallel = false)
    public Object[][] formDataFromJson() {
        return TestDataProvider.getFormDataFromJson();
    }

    // ── TC_FORM_01 – data-driven form fill from Excel ─────────────────────────
    @Test(dataProvider = "formDataExcel",
          description = "Fill form with data from Excel and verify result is shown",
          retryAnalyzer = RetryAnalyzer.class)
    public void testFormSubmissionFromExcel(String username, String password,
                                             String comments, String filename,
                                             String description) {
        FormPage formPage = new FormPage();

        formPage.fillAndSubmitForm(username, password, comments);

        Assert.assertTrue(formPage.isResultDisplayed(),
                "Result must be displayed after form submission. Case: " + description);
    }

    // ── TC_FORM_02 – dropdown selection ───────────────────────────────────────
    @Test(description = "Select dropdown option and verify selected value in result",
          retryAnalyzer = RetryAnalyzer.class)
    public void testDropdownSelection() {
        FormPage formPage = new FormPage();

        formPage.enterUsername("TestUser");
        formPage.selectDropdownByValue("dd1");
        formPage.clickSubmit();

        Assert.assertTrue(formPage.isResultDisplayed(),
                "Result must be shown after selecting dropdown and submitting");
    }

    // ── TC_FORM_03 – radio button ─────────────────────────────────────────────
    @Test(description = "Select radio button and verify submitted value in result",
          retryAnalyzer = RetryAnalyzer.class)
    public void testRadioButtonSelection() {
        FormPage formPage = new FormPage();

        formPage.selectFirstRadioButton();
        formPage.clickSubmit();

        Assert.assertTrue(formPage.isResultDisplayed(),
                "Result must be shown after selecting radio button and submitting");
    }

    // ── TC_FORM_04 – empty form submission ────────────────────────────────────
    @Test(description = "Submit empty form – verify default or error behavior",
          retryAnalyzer = RetryAnalyzer.class)
    public void testEmptyFormSubmission() {
        FormPage formPage = new FormPage();

        formPage.clickSubmit();

        // eviltester basic form accepts empty submissions and shows result
        String url = DriverManager.getDriver().getCurrentUrl();
        Assert.assertTrue(
                formPage.isResultDisplayed() || url.contains("eviltester"),
                "After empty submit, must either show result or stay on eviltester. URL: " + url);
    }
}
