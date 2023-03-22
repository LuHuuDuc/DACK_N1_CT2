package test;

import driver.driverFactory;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.time.Duration;

public class TestCase6 {
    @Test
    public void TestCase6() throws IOException, InterruptedException {

        // Go to https://didongthongminh.vn/
        WebDriver driver = driverFactory.getFirefoxDriver();
        driver.get("https://didongthongminh.vn/");

        // Click on my account link
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector(".member-btn")))).click();

        // Login in application using previously created credential (Đã có kiểm tra email đã đăng kí, nhưng Trang login không hoạt động)
        try {
            WebElement EmailElem = wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector("#log_email"))));
            EmailElem.sendKeys("cuong.php3@gmail.com");
            WebElement PassElem = wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector("#log_pass"))));
            PassElem.sendKeys("123456");
            wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector("#frmLogin > div:nth-child(4) > a")))).click();
        } catch (NoSuchElementException e) {
            System.out.println("Element not found");
        }
        // Click on MY WISHLIST link (Do không thể đăng nhập được nên không có danh sách yêu thích)
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector("#a_message")))).click();

        // In next page, Click ADD TO CART link
            // ( Thay bằng 3 bước này )
            // 1. Click on Điện thoại menu
            wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector(".cate-btn")))).click();
            wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector(".for-click > div:nth-child(1) > div:nth-child(1) > ul:nth-child(1) > li:nth-child(1) > a:nth-child(1)")))).click();

            // 3. Chọn 1 sp nào đó
            wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector("div.col-sm-5ths:nth-child(1) > div:nth-child(1) > a:nth-child(1) > div:nth-child(1) > div:nth-child(3) > h3:nth-child(1)")))).click();

            // 2. Thêm sản phẩm vào giỏ hàng
            wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector(".add_cart > button:nth-child(1)")))).click();

        // Enter general shipping country, state/province and zip for the shipping cost estimate (Nhập thông tin cá nhân trên trang web)
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector("#formPayment > div:nth-child(2) > label:nth-child(2)")))).click();
        WebElement name = wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector("#name"))));
        name.sendKeys("Test1");
        WebElement telephone = wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector("#telephone"))));
        telephone.sendKeys("0947111111");

        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector("#select2-city-container")))).click();
        WebElement search__field = wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector(".select2-search__field"))));
        search__field.sendKeys("TP HCM");
        search__field.sendKeys(Keys.ENTER);

        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector("#select2-district-container")))).click();
        WebElement search__field1 = wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector(".select2-search__field"))));
        search__field1.sendKeys("Quan 10");
        search__field1.sendKeys(Keys.ENTER);

        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector("div.col-md-6:nth-child(4) > span:nth-child(2) > span:nth-child(1) > span:nth-child(1)")))).click();
        WebElement search__field2 = wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector(".select2-search__field"))));
        search__field2.sendKeys("09");
        search__field2.sendKeys(Keys.ENTER);

        WebElement address = wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector("#address"))));
        address.sendKeys("Test Address");

        WebElement note = wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector("#note"))));
        note.sendKeys("Dạ em test, anh chị thông cảm.");
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector("#cod-btn")))).click();

        driver.quit();

    }
}
