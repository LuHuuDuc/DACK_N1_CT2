package test;

import driver.driverFactory;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;

@Test
public class LaunchBrowser {
    public static void testLaunchBrowser() {
        WebDriver driver = driverFactory.getFirefoxDriver();
        driver.get("https://google.com");
        //Debug only
        try {
            Thread.sleep(2000);
        } catch (Exception ignored) {
        }
        driver.close();
        driver.quit();
    }
}
