package JavaTest.LambdaTest;

import java.net.URL;
import java.time.Duration;
import java.util.HashMap;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class TC01_Simpleform {

    public static String username = "kailashbangad13";
    public static String accesskey = "LT_zzcYSkD0pC39dk0WThXINcOSiaPX6C15MZItTk9wehQVdzl";

    public RemoteWebDriver driver = null;
    public boolean status = false;
    public WebDriverWait wait;

    @BeforeTest
    public void setUp() {
        try {
            ChromeOptions chromeOptions = new ChromeOptions();
            chromeOptions.setPlatformName("Windows 11");
            chromeOptions.setBrowserVersion("146.0");

            HashMap<String, Object> ltOptions = new HashMap<>();
            ltOptions.put("build", "LambdaTest");
            ltOptions.put("name", "Simple Form");
            ltOptions.put("visual", true);
            ltOptions.put("video", true);
            ltOptions.put("network", true);
            ltOptions.put("w3c", true);

            chromeOptions.setCapability("LT:Options", ltOptions);

            driver = new RemoteWebDriver(
                    new URL("https://" + username + ":" + accesskey + "@hub.lambdatest.com/wd/hub"),
                    chromeOptions
            );

            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
            driver.manage().window().maximize();

            wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSimpleForm() {

        try {
            driver.get("https://www.lambdatest.com/selenium-playground");

            // ✅ Always use By locator (NO WebElement storing)
            By simpleForm = By.xpath("//a[contains(text(),'Simple Form Demo')]");

            // ✅ Retry logic to avoid stale element
            for (int i = 0; i < 3; i++) {
                try {
                    wait.until(ExpectedConditions.elementToBeClickable(simpleForm)).click();
                    break;
                } catch (Exception e) {
                    System.out.println("Retrying click due to stale element...");
                }
            }

            // ✅ Wait for URL change properly
            wait.until(ExpectedConditions.urlContains("simple-form-demo"));
            Assert.assertTrue(driver.getCurrentUrl().contains("simple-form-demo"));

            // ✅ Enter message safely
            By messageBox = By.xpath("//input[@placeholder='Please enter your Message']");
            wait.until(ExpectedConditions.visibilityOfElementLocated(messageBox))
                    .sendKeys("Welcome to TestMu AI");

            // ✅ Click button
            driver.findElement(By.id("showInput")).click();

            // ✅ Validate output
            By output = By.id("message");
            String actualMessage = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(output)
            ).getText();

            Assert.assertEquals(actualMessage, "Welcome to TestMu AI");

            status = true;

        } catch (Exception e) {
            status = false;
            e.printStackTrace();
            Assert.fail("Test failed due to exception");
        }
    }

    @AfterTest
    public void tearDown() {
        if (driver != null) {
            ((JavascriptExecutor) driver).executeScript("lambda-status=" + status);
            driver.quit();
        }
    }
}