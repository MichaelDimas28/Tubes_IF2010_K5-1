import java.util.List;

public class Store extends NPCHouse {
    private List<Items> items;

    public Store(List<String> availableAction, NPC Owner, List<Items> items) {
        super(availableAction, Owner);
        this.items = items;
    }

    public List<Items> getItems() {
        return items;
    }
}