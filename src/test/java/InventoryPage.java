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

    private final By firstItemAddToCartButton = By.id("add-to-cart-sauce-labs-backpack");
    private final By cartBadge = By.className("shopping_cart_badge");
    private final By removeButton = By.id("remove-sauce-labs-backpack");

    public InventoryPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    @Step("Добавление товара в корзину через JS")
    public void addToCart() {
        WebElement button = wait.until(ExpectedConditions.presenceOfElementLocated(firstItemAddToCartButton));
        // Прямой клик через JavaScript - самый надежный метод для Docker
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", button);
    }

    @Step("Удаление товара из корзины через JS")
    public void removeItem() {
        WebElement button = wait.until(ExpectedConditions.presenceOfElementLocated(removeButton));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", button);
    }

    @Step("Получение количества товаров")
    public String getCartItemsCount() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(cartBadge)).getText();
    }

    @Step("Ожидание исчезновения счетчика")
    public void waitForBadgeToDisappear() {
        wait.until(d -> d.findElements(cartBadge).isEmpty());
    }

    public boolean isCartBadgePresent() {
        return !driver.findElements(cartBadge).isEmpty();
    }
}
