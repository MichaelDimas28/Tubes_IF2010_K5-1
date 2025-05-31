public class Recipe extends Items {
    private String unlocksFoodName;
    public Recipe(String itemName, int buyPrice, String imageName, String unlocksFoodName) {
        // Harga jual resep biasanya 0 karena hanya dibeli sekali
        super(itemName, buyPrice, 0, imageName); 
        this.unlocksFoodName = unlocksFoodName;
    }
    public String getUnlocksFoodName() {
        return unlocksFoodName;
    }
}
