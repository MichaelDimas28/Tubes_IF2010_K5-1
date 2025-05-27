import java.awt.Graphics2D;
import java.io.BufferedReader;
// import java.io.IO;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class TileManager {
    GamePanel gp;
    public Tile[] tile;
    public int mapTileNum[][][];

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
        is = getClass().getResourceAsStream("/maps/map1.txt");
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

        loadMap("/maps/map1.txt", 0);
    }

    public void getTileImage() {
        for (int i=0 ; i < fileNames.size() ; i++) {
            String fileName;
            boolean collision;

            fileName = fileNames.get(i);
            if (collisionStatus.get(i).equals("true")) {
                collision = true;
            } else {
                collision = false;
            }
            setup(i, fileName, collision);
        }
    }

    public void setup(int index, String imageName, boolean collision) {
        // UtilityTool uTool = new UtilityTool();

        try {
            tile[index] = new Tile();
            tile[index].image = ImageIO.read(getClass().getResourceAsStream("/tiles/"+imageName));
            // tile[index].image = uTool.scaleImage(tile[index].image, gp.tileSize, gp.tileSize);
            tile[index].collision = collision;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadMap(String mapFile, int map) {
        try {
            InputStream is = getClass().getResourceAsStream(mapFile);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            int col = 0;
            int row = 0;
            while (col < gp.maxWorldCol && row < gp.maxWorldRow) {
                String line = br.readLine(); // baca satu baris dari txt

                while (col < gp.maxWorldCol) {
                    String numbers[] = line.split(" ");
                    int num = Integer.parseInt(numbers[col]); //Convert String menjadi integer

                    mapTileNum[map][col][row] = num;
                    col++;
                }
                if (col == gp.maxWorldCol) {
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
            int worldX = worldCol * gp.tileSize;
            int worldY = worldRow * gp.tileSize;
            int screenX = worldX - gp.player.worldX + gp.player.screenX;
            int screenY = worldY - gp.player.worldY + gp.player.screenY;

            if (worldX+gp.tileSize > gp.player.worldX - gp.player.screenX && worldX-gp.tileSize < gp.player.worldX + gp.player.screenX && worldY+gp.tileSize > gp.player.worldY - gp.player.screenY && worldY-gp.tileSize < gp.player.worldY + gp.player.screenY) {
                g2.drawImage(tile[tileNum].image, screenX, screenY, gp.tileSize, gp.tileSize, null);
            }
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
