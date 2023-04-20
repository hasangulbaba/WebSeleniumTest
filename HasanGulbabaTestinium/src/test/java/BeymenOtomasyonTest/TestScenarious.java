package BeymenOtomasyonTest;

import io.github.bonigarcia.wdm.WebDriverManager;
import methods.Methods;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.safari.SafariDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.sql.DriverManager;
import java.util.Random;
import java.util.SimpleTimeZone;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class TestScenarious {

    public static Logger logger = Logger.getLogger(String.valueOf(TestScenarious.class));

    public WebDriver driver;
    private Methods methods;

    String beymenURL = "https://www.beymen.com/";
    String searchBoxPath = "input[class='default-input o-header__search--input']";
    String productList = "div[class='m-productCard__detail']";
    String productDetailNameTxt = "span[class='o-productDetail__description']";
    String priceProduct = "ins[id='priceNew']";

    String size = "span[class='m-variation__item']";
    String sepetim = "a[title='Sepetim']";
    String sepetPrice = "li[class='m-orderSummary__item -grandTotal'] [class=m-orderSummary__value]";
    String emptySepet = "strong[class='m-empty__messageTitle']";

    Random rnd = new Random();
    int index = rnd.nextInt(3);
    public TestScenarious(){
        methods = new Methods();
        this.driver = new ChromeDriver();
    }

    @Test
    public void beymenTest() throws InterruptedException {

        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        driver.get("https://www.beymen.com/");
        String currentUrl = driver.getCurrentUrl();
        Assert.assertEquals(beymenURL,currentUrl);
        logger.info("beymen sitesinin açılıp açılmadığı kontrol edildi.");

        methods.clickElement(methods.getById("onetrust-accept-btn-handler"));
        logger.info("Tüm çerezler kabul edildi.");

        methods.sendKeys(methods.getByCssSelector(searchBoxPath),"şort");
        logger.info("şort kelimesi arama alanına girildi.");

        methods.clearToElement(methods.getByCssSelector(searchBoxPath));
        logger.info("Arama alanı temizlendi");

        methods.sendKeysPressEnter(methods.getByCssSelector(searchBoxPath),"gömlek",true);
        logger.info("gömlek kelimesi arama alanına girildi.");

        methods.findElements(methods.getByCssSelector(productList)).get(index - 1).click();
        methods.waitByMilliSeconds(250);
        logger.info("Ürünlerden rastgele 3 ünden birine tıklandı");

        methods.putValueInTestMapElementGetText("urunAdi",productDetailNameTxt);
        methods.writeMapValueToTxt();
        logger.info("Ürün bilgisi txt ye yazıldı");

        methods.putValueInTestMapElementGetText("urunFiyat",priceProduct);
        methods.writeMapValueToTxt();
        logger.info("Fiyat bilgisi txtye yazıldı");

        methods.getElementTextFindTextClick(methods.getByCssSelector(size), "S");
        logger.info("S beden seçildi");

        methods.clickElement(methods.getById("addBasket"));
        logger.info("Ürün sepete eklendi");
        methods.waitBySeconds(4);

        methods.clickElement(methods.getByCssSelector(sepetim));
        logger.info("Sepetime gidildi");
        methods.waitBySeconds(2);

        methods.checkPricEqualSepetim(sepetPrice,"urunFiyat");

        methods.clickElement(methods.getById("removeCartItemBtn0-key-0"));
        logger.info("Ürün sepetten silindi");
        methods.waitBySeconds(2);

        methods.getElementTextFindTextContains(methods.getByCssSelector(emptySepet),"Sepetinizde Ürün Bulunmamaktadır");
        logger.info("Sepet boş kontolü");
    }

}
