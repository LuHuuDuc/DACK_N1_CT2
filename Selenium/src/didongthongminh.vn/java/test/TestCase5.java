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

public class TestCase5 {
    @Test
    public void TestCase5() throws IOException, InterruptedException {


        // 1. Goto https://didongthongminh.vn/
        WebDriver driver = driverFactory.getFirefoxDriver();
        driver.get("https://didongthongminh.vn/");

        // 2. Click on my account link ( click vào đường dẫn tài khoản để tiến hành đăng kí)
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector(".member-btn")))).click();

        // 3. Click Create an Account link and fill New User information except Email ID ( Chọn tạo tài khoản và điền thông tin, email,... )
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector(".top-title > a:nth-child(2)")))).click();
        WebElement hoten = wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector("#reg_name"))));
        hoten.sendKeys("Đỗ Cao Cường");
        WebElement sdt = wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector("#reg_tel"))));
        sdt.sendKeys("0945678911");
        WebElement email = wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector("#reg_email"))));
        email.sendKeys("cuong.php3@gmail.com");
        WebElement pass = wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector("#reg_pass"))));
        pass.sendKeys("123456");
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector("#a_message")))).click();
        // 4. Click Register ( Chọn nút đăng ký )
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector(".submitRegister")))).click();

        // 5. Verify Registration is done. Expected account registration done. ( Xác nhận việc tạo tài khoản thành công )
        Thread.sleep(4000);
        File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(screenshotFile, new File("src/didongthongminh.vn/java/img/testcase5_registerSuccess.png"));

        // 6. Go to TV menu ( Ở đây mà danh mục Điện Thoại )
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector(".for-hover > div:nth-child(1) > div:nth-child(1) > ul:nth-child(1) > li:nth-child(2) > a:nth-child(1) > span:nth-child(1)")))).click();

        // 7. Add product in your wish list - use product - LG LCD ( Thêm vào danh sách yêu thích, nhưng trong trang didongthongminh.vn lại không có )
        // 8. Click SHARE WISHLIST (Chọn danh sách yêu thích)
        // 9. In next page enter Email and a message and click SHARE WISHLIST
        // 10.Check wishlist is shared. Expected wishlist shared successfully.(Kiểm tra trang danh sách yêu thích đã chia sẻ thành công)



    }
}
