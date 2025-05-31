import java.util.ArrayList;
import java.util.List;

public class Store {
    GamePanel gp;
    private List<Items> springStore = new ArrayList<>();
    private List<Items> summerStore = new ArrayList<>();
    private List<Items> fallStore = new ArrayList<>();
    private List<Items> winterStore = new ArrayList<>();

    public Store(GamePanel gp) {
        this.gp = gp;
        loadStore();
    }

    public void loadStore() {
        // Spring Store
        springStore.add(gp.itemManager.getItem("Parsnip Seeds"));
        springStore.add(gp.itemManager.getItem("Cauliflower Seeds"));
        springStore.add(gp.itemManager.getItem("Potato Seeds"));
        springStore.add(gp.itemManager.getItem("Wheat Seeds (Spring)"));
        springStore.add(gp.itemManager.getItem("Parsnip"));
        springStore.add(gp.itemManager.getItem("Cauliflower"));
        springStore.add(gp.itemManager.getItem("Wheat"));
        springStore.add(gp.itemManager.getItem("Blueberry"));
        springStore.add(gp.itemManager.getItem("Tomato"));
        springStore.add(gp.itemManager.getItem("Pumpkin"));
        springStore.add(gp.itemManager.getItem("Grape"));
        springStore.add(gp.itemManager.getItem("Eggplant"));
        springStore.add(gp.itemManager.getItem("Egg"));
        springStore.add(gp.itemManager.getItem("Firewood"));
        springStore.add(gp.itemManager.getItem("Coal"));
        springStore.add(gp.itemManager.getItem("Proposal Ring"));
        springStore.add(gp.itemManager.getItem("Fish n' Chips"));
        springStore.add(gp.itemManager.getItem("Baguette"));
        springStore.add(gp.itemManager.getItem("Sashimi"));
        springStore.add(gp.itemManager.getItem("Wine"));
        springStore.add(gp.itemManager.getItem("Pumpkin Pie"));
        springStore.add(gp.itemManager.getItem("Veggie Soup"));
        springStore.add(gp.itemManager.getItem("Fish Stew"));
        springStore.add(gp.itemManager.getItem("Fish Sandwich"));
        springStore.add(gp.itemManager.getItem("Cooked Pig's Head"));

        // Summer Store
        summerStore.add(gp.itemManager.getItem("Blueberry Seeds"));
        summerStore.add(gp.itemManager.getItem("Tomato Seeds"));
        summerStore.add(gp.itemManager.getItem("Hot Pepper Seeds"));
        summerStore.add(gp.itemManager.getItem("Melon Seeds"));
        summerStore.add(gp.itemManager.getItem("Parsnip"));
        summerStore.add(gp.itemManager.getItem("Cauliflower"));
        summerStore.add(gp.itemManager.getItem("Wheat"));
        summerStore.add(gp.itemManager.getItem("Blueberry"));
        summerStore.add(gp.itemManager.getItem("Tomato"));
        summerStore.add(gp.itemManager.getItem("Pumpkin"));
        summerStore.add(gp.itemManager.getItem("Grape"));
        summerStore.add(gp.itemManager.getItem("Eggplant"));
        summerStore.add(gp.itemManager.getItem("Egg"));
        summerStore.add(gp.itemManager.getItem("Firewood"));
        summerStore.add(gp.itemManager.getItem("Coal"));
        summerStore.add(gp.itemManager.getItem("Proposal Ring"));
        summerStore.add(gp.itemManager.getItem("Fish n' Chips"));
        summerStore.add(gp.itemManager.getItem("Baguette"));
        summerStore.add(gp.itemManager.getItem("Sashimi"));
        summerStore.add(gp.itemManager.getItem("Wine"));
        summerStore.add(gp.itemManager.getItem("Pumpkin Pie"));
        summerStore.add(gp.itemManager.getItem("Veggie Soup"));
        summerStore.add(gp.itemManager.getItem("Fish Stew"));
        summerStore.add(gp.itemManager.getItem("Fish Sandwich"));
        summerStore.add(gp.itemManager.getItem("Cooked Pig's Head"));

        // Fall Store
        fallStore.add(gp.itemManager.getItem("Cranberry Seeds"));
        fallStore.add(gp.itemManager.getItem("Pumpkin Seeds"));
        fallStore.add(gp.itemManager.getItem("Grape Seeds"));
        fallStore.add(gp.itemManager.getItem("Wheat Seeds (Fall)"));
        fallStore.add(gp.itemManager.getItem("Eggplant Seeds"));
        fallStore.add(gp.itemManager.getItem("Parsnip"));
        fallStore.add(gp.itemManager.getItem("Cauliflower"));
        fallStore.add(gp.itemManager.getItem("Wheat"));
        fallStore.add(gp.itemManager.getItem("Blueberry"));
        fallStore.add(gp.itemManager.getItem("Tomato"));
        fallStore.add(gp.itemManager.getItem("Pumpkin"));
        fallStore.add(gp.itemManager.getItem("Grape"));
        fallStore.add(gp.itemManager.getItem("Eggplant"));
        fallStore.add(gp.itemManager.getItem("Egg"));
        fallStore.add(gp.itemManager.getItem("Firewood"));
        fallStore.add(gp.itemManager.getItem("Coal"));
        fallStore.add(gp.itemManager.getItem("Proposal Ring"));
        fallStore.add(gp.itemManager.getItem("Fish n' Chips"));
        fallStore.add(gp.itemManager.getItem("Baguette"));
        fallStore.add(gp.itemManager.getItem("Sashimi"));
        fallStore.add(gp.itemManager.getItem("Wine"));
        fallStore.add(gp.itemManager.getItem("Pumpkin Pie"));
        fallStore.add(gp.itemManager.getItem("Veggie Soup"));
        fallStore.add(gp.itemManager.getItem("Fish Stew"));
        fallStore.add(gp.itemManager.getItem("Fish Sandwich"));
        fallStore.add(gp.itemManager.getItem("Cooked Pig's Head"));

        // Winter Store
        winterStore.add(gp.itemManager.getItem("Parsnip"));
        winterStore.add(gp.itemManager.getItem("Cauliflower"));
        winterStore.add(gp.itemManager.getItem("Wheat"));
        winterStore.add(gp.itemManager.getItem("Blueberry"));
        winterStore.add(gp.itemManager.getItem("Tomato"));
        winterStore.add(gp.itemManager.getItem("Pumpkin"));
        winterStore.add(gp.itemManager.getItem("Grape"));
        winterStore.add(gp.itemManager.getItem("Eggplant"));
        winterStore.add(gp.itemManager.getItem("Egg"));
        winterStore.add(gp.itemManager.getItem("Firewood"));
        winterStore.add(gp.itemManager.getItem("Coal"));
        winterStore.add(gp.itemManager.getItem("Proposal Ring"));
        winterStore.add(gp.itemManager.getItem("Fish n' Chips"));
        winterStore.add(gp.itemManager.getItem("Baguette"));
        winterStore.add(gp.itemManager.getItem("Sashimi"));
        winterStore.add(gp.itemManager.getItem("Wine"));
        winterStore.add(gp.itemManager.getItem("Pumpkin Pie"));
        winterStore.add(gp.itemManager.getItem("Veggie Soup"));
        winterStore.add(gp.itemManager.getItem("Fish Stew"));
        winterStore.add(gp.itemManager.getItem("Fish Sandwich"));
        winterStore.add(gp.itemManager.getItem("Cooked Pig's Head"));
    }
    
    public List<Items> getSpringItems() {
        return springStore;
    }
    public List<Items> getSummerItems() {
        return summerStore;
    }
    public List<Items> getFallItems() {
        return fallStore;
    }
    public List<Items> getWinterItems() {
        return winterStore;
    }

    public List<Items> getCurrentSeasonItems() {
        List<Items> storeList = new ArrayList<>();
        if (gp.farm.getSeason() == Season.Spring) {
            storeList = getSpringItems();
        } else if (gp.farm.getSeason() == Season.Summer) {
            storeList = getSummerItems();
        } else if (gp.farm.getSeason() == Season.Fall) {
            storeList = getFallItems();
        } else if (gp.farm.getSeason() == Season.Winter) {
            storeList = getWinterItems();
        }
        return storeList;
    }

    public int calculateTotalPrice(List<InventoryItem> cartItems) {
        int totalPrice = 0;
        for (InventoryItem item : cartItems) {
            totalPrice += item.getItem().getBuyPrice();
        }
        return totalPrice;
    }


}