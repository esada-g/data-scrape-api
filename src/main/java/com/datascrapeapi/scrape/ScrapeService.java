package com.datascrapeapi.scrape;

import com.datascrapeapi.company.Company;
import com.datascrapeapi.company.CompanyDTO;
import com.datascrapeapi.company.CompanyService;
import com.datascrapeapi.exceptions.StockDataException;
import com.datascrapeapi.exceptions.TickerDoesNotExistException;
import com.datascrapeapi.headquarter.HeadquarterDTO;
import com.datascrapeapi.headquarter.HeadquarterService;
import com.datascrapeapi.stock.data.StockData;
import com.datascrapeapi.stock.data.StockDataDTO;
import com.datascrapeapi.stock.data.StockDataService;
import com.datascrapeapi.utils.AddressUtils;
import com.datascrapeapi.utils.DateUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.*;

@Service
public class ScrapeService {

    private static final String CHROMEDRIVER_PATH = "/usr/bin/chromedriver";
    private static final String CHROME_DRIVER_PROPERTY =
            "webdriver.chrome.driver";
    private static final String CHROME_OPTIONS_HEADLESS = "--headless";
    private static final String CHROME_OPTIONS_DISABLE_GPU = "--disable-gpu";
    private static final String BASE_URL = "https://finance.yahoo.com/quote/";
    private static final String PROFILE_XPATH = "//h1[contains(@class, 'D(ib)')]";
    private static final String DESCRIPTION_XPATH =
            "//section[contains(@class, 'quote-sub-section')]/p";
    private static final String FULL_TIME_EMPLOYEES_XPATH =
            "//span[contains(text(), 'Full Time Employees')]/following-sibling::span[1]";
    private static final String MARKET_CAP_XPATH =
            "//td[@data-test='MARKET_CAP-value']";
    private static final String HISTORY_TABLE_XPATH =
            "//table[@data-test='historical-prices']";
    private static final String QSP_PROFILE_XPATH =
            "//div[@data-test='qsp-profile']/div/p";
    private static final String SUMMARY_XPATH = "//li[@data-test='SUMMARY']/a";
    private static final String FOUNDED_IN_KEYWORD = "was founded in ";
    private static final String INCORPORATED_IN_KEYWORD = "was incorporated in ";
    private static final String EMPTY_STRING = "";
    private final CompanyService companyService;
    private final HeadquarterService headquarterService;
    private final StockDataService stockDataService;

    public ScrapeService(
            CompanyService companyService,
            HeadquarterService headquarterService,
            StockDataService stockDataService
    ) {
        this.companyService = companyService;
        this.headquarterService = headquarterService;
        this.stockDataService = stockDataService;
    }

    private static int findIndex(String description, String keyword) {
        return description.indexOf(keyword);
    }

    private static String extractYear(String description, int start, int length) {
        if (start != -1 && start + length + 4 <= description.length()) {
            String yearSubstring = description.substring(
                    start + length,
                    start + length + 4
            );
            return yearSubstring.trim();
        }
        return EMPTY_STRING;
    }

    private static String getYearFoundedFromString(String description) {
        int foundedIndex = findIndex(description, FOUNDED_IN_KEYWORD);
        int incorporatedIndex = findIndex(description, INCORPORATED_IN_KEYWORD);

        if (foundedIndex != -1) {
            return extractYear(
                    description,
                    foundedIndex,
                    FOUNDED_IN_KEYWORD.length()
            );
        } else if (incorporatedIndex != -1) {
            return extractYear(
                    description,
                    incorporatedIndex,
                    INCORPORATED_IN_KEYWORD.length()
            );
        }

        return EMPTY_STRING;
    }

    @Transactional
    public List<ScrapeDataResponse> scrapeData(String date, List<String> tickers)
            throws StockDataException, TickerDoesNotExistException {
        List<ScrapeDataResponse> responses = new ArrayList<>();
        WebDriver driver = setupWebDriver();
        for (String ticker : tickers) {
            ScrapeDataResponse response = new ScrapeDataResponse();
            response.setTicker(ticker);
            try {
                processTicker(driver, ticker, date);
                response.setOk(true);
            } catch (StockDataException | TickerDoesNotExistException e) {
                response.setOk(false);
                response.setError(e.getMessage());
            }
            responses.add(response);
        }
        driver.quit();
        return responses;
    }

    private WebDriver setupWebDriver() {
        System.setProperty(CHROME_DRIVER_PROPERTY, CHROMEDRIVER_PATH);
        ChromeOptions options = new ChromeOptions();
        options.addArguments(CHROME_OPTIONS_HEADLESS, CHROME_OPTIONS_DISABLE_GPU);
        return new ChromeDriver(options);
    }

    private void processTicker(WebDriver driver, String ticker, String date)
            throws StockDataException, TickerDoesNotExistException {
        extractData(driver, ticker, date);
    }

    private void extractData(WebDriver driver, String ticker, String date)
            throws StockDataException {
        navigateToProfile(driver, ticker);
        String companyNameAndTicker = extractTextFromWebElement(
                driver,
                PROFILE_XPATH
        );

        if (companyNameAndTicker.isEmpty()) {
            throw new TickerDoesNotExistException(
                    "Company with ticker: " + ticker + " does not exist on Yahoo! Finance."
            );
        }
        String companyName = getCompanyName(companyNameAndTicker);
        String companyTicker = getCompanyTicker(companyNameAndTicker);
        String description = extractTextFromWebElement(driver, DESCRIPTION_XPATH);
        String yearFounded = getYearFoundedFromString(description);
        String fullTimeEmployees = extractTextFromWebElement(
                driver,
                FULL_TIME_EMPLOYEES_XPATH
        );

        List<String> cityAndState = extractAddressFromWebElement(driver);
        String city = AddressUtils.getCity(cityAndState);
        String state = AddressUtils.getState(cityAndState);

        navigateToSummary(driver);
        String marketCap = extractTextFromWebElement(driver, MARKET_CAP_XPATH);

        navigateToHistoricalData(driver, ticker, date);
        WebElement table = driver.findElement(By.xpath(HISTORY_TABLE_XPATH));
        String openPrice = getOpenPrice(table);
        String previousClosePrice = getPreviousClosePrice(table);

        if (openPrice.isEmpty() && previousClosePrice.isEmpty()) {
            throw new StockDataException(
                    "No historical data " + " on " + date + " for company: " + companyName
            );
        }
        if (!openPrice.isEmpty() && previousClosePrice.isEmpty()) {
            previousClosePrice = getClosePrice(table);
        }

        saveScrapedData(
                companyName,
                companyTicker,
                yearFounded,
                fullTimeEmployees,
                city,
                state,
                marketCap,
                openPrice,
                previousClosePrice,
                date
        );
    }

    private String getPriceFromRow(
            WebElement table,
            int rowIndex,
            int columnIndex
    ) {
        String price = EMPTY_STRING;
        try {
            WebElement row = table.findElement(
                    By.xpath("//tbody/tr[count(td) > 2][" + rowIndex + "]")
            );
            price =
                    row.findElement(By.xpath(".//td[" + columnIndex + "]/span")).getText();
            return price;
        } catch (NoSuchElementException e) {
            return price;
        }
    }

    private String getPreviousClosePrice(WebElement table) {
        return getPriceFromRow(table, 2, 6);
    }

    private String getOpenPrice(WebElement table) {
        return getPriceFromRow(table, 1, 2);
    }

    private String getClosePrice(WebElement table) {
        return getPriceFromRow(table, 1, 6);
    }

    private String extractTextFromWebElement(WebDriver driver, String xpath) {
        try {
            WebElement element = driver.findElement(By.xpath(xpath));
            return element.getText();
        } catch (NoSuchElementException e) {
            return EMPTY_STRING;
        }
    }

    private List<String> extractAddressFromWebElement(WebDriver driver) {
        try {
            WebElement addressElement = driver
                    .findElements(By.xpath(QSP_PROFILE_XPATH))
                    .get(0);
            String address = addressElement.getText();
            return AddressUtils.getCityAndState(address);
        } catch (IndexOutOfBoundsException e) {
            return Collections.emptyList();
        }
    }

    private String getCompanyName(String companyText) {
        return companyText.split("\\(")[0].trim();
    }

    private String getCompanyTicker(String companyText) {
        return companyText.split("\\(").length > 1
                ? companyText.split("\\(")[1].replace(")", "").trim()
                : "";
    }

    private void navigateToProfile(WebDriver driver, String ticker) {
        String url = BASE_URL + ticker + "/profile";
        driver.get(url);
        System.out.println(url);
    }

    private void navigateToSummary(WebDriver driver) {
        WebElement summary = driver.findElement(By.xpath(SUMMARY_XPATH));
        summary.click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//div[@id='quote-summary']")
                )
        );
    }

    private void navigateToHistoricalData(
            WebDriver driver,
            String ticker,
            String date
    ) {
        String startDate = DateUtils.setDateToOneYearBefore(date);
        String url =
                BASE_URL +
                        ticker +
                        "/history?period1=" +
                        DateUtils.dateStringToUnixTimestamp(startDate) +
                        "&period2=" +
                        DateUtils.dateStringToUnixTimestamp(date) +
                        "&interval=1d&filter=history&frequency=1d&includeAdjustedClose=true";
        driver.get(url);
    }

    private void checkIfDataAlreadySaved(String date, Company company) {
        Date targetDate = DateUtils.stringToDate(date);
        Optional<StockData> stockData = stockDataService.getStockDataByCompanyIdAndTargetDate(
                company.getId(),
                targetDate
        );
        if (stockData.isPresent()) {
            throw new StockDataException(
                    "Stock data for " +
                            company.getTicker() +
                            " on " +
                            date +
                            " already saved."
            );
        }
    }

    private void saveScrapedData(
            String companyName,
            String companyTicker,
            String yearFounded,
            String fullTimeEmployees,
            String city,
            String state,
            String marketCap,
            String openPrice,
            String previousClosePrice,
            String date
    ) {
        HeadquarterDTO headquarterDTO = new HeadquarterDTO();
        headquarterDTO.setCity(city);
        headquarterDTO.setState(state);

        if (
                !headquarterDTO.getCity().isEmpty() &&
                        !headquarterDTO.getState().isEmpty()
        ) {
            headquarterService.saveHeadquarterIfNotExists(headquarterDTO);
        }

        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setCompanyName(companyName);
        companyDTO.setFullTimeEmployees(fullTimeEmployees);
        companyDTO.setCompanyTicker(companyTicker);
        companyDTO.setYearFounded(yearFounded);
        companyDTO.setMarketCap(marketCap);

        Company company = companyService.saveOrUpdateCompany(
                companyDTO,
                headquarterDTO
        );

        StockDataDTO stockDataDTO = new StockDataDTO();
        stockDataDTO.setScrapingDate(new Date());
        stockDataDTO.setTargetDate(date);
        stockDataDTO.setPreviousClosePrice(previousClosePrice);
        stockDataDTO.setOpenPrice(openPrice);

        checkIfDataAlreadySaved(date, company);

        stockDataService.saveStockData(stockDataDTO, company);
    }
}
