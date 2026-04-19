package com.adminpanel.utils;

import com.adminpanel.config.ConfigReader;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.util.HashMap;
import java.util.Map;

public class DriverManager {

    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

    private DriverManager() {}

    public static WebDriver getDriver() { return driverThreadLocal.get(); }

    public static void initDriver() {
        ConfigReader config  = ConfigReader.getInstance();
        String  browser      = config.getBrowser().toLowerCase();
        boolean headless     = config.isHeadless();
        WebDriver driver;

        switch (browser) {
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions ff = new FirefoxOptions();
                if (headless) ff.addArguments("--headless");
                driver = new FirefoxDriver(ff);
                break;
            default:
                WebDriverManager.chromedriver().setup();
                ChromeOptions ch = new ChromeOptions();
                Map<String, Object> prefs = new HashMap<>();
                prefs.put("credentials_enable_service", false);
                prefs.put("profile.password_manager_enabled", false);
                prefs.put("profile.password_manager_leak_detection", false);
                ch.setExperimentalOption("prefs", prefs);
                ch.addArguments("--disable-save-password-bubble");
                ch.addArguments("--disable-features=PasswordLeakDetection");
                ch.addArguments("--password-store=basic");
                if (headless) {
                    ch.addArguments("--headless=new", "--disable-gpu", "--window-size=1920,1080");
                }
                ch.addArguments("--no-sandbox", "--disable-dev-shm-usage");
                driver = new ChromeDriver(ch);
        }
        driver.manage().window().maximize();
        driverThreadLocal.set(driver);
    }

    public static void quitDriver() {
        if (driverThreadLocal.get() != null) {
            driverThreadLocal.get().quit();
            driverThreadLocal.remove();
        }
    }
}
