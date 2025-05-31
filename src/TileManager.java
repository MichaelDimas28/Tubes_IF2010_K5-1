import java.awt.Graphics2D;
import java.io.BufferedReader;
// import java.io.IO;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import java.util.HashMap;
import java.util.Map;
    
    public class TileManager {
        GamePanel gp;
        public Tile[] tile;
        public int mapTileNum[][][];
        public int[] mapCols;
        public int[] mapRows;
        public Map<Integer, Tile> tileMap = new HashMap<>();
        
        ArrayList<String> fileNames = new ArrayList<>();
    ArrayList<String> collisionStatus = new ArrayList<>();

    public TileManager(GamePanel gp) {
        this.gp = gp;

        // untuk membaca tiledata.txt
        InputStream is = getClass().getResourceAsStream("/maps/tiledata.txt");
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        
        // untuk menyimpan data collision dari setiap tile dari tiledata.txt
        String line;
        try {
            while ((line = br.readLine()) != null) {
                fileNames.add(line);
                collisionStatus.add(br.readLine());
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //inisialisasi tile array
        tile = new Tile[fileNames.size()];
        getTileImage();

        //mendapatkan row dan col map
        is = getClass().getResourceAsStream("/maps/farm_map.txt");
        br = new BufferedReader(new InputStreamReader(is));

        try {
            String readline = br.readLine(); //baca 1 baris dari map
            String maxTile[] = readline.split(" "); //Tidak membaca spasi pada txt

            gp.maxWorldCol = maxTile.length;
            gp.maxWorldRow = maxTile.length;
            mapTileNum = new int[gp.maxMap][gp.maxWorldCol][gp.maxWorldRow];

            br.close();
        } catch (IOException e) {
            System.out.println("Exception!");
        }
        mapCols = new int[gp.maxMap];
        mapRows = new int[gp.maxMap];

        loadMap("/maps/farm_map.txt", 0, 32, 32);
        loadMap("/maps/npc_house.txt", 1, 16, 9);
        loadMap("/maps/npc_house.txt", 2, 16, 9);
        loadMap("/maps/npc_house.txt", 3, 16, 9);
        loadMap("/maps/npc_house.txt", 4, 16, 9);
        loadMap("/maps/npc_house.txt", 5, 16, 9);
        loadMap("/maps/store.txt", 6, 18, 12);
        loadMap("/maps/ocean.txt", 7, 5, 10);
        loadMap("/maps/forest_river.txt", 8, 21, 21);
        loadMap("/maps/mountain_lake.txt", 9, 20, 20);
        loadMap("/maps/world_map.txt", 10, 18, 8);
        loadMap("/maps/player_house.txt", 11, 24, 24); // House
        loadMap("/maps/player_house.txt", 12, 24, 24); // House
    }

    public void getTileImage() {
    for (int i = 0; i < fileNames.size(); i++) {
        String fileName = fileNames.get(i);
        boolean collision = collisionStatus.get(i).equals("true");

        // Ambil angka tile ID dari nama file, misal: 200.png → 200
        int tileId = Integer.parseInt(fileName.replace(".png", ""));
        setup(tileId, fileName, collision);
    }
}

    public void setup(int index, String imageName, boolean collision) {
        UtilityTool uTool = new UtilityTool();
    // Untuk mengecek apakah ada Tiles yang ada pada tiledata namun tidak ada pada folder tiles
    //     InputStream stream = getClass().getResourceAsStream("/tiles/" + imageName);
    // if (stream == null) {
    //     System.err.println(" Tile image missing: " + imageName);
    //     return; // skip setup
    // }

    try {
        Tile newTile = new Tile();
        newTile.image = ImageIO.read(getClass().getResourceAsStream("/tiles/" + imageName));
        newTile.imageName = imageName;
        newTile.image = uTool.scaleImage(newTile.image, gp.tileSize, gp.tileSize);
        newTile.collision = collision;
        tileMap.put(index, newTile);  // pakai HashMap
    } catch (IOException e) {
        System.out.println("Gagal load tile: " + imageName);
        e.printStackTrace();
    }
}

    public boolean isShippingBin(int tileIndex) {
        return tileIndex == 222 || tileIndex == 211 || tileIndex == 200 || tileIndex == 255 || tileIndex == 244 || tileIndex == 233;
    }

    public String getFrontTile() {
        int tileX = gp.player.worldX / gp.tileSize;
        int tileY = gp.player.worldY / gp.tileSize;

        switch (gp.player.direction) {
            case "up":
                tileY -= 1;
                break;
            case "down":
                tileY += 1;
                break;
            case "left":
                tileX -= 1;
                break;
            case "right":
                tileX += 1;
                break;
        }

    if (tileX >= 0 && tileX < gp.maxWorldCol && tileY >= 0 && tileY < gp.maxWorldRow) {
        int tileIndex = mapTileNum[gp.currentMap][tileX][tileY];
            return gp.tileM.tileMap.get(tileIndex).getImageName();
        } else {
            return null; // di luar batas map
        }
    }

    public int getFrontTileID() { // Metode baru atau modifikasi getFrontTile
        int tileX = gp.player.worldX / gp.tileSize;
        int tileY = gp.player.worldY / gp.tileSize;
        int playerDirectionX = 0;
        int playerDirectionY = 0;

        switch (gp.player.direction) {
            case "up":
                tileY -= 1;
                playerDirectionY = -1;
                break;
            case "down":
                tileY += 1;
                playerDirectionY = 1;
                break;
            case "left":
                tileX -= 1;
                playerDirectionX = -1;
                break;
            case "right":
                tileX += 1;
                playerDirectionX = 1;
                break;
        }

        // Boundary checks (penting agar tidak error ArrayOutOfBounds)
        if (tileX >= 0 && tileX < gp.maxWorldCol && tileY >= 0 && tileY < gp.maxWorldRow) {
            // Pastikan gp.currentMap sudah benar dan mapTileNum[gp.currentMap] terisi
            if (gp.currentMap >= 0 && gp.currentMap < mapTileNum.length &&
                mapTileNum[gp.currentMap] != null &&
                tileX < mapTileNum[gp.currentMap].length &&
                mapTileNum[gp.currentMap][tileX] != null &&
                tileY < mapTileNum[gp.currentMap][tileX].length) {
                return mapTileNum[gp.currentMap][tileX][tileY];
            }
        }
        return -1; // Mengembalikan nilai invalid jika di luar batas atau error
    }


    public int getTileIDAtPlayerPosition() {
        int tileX = gp.player.worldX / gp.tileSize;
        int tileY = gp.player.worldY / gp.tileSize;

        if (tileX >= 0 && tileX < gp.maxWorldCol && tileY >= 0 && tileY < gp.maxWorldRow) {
            if (gp.currentMap >= 0 && gp.currentMap < mapTileNum.length &&
                mapTileNum[gp.currentMap] != null &&
                tileX < mapTileNum[gp.currentMap].length &&
                mapTileNum[gp.currentMap][tileX] != null &&
                tileY < mapTileNum[gp.currentMap][tileX].length) {
                return mapTileNum[gp.currentMap][tileX][tileY];
            }
        }
        return -1;
    }



    public void loadMap(String mapFile, int map, int colSize, int rowSize) {
        mapCols[map] = colSize;
        mapRows[map] = rowSize;
        try {
            InputStream is = getClass().getResourceAsStream(mapFile);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            int col = 0;
            int row = 0;
            while (col < colSize && row < rowSize) {
                String line = br.readLine(); // baca satu baris dari txt

                while (col < colSize) {
                    String numbers[] = line.split(" ");
                    int num = Integer.parseInt(numbers[col]); //Convert String menjadi integer

                    mapTileNum[map][col][row] = num;
                    col++;
                }
                if (col == colSize) {
                    col = 0;
                    row++;
                }
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2) {
        int worldCol = 0;
        int worldRow = 0;
        // int x = 0;
        // int y = 0;
        

        while (worldCol < gp.maxWorldCol && worldRow < gp.maxWorldRow) {
            int tileNum = mapTileNum[gp.currentMap][worldCol][worldRow];
            Tile t = tileMap.get(tileNum);

            
            int worldX = worldCol * gp.tileSize;
            int worldY = worldRow * gp.tileSize;
            int screenX = worldX - gp.player.worldX + gp.player.screenX;
            int screenY = worldY - gp.player.worldY + gp.player.screenY;
            
            if (t != null) {
                g2.drawImage(t.image, screenX, screenY, null);
            }
            // if (worldX+gp.tileSize > gp.player.worldX - gp.player.screenX && worldX-gp.tileSize < gp.player.worldX + gp.player.screenX && worldY+gp.tileSize > gp.player.worldY - gp.player.screenY && worldY-gp.tileSize < gp.player.worldY + gp.player.screenY) {
            //     g2.drawImage(tile[tileNum].image, screenX, screenY, gp.tileSize, gp.tileSize, null);
            // }
            worldCol++;
            // x+= gp.tileSize;
            if (worldCol == gp.maxWorldCol) {
                worldCol = 0;
                // x = 0;
                worldRow++;
                // y += gp.tileSize;
            }
        }
    }
}
