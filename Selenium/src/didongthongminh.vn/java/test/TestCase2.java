package test;

import driver.driverFactory;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.time.Duration;

import static org.testng.AssertJUnit.assertEquals;

public class TestCase2 {
    @Test
    public void TestCase2() throws IOException {
        // 1. Goto https://didongthongminh.vn/
        WebDriver driver = driverFactory.getFirefoxDriver();
        driver.get("https://didongthongminh.vn/");

        // 2. Click on Điện thoại menu
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector(".slide_banner > div:nth-child(1) > div:nth-child(1) > ul:nth-child(1) > li:nth-child(1)")))).click();

        // 3. Tìm giá trị thực tế và so sánh với giá trị mong đợi
        // 3.1. Tim giá trị thực tế
        String iphoneCost = driver.findElement(By.cssSelector("*[title='iPhone 12 Pro Max Cũ Đẹp 512GB'] .price")).getText();
        // 3.2. So sánh giá trị thực tế với giá trị mong đợi.
        assertEquals("17.690.000 đ", iphoneCost);

        // 4. Chọn một sản phẩm bất kì
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector("div.col-sm-5ths:nth-child(1) > div:nth-child(1) > a:nth-child(1) > div:nth-child(1) > div:nth-child(3)")))).click();


    }
}
