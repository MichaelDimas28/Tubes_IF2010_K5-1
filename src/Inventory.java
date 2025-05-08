import java.util.List;
import java.util.ArrayList;

public class Inventory {
    private List<Items> items;

    public Inventory() {
        this.items = new ArrayList<>();
    }

    public void addItem(Items item) {
        items.add(item);
    }

    public void removeItem(Items item) {
        items.remove(item);
    }

    public boolean haveItem(Items item) {
        return items.contains(item);
    }

    public List<Items> getItems() {
        return items;
    }
}
