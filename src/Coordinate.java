public class Coordinate {
    private int column;
    private int row;

    public Coordinate(int col, int row) {
        this.column = col;
        this.row = row;
    }

    public void setColumn(int col) {
        this.column = col;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }
}
