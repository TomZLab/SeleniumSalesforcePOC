import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.Allure;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.io.FileHandler;
import testHelpers.TestStatus;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class BaseTest {

    WebDriver driver;
    private boolean headless = false;
    private boolean useExternalDisplay = false;
    private int browserWindowXAxis = 0;
    private int browserWindowYAxis = 0;

    private String pathToCredentials = "src/test/resources/credentials.json";

    protected String USERNAME;
    protected String PASSWORD;
    protected String URL = "https://test.salesforce.com";

    @RegisterExtension
    TestStatus testStatus = new TestStatus();

    @BeforeAll
    private void setup() throws IOException, ParseException {

        getCredentials();

        //block notifications popup
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("profile.default_content_setting_values.notifications", 2);
        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("prefs", prefs);

        //headless setup
        if (headless) {
            options.addArguments("--headless", "--no-sandbox", "--disable-dev-shm-usage", "--disable-gpu", "--window-size=1920,1040", "--ignore-certificate-errors");
        }

        WebDriverManager.chromedriver().setup();

        driver = new ChromeDriver(options);

        //set window position e.g. for external monitors
        if (useExternalDisplay) {
            driver.manage().window().setPosition(new Point(browserWindowXAxis, browserWindowYAxis));
        }

        //maximize browser window
        driver.manage().window().maximize();
    }

    @AfterEach
    public void afterTest() {
        //Add print screen to allure
        if (testStatus.isFailed) {
            String timestamp = java.time.LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSSS"));
            String pathToPNG = takeScreenshot(timestamp);
            Path content = Paths.get(pathToPNG);
            try (InputStream is = Files.newInputStream(content)) {
                Allure.addAttachment("Screenshot", is);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //Delete source files
            try {
                Files.deleteIfExists(Paths.get(pathToPNG));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @AfterAll
    private void tearDown(){
        if (driver != null){
            driver.quit();
        }
    }

    private void getCredentials() throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        Object object = parser.parse(new FileReader(pathToCredentials));
        JSONObject jsonObject = (JSONObject) object;
        USERNAME = (String) jsonObject.get("username");
        PASSWORD = (String) jsonObject.get("password");
    }

    public String takeScreenshot(String timeStamp) {
        String pathToDirectory = "target/screenshots";
        String pathToFile = pathToDirectory + "/" + timeStamp + "_error.png";
        File directory = new File(pathToDirectory);
        if (!directory.exists()) {
            directory.mkdir();
        }
        File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        try {
            FileHandler.copy(screenshot, new File(pathToFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pathToFile;
    }
}
