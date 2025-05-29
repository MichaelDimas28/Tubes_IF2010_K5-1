import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;

public class Maps {
    private String mapName;
    private int column;
    private int row;
    private Tile[][] tiles;

    public Maps(String mapName, int column, int row, String filePath) throws IOException {
        this.mapName = mapName;
        this.column = column;
        this.row = row;
        loadMap(column, row, filePath);
    }

    public void loadMap(int column, int row, String filePath) throws IOException {
        if (filePath == null) {
            throw new IllegalArgumentException("File path tidak bisa kosong");
        }
        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException("File "+filePath+" tidak ditemukan");
        }

        this.tiles = new Tile[row][column];
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {

            String line;
            int rowIdx = 0;
            
            while ((line = reader.readLine()) != null && rowIdx < row) {
                String[] mapTiles = line.trim().split("\\s+");
                if (mapTiles.length != column) {
                    throw new IOException("Jumlah kolom pada row "+rowIdx+" tidak sesuai");
                }
    
                for (int colIdx = 0; colIdx < column ; colIdx++) {
                    // tiles[rowIdx][colIdx] = new Tile(mapTiles[colIdx].charAt(0));
                }
                rowIdx++;
            }
    
            if (rowIdx != row) {
                throw new IOException("Jumlah row pada file tidak sesuai");
            }
        }
    }

    public String getMapName() {
        return mapName;
    }

    public Tile[][] getMap() {
        return tiles;
    }

    public void showMap() {
        for (int i = 0; i<row; i++) {
            for (int j = 0; j<column; j++) {
                if (j == column-1) {
                    System.out.print(tiles[i][j].getTile()+"\n");
                } else {
                    System.out.print(tiles[i][j].getTile()+" ");
                }
            }
        }
    }

    public int getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }
}
