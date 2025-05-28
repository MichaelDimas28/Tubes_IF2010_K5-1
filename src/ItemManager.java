import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;


import java.io.IOException;
import java.util.List;

public class ItemManager {
    private Map<String, Items> itemMap = new HashMap<>();

    public ItemManager() {
        loadItems();
    }

    public void loadItems() {
        // Spring Seeds
        itemMap.put("Parsnip Seeds", new Seeds("Parsnip Seeds", 20, Season.Spring, 1, "seed_spring.png"));
        itemMap.put("Cauliflower Seeds", new Seeds("Cauliflower Seeds", 80, Season.Spring, 5, "seed_spring.png"));
        itemMap.put("Potato Seeds", new Seeds("Potato Seeds", 50, Season.Spring, 3, "seed_spring.png"));
        itemMap.put("Wheat Seeds (Spring)", new Seeds("Wheat Seeds", 60, Season.Spring, 1, "seed_spring.png"));
        
        //Summer Seeds
        itemMap.put("Blueberry Seeds", new Seeds("Blueberry Seeds", 80, Season.Summer, 7, "seed_summer.png"));
        itemMap.put("Tomato Seeds", new Seeds("Tomato Seeds", 50, Season.Summer, 3, "seed_summer.png"));
        itemMap.put("Hot Pepper Seeds", new Seeds("Hot Pepper Seeds", 40, Season.Summer, 1, "seed_summer.png"));
        itemMap.put("Melon Seeds", new Seeds("Melon Seeds", 80, Season.Summer, 4, "seed_summer.png"));
        
        // Fall Seeds
        itemMap.put("Cranberry Seeds", new Seeds("Cranberry Seeds", 100, Season.Fall, 2, "seed_fall.png"));
        itemMap.put("Pumpkin Seeds", new Seeds("Pumpkin Seeds", 150, Season.Fall, 7, "seed_fall.png"));
        itemMap.put("Wheat Seeds (Fall)", new Seeds("Wheat Seeds", 60, Season.Fall, 1, "seed_fall.png"));
        itemMap.put("Grape Seeds", new Seeds("Grape Seeds", 60, Season.Fall, 3, "seed_fall.png"));
        itemMap.put("Eggplant Seeds", new Seeds("Eggplant Seeds", 100, Season.Fall, 5, "seed_fall.png"));

        // Common Fish
        itemMap.put("Bullhead", new Fish("Bullhead", List.of("Any"), List.of(new int[]{0, 2400}), List.of("Any"), List.of("Mountain Lake"), Rarity.Common, "common_fish.png"));
        itemMap.put("Carp", new Fish("Carp", List.of("Any"), List.of(new int[]{0, 2400}), List.of("Any"), List.of("Mountain Lake, Pond"), Rarity.Common, "common_fish.png"));
        itemMap.put("Chub", new Fish("Chub", List.of("Any"), List.of(new int[]{0, 2400}), List.of("Any"), List.of("Forest River, Mountain Lake"), Rarity.Common, "common_fish.png"));
        
        // Regular Fish
        itemMap.put("Largemouth Bass", new Fish("Largemouth Bass", List.of("Any"), List.of(new int[]{600, 1800}), List.of("Any"), List.of("Mountain Lake"), Rarity.Regular, "regular_fish.png"));
        itemMap.put("Rainbow Trout", new Fish("Rainbow Trout", List.of("Summer"), List.of(new int[]{600, 1800}), List.of("Sunny"), List.of("Mountain Lake, Forest River"), Rarity.Regular, "regular_fish.png"));
        itemMap.put("Sturgeon", new Fish("Sturgeon", List.of("Summer", "Winter"), List.of(new int[]{600, 1800}), List.of("Any"), List.of("Mountain Lake"), Rarity.Regular, "regular_fish.png"));
        itemMap.put("Midnight Carp", new Fish("Midnight Carp", List.of("Winter","Fall"), List.of(new int[]{2000, 2400}, new int[]{0, 200}), List.of("Any"), List.of("Mountain Lake, Pond"), Rarity.Regular, "regular_fish.png"));
        itemMap.put("Flounder", new Fish("Flounder", List.of("Spring","Summer"), List.of(new int[]{600, 2200}), List.of("Any"), List.of("Ocean"), Rarity.Regular, "regular_fish.png"));
        itemMap.put("Halibut", new Fish("Halibut", List.of("Any"), List.of(new int[]{600, 1100}, new int[]{1900,2400}, new int[]{0,200}), List.of("Any"), List.of("Ocean"), Rarity.Regular, "regular_fish.png"));
        itemMap.put("Octopus", new Fish("Octopus", List.of("Summer"), List.of(new int[]{600, 2200}), List.of("Any"), List.of("Ocean"), Rarity.Regular, "regular_fish.png"));
        itemMap.put("Pufferfish", new Fish("Pufferfish", List.of("Summer"), List.of(new int[]{0, 1600}), List.of("Sunny"), List.of("Ocean"), Rarity.Regular, "regular_fish.png"));
        itemMap.put("Sardine", new Fish("Sardine", List.of("Any"), List.of(new int[]{600, 1800}), List.of("Any"), List.of("Ocean"), Rarity.Regular, "regular_fish.png"));
        itemMap.put("Super Cucumber", new Fish("Super Cucumber", List.of("Summer","Fall", "Winter"), List.of(new int[]{1800, 2400}, new int[]{0,200}), List.of("Any"), List.of("Ocean"), Rarity.Regular, "regular_fish.png"));
        itemMap.put("Catfish", new Fish("Catfish", List.of("Spring", "Summer", "Fall"), List.of(new int[]{600, 2200}), List.of("Rainy"), List.of("Forest River, Pond"), Rarity.Regular, "regular_fish.png"));
        itemMap.put("Salmon", new Fish("Salmon", List.of("Fall"), List.of(new int[]{600, 1800}), List.of("Any"), List.of("Forest River"), Rarity.Regular, "regular_fish.png"));
        
        // Legendary Fish
        itemMap.put("Angler", new Fish("Angler", List.of("Fall"), List.of(new int[]{800, 2000}), List.of("Any"), List.of("Pond"), Rarity.Legendary, "legendary_fish.png"));
        itemMap.put("Crimsonfish", new Fish("Crimsonfish", List.of("Summer"), List.of(new int[]{800, 2000}), List.of("Any"), List.of("Ocean"), Rarity.Legendary, "legendary_fish.png"));
        itemMap.put("Glacierfish", new Fish("Glacierfish", List.of("Winter"), List.of(new int[]{800, 2000}), List.of("Any"), List.of("Forest River"), Rarity.Legendary, "legendary_fish.png"));
        itemMap.put("Legend", new Fish("Legend", List.of("Spring"), List.of(new int[]{800, 2000}), List.of("Rainy"), List.of("Mountain Lake"), Rarity.Legendary, "legendary_fish.png"));

        // Crops
        itemMap.put("Parsnip", new Crops("Parsnip", 50, 35, 1, "crop_spring.png"));
        itemMap.put("Cauliflower", new Crops("Cauliflower", 200, 150, 1, "crop_spring.png"));
        itemMap.put("Potato", new Crops("Potato", 0, 80, 1, "crop_spring.png"));
        itemMap.put("Wheat", new Crops("Wheat", 50, 30, 3, "crop_spring.png"));
        itemMap.put("Blueberry", new Crops("Blueberry", 150, 40, 3, "crop_summer.png"));
        itemMap.put("Tomato", new Crops("Tomato", 90, 60, 1, "crop_summer.png"));
        itemMap.put("Hot Pepper", new Crops("Hot Pepper", 0, 40, 1, "crop_summer.png"));
        itemMap.put("Melon", new Crops("Melon", 0, 250, 1, "crop_summer.png"));
        itemMap.put("Cranberry", new Crops("Cranberry", 0, 25, 10, "crop_fall.png"));
        itemMap.put("Pumpkin", new Crops("Pumpkin", 300, 250, 1, "crop_fall.png"));
        itemMap.put("Grape", new Crops("Grape", 100, 10, 20, "crop_fall.png"));
        itemMap.put("Eggplant", new Crops("Eggplant", 200, 180, 1, "crop_fall.png"));

        // Food
        Items anyFish = new Fish("Any Fish", List.of("Any"), List.of(new int[]{0, 2400}), List.of("Any"), List.of("Any"), Rarity.Common, "common_fish.png");
        Items legendFish = new Fish("Legend Fish", List.of("Any"), List.of(new int[]{0, 2400}), List.of("Any"), List.of("Any"), Rarity.Legendary, "legendary_fish.png");
        itemMap.put("Fish n' Chips", new Food("Fish n' Chips", 150, 135, 50,List.of(itemMap.get("Wheat"), itemMap.get("Potato"), anyFish, anyFish), "seafood.png", false));
        itemMap.put("Baguette", new Food("Baguette", 100, 80, 25, List.of(itemMap.get("Wheat"), itemMap.get("Wheat"), itemMap.get("Wheat")),"bread.png", true));
        itemMap.put("Sashimi", new Food("Sashimi", 300, 275, 70, List.of(itemMap.get("Salmon"), itemMap.get("Salmon"), itemMap.get("Salmon")),"seafood.png", false));
        itemMap.put("Fugu", new Food("Fugu", 0, 135, 50, List.of(itemMap.get("Pufferfish")),"seafood.png", false));
        itemMap.put("Wine", new Food("Wine", 100, 90, 20, List.of(itemMap.get("Grape"), itemMap.get("Grape")),"wine.png", true));
        itemMap.put("Pumpkin Pie", new Food("Pumpkin Pie", 120, 100, 35, List.of(itemMap.get("Wheat"), itemMap.get("Pumpkin")),"pie.png", true));
        itemMap.put("Veggie Soup", new Food("Veggie Soup", 140, 120, 40, List.of(itemMap.get("Cauliflower"), itemMap.get("Parsnip"), itemMap.get("Potato"), itemMap.get("Tomato")),"soup.png", false));
        itemMap.put("Fish Stew", new Food("Fish Stew", 280, 260, 70,List.of(anyFish, anyFish, itemMap.get("Hot Pepper"), itemMap.get("Cauliflower"),  itemMap.get("Cauliflower")), "soup.png", false));
        itemMap.put("Spakbor Salad", new Food("Spakbor Salad", 0, 250, 70,List.of(itemMap.get("Melon"), itemMap.get("Cranberry"),  itemMap.get("Blueberry"),  itemMap.get("Tomato")), "salad.png", true));
        itemMap.put("Fish Sandwich", new Food("Fish Sandwich", 200, 180, 50,List.of(anyFish, itemMap.get("Wheat"),  itemMap.get("Wheat"),  itemMap.get("Tomato"), itemMap.get("Hot Pepper")), "bread.png", false));
        itemMap.put("The Legends of Spakbor", new Food("The Legends of Spakbor", 0, 2000, 100,List.of(legendFish, itemMap.get("Potato"),  itemMap.get("Potato"),  itemMap.get("Parsnip"), itemMap.get("Tomato"), itemMap.get("Eggplant")), "legendspakbor.png", false));
        itemMap.put("Cooked Pig's Head", new Food("Cooked Pig's Head", 1000, 0, 100, "pighead.png"));

        // Equipments
        itemMap.put("Hoe", new Equipment("Hoe", "hoe.png"));
        itemMap.put("Watering Can", new Equipment("Watering Can", "wateringcan.png"));
        itemMap.put("Pickaxe", new Equipment("Pickaxe", "pickaxe.png"));
        itemMap.put("Fishing Rod", new Equipment("Fishing Rod", "fishingrod.png"));

        // Miscellaneous
        itemMap.put("Coal", new Miscellaneous("Coal", 45, 30, "coal.png"));
        itemMap.put("Firewood", new Miscellaneous("Firewood", 30, 15, "wood.png"));
        
        for (Items item : itemMap.values()) {
        item.setImage(setup(item.getImageName(), 48, 48));
        }
    }

    public Items getItem(String name) {
        return itemMap.get(name);
    }

    public BufferedImage setup(String imageName, int width, int height) {
        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/items/" + imageName));
            image = uTool.scaleImage(image, width, height);
        } catch (IOException | IllegalArgumentException e) {
            e.printStackTrace();
        }
        return image;
    }

    public Map<String, Items> getItemMap() {
    return itemMap;
    }

}
