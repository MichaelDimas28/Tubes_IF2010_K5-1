import java.util.List;
import java.util.ArrayList;

public class Inventory {
    private List<InventoryItem> items;

    public Inventory() {
        this.items = new ArrayList<>();
    }

    public void addItem(InventoryItem item) {
        items.add(item);
    }

    public void removeItem(InventoryItem item) {
        items.remove(item);
    }
    /*
    public boolean haveItem(InventoryItem item) {
        return items.contains(item);
    }
    */
    public List<InventoryItem> getItems() {
        return items;
    }
}