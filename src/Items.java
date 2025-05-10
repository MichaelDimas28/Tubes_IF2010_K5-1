public abstract class Items {
    private String itemName;
    private int buyPrice;
    private int sellPrice;

    public Items(String itemName, int buyPrice, int sellPrice) {
        this.itemName = itemName;
        this.buyPrice = buyPrice;
        this.sellPrice = sellPrice;
    }

    public String getItemName() {
        return itemName;
    }

    public int getBuyPrice() {
        return buyPrice;
    }

    public int getSellPrice() {
        return sellPrice;
    }
}