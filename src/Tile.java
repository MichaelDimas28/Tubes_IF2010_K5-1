import java.awt.image.BufferedImage;

public class Tile {
    public BufferedImage image;
    public boolean collision = false;
    public String imageName = "";

    public BufferedImage getImage() {
        return image;
    }

    public String getImageName() {
        return imageName;
    }

    public void setTile(BufferedImage image, String imageName) {
        this.image = image;
        this.imageName = imageName;
    }

    public boolean isAccessible() {
        return !collision;
    }
}
