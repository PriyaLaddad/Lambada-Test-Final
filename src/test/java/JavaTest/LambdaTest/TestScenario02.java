package JavaTest.LambdaTest;

import java.net.URL;
import java.time.Duration;
import java.util.HashMap;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TestScenario02 {

	public static String username = "priyartoshniwal09";
	public static String accesskey = "LT_dFS8UUR3760AJ0Ir3mCl0QpF3Frc0kMPqWB90bE8g90TPne";
	// static String gridURL = "@hub.lambdatest.com/wd/hub";

	public static void main(String[] args) throws Exception {

		String hub = "https://" + username + ":" + accesskey + "@hub.lambdatest.com/wd/hub";

		ChromeOptions options = new ChromeOptions();
		options.setPlatformName("Windows 11");
		options.setBrowserVersion("latest");

		HashMap<String, Object> ltOptions = new HashMap<>();
		ltOptions.put("project", "Slider Test");
		ltOptions.put("build", "Drag");
		ltOptions.put("name", "Drag and slide");
		ltOptions.put("w3c", true);

		options.setCapability("LT:Options", ltOptions);

		WebDriver driver = new RemoteWebDriver(new URL(hub), options);

		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(60));

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

		// Open URL
		driver.get("https://www.testmuai.com/selenium-playground/");

		// Click menu
		wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Drag & Drop Sliders"))).click();

		// Ensure slider is present (no storing)
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@value='15']")));

		Actions actions = new Actions(driver);

		try {
			actions.dragAndDropBy(driver.findElement(By.xpath("//input[@value='15']")), 240, 0).perform();
		} catch (StaleElementReferenceException e) {

			// retry same step again
			actions.dragAndDropBy(driver.findElement(By.xpath("//input[@value='15']")), 240, 0).perform();
		}

		// Get value
		String value = driver.findElement(By.xpath("//output[@id='rangeSuccess']")).getText();

		if (value.equals("95")) {
			System.out.println("Test Passed: Slider value is 95");
		} else {
			System.out.println("Test Failed: Current value is " + value);
		}

		driver.quit();
	}
}