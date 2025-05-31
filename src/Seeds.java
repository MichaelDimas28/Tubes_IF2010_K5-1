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

    public Crops getResultItem(ItemManager itemManager) {
        // Hilangkan spasi dan kata "Seeds"
        String resultName = getItemName().replace("Seeds", "").replace("(", "").replace(")", "").trim();
        resultName = resultName.replaceAll("\\s+", " ").trim();

        Items result = itemManager.getItem(resultName);

        if (result instanceof Crops) {
            return (Crops) result;
        } else {
            return null;
        }
    }

}
