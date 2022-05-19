package pageObjects;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.Duration;

public class BasePage {

    WebDriver driver;
    WebDriverWait wait;
    Actions action;
    JavascriptExecutor js;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        this.action = new Actions(driver);
        this.js = (JavascriptExecutor) driver;
    }

    protected void takeScreenshot() {
        File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        try {
            FileUtils.copyFile(screenshot, new File(Paths.get("target") + "\\screenshots\\screenshot_" + java.time.LocalDateTime.now() + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
