package com.adminpanel.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

/**
 * TablePage – handles both sortable and paged tables on eviltester.
 *
 * KEY FIX: The paged table at /styled/paged-table-test.html renders its rows
 * via JavaScript AFTER page load. Every method that reads rows MUST wait.
 * Using a dedicated waitForRows() that tries multiple table row selectors.
 */
public class TablePage extends BasePage {

    // Works for both sortable and paged table pages
    private final By anyTable     = By.cssSelector("table");
    private final By tableHeaders = By.cssSelector("table thead th, table th");
    private final By tableRows    = By.cssSelector("table tbody tr, table tr:not(:first-child)");

    // ── Core wait helpers ─────────────────────────────────────────────────────

    /**
     * Wait up to 20s for the table element to be visible.
     * The paged table page loads data asynchronously.
     */
    public boolean isTableVisible() {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(20))
                    .until(ExpectedConditions.visibilityOfElementLocated(anyTable));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Wait up to 20s for at least one data row to appear in ANY table on the page.
     * Tries both tbody>tr and plain tr selectors.
     */
    private boolean waitForRows() {
        // First make sure the table shell is there
        isTableVisible();
        // Then wait for rows
        By[] rowSelectors = {
            By.cssSelector("table tbody tr"),
            By.cssSelector("table tr:not(:first-child)"),
            By.cssSelector("tbody tr"),
            By.tagName("tr")
        };
        for (By sel : rowSelectors) {
            try {
                new WebDriverWait(driver, Duration.ofSeconds(20))
                        .until(ExpectedConditions.numberOfElementsToBeMoreThan(sel, 0));
                return true;
            } catch (Exception ignored) {}
        }
        return false;
    }

    // ── Sortable table ────────────────────────────────────────────────────────

    public boolean hasColumnHeaders() {
        isTableVisible();
        return !driver.findElements(tableHeaders).isEmpty();
    }

    public int getColumnHeaderCount() {
        return driver.findElements(tableHeaders).size();
    }

    public List<String> getColumnHeaderTexts() {
        return driver.findElements(tableHeaders)
                .stream().map(WebElement::getText)
                .collect(Collectors.toList());
    }

    public int getRowCount() {
        waitForRows();
        return driver.findElements(tableRows).size();
    }

    public String getFirstRowFirstCellText() {
        waitForRows();
        List<WebElement> cells = driver.findElements(
                By.cssSelector("table tbody tr:first-child td, table tr:nth-child(2) td"));
        return cells.isEmpty() ? "" : cells.get(0).getText().trim();
    }

    public void clickFirstColumnHeader() {
        isTableVisible();
        List<WebElement> headers = driver.findElements(tableHeaders);
        if (!headers.isEmpty()) {
            ((JavascriptExecutor) driver)
                    .executeScript("arguments[0].scrollIntoView({block:'center'});", headers.get(0));
            headers.get(0).click();
        }
    }

    public void clickColumnHeader(int index) {
        List<WebElement> headers = driver.findElements(tableHeaders);
        if (headers.size() > index) headers.get(index).click();
    }

    // ── Paged table ───────────────────────────────────────────────────────────

    /** Returns true once rows are visible (waits up to 20s). */
    public boolean hasRows() {
        return waitForRows();
    }

    public String getFirstRowText() {
        waitForRows();
        List<WebElement> rows = driver.findElements(tableRows);
        return rows.isEmpty() ? "" : rows.get(0).getText().trim();
    }

    /**
     * Click the "next page" control. Tries every known pattern on the
     * eviltester paged-table page in order of specificity.
     */
    public void clickNextPage() {
        // Scroll down so the nav is in view
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");

        String[] strategies = {
            "next-page", "nextpage", "next_page"
        };
        // Try by ID fragments
        for (String id : strategies) {
            try { driver.findElement(By.id(id)).click(); return; } catch (Exception ignored) {}
        }
        // Try link text variants
        String[] texts = {"next", "Next", "NEXT", ">", "»"};
        for (String t : texts) {
            try { driver.findElement(By.linkText(t)).click(); return; } catch (Exception ignored) {}
            try { driver.findElement(By.partialLinkText(t)).click(); return; } catch (Exception ignored) {}
        }
        // Try button/input with value "next"
        try {
            driver.findElements(By.tagName("button")).stream()
                .filter(e -> e.getText().toLowerCase().contains("next"))
                .findFirst().ifPresent(WebElement::click);
            return;
        } catch (Exception ignored) {}
        try {
            driver.findElements(By.cssSelector("input[type='button'],input[type='submit']")).stream()
                .filter(e -> (e.getAttribute("value") != null &&
                              e.getAttribute("value").toLowerCase().contains("next")))
                .findFirst().ifPresent(WebElement::click);
            return;
        } catch (Exception ignored) {}
        // JS fallback: click any element whose text is "next"
        try {
            ((JavascriptExecutor) driver).executeScript(
                "var els = document.querySelectorAll('a,button,input');" +
                "for(var i=0;i<els.length;i++){" +
                "  var t=(els[i].textContent||els[i].value||'').trim().toLowerCase();" +
                "  if(t==='next'||t==='>'||t==='»'){els[i].click();break;}" +
                "}");
        } catch (Exception ignored) {}
    }
}
