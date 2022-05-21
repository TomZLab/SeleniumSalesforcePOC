import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import pageObjects.LoginPage;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LoginTest extends BaseTest {

    @Test
    public void shouldNotLogInWithIncorrectCredentials() {
        String username = "test";
        String password = "test";
        String expectedErrorMessage = "Please check your username and password. If you still can't log in, contact your Salesforce administrator.";
        LoginPage loginPage = new LoginPage(driver);

        loginPage
                .loginInToSandbox(URL, username, password);

        Assertions.assertEquals(expectedErrorMessage, loginPage.getErrorMessage(), "Wrong error message");
    }

    @Test
    public void shouldNotLogInWithIncorrectPassword() {
        String password = "test";
        String expectedErrorMessage = "Please check your username and password. If you still can't log in, contact your Salesforce administrator.";
        LoginPage loginPage = new LoginPage(driver);

        loginPage
                .loginInToSandbox(URL, USERNAME, password);

        Assertions.assertEquals(expectedErrorMessage, loginPage.getErrorMessage(), "Wrong error message");
    }

    @Test
    public void shouldNotLogInWithoutPassword() {
        String password = "";
        String expectedErrorMessage = "Please enter your password.";
        LoginPage loginPage = new LoginPage(driver);

        loginPage
                .loginInToSandbox(URL, USERNAME, password);

        Assertions.assertEquals(expectedErrorMessage, loginPage.getErrorMessage(), "Wrong error message");
    }
}
