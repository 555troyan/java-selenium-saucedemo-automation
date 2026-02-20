import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class InventoryPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By firstItemAddToCartButton = By.id("add-to-cart-sauce-labs-backpack");
    private final By cartBadge = By.className("shopping_cart_badge");
    private final By removeButton = By.id("remove-sauce-labs-backpack");

    public InventoryPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @Step("Добавление рюкзака в корзину")
    public void addToCart() {
        wait.until(ExpectedConditions.elementToBeClickable(firstItemAddToCartButton)).click();
    }

    @Step("Удаление рюкзака из корзины")
    public void removeItem() {
        wait.until(ExpectedConditions.elementToBeClickable(removeButton)).click();
    }

    @Step("Ожидание исчезновения счетчика корзины")
    public void waitForBadgeToDisappear() {
        // Ждем до 10 секунд, пока список элементов станет пустым
        wait.until(d -> d.findElements(cartBadge).isEmpty());
    }

    @Step("Получение количества товаров в корзине")
    public String getCartItemsCount() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(cartBadge)).getText();
    }

    @Step("Проверка: виден ли счетчик товаров")
    public boolean isCartBadgePresent() {
        return !driver.findElements(cartBadge).isEmpty();
    }
}
