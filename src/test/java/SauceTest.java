import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import java.time.Duration;

public class SauceTest {
    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

    public WebDriver getDriver() {
        return driverThreadLocal.get();
    }

    @BeforeEach
    void setup() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--headless=new"); // Режим без окна для GitHub
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--remote-allow-origins=*");

        WebDriverManager.chromedriver().setup();
        driverThreadLocal.set(new ChromeDriver(options));
        getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
    }

    @Test
    @DisplayName("Позитивный: Полный цикл работы с корзиной")
    void testCartCycle() {
        WebDriver driver = getDriver();
        SauceLoginPage loginPage = new SauceLoginPage(driver);
        InventoryPage inventoryPage = new InventoryPage(driver);

        driver.get("https://www.saucedemo.com");

        loginPage.login("standard_user", "secret_sauce");

        // 1. Добавляем и проверяем появление
        inventoryPage.addToCart();
        Assertions.assertEquals("1", inventoryPage.getCartItemsCount(), "Товар не добавился!");

        // 2. Удаляем
        inventoryPage.removeItem();

        // 3. ЖДЕМ, пока счетчик реально исчезнет из DOM (решает проблему Flaky тестов)
        inventoryPage.waitForBadgeToDisappear();

        // 4. Финальная проверка
        Assertions.assertFalse(inventoryPage.isCartBadgePresent(), "Счетчик не исчез после удаления!");
    }

    @AfterEach
    void tearDown() {
        if (getDriver() != null) {
            getDriver().quit();
            driverThreadLocal.remove();
        }
    }
}
