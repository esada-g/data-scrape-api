package com.datascrapeapi.scrape;

public class ScrapeConstants {

    public static final String EMPTY_STRING = "";
    public static final String FOUNDED_IN_KEYWORD = "was founded in ";
    public static final String INCORPORATED_IN_KEYWORD = "was incorporated in ";
    public static final String CHROMEDRIVER_PATH = "/usr/bin/chromedriver";
    public static final String CHROME_DRIVER_PROPERTY = "webdriver.chrome.driver";
    public static final String CHROME_OPTIONS_HEADLESS = "--headless";
    public static final String CHROME_OPTIONS_DISABLE_GPU = "--disable-gpu";
    public static final String BASE_URL = "https://finance.yahoo.com/quote/";
    public static final String PROFILE_XPATH = "//h1[contains(@class, 'D(ib)')]";
    public static final String DESCRIPTION_XPATH = "//section[contains(@class, 'quote-sub-section')]/p";
    public static final String FULL_TIME_EMPLOYEES_XPATH = "//span[contains(text(), 'Full Time Employees')]/following-sibling::span[1]";
    public static final String MARKET_CAP_XPATH = "//td[@data-test='MARKET_CAP-value']";
    public static final String HISTORY_TABLE_XPATH = "//table[@data-test='historical-prices']";
    public static final String QSP_PROFILE_XPATH = "//div[@data-test='qsp-profile']/div/p";
    public static final String SUMMARY_XPATH = "//li[@data-test='SUMMARY']/a";

}
