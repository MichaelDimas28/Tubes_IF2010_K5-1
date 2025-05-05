import java.util.List;

public class Store extends NPCHouse {
    private List<Items> storeItems;

    public Store(List<String> availableAction, NPC Owner, List<Items> storeItems) {
        super(availableAction, Owner);
        this.storeItems = storeItems;
    }

    public List<Items> getStoreItems() {
        return storeItems;
    }

    public int calculateTotalPrice(List<Items> cartItems) {
        int totalPrice = 0;
        for (Items item : cartItems) {
            totalPrice += item.getBuyPrice();
        }
        return totalPrice;
    }
}