import java.awt.image.BufferedImage;

public abstract class Items {
    private String itemName;
    protected int buyPrice;
    protected int sellPrice;
    private String imageName;
    private BufferedImage image;

    public Items(String itemName, int buyPrice, int sellPrice, String imageName) {
        this.itemName = itemName;
        this.buyPrice = buyPrice;
        this.sellPrice = sellPrice;
        this.imageName = imageName;
    }
    public Items(String itemName, int buyPrice, String imageName) {
        this.itemName = itemName;
        this.buyPrice = buyPrice;
        sellPrice = 0;
        this.imageName = imageName;
    }
    public Items(String itemName, String imageName) {
        this.itemName = itemName;
        buyPrice = 0;
        sellPrice = 0;
        this.imageName = imageName;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public BufferedImage getImage() {
        return image;
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

    public String getImageName() {
    return imageName;
    }
}