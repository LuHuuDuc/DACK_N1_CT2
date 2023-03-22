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

public class TestCase8 {
    @Test
    public void TestCase8() throws IOException, InterruptedException {

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

        //  4. Click on 'REORDER' link , change QTY & click Update
        //  (Do không có danh sách đơn đặt hàng, nên không thể thay đổi số lượng và nút cập nhật)
        //  Phương án : Có thể thay đổi trực tiếp trên giỏ hàng.

        // Click on Điện thoại menu
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector(".cate-btn")))).click();
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector(".for-click > div:nth-child(1) > div:nth-child(1) > ul:nth-child(1) > li:nth-child(1) > a:nth-child(1)")))).click();

        // Chọn 1 sp nào đó
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector("div.col-sm-5ths:nth-child(1) > div:nth-child(1) > a:nth-child(1) > div:nth-child(1) > div:nth-child(3) > h3:nth-child(1)")))).click();

        // Thêm sản phẩm vào giỏ hàng
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector(".add_cart > button:nth-child(1)")))).click();

        // Thay đổi số lượng sản phẩm
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector("div.product-cart1:nth-child(1) > div:nth-child(1) > div:nth-child(3) > div:nth-child(2) > span:nth-child(1) > button:nth-child(3)")))).click();


        //  5. Verify Grand Total is changed
        Thread.sleep(2000);
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector("#total_money")))).click();
        File screenshotFile_expected = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(screenshotFile_expected, new File("src/didongthongminh.vn/java/img/testcase8_verifyGrandTotal.png"));

        //  6. Complete Billing & Shipping Information
            // Enter general shipping country, state/province and zip for the shipping cost estimate
            // (Nhập thông tin cá nhân trên trang web)
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

            // Tiến hành đặt hàng
            wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector("#cod-btn")))).click();

            // Xác nhận đơn đặt hàng.
            File screenshotFile1 = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(screenshotFile1, new File("src/test/java/img/testcase8_verifyPurOrder.png"));

            // Đến trang xác nhận thanh toán
            // Click vào Thanh toán khi giao hàng (COD)
            Thread.sleep(5000);
            wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector(".tien-mat")))).click();
            // click Nút Xán nhận
            wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector("#submitForm")))).click();

        //  7. Verify order is generated and note the order number
        Thread.sleep(9000);
        File screenshotFile2 = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(screenshotFile2, new File("src/test/java/img/testcase8_verifyOrder.png"));

    }
}
