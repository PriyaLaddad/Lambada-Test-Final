package JavaTest.LambdaTest;

import java.net.URL;
import java.time.Duration;
import java.util.HashMap;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariOptions;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class TC02_Slider {

	public static String username = "priyartoshniwal09";
	public static String accesskey = "LT_dFS8UUR3760AJ0Ir3mCl0QpF3Frc0kMPqWB90bE8g90TPne";

	public RemoteWebDriver driver;
	public WebDriverWait wait;
	public boolean status = false;

	@BeforeTest
	public void setUp() {
		try {
			String hub = "https://" + username + ":" + accesskey + "@hub.lambdatest.com/wd/hub";

			/*
			 * ChromeOptions options = new ChromeOptions();
			 * options.setPlatformName("Windows 11"); options.setBrowserVersion("latest");
			 */
			SafariOptions options = new SafariOptions();
			options.setPlatformName("macOS Ventura");
			options.setBrowserVersion("latest");

			HashMap<String, Object> ltOptions = new HashMap<>();
			ltOptions.put("project", "Slider Test");
			ltOptions.put("build", "Drag");
			ltOptions.put("name", "Drag and slide");
			ltOptions.put("w3c", true);

			options.setCapability("LT:Options", ltOptions);

			driver = new RemoteWebDriver(new URL(hub), options);

			driver.manage().window().maximize();
			driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0)); // ❗ avoid mixing waits

			wait = new WebDriverWait(driver, Duration.ofSeconds(20));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testSlider() {

		try {
			driver.get("https://www.testmuai.com/selenium-playground/");

			// Click menu
			wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Drag & Drop Sliders"))).click();

			// Wait for slider
			By slider = By.xpath("//input[@value='15']");
			wait.until(ExpectedConditions.presenceOfElementLocated(slider));

			Actions actions = new Actions(driver);

			// ✅ Retry logic for stale element
			for (int i = 0; i < 2; i++) {
				try {
					actions.dragAndDropBy(driver.findElement(slider), 240, 0).perform();
					break;
				} catch (StaleElementReferenceException e) {
					System.out.println("Retrying slider due to stale element...");
				}
			}

			// Validate value
			By output = By.id("rangeSuccess");
			String value = wait.until(ExpectedConditions.visibilityOfElementLocated(output)).getText();

			Assert.assertEquals(value, "95", "Slider value mismatch!");

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