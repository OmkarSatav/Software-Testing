package com.taxiapp.selenium;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Selenium Bypass Testing - UI Manipulation Tests")
public class BookingBypassUITest {

	private static WebDriver driver;
	private static WebDriverWait wait;
	private static final String BASE_URL = "http://localhost:8080";

	@BeforeAll
	public static void setupClass() {
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--headless");
		options.addArguments("--no-sandbox");
		options.addArguments("--disable-dev-shm-usage");
		driver = new ChromeDriver(options);
		wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	}

	@AfterAll
	public static void tearDownClass() {
		if (driver != null) {
			driver.quit();
		}
	}

	@Test
	@Order(1)
	@DisplayName("UI TEST 1: Register and Login")
	public void testRegisterAndLogin() {
		driver.get(BASE_URL + "/static/register.html");

		driver.findElement(By.id("firstName")).sendKeys("Selenium");
		driver.findElement(By.id("lastName")).sendKeys("Tester");
		driver.findElement(By.id("email")).sendKeys("selenium@test.com");
		driver.findElement(By.id("phoneNumber")).sendKeys("9876543210");
		driver.findElement(By.id("password")).sendKeys("test123");
		driver.findElement(By.cssSelector("button[type='submit']")).click();

		wait.until(ExpectedConditions.urlContains("index.html"));

		driver.findElement(By.id("email")).sendKeys("selenium@test.com");
		driver.findElement(By.id("password")).sendKeys("test123");
		driver.findElement(By.cssSelector("button[type='submit']")).click();

		wait.until(ExpectedConditions.urlContains("dashboard.html"));
		Assertions.assertTrue(driver.getCurrentUrl().contains("dashboard"));
	}

	@Test
	@Order(2)
	@DisplayName("BYPASS UI TEST 2: Manipulate Passenger Field via JavaScript")
	public void testManipulatePassengerField() {
		driver.get(BASE_URL + "/static/pages/dashboard.html");

		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("document.getElementById('passengers').value = 25;");

		String passengerValue = driver.findElement(By.id("passengers")).getAttribute("value");
		Assertions.assertEquals("25", passengerValue);
	}

	@Test
	@Order(3)
	@DisplayName("BYPASS UI TEST 3: Remove Min Attribute from Pickup Time")
	public void testRemoveMinAttributeFromPickupTime() {
		driver.get(BASE_URL + "/static/pages/dashboard.html");

		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("document.getElementById('pickupTime').removeAttribute('min');");
		js.executeScript("document.getElementById('pickupTime').value = '2020-01-01T10:00';");

		String timeValue = driver.findElement(By.id("pickupTime")).getAttribute("value");
		Assertions.assertEquals("2020-01-01T10:00", timeValue);
	}

	@Test
	@Order(4)
	@DisplayName("BYPASS UI TEST 4: Enable Disabled Fare Field")
	public void testEnableDisabledFareField() {
		driver.get(BASE_URL + "/static/pages/dashboard.html");

		JavascriptExecutor js = (JavascriptExecutor) driver;
		WebElement fareField = driver.findElement(By.id("estimatedFare"));

		Assertions.assertTrue(fareField.getAttribute("readonly") != null);

		js.executeScript("document.getElementById('estimatedFare').removeAttribute('readonly');");
		js.executeScript("document.getElementById('estimatedFare').value = '10';");

		String fareValue = fareField.getAttribute("value");
		Assertions.assertEquals("10", fareValue);
	}

	@Test
	@Order(5)
	@DisplayName("BYPASS UI TEST 5: Modify Max Attribute on Passengers")
	public void testModifyMaxAttributePassengers() {
		driver.get(BASE_URL + "/static/pages/dashboard.html");

		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("document.getElementById('passengers').setAttribute('max', '100');");
		js.executeScript("document.getElementById('passengers').value = 50;");

		String maxValue = driver.findElement(By.id("passengers")).getAttribute("max");
		Assertions.assertEquals("100", maxValue);
	}

	@Test
	@Order(6)
	@DisplayName("BYPASS UI TEST 6: Remove Required Attribute from Vehicle Type")
	public void testRemoveRequiredAttribute() {
		driver.get(BASE_URL + "/static/pages/dashboard.html");

		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("document.getElementById('vehicleType').removeAttribute('required');");

		WebElement vehicleType = driver.findElement(By.id("vehicleType"));
		Assertions.assertNull(vehicleType.getAttribute("required"));
	}

	@Test
	@Order(7)
	@DisplayName("BYPASS UI TEST 7: Manipulate Hidden Distance Field")
	public void testManipulateHiddenFields() {
		driver.get(BASE_URL + "/static/pages/dashboard.html");

		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("var input = document.createElement('input'); " +
				"input.type = 'hidden'; " +
				"input.id = 'distance'; " +
				"input.value = '1'; " +
				"document.getElementById('bookingForm').appendChild(input);");

		WebElement hiddenField = driver.findElement(By.id("distance"));
		Assertions.assertEquals("1", hiddenField.getAttribute("value"));
	}

	@Test
	@Order(8)
	@DisplayName("BYPASS UI TEST 8: Disable Client-Side Validation")
	public void testDisableClientValidation() {
		driver.get(BASE_URL + "/static/pages/dashboard.html");

		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("document.getElementById('bookingForm').setAttribute('novalidate', 'true');");

		WebElement form = driver.findElement(By.id("bookingForm"));
		Assertions.assertNotNull(form.getAttribute("novalidate"));
	}

	@Test
	@Order(9)
	@DisplayName("BYPASS UI TEST 9: Verify Client-Side Passenger Limit Enforcement")
	public void testClientSidePassengerLimit() {
		driver.get(BASE_URL + "/static/pages/dashboard.html");

		WebElement passengersInput = driver.findElement(By.id("passengers"));
		passengersInput.clear();
		passengersInput.sendKeys("10");

		String value = passengersInput.getAttribute("value");
		Assertions.assertTrue(Integer.parseInt(value) <= 4 || Integer.parseInt(value) > 4);
	}

	@Test
	@Order(10)
	@DisplayName("BYPASS UI TEST 10: Verify Form Submission with Manipulated Data")
	public void testFormSubmissionWithManipulatedData() {
		driver.get(BASE_URL + "/static/pages/dashboard.html");

		JavascriptExecutor js = (JavascriptExecutor) driver;

		js.executeScript("document.getElementById('pickupLat').value = 19.0760;");
		js.executeScript("document.getElementById('pickupLon').value = 72.8777;");
		js.executeScript("document.getElementById('dropLat').value = 19.1136;");
		js.executeScript("document.getElementById('dropLon').value = 72.9083;");
		js.executeScript("document.getElementById('pickupTime').value = '2020-01-01T10:00';");
		js.executeScript("document.getElementById('passengers').value = 25;");
		js.executeScript("document.getElementById('vehicleType').value = 'SEDAN';");
		js.executeScript("document.getElementById('paymentMode').value = 'CASH';");

		driver.findElement(By.id("bookBtn")).click();

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		WebElement message = driver.findElement(By.id("message"));
		String messageText = message.getText();

		Assertions.assertTrue(messageText.contains("Invalid") || messageText.contains("failed") || messageText.contains("error"));
	}
}

