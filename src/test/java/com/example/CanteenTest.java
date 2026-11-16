package com.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;

public class CanteenTest {
    private WebDriver driver;

    @BeforeMethod
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        driver = new ChromeDriver(options);
    }

    @Test
    public void testCanteenHomePage() {
        File file = new File("src/main/resources/index.html");
        driver.get(file.toURI().toString());

        String title = driver.getTitle();
        Assert.assertEquals(title, "Campus Canteen Portal", "Page title does not match!");

        WebElement orderButton = driver.findElement(By.id("orderBtn"));
        Assert.assertTrue(orderButton.isDisplayed(), "Order button is missing!");
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}