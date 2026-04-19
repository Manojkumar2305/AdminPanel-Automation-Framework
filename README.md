# AdminPanel – EvilTester Admin Dashboard Automation Framework

## AUT
**URL:** https://testpages.eviltester.com/styled/index.html
**No registration needed** – public test practice site

## Page URLs Used
| Module | Page | URL |
|---|---|---|
| Forms | Basic HTML Form | /styled/basic-html-form-test.html |
| Tables | Sortable Table | /styled/tag/table.html |
| Tables | Paged Table | /styled/paged-table-test.html |
| Alerts | Alert/Confirm/Prompt | /styled/alerts/alert-test.html |
| Dynamic | Dynamic Fields | /styled/dynamic/dynamic-fields-test.html |
| Dynamic | Redirects | /styled/redirects-test.html |
| Frames | iFrames | /styled/iframes/iframes-test.html |

## Tech Stack
| Tool | Version |
|---|---|
| Java | 11+ |
| Selenium | 4.18.1 |
| TestNG | 7.9.0 |
| WebDriverManager | 5.7.0 |
| ExtentReports | 5.1.1 |
| Apache POI | 5.2.5 (Excel) |
| Jackson | 2.16.1 (JSON) |

## Project Structure
```
AdminPanel/
├── pom.xml
├── testng.xml
├── screenshots/                      ← auto-created on failure
├── test-output/
│   └── AdminPanelReport.html         ← auto-generated after run
└── src/
    ├── main/java/com/adminpanel/
    │   ├── config/ConfigReader.java
    │   ├── pages/
    │   │   ├── BasePage.java          ← waitForElement + switchToAlert
    │   │   ├── FormPage.java          ← Module 1
    │   │   ├── TablePage.java         ← Module 2
    │   │   ├── AlertPage.java         ← Module 3
    │   │   ├── DynamicPage.java       ← Module 4
    │   │   └── FramePage.java         ← Module 5
    │   ├── utils/
    │   │   ├── DriverManager.java
    │   │   ├── ScreenshotUtil.java
    │   │   ├── ExtentReportManager.java
    │   │   └── RetryAnalyzer.java
    │   └── listeners/TestListener.java
    └── test/
        ├── java/com/adminpanel/
        │   ├── dataproviders/TestDataProvider.java
        │   └── tests/
        │       ├── BaseTest.java
        │       ├── FormInteractionTest.java   ← Module 1
        │       ├── TableOperationTest.java    ← Module 2
        │       ├── AlertDialogTest.java       ← Module 3
        │       ├── DynamicElementTest.java    ← Module 4
        │       └── FrameTest.java             ← Module 5
        └── resources/
            ├── config.properties
            ├── formData.json
            └── testdata.xlsx (FormData + AlertData sheets)
```

## Eclipse Setup
1. File → Import → Maven → Existing Maven Projects → browse to AdminPanel
2. Right-click pom.xml → Maven → Update Project (Alt+F5)
3. Run: `mvn test`

## Test Count Breakdown
| Module | Tests | Data Source |
|---|---|---|
| 1 – Forms | 3 (Excel DP) + 3 = 6 | testdata.xlsx FormData |
| 2 – Tables | 4 | inline |
| 3 – Alerts | 4 | inline |
| 4 – Dynamic | 3 | inline |
| 5 – Frames | 3 | inline |
| **Total** | **20** | |

## All Hackathon Requirements Met
| Requirement | Implementation |
|---|---|
| 5 Page classes + BasePage | FormPage, TablePage, AlertPage, DynamicPage, FramePage |
| BasePage.switchToAlert() | Built into BasePage as required by spec |
| No Thread.sleep() | WebDriverWait + FluentWait only |
| No hardcoded values | config.properties + testdata.xlsx + formData.json |
| @DataProvider from Excel | FormData sheet via Apache POI |
| @DataProvider from JSON | formData.json via Jackson |
| Screenshot on failure | TestListener auto-captures with timestamp |
| ExtentReports HTML | test-output/AdminPanelReport.html |
| Parallel execution | thread-count="2" (Alerts run serially) |
| Retry on failure | RetryAnalyzer (1 retry per test) |
| Headless mode | headless=true in config.properties |
| mvn test via testng.xml | Surefire plugin configured |
