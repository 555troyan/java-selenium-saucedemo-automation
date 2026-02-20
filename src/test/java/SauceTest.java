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
        options.addArguments("--disable-gpu", "--no-sandbox", "--disable-dev-shm-usage");

        if (System.getProperty("headless", "false").equals("true")) {
            options.addArguments("--headless");
        }

        WebDriverManager.chromedriver().setup();
        driverThreadLocal.set(new ChromeDriver(options));
        getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
    }

    @Test
    @DisplayName("Позитивный: Полный цикл работы с корзиной")
    void testCartCycle() throws InterruptedException {
        WebDriver driver = getDriver();
        SauceLoginPage loginPage = new SauceLoginPage(driver);
        InventoryPage inventoryPage = new InventoryPage(driver);

        driver.get("https://www.saucedemo.com");

        loginPage.login("standard_user", "secret_sauce");

        // Добавление
        inventoryPage.addToCart();
        Assertions.assertEquals("1", inventoryPage.getCartItemsCount());

        // Удаление
        inventoryPage.removeItem();

        // Маленькая страховочная пауза для обновления DOM в параллели
        Thread.sleep(500);

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
