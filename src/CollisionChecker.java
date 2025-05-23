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

        switch(player.direction) {
            case "up":
            playerTopRow = (playerTopWorldY-player.speed)/gp.tileSize;
            tileNum1 = gp.tileM.mapTileNum[playerLeftCol][playerTopRow];
            tileNum2 = gp.tileM.mapTileNum[playerRightCol][playerTopRow];
            if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                player.collisionOn = true;
            }
            break;
            case "down":
            playerBottomRow = (playerBottomWorldY+player.speed)/gp.tileSize;
            tileNum1 = gp.tileM.mapTileNum[playerLeftCol][playerBottomRow];
            tileNum2 = gp.tileM.mapTileNum[playerRightCol][playerBottomRow];
            if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                player.collisionOn = true;
            }
            break;
            case "left":
            playerLeftCol = (playerLeftWorldX-player.speed)/gp.tileSize;
            tileNum1 = gp.tileM.mapTileNum[playerLeftCol][playerTopRow];
            tileNum2 = gp.tileM.mapTileNum[playerLeftCol][playerBottomRow];
            if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                player.collisionOn = true;
            }
            break;
            case "right":
            playerRightCol = (playerLeftWorldX+player.speed)/gp.tileSize;
            tileNum1 = gp.tileM.mapTileNum[playerRightCol][playerTopRow];
            tileNum2 = gp.tileM.mapTileNum[playerRightCol][playerBottomRow];
            if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
                player.collisionOn = true;
            }
                break;
        }
    }
}
