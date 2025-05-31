import java.util.List;
import java.util.ArrayList;

public class Inventory {
    private List<InventoryItem> items;

    public Inventory() {
        this.items = new ArrayList<>();
    }

    public void addItem(InventoryItem newItem) {
        for (InventoryItem invItem : items) {
            if (invItem.getItem().getItemName().equals(newItem.getItem().getItemName())) {
                // Item sudah ada, tambahkan quantity saja
                invItem.setQuantity(invItem.getQuantity() + newItem.getQuantity());
                return; // keluar dari method, tidak menambah item baru
            }
        }
        // Jika item belum ada, tambahkan ke list
        items.add(newItem);
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

    public int totalItems() {
        return items.size();
    }
    public boolean hasItemByName(String itemName) {
        for (InventoryItem invItem : items) {
            if (invItem.getItem().getItemName().equals(itemName) && invItem.getQuantity() > 0) {
                return true;
            }
        }
        return false;
    }

    public InventoryItem findItemByName(String name) {
        for (InventoryItem invItem : items) {
            if (invItem.getItem().getItemName().equals(name)) {
                return invItem;
            }
        }
        return null;
    }
}