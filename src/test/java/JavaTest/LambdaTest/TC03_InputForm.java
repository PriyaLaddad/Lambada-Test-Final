package JavaTest.LambdaTest;

import java.net.URL;
import java.time.Duration;
import java.util.HashMap;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class TC03_InputForm {

    public static String username = "priyartoshniwal09";
    public static String accesskey = "LT_dFS8UUR3760AJ0Ir3mCl0QpF3Frc0kMPqWB90bE8g90TPne";

    public RemoteWebDriver driver;
    public WebDriverWait wait;
    public boolean status = false;

    @BeforeTest
    public void setUp() {
        try {
            ChromeOptions browserOptions = new ChromeOptions();
            browserOptions.setPlatformName("Windows 11");
            browserOptions.setBrowserVersion("latest");

            HashMap<String, Object> ltOptions = new HashMap<>();
            ltOptions.put("build", "Input Form");
            ltOptions.put("name", "Input Form Submit");
            ltOptions.put("visual", true);
            ltOptions.put("video", true);
            ltOptions.put("network", true);
            ltOptions.put("w3c", true);

            browserOptions.setCapability("LT:Options", ltOptions);

            driver = new RemoteWebDriver(
                    new URL("https://" + username + ":" + accesskey + "@hub.lambdatest.com/wd/hub"),
                    browserOptions
            );

            driver.manage().window().maximize();
            wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testInputFormSubmit() {

        try {
            // Step 1: Open Playground
            driver.get("https://www.lambdatest.com/selenium-playground");

            By inputForm = By.xpath("//a[text()='Input Form Submit']");
            wait.until(ExpectedConditions.refreshed(ExpectedConditions.elementToBeClickable(inputForm))).click();

            wait.until(ExpectedConditions.urlContains("input-form-demo"));
            System.out.println("Navigated to Input Form Submit");

            // Step 2: Click Submit without filling
            By submitBtn = By.xpath("//button[text()='Submit']");
            wait.until(ExpectedConditions.elementToBeClickable(submitBtn)).click();
            System.out.println("Clicked Submit without data");

            // Step 3: Validate HTML5 error message
            String validationMessage = driver.findElement(By.id("name")).getAttribute("validationMessage");
            System.out.println("Validation message: " + validationMessage);
            Assert.assertTrue(validationMessage.contains("Please fill"));
            System.out.println("Validation message verified");

            // Step 4 & 5: Fill form and select country
            driver.findElement(By.id("name")).sendKeys("John Doe");
            driver.findElement(By.id("inputEmail4")).sendKeys("john@test.com");
            driver.findElement(By.id("inputPassword4")).sendKeys("Password123");
            driver.findElement(By.id("company")).sendKeys("ABC Pvt Ltd");
            driver.findElement(By.id("websitename")).sendKeys("https://abc.com");

            new Select(driver.findElement(By.name("country"))).selectByVisibleText("United States");

            driver.findElement(By.id("inputCity")).sendKeys("New York");
            driver.findElement(By.id("inputAddress1")).sendKeys("Street 1");
            driver.findElement(By.id("inputAddress2")).sendKeys("Street 2");
            driver.findElement(By.id("inputState")).sendKeys("NY");
            driver.findElement(By.id("inputZip")).sendKeys("10001");

            System.out.println("Form filled and country selected");

            // Step 6: Submit form
            wait.until(ExpectedConditions.elementToBeClickable(submitBtn)).click();
            System.out.println("Form submitted");

            // Step 7: Validate success message
            By successLocator = By.xpath("//p[contains(@class,'success-msg')]");
            String successMsg = wait.until(ExpectedConditions.refreshed(
                    ExpectedConditions.visibilityOfElementLocated(successLocator)
            )).getText();

            System.out.println("Success message: " + successMsg);
            Assert.assertEquals(successMsg, "Thanks for contacting us, we will get back to you shortly.");
            System.out.println("Success message validated");

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