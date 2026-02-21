import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import java.time.Duration;

public class SauceTest {
    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();
    private SauceLoginPage loginPage;
    private InventoryPage inventoryPage;

    public WebDriver getDriver() {
        return driverThreadLocal.get();
    }

    @BeforeEach
    void setup() {
        ChromeOptions options = new ChromeOptions();

        // КРИТИЧЕСКИЕ ФЛАГИ ДЛЯ DOCKER
        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--remote-allow-origins=*");

        // Уникальная папка профиля для каждого потока
        String userDataDir = "/tmp/chrome-user-data-sauce-" + Thread.currentThread().getId();
        options.addArguments("--user-data-dir=" + userDataDir);

        WebDriverManager.chromedriver().setup();

        // ВАЖНО: Передаем 'options' в скобки!
        WebDriver localDriver = new ChromeDriver(options);
        driverThreadLocal.set(localDriver);

        getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
        loginPage = new SauceLoginPage(getDriver());
        inventoryPage = new InventoryPage(getDriver());
    }

    @Test
    @DisplayName("Позитивный: Полный цикл работы с корзиной")
    void testCartCycle() {
        getDriver().get("https://www.saucedemo.com");
        loginPage.login("standard_user", "secret_sauce");

        inventoryPage.addToCart();
        Assertions.assertEquals("1", inventoryPage.getCartItemsCount());

        inventoryPage.removeItem();
        inventoryPage.waitForBadgeToDisappear();
        Assertions.assertFalse(inventoryPage.isCartBadgePresent(), "Счетчик не исчез!");
    }

    @AfterEach
    void tearDown() {
        if (getDriver() != null) {
            getDriver().quit();
            driverThreadLocal.remove();
        }
    }
}
