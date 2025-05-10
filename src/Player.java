import java.util.List;

public class Player implements Action{
    private String name;
    private Gender gender;
    private int energy;
    private int gold;
    private Items itemHeld;
    private Map currentMap;
    private int total_income;
    private int total_expenditure;
    private float avarage_season_income;
    private float avarage_season_expenditure;
    private int total_days_played;
    private int crops_harvested;
    private int fish_caught;
    private Coordinate coordinate;
    private Inventory inventory;
    private List<NPC> npcRelationshipStats = NPC.getListOfNPC();

    energy = 100;
    itemHeld = null;
    currentMap = null;
    total_income = 0;
    total_expenditure = 0;
    avarage_season_income = 0;
    avarage_season_expenditure = 0;
    total_days_played = 0;
    crops_harvested = 0;
    fish_caught = 0;
    coordinate = new Coordinate(0,0);
    inventory = new Inventory();

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
    public int getAvgSeasonIncome(){
        return avarage_season_income;
    }

    public void setAvgSeasonIncome(int income){
        avarage_season_income = income;
    }

    //Avarage Season Expenditure
    public int getAvgSeasonExpenditure(){
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
        this.coordinate.column = this.coordinate.setColumn(coordinate.getColumn);
        this.coordinate.row = this.coordinate.setRow(coordinate.getRow);
    }

    //Inventory
    public Inventory getInventory(){
        return inventory;
    }
    
    //NPC
    public List<NPC> getRelationshipStatus(){
        return relationshipStatus;
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

    public void plant(Seed seed){
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

    public void eat(Items food){
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

    public void cook(Items recipe){
        setEnergy(getEnergy() - 10);
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

    public void gift(Items goods){
        npc.receiveGift(goods);
        setEnergy(getEnergy() - 5);
    }

    public void move(Coordinate coordinate){
        this.setCoordinate(coordinate);
    }

    public void openInventory(){
        for(Items item : this.getInventory()){
            System.out.println(item.getItemName);
            System.out.println("Buy Price: " + item.getBuyPrice());
            System.out.println("Sell Price: " + item.getSellPrice());
        }
    }

    public void showTime(){}

    public void showLocation(){}

    public void sell(Items item){}
}