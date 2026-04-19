# AdminPanel-Automation-Framework
AdminPanel Automation Framework – A robust Selenium Java Test Automation Framework built using Page Object Model (POM) and TestNG to automate a web-based admin dashboard. Covers forms, tables, alerts, dynamic elements, and frames with reusable design, data-driven testing, and detailed reporting.

# 🚀 AdminPanel – Selenium Java Automation Framework

## 📌 Overview

AdminPanel is a **Selenium WebDriver + Java Test Automation Framework** designed to test a web-based admin dashboard application.
It follows **Page Object Model (POM)** architecture for scalability, reusability, and maintainability.

The framework automates **end-to-end UI scenarios** including forms, tables, alerts, dynamic elements, and frames.

---

## 🎯 Features

* ✅ Page Object Model (POM) design
* ✅ TestNG framework with annotations & DataProviders
* ✅ WebDriverManager (no manual driver setup)
* ✅ Explicit waits using WebDriverWait
* ✅ Config-driven execution (browser, URL, timeout)
* ✅ Screenshot capture on failure
* ✅ ExtentReports HTML reporting
* ✅ Retry mechanism for flaky tests
* ✅ Clean and reusable architecture

---

## 🌐 Application Under Test

🔗 https://testpages.eviltester.com/styled/index.html

Modules covered:

* Forms
* Tables
* Alerts
* Dynamic Elements
* Frames

---

## 🧱 Project Structure

```
AdminPanelFramework/
│
├── src/test/java
│   ├── tests/           → Test classes (TestNG)
│   ├── pages/           → Page Object classes
│   ├── utils/           → Utilities (ConfigReader, Waits, Listeners)
│   ├── dataproviders/   → Test data handling
│
├── src/main/resources
│   └── config.properties
│
├── screenshots/         → Failure screenshots
├── test-output/         → Reports
├── pom.xml              → Maven dependencies
├── testng.xml           → Test suite configuration
```

---

## ⚙️ Tech Stack

* Java
* Selenium WebDriver
* TestNG
* Maven
* WebDriverManager
* ExtentReports

---

## 🔧 Configuration (config.properties)

```
browser=chrome
base.url=https://testpages.eviltester.com/styled/index.html
timeout=15
```

---

## 🧪 Test Modules

### 1️⃣ Form Interactions

* Fill forms and validate submission
* Handle dropdowns, radio buttons, checkboxes
* Validate empty input behavior

### 2️⃣ Table Operations

* Sort table columns
* Validate pagination behavior

### 3️⃣ Alerts & Dialogs

* Handle Alert, Confirm, Prompt
* Validate results after interaction

### 4️⃣ Dynamic Elements

* Handle hidden/dynamic fields
* Validate visibility and interaction
* Verify redirects

### 5️⃣ Frames

* Switch to iframe and nested frames
* Validate content inside frames
* Switch back to main page

---

## ▶️ How to Run

### Run via Maven

```
mvn clean test
```

### Run via TestNG

* Right click → `testng.xml` → Run

---

## 📊 Reporting

* ExtentReports HTML generated after execution
* Includes:

  * Test status (Pass/Fail)
  * Logs
  * Screenshots on failure

---

## 📸 Screenshot on Failure

* Automatically captured using TestNG Listener
* Stored in `/screenshots/` with timestamp

---
