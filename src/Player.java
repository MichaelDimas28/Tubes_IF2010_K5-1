import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

// import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
// import java.util.HashMap;
import java.util.Arrays;
import java.util.HashMap;

public class Player implements Action {
    public int worldX, worldY;
    public int speed;
    public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
    public BufferedImage hoeUp1, hoeUp2, hoeDown1, hoeDown2, hoeRight1, hoeRight2, hoeLeft1, hoeLeft2, waterUp1, waterUp2, waterDown1, waterDown2, waterLeft1, waterLeft2, waterRight1, waterRight2, rodUp1, rodUp2, rodDown1, rodDown2, rodLeft1, rodLeft2, rodRight1, rodRight2;
    public BufferedImage guidebox;
    public String direction;
    public int spriteCounter = 0;
    public int spriteNum = 1;
    public Rectangle solidArea; //hitbox karakter player
    public boolean collisionOn = false;
    public boolean tilling = false;
    public boolean watering = false;
    public boolean fishing = false;
    public boolean recoverLand = false;

    public boolean canTill = true;
    public int tillCooldownCounter = 0;
    public final int tillCooldownMax = 30; // frame (30 = 0.5 detik kalau 60FPS)
    public boolean canWater = true;
    public int waterCooldownCounter = 0;
    public final int waterCooldownMax = 30; // frame (30 = 0.5 detik kalau 60FPS)
    public boolean canFish = true;
    public int fishCooldownCounter = 0;
    public final int fishCooldownMax = 30; // frame (30 = 0.5 detik kalau 60FPS)
    int fishingPhase = 0; // 0 = belum dimulai, 1 = rod_1, 2 = rod_2
    int fishingFrameCounter = 0;

    private boolean hasHarvestedForFirstTime = false;
    private boolean hotPepperObtained = false; 
    public boolean isCooking = false;
    private Food recipeBeingCooked = null;
    private int cookingCompletionDay;
    private int cookingCompletionHour;
    private int cookingCompletionMinute;
    private boolean coalPartiallyUsed = false;

    public UI ui;
    GamePanel gp;
    KeyHandler keyH;

    public final int screenX;
    public final int screenY;
    int standCounter = 0; //counter agar player kembali ke gambar idle ketika tidak ada input
    boolean moving = false;
    int pixelCounter = 0;

    private String name;
    private Gender gender;
    private int energy = 100;
    private int gold;
    private Items itemHeld = null;
    private int total_income = 0;
    private int total_expenditure = 0;
    private float avarage_season_income = 0;
    private float avarage_season_expenditure = 0;
    private int total_days_played = 0;
    private int crops_harvested = 0;
    private int fish_caught = 0;
    private int tileCol;
    private int tileRow;
    FarmTile targetTile;
    private Coordinate coordinate = new Coordinate(0,0);
    private Inventory inventory = new Inventory();
    // public final int maxInventorySize = 60;
    private List<NPC> npcRelationshipStats = new ArrayList<>();

    private String forbiddenMessage = "Anda tidak bisa menggunakan item ini di sini";
   
    //Constructor
    public Player(String name, Gender gender, int energy, int gold, GamePanel gp, KeyHandler keyH){
        this.name = name;
        this.gender = gender;
        this.energy = energy;
        this.gold = gold;
        this.gp = gp;
        this.keyH = keyH;
        
        screenX = gp.screenWidth/2 - (gp.tileSize/2);
        screenY = gp.screenHeight/2 - (gp.tileSize/2);
        
        solidArea = new Rectangle(1, 1, 46, 46);
        // solidArea.x = 1;
        // solidArea.y = 1;
        // solidArea.width = 46;
        // solidArea.height = 46;
        
        setDefaultValues(gp.tileSize*14, gp.tileSize*15, "down");
        getPlayerImage();
        getPlayerHoeImage();
        getPlayerWaterImage();
        getPlayerFishingImage();
        setItems();
    }
    
    public void setDefaultValues(int worldX, int worldY, String direction) {
        this.worldX = worldX;
        this.worldY = worldY;
        speed = 4;
        this.direction = direction;
    }
    
    public void getPlayerImage() {
        up1 = setup("char_up_1.png", gp.tileSize, gp.tileSize);
        up2 = setup("char_up_2.png", gp.tileSize, gp.tileSize);
        down1 = setup("char_down_1.png", gp.tileSize, gp.tileSize);
        down2 = setup("char_down_2.png", gp.tileSize, gp.tileSize);
        left1 = setup("char_left_1.png", gp.tileSize, gp.tileSize);
        left2 = setup("char_left_2.png", gp.tileSize, gp.tileSize);
        right1 = setup("char_right_1.png", gp.tileSize, gp.tileSize);
        right2 = setup("char_right_2.png", gp.tileSize, gp.tileSize);
        try {
            guidebox = ImageIO.read(getClass().getResourceAsStream("/objects/guidebox.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    
    public void getPlayerHoeImage() {
        hoeUp1 = setup("char_hoe_up_1.png", gp.tileSize*2, gp.tileSize*2);
        hoeUp2 = setup("char_hoe_up_2.png", gp.tileSize, gp.tileSize*2);
        hoeDown1 = setup("char_hoe_down_1.png", gp.tileSize, gp.tileSize*2);
        hoeDown2 = setup("char_hoe_down_2.png", gp.tileSize, gp.tileSize*3);
        hoeLeft1 = setup("char_hoe_left_1.png", gp.tileSize, gp.tileSize*2);
        hoeLeft2 = setup("char_hoe_left_2.png", gp.tileSize*2, gp.tileSize*2);
        hoeRight1 = setup("char_hoe_right_1.png", gp.tileSize, gp.tileSize*2);
        hoeRight2 = setup("char_hoe_right_2.png", gp.tileSize*2, gp.tileSize*2);
    }
    
    public void getPlayerFishingImage() {
        rodUp1 = setup("char_rod_up_1.png", gp.tileSize*2, gp.tileSize*2);
        rodUp2 = setup("char_rod_up_2.png", gp.tileSize, gp.tileSize*2);
        rodDown1 = setup("char_rod_down_1.png", gp.tileSize, gp.tileSize*2);
        rodDown2 = setup("char_rod_down_2.png", gp.tileSize, gp.tileSize*2);
        rodLeft1 = setup("char_rod_left_1.png", gp.tileSize, gp.tileSize*2);
        rodLeft2 = setup("char_rod_left_2.png", gp.tileSize*2, gp.tileSize*2);
        rodRight1 = setup("char_rod_right_1.png", gp.tileSize, gp.tileSize*2);
        rodRight2 = setup("char_rod_right_2.png", gp.tileSize*2, gp.tileSize*2);
    }
    
    public void getPlayerWaterImage() {
        waterUp1 = setup("char_watering_up_1.png", gp.tileSize, gp.tileSize*2);
        waterUp2 = setup("char_watering_up_2.png", gp.tileSize, gp.tileSize*2);
        waterDown1 = setup("char_watering_down_1.png", gp.tileSize, gp.tileSize*2);
        waterDown2 = setup("char_watering_down_2.png", gp.tileSize, gp.tileSize*2);
        waterLeft1 = setup("char_watering_left_1.png", gp.tileSize*2, gp.tileSize);
        waterLeft2 = setup("char_watering_left_2.png", gp.tileSize*2, gp.tileSize);
        waterRight1 = setup("char_watering_right_1.png", gp.tileSize*2, gp.tileSize);
        waterRight2 = setup("char_watering_right_2.png", gp.tileSize*2, gp.tileSize);
    }
    
    public BufferedImage setup(String imageName, int width, int height) {
        UtilityTool uTool = new UtilityTool();
        
        BufferedImage image = null;
        
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/player/"+imageName));
            image = uTool.scaleImage(image, width, height);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }
    
    public void setItems() {
        inventory.addItem(new InventoryItem(gp.itemManager.getItem("Watering Can"), 1));
        inventory.addItem(new InventoryItem(gp.itemManager.getItem("Hoe"), 1));
        inventory.addItem(new InventoryItem(gp.itemManager.getItem("Fishing Rod"), 1));
        inventory.addItem(new InventoryItem(gp.itemManager.getItem("Pickaxe"), 1));
        inventory.addItem(new InventoryItem(gp.itemManager.getItem("Parsnip Seeds"), 15));
        inventory.addItem(new InventoryItem(gp.itemManager.getItem("Parsnip"), 10));

    }
    
    public void update() {
        tileCol = (worldX + solidArea.x) / gp.tileSize;
        tileRow = (worldY + solidArea.y) / gp.tileSize;
        switch (direction) {
            case "up":
            tileRow -= 1;
            // if (tileCol)
            break;
            case "down":
            tileRow += 1;
            break;
            case "left":
            tileCol -= 1;
            break;
            case "right":
            tileCol += 1;
            break;
        }
        targetTile = gp.farm.getTileAt(tileCol, tileRow);
        
        if (targetTile == null) {
            gp.ui.showMessage("Tidak dapat menggunakan item di luar batas lahan!");
            keyH.enterPressed = false;
            return;
        }
        if (gp.dialogueOn) {
            return;
        }

        if (tilling) {
            tilling();
            // tile();
        }
        if (!canTill) {
            tillCooldownCounter++;
            if (tillCooldownCounter > tillCooldownMax) {
                canTill = true;
                tillCooldownCounter = 0;
            }
        }

        if (recoverLand) {
            tilling();
            // recoverLand();
        }
        if (!canTill) {
            tillCooldownCounter++;
            if (tillCooldownCounter > tillCooldownMax) {
                canTill = true;
                tillCooldownCounter = 0;
            }
        }

        if (watering) {
            watering();
            // water();
        }
        if (!canWater) {
            waterCooldownCounter++;
            if (waterCooldownCounter > waterCooldownMax) {
                canWater = true;
                waterCooldownCounter = 0;
            }
        }

        if (fishing) {
            fishing();
            // fish();
        }
        if (!canFish) {
            fishCooldownCounter++;
            if (fishCooldownCounter > fishCooldownMax) {
                canFish = true;
                fishCooldownCounter = 0;
            }
        }

        // Reset animasi setelah fishing selesai
        if (fishing && !gp.ui.fishingActive) {
            fishing = false;
            fishingPhase = 0;
            spriteNum = 1; // balik ke frame idle
        }


        if (!moving && !tilling && !watering && !fishing && !recoverLand) {
            if (keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed) {
                if (keyH.upPressed) {
                    direction = "up";
                } else if (keyH.downPressed) {
                    direction = "down";
                } else if (keyH.leftPressed) {
                    direction = "left";
                } else if (keyH.rightPressed) {
                    direction = "right";
                }
                moving = true;
                
                //Check Tile Collision
                collisionOn = false;
                gp.collisionChecker.checkTile(this);
                gp.collisionChecker.checkNPC(this, gp.npcManager.npcMapList[gp.currentMap]);


            } else if (itemHeld != null && keyH.spacePressed && itemHeld!= null && !watering && !fishing && !tilling && !gp.dialogueOn) {
                int npcIndex = gp.collisionChecker.checkNPC(this, gp.npcManager.npcMapList[gp.currentMap]);
                if (npcIndex != -1) {
                    NPC npc = gp.npcManager.npcMapList[gp.currentMap].get(npcIndex);
                    interactWithNPC(npc);
                }
                keyH.spacePressed = false;
            } else if (itemHeld != null && keyH.enterPressed && canTill && itemHeld.getItemName().equals("Hoe") && !watering && !fishing && !recoverLand) {
                String tileName = gp.tileM.getFrontTile();
                if (gp.currentMap != 0) {
                    forbiddenMessage = "Anda tidak bisa menggunakan "+itemHeld.getItemName()+" di sini!";
                    gp.ui.showMessage(forbiddenMessage);
                } else if (tileName != null && Arrays.asList("210.png").contains(tileName) && targetTile.getSoilState().equals(SoilState.LAND)) {
                    targetTile.setSoilState(SoilState.TILLED);
                    energy -= 5;
                    gp.farm.getTime().skipTime(5, null);
                    tilling = true;
                    canTill = false;
                    keyH.enterPressed = false;
                } else {
                    gp.ui.showMessage("Tidak dapat menggemburkan tanah!");
                }
            } else if (itemHeld != null && keyH.enterPressed && canWater && itemHeld.getItemName().equals("Watering Can") && !tilling && !fishing && !recoverLand) {
                String tileName = gp.tileM.getFrontTile();
                if (gp.currentMap != 0) {
                    forbiddenMessage = "Anda tidak bisa menggunakan "+itemHeld.getItemName()+" di sini!";
                    gp.ui.showMessage(forbiddenMessage);
                // } else if (tileName != null && Arrays.asList("291.png").contains(tileName)) {
                } else if (tileName != null && targetTile.getSoilState().equals(SoilState.TILLED)) {
                    targetTile.setSoilState(SoilState.WATERED);
                    energy -= 5;
                    gp.farm.getTime().skipTime(5, null);
                    watering = true;
                    canWater = false;
                    keyH.enterPressed = false;
                } else {
                    gp.ui.showMessage("Tidak dapat menyiram!");
                }
            } else if (itemHeld != null && keyH.enterPressed && canFish && itemHeld.getItemName().equals("Fishing Rod") && !tilling && !watering && !recoverLand) {
                String tileName = gp.tileM.getFrontTile();
                if (gp.currentMap != 0 && gp.currentMap != 7 && gp.currentMap != 8 && gp.currentMap != 9) {
                    forbiddenMessage = "Anda tidak bisa menggunakan "+itemHeld.getItemName()+" di sini!";
                    gp.ui.showMessage(forbiddenMessage);
                } else if (tileName != null && Arrays.asList("136.png","154.png","189.png","192.png","197.png","198.png","279.png","281.png","282.png","283.png","285.png","286.png","287.png","289.png","290.png","448.png","450.png","451.png","452.png","453.png","454.png","455.png").contains(tileName)) {
                    gp.fishingManager.startFishing();
                    fishing = true;
                    fishingPhase = 1; // Mulai animasi dari rod_1 dulu
                    canFish = false;
                    keyH.enterPressed = false;
                } else {
                    gp.ui.showMessage("Anda tidak bisa memancing di sini!");
                }
            } else if (itemHeld != null && keyH.enterPressed && canTill && itemHeld.getItemName().equals("Pickaxe") && !tilling && !watering && !fishing) {
                String tileName = gp.tileM.getFrontTile();
                if (gp.currentMap != 0) {
                    forbiddenMessage = "Anda tidak bisa menggunakan "+itemHeld.getItemName()+" di sini!";
                    gp.ui.showMessage(forbiddenMessage);
                // } else if (tileName != null && Arrays.asList("291.png").contains(tileName)) {
                } else if (tileName != null && targetTile.getSoilState().equals(SoilState.TILLED)) {
                    targetTile.reset();
                    energy -= 5;
                    gp.farm.getTime().skipTime(5, null);
                    tilling = true;
                    canTill = false;
                    keyH.enterPressed = false;
                } else {
                    gp.ui.showMessage("Tidak dapat recover land!");
                }
            } else if (itemHeld != null && keyH.enterPressed && (targetTile.getSoilState() == SoilState.TILLED||targetTile.getSoilState().equals(SoilState.WATERED)) && itemHeld instanceof Seeds && !tilling && !fishing && !watering && !recoverLand) {
                String tileName = gp.tileM.getFrontTile();
                if (gp.currentMap != 0) {
                    forbiddenMessage = "Anda tidak bisa menggunakan "+itemHeld.getItemName()+" di sini!";
                    gp.ui.showMessage(forbiddenMessage);
                // } else if (tileName != null && Arrays.asList("291.png").contains(tileName)) {
                } else if (tileName != null && (targetTile.getSoilState().equals(SoilState.TILLED)|| targetTile.getSoilState().equals(SoilState.WATERED))) {
                    if (targetTile.getSeed() == null) {
                        Seeds seed = (Seeds) itemHeld;
                        if (seed.getSeasonGrow() == gp.farm.getSeason()) {
                            targetTile.plantSeed(seed);
                            inventory.reduceItem(seed, 1);
                            energy -= 5;
                            gp.farm.getTime().skipTime(5, null);
                            keyH.enterPressed = false;
                        } else {
                            gp.ui.showMessage("Benih ini tidak bisa tumbuh di musim ini!");
                        }
                    } else {
                        gp.ui.showMessage("Sudah ada benih ditanam di sini!");
                    }
                } else {
                    gp.ui.showMessage("Tidak dapat menanam benih!");
                }
            } else if (itemHeld == null && targetTile.getPlantState() == PlantState.HARVEST) {
                if (targetTile.getSeed() != null) {
                    Crops crop = targetTile.getSeed().getResultItem(gp.itemManager);
                    inventory.addItem(new InventoryItem(crop, crop.getHarvestAmount()));
                    targetTile.reset();
                    energy -= 5;
                    gp.farm.getTime().skipTime(5, null);
                    keyH.enterPressed = false;
                }
            }   else {
                standCounter++;
                if (standCounter == 20) {
                    spriteNum = 1;
                    standCounter = 0;
                }
            }
        }
        if (moving && !tilling && !watering && !fishing && !recoverLand) {
            //If collision false, player can move
            if (collisionOn == false) {
                switch(direction) {
                    case "up":
                        worldY -= speed;
                    break;
                    case "down":
                        worldY += speed;
                    break;
                    case "left":
                        worldX -= speed;
                    break;
                    case "right":
                        worldX += speed;
                    break;
                }
            }
            
            
            spriteCounter++;
            if (spriteCounter > 12) {
                spriteNum = (spriteNum == 1) ? 2 : 1;
                spriteCounter = 0;
            }
            pixelCounter += speed;
            if (pixelCounter == 48) {
                moving = false;
                pixelCounter = 0;

                checkMapTransition();
            }
        }
    }

    // Untuk Fungsi Fishing, mengecek lokasi
    public String locationName() {
            switch(gp.currentMap) {
                case 0:
                return "Pond";
                case 7:
                return "Ocean";
                case 8:
                return "Forest River";
                case 9:
                return "Mountain Lake";
                case 1:
                return "Mayor Tadi's House";
                case 2:
                return "Caroline's House";
                case 3:
                return "Perry's House";
                case 4:
                return "Dasco's House";
                case 5:
                return "Abigail's House";
                case 6:
                return "Store";
                case 10:
                return "World Map";
                case 11:
                return "House";
                case 12:
                return "Kost";
                default:
                return null;
            }
        }

    // Untuk berpindah dari map ke map
    public void checkMapTransition() {
        int col = worldX / gp.tileSize;
        int row = worldY / gp.tileSize;

        if (gp.currentMap == 0) { // Jika di Farm Maps
            if (row == 0 || col == 0 || row == gp.maxWorldRow-1 || col == gp.maxWorldCol-1) {
                gp.currentMap = 10;
                setDefaultValues(7, 4, "up");
                worldX = 8*gp.tileSize;
                worldY = 4*gp.tileSize;
            } 
            if (row == 14 && col == 14) {
                gp.currentMap = 11;
                gp.maxWorldCol = gp.tileM.mapCols[gp.currentMap];
                gp.maxWorldRow = gp.tileM.mapRows[gp.currentMap];
                setDefaultValues(11, 22, "up");
                worldX = 11*gp.tileSize;
                worldY = 22*gp.tileSize;
            }
        } else if (gp.currentMap == 10) {
            gp.maxWorldCol = gp.tileM.mapCols[gp.currentMap];
            gp.maxWorldRow = gp.tileM.mapRows[gp.currentMap];
            
            if (row == 5 && (col == 7 || col == 8 || col == 9)) {
                gp.currentMap = 0;
                gp.maxWorldCol = gp.tileM.mapCols[gp.currentMap];
                gp.maxWorldRow = gp.tileM.mapRows[gp.currentMap];
                setDefaultValues(30, 1, "down");
                worldX = 30*gp.tileSize;
                worldY = 1*gp.tileSize;
            }
            if (row==2 && col ==7) {
                gp.currentMap = 6;
                gp.maxWorldCol = gp.tileM.mapCols[gp.currentMap];
                gp.maxWorldRow = gp.tileM.mapRows[gp.currentMap];
                setDefaultValues(16, 10, "up");
                worldX = 16*gp.tileSize;
                worldY = 10*gp.tileSize;
            }
            if (row==2 && col == 5) {
                gp.currentMap = 1;
                gp.maxWorldCol = gp.tileM.mapCols[gp.currentMap];
                gp.maxWorldRow = gp.tileM.mapRows[gp.currentMap];
                setDefaultValues(7, 7, "up");
                worldX = 7*gp.tileSize;
                worldY = 7*gp.tileSize;
            }
            if (row==6 && col == 5) {
                gp.currentMap = 2;
                gp.maxWorldCol = gp.tileM.mapCols[gp.currentMap];
                gp.maxWorldRow = gp.tileM.mapRows[gp.currentMap];
                setDefaultValues(7, 7, "up");
                worldX = 7*gp.tileSize;
                worldY = 7*gp.tileSize;
            }
            if (row==6 && col == 11) {
                gp.currentMap = 3;
                gp.maxWorldCol = gp.tileM.mapCols[gp.currentMap];
                gp.maxWorldRow = gp.tileM.mapRows[gp.currentMap];
                setDefaultValues(7, 7, "up");
                worldX = 7*gp.tileSize;
                worldY = 7*gp.tileSize;
            }
            if (row==2 && col == 11) {
                gp.currentMap = 4;
                gp.maxWorldCol = gp.tileM.mapCols[gp.currentMap];
                gp.maxWorldRow = gp.tileM.mapRows[gp.currentMap];
                setDefaultValues(7, 7, "up");
                worldX = 7*gp.tileSize;
                worldY = 7*gp.tileSize;
            }
            if (row==2 && col == 9) {
                gp.currentMap = 5;
                gp.maxWorldCol = gp.tileM.mapCols[gp.currentMap];
                gp.maxWorldRow = gp.tileM.mapRows[gp.currentMap];
                setDefaultValues(7, 7, "up");
                worldX = 7*gp.tileSize;
                worldY = 7*gp.tileSize;
            }
            if (row==2 && col == 13) {
                gp.currentMap = 12;
                gp.maxWorldCol = gp.tileM.mapCols[gp.currentMap];
                gp.maxWorldRow = gp.tileM.mapRows[gp.currentMap];
                setDefaultValues(11, 22, "up");
                worldX = 11*gp.tileSize;
                worldY = 22*gp.tileSize;
            }
            if (row == 2 && col == 17) { // Mountain Lake
                gp.currentMap = 9;
                gp.maxWorldCol = gp.tileM.mapCols[gp.currentMap];
                gp.maxWorldRow = gp.tileM.mapRows[gp.currentMap];
                setDefaultValues(9, 1, "down");
                worldX = 9*gp.tileSize;
                worldY = 1*gp.tileSize;
            }
            if (row == 6 && col == 16) { // Ocean
                gp.currentMap = 7;
                gp.maxWorldCol = gp.tileM.mapCols[gp.currentMap];
                gp.maxWorldRow = gp.tileM.mapRows[gp.currentMap];
                setDefaultValues(2, 2, "down");
                worldX = 2*gp.tileSize;
                worldY = 2*gp.tileSize;
            }
            if (row == 4 && col == 2) { // Forest River
                gp.currentMap = 8;
                gp.maxWorldCol = gp.tileM.mapCols[gp.currentMap];
                gp.maxWorldRow = gp.tileM.mapRows[gp.currentMap];
                setDefaultValues(7, 1, "down");
                worldX = 7*gp.tileSize;
                worldY = 1*gp.tileSize;
            }
        } else if (gp.currentMap == 6) {
            gp.maxWorldCol = gp.tileM.mapCols[gp.currentMap];
            gp.maxWorldRow = gp.tileM.mapRows[gp.currentMap];
            if (row==11 && col == 16) {
                gp.currentMap = 10;
                gp.maxWorldCol = gp.tileM.mapCols[gp.currentMap];
                gp.maxWorldRow = gp.tileM.mapRows[gp.currentMap];
                setDefaultValues(7, 3, "down");
                worldX = 7*gp.tileSize;
                worldY = 3*gp.tileSize;
            }
        } else if (gp.currentMap == 1) {
            gp.maxWorldCol = gp.tileM.mapCols[gp.currentMap];
            gp.maxWorldRow = gp.tileM.mapRows[gp.currentMap];
            if (row==8 && col == 7) {
                gp.currentMap = 10;
                gp.maxWorldCol = gp.tileM.mapCols[gp.currentMap];
                gp.maxWorldRow = gp.tileM.mapRows[gp.currentMap];
                setDefaultValues(5, 3, "down");
                worldX = 5*gp.tileSize;
                worldY = 3*gp.tileSize;
            }
        } else if (gp.currentMap == 2) {
            gp.maxWorldCol = gp.tileM.mapCols[gp.currentMap];
            gp.maxWorldRow = gp.tileM.mapRows[gp.currentMap];
            if (row==8 && col == 7) {
                gp.currentMap = 10;
                gp.maxWorldCol = gp.tileM.mapCols[gp.currentMap];
                gp.maxWorldRow = gp.tileM.mapRows[gp.currentMap];
                setDefaultValues(5, 5, "up");
                worldX = 5*gp.tileSize;
                worldY = 5*gp.tileSize;
            }
        } else if (gp.currentMap == 3) {
            gp.maxWorldCol = gp.tileM.mapCols[gp.currentMap];
            gp.maxWorldRow = gp.tileM.mapRows[gp.currentMap];
            if (row==8 && col == 7) {
                gp.currentMap = 10;
                gp.maxWorldCol = gp.tileM.mapCols[gp.currentMap];
                gp.maxWorldRow = gp.tileM.mapRows[gp.currentMap];
                setDefaultValues(11, 5, "up");
                worldX = 11*gp.tileSize;
                worldY = 5*gp.tileSize;
            }
        } else if (gp.currentMap == 4) {
            gp.maxWorldCol = gp.tileM.mapCols[gp.currentMap];
            gp.maxWorldRow = gp.tileM.mapRows[gp.currentMap];
            if (row==8 && col == 7) {
                gp.currentMap = 10;
                gp.maxWorldCol = gp.tileM.mapCols[gp.currentMap];
                gp.maxWorldRow = gp.tileM.mapRows[gp.currentMap];
                setDefaultValues(11, 3, "down");
                worldX = 11*gp.tileSize;
                worldY = 3*gp.tileSize;
            }
        } else if (gp.currentMap == 5) {
            gp.maxWorldCol = gp.tileM.mapCols[gp.currentMap];
            gp.maxWorldRow = gp.tileM.mapRows[gp.currentMap];
            if (row==8 && col == 7) {
                gp.currentMap = 10;
                gp.maxWorldCol = gp.tileM.mapCols[gp.currentMap];
                gp.maxWorldRow = gp.tileM.mapRows[gp.currentMap];
                setDefaultValues(9, 3, "down");
                worldX = 9*gp.tileSize;
                worldY = 3*gp.tileSize;
            }
        } else if (gp.currentMap == 12) {
            gp.maxWorldCol = gp.tileM.mapCols[gp.currentMap];
            gp.maxWorldRow = gp.tileM.mapRows[gp.currentMap];
            if (row==23 && col == 11) {
                gp.currentMap = 10;
                gp.maxWorldCol = gp.tileM.mapCols[gp.currentMap];
                gp.maxWorldRow = gp.tileM.mapRows[gp.currentMap];
                setDefaultValues(13, 3, "down");
                worldX = 13*gp.tileSize;
                worldY = 3*gp.tileSize;
            }
        } else if (gp.currentMap == 11) {
            gp.maxWorldCol = gp.tileM.mapCols[gp.currentMap];
            gp.maxWorldRow = gp.tileM.mapRows[gp.currentMap];
            if (row==23 && col == 11) {
                gp.currentMap = 0;
                gp.maxWorldCol = gp.tileM.mapCols[gp.currentMap];
                gp.maxWorldRow = gp.tileM.mapRows[gp.currentMap];
                setDefaultValues(14, 15, "down");
                worldX = 14*gp.tileSize;
                worldY = 15*gp.tileSize;
            }
        } else if (gp.currentMap == 8) {
            if (row == 0 || col == 0 || row == gp.maxWorldRow-1 || col == gp.maxWorldCol-1) {
                gp.currentMap = 10;
                gp.maxWorldCol = gp.tileM.mapCols[gp.currentMap];
                gp.maxWorldRow = gp.tileM.mapRows[gp.currentMap];
                setDefaultValues(3, 4, "right");
                worldX = 3*gp.tileSize;
                worldY = 4*gp.tileSize;
            }
        } else if (gp.currentMap == 7) {
            if (row == 0 || col == 0 || row == gp.maxWorldRow-1 || col == gp.maxWorldCol-1) {
                gp.currentMap = 10;
                gp.maxWorldCol = gp.tileM.mapCols[gp.currentMap];
                gp.maxWorldRow = gp.tileM.mapRows[gp.currentMap];
                setDefaultValues(16, 5, "up");
                worldX = 16*gp.tileSize;
                worldY = 5*gp.tileSize;
            }
        } else if (gp.currentMap == 9) {
            if (row == 0 || col == 0 || row == gp.maxWorldRow-1 || col == gp.maxWorldCol-1) {
                gp.currentMap = 10;
                gp.maxWorldCol = gp.tileM.mapCols[gp.currentMap];
                gp.maxWorldRow = gp.tileM.mapRows[gp.currentMap];
                setDefaultValues(17, 3, "down");
                worldX = 17*gp.tileSize;
                worldY = 3*gp.tileSize;
            }
        }
    }

    public void draw(Graphics2D g2) {
        BufferedImage image = null;
        int tempScreenX = screenX;
        int tempScreenY = screenY;
        int guideboxX = screenX;
        int guideboxY = screenY;

        switch(direction) {
        case "up":
            guideboxY -= gp.tileSize;
            if (!tilling && !watering) {
                if (spriteNum == 1) {image = up1;}
                if (spriteNum == 2) {image = up2;}
            }
            if (tilling) {
                tempScreenY = screenY - gp.tileSize;
                if (spriteNum == 1) {image = hoeUp1;}
                if (spriteNum == 2) {image = hoeUp2;}
            }
            if (watering) {
                tempScreenY = screenY - gp.tileSize;
                if (spriteNum == 1) {image = waterUp1;}
                if (spriteNum == 2) {image = waterUp2;}
            }
            if (fishing) {
                tempScreenY = screenY - gp.tileSize;
                if (spriteNum == 1) {image = rodUp1;}
                else if (spriteNum == 2) {image = rodUp2;}
            }
            break;
            case "down":
            guideboxY += gp.tileSize;
            if (!tilling && !watering) {
                if (spriteNum == 1) {image = down1;}
                if (spriteNum == 2) {image = down2;}
            }
            if (tilling) {
                tempScreenY = screenY - gp.tileSize;
                if (spriteNum == 1) {image = hoeDown1;}
                if (spriteNum == 2) {image = hoeDown2;}
            }
            if (watering) {
                // tempScreenY = screenY - gp.tileSize;
                if (spriteNum == 1) {image = waterDown1;}
                if (spriteNum == 2) {image = waterDown2;}
            }
            if (fishing) {
                tempScreenY = screenY - gp.tileSize;
                if (spriteNum == 1) {image = rodDown1;}
                else if (spriteNum == 2) {image = rodDown2;}
            }
            break;
            case "left":
            guideboxX -= gp.tileSize;
            if (!tilling && !watering) {
                if (spriteNum == 1) {image = left1;}
                if (spriteNum == 2) {image = left2;}
            }
            if (tilling) {
                tempScreenY = screenY - gp.tileSize;
                if (spriteNum == 1) {image = hoeLeft1;}
                if (spriteNum == 2) {
                    tempScreenX = screenX - gp.tileSize;
                    image = hoeLeft2;
                }
            }
            if (watering) {
                // tempScreenY = screenY - gp.tileSize;
                tempScreenX = screenX - gp.tileSize;
                if (spriteNum == 1) {image = waterLeft1;}
                if (spriteNum == 2) {image = waterLeft2;}
            }
            if (fishing) {
                tempScreenY = screenY - gp.tileSize;
                if (spriteNum == 1) {image = rodLeft1;}
                else if (spriteNum == 2) {
                    tempScreenX = screenX - gp.tileSize;
                    image = rodLeft2;
                }
            }
            break;
            case "right":
            guideboxX += gp.tileSize;
            if (!tilling && !watering) {
                if (spriteNum == 1) {image = right1;}
                if (spriteNum == 2) {image = right2;}
            }
            if (tilling) {
                tempScreenY = screenY - gp.tileSize;
                if (spriteNum == 1) {image = hoeRight1;}
                if (spriteNum == 2) {
                    image = hoeRight2;
                }
            }
            if (watering) {
                // tempScreenY = screenY - gp.tileSize;
                if (spriteNum == 1) {image = waterRight1;}
                if (spriteNum == 2) {image = waterRight2;}
            }
            if (fishing) {
                tempScreenY = screenY - gp.tileSize;
                if (spriteNum == 1) {image = rodRight1;}
                else if (spriteNum == 2) {
                    image = rodRight2;
                }
            }
            break;
        }

        if ((gp.currentMap == 0 || gp.currentMap == 7 || gp.currentMap == 8 || gp.currentMap == 9) && itemHeld!=null && (itemHeld instanceof Equipment || itemHeld instanceof Seeds)) {
            g2.drawImage(guidebox, guideboxX, guideboxY, gp.tileSize, gp.tileSize, null);
        }
        g2.drawImage(image, tempScreenX, tempScreenY, null);

        // Untuk cek Hitbox Collision Karakter
        // g2.setColor((Color.red));
        // g2.drawRect(screenX+solidArea.x, screenY+solidArea.y, solidArea.width, solidArea.height);
    }

    public void tilling() {
        int tillingSpeed = 15;
        spriteCounter++;
        if (spriteCounter <= tillingSpeed) {
            spriteNum = 1;
        } 
        if (spriteCounter > tillingSpeed && spriteCounter <= tillingSpeed*2) {
            spriteNum = 2;
        }
        if (spriteCounter > tillingSpeed*2) {
            spriteNum = 1;
            spriteCounter = 0;
            tilling = false;
            canTill = false;
            tillCooldownCounter = 0;
        }
    }

    public void watering() {
        int wateringSpeed = 15;
        spriteCounter++;
        if (spriteCounter <= wateringSpeed) {
            spriteNum = 1;
        } 
        if (spriteCounter > wateringSpeed && spriteCounter <= wateringSpeed*2) {
            spriteNum = 2;
        }
        if (spriteCounter > wateringSpeed*2) {
            spriteNum = 1;
            spriteCounter = 0;
            watering = false;
            canWater = false;
            waterCooldownCounter = 0;
        }
    }

    public void fishing() {
        // Phase 1: Tampilkan rod_1 untuk beberapa frame
        if (fishingPhase == 1) {
            fishingFrameCounter++;
            spriteNum = 1;

            if (fishingFrameCounter > 15) { // Durasi 15 frame (~0.25 detik @60FPS)
                fishingPhase = 2; // Ganti ke pose statis rod_2
                fishingFrameCounter = 0;
            }
        }

        // Phase 2: Tetap pada sprite rod_2
        else if (fishingPhase == 2) {
            spriteNum = 2;

            // Tekan Enter lagi untuk membatalkan fishing
            if (keyH.enterPressed) {
                fishing = false;
                canFish = false;
                fishCooldownCounter = 0;
                fishingPhase = 0;
                spriteNum = 1;
                keyH.enterPressed = false;
            }
        }
    }

    public void interactWithNPC(NPC npc) {
        if (npc.getName().equals("Emily")) {
            gp.ui.setEmilyInteractionMode(npc);
        } else {
            String dialog = getNPCDialog(npc, null);
            gp.ui.currentDialogue = npc.getName()+": "+dialog;
            gp.dialogueOn = true;
            if (!npc.getHasTalked()) {
                npc.setHasTalked(true);
                npc.setHeartPoints(npc.getHeartPoints()+10);
                npc.setFreqChat(npc.getFreqChat()+1);
            }
        }
    }

    public String getNPCDialog(NPC npc, Items itemGiven) {
        List<String> dialogues = gp.ui.npcDialogues.get(npc.getName());
        if (dialogues == null) return "[NPC tidak memiliki dialog!]";
        if (itemGiven == null) {
            if (npc.getFreqChat() == 0) {
                return dialogues.get(0); // perkenalan
            } else {
                return dialogues.get(8+ (int)(Math.random() * 3)); // random dari 8-10
            }
        } else {
            if (npc.getLovedItems().contains(itemGiven)) return dialogues.get(1);
            else if (npc.getLikedItems().contains(itemGiven)) return dialogues.get(2);
            else if (npc.getHatedItems().contains(itemGiven)) return dialogues.get(3);
            else return dialogues.get(4);
        }
    }

    //Name
    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    //Gender
    public Gender getGender(){
        return gender;
    }

    public void setGender(Gender gender){
        this.gender = gender;
    }

    //Energy
    public int getEnergy(){
        return energy;
    }

    public void setEnergy(int energy){
        this.energy = energy;
    }

    //Gold
    public int getGold(){
        return gold;
    }

    public void setGold(int gold){
        this.gold = gold;
    }

    //Item Held
    public Items getItemHeld(){
        return itemHeld;
    }

    public void setItemHeld(Items item){
        itemHeld = item;
    }

    //Total Income
    public int getTotalIncome(){
        return total_income;
    }

    public void addTotalIncome(int income){
        total_income += income;
    }

    //Total Expenditure
    public int getTotalExpenditure(){
        return total_expenditure;
    }

    public void addTotalExpenditure(int expenditure){
        total_expenditure += expenditure;
    }

    //Avarage Season Income
    public float getAvgSeasonIncome(){
        return avarage_season_income;
    }

    public void setAvgSeasonIncome(int income){
        avarage_season_income = income;
    }

    //Avarage Season Expenditure
    public float getAvgSeasonExpenditure(){
        return avarage_season_expenditure;
    }

    public void setAvgSeasonExpenditure(int income){
        avarage_season_expenditure = income;
    }

    //Total Days Played
    public int getTotalDaysPlayed(){
        return total_days_played;
    }

    public void addTotalDaysPlayed(){
        total_days_played++;
    }

    //Crops Harvested
    public int getCropsHarvested(){
        return crops_harvested;
    }

    public void addCropsHarvested(int crops){
        crops_harvested += crops;
    }

    //Fish Caught
    public int getFishCaught(){
        return fish_caught;
    }

    public void addFishCaught(int fish){
        fish_caught += fish;
        checkAndUnlockRecipes(); 
    }

    //Coordinate
    public Coordinate getCoordinate(){
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate){
        this.coordinate.setColumn(coordinate.getColumn());
        this.coordinate.setRow(coordinate.getRow());
    }

    //Inventory
    public Inventory getInventory(){
        return inventory;
    }
    
    //NPC
    public List<NPC> getRelationshipStatus(){
        return npcRelationshipStats;
    }

    //Action
    public void tile(){
        if(this.getItemHeld().getItemName() == "Hoe"){
            setEnergy(getEnergy() - 5);
        }
    }
    
    public void recoverLand(){
        if(this.getItemHeld().getItemName() == "Pickaxe"){
            setEnergy(getEnergy() - 5);
        }
    }
    
    public void plant(Seeds seed){
        setEnergy(getEnergy() - 5);
    }

    public void water(){
        if(this.getItemHeld().getItemName() == "Watering Can"){
            setEnergy(getEnergy() - 5);
        }
    }

    public void harvest(){
        setEnergy(getEnergy() - 5);
    }

    public void eat(Food food){
        setEnergy(getEnergy() + food.getEnergyRestore());
    }

    public void sleep(){
        if(getEnergy() < 10){
            setEnergy(50);
        }
        else{
            setEnergy(100);
        }
    }

    public void setHasHarvestedForFirstTime(boolean val) {
        this.hasHarvestedForFirstTime = val;
        if (val) {
            checkAndUnlockRecipes();
        }
    }

    public void onItemAddedToInventory(Items item) {
        if (item.getItemName().equals("Hot Pepper") && !hotPepperObtained) {
            this.hotPepperObtained = true;
            checkAndUnlockRecipes();
        }
        
        if (item.getItemName().equals("Fish n' Chips")) {
            Food fishNChipsRecipe = (Food) gp.itemManager.getItem("Fish n' Chips");
            if (fishNChipsRecipe != null && !fishNChipsRecipe.isRecipeAcquired()) {
                fishNChipsRecipe.setRecipeAcquired(true);
                gp.ui.showMessage("Resep Baru Terbuka: Fish n' Chips!");
            }
        }
        if (item.getItemName().equals("Fish Sandwich")) {
            Food fishSandwichRecipe = (Food) gp.itemManager.getItem("Fish Sandwich");
            if (fishSandwichRecipe != null && !fishSandwichRecipe.isRecipeAcquired()) {
                fishSandwichRecipe.setRecipeAcquired(true);
                gp.ui.showMessage("Resep Baru Terbuka: Fish Sandwich!");
            }
        }
        // Untuk resep Fugu, periksa setelah item ditambahkan
        checkAndUnlockRecipes();
    }

    public void checkAndUnlockRecipes() {
        // Sashimi: Setelah memancing 10 ikan
        Food sashimiRecipe = (Food) gp.itemManager.getItem("Sashimi");
        if (sashimiRecipe != null && !sashimiRecipe.isRecipeAcquired() && getFishCaught() >= 10) {
            sashimiRecipe.setRecipeAcquired(true);
            gp.ui.showMessage("Resep Baru Terbuka: Sashimi!");
        }

        // Fugu: Memancing pufferfish (artinya Pufferfish ada di inventory)
        Food fuguRecipe = (Food) gp.itemManager.getItem("Fugu");
        if (fuguRecipe != null && !fuguRecipe.isRecipeAcquired() && inventory.hasItemByName("Pufferfish")) {
            fuguRecipe.setRecipeAcquired(true);
            gp.ui.showMessage("Resep Baru Terbuka: Fugu!");
        }

        // Veggie Soup: Memanen untuk pertama kalinya
        Food veggieSoupRecipe = (Food) gp.itemManager.getItem("Veggie Soup");
        if (veggieSoupRecipe != null && !veggieSoupRecipe.isRecipeAcquired() && hasHarvestedForFirstTime) {
            veggieSoupRecipe.setRecipeAcquired(true);
            gp.ui.showMessage("Resep Baru Terbuka: Veggie Soup!");
        }

        // Fish Stew: Dapatkan "Hot Pepper" terlebih dahulu
        Food fishStewRecipe = (Food) gp.itemManager.getItem("Fish Stew");
        if (fishStewRecipe != null && !fishStewRecipe.isRecipeAcquired() && hotPepperObtained) {
            fishStewRecipe.setRecipeAcquired(true);
            gp.ui.showMessage("Resep Baru Terbuka: Fish Stew!");
        }

        // The Legends of Spakbor: Memancing "Legend"
        Food legendsRecipe = (Food) gp.itemManager.getItem("The Legends of Spakbor");
        if (legendsRecipe != null && !legendsRecipe.isRecipeAcquired() && inventory.hasItemByName("Legend")) {
            legendsRecipe.setRecipeAcquired(true);
            gp.ui.showMessage("Resep Baru Terbuka: The Legends of Spakbor!");
        }
    }

    public Food getRecipeBeingCooked() { return recipeBeingCooked; }
    public int getCookingCompletionDay() { return cookingCompletionDay; }
    public int getCookingCompletionTimeAsInt() { return cookingCompletionHour * 100 + cookingCompletionMinute; }

    public void finishCooking() {
        if (!isCooking || recipeBeingCooked == null) {
            return;
        }

        // 1. Konsumsi Bahan
        Map<String, Integer> requiredIngredientsMap = new HashMap<>();
        for (Items ing : recipeBeingCooked.getIngredients()) {
            requiredIngredientsMap.put(ing.getItemName(), requiredIngredientsMap.getOrDefault(ing.getItemName(), 0) + 1);
        }

        for (Map.Entry<String, Integer> entry : requiredIngredientsMap.entrySet()) {
            InventoryItem playerIng = inventory.findItemByName(entry.getKey());
            // Seharusnya sudah dicek sebelum memulai, tapi cek lagi untuk keamanan
            if (playerIng == null || playerIng.getQuantity() < entry.getValue()) {
                System.err.println("Error: Kekurangan bahan " + entry.getKey() + " saat menyelesaikan masakan!");
                gp.ui.showMessage("Gagal menyelesaikan masakan: " + recipeBeingCooked.getItemName() + ", bahan kurang.");
                isCooking = false;
                recipeBeingCooked = null;
                // Pertimbangkan untuk mengembalikan energi jika gagal di tahap ini
                return;
            }
            playerIng.setQuantity(playerIng.getQuantity() - entry.getValue());
            if (playerIng.getQuantity() == 0) {
                inventory.removeItem(playerIng);
            }
        }

        // 2. Konsumsi Bahan Bakar
        boolean fuelConsumedThisAction = false;
        if (coalPartiallyUsed) {
            coalPartiallyUsed = false; // Penggunaan kedua Coal, tidak mengurangi item lagi
            fuelConsumedThisAction = true;
            gp.ui.showMessage("Menyelesaikan penggunaan Coal.");
        } else {
            InventoryItem coal = inventory.findItemByName("Coal");
            if (coal != null && coal.getQuantity() > 0) {
                coal.setQuantity(coal.getQuantity() - 1);
                if (coal.getQuantity() == 0) {
                    inventory.removeItem(coal);
                }
                coalPartiallyUsed = true; // Tandai Coal sudah terpakai sekali
                fuelConsumedThisAction = true;
                gp.ui.showMessage("Menggunakan 1 Coal (sisa 1x masak).");
            } else {
                InventoryItem firewood = inventory.findItemByName("Firewood");
                if (firewood != null && firewood.getQuantity() > 0) {
                    firewood.setQuantity(firewood.getQuantity() - 1);
                    if (firewood.getQuantity() == 0) {
                        inventory.removeItem(firewood);
                    }
                    fuelConsumedThisAction = true;
                    gp.ui.showMessage("Menggunakan 1 Firewood.");
                }
            }
        }

        if (!fuelConsumedThisAction) {
            System.err.println("Error: Tidak ada bahan bakar untuk menyelesaikan masakan " + recipeBeingCooked.getItemName());
            gp.ui.showMessage("Gagal menyelesaikan masakan: " + recipeBeingCooked.getItemName() + ", bahan bakar habis.");
            isCooking = false;
            recipeBeingCooked = null;
            // Kembalikan energi yang terpakai untuk memulai
            setEnergy(getEnergy() + 10);
            return;
        }

        // 3. Tambahkan makanan yang sudah jadi ke inventory
        inventory.addItem(new InventoryItem(recipeBeingCooked, 1));
        gp.ui.showMessage(recipeBeingCooked.getItemName() + " sudah matang!");

        isCooking = false;
        recipeBeingCooked = null;
    }

    public void startCooking(Food recipe) {
        if (isCooking) {
            gp.ui.showMessage("Kamu sudah memasak sesuatu yang lain.");
            return;
        }

        if (this.getEnergy() < 10) {
            gp.ui.showMessage("Energi tidak cukup untuk mulai memasak (-10 diperlukan).");
            return;
        }

        Map<String, Integer> requiredIngredientsMap = new HashMap<>();
        if (recipe.getIngredients() == null) {
            gp.ui.showMessage("Resep ini tidak memiliki daftar bahan!");
            return;
        }
        for (Items ing : recipe.getIngredients()) {
            requiredIngredientsMap.put(ing.getItemName(), requiredIngredientsMap.getOrDefault(ing.getItemName(), 0) + 1);
        }

        for (Map.Entry<String, Integer> entry : requiredIngredientsMap.entrySet()) {
            InventoryItem playerIng = inventory.findItemByName(entry.getKey());
            if (playerIng == null || playerIng.getQuantity() < entry.getValue()) {
                gp.ui.showMessage("Kekurangan " + entry.getKey() + " (Butuh: " + entry.getValue() + ", Punya: " + (playerIng == null ? 0 : playerIng.getQuantity()) + ")");
                return;
            }
        }
        boolean hasFuel = false;
        if (coalPartiallyUsed) {
            hasFuel = true;
        } else {
            InventoryItem coal = inventory.findItemByName("Coal");
            if (coal != null && coal.getQuantity() > 0) {
                hasFuel = true;
            } else {
                InventoryItem firewood = inventory.findItemByName("Firewood");
                if (firewood != null && firewood.getQuantity() > 0) {
                    hasFuel = true;
                }
            }
        }

        if (!hasFuel) {
            gp.ui.showMessage("Tidak ada bahan bakar (Firewood atau Coal) untuk memasak.");
            return;
        }

        setEnergy(getEnergy() - 10);
        this.isCooking = true;
        this.recipeBeingCooked = recipe;

        this.cookingCompletionDay = gp.farm.getDay();
        int currentHour = gp.farm.getTime().getHour();
        int currentMinute = gp.farm.getTime().getMinute();

        this.cookingCompletionMinute = currentMinute;
        this.cookingCompletionHour = currentHour + 1;

        if (this.cookingCompletionHour >= 24) {
            this.cookingCompletionHour -= 24;
            this.cookingCompletionDay += 1;
        }
        gp.farm.checkPassiveActions(this); 

        gp.ui.showMessage("Mulai memasak " + recipe.getItemName() + ". Akan siap dalam 1 jam.");
        gp.cookingMenuActive = false;
        gp.ui.showRecipeDetails = false; 
    }

    public void cook(Food food) {
        if (!food.isRecipeAcquired()) {
            System.out.println("Resep belum diperoleh.");
            return;
        }

        List<Items> ingredients = food.getIngredients();
        List<InventoryItem> itemsToConsume = new ArrayList<>();
        boolean hasAllIngredients = true;

        // Cek apakah semua bahan tersedia
        for (Items ingredient : ingredients) {
            boolean found = false;
            for (InventoryItem invItem : inventory.getItems()) {
                if (invItem.getItem().getItemName().equals(ingredient) && invItem.getQuantity() >= 1) {
                    itemsToConsume.add(invItem); // siapkan untuk dikonsumsi
                    found = true;
                    break;
                }
            }
            if (!found) {
                hasAllIngredients = false;
                break;
            }
        }

        // Cek bahan bakar (Firewood / Coal)
        InventoryItem fuel = null;
        for (InventoryItem invItem : inventory.getItems()) {
            String name = invItem.getItem().getItemName();
            if ((name.equals("Firewood") || name.equals("Coal")) && invItem.getQuantity() >= 1) {
                fuel = invItem;
                break;
            }
        }


        if (!hasAllIngredients || fuel == null) {
            System.out.println("Gagal memasak. Bahan atau bahan bakar tidak cukup.");
            return;
        }

        // Konsumsi bahan
        for (InventoryItem item : itemsToConsume) {
            item.setQuantity(item.getQuantity() - 1);
            if (item.getQuantity() <= 0) {
                inventory.removeItem(item);
            }
        }

        // Konsumsi bahan bakar
        fuel.setQuantity(fuel.getQuantity() - 1); // atau -0.5 kalau kamu ingin Coal bisa masak 2x
        if (fuel.getQuantity() <= 0) {
            inventory.removeItem(fuel);
        }

        // Tambahkan makanan hasil masakan
        boolean alreadyExists = false;
        for (InventoryItem invItem : inventory.getItems()) {
            if (invItem.getItem().getItemName().equals(food.getItemName())) {
                invItem.setQuantity(invItem.getQuantity() + 1);
                alreadyExists = true;
                break;
            }
        }
        if (!alreadyExists) {
            inventory.addItem(new InventoryItem(food, 1));
        }

        // Kurangi energi
        setEnergy(getEnergy() - 10);

        System.out.println("Berhasil memasak " + food.getItemName() + " dan menambahkannya ke inventory.");
    }



    public void fish(){
        if(this.getItemHeld().getItemName() == "Fishing Rod"){
            setEnergy(getEnergy() - 5);
        }
    }

    public void propose(NPC npc){
        if(npc.getHeartPoints() == 150 && this.getItemHeld().getItemName() == "Proposal Ring"){
            setEnergy(getEnergy() - 10);
        }
    }

    public void marry(){
        if(this.getItemHeld().getItemName() == "Proposal Ring"){
            setEnergy(getEnergy() - 80);
        }
    }

    public void watch(){
        setEnergy(getEnergy() - 5);
    }

    public void visit(){
        setEnergy(getEnergy() - 10);
    }

    public void chat(NPC npc){
        npc.setHeartPoints(npc.getHeartPoints() + 10);
        setEnergy(getEnergy() - 10);
    }

    public void gift(Items goods, NPC npc){
        npc.receiveGift(goods);
        setEnergy(getEnergy() - 5);
    }

    public void move(Coordinate coordinate){
        this.setCoordinate(coordinate);
    }

    public void openInventory() {
        System.out.println("Isi Inventory:");
        for (InventoryItem inventoryItem : inventory.getItems()) {
            Items item = inventoryItem.getItem();
            System.out.println("- " + item.getItemName() +
                            " | Qty: " + inventoryItem.getQuantity() +
                            " | Buy Price: " + item.getBuyPrice() +
                            " | Sell Price: " + item.getSellPrice());
        }
    }

    public void showTime(){}

    public void showLocation(){}

    public void sell(Items item){}

    public void gift() {
        if (itemHeld == null) {
            System.out.println("Kamu tidak memegang item apa pun.");
            return;
        }

        for (NPC npc : npcRelationshipStats) {
            npc.receiveGift(itemHeld);
            System.out.println("Kamu memberi hadiah kepada " + npc.getName());
            break;
        }

        setEnergy(getEnergy() - 5);
    }

    // private FarmTile getTileInFront() {
    // int frontCol = tileCol;
    // int frontRow = tileRow;

    // switch (direction) {
    //     case "up": frontRow--; break;
    //     case "down": frontRow++; break;
    //     case "left": frontCol--; break;
    //     case "right": frontCol++; break;
    // }

    // // Cegah index -1 atau melebihi batas
    // if (gp.farm.isValidTile(frontCol, frontRow)) {
    //     return gp.farm.getTileAt(frontCol, frontRow);
    // } else {
    //     return null; // tile tidak valid
    // }
    public boolean isCoalPartiallyUsed() {
        return coalPartiallyUsed;
    }
}
