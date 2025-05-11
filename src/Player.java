import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

public class Player implements Action{
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
    public Player(String name, Gender gender, int energy, int gold){
        this.name = name;
        this.gender = gender;
        this.energy = energy;
        this. gold = gold;
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