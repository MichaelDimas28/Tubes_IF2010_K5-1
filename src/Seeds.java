public class Seeds extends Items{
    
    private Season seasonGrow;
    private int daysToHarvest;
    
    public Seeds(String itemName, int buyPrice,Season seasonGrow, int daysToHarvest, String imageName){
        super(itemName, buyPrice, imageName);
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
