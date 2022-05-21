package pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class LoginPage extends BasePage{

    // CONSTRUCTOR
    public LoginPage(WebDriver driver) {
        super(driver);
    }

    //SELECTORS
    private final By usernameInputFieldSelector = By.id("username");
    private final By passwordInputFieldSelector = By.id("password");
    private final By loginButtonSelector = By.id("Login");
    private final By errorMessageDivSelector = By.id("error");

    //METHODS
    public void loginInToSandbox(String url, String username, String password){
        driver.get(url);
        wait.until(ExpectedConditions.visibilityOfElementLocated(usernameInputFieldSelector)).sendKeys(username);
        wait.until(ExpectedConditions.visibilityOfElementLocated(passwordInputFieldSelector)).sendKeys(password);
        wait.until(ExpectedConditions.visibilityOfElementLocated(loginButtonSelector)).click();
    }

    //GET METHODS
    public String getErrorMessage(){
        return getInnerTextFromWebElement(errorMessageDivSelector);
    }
}
