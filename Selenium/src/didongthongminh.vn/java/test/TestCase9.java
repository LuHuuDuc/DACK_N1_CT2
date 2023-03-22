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

public class TestCase9 {
    @Test
    public void TestCase9() throws IOException, InterruptedException {

        //1. Go to https://didongthongminh.vn/
        WebDriver driver = driverFactory.getFirefoxDriver();
        driver.get("https://didongthongminh.vn/");

        //2. Go to Mobile and add IPHONE to cart
            // Click on Điện thoại menu
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector(".slide_banner > div:nth-child(1) > div:nth-child(1) > ul:nth-child(1) > li:nth-child(1)")))).click();

            // Chọn 1 sp nào đó
            wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector("div.col-sm-5ths:nth-child(1) > div:nth-child(1) > a:nth-child(1) > div:nth-child(1) > div:nth-child(3) > h3:nth-child(1)")))).click();

            // Thêm sản phẩm vào giỏ hàng
            wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector(".add_cart > button:nth-child(1)")))).click();

        //3. Enter Coupon Code ( Không có mã giảm giá )
            wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector("p.menubot > span:nth-child(1)")))).click();
            WebElement code_down = wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector("#code_down"))));
            code_down.sendKeys("MAGIAM50");
            wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector("#add_code_price")))).click();

        //4. Verify the discount generated
        Thread.sleep(2000);
        File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(screenshotFile, new File("src/test/java/img/testcase9_verifyResult.png"));
    }
}
