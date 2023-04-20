package methods;

import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.sql.Driver;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static java.time.Duration.ofMillis;

public class Methods {

    private static Logger logger = LoggerFactory.getLogger(Methods.class);
    public static WebDriver driver=null;
    private FluentWait wait;
    private long pollingMillis;
    private By by;
    public static HashMap<String, String> TestMap;
    JavascriptExecutor jsDriver;

    public Methods() {
        //this.driver = new ChromeDriver();
        wait = setFluentWait(30);
        pollingMillis = 300;

    }

    public void navigateToBack() {
        driver.navigate().back();
    }

    public FluentWait setFluentWait(long timeout) {

        FluentWait fluentWait = new FluentWait(driver);
        fluentWait.withTimeout(Duration.ofSeconds(timeout))
                .pollingEvery(ofMillis(pollingMillis))
                .ignoring(NoSuchElementException.class);
        return fluentWait;
    }

    public void waitByMilliSeconds(long milliSeconds) {

        try {
            Thread.sleep(milliSeconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void waitBySeconds(long seconds) {

        waitBySeconds(seconds);
    }

    public By getById(String key){
        return By.id(key);
    }

    public By getByXpath(String key){
        return By.xpath(key);
    }

    public By getByClass(String key){
        return By.className(key);
    }

    public By getByCssSelector(String key){
        return By.cssSelector(key);
    }

    public void clickElement(By by) {
        findElement(by).click();
        logger.info("Elemente tıklandı.");
    }

    public WebElement findElement(By by) {
        logger.info("Element " + by.toString() + " by değerine sahip");
        return (WebElement) wait.until(ExpectedConditions.presenceOfElementLocated(by));
    }

    public List<WebElement> findElements(By by) {
        logger.info("Element " + by.toString() + " by değerine sahip");
        return (List<WebElement>) wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(by));
    }

    public String getText(By by) {
        return findElement(by).getText();
    }

    public void sendKeys(By by, String text) {

        findElement(by).sendKeys(text);
        logger.info("Elemente " + text + " texti yazıldı.");
    }

    public String getTextToElement(By by) {
        return findElement(by).getText();
    }

    public void clearToElement(By by) {

        findElement(by).clear();
    }

    public void sendKeysPressEnter(By by, String text, boolean condition) {

        findElement(by).sendKeys(text);
        if (condition)
            findElement(by).sendKeys(Keys.ENTER);
        logger.info("Elemente " + text + " texti yazıldı.");
    }

    public void putValueInTestMapElementGetText(String key, String parameter) {
        putValueInTestMap(parameter, getTextToElement(getByCssSelector(key)));
        logger.info("Map değerine paramtere: " + parameter + " değeri " + getTextToElement(getByCssSelector(key)) + " eklendi");
    }

    public void putValueInTestMap(String key, String value) {

        TestMap.put(key, value);
    }

    public String getValueInTestMap(String key) {

        return TestMap.get(key).toString();
    }

    public void writeMapValueToTxt(){

        try {
            FileWriter writer = new FileWriter("output.txt");
            for (String value : TestMap.values()) {
                String line =  value + "\n";
                writer.write(line);
                writer.flush();
            }
            TestMap.clear();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public WebElement getElementList(By by, String value, String option) {
        WebElement element = null;
        while (element == null) {
            element = getElementLists(by, value, option);
            if (element == null) {
                if (element.isDisplayed() == false) {
                    return null;
                }
            }
        }
        return element;
    }

    private WebElement getElementLists(By by, String value, String option) {
        int rowCount = 0;

        List<WebElement> allRows = findElements(by);

        rowCount = allRows.size();


        if (rowCount == 0)

            return null;

        WebElement elem = null;


        for (WebElement row : allRows) {
            try {
                scrollElement(row);
                elem = row;
            } catch (Exception e) {
                e.printStackTrace();
            }


            if (check(elem, value, option)) {
                scrollElement(elem);
                logger.info(value + " değeri taşıyan element satırı bulundu. Kontrolü başarılı!");
                return row;
            }
        }

        throw new java.util.NoSuchElementException("Elementi alınmak istenen Listenin içerisindeki satırın elementi bulunamadı! Kontrol ediniz.");
    }

    private boolean check(WebElement element, String value, String option) {
        boolean exit = false;

        switch (option) {
            case "text":
                exit = element.getText().contains(value);
                break;
            case "value":
                exit = element.getAttribute(option).contains(value);
                break;

        }
        return exit;
    }

    public void scrollElement(WebElement webElement) {

        jsDriver.executeScript("arguments[0].scrollIntoView();", webElement);
    }

    public void getElementTextFindTextClick(By by, String expectedText) {
        waitByMilliSeconds(250);
        WebElement element = getElementList(by, expectedText, "text");
        logger.info("Beklenen text: " + expectedText);
        logger.info("Alınan text: " + element.getText());
        Assert.assertTrue("Beklenen değer kontrolü başarılı", element.getText().contains(expectedText));
        clickByElement(element);
        logger.info(expectedText + " değerine tıklandı.");
    }

    public void clickByElement(WebElement webElement) {

        jsDriver.executeScript("arguments[0].click();", webElement);
    }

    public void checkPricEqualSepetim(String key, String mapKey) {
        String actualText = getTextToElement(getByCssSelector(key)).replace("\r", "").replace("\n", "");
        logger.info("Alınan text: " + actualText);
        String value = getValueInTestMap(mapKey);
        logger.info("Beklenen text: " + value);
        Assert.assertEquals("Text değerleri eşit değil", value, actualText);
        logger.info("Text değerleri eşit");
    }

    public void getElementTextFindTextContains(By by, String expectedText) {
        waitByMilliSeconds(250);
        WebElement element = getElementList(by, expectedText, "text");
        logger.info("Beklenen text: " + expectedText);
        logger.info("Alınan text: " + element.getText());
        Assert.assertTrue("Beklenen değer kontrolü başarılı", element.getText().contains(expectedText));

    }

}
