public class Seeds extends Items{
    
    private Season seasonGrow;
    private int daysToHarvest;
    
    public Seeds(String itemName, int buyPrice, int sellPrice,Season seasonGrow, int daysToHarvest){
        super(itemName, buyPrice, sellPrice);
        this.seasonGrow = seasonGrow;
        this.daysToHarvest = daysToHarvest;

    }

    public Season getSeasonGrow() {
        return seasonGrow;
    }

    public int getDaysToHarvest() {
        return daysToHarvest;
    }



}
