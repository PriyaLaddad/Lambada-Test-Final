package JavaTest.LambdaTest;

import java.net.URL;
import java.time.Duration;
import java.util.HashMap;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

public class TestScenario01 {
	public static String username = "priyartoshniwal09";
	public static String accesskey = "LT_dFS8UUR3760AJ0Ir3mCl0QpF3Frc0kMPqWB90bE8g90TPne";
	public static RemoteWebDriver driver = null;
	// static String gridURL = "@hub.lambdatest.com/wd/hub";
	boolean status = false;

	public static void main(String[] args) {
		new TestScenario01().test();
	}

	public void test() {
		// To Setup driver
		setUp();

	}

	public void setUp() {

		ChromeOptions chromeOptions = new ChromeOptions();
		chromeOptions.setPlatformName("Windows 11");
		chromeOptions.setBrowserVersion("146.0");
		HashMap<String, Object> ltOptions = new HashMap<String, Object>();
		ltOptions.put("build", "LamdaTest");
		ltOptions.put("name", "Simple form");
		ltOptions.put("visual", true);
		ltOptions.put("video", true);
		ltOptions.put("network", true);
		ltOptions.put("w3c", true);
		chromeOptions.setCapability("LT:Options", ltOptions);

		try {

			driver = new RemoteWebDriver(
					new URL("https://" + username + ":" + accesskey + "@hub.lambdatest.com/wd/hub"), chromeOptions);

			System.out.println("Navigating to Input Field section");
			driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
			driver.manage().window().maximize();
			driver.get("https://www.lambdatest.com/selenium-playground");
			// Click "Simple Form Demo

			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
			WebElement simpleFormLink = wait.until(ExpectedConditions
					.elementToBeClickable(By.xpath("//div[@class='container__selenium']//ul//li[34]")));

			boolean b = driver.findElement(By.xpath("//a[contains(text(),'Simple Form Demo')]")).isDisplayed();

			System.out.println(b);
			Assert.assertTrue(b);

			driver.findElement(By.xpath("//a[contains(text(),'Simple Form Demo')]")).click();

			// valid the url is Simple form demo which clicked
			ExpectedConditions.urlContains("simple-form-demo");
			String currentUrl = driver.getCurrentUrl();
			Assert.assertTrue(currentUrl.contains("simple-form-demo"));

			System.out.println(currentUrl);

			// Step 4 & 5 : Create variable and enter value in the Enter message and
			// validate input messsage

			boolean b1 = driver.findElement(By.xpath("//input[@placeholder='Please enter your Message']"))
					.isDisplayed();
			System.out.println(b1);

			String textMessage = "Welcome to TestMu AI";
			try {
				WebElement textbox = wait.until(ExpectedConditions
						.elementToBeClickable(By.xpath("//input[@placeholder='Please enter your Message']")));
				driver.findElement(By.xpath("//input[@placeholder='Please enter your Message']")).sendKeys(textMessage);
				Thread.sleep(3000);
				driver.findElement(By.xpath("//button[@id='showInput']")).click();
			} catch (Exception e) {
				e.printStackTrace();
			}

			String yourMessage = driver.findElement(By.id("message")).getText();

			Assert.assertEquals(yourMessage, "Welcome to TestMu AI",
					"Message is incorrect. Expected : Welcome to LambdaTest but Found : " + yourMessage);

		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			tearDown();
		}
	}

	private void tearDown() {
		if (driver != null) {
			((JavascriptExecutor) driver).executeScript("lambda-status=" + status);
			driver.quit(); // really important statement for preventing your test execution from a timeout.
		}
	}

}
