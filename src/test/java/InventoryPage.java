import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class InventoryPage {
    private WebDriver driver;

    // 1. Локаторы элементов каталога
    private By firstItemAddToCartButton = By.id("add-to-cart-sauce-labs-backpack");
    private By cartBadge = By.className("shopping_cart_badge");
    private By removeButton = By.id("remove-sauce-labs-backpack");

    public InventoryPage(WebDriver driver) {
        this.driver = driver;
    }

    // 2. Метод: Добавить первый товар (рюкзак) в корзину
    public void addToCart() {
        driver.findElement(firstItemAddToCartButton).click();
    }

    // 3. Метод: Получить число товаров на иконке корзины
    public String getCartItemsCount() {
        return driver.findElement(cartBadge).getText();
    }

    // 4. Проверка: Изменилась ли кнопка на "Remove"
    public boolean isRemoveButtonDisplayed() {
        return driver.findElement(removeButton).isDisplayed();
    }



    // Добавьте это в класс InventoryPage
    public void removeItem() {
        driver.findElement(removeButton).click();
    }

    public boolean isCartBadgePresent() {
        // Если список элементов пуст, значит счетчика на корзине нет
        return driver.findElements(cartBadge).size() > 0;
    }



















}
