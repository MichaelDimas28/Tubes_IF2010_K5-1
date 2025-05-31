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
    
    public void reduceItem(InventoryItem item) {
        item.setQuantity(item.getQuantity()-1);
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

    public boolean reduceItem(Items item, int amount) {
        for (InventoryItem invItem : items) {
            if (invItem.getItem().equals(item)) {
                invItem.setQuantity(invItem.getQuantity() - amount);
                if (invItem.getQuantity() <= 0) {
                    items.remove(invItem);
                }
                return true;
            }
        }
        return false;
    }

}