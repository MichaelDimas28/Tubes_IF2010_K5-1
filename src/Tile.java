public class Tile {
    private char tile;
    private boolean accessability;

    public Tile(char tile) {
        this.tile = tile;
        this.accessability = (tile!='o'||tile!='h'||tile!='s'||tile!='x');
    }

    public boolean isAccessible() {
        return accessability;
    }

    public char getTile() {
        return tile;
    }
}
