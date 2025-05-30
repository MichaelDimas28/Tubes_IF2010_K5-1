import java.util.List;
import java.util.ArrayList;

public class ShippingBin {
    private final int maxSlot = 16;
    private List<InventoryItem> shippedItems;

    public ShippingBin() {
        this.shippedItems = new ArrayList<>();
    }

    public boolean addItem(InventoryItem item) {
        if (isFull()) {
            return false;
        }

        if (item.getItem().getSellPrice()==0) {
            return false;
        }

        for (InventoryItem invItem : shippedItems) {
            if (invItem.getItem().getItemName().equals(item.getItem().getItemName())) {
                invItem.setQuantity(invItem.getQuantity()+1);
                return true;
            }
        }
        shippedItems.add(item);
        return true;
    }

    public int calculatePrice() {
        int total = 0;
        for (InventoryItem item : shippedItems) {
            total += item.getItem().getSellPrice();
        }
        return total;
    }

    public boolean isFull() {
        return shippedItems.size() >= maxSlot;
    }

    public void clearShippingBin() {
        shippedItems.clear();
    }

    public List<InventoryItem> getShippedItems() {
        return shippedItems;
    }

    public int totalItems() {
        return shippedItems.size();
    }
}
