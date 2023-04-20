package runner;

import dev.failsafe.ExecutionContext;
import io.github.bonigarcia.wdm.WebDriverManager;
import methods.Methods;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.*;

import java.sql.DriverManager;

public class TestRunner {

    private static Logger logger = LoggerFactory.getLogger(TestRunner.class);

    WebDriver driver;
    public TestRunner() {
    this.driver = Methods.driver;
    }


    @BeforeSuite
    public void beforeSuite() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeTest
    public void beforeSpec(ExecutionContext executionContext) {



    }

    @BeforeClass
    public void beforeScenario(ExecutionContext executionContext) {



    }


    @BeforeMethod
    public void beforeStep(ExecutionContext executionContext) {


    }

    @AfterMethod
    public void afterScenario(ExecutionContext executionContext) {

    driver.quit();
    }

    @AfterClass
    public void afterSpec() {


    }


    @AfterTest
    public void afterSuite() {


    }
}
