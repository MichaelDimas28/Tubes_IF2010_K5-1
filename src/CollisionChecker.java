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
}
