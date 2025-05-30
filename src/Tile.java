import java.awt.image.BufferedImage;

public class Tile {
    // private char tile;
    public BufferedImage image;
    public boolean collision = false;
    public String imageName = "";

    // public Tile(char tile) {
    //     this.tile = tile;
    //     this.collision = (tile!='o'||tile!='h'||tile!='s'||tile!='x');
    // }

    public boolean isAccessible() {
        return collision;
    }

    // public char getTile() {
    //     return tile;
    // }

    public BufferedImage getImage() {
        return image;
    }

    public String getImageName() {
        return imageName;
    }

    public void setTile(BufferedImage image) {
        this.image = image;
    }
}
