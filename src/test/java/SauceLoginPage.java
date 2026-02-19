import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import io.qameta.allure.Step;




public class SauceLoginPage {
    private WebDriver driver;

    // 1. Локаторы элементов
    private By usernameField = By.id("user-name");
    private By passwordField = By.id("password");
    private By loginButton = By.id("login-button");
    private By errorContainer = By.cssSelector("h3[data-test='error']");

    public SauceLoginPage(WebDriver driver) {
        this.driver = driver;
    }



    // 3. Метод для получения текста ошибки
    public String getErrorMessage() {
        return driver.findElement(errorContainer).getText();
    }


    @Step
    public void login(String user, String pass) {
        driver.findElement(usernameField).sendKeys(user);
        driver.findElement(passwordField).sendKeys(pass);
        driver.findElement(loginButton).click();
    }


}



















