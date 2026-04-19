package com.adminpanel.config;

import java.io.InputStream;
import java.util.Properties;

/**
 * ConfigReader – singleton that reads config.properties.
 * All URLs and values come from here — no hardcoding in test or page classes.
 */
public class ConfigReader {

    private static ConfigReader instance;
    private final Properties properties = new Properties();

    private ConfigReader() {
        try (InputStream in = getClass().getClassLoader()
                .getResourceAsStream("config.properties")) {
            if (in == null) throw new RuntimeException("config.properties not found on classpath");
            properties.load(in);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }
    }

    public static synchronized ConfigReader getInstance() {
        if (instance == null) instance = new ConfigReader();
        return instance;
    }

    public String  getBrowser()        { return properties.getProperty("browser", "chrome").trim(); }
    public String  getBaseUrl()        { return properties.getProperty("base.url", "").trim(); }
    public int     getTimeout()        { return Integer.parseInt(properties.getProperty("timeout", "15").trim()); }
    public boolean isHeadless()        { return Boolean.parseBoolean(properties.getProperty("headless", "false").trim()); }
    public String  getScreenshotsPath(){ return properties.getProperty("screenshots.path", "screenshots/").trim(); }
    public String  getReportPath()     { return properties.getProperty("report.path", "test-output/report.html").trim(); }

    // Page-specific URLs (base.url + path)
    public String getFormUrl()         { return getBaseUrl() + properties.getProperty("form.url", "/styled/basic-html-form-test.html"); }
    public String getTableSortUrl()    { return getBaseUrl() + properties.getProperty("table.sort.url", "/styled/tag/table.html"); }
    public String getTablePagedUrl()   { return getBaseUrl() + properties.getProperty("table.paged.url", "/styled/paged-table-test.html"); }
    public String getAlertUrl()        { return getBaseUrl() + properties.getProperty("alert.url", "/styled/alerts/alert-test.html"); }
    public String getDynamicUrl()      { return getBaseUrl() + properties.getProperty("dynamic.url", "/styled/dynamic/dynamic-fields-test.html"); }
    public String getRedirectUrl()     { return getBaseUrl() + properties.getProperty("redirect.url", "/styled/redirects-test.html"); }
    public String getFrameUrl()        { return getBaseUrl() + properties.getProperty("frame.url", "/styled/iframes/iframes-test.html"); }
}
