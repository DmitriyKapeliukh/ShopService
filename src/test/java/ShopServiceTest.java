import com.market.domain.Item;
import com.market.domain.Order;
import com.market.domain.User;
import com.market.service.PurchaseStatus;
import com.market.service.ShopService;
import org.junit.After;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ShopServiceTest {
    private Map<Item, Long> goods = new HashMap<>();

    @After
    public void cleanup() {
        goods.clear();
    }

    @Test
    public void shouldValidateIfUserOrderUnknownItem() {
        goods.put(TestData.validItem, 1L);
        ShopService shopService = new ShopService(getItemsQuantity());
        User user = new User("Tom", 100d);
        Order order = new Order(user, Arrays.asList(TestData.itemWithZeroCost));

        PurchaseStatus status = shopService.makePurchase(order);

        assert status == PurchaseStatus.UNKNOWN_ITEM :
                "Incorrect status after purchase. Expected: " + PurchaseStatus.UNKNOWN_ITEM + ", actual: " + status;
    }

    @Test
    public void shouldValidateIfUserHasLowBalance() {
        goods.put(TestData.validItem, 2L);
        ShopService shopService = new ShopService(getItemsQuantity());
        User user = new User("Mike", 3d);
        Order order = new Order(user, Arrays.asList(TestData.validItem));

        PurchaseStatus status = shopService.makePurchase(order);

        assert status == PurchaseStatus.USER_HAS_LOW_BALANCE :
                "Incorrect status after purchase. Expected: " + PurchaseStatus.USER_HAS_LOW_BALANCE + ", actual: " + status;
    }

    @Test
    public void shouldValidateInsufficientItemsInStock() {
        goods.put(TestData.validItem, 1L);
        ShopService shopService = new ShopService(getItemsQuantity());
        User user = new User("Piter", 11d);
        Order order = new Order(user, Arrays.asList(TestData.validItem, TestData.validItem));

        PurchaseStatus status = shopService.makePurchase(order);

        assert status == PurchaseStatus.INSUFFICIENT_ITEMS_STOCK :
                "Incorrect status after purchase. Expected: " + PurchaseStatus.INSUFFICIENT_ITEMS_STOCK + ", actual: " + status;
    }

    @Test
    public void shouldSuccessfullyMakePurchase() {
        goods.put(TestData.validItem, 1L);
        ShopService shop = new ShopService(getItemsQuantity());
        User user = new User("John", 200d);
        Order order = new Order(user, Arrays.asList(TestData.validItem));

        PurchaseStatus status = shop.makePurchase(order);

        assert status == PurchaseStatus.OK :
                "Incorrect status after purchase. Expected: " + PurchaseStatus.OK + ", actual: " + status;
    }

    @Test
    public void shouldCorrectlyDecreaseBalance() {
        goods.put(TestData.validItem, 2L);
        ShopService shop = new ShopService(getItemsQuantity());

        double userInitialBalance = 200d;
        User user = new User("John", userInitialBalance);

        List<Item> itemsInOrder = Arrays.asList(TestData.validItem, TestData.validItem);
        Order order = new Order(user, itemsInOrder);

        double itemsTotalPrice = TestData.validItem.getCost() * 2;
        double expectedUserBalance = roundAvoid(userInitialBalance - itemsTotalPrice, 2);

        shop.makePurchase(order);

        assert user.getBalance() == expectedUserBalance :
                "User has insufficient balance. \n Initial user balance: " + userInitialBalance +
                        "\n User current balance: " + user.getBalance() + "\n Items cost: " + itemsTotalPrice;
    }

    private Map<Long, Long> getItemsQuantity() {
        return goods.entrySet().stream()
                .collect(Collectors.toMap(e -> e.getKey().getId(), Map.Entry::getValue));
    }

    private double roundAvoid(double value, int places) {
        double scale = Math.pow(10, places);
        return Math.round(value * scale) / scale;
    }
}
