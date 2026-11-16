package com.tests;

import org.openqa.selenium.Alert;
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
import java.util.List;

public class CanteenTest {
    private WebDriver driver;

    @BeforeMethod
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        driver = new ChromeDriver(options);

        File file = new File("src/main/java/com/example/index.html");
        driver.get(file.toURI().toString());
    }

    @Test(priority = 1)
    public void testAllMenuItemsAndPriceAddition() {
        // 1. Verify title
        Assert.assertEquals(driver.getTitle(), "Campus Canteen Portal");

        // Grab all 3 "Add" buttons in order: [0] Orange Juice, [1] Super Burger, [2] Cheese Sandwich
        List<WebElement> addButtons = driver.findElements(By.cssSelector("button.add-btn"));
        Assert.assertEquals(addButtons.size(), 3, "Expected 3 menu items!");

        WebElement totalPrice = driver.findElement(By.id("totalPrice"));

        // 2. Add Orange Juice (₹20) -> Total = 20
        addButtons.get(0).click();
        Assert.assertEquals(totalPrice.getText(), "20", "Orange Juice price mismatch!");

        // 3. Add Super Burger(Veg) (₹100) -> Cumulative Total = 120
        addButtons.get(1).click();
        Assert.assertEquals(totalPrice.getText(), "120", "Burger addition failed!");

        // 4. Add Cheese Sandwich (₹50) -> Cumulative Total = 170
        addButtons.get(2).click();
        Assert.assertEquals(totalPrice.getText(), "170", "Cheese Sandwich addition failed!");

        // 5. Add Orange Juice a second time (₹20) -> Cumulative Total = 190
        addButtons.get(0).click();
        Assert.assertEquals(totalPrice.getText(), "190", "Repeat item addition failed!");

        // 6. Verify 4 items appear in the cart list
        List<WebElement> cartEntries = driver.findElements(By.cssSelector("#cartItems li"));
        Assert.assertEquals(cartEntries.size(), 4, "Cart item count mismatch!");
    }

    @Test(priority = 2)
    public void testClearCartButton() {
        List<WebElement> addButtons = driver.findElements(By.cssSelector("button.add-btn"));
        WebElement totalPrice = driver.findElement(By.id("totalPrice"));

        // Add Super Burger (₹100) + Cheese Sandwich (₹50) = 150
        addButtons.get(1).click();
        addButtons.get(2).click();
        Assert.assertEquals(totalPrice.getText(), "150");

        // Click "Clear Cart" button
        WebElement clearButton = driver.findElement(By.cssSelector("button.clear-btn"));
        clearButton.click();

        // Verify total resets to 0 and "Cart is empty" returns
        Assert.assertEquals(totalPrice.getText(), "0", "Total did not reset to 0!");
        String emptyMsg = driver.findElement(By.cssSelector("#cartItems li")).getText();
        Assert.assertEquals(emptyMsg, "Cart is empty", "Empty cart message missing!");
    }

    @Test(priority = 3)
    public void testCheckoutButtonAlert() {
        // Add Cheese Sandwich (₹50)
        driver.findElements(By.cssSelector("button.add-btn")).get(2).click();

        // Click "Checkout" button (#orderBtn) and verify JS alert text
        driver.findElement(By.id("orderBtn")).click();

        Alert alert = driver.switchTo().alert();
        Assert.assertEquals(alert.getText(), "Order Placed Successfully!");
        alert.accept(); // Close the alert
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}