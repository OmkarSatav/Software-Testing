package com.taxiapp.selenium;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.jupiter.api.Assertions.*;

public class AdminBypassTest {

    private WebDriver driver;

    @BeforeEach
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "path/to/chromedriver");
        driver = new ChromeDriver();
        driver.get("http://localhost:8080/admin.html");
    }

    @Test
    public void attemptRoleSpoof() throws InterruptedException {
        driver.executeScript("localStorage.setItem('role','ADMIN')");
        driver.navigate().refresh();
        boolean canSeeAdmin = driver.getPageSource().contains("Block User");
        assertTrue(canSeeAdmin);
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
    }
}

