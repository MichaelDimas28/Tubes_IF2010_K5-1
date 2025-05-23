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
    public int worldX, worldY;
    public int speed;
    public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
    public String direction;
    public int spriteCounter = 0;
    public int spriteNum = 1;
    public Rectangle solidArea; //hitbox karakter player
    public boolean collisionOn = false;

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

        setDefaultValues();
        getPlayerImage();
    }

    public void setDefaultValues() {
        worldX = gp.tileSize*3;
        worldY = gp.tileSize*3;
        speed = 4;
        direction = "down";
    }

    public void getPlayerImage() {
        try {
            up1 = ImageIO.read(getClass().getResourceAsStream("/player/char_up_1.png"));
            up2 = ImageIO.read(getClass().getResourceAsStream("/player/char_up_2.png"));
            down1 = ImageIO.read(getClass().getResourceAsStream("/player/char_down_1.png"));
            down2 = ImageIO.read(getClass().getResourceAsStream("/player/char_down_2.png"));
            left1 = ImageIO.read(getClass().getResourceAsStream("/player/char_left_1.png"));
            left2 = ImageIO.read(getClass().getResourceAsStream("/player/char_left_2.png"));
            right1 = ImageIO.read(getClass().getResourceAsStream("/player/char_right_1.png"));
            right2 = ImageIO.read(getClass().getResourceAsStream("/player/char_right_2.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update() {
        if (!moving) {
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

            }
            else {
                standCounter++;
                if (standCounter == 20) {
                    spriteNum = 1;
                    standCounter = 0;
                }
            }
        }
        if (moving) {
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
                if (spriteNum == 1) {
                    spriteNum = 2;
                } else if (spriteNum == 2) {
                    spriteNum = 1;
                }
                spriteCounter = 0;
            }
        }
        pixelCounter += speed;
        if (pixelCounter == 48) {
            moving = false;
            pixelCounter = 0;
        }
    }

    public void draw(Graphics2D g2) {
        // g2.setColor(Color.white);
        // g2.fillRect(x, y, gp.tileSize, gp.tileSize);

        BufferedImage image = null;

        switch(direction) {
        case "up":
            if (spriteNum == 1) {
                image = up1;
            }
            if (spriteNum == 2) {
                image = up2;
            }
            break;
        case "down":
            if (spriteNum == 1) {
                image = down1;
            }
            if (spriteNum == 2) {
                image = down2;
            }
            break;
        case "left":
            if (spriteNum == 1) {
                image = left1;
            }
            if (spriteNum == 2) {
                image = left2;
            }
            break;
        case "right":
            if (spriteNum == 1) {
                image = right1;
            }
            if (spriteNum == 2) {
                image = right2;
            }
            break;
        }

        g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
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