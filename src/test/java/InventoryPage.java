import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class InventoryPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    // Локаторы
    private final By firstItemAddToCartButton = By.id("add-to-cart-sauce-labs-backpack");
    private final By cartBadge = By.className("shopping_cart_badge");
    private final By removeButton = By.id("remove-sauce-labs-backpack");

    public InventoryPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @Step("Добавление рюкзака в корзину с принудительным скроллом")
    public void addToCart() {
        // 1. Ждем появления кнопки в DOM
        WebElement button = wait.until(ExpectedConditions.presenceOfElementLocated(firstItemAddToCartButton));

        // 2. Скроллим к кнопке (важно для Docker/Headless, чтобы элемент был "в фокусе")
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", button);

        // 3. Кликаем именно тогда, когда кнопка готова
        wait.until(ExpectedConditions.elementToBeClickable(button)).click();
    }

    @Step("Удаление рюкзака из корзины")
    public void removeItem() {
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(removeButton));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", button);
        button.click();
    }

    @Step("Ожидание исчезновения счетчика корзины")
    public void waitForBadgeToDisappear() {
        wait.until(d -> d.findElements(cartBadge).isEmpty());
    }

    @Step("Получение количества товаров в корзине")
    public String getCartItemsCount() {
        // Ждем видимости счетчика перед тем как брать текст
        return wait.until(ExpectedConditions.visibilityOfElementLocated(cartBadge)).getText();
    }

    @Step("Проверка: виден ли счетчик товаров")
    public boolean isCartBadgePresent() {
        return !driver.findElements(cartBadge).isEmpty();
    }
}
