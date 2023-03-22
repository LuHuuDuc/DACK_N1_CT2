package driver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class driverFactory{
    public static WebDriver getFirefoxDriver() {
        String currentProjectLocation = System.getProperty("user.dir");
        String geckoDriverRelativePath = "/src/didongthongminh.vn/resources/drivers/geckodriver.exe";
        String geckoDriverLocation = currentProjectLocation.concat(geckoDriverRelativePath);
        System.setProperty("webdriver.chrome.driver", geckoDriverLocation);
        FirefoxOptions options = new FirefoxOptions();
        options.setHeadless(false);
        WebDriver driver = new FirefoxDriver(options);
        driver.manage().window(). minimize();
        return driver;
    }
}
