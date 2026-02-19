import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import java.time.Duration;
import io.qameta.allure.*;








@Feature("Авторизация")
@Story("Проверка различных сценариев входа")


@DisplayName("Проверки системы входа (SauceDemo)")
public class LoginTests {
    WebDriver driver;
    SauceLoginPage loginPage;

    @BeforeEach
    void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        loginPage = new SauceLoginPage(driver);
        driver.get("https://www.saucedemo.com");
    }

    @Test
    @DisplayName("1. Успешный логин (Standard)")
    void test1_SuccessLogin() {
        loginPage.login("standard_user", "secret_sauce");
        Assertions.assertTrue(driver.getCurrentUrl().contains("inventory"));
    }

    @Test
    @DisplayName("2. Неверный пароль")
    void test2_WrongPassword() {
        loginPage.login("standard_user", "wrong_pass");
        Assertions.assertTrue(loginPage.getErrorMessage().toLowerCase().contains("match"));
    }

    @Test
    @DisplayName("3. Заблокированный пользователь")
    void test3_LockedOutUser() {
        loginPage.login("locked_out_user", "secret_sauce");
        Assertions.assertTrue(loginPage.getErrorMessage().toLowerCase().contains("locked out"));
    }

    @Test
    @DisplayName("4. Пустые поля")
    void test4_EmptyFields() {
        loginPage.login("", "");
        Assertions.assertTrue(loginPage.getErrorMessage().toLowerCase().contains("username is required"));
    }

    @Test
    @DisplayName("5. Логин с задержкой (Performance)")
    void test5_PerformanceGlitch() {
        loginPage.login("performance_glitch_user", "secret_sauce");
        Assertions.assertTrue(driver.getCurrentUrl().contains("inventory"), "Долгая загрузка не удалась");
    }



    @Test
    @DisplayName("Негативный: Ввод спецсимволов (SQL Injection test)")
    void testSqlInjectionLogin() {
        driver.get("https://www.saucedemo.com");

        // Пробуем "сломать" логин классической кавычкой
        loginPage.login("standard_user' OR '1'='1", "secret_sauce");

        // ПРОВЕРКА: Сайт должен выдать стандартную ошибку "do not match", а не упасть с 500 ошибкой
        String errorText = loginPage.getErrorMessage();
        Assertions.assertTrue(errorText.toLowerCase().contains("match"),
                "Сайт выдал странную ошибку на спецсимволы: " + errorText);
    }
















    @AfterEach
    void tearDown() {
        if (driver != null) {
             driver.quit();
        }
    }
}
