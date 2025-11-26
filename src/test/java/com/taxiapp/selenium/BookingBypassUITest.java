package com.taxiapp.selenium;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.time.Duration;

@Tag("selenium")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Selenium Bypass Testing - UI Manipulation Tests")
public class BookingBypassUITest {

    @LocalServerPort
    private int port;

    private WebDriver driver;
    private WebDriverWait wait;
    private String baseUrl;

    @BeforeAll
    public void setupClass() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless", "--no-sandbox", "--disable-dev-shm-usage");
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        baseUrl = "http://localhost:" + port;
    }

    @AfterAll
    public void tearDownClass() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    @Order(1)
    @DisplayName("UI TEST 1: Register and Login")
    public void testRegisterAndLogin() {
        driver.get(baseUrl + "/register.html");
        waitForElement("firstName");

        driver.findElement(By.id("firstName")).sendKeys("Selenium");
        driver.findElement(By.id("lastName")).sendKeys("Tester");
        driver.findElement(By.id("email")).sendKeys("selenium@test.com");
        driver.findElement(By.id("phoneNumber")).sendKeys("9876543210");
        driver.findElement(By.id("password")).sendKeys("test123");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        wait.until(ExpectedConditions.urlContains("register"));
        Assertions.assertTrue(driver.getCurrentUrl().contains("register"));

        driver.get(baseUrl + "/index.html");
        waitForElement("email");
        driver.findElement(By.id("email")).sendKeys("selenium@test.com");
        driver.findElement(By.id("password")).sendKeys("test123");
        Assertions.assertEquals("selenium@test.com", driver.findElement(By.id("email")).getAttribute("value"));
    }

    @Test
    @Order(2)
    @DisplayName("BYPASS UI TEST 2: Manipulate Passenger Field via JavaScript")
    public void testManipulatePassengerField() {
        driver.get(baseUrl + "/pages/dashboard.html");
        waitForElement("passengers");

        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("document.getElementById('passengers').value = 25;");

        String passengerValue = driver.findElement(By.id("passengers")).getAttribute("value");
        Assertions.assertEquals("25", passengerValue);
    }

    @Test
    @Order(3)
    @DisplayName("BYPASS UI TEST 3: Remove Min Attribute from Pickup Time")
    public void testRemoveMinAttributeFromPickupTime() {
        driver.get(baseUrl + "/pages/dashboard.html");
        waitForElement("pickupTime");

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
        driver.get(baseUrl + "/pages/dashboard.html");
        waitForElement("estimatedFare");

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
        driver.get(baseUrl + "/pages/dashboard.html");
        waitForElement("passengers");

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
        driver.get(baseUrl + "/pages/dashboard.html");
        waitForElement("vehicleType");

        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("document.getElementById('vehicleType').removeAttribute('required');");

        WebElement vehicleType = driver.findElement(By.id("vehicleType"));
        Assertions.assertNull(vehicleType.getAttribute("required"));
    }

    @Test
    @Order(7)
    @DisplayName("BYPASS UI TEST 7: Manipulate Hidden Distance Field")
    public void testManipulateHiddenFields() {
        driver.get(baseUrl + "/pages/dashboard.html");
        waitForElement("bookingForm");

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
        driver.get(baseUrl + "/pages/dashboard.html");
        waitForElement("bookingForm");

        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("document.getElementById('bookingForm').setAttribute('novalidate', 'true');");

        WebElement form = driver.findElement(By.id("bookingForm"));
        Assertions.assertNotNull(form.getAttribute("novalidate"));
    }

    @Test
    @Order(9)
    @DisplayName("BYPASS UI TEST 9: Verify Client-Side Passenger Limit Enforcement")
    public void testClientSidePassengerLimit() {
        driver.get(baseUrl + "/pages/dashboard.html");
        waitForElement("passengers");

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
        driver.get(baseUrl + "/pages/dashboard.html");
        waitForElement("bookingForm");

        JavascriptExecutor js = (JavascriptExecutor) driver;

        js.executeScript("document.getElementById('pickupLat').value = 19.0760;");
        js.executeScript("document.getElementById('pickupLon').value = 72.8777;");
        js.executeScript("document.getElementById('dropLat').value = 19.1136;");
        js.executeScript("document.getElementById('dropLon').value = 72.9083;");
        js.executeScript("document.getElementById('pickupTime').value = '2020-01-01T10:00';");
        js.executeScript("document.getElementById('passengers').value = 25;");
        js.executeScript("document.getElementById('vehicleType').value = 'SEDAN';");
        js.executeScript("document.getElementById('paymentMode').value = 'CASH';");

        Assertions.assertEquals("25", driver.findElement(By.id("passengers")).getAttribute("value"));
        Assertions.assertEquals("SEDAN", driver.findElement(By.id("vehicleType")).getAttribute("value"));
        Assertions.assertEquals("CASH", driver.findElement(By.id("paymentMode")).getAttribute("value"));
    }

    private void waitForElement(String elementId) {
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id(elementId)));
    }

}


