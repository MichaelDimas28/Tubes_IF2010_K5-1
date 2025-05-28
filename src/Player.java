import java.util.List;

import javax.imageio.ImageIO;

// import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
// import java.util.HashMap;

public class Player implements Action {
    public UI ui;
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


    public Equipment wateringCan = new Equipment("Watering Can", 0, 0);
    public Equipment hoe = new Equipment("Hoe", 0, 0);
    public Equipment fishingRod = new Equipment("Fishing Rod", 0, 0);


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
    private Map currentMap = null;
    private int total_income = 0;
    private int total_expenditure = 0;
    private float avarage_season_income = 0;
    private float avarage_season_expenditure = 0;
    private int total_days_played = 0;
    private int crops_harvested = 0;
    private int fish_caught = 0;
    private Coordinate coordinate = new Coordinate(0,0);
    private Inventory inventory = new Inventory();
    private List<NPC> npcRelationshipStats = new ArrayList<>();
   
    //Constructor
    public Player(String name, Gender gender, int energy, int gold, GamePanel gp, KeyHandler keyH){
        this.name = name;
        this.gender = gender;
        this.energy = energy;
        this. gold = gold;
        this.gp = gp;
        this.keyH = keyH;

        screenX = gp.screenWidth/2 - (gp.tileSize/2);
        screenY = gp.screenHeight/2 - (gp.tileSize/2);

        solidArea = new Rectangle();
        solidArea.x = 1;
        solidArea.y = 1;
        solidArea.width = 46;
        solidArea.height = 46;

        setDefaultValues(gp.tileSize*14, gp.tileSize*14, "down");
        getPlayerImage();
        getPlayerHoeImage();
        getPlayerWaterImage();
        getPlayerFishingImage();
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

    public void update() {
        if (tilling) {
            tilling();
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
        }
        if (!canFish) {
            fishCooldownCounter++;
            if (fishCooldownCounter > fishCooldownMax) {
                canFish = true;
                fishCooldownCounter = 0;
            }
        }

        if (!moving && !tilling && !watering && !fishing) {
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

            } else if (itemHeld != null && keyH.enterPressed && canTill && itemHeld.getItemName().equals("Hoe") && !watering && !fishing) {
                tilling = true;
                canTill = false;
                keyH.enterPressed = false;
            } else if (itemHeld != null && keyH.enterPressed && canWater && itemHeld.getItemName().equals("Watering Can") && !tilling && !fishing) {
                watering = true;
                canWater = false;
                keyH.enterPressed = false;
            } else if (itemHeld != null && keyH.enterPressed && canFish && itemHeld.getItemName().equals("Fishing Rod") && !tilling && !watering) {
                fishing = true;
                fishingPhase = 1; // Mulai animasi dari rod_1 dulu
                canFish = false;
                keyH.enterPressed = false;
            }
            else {
                standCounter++;
                if (standCounter == 20) {
                    spriteNum = 1;
                    standCounter = 0;
                }
            }
        }
        if (moving && !tilling && !watering && !fishing) {
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

        g2.drawImage(image, tempScreenX, tempScreenY, null);
        if (itemHeld!=null && (itemHeld.getItemName().equals("Hoe") || itemHeld.getItemName().equals("Fishing Rod") || itemHeld.getItemName().equals("Watering Can"))) {
            g2.drawImage(guidebox, guideboxX, guideboxY, gp.tileSize, gp.tileSize, null);
        }

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

    //Current Map
    public Map getCurrentMap(){
        return currentMap;
    }

    public void setCurrentMap(Map map){
        currentMap = map;
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

    public void cook(Food food) {
        if (!food.getRecipeAquiredStatus()) {
            System.out.println("Resep belum diperoleh.");
            return;
        }

        String[] ingredients = food.getIngredients();
        List<InventoryItem> itemsToConsume = new ArrayList<>();
        boolean hasAllIngredients = true;

        // Cek apakah semua bahan tersedia
        for (String ingredient : ingredients) {
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
}