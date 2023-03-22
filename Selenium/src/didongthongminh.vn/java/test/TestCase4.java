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
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.testng.AssertJUnit.assertEquals;

public class TestCase4 {
    @Test
    public void TestCase4() throws IOException, InterruptedException {

        // 1. Goto https://didongthongminh.vn/
        WebDriver driver = driverFactory.getFirefoxDriver();
        driver.get("https://didongthongminh.vn/");

        // 2. Click on Điện thoại menu
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector(".slide_banner > div:nth-child(1) > div:nth-child(1) > ul:nth-child(1) > li:nth-child(1)")))).click();

        //  3. Thêm 2 sản phẩm vào danh sách so sánh
        Thread.sleep(1000);
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector(".compare67030")))).click();
        Thread.sleep(1000);
        driver.findElement(By.cssSelector(".compare67054 > span:nth-child(2)")).click();

        //  4. Nhấn vào nút so sánh
        Thread.sleep(1000);
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector(".compare_all")))).click();

        //  5.Check that the products are reflected in it
        // Kiểm tra sản phẩm trong danh sách so sánh
        List<WebElement> productList = driver.findElements(By.cssSelector(".product"));
        List<String> expectedProducts = new ArrayList<String>();
        expectedProducts.add("iPhone 12 Pro 128GB Cũ Nguyên Bản Đẹp Như Mới");
        expectedProducts.add("iPhone 12 Pro Max Cũ Đẹp 256GB");
        List<String> actualProducts = new ArrayList<String>();
        for (WebElement product : productList) {
            actualProducts.add(product.getAttribute("title"));
        }

        // So sánh danh sách sản phẩm
        assertEquals(expectedProducts, actualProducts);
        //  6. Close the Popup Windows
        driver.quit();
    }
}