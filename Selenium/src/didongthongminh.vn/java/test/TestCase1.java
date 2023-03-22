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
import java.util.Collections;
import java.util.List;

import static org.testng.Assert.assertEquals;

public class TestCase1 {
    @Test
    public void test() throws IOException, InterruptedException {
        //  Step 1. Truy cập https://didongthongminh.vn/
        WebDriver driver = driverFactory.getFirefoxDriver();
        driver.get("https://didongthongminh.vn/");

        //  Step 2.Xác minh tiêu đề trên Website (title: Di Động Thông Minh Bán Điện Thoại Đáng Mua,Phụ Kiện,Tablet, Máy Tính)
        String expectedTitle = "Di Động Thông Minh Bán Điện Thoại Đáng Mua,Phụ Kiện,Tablet, Máy Tính";
        String actualTitle = driver.getTitle();
        assertEquals(actualTitle, expectedTitle);

        //  Step 3. Click vào danh mục Điện thoại
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(1));
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector(".slide_banner > div:nth-child(1) > div:nth-child(1) > ul:nth-child(1) > li:nth-child(1)")))).click();

        //  Step 4. Sắp xếp theo tên
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector("#cat_order > button:nth-child(1)")))).click();
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector("ul.dropdown-menu > li:nth-child(1)")))).click();

        //  Step 5. Xác nhận hàm lọc theo tên có đúng không?
        List<WebElement> productList = driver.findElements(By.className("frame_title"));
        List<String> productNames = new ArrayList<>();
        for (WebElement product : productList) {
            productNames.add(product.getText());
        }
        List<String> sortedProductNames = new ArrayList<>(productNames);
        Collections.sort(sortedProductNames, String.CASE_INSENSITIVE_ORDER);
        assertEquals(productNames, sortedProductNames);
        // Lý do bị false: Kết quả sắp xếp theo bảng chữ cái không khớp.
    }
}
