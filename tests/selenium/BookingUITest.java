package com.taxiapp.selenium;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.jupiter.api.Assertions.*;

public class BookingUITest {

    private WebDriver driver;

    @BeforeEach
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "path/to/chromedriver");
        driver = new ChromeDriver();
        driver.get("http://localhost:8080/index.html");
    }

    @Test
    public void testValidBooking() {
        driver.findElement(By.id("loginEmail")).sendKeys("user@test.com");
        driver.findElement(By.id("loginPassword")).sendKeys("password123");
        driver.findElement(By.tagName("button")).click();

        driver.findElement(By.id("pickupLocation")).sendKeys("Location A");
        driver.findElement(By.id("dropLocation")).sendKeys("Location B");
        driver.findElement(By.id("pickupTime")).sendKeys("2025-12-25T10:00");
        driver.findElement(By.id("passengers")).sendKeys("2");
        driver.findElement(By.id("vehicleType")).sendKeys("sedan");
        driver.findElement(By.id("bookBtn")).click();

        WebElement successMessage = driver.findElement(By.tagName("body"));
        assertTrue(successMessage.getText().contains("Booking confirmed"));
    }

    @Test
    public void testInvalidPassengers() {
        driver.findElement(By.id("loginEmail")).sendKeys("user@test.com");
        driver.findElement(By.id("loginPassword")).sendKeys("password123");
        driver.findElement(By.tagName("button")).click();

        driver.findElement(By.id("pickupLocation")).sendKeys("Location A");
        driver.findElement(By.id("dropLocation")).sendKeys("Location B");
        driver.findElement(By.id("pickupTime")).sendKeys("2025-12-25T10:00");
        driver.findElement(By.id("passengers")).sendKeys("20");
        driver.findElement(By.id("vehicleType")).sendKeys("sedan");
        driver.findElement(By.id("bookBtn")).click();

        WebElement errorMessage = driver.findElement(By.tagName("body"));
        assertTrue(errorMessage.getText().contains("Invalid passengers"));
    }

    @Test
    public void testDisabledFareField() {
        // Test if fare field is disabled
        WebElement fareField = driver.findElement(By.id("fare"));
        assertTrue(fareField.getAttribute("disabled").equals("true"));
    }

    @Test
    public void testReenableDisabledButton() {
        // Bypass test: Re-enable disabled "Book Now" button
        WebElement bookButton = driver.findElement(By.id("bookBtn"));
        driver.executeScript("arguments[0].removeAttribute('disabled')", bookButton);
        bookButton.click();

        WebElement errorMessage = driver.findElement(By.tagName("body"));
        assertTrue(errorMessage.getText().contains("Invalid booking"));
    }

    @Test
    void testBookingButton() {
        driver.get("http://localhost:8080/book");
        WebElement button = driver.findElement(By.id("book-now"));
        button.click();
        // Assert expected behavior
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
    }
}
