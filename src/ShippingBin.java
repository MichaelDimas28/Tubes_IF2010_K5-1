import java.util.List;
import java.util.ArrayList;

public class ShippingBin {
    private final int maxSlot = 16;
    private List<Items> shippedItems;

    public ShippingBin() {
        this.shippedItems = new ArrayList<>();
    }

    public int calculatePrice() {
        int total = 0;
        for (Items item : shippedItems) {
            total += item.getSellPrice();
        }
        return total;
    }

    public boolean isFull() {
        return shippedItems.size() >= maxSlot;
    }

    public void clearShippingBin() {
        shippedItems.clear();
    }
}
