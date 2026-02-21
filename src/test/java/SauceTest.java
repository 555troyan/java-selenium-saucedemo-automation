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

    public WebDriver getDriver() { return driverThreadLocal.get(); }

    @BeforeEach
    void setup() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new", "--no-sandbox", "--disable-dev-shm-usage", "--disable-gpu", "--window-size=1920,1080", "--remote-allow-origins=*");
        String userDataDir = "/tmp/chrome-user-data-sauce-" + Thread.currentThread().getId();
        options.addArguments("--user-data-dir=" + userDataDir);

        WebDriverManager.chromedriver().setup();
        driverThreadLocal.set(new ChromeDriver(options));
        getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(15));

        loginPage = new SauceLoginPage(getDriver());
        inventoryPage = new InventoryPage(getDriver());
    }

    @Test
    @DisplayName("Позитивный: Полный цикл работы с корзиной")
    void testCartCycle() {
        getDriver().get("https://www.saucedemo.com");
        loginPage.login("standard_user", "secret_sauce");

        // Ждем, пока URL сменится на каталог (подтверждение логина)
        org.openqa.selenium.support.ui.WebDriverWait wait = new org.openqa.selenium.support.ui.WebDriverWait(getDriver(), Duration.ofSeconds(10));
        wait.until(org.openqa.selenium.support.ui.ExpectedConditions.urlContains("inventory"));

        inventoryPage.addToCart();
        Assertions.assertEquals("1", inventoryPage.getCartItemsCount(), "Счетчик не появился!");

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
