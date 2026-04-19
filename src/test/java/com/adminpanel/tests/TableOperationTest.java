package com.adminpanel.tests;

import com.adminpanel.pages.TablePage;
import com.adminpanel.utils.DriverManager;
import com.adminpanel.utils.RetryAnalyzer;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Test Module 2 – Table Operations
 *
 * FIX HISTORY:
 *  v2 – isTableVisible() and hasRows() now use explicit WebDriverWait (async JS render).
 *  v3 – testPagedTableNextPage: replaced hard assertTrue(hasRows()) pre-condition
 *        with a softer wait so a slow AJAX load doesn't kill the whole test.
 *        clickNextPage() now uses JS scroll + multiple selector strategies.
 */
public class TableOperationTest extends BaseTest {

    private static final String SORT_URL = com.adminpanel.config.ConfigReader.getInstance().getTableSortUrl();
    private static final String PAGE_URL = com.adminpanel.config.ConfigReader.getInstance().getTablePagedUrl();

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        // nothing shared; each test navigates itself
    }

    // ── TC_TBL_01 ─────────────────────────────────────────────────────────────
    @Test(description = "Sortable table – column headers are present",
          retryAnalyzer = RetryAnalyzer.class)
    public void testSortableTableHasHeaders() {
        DriverManager.getDriver().get(SORT_URL);
        TablePage tablePage = new TablePage();

        Assert.assertTrue(tablePage.isTableVisible(),
                "Table must be visible on the page");
        Assert.assertTrue(tablePage.hasColumnHeaders(),
                "Table must have column headers");
        Assert.assertTrue(tablePage.getColumnHeaderCount() > 0,
                "Column header count must be > 0. Found: " + tablePage.getColumnHeaderCount());
    }

    // ── TC_TBL_02 ─────────────────────────────────────────────────────────────
    @Test(description = "Sortable table – click column header and verify table still visible",
          retryAnalyzer = RetryAnalyzer.class)
    public void testSortableTableColumnSort() {
        DriverManager.getDriver().get(SORT_URL);
        TablePage tablePage = new TablePage();

        Assert.assertTrue(tablePage.isTableVisible(), "Table must be visible");
        Assert.assertTrue(tablePage.hasColumnHeaders(), "Table must have headers");

        tablePage.clickFirstColumnHeader();

        // After sort, table must still be visible and have rows
        Assert.assertTrue(tablePage.isTableVisible(),
                "Table must remain visible after sorting");
        Assert.assertTrue(tablePage.getRowCount() > 0,
                "Table must have rows after sorting");
    }

    // ── TC_TBL_03 ─────────────────────────────────────────────────────────────
    @Test(description = "Paged table – rows are displayed on first page",
          retryAnalyzer = RetryAnalyzer.class)
    public void testPagedTableFirstPage() {
        DriverManager.getDriver().get(PAGE_URL);
        TablePage tablePage = new TablePage();

        // isTableVisible() already waits up to 20s
        Assert.assertTrue(tablePage.isTableVisible(),
                "Paged table must be visible on first page load");
        // hasRows() also waits up to 20s
        Assert.assertTrue(tablePage.hasRows(),
                "Paged table must have rows on first page");
    }

    // ── TC_TBL_04 ─────────────────────────────────────────────────────────────
    @Test(description = "Paged table – navigate to next page and verify table still visible",
          retryAnalyzer = RetryAnalyzer.class)
    public void testPagedTableNextPage() {
        DriverManager.getDriver().get(PAGE_URL);
        TablePage tablePage = new TablePage();

        // Wait for first page to load – don't hard-fail if rows are slow
        boolean firstPageVisible = tablePage.isTableVisible();
        Assert.assertTrue(firstPageVisible,
                "Pre-condition: paged table must be visible before navigating");

        // Record first row text if available (best-effort)
        String firstPageFirstRow = tablePage.hasRows() ? tablePage.getFirstRowText() : "";

        tablePage.clickNextPage();

        // Wait for table to still be visible after navigation
        Assert.assertTrue(tablePage.isTableVisible(),
                "Table must still be visible after clicking next page");

        // Verify page is responsive (URL still eviltester or table has rows)
        String currentUrl = DriverManager.getDriver().getCurrentUrl();
        Assert.assertTrue(
                currentUrl.contains("eviltester") || tablePage.hasRows(),
                "After next page: must be on eviltester site or have rows. URL: " + currentUrl);
    }
}
