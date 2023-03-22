package test;

import driver.driverFactory;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.List;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

public class TestCase3 {
    @Test
    public void TestCase3() throws IOException, InterruptedException {
        // 1. Goto https://didongthongminh.vn/
        WebDriver driver = driverFactory.getFirefoxDriver();
        driver.get("https://didongthongminh.vn/");

        // 2. Click on Điện thoại menu
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector(".slide_banner > div:nth-child(1) > div:nth-child(1) > ul:nth-child(1) > li:nth-child(1)")))).click();

        // 3. Thêm sản phẩm vào giỏ hàng
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector("div.col-sm-5ths:nth-child(1) > div:nth-child(1) > a:nth-child(1) > div:nth-child(1) > div:nth-child(3)")))).click();
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector(" .add_cart > button:nth-child(1)")))).click();

        // 4. Thay đổi số lượng sản phẩm
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector("div.product-cart1:nth-child(1) > div:nth-child(1) > div:nth-child(3) > div:nth-child(2) > span:nth-child(1) > button:nth-child(3)")))).click();

        //5. Xác nhận giá trị sau khi nhập trên input trong giỏ hàng
        String actualQtyValue = wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector(".product-price > div:nth-child(2) > span:nth-child(1) > input:nth-child(2)")))).getAttribute("value");
        String expectedQtyValue = "2";
        assertEquals(actualQtyValue, expectedQtyValue);

        //6. Then click on �EMPTY CART� link in the footer of list of all mobiles. A message "SHOPPING CART IS EMPTY" is shown.
        //7. Verify cart is empty

        driver.quit();
    }
}
