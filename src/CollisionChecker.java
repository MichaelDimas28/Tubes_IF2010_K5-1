import java.awt.Rectangle;
import java.util.List;

public class CollisionChecker {
    GamePanel gp;
    
    public CollisionChecker(GamePanel gp) {
        this.gp = gp;
    }

    public void checkTile(Player player) {
        int playerLeftWorldX = player.worldX + player.solidArea.x;
        int playerRightWorldX = player.worldX + player.solidArea.x + player.solidArea.width;
        int playerTopWorldY = player.worldY + player.solidArea.y;
        int playerBottomWorldY = player.worldY + player.solidArea.y + player.solidArea.height;

        int playerLeftCol = playerLeftWorldX/gp.tileSize;
        int playerRightCol = playerRightWorldX/gp.tileSize;
        int playerTopRow = playerTopWorldY/gp.tileSize;
        int playerBottomRow = playerBottomWorldY/gp.tileSize;

        int tileNum1, tileNum2;
        Tile tile1, tile2;
        
        
        switch(player.direction) {
            case "up":
            playerTopRow = (playerTopWorldY-player.speed)/gp.tileSize;
            tileNum1 = gp.tileM.mapTileNum[gp.currentMap][playerLeftCol][playerTopRow];
            tileNum2 = gp.tileM.mapTileNum[gp.currentMap][playerRightCol][playerTopRow];
            tile1 = gp.tileM.tileMap.get(tileNum1);
            tile2 = gp.tileM.tileMap.get(tileNum2);
    
            if ((tile1 != null && tile1.collision) || (tile2 != null && tile2.collision)) {
                player.collisionOn = true;
            }
            break;
            case "down":
            playerBottomRow = (playerBottomWorldY+player.speed)/gp.tileSize;
            tileNum1 = gp.tileM.mapTileNum[gp.currentMap][playerLeftCol][playerBottomRow];
            tileNum2 = gp.tileM.mapTileNum[gp.currentMap][playerRightCol][playerBottomRow];
            tile1 = gp.tileM.tileMap.get(tileNum1);
            tile2 = gp.tileM.tileMap.get(tileNum2);
    
            if ((tile1 != null && tile1.collision) || (tile2 != null && tile2.collision)) {
                player.collisionOn = true;
            }
            break;
            case "left":
            playerLeftCol = (playerLeftWorldX-player.speed)/gp.tileSize;
            tileNum1 = gp.tileM.mapTileNum[gp.currentMap][playerLeftCol][playerTopRow];
            tileNum2 = gp.tileM.mapTileNum[gp.currentMap][playerLeftCol][playerBottomRow];
            tile1 = gp.tileM.tileMap.get(tileNum1);
            tile2 = gp.tileM.tileMap.get(tileNum2);
            
            if ((tile1 != null && tile1.collision) || (tile2 != null && tile2.collision)) {
                player.collisionOn = true;
            }
            break;
            case "right":
            playerRightCol = (playerRightWorldX+player.speed)/gp.tileSize;
            tileNum1 = gp.tileM.mapTileNum[gp.currentMap][playerRightCol][playerTopRow];
            tileNum2 = gp.tileM.mapTileNum[gp.currentMap][playerRightCol][playerBottomRow];
            tile1 = gp.tileM.tileMap.get(tileNum1);
            tile2 = gp.tileM.tileMap.get(tileNum2);
            if ((tile1 != null && tile1.collision) || (tile2 != null && tile2.collision)) {
                player.collisionOn = true;
            }
            break;
        }
    }

    public int checkNPC(Player player, List<NPC> npcs) {
        int index = -1;

        Rectangle playerSolidArea = new Rectangle(
            player.worldX + player.solidArea.x,
            player.worldY + player.solidArea.y,
            player.solidArea.width,
            player.solidArea.height
        );

        // Prediksi arah gerakan
        switch (player.direction) {
            case "up":    playerSolidArea.y -= gp.tileSize / 2; break;
            case "down":  playerSolidArea.y += gp.tileSize / 2; break;
            case "left":  playerSolidArea.x -= gp.tileSize / 2; break;
            case "right": playerSolidArea.x += gp.tileSize / 2; break;
        }


        for (int i = 0; i < npcs.size(); i++) {
            NPC npc = npcs.get(i);
            if (npc != null) {
                Rectangle npcArea = new Rectangle(
                    npc.worldX + npc.solidArea.x,
                    npc.worldY + npc.solidArea.y,
                    npc.solidArea.width,
                    npc.solidArea.height
                );

                if (playerSolidArea.intersects(npcArea)) {
                    player.collisionOn = true;
                    index = i;
                    break;
                }
            }
        }


        return index;
    }

}
