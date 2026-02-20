import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import java.time.Duration;
import org.openqa.selenium.chrome.ChromeOptions;

public class SauceTest {
    WebDriver driver;
    SauceLoginPage loginPage;
    InventoryPage inventoryPage; // Добавляем вторую страницу


    @BeforeEach
    void setup() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--window-size=1920,1080");


        if (System.getProperty("headless", "false").equals("true")) {
            options.addArguments("--headless");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
        }

        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver(options);

        // Устанавливаем ожидание, чтобы Selenium не "торопился"
        driver.manage().timeouts().implicitlyWait(java.time.Duration.ofSeconds(10));

        // ИНИЦИАЛИЗИРУЕМ СТРАНИЦЫ (Без этого тест не увидит кнопки)
        loginPage = new SauceLoginPage(driver);
        inventoryPage = new InventoryPage(driver);
    } // Обязательно закрываем метод этой скобкой


    @Test
    @DisplayName("Позитивный тест: Добавление товара в корзину")
    void testAddToCart() {
        // Добавьте / в конце или убедитесь, что URL полный
        driver.get("https://www.saucedemo.com");

        loginPage.login("standard_user", "secret_sauce");
        inventoryPage.addToCart();

        String count = inventoryPage.getCartItemsCount();
        Assertions.assertEquals("1", count, "Количество товаров в корзине не совпадает!");

        Assertions.assertTrue(inventoryPage.isRemoveButtonDisplayed(),
                "Кнопка 'Remove' не появилась после добавления в корзину!");
    }



















    @Test
    @DisplayName("Позитивный: Удаление товара из корзины")
    void testRemoveFromCart() {
        driver.get("https://www.saucedemo.com");
        loginPage.login("standard_user", "secret_sauce");

        // 1. Добавляем и проверяем, что счетчик "1"
        inventoryPage.addToCart();
        Assertions.assertEquals("1", inventoryPage.getCartItemsCount());

        // 2. Удаляем товар
        inventoryPage.removeItem();

        // 3. ПРОВЕРКА: Счетчик (красный кружок) должен исчезнуть
        Assertions.assertFalse(inventoryPage.isCartBadgePresent(),
                "Ошибка: Товар не удалился, счетчик всё еще виден!");
    }


    @AfterEach
    void tearDown() {
        if (driver != null) {
           // driver.quit();
        }
    }
}
