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

public class TestCase7 {
    @Test
    public void TestCase6() throws IOException, InterruptedException {

        // Go to https://didongthongminh.vn/
        WebDriver driver = driverFactory.getFirefoxDriver();
        driver.get("https://didongthongminh.vn/");

        // Click on my account link
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector(".member-btn")))).click();


        try {
            // Login in application using previously created credential (Đã có kiểm tra email đã đăng kí, nhưng Trang login không hoạt động)
            WebElement EmailElem = wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector("#log_email"))));
            EmailElem.sendKeys("cuong.php3@gmail.com");
            WebElement PassElem = wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector("#log_pass"))));
            PassElem.sendKeys("123456");
            wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector("#frmLogin > div:nth-child(4) > a")))).click();
        } catch (NoSuchElementException e) {
            System.out.println("Element not found");
        }

        // Click on 'My Orders' (Do không đăng nhập được nên không có Danh sách đơn hàng)
        // Click on 'View Order' (Do không đăng nhập được nên không có Danh sách đơn hàng)

        //6. *** note: After steps 4 and 5, step 6 "RECENT ORDERS" was not displayed
        //   Verify the previously created order is displayed in 'RECENT ORDERS' table and status is Pending (Do không đăng nhập được nên không có Danh sách đơn hàng)

        //7. Click on 'Print Order' link (Do không đăng nhập được nên không có Danh sách đơn hàng)

        //  8. *** note: the link was not found.
        // Click 'Change...' link and a popup will be opened as 'Select a destination' , select 'Save as PDF' link.
        
        driver.quit();
    }
}
