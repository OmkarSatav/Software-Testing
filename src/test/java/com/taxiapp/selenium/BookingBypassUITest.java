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
@DisplayName("Selenium Bypass Testing - Client-Side Validation Bypass + UI Manipulation")
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
        baseUrl = "http://localhost:" + 8080;
    }

    @AfterAll
    public void tearDownClass() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    @Order(1)
    @DisplayName("UI BYPASS 1: Register + Login with INVALID email by disabling HTML5 validation")
    public void testRegisterAndLoginWithInvalidEmailBypassingValidation() {
        // === Step 1: Register with invalid email ===
        driver.get(baseUrl + "/register.html");
        waitForElement("registerForm");

        JavascriptExecutor js = (JavascriptExecutor) driver;
        WebElement registerForm = driver.findElement(By.id("registerForm"));
        WebElement submitButton = driver.findElement(By.cssSelector("#registerForm button[type='submit']"));

        js.executeScript("arguments[0].setAttribute('novalidate','true');", registerForm);

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        driver.findElement(By.id("firstName")).sendKeys("Selenium");
        driver.findElement(By.id("lastName")).sendKeys("Tester");
        driver.findElement(By.id("email")).sendKeys("seleniumtest@com");
        driver.findElement(By.id("phoneNumber")).sendKeys("9876543210");
        driver.findElement(By.id("password")).sendKeys("test123");

        submitButton.click();

        wait.until(ExpectedConditions.urlContains("register"));

        // === Step 2: Login with same INVALID email, again bypassing HTML validation ===
        driver.get(baseUrl + "/index.html");
        waitForElement("loginForm");

        WebElement loginForm = driver.findElement(By.id("loginForm"));
        WebElement loginButton = driver.findElement(By.cssSelector("#loginForm button[type='submit']"));

        js.executeScript("arguments[0].setAttribute('novalidate','true');", loginForm);

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        driver.findElement(By.id("email")).clear();
        driver.findElement(By.id("email")).sendKeys("seleniumtest@com");
        driver.findElement(By.id("password")).sendKeys("test123");

        loginButton.click();

        wait.until(ExpectedConditions.not(
                ExpectedConditions.urlToBe(baseUrl + "/index.html")
        ));
    }

    @Test
    @Order(2)
    @DisplayName("UI BYPASS 2: Submit booking with 25 passengers (bypass min/max and validation)")
    public void testBookingWithExcessivePassengersBypassingValidation() {
        driver.get(baseUrl + "/pages/dashboard.html");
        waitForElement("bookingForm");

        JavascriptExecutor js = (JavascriptExecutor) driver;

        js.executeScript("document.getElementById('bookingForm').setAttribute('novalidate','true');");

        js.executeScript("var p = document.getElementById('passengers'); " +
                "p.removeAttribute('min'); p.removeAttribute('max'); p.value = 25;");

        js.executeScript("document.getElementById('pickupLat').value = 19.0760;");
        js.executeScript("document.getElementById('pickupLon').value = 72.8777;");
        js.executeScript("document.getElementById('dropLat').value = 19.1136;");
        js.executeScript("document.getElementById('dropLon').value = 72.9083;");
        js.executeScript("document.getElementById('pickupTime').value = '2030-01-01T10:00';");
        js.executeScript("document.getElementById('vehicleType').value = 'SEDAN';");
        js.executeScript("document.getElementById('paymentMode').value = 'CASH';");

        Assertions.assertEquals("25",
                driver.findElement(By.id("passengers")).getAttribute("value"));

        WebElement form = driver.findElement(By.id("bookingForm"));
        form.submit();

        wait.until(ExpectedConditions.or(
                ExpectedConditions.urlContains("dashboard"),
                ExpectedConditions.urlContains("bookings"),
                ExpectedConditions.presenceOfElementLocated(By.id("message"))
        ));
    }

    @Test
    @Order(3)
    @DisplayName("UI BYPASS 3: Submit booking with PAST pickup time")
    public void testBookingWithPastPickupTimeBypassingValidation() {
        driver.get(baseUrl + "/pages/dashboard.html");
        waitForElement("bookingForm");

        JavascriptExecutor js = (JavascriptExecutor) driver;

        js.executeScript("document.getElementById('bookingForm').setAttribute('novalidate','true');");

        js.executeScript("document.getElementById('pickupLat').value = 19.0760;");
        js.executeScript("document.getElementById('pickupLon').value = 72.8777;");
        js.executeScript("document.getElementById('dropLat').value = 19.1136;");
        js.executeScript("document.getElementById('dropLon').value = 72.9083;");

        js.executeScript("document.getElementById('pickupTime').value = '2020-01-01T10:00';");

        js.executeScript("document.getElementById('passengers').value = 2;");
        js.executeScript("document.getElementById('vehicleType').value = 'SEDAN';");
        js.executeScript("document.getElementById('paymentMode').value = 'CASH';");

        WebElement form = driver.findElement(By.id("bookingForm"));
        form.submit();

        wait.until(ExpectedConditions.or(
                ExpectedConditions.urlContains("dashboard"),
                ExpectedConditions.urlContains("bookings"),
                ExpectedConditions.presenceOfElementLocated(By.id("message"))
        ));
    }

    @Test
    @Order(4)
    @DisplayName("UI BYPASS 4: Submit booking with ZERO passengers")
    public void testBookingWithZeroPassengersBypassingValidation() {
        driver.get(baseUrl + "/pages/dashboard.html");
        waitForElement("bookingForm");

        JavascriptExecutor js = (JavascriptExecutor) driver;

        js.executeScript("document.getElementById('bookingForm').setAttribute('novalidate','true');");

        js.executeScript("var p = document.getElementById('passengers'); " +
                "p.removeAttribute('min'); p.value = 0;");

        js.executeScript("document.getElementById('pickupLat').value = 19.0760;");
        js.executeScript("document.getElementById('pickupLon').value = 72.8777;");
        js.executeScript("document.getElementById('dropLat').value = 19.1136;");
        js.executeScript("document.getElementById('dropLon').value = 72.9083;");
        js.executeScript("document.getElementById('pickupTime').value = '2030-01-01T10:00';");
        js.executeScript("document.getElementById('vehicleType').value = 'SEDAN';");
        js.executeScript("document.getElementById('paymentMode').value = 'CASH';");

        Assertions.assertEquals("0",
                driver.findElement(By.id("passengers")).getAttribute("value"));

        WebElement form = driver.findElement(By.id("bookingForm"));
        form.submit();

        wait.until(ExpectedConditions.or(
                ExpectedConditions.urlContains("dashboard"),
                ExpectedConditions.urlContains("bookings"),
                ExpectedConditions.presenceOfElementLocated(By.id("message"))
        ));
    }

    @Test
    @Order(5)
    @DisplayName("UI BYPASS 5: Submit booking with NEGATIVE passengers")
    public void testBookingWithNegativePassengersBypassingValidation() {
        driver.get(baseUrl + "/pages/dashboard.html");
        waitForElement("bookingForm");

        JavascriptExecutor js = (JavascriptExecutor) driver;

        js.executeScript("document.getElementById('bookingForm').setAttribute('novalidate','true');");

        js.executeScript("var p = document.getElementById('passengers'); " +
                "p.removeAttribute('min'); p.value = -5;");

        js.executeScript("document.getElementById('pickupLat').value = 19.0760;");
        js.executeScript("document.getElementById('pickupLon').value = 72.8777;");
        js.executeScript("document.getElementById('dropLat').value = 19.1136;");
        js.executeScript("document.getElementById('dropLon').value = 72.9083;");
        js.executeScript("document.getElementById('pickupTime').value = '2030-01-01T10:00';");
        js.executeScript("document.getElementById('vehicleType').value = 'SEDAN';");
        js.executeScript("document.getElementById('paymentMode').value = 'CASH';");

        Assertions.assertEquals("-5",
                driver.findElement(By.id("passengers")).getAttribute("value"));

        WebElement form = driver.findElement(By.id("bookingForm"));
        form.submit();

        wait.until(ExpectedConditions.or(
                ExpectedConditions.urlContains("dashboard"),
                ExpectedConditions.urlContains("bookings"),
                ExpectedConditions.presenceOfElementLocated(By.id("message"))
        ));
    }

    @Test
    @Order(6)
    @DisplayName("UI BYPASS 6: Submit booking with DROP outside service area")
    public void testBookingWithOutsideServiceAreaBypassingValidation() {
        driver.get(baseUrl + "/pages/dashboard.html");
        waitForElement("bookingForm");

        JavascriptExecutor js = (JavascriptExecutor) driver;

        js.executeScript("document.getElementById('bookingForm').setAttribute('novalidate','true');");

        js.executeScript("document.getElementById('pickupLat').value = 19.0760;");
        js.executeScript("document.getElementById('pickupLon').value = 72.8777;");

        js.executeScript("document.getElementById('dropLat').value = 28.6139;");
        js.executeScript("document.getElementById('dropLon').value = 77.2090;");

        js.executeScript("document.getElementById('pickupTime').value = '2030-01-01T10:00';");
        js.executeScript("document.getElementById('passengers').value = 2;");
        js.executeScript("document.getElementById('vehicleType').value = 'SEDAN';");
        js.executeScript("document.getElementById('paymentMode').value = 'CASH';");

        WebElement form = driver.findElement(By.id("bookingForm"));
        form.submit();

        wait.until(ExpectedConditions.or(
                ExpectedConditions.urlContains("dashboard"),
                ExpectedConditions.urlContains("bookings"),
                ExpectedConditions.presenceOfElementLocated(By.id("message"))
        ));
    }

    @Test
    @Order(7)
    @DisplayName("UI BYPASS 7: Enable and override readonly Estimated Fare field via JavaScript")
    public void testEnableAndManipulateEstimatedFareField() {
        driver.get(baseUrl + "/pages/dashboard.html");
        waitForElement("estimatedFare");

        JavascriptExecutor js = (JavascriptExecutor) driver;
        WebElement fareField = driver.findElement(By.id("estimatedFare"));

        Assertions.assertNotNull(fareField.getAttribute("readonly"));

        js.executeScript("document.getElementById('estimatedFare').removeAttribute('readonly');");
        js.executeScript("document.getElementById('estimatedFare').value = '10';");

        String fareValue = fareField.getAttribute("value");
        Assertions.assertEquals("10", fareValue);
    }

    @Test
    @Order(8)
    @DisplayName("UI BYPASS 8: Disable client-side validation globally with novalidate")
    public void testDisableClientSideValidationFlag() {
        driver.get(baseUrl + "/pages/dashboard.html");
        waitForElement("bookingForm");

        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("document.getElementById('bookingForm').setAttribute('novalidate', 'true');");

        WebElement form = driver.findElement(By.id("bookingForm"));
        Assertions.assertNotNull(form.getAttribute("novalidate"));
    }


    private void waitForElement(String elementId) {
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id(elementId)));
    }
}