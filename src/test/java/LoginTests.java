import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import java.time.Duration;

public class LoginTests {
    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();
    private SauceLoginPage loginPage;

    public WebDriver getDriver() {
        return driverThreadLocal.get();
    }

    @BeforeEach
    void setup() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--remote-allow-origins=*");

        String userDataDir = "/tmp/chrome-user-data-login-" + Thread.currentThread().getId();
        options.addArguments("--user-data-dir=" + userDataDir);

        WebDriverManager.chromedriver().setup();

        // ВАЖНО: Передаем 'options' сюда!
        WebDriver localDriver = new ChromeDriver(options);
        driverThreadLocal.set(localDriver);

        getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
        getDriver().get("https://www.saucedemo.com");
        loginPage = new SauceLoginPage(getDriver());
    }

    @Test
    @DisplayName("1. Успешный логин")
    void testSuccessLogin() {
        loginPage.login("standard_user", "secret_sauce");
        Assertions.assertTrue(getDriver().getCurrentUrl().contains("inventory"));
    }

    @Test
    @DisplayName("2. Неверный пароль")
    void testWrongPassword() {
        loginPage.login("standard_user", "wrong_pass");
        Assertions.assertTrue(loginPage.getErrorMessage().toLowerCase().contains("match"));
    }

    @AfterEach
    void tearDown() {
        if (getDriver() != null) {
            getDriver().quit();
            driverThreadLocal.remove();
        }
    }
}
