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
    public void testCanteenCartFunctionality() {
        // Load local page
        File file = new File("src/main/java/com/example/index.html");
        driver.get(file.toURI().toString());

        // 1. Verify title
        Assert.assertEquals(driver.getTitle(), "Campus Canteen Portal");

        // 2. Find the first "Add" button (for Masala Chai) and click it
        WebElement addButton = driver.findElement(By.xpath("//button[text()='Add']"));
        addButton.click();

        // 3. Verify total price updates to 20
        WebElement totalPrice = driver.findElement(By.id("totalPrice"));
        Assert.assertEquals(totalPrice.getText(), "20", "Cart total calculation failed!");
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}