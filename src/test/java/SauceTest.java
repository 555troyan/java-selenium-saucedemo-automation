import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import java.time.Duration;

public class SauceTest {
    WebDriver driver;
    SauceLoginPage loginPage;
    InventoryPage inventoryPage; // Добавляем вторую страницу

    @BeforeEach
    void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        // Инициализируем обе страницы
        loginPage = new SauceLoginPage(driver);
        inventoryPage = new InventoryPage(driver);
    }

    @Test
    @DisplayName("Позитивный тест: Добавление товара в корзину")
    void testAddToCart() {
        driver.get("https://www.saucedemo.com");

        // 1. Логинимся (используем первый Page Object)
        loginPage.login("standard_user", "secret_sauce");

        // 2. Добавляем товар (используем второй Page Object)
        inventoryPage.addToCart();

        // 3. ПРОВЕРКА 1: На иконке корзины появилась цифра "1"
        String count = inventoryPage.getCartItemsCount();
        Assertions.assertEquals("1", count, "Количество товаров в корзине не совпадает!");

        // 4. ПРОВЕРКА 2: Кнопка сменила текст на "Remove"
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
